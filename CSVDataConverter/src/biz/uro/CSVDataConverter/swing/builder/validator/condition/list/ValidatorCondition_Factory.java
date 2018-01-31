package biz.uro.CSVDataConverter.swing.builder.validator.condition.list;

import biz.uro.CSVDataConverter.swing.builder.StatementBuilder;
import biz.uro.CSVDataConverter.swing.json.JSONClassID;
import biz.uro.CSVDataConverter.swing.json.JSONReader;
import biz.uro.CSVDataConverter.swing.json.JSONWriter;
import biz.uro.CSVDataConverter.swing.json.JSONObjectMap.IJSONObjectLoadedListener;

@JSONClassID( "Factory" )
public class ValidatorCondition_Factory extends ValidatorCondition_List {

	private static final String FACTORY_HASH_CODE = "FactoryHashCode";
	
	private StatementBuilder._Factory mFactory;
	
	@Override
	public void jsonMapping( JSONWriter writer ) {
		writer.add( FACTORY_HASH_CODE, mFactory.jsonHashCode() );
	}
	
	public ValidatorCondition_Factory( JSONReader reader ) {
		super( reader );
		reader.get( FACTORY_HASH_CODE ).reserveObject( new IJSONObjectLoadedListener() {
			@Override
			public void onLoaded( Object obj ) {
				mFactory = (StatementBuilder._Factory)obj;
			}
		});
	}
	
	public ValidatorCondition_Factory( StatementBuilder._Factory function ) {
		mFactory = function;
	}

	@Override
	public String getComment() {
		return "Factory";
	}

	public String toString() {
		return mFactory.getName();		
	}
}
