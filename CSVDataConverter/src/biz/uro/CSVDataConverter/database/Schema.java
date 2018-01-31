package biz.uro.CSVDataConverter.database;

import biz.uro.CSVDataConverter.database.element.Element;

public class Schema extends ISchema<Record> {
	
	private final PrimaryKey mPrimaryKey;
	
	public Schema( String... primaryKey ) {
		mPrimaryKey = new PrimaryKey( primaryKey );
	}
	
	public void element( Element element ) {
		final String name = element.getName();
		mPrimaryKey.addElement( name, element );
		addElement( name, element );
	}

	public PrimaryKey getPrimaryKey() {
		return mPrimaryKey;
	}

	@Override
	protected Record createRecord() {
		return new Record( this );
	}
	

}
