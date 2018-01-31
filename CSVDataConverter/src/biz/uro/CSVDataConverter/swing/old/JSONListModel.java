package biz.uro.CSVDataConverter.swing.old;

import biz.uro.CSVDataConverter.swing.json.IJSONDataModel;
import biz.uro.CSVDataConverter.swing.json.IJSONList;
import biz.uro.CSVDataConverter.swing.json.JSONReader;
import biz.uro.CSVDataConverter.swing.json.JSONWriter;

public class JSONListModel<T extends IJSONDataModel> extends DataListModel<T> implements IJSONList {

	public interface IJsonParseProcess<T extends IJSONDataModel> {
		T parse( JSONReader reader, Class<T> clazz );
		void loadConstants( JSONListModel<T> model );
	}
	
	private final IJsonParseProcess<T> mProcess;
	private final Class<T> mClass;
	
	@Override
	public void jsonParse( JSONReader reader ) {
		loadConstants();
		//constantsのリストを取得
		//readerのマップに登録
		//read済みのオブジェクトとなるのでセーフ
		final int count = reader.size();
		for ( int i = 0; i < count; i++ ) {
			T obj = null;
			try {
				final JSONReader elementReader = reader.get( i );
				obj = mProcess.parse( elementReader, mClass );
			} catch ( Exception e ) {
				e.printStackTrace();
			}
			addElement( obj );
		}
	}
	
	@Override
	public void jsonMapping( String name, JSONWriter writer ) {
		writer.createList( name );
//		final int constantsCount = mConstants.size();
//		for ( int i = 0; i < constantsCount; i++ ) {
//			final T data = mConstants.get( i );	
//			//addListConstantsはhashCodeのみの書き込みとなる
//			//（hashCodeすら必要ないかも）
//			writer.addListConstants( name, data );
//		}
		final int dataMapCount = mListModel.size();
		for ( int i = 0; i < dataMapCount; i++ ) {
			final T data = mListModel.get( i );
			if ( mConstants.contains( data ) ) {
				continue;
			}
			writer.addListElement( name, data );
		}
	}
	
	public JSONListModel( Class<T> clazz ) {
		this( clazz, null );
	}

	public JSONListModel( Class<T> clazz, IJsonParseProcess<T> process ) {
		mClass = clazz;
		mProcess = process;
	}
	
	public void loadConstants() {
		mProcess.loadConstants( this );
	}
	
}
