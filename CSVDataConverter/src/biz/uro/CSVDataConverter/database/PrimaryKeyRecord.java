package biz.uro.CSVDataConverter.database;

import biz.uro.CSVDataConverter.database.element.ElementValue;

public class PrimaryKeyRecord extends IRecord {

	private final PrimaryKey mPrimaryKey;

	public PrimaryKeyRecord( PrimaryKey primaryKey ) {
		super( primaryKey );
		mPrimaryKey = primaryKey;
	}
	
	@Override
	public void addValue( String name, ElementValue value ) {
		if ( !mPrimaryKey.isPrimaryKey( name ) ) {
			return;
		}
		super.addValue( name, value );
	}
	
}
