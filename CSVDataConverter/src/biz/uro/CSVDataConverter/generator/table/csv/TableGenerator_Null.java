package biz.uro.CSVDataConverter.generator.table.csv;

import biz.uro.CSVDataConverter.database.ISchema;
import biz.uro.CSVDataConverter.database.Table;

public class TableGenerator_Null extends TableGenerator_UseEnum {

	@Override
	protected ISchema<?> getConstructorSchema( Table table ) {
		return null;
	}

	@Override
	protected ISchema<?> getSchema( Table table ) {
		return table.getSchema();
	}
	
}
