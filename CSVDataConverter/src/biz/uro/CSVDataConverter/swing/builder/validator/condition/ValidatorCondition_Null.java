package biz.uro.CSVDataConverter.swing.builder.validator.condition;

import biz.uro.CSVDataConverter.swing.json.JSONClassID;
import biz.uro.CSVDataConverter.swing.json.JSONReader;
import biz.uro.CSVDataConverter.swing.json.JSONWriter;

@JSONClassID( "Null" )
public final class ValidatorCondition_Null extends ValidatorCondition {

	@Override
	public void jsonMapping( JSONWriter writer ) {
	}

	public ValidatorCondition_Null( JSONReader reader ) {
		super( reader );
	}
	
	public ValidatorCondition_Null() {
	}
	
	@Override
	public boolean validation( Object value ) {
		return true;
	}

	@Override
	public ValidatorParmModel model1() {
		return null;
	}

	@Override
	public ValidatorParmModel model2() {
		return null;
	}

	@Override
	public String getComment() {
		return "";
	}

	@Override
	public void test( int input ) {
		
	}

}
