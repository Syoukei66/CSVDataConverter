package biz.uro.CSVDataConverter.swing.window.create;

import java.awt.Dimension;

import biz.uro.CSVDataConverter.swing.constants.Constants;
import biz.uro.CSVDataConverter.swing.old.StatePanel;
import biz.uro.CSVDataConverter.swing.old.TransitionWindow.IStatePanel;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class GenerateProgramCSVFileSelectPanel extends StatePanel<GenerateProgramWindow> {

	private String mOutputPath;
	private JTextField mInputPath;
	private boolean mOutputDate;
	
	public GenerateProgramCSVFileSelectPanel( GenerateProgramWindow window ) {
		super( window );
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JPanel panel = new JPanel();
		add(panel);
		panel.setMaximumSize( new Dimension( Short.MAX_VALUE, Constants.TEXT_BOX_HEIGHT ) );
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		JLabel lblNewLabel = new JLabel("CSVファイルのフォルダー");
		panel.add(lblNewLabel);
		
		mInputPath = new JTextField();
		panel.add(mInputPath);
		mInputPath.setColumns(10);
		
		JButton btnNewButton = new JButton("…");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final JFileChooser fileChooser = mWindow.getCSVFolderChooser();
				final int selected = fileChooser.showOpenDialog( mWindow );
				if ( selected == JFileChooser.APPROVE_OPTION ) {
					mInputPath.setText( fileChooser.getSelectedFile().getPath() );
				}
				onUpdateForm();
			}
		});
		panel.add(btnNewButton);
	}
	
	private void onUpdateForm() {
		mWindow.setActionEnabled( mInputPath.getText().length() > 0 );		
	}
	
	public void init( String outputPath, boolean outputDate ) {
		mOutputPath = outputPath;
		mOutputDate = outputDate;
	}

	@Override
	public void action() {
		GenerateProgramWindow.generateProgram( this, mInputPath.getText(), mOutputPath, mOutputDate);
	}

	@Override
	public IStatePanel next() {
		return null;
	}

	@Override
	protected void onShow() {
		onUpdateForm();
	}

	@Override
	protected void onHide() {
		
	}

}
