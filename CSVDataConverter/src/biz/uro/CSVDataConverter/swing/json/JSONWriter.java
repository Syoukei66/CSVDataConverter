package biz.uro.CSVDataConverter.swing.json;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JSONWriter {

	protected static final String HASH_CODE = "_HashCode";
	protected static final String CLASS_ID = "_ClassID";
	
	private static final String[] RESERVED = new String[] {
		HASH_CODE,
		CLASS_ID,
	};
	
	private final Map<String,Object> mMap = new LinkedHashMap<>();
	
	//withoutCheckを作成
	private void checkReserved( String name ) {
		for ( String reserved : RESERVED ) {
			if ( reserved.equals( name ) ) {
				throw new IllegalArgumentException( name + "は予約語なので使用できません。" );
			}
		}
	}
	
	public void addWithoutCheck( String name, Object obj ) {
		mMap.put( name, obj );			
	}
	
	public void add( String name, Object obj ) {
		checkReserved( name );
		addWithoutCheck( name, obj );
	}
	
	public void add( String name, IJSONList jsonList ) {
		checkReserved( name );
		jsonList.jsonMapping( name, this );
	}
	
	private JSONWriter addWithoutCheck( String name, IJSONObject jsonObj ) {
		if ( jsonObj == null ) {
			return null;
		}
		final JSONWriter writer = new JSONWriter();
		final String jsonHashCode = jsonObj.jsonHashCode();
		if ( jsonHashCode != null ) {
			writer.addWithoutCheck( HASH_CODE, jsonHashCode );
		}
		try {
			final String[] classIDs = jsonObj.getClass().getAnnotation( JSONClassID.class ).value();
			writer.addWithoutCheck( CLASS_ID, classIDs[0] );
		}
		catch ( Exception e ) {
			
		}
		try {
			jsonObj.jsonMapping( writer );			
		}
		catch ( NullPointerException e ) {
			throw new JSONException( jsonObj.getClass().getSimpleName() + "\n" + jsonObj.toString() + "\nで必要なデータが不足しています。\n＊のある項目は必ず設定してください。\n**のある項目はデータ定義にEnumを使用する場合は必ず設定してください。" );
		}
		return writer;
	}

	public void add( String name, IJSONObject jsonObj ) {
		checkReserved( name );
		mMap.put( name, addWithoutCheck( name, jsonObj ) );
	}
	
	public void createList( String name ) {
		checkReserved( name );
		createListWithoutCheck( name );
	}

	public void createListWithoutCheck( String name ) {
		mMap.put( name, new ArrayList<Object>() );
	}

	@SuppressWarnings("unchecked")
	public void addListElement( String name, Object obj ) {
		List<Object> list = (List<Object>)mMap.get( name );
		list.add( obj );
	}

	@SuppressWarnings("unchecked")
	public void addListElement( String name, IJSONObject jsonObj ) {
		List<Object> list = (List<Object>)mMap.get( name );
		list.add( addWithoutCheck( name, jsonObj ) );
	}
	
	@SuppressWarnings("unchecked")
	public Map<String,Object> buildObjectMap() {
		final Map<String,Object> map = new LinkedHashMap<>();
		for ( Map.Entry<String,Object> entry : mMap.entrySet() ) {
			final String jsonName = entry.getKey();
			final Object obj = entry.getValue();
			if ( obj instanceof JSONWriter ) {
				final JSONWriter writer = (JSONWriter)obj;
				map.put( jsonName, writer.buildObjectMap() );
				continue;
			}
			if ( obj instanceof List ) {
				final List<Object> writerList = (List<Object>)obj;
				final List<Object> jsonList = new ArrayList<>();
				for ( Object writer : writerList ) {
					if ( writer instanceof JSONWriter ) {
						jsonList.add( ((JSONWriter)writer).buildObjectMap() );
						continue;
					}
					jsonList.add( writer );
				}
				map.put( jsonName, jsonList );
				continue;
			}
			map.put( jsonName, obj );
		}
		return map;
	}
	
	public String build( ObjectMapper mapper ) {
		mapper.enable( SerializationFeature.INDENT_OUTPUT );
		String jsonString = null;
		final Map<String,Object> map = buildObjectMap();
		if ( map.size() == 0 ) {
			return null;
		}
		try {
			jsonString = mapper.writeValueAsString( map );
		} catch ( JsonProcessingException e ) {
			e.printStackTrace();
		}
		return jsonString;
	}
	
}
