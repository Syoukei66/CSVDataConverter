package biz.uro.CSVDataConverter.swing.builder.validator.condition.list;

import java.util.ArrayList;

import biz.uro.CSVDataConverter.swing.builder.validator.condition.ValidatorCondition;
import biz.uro.CSVDataConverter.swing.json.JSONReader;

public abstract class ValidatorCondition_List extends ValidatorCondition {
	
	protected final ArrayList<Object> mList = new ArrayList<>();
	
	public ValidatorCondition_List( JSONReader reader ) {
		super( reader );
	}
	
	public ValidatorCondition_List() {
	}

	@Override
	public boolean validation( Object value ) {
		return mList.contains( value );
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
	public void test( int input ) {
		
	}

}