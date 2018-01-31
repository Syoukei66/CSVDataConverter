package biz.uro.CSVDataConverter.swing.window.tab.elementType;

import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JTextField;
import javax.swing.JLabel;

import biz.uro.CSVDataConverter.swing.constants.Constants;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ElementTypePropertyPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	protected final JTextField mPackageName;
	protected final JTextField mClassName;
	
	/**
	 * Create the panel.
	 */
	public ElementTypePropertyPanel( ElementTypePropertyWindow window ) {
		
		JPanel panel_4 = new JPanel();
		
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS ) );
		
		JLabel label = new JLabel( "パッケージ　" );
		panel.add( label );
		
		mPackageName = new JTextField();
		mPackageName.addKeyListener( new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				window.onUpdateData();
			}
		} );
		mPackageName.setMaximumSize( new Dimension( Short.MAX_VALUE, Constants.LABEL_HEIGHT ) );
		label.setLabelFor( mPackageName );
		
		panel.add( mPackageName );
				
		JPanel panel_1 = new JPanel();
		panel_1.setLayout( new BoxLayout( panel_1, BoxLayout.X_AXIS ) );
		
		JLabel label_1 = new JLabel( "クラス名　　" );
		panel_1.add( label_1 );
		
		mClassName = new JTextField();
		mClassName.addKeyListener( new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				window.onUpdateData();
			}
		} );
		mClassName.setMaximumSize( new Dimension( Short.MAX_VALUE, Constants.LABEL_HEIGHT ) );
		label_1.setLabelFor( mClassName );
		panel_1.add( mClassName );
		
		setLayout( new BoxLayout( this, BoxLayout.Y_AXIS ) );
		add( panel_4 );
		panel_4.setLayout( new BoxLayout( panel_4, BoxLayout.Y_AXIS ) );
		panel_4.add( panel );
		panel_4.add( panel_1 );
	}
	
}
