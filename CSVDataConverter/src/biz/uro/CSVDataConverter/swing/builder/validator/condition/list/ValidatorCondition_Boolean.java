package biz.uro.CSVDataConverter.swing.builder.validator.condition.list;

import biz.uro.CSVDataConverter.swing.json.JSONClassID;
import biz.uro.CSVDataConverter.swing.json.JSONReader;
import biz.uro.CSVDataConverter.swing.json.JSONWriter;

@JSONClassID( "Boolean" )
public class ValidatorCondition_Boolean extends ValidatorCondition_List {

	@Override
	public void jsonMapping( JSONWriter writer ) {
	}

	public ValidatorCondition_Boolean( JSONReader reader ) {
		super( reader );
	}
	
	public ValidatorCondition_Boolean() {
		mList.add( true );
		mList.add( false );
	}

	@Override
	public String getComment() {
		return "Boolean";
	}
	
}