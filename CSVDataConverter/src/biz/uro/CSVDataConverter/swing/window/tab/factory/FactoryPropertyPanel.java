package biz.uro.CSVDataConverter.swing.window.tab.factory;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import biz.uro.CSVDataConverter.swing.ProjectDataModel;
import biz.uro.CSVDataConverter.swing.ProjectDataModel.IProjectDataReloadListener;
import biz.uro.CSVDataConverter.swing.builder.ElementTypeBuilder;
import biz.uro.CSVDataConverter.swing.builder.FactoryMethodBuilder;
import biz.uro.CSVDataConverter.swing.builder.ProjectData;
import biz.uro.CSVDataConverter.swing.constants.Constants;
import biz.uro.CSVDataConverter.swing.window.component.EditableList;
import biz.uro.CSVDataConverter.swing.window.component.EditableListFactory;

@SuppressWarnings("serial")
public class FactoryPropertyPanel extends JPanel {
	
	protected final JTextField mPackageName;
	protected final JTextField mClassName;
	protected final JComboBox<ElementTypeBuilder> mTypeComboBox;
	protected final EditableList<FactoryMethodBuilder> mMethodList;
	
	/**
	 * Create the panel.
	 */
	public FactoryPropertyPanel( FactoryPropertyWindow window ) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JPanel panel = new JPanel();
		add(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		JLabel PackageNameLabel = new JLabel("パッケージ*　");
		panel.add(PackageNameLabel);
		
		mPackageName = new JTextField();
		panel.add(mPackageName);
		mPackageName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				window.onUpdateData();
			}
		});
		mPackageName.setColumns(10);
		mPackageName.setMaximumSize( new Dimension( Short.MAX_VALUE, Constants.LABEL_HEIGHT ) );
		
		JLabel TableClassNameLabel = new JLabel("クラス名*　　");
		
		mClassName = new JTextField();
		mClassName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				window.onUpdateData();
			}
		});
		JPanel EditorPanel = new JPanel();
		add(EditorPanel);
		
		mClassName.setColumns(10);
		mClassName.setMaximumSize( new Dimension( Short.MAX_VALUE, Constants.LABEL_HEIGHT ) );
		EditorPanel.setLayout(new BoxLayout(EditorPanel, BoxLayout.X_AXIS));
		EditorPanel.add(TableClassNameLabel);
		EditorPanel.add(mClassName);
		
		JPanel panel_1 = new JPanel();
		panel_1.setMaximumSize( new Dimension( Short.MAX_VALUE, Constants.LABEL_HEIGHT ) );
		add(panel_1);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.X_AXIS));
		
		JLabel lblNewLabel_1 = new JLabel("型　　");
		panel_1.add(lblNewLabel_1);
		
		mTypeComboBox = new JComboBox<>();
		mTypeComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				window.onUpdateData();
			}
		});
		panel_1.add( mTypeComboBox );

		mMethodList = EditableListFactory.INSTANCE.createFactoryMethodBuilderEditableList();
		add( mMethodList );
		
		ProjectDataModel.INSTANCE.addProjectReloadListener( new IProjectDataReloadListener() {
			@Override
			public void onReloadProject( ProjectData data ) {
				mTypeComboBox.setModel( data.getElementTypeModel() );
			}
		} );
	}
}
