package biz.uro.CSVDataConverter.swing.builder.validator.condition.number;

import biz.uro.CSVDataConverter.swing.builder.validator.condition.ValidatorCondition;
import biz.uro.CSVDataConverter.swing.json.JSONReader;
import biz.uro.CSVDataConverter.swing.json.JSONWriter;

public abstract class ValidatorCondition_Value extends ValidatorCondition {
	
	private static final String VALUE_TEXT = "値";
	private static final String VALUE = "Value";
	
	protected ValidatorParmModel mValue;

	@Override
	public void jsonMapping( JSONWriter writer ) {
		writer.add( VALUE, mValue );
	}
	
	public ValidatorCondition_Value( JSONReader reader ) {
		super( reader );
		mValue = reader.get( VALUE ).asObject( ValidatorParmModel.class );
		mValue.setName( VALUE_TEXT );
	}
	
	public ValidatorCondition_Value() {
	}
	
	public double value() {
		return Double.parseDouble( mValue.toString() );
	}
	
	public ValidatorParmModel model1() {
		if ( mValue == null ) {
			mValue = new ValidatorParmModel();
			mValue.setName( VALUE_TEXT );
			mValue.setValue( 0 );
		}
		return mValue;
	};
	
	public ValidatorParmModel model2() {
		return null;
	}
	
	@Override
	public void test( int input ) {
		model1().setValue( input );
	}

}