package biz.uro.CSVDataConverter.swing.json;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;

public class JSONObjectBuilder<T extends IJSONObject> {

	public interface IJSONObjectBuilderPatch<T extends IJSONObject> {
		T patch( JSONReader reader, T obj );
	}
	
	@SuppressWarnings("serial")
	public static class JSONObjectBuilderPatchException extends RuntimeException {
		public JSONObjectBuilderPatchException( IJSONObject obj ) {
			super( obj.jsonHashCode() + "のパッチ処理が競合しています" );
		}
	}
	
	private final JSONReader mReader;
	private final HashMap<String,Class<? extends T>> mClassMap = new HashMap<>();
	private final ArrayList<IJSONObjectBuilderPatch<T>> mPatchList = new ArrayList<>();
	
	public JSONObjectBuilder( JSONReader reader ) {
		mReader = reader;
	}
	
	public JSONObjectBuilder<T> addBase( Class<? extends T> clazz ) {
		mClassMap.put( null, clazz );
		return this;
	}
	
	public JSONObjectBuilder<T> addImplements( Class<? extends T> clazz ) {
		String[] classIDs;
		try {
			classIDs = clazz.getAnnotation( JSONClassID.class ).value();
		}
		catch ( NullPointerException e ) {
			throw new JSONException( clazz.getName() + "は@JSONClassIDが設定されていません" );
		}
		for ( String classID : classIDs ) {
			mClassMap.put( classID, clazz );
		}
		return this;
	}
	
	public JSONObjectBuilder<T> addPatch( IJSONObjectBuilderPatch<T> patch ) {
		mPatchList.add( patch );
		return this;
	}
	
	public JSONObjectBuilder<T> removePatch( IJSONObjectBuilderPatch<T> patch ) {
		mPatchList.remove( patch );
		return this;
	}
	
	public JSONObjectBuilder<T> clearPatch( IJSONObjectBuilderPatch<T> patch ) {
		mPatchList.clear();
		return this;
	}
	
	@SuppressWarnings("unchecked")
	public T build() {
		final JsonNode current = mReader.getCurrent();
		String classID;
		try {
			classID = current.get( JSONWriter.CLASS_ID ).asText();
		}
		catch ( NullPointerException e ) {
			classID = null;
		}
		Class<T> clazz;
		try {
			clazz = (Class<T>)mClassMap.get( classID );
		}
		catch ( ClassCastException e ) {
			throw new JSONException( "classID = " + classID + ";のclassロードに誤りがあります" );
		}
		if ( clazz == null ) {
			try {
				clazz = (Class<T>)mClassMap.get( null );
			}
			catch ( ClassCastException e ) {
				final JsonNode hashCodeNode = current.get( JSONWriter.HASH_CODE );
				final String hashCode = hashCodeNode == null ? null : hashCodeNode.asText();
				throw new JSONException( "hashCode = " + hashCode + ";のclassを事前にロードしてください" );
			}			
		}
		return build( clazz );
	}
	
	@SuppressWarnings("unchecked")
	private T build( Class<T> clazz ) {
		final JSONObjectMap map = mReader.getJSONObjectMap();
		final JsonNode current = mReader.getCurrent();
		JsonNode hashCodeNode = current == null ? null : current.get( JSONWriter.HASH_CODE );
		final String hashCode = hashCodeNode == null ? null : hashCodeNode.asText();
		if ( map.containsKey( hashCode ) ) {
			return (T)map.get( hashCode );
		}
		try {
			mReader.init();
			final T obj = clazz.getConstructor( mReader.getClass() ).newInstance( mReader );	
			T patchedObj = obj;
			T finallyObj = obj;
			for ( IJSONObjectBuilderPatch<T> patch : mPatchList ) {
				patchedObj = patch.patch( mReader, obj );
				if ( patchedObj != obj ) {
					if ( finallyObj != obj ) {
						throw new JSONObjectBuilderPatchException( obj );
					}
					finallyObj = patchedObj;
				}
			}
			map.put( hashCode, finallyObj );
			final Set<String> referedHashCodeList = mReader.getReferedHashCodeList();
			for ( String referedHashCode : referedHashCodeList ) {
				map.onRefered( referedHashCode, finallyObj );
			}
			return finallyObj;
		}
		catch ( InvocationTargetException e ) {
			throw new JSONException( e.getCause() + "\n" + clazz.getName() + "の初期化処理にエラーがあります" );			
		}
		catch (	NoSuchMethodException e ) {
			throw new JSONException( clazz.getName() + ";のコンストラクタを定義してください" );			
		} 
		catch ( InstantiationException | IllegalAccessException |
				IllegalArgumentException | SecurityException e ) {
			e.printStackTrace();
		}
		return null;
	}
	
}
