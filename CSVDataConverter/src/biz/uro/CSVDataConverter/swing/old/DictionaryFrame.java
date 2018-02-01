package biz.uro.CSVDataConverter.swing.old;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.BoxLayout;
import javax.swing.ButtonModel;
import javax.swing.DefaultButtonModel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import biz.uro.CSVDataConverter.swing.ProjectDataModel.IProjectDataReloadListener;
import biz.uro.CSVDataConverter.swing.json.IJSONDataModel;
import biz.uro.CSVDataConverter.swing.window.PropertyWindow;
import biz.uro.CSVDataConverter.swing.window.component.EditableList;

@SuppressWarnings("serial")
public abstract class DictionaryFrame<T1 extends IJSONDataModel, T2 extends PropertyWindow<T1>> extends JFrame implements IProjectDataReloadListener {

	protected JPanel contentPane;

	private final DefaultButtonModel mShowModel = new DefaultButtonModel();
	
	protected final EditableList<T1> mList;
	protected T2 mPropertyWindow;
	
	protected abstract T2 createPropertyWindow( String name );
	
	/**
	 * Create the frame.
	 */
	public DictionaryFrame( String name, String comment ) {
		setType(Type.UTILITY);
		addComponentListener( new ComponentAdapter() {
			@Override
			public void componentShown( ComponentEvent e ) {
				mShowModel.setSelected( true );				
			};
			@Override
			public void componentHidden( ComponentEvent e ) {
				mShowModel.setSelected( false );
			}
		} );
		setTitle( name + "一覧" );
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		mPropertyWindow = DictionaryFrame.this.createPropertyWindow( name );
		mList = new EditableList<T1>( mPropertyWindow, comment );
		mShowModel.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent e ) {
				final boolean selected = !mShowModel.isSelected();
				mShowModel.setSelected( selected );
				setVisible( selected );
			}
		});

		contentPane.add( mList );
		setContentPane( contentPane );
	}
	
	public ButtonModel getShowModel() {
		return mShowModel;
	}
	
}
