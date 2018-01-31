package biz.uro.CSVDataConverter.swing.window.tab.factory.method;

import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import biz.uro.CSVDataConverter.swing.builder.ElementBuilder;
import biz.uro.CSVDataConverter.swing.constants.Constants;
import biz.uro.CSVDataConverter.swing.window.component.EditableList;
import biz.uro.CSVDataConverter.swing.window.component.EditableListFactory;

@SuppressWarnings("serial")
public class FactoryMethodPropertyPanel extends JPanel {

	protected JTextField mIdentifier;
	protected JTextField mComment;
	protected final EditableList<ElementBuilder> mElementList;
	
	public FactoryMethodPropertyPanel( FactoryMethodPropertyWindow window ) {
		setLayout( new BoxLayout( this, BoxLayout.Y_AXIS ) );
		
		JPanel panel = new JPanel();
		add( panel );
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		JPanel panel_3 = new JPanel();
		panel_3.setMaximumSize( new Dimension( Short.MAX_VALUE, Constants.LABEL_HEIGHT ) );
		panel.add(panel_3);
		panel_3.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		JLabel lblNewLabel = new JLabel("識別子");
		panel_3.add(lblNewLabel);
		
		mIdentifier = new JTextField();
		mIdentifier.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				window.onUpdateData();
			}
		});
		panel.add(mIdentifier);
		mIdentifier.setColumns(10);
		mIdentifier.setMaximumSize( new Dimension( Short.MAX_VALUE, Constants.LABEL_HEIGHT ) );
		
		JPanel panel_2 = new JPanel();
		add(panel_2);
		panel_2.setLayout(new BoxLayout(panel_2, BoxLayout.Y_AXIS));
		
		
		JPanel panel_1 = new JPanel();
		add( panel_1 );
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.Y_AXIS));
		
		JPanel panel_6 = new JPanel();
		add(panel_6);
		panel_6.setLayout(new BoxLayout(panel_6, BoxLayout.Y_AXIS));
		
		JPanel panel_7 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_7.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		panel_6.add(panel_7);
		
		JLabel lblNewLabel_3 = new JLabel("Excel上でのコメント");
		panel_7.add(lblNewLabel_3);
		
		mComment = new JTextField();
		panel_6.add(mComment);
		mComment.setColumns(10);
		
		mElementList = EditableListFactory.INSTANCE.createElementBuilderEditableList();
		add( mElementList );
	}

}
