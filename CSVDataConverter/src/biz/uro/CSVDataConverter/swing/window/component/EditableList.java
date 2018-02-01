package biz.uro.CSVDataConverter.swing.window.component;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import biz.uro.CSVDataConverter.swing.IDataModel;
import biz.uro.CSVDataConverter.swing.constants.Constants;
import biz.uro.CSVDataConverter.swing.json.IJSONDataModel;
import biz.uro.CSVDataConverter.swing.old.DataListModel;
import biz.uro.CSVDataConverter.swing.old.JSONListModel;
import biz.uro.CSVDataConverter.swing.old.DataListModel.IOnListChangedListener;
import biz.uro.CSVDataConverter.swing.window.PropertyWindow;
import biz.uro.CSVDataConverter.swing.window.PropertyWindow.IItemBuildListener;

public class EditableList<T extends IJSONDataModel> extends JPanel implements IOnListChangedListener<T> {

	private static final long serialVersionUID = 1L;

	public interface IOnItemSelectedListener<T extends IDataModel> {
		void onItemSelected( T target );
	}
	
	private final JButton mListControlButton_Up;
	private final JButton mListControlButton_Down;
	private final JButton mListControlButton_Add;
	private final PropertyWindow<T> mPropertyWindow;
	private final JList<T> mListList;
	private JSONListModel<T> mModel;

	private final ArrayList<IOnItemSelectedListener<T>> mItemSelectedListener = new ArrayList<>();
	private JLabel mPropertyComment;

	public void addItemSelectedListener( IOnItemSelectedListener<T> listener ) {
		mItemSelectedListener.add( listener );
	}
	
	private void onItemSelected( T target ) {
		for ( IOnItemSelectedListener<T> listener : mItemSelectedListener ) {
			listener.onItemSelected( target );	
		}
	}
	
	public EditableList( PropertyWindow<T> propertyWindow, String comment ) {
		mPropertyWindow = propertyWindow;
	
		setLayout(new BoxLayout( this, BoxLayout.Y_AXIS ));
		mListList = new JList<>();
		mListList.addListSelectionListener( new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				final int index = mListList.getSelectedIndex();
				updateControlButton();
				if ( index == -1 ) {
					return;
				}
				final T target = mModel.get( index );
				onItemSelected( target );
			}
		});
		
		final JPopupMenu TableListPopupMenu = new JPopupMenu();
		
		JMenuItem TableDeleteMenuItem = new JMenuItem("削除");
		TableDeleteMenuItem.addActionListener( new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				final int index = mListList.getSelectedIndex();
				if ( index == -1 ) {
					return;
				}
				mModel.remove( index );
			}
		});
		TableListPopupMenu.add(TableDeleteMenuItem);

		mListList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked( MouseEvent e ) {
				final int index = mListList.locationToIndex( e.getPoint() );
				mListList.setSelectedIndex( index );
				if ( index == -1 ) {
					return;
				}
				final T target = mModel.get( index );
				if ( !target.isEditable() ) {
					return;
				}
				if ( SwingUtilities.isRightMouseButton(e) ) {
					TableListPopupMenu.show( mListList, e.getX() + 20, e.getY() + 4 );
					return;
				}
				if ( e.getClickCount() != 2 ) {
					return;
				}
				mPropertyWindow.editData( target );
			}
		});

		JPanel TableListControlPane = new JPanel();
		TableListControlPane.setMaximumSize( new Dimension( Short.MAX_VALUE, Constants.LABEL_HEIGHT ) );
		add(TableListControlPane);
		TableListControlPane.setLayout(new BoxLayout(TableListControlPane, BoxLayout.X_AXIS));
		
		JLabel TableListControlLabel = new JLabel(mPropertyWindow.getPropertyName() + "一覧");
		TableListControlLabel.setFont(new Font("MS UI Gothic", Font.PLAIN, 12));
		TableListControlLabel.setHorizontalAlignment(SwingConstants.LEFT);
		TableListControlPane.add(TableListControlLabel);

		JPanel TableListControlButtonPane = new JPanel();
		FlowLayout fl_SchemaListControlButtonPane = (FlowLayout) TableListControlButtonPane.getLayout();
		fl_SchemaListControlButtonPane.setAlignment(FlowLayout.RIGHT);
		TableListControlPane.add(TableListControlButtonPane);
		
		mListControlButton_Add = new JButton("＋");
		mListControlButton_Add.addActionListener(new ActionListener() {
			public void actionPerformed( ActionEvent e ) {
				mPropertyWindow.newData( new IItemBuildListener<T>() {
					@Override
					public void onBuild( T newItem ) {
						mModel.addElement( newItem );
					}
				});
			}
		});
		TableListControlButtonPane.add(mListControlButton_Add);
		mListControlButton_Add.setFont(new Font("MS UI Gothic", Font.PLAIN, 12));
		
		mListControlButton_Up = new JButton("▲");
		mListControlButton_Up.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			 	mListList.setSelectedIndex( moveElementToUp( mListList.getSelectedIndex() ) );
			}
		});
		TableListControlButtonPane.add(mListControlButton_Up);
		mListControlButton_Up.setFont(new Font("MS UI Gothic", Font.PLAIN, 12));
		
		mListControlButton_Down = new JButton("▼");
		mListControlButton_Down.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mListList.setSelectedIndex( moveElementToDown( mListList.getSelectedIndex() ) );
			}
		});
		TableListControlButtonPane.add( mListControlButton_Down );
		
		JPanel panel = new JPanel();
		panel.setMaximumSize( new Dimension( Short.MAX_VALUE, Constants.LABEL_HEIGHT ) );
		add(panel);
		panel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 5));
		
		mPropertyComment = new JLabel( comment );
		panel.add(mPropertyComment);
	
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView( mListList );
		add( scrollPane );
	
		onUpdateTableList();
	}
	
	private void onUpdateTableList() {
		if ( mModel == null ) {
			mListControlButton_Add.setEnabled( false );
			mListControlButton_Up.setEnabled( false );
			mListControlButton_Down.setEnabled( false );
			return;
		}
		mListControlButton_Add.setEnabled( true );
		updateControlButton();		
	}
	
	@Override
	public void onListChanged( DataListModel<T> model ) {
		onUpdateTableList();
	}
	
	private void updateControlButton() {
		final int index = mListList.getSelectedIndex();
		mListControlButton_Up.setEnabled( canElementMoveToUp( index ) );
		mListControlButton_Down.setEnabled( canElementMoveToDown( index ) );
	}
	
	private boolean canElementMoveToUp( int index ) {
		return canElementMove( index, index - 1 );
	}
	
	private boolean canElementMoveToDown( int index ) {
		return canElementMove( index, index + 1 );		
	}
	
	private boolean canElementMove( int from, int to ) {
		if ( from == -1 ) {
			return false;
		}
		if ( to < 0 || to >= mModel.size() ) {
			return false;
		}
		if ( !mModel.get( to ).isEditable() ) {
			return false;
		}
		if ( !mModel.get( from ).isEditable() ) {
			return false;
		}
		return true;
	}
	
	private int moveElementToUp( int index ) {
		return moveElement( index, index - 1 );
	}
	
	private int moveElementToDown( int index ) {
		return moveElement( index, index + 1 );
	}

	private int moveElement( int from, int to ) {
		if ( !canElementMove( from, to ) ) {
			return from;
		}
		final T element = mModel.get( from );
		mModel.removeWithoutEvents( from );
		mModel.add( to, element );
		return to;
	}

	public void setDataModel( JSONListModel<T> model ) {
		mModel = model;
		mListList.setModel( mModel );
		onUpdateTableList();
	}
	
	public JSONListModel<T> getDataModel() {
		return mModel;
	}
	
}
