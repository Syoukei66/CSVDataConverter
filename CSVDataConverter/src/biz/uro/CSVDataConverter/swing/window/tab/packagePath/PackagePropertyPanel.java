package biz.uro.CSVDataConverter.swing.window.tab.packagePath;

import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;

import biz.uro.CSVDataConverter.swing.constants.Constants;

import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

@SuppressWarnings("serial")
public class PackagePropertyPanel extends JPanel {

	protected JTextField mPackagePath;
	protected JTextField mClassName;

	/**
	 * Create the panel.
	 */
	public PackagePropertyPanel( PackagePropertyWindow window ) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JPanel panel = new JPanel();
		add(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		JLabel lblNewLabel = new JLabel("パッケージ　");
		panel.add(lblNewLabel);
		
		mPackagePath = new JTextField();
		mPackagePath.setMaximumSize( new Dimension( Short.MAX_VALUE, Constants.LABEL_HEIGHT ) );
		mPackagePath.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				window.onUpdateData();
			}
		});
		panel.add(mPackagePath);
		mPackagePath.setColumns(10);
		
		JPanel panel_1 = new JPanel();
		add(panel_1);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.X_AXIS));
		
		JLabel lblNewLabel_1 = new JLabel("クラス名　　");
		panel_1.add(lblNewLabel_1);
		
		mClassName = new JTextField();
		mClassName.setMaximumSize( new Dimension( Short.MAX_VALUE, Constants.LABEL_HEIGHT ) );
		mClassName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				window.onUpdateData();
			}
		});
		panel_1.add(mClassName);
		mClassName.setColumns(10);

	}

}
