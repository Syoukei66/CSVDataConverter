package biz.uro.CSVDataConverter.swing.builder.validator.kind.list;

import biz.uro.CSVDataConverter.swing.builder.StatementBuilder;
import biz.uro.CSVDataConverter.swing.builder.validator.condition.ValidatorCondition;
import biz.uro.CSVDataConverter.swing.builder.validator.condition.list.ValidatorCondition_Factory;
import biz.uro.CSVDataConverter.swing.json.JSONClassID;
import biz.uro.CSVDataConverter.swing.json.JSONReader;
import biz.uro.CSVDataConverter.swing.json.JSONWriter;
import biz.uro.CSVDataConverter.swing.json.JSONObjectMap.IJSONObjectLoadedListener;

@JSONClassID( "Factory" )
public class ValidatorKind_Factory extends ValidatorKind_List {

	private static final String STATEMENT_HASH_CODE = "StatementHashCode";

	private StatementBuilder._Factory mFactory;
	
	@Override
	public void jsonMapping( JSONWriter writer ) {
		super.jsonMapping( writer );
		writer.add( STATEMENT_HASH_CODE, mFactory.jsonHashCode() );
	}
	
	public ValidatorKind_Factory( JSONReader reader ) {
		super( reader );
		reader.get( STATEMENT_HASH_CODE ).reserveObject( new IJSONObjectLoadedListener() {
			@Override
			public void onLoaded(Object obj) {
				mFactory = (StatementBuilder._Factory)obj;
			}
		} );
	}

	public ValidatorKind_Factory( StatementBuilder._Factory factory ) {
		super( factory.getName() );
		mFactory = factory;
	}

	@Override
	protected ValidatorCondition createConditioner() {
		return new ValidatorCondition_Factory( mFactory );
	}
	
	@Override
	public String jsonHashCode() {
		return Integer.toString( hashCode() );
	}

}
