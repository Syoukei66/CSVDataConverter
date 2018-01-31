package biz.uro.CSVDataConverter.swing.builder.validator.kind.list;

import biz.uro.CSVDataConverter.swing.builder.validator.kind.ValidatorKind;
import biz.uro.CSVDataConverter.swing.json.JSONReader;

public abstract class ValidatorKind_List extends ValidatorKind {

	public ValidatorKind_List( JSONReader reader ) {
		super( reader );
	}
	
	public ValidatorKind_List( String name ) {
		super( name, InputKind.LIST );
	}

}
