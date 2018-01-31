package biz.uro.CSVDataConverter.swing.json;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Consumer;

import biz.uro.CSVDataConverter.swing.json.JSONObjectMap.IJSONObjectLoadedListener;
import biz.uro.CSVDataConverter.swing.json.JSONObjectMap.IJSONObjectReferedListener;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONReader implements Iterable<JsonNode> {
	
	private final JSONObjectMap mMap;
	private final JsonNode mCurrent;
	private final Map<Integer,JSONReader> mIntegerChildMap = new HashMap<>();
	private final Map<String,JSONReader> mStringChildMap = new HashMap<>();
	private final HashSet<String> mReferedHashCodeSet = new HashSet<>();
	
	public JSONReader( ObjectMapper mapper, String content ) throws IOException {
		mMap = new JSONObjectMap();
		mCurrent = mapper.readTree( content );
	}
	
	public JSONReader( JsonNode node, JSONObjectMap map ) {
		mCurrent = node;
		mMap = map;
	}
	
	public void register( IJSONObject obj ) {
		mMap.put( obj.jsonHashCode(), obj );
	}
	
	public <T extends IJSONObject> T asObject( Class<T> clazz ) {
		final JSONObjectBuilder<T> builder = new JSONObjectBuilder<T>( this );
		return builder.addBase( clazz ).build();
	}
	
	public <T extends IJSONObject> JSONObjectBuilder<T> asAbstractObject( Class<T> baseClazz ) {
		return new JSONObjectBuilder<T>( this );
	}

	public <T extends IJSONList> T asList( T object ) {
		object.jsonParse( this );
		return object;
	}

	public <T extends IJSONObject> T pullObject() {
		if ( mCurrent == null ) {
			return null;
		}
		final String hashCode = mCurrent.asText();
		mReferedHashCodeSet.add( hashCode );
		return mMap.get( hashCode );
	}

	public <T extends IJSONObject> void reserveObject( IJSONObjectLoadedListener listener ) {
		if ( mCurrent == null ) {
			return;
		}
		final String hashCode = mCurrent.asText();
		mReferedHashCodeSet.add( hashCode );
		mMap.addLoadedListener( hashCode, listener );
	}

	public <T extends IJSONObject> void waitObject( IJSONObjectReferedListener listener ) {
		final String hashCode = mCurrent.get( JSONWriter.HASH_CODE ).asText();
		mMap.addReferedListener( hashCode, listener );
	}
	
	public JSONReader get( int arg0 ) {
		JSONReader ret = mIntegerChildMap.get( arg0 );
		if ( ret == null ) {
			if ( mCurrent == null ) {
				ret = new JSONReader( null, mMap );
			}
			else {				
				ret = new JSONReader( mCurrent.get( arg0 ), mMap );
			}
			mIntegerChildMap.put( arg0, ret );
		}
		return ret;
	}

	public JSONReader get( String fieldName ) {
		JSONReader ret = mStringChildMap.get( fieldName );
		if ( ret == null ) {
			if ( mCurrent == null ) {
				ret = new JSONReader( null, mMap );
			}
			else {				
				ret = new JSONReader( mCurrent.get( fieldName ), mMap );
			}
			mStringChildMap.put( fieldName, ret );
		}
		return ret;
	}

	public JsonNode getCurrent() {
		return mCurrent;
	}
	
	protected JSONObjectMap getJSONObjectMap() {
		return mMap;
	}
	
	public void init() {
		mReferedHashCodeSet.clear();
	}
	
	protected Set<String> getReferedHashCodeList() {
		final HashSet<String> ret = new HashSet<>();
		ret.addAll( mReferedHashCodeSet );
		for ( Map.Entry<String, JSONReader> e : mStringChildMap.entrySet() ) {
			final JSONReader child = e.getValue();
			ret.addAll( child.getReferedHashCodeList() );
		}
		return ret;
	}
	
	/* ======================================================================
	 * delegate
	 * ====================================================================== */
	
	public boolean asBoolean() {
		return asBoolean( false );
	}

	public boolean asBoolean( boolean defaultValue ) {
		if ( mCurrent == null ) {
			return defaultValue;
		}
		return mCurrent.asBoolean( defaultValue );
	}

	public double asDouble() {
		return asDouble( 0 );
	}

	public double asDouble( double defaultValue ) {
		if ( mCurrent == null ) {
			return defaultValue;
		}
		return mCurrent.asDouble( defaultValue );
	}

	public int asInt() {
		return asInt( 0 );
	}

	public int asInt( int defaultValue ) {
		if ( mCurrent == null ) {
			return defaultValue;
		}
		return mCurrent.asInt( defaultValue );
	}

	public long asLong() {
		return asLong( 0 );
	}

	public long asLong( long defaultValue ) {
		if ( mCurrent == null ) {
			return defaultValue;
		}
		return mCurrent.asLong( defaultValue );
	}

	public String asText() {
		return asText( null );
	}

	public String asText( String defaultValue ) {
		if ( mCurrent == null ) {
			return defaultValue;
		}
		return mCurrent.asText( defaultValue );
	}
	
	public Iterator<JsonNode> elements() {
		if ( mCurrent == null ) {
			return null;
		}
		return mCurrent.elements();
	}

	public boolean equals( Comparator<JsonNode> comparator, JsonNode other ) {
		if ( mCurrent == null ) {
			return other == null;
		}
		return mCurrent.equals(comparator, other);
	}

	@Override
	public boolean equals( Object arg0 ) {
		if ( mCurrent == null ) {
			return arg0 == null;
		}
		return mCurrent.equals( arg0 );
	}

	public Iterator<Entry<String, JsonNode>> fields() {
		if ( mCurrent == null ) {
			return null;
		}
		return mCurrent.fields();
	}
	
	@Override
	public void forEach( Consumer<? super JsonNode> action ) {
		if ( mCurrent == null ) {
			return;
		}
		mCurrent.forEach( action );
	}

	public boolean has( int index ) {
		if ( mCurrent == null ) {
			return false;
		}
		return mCurrent.has( index );
	}

	public boolean has( String fieldName ) {
		if ( mCurrent == null ) {
			return false;
		}
		return mCurrent.has( fieldName );
	}

	public boolean hasNonNull( int index ) {
		if ( mCurrent == null ) {
			return false;
		}
		return mCurrent.hasNonNull( index );
	}

	public boolean hasNonNull( String fieldName ) {
		if ( mCurrent == null ) {
			return false;
		}
		return mCurrent.hasNonNull( fieldName );
	}

	public int hashCode() {
		if ( mCurrent == null ) {
			return 0;
		}
		return mCurrent.hashCode();
	}

	public final boolean isNull() {
		if ( mCurrent == null ) {
			return true;
		}
		return mCurrent.isNull();
	}

	@Override
	public final Iterator<JsonNode> iterator() {
		if ( mCurrent == null ) {
			return null;
		}
		return mCurrent.iterator();
	}

	public int size() {
		if ( mCurrent == null ) {
			return 0;
		}
		return mCurrent.size();
	}

	@Override
	public String toString() {
		if ( mCurrent == null ) {
			return null;
		}
		return mCurrent.toString();
	}

}
