package biz.uro.CSVDataConverter.swing.old;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import biz.uro.CSVDataConverter.swing.old.TransitionWindow.IStatePanel;

@SuppressWarnings("serial")
public abstract class StatePanel<T extends TransitionWindow> extends JPanel implements IStatePanel {

	protected final T mWindow;
	
	protected abstract void onShow();
	protected abstract void onHide();
	
	public StatePanel( T window ) {
		mWindow = window;
	}
	
	@Override
	public void showState() {
		mWindow.getContentPane().add( this, BorderLayout.CENTER );
		onShow();
	}
	
	@Override
	public void hideState() {
		mWindow.getContentPane().remove( this );
		onHide();
	}
	
}
