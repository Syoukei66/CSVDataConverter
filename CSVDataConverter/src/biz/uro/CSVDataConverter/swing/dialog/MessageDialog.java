package biz.uro.CSVDataConverter.swing.dialog;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.BoxLayout;

@SuppressWarnings("serial")
public class MessageDialog extends JDialog {

	public static abstract class DialogButton {
		private String mText;
		public DialogButton( String text ) {
			mText = text;
		}
		public abstract void onPush();
	}
	
	private final ArrayList<DialogButton> mButtonList = new ArrayList<>();
	private final JPanel contentPanel = new JPanel();
	private JPanel mButtonPane;

	/**
	 * Create the dialog.
	 */
	public MessageDialog( String message ) {
		setBounds(100, 100, 400, 146);
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		contentPanel.setBorder(new EmptyBorder(20, 5, 20, 5));
		getContentPane().add(contentPanel);
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.X_AXIS));
		{
			JLabel lblNewLabel = new JLabel( message );
			contentPanel.add(lblNewLabel);
		}
		{
			mButtonPane = new JPanel();
			mButtonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
			getContentPane().add(mButtonPane);
		}
	}
	
	public JButton addDefaultButton( DialogButton button ) {
		JButton btn = addButton( button );
		getRootPane().setDefaultButton( btn );
		return btn;
	}
	
	public JButton addButton( DialogButton button ) {
		mButtonList.add( button );
		JButton btn = new JButton( button.mText );
		btn.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent e ) {
				button.onPush();
				MessageDialog.this.setVisible( false );
			}
		});
		mButtonPane.add( btn );
		return btn;
	}
	
	public void run( JFrame target ) {
		setLocation( 
				(int)( target.getX() + target.getWidth() * 0.5 - getWidth() * 0.5 ),
				(int)( target.getY() + target.getHeight() * 0.5 - getHeight() * 0.5 )
				);
		setVisible( true );
	}

}
