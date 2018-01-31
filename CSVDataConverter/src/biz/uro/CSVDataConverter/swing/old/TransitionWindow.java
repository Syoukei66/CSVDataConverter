package biz.uro.CSVDataConverter.swing.old;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.LinkedList;

@SuppressWarnings("serial")
public abstract class TransitionWindow extends JDialog {

	public interface IStatePanel {
		void showState();
		void hideState();
		void action();
		IStatePanel next();
	}
	
	private final LinkedList<IStatePanel> mStateStack = new LinkedList<>();
	private IStatePanel mNowState;
	
	private final JButton mPrev;
	private final JButton mNext;
	private final JButton mAction;
	
	protected abstract IStatePanel initialPanel();
	
	/**
	 * Create the dialog.
	 */
	public TransitionWindow( String title, String actionName ) {
		setBounds(100, 100, 548, 339);
		setTitle( title );
		getContentPane().setLayout(new BorderLayout());
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				mPrev = new JButton( "戻る" );
				mPrev.addActionListener( new ActionListener() {
					public void actionPerformed( ActionEvent e) {
						returnState();
					}
				});
				mPrev.setActionCommand( "Prev" );
				buttonPane.add( mPrev );
				getRootPane().setDefaultButton( mPrev );
			}
			{
				mNext = new JButton( "次へ" );
				mNext.addActionListener( new ActionListener() {
					public void actionPerformed( ActionEvent e) {
						changeState( mNowState.next() );
					}
				});
				mNext.setActionCommand("Next");
				buttonPane.add(mNext);
			}
			{
				mAction = new JButton(actionName);
				mAction.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						mNowState.action();
						TransitionWindow.this.finish();
					}
				});
				buttonPane.add(mAction);
			}
		}
	}
	
	public void changeState( IStatePanel next ) {
		if ( mNowState != null ) {
			mStateStack.add( mNowState );			
		}
		mPrev.setEnabled( mStateStack.size() > 0 );
		setState( next );
	}
	
	public void returnState() {
		if ( mStateStack.size() == 0 ) {
			return;
		}
		setState( mStateStack.pop() );
		mPrev.setEnabled( mStateStack.size() > 0 );
	}
	
	private void setState( IStatePanel next ) {
		if ( mNowState != null ) {
			mNowState.hideState();			
		}
		mNext.setEnabled( false );
		mAction.setEnabled( false );
		if ( next != null ) {
			next.showState();			
		}
		mNowState = next;
		getContentPane().revalidate();
		getContentPane().repaint();
	}

	public void setPrevEnabled( boolean enabled ) {
		mPrev.setEnabled( enabled );
	}
	
	public void setNextEnabled( boolean enabled ) {
		mNext.setEnabled( enabled );
	}

	public void setActionEnabled( boolean enabled ) {
		mAction.setEnabled( enabled );		
	}
	
	public void run( JFrame target ) {
		setLocation( 
				(int)( target.getX() + target.getWidth() * 0.5 - getWidth() * 0.5 ),
				(int)( target.getY() + target.getHeight() * 0.5 - getHeight() * 0.5 )
				);
		mPrev.setEnabled( false );
		mNext.setEnabled( false );
		mAction.setEnabled( false );
		changeState( initialPanel() );
		setVisible( true );
	}
	
	public void finish() {
		mStateStack.clear();
		setState( null );
		setVisible( false );
	}
	
}
