package biz.uro.CSVDataConverter.swing.old;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import biz.uro.CSVDataConverter.swing.IDataModel;

public class DataListModel<T extends IDataModel> implements ListModel<T>, ComboBoxModel<T> {

	public interface IOnListChangedListener<T extends IDataModel> {
		void onListChanged( DataListModel<T> model );
	}
	
	protected final ArrayList<T> mConstants = new ArrayList<>();
	protected final DefaultListModel<T> mListModel = new DefaultListModel<>();
	private final DefaultComboBoxModel<T> mComboBoxModel = new DefaultComboBoxModel<>();
	private final ArrayList<IOnListChangedListener<T>> mListChangedListener = new ArrayList<>();
	
	public DataListModel() {
		mListModel.addListDataListener( new ListDataListener() {
			@Override
			public void intervalRemoved(ListDataEvent e) {
			}
			
			@Override
			public void intervalAdded(ListDataEvent e) {
			}
			
			@Override
			public void contentsChanged(ListDataEvent e) {
				onListChanged();
			}
		});
		addListChangedListener( new IOnListChangedListener<T>() {
			@Override
			public void onListChanged( DataListModel<T> model ) {
				@SuppressWarnings("unchecked")
				final T selectedItem = (T) mComboBoxModel.getSelectedItem();
				mComboBoxModel.removeAllElements();
				final int itemCount = model.getSize();
				for ( int i = 0; i < itemCount; i++ ) {
					mComboBoxModel.addElement( model.get( i ) );
				}
				mComboBoxModel.setSelectedItem( selectedItem );
			}
		});
	}

	public void addListChangedListener( IOnListChangedListener<T> listener ) {
		mListChangedListener.add( listener );
		listener.onListChanged( this );
	}
	
	private void onListChanged() {
		for ( IOnListChangedListener<T> listener : mListChangedListener ) {
			listener.onListChanged( this );	
		}
	}
	
	public void addConstants( T element ) {
		mConstants.add( element );
		addElement( element );
	}

	/* ========================================================== 
	 * delegate
	 * ========================================================== */
		
	public int size() {
		return mListModel.getSize();
	}
	
	public T get( int index ) {
		return mListModel.get( index );
	}
	
	public void addWithoutEvents( int index, T element ) {
		mListModel.add( index, element );
	}

	public void add( int index, T element ) {
		addWithoutEvents( index, element );
		onListChanged();
	}
	
	public void removeWithoutEvents( int index ) {
		mListModel.remove( index );
	}
	
	public void remove( int index ) {
		removeWithoutEvents( index );
		onListChanged();
	}

	public void addElementWithoutEvents( T element ) {
		mListModel.addElement( element );
	}

	public void addElement( T element ) {
		addElementWithoutEvents( element );
		onListChanged();
	}

	public Object[] toArray() {
		return mListModel.toArray();
	}
	
	public T[] toArray( T[] array ) {
		for ( int i = 0; i < array.length; i++ ) {
			array[i] = mListModel.get( i );
		}
		return array;
	}
	
	public List<T> asList( T[] array ) {
		return Arrays.asList( toArray( array ) );
	}
	
	/* ========================================================== 
	 * override
	 * ========================================================== */

	@Override
	public int getSize() {
		return mListModel.getSize();
	}

	@Override
	public T getElementAt( int index ) {
		return mListModel.getElementAt( index );
	}

	@Override
	public void addListDataListener( ListDataListener l ) {
		mListModel.addListDataListener( l );
	}

	@Override
	public void removeListDataListener( ListDataListener l ) {
		mListModel.removeListDataListener( l );
	}

	@Override
	public void setSelectedItem( Object anItem ) {
		mComboBoxModel.setSelectedItem( anItem );
	}

	@Override
	public Object getSelectedItem() {
		return mComboBoxModel.getSelectedItem();
	}

}
