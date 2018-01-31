package biz.uro.CSVDataConverter.swing.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JSONObjectMap {

	public interface IJSONObjectLoadedListener {
		void onLoaded( Object obj );
	}
	
	public interface IJSONObjectReferedListener {
		void onRefered( Object source );
	}
	
	private final Map<String,Object> mStringMap = new HashMap<>();
	
	private final Map<String,ArrayList<IJSONObjectLoadedListener>> mLoadedListener = new HashMap<>();
	private final Map<String,ArrayList<IJSONObjectReferedListener>> mReferedListener = new HashMap<>();
	
	public void addLoadedListener( String hashCode, IJSONObjectLoadedListener listener ) {
		ArrayList<IJSONObjectLoadedListener> list = mLoadedListener.get( hashCode );
		if ( list == null ) {
			list = new ArrayList<>();
			mLoadedListener.put( hashCode, list );
		}
		list.add( listener );
		final Object obj = mStringMap.get( hashCode );
		if ( obj != null ) {
			listener.onLoaded( obj );			
		}
	}
	
	public void addReferedListener( String hashCode, IJSONObjectReferedListener listener ) {
		ArrayList<IJSONObjectReferedListener> list = mReferedListener.get( hashCode );
		if ( list == null ) {
			list = new ArrayList<>();
			mReferedListener.put( hashCode, list );
		}
		list.add( listener );
	}

	public void put( String hashCode, Object obj ) {
		if ( hashCode != null ) {
			mStringMap.put( hashCode, obj );
		}
		onObjectLoaded( hashCode, obj );
	}
	
	private void onObjectLoaded( String hashCode, Object obj ) {
		final ArrayList<IJSONObjectLoadedListener> list = mLoadedListener.get( hashCode );
		if ( list == null ) {
			return;
		}
		for ( IJSONObjectLoadedListener listener : list ) {
			listener.onLoaded( obj );
		}
	}
	
	protected void onRefered( String hashCode, Object source ) {
		final ArrayList<IJSONObjectReferedListener> list = mReferedListener.get( hashCode );
		if ( list == null ) {
			return;
		}
		for ( IJSONObjectReferedListener listener : list ) {
			listener.onRefered( source );
		}
	}

	public boolean containsKey( String hashCode ) {
		return mStringMap.containsKey( hashCode );
	}
	
	@SuppressWarnings("unchecked")
	public <T extends IJSONObject> T get( String hashCode ) {
		return (T)mStringMap.get( hashCode );
	}

	@SuppressWarnings("unchecked")
	public <T extends IJSONList> T getList( String hashCode ) {
		return (T)mStringMap.get( hashCode );
	}

}
