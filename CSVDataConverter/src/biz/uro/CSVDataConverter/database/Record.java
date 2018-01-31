package biz.uro.CSVDataConverter.database;

import biz.uro.CSVDataConverter.database.element.ElementValue;

public class Record extends IRecord {

	private final PrimaryKeyRecord mPrimaryKeyRecord;
	
	public Record( Schema schema ) {
		super( schema );
		mPrimaryKeyRecord = schema.getPrimaryKey().createRecord();
	}
	
	public void addValue( String name, Object value ) {
		addValue( name, ElementValue.OBJECT( value ) );
	}
	
	@Override
	public void addValue( String name, ElementValue value ) {
		mPrimaryKeyRecord.addValue( name, value );
		super.addValue( name, value );
	}
	
	@Override
	public void clear() {
		mPrimaryKeyRecord.clear();
		super.clear();
	};
	
	public PrimaryKeyRecord getPrimaryRecord() {
		return mPrimaryKeyRecord;
	}
	
}
