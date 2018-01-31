package biz.uro.CSVDataConverter.swing.builder.validator.kind.list;

import biz.uro.CSVDataConverter.swing.builder.validator.condition.ValidatorCondition;
import biz.uro.CSVDataConverter.swing.builder.validator.condition.list.ValidatorCondition_Boolean;
import biz.uro.CSVDataConverter.swing.json.JSONReader;

public class ValidatorKind_Boolean extends ValidatorKind_List {

	public ValidatorKind_Boolean( JSONReader reader ) {
		super( reader );
	}
	
	public ValidatorKind_Boolean() {
		super( "Boolean" );
	}

	@Override
	protected ValidatorCondition createConditioner() {
		return new ValidatorCondition_Boolean();
	}

	@Override
	public String toString() {
		return "true(=0)/false(=1) のみ受け付ける";
	}

	@Override
	public String jsonHashCode() {
		return "ValidatorKind_Boolean";
	}

}
