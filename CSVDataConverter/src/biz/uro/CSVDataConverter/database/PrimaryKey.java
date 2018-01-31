package biz.uro.CSVDataConverter.database;

import biz.uro.CSVDataConverter.database.element.Element;

public class PrimaryKey extends ISchema<PrimaryKeyRecord> {
	
	private final String[] mKeys;

	public PrimaryKey( String... keys ) {
		mKeys = keys;
	}
	
	@Override
	public void addElement( String name, Element element ) {
		if ( !isPrimaryKey( name ) ) {
			return;
		}
		super.addElement( name, element );
	}
	
	public boolean isPrimaryKey( String name ) {
		for ( String primaryKey : mKeys ) {
			if ( primaryKey.equals( name ) ) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public PrimaryKeyRecord createRecord() {
		return new PrimaryKeyRecord( this );
	}

}
