//package biz.uro.CSVDataConverter.util;
//
//import java.util.ArrayList;
//import java.util.LinkedHashMap;
//import java.util.Map;
//
//import biz.uro.CSVDataConverter.constants.Constants;
//import biz.uro.CSVDataConverter.database.Record;
//import biz.uro.CSVDataConverter.database.Schema;
//import biz.uro.CSVDataConverter.database.Table;
//import biz.uro.CSVDataConverter.database.element.Element;
//import biz.uro.CSVDataConverter.database.element.ElementType;
//import biz.uro.CSVDataConverter.database.element.ElementValue;
//
//public class ForeignTable extends Table {
//	
//	public interface IForeignRecord {
//		String recordName();
//		void records( Record record );
//	}
//	
//	public static class Builder {
//		private final String mPackagePath;
//		private final String mName;
//		private final ArrayList<String> mPrimaryKeys = new ArrayList<>();
//		private final Map<String, Element.Base> mElementMap = new LinkedHashMap<>();
//		private String mGetterName = Constants.TABLE_GETTER_METHOD_NAME;
//		public Builder( String packagePath, String name ) {
//			mPackagePath = packagePath;
//			mName = name;
//		}
//		
//		public Builder getterName( String getterName ) {
//			mGetterName = getterName;
//			return this;
//		}
//		
//		public Builder primaryKey( String name, Element.Base base ) {
//			mPrimaryKeys.add( name );
//			return element( name, base );
//		}
//		
//		public Builder element( String name, Element.Base base ) {
//			mElementMap.put( name, base );
//			return this;
//		}
//		
//		public ForeignTable build() {
//			final Schema schema = new Schema( mPrimaryKeys.toArray( new String[mPrimaryKeys.size()] ) ) {
//				{
//					for ( Map.Entry<String, Element.Base> e : mElementMap.entrySet() ) {
//						element( e.getKey(), e.getValue() );
//					}
//				}
//			};
//			return new ForeignTable( this, schema );
//		}
//	}
//	
//	private static final String NAME = "identifier";
//	private final String mGetterName;
//	private boolean mIsCreated;
//	
//	private ForeignTable( Builder builder, Schema schema ) {
//		super( builder.mPackagePath, builder.mName, schema );
//		mGetterName = builder.mGetterName;
//	}
//
//	private void createTable( IForeignRecord[] records ) {
//		if ( mIsCreated ) {
//			return;
//		}
//		for ( IForeignRecord foreignecord : records ) {
//			final Record record = new Record( getSchema() );
//			final String name = foreignecord.recordName();
//			record.addValue( NAME, new ElementValue( name, name, "" ) );
//			foreignecord.records( record );
//			addRecord( record );
//		}
//		mIsCreated = true;
//	}
//	
//	public ElementType createType( IForeignRecord[] records ) {
//		createTable( records );
//		return ElementType._ForeignKey( this, mGetterName );
//	}
//	
//	@Override
//	public String parseRecordName( Record record ) {
//		return record.getValue( NAME ).get( 0 ).getProgram();
//	}
//
//	@Override
//	public String parseComment( Record record ) {
//		return null;
//	}
//
//}
