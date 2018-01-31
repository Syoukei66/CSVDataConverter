package biz.uro.CSVDataConverter.swing.window.create;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import biz.uro.CSVDataConverter.swing.ProjectDataModel;
import biz.uro.CSVDataConverter.swing.constants.Constants;
import biz.uro.CSVDataConverter.swing.old.StatePanel;
import biz.uro.CSVDataConverter.swing.old.TransitionWindow.IStatePanel;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

@SuppressWarnings("serial")
public class GenerateProgramTopPanel extends StatePanel<GenerateProgramWindow> {

	private JTextField mOutputPath;

	private final JCheckBox mCheckBox_Date;
	
	private final GenerateProgramCSVFileSelectPanel mNextPanel;
	
	private boolean mUseCSV;
	
	public GenerateProgramTopPanel( GenerateProgramWindow window ) {
		super( window );
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_4.setMaximumSize( new Dimension( Short.MAX_VALUE, 50 ) );
		add(panel_4);
		panel_4.setLayout(new BoxLayout(panel_4, BoxLayout.Y_AXIS));
		
		JPanel panel = new JPanel();
		panel_4.add(panel);
		panel.setMaximumSize( new Dimension( Short.MAX_VALUE, Constants.LABEL_HEIGHT ) );
		panel.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		JLabel label = new JLabel("出力先のフォルダー");
		panel.add(label);
		
		mOutputPath = new JTextField();
		mOutputPath.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				onUpdateForm();
			}
		});
		mOutputPath.setMaximumSize( new Dimension( Short.MAX_VALUE, Constants.LABEL_HEIGHT ) );
		panel.add(mOutputPath);
		mOutputPath.setColumns(10);
		
		JButton btnNewButton = new JButton("…");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final JFileChooser fileChooser = mWindow.getCSVFolderChooser();
				final int selected = fileChooser.showOpenDialog( mWindow );
				if ( selected == JFileChooser.APPROVE_OPTION ) {
					mOutputPath.setText( fileChooser.getSelectedFile().getPath() );
					onUpdateForm();
				}
			}
		});
		panel.add(btnNewButton);
		
		JPanel panel_1 = new JPanel();
		panel_1.setMaximumSize( new Dimension( Short.MAX_VALUE, Constants.LABEL_HEIGHT ) );
		panel_4.add(panel_1);
		panel_1.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		mCheckBox_Date = new JCheckBox("保存日時でフォルダーを作成する");
		panel_1.add(mCheckBox_Date);
		
		JPanel panel_2 = new JPanel();
		panel_2.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(panel_2);
		panel_2.setLayout(new BoxLayout(panel_2, BoxLayout.PAGE_AXIS));

		mNextPanel = new GenerateProgramCSVFileSelectPanel( window );
	}
	
	public void initFormEnabled() {
		mWindow.setActionEnabled( false );
		mWindow.setNextEnabled( false );
	}

	public void onUpdateForm() {
		initFormEnabled();
		if ( mOutputPath.getText().length() == 0 ) {
			return;
		}
		if ( mUseCSV ) {
			mWindow.setNextEnabled( true );
		}
		mWindow.setNextEnabled( true );
	}
	
	@Override
	public void action() {
		GenerateProgramWindow.generateProgram( this, null, mOutputPath.getText(), mCheckBox_Date.isSelected() );
	}

	@Override
	public IStatePanel next() {
		mNextPanel.init( mOutputPath.getText(), mCheckBox_Date.isSelected() );
		return mNextPanel;
	}
	
	@Override
	protected void onShow() {
		mUseCSV = ProjectDataModel.INSTANCE.getProjectData().useCSV();
		onUpdateForm();
	}

	@Override
	protected void onHide() {
		
	}

}
