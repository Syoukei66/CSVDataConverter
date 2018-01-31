package biz.uro.CSVDataConverter.swing.old;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import biz.uro.CSVDataConverter.swing.IDataModel;
import biz.uro.CSVDataConverter.swing.constants.Constants;

@SuppressWarnings("serial")
public abstract class PropertyWindow<T extends IDataModel> extends JDialog {
	
	private interface IPropertySettingCallBack<T> {
		public void callBack();
	}
	
	public interface IItemBuildListener<T> {
		public void onBuild( T newItem );
	}
	
	protected abstract boolean validation();
	protected abstract void init();
	protected abstract void setTarget( T target );
	protected abstract void edit( T target );
	protected abstract T build();
	
	private String mPropertyName;
	protected final JPanel mContentPane;
	private final Component mParent;
	private final JButton mFinishButton;
	private IPropertySettingCallBack<T> mCallBack;
	private boolean mIsShowed;
	
	public PropertyWindow( String name, Component parent ) {
		mPropertyName = name;
		mParent = parent;
		setModalityType(ModalityType.APPLICATION_MODAL);
		setModal(true);
		setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		setTitle(name + "のプロパティ");
		setResizable(false);
		
		mContentPane = new JPanel();
		mContentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(mContentPane);
		mContentPane.setLayout(new BoxLayout(mContentPane, BoxLayout.Y_AXIS));
		
		JPanel ButtonPanel = new JPanel();
		ButtonPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		ButtonPanel.setMaximumSize( new Dimension( Short.MAX_VALUE, Constants.PROPERTY_WINDOW_BUTTON_HEIGHT ) );
		mContentPane.add(ButtonPanel);
		ButtonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
		
		mFinishButton = new JButton("完了");
		mFinishButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				delegateFinish();
			}
		});
		mFinishButton.setHorizontalAlignment(SwingConstants.RIGHT);
		ButtonPanel.add(mFinishButton);
		
		getRootPane().setDefaultButton( mFinishButton );
		mFinishButton.setEnabled( false );
	}
	
	public void delegateFinish() {
		mCallBack.callBack();
		setVisible( false );
	}
	
	public void editData( T target ) {
		setTarget( target );
		onUpdateData();
		delegate( new IPropertySettingCallBack<T>() {
			@Override
			public void callBack() {
				edit( target );
			}
		} );
	}
	
	public void newData( IItemBuildListener<T> listener ) {
		init();
		onUpdateData();
		delegate( new IPropertySettingCallBack<T>() {
			@Override
			public void callBack() {
				listener.onBuild( build() );
			}
		} );
	}
	
	private void delegate( IPropertySettingCallBack<T> callback ) {
		if ( !mIsShowed ) {			
			setLocation( mParent.getLocation() );
			mIsShowed = true;
		}
		mCallBack = callback;
		setVisible( true );
	}

	public final void onUpdateData() {
		mFinishButton.setEnabled( validation() );
	}
	
	public String getPropertyName() {
		return mPropertyName;
	}
	
}
