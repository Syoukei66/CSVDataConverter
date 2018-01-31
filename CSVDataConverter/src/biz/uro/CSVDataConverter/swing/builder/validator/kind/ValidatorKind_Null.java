package biz.uro.CSVDataConverter.swing.builder.validator.kind;

import biz.uro.CSVDataConverter.swing.builder.validator.condition.ValidatorCondition;
import biz.uro.CSVDataConverter.swing.builder.validator.condition.ValidatorCondition_Null;
import biz.uro.CSVDataConverter.swing.json.JSONReader;

public class ValidatorKind_Null extends ValidatorKind {

	public ValidatorKind_Null( JSONReader reader ) {
		super( reader );
	}
	
	public ValidatorKind_Null() {
		super( "Null", InputKind.ALL );
	}

	@Override
	protected ValidatorCondition createConditioner() {
		return new ValidatorCondition_Null();
	}
	
	@Override
	public String toString() {
		return "無し";
	}

}
