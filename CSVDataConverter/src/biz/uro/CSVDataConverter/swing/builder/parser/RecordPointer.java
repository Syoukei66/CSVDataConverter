package biz.uro.CSVDataConverter.swing.builder.parser;

import biz.uro.CSVDataConverter.database.Record;
import biz.uro.CSVDataConverter.database.element.ElementValue;

public class RecordPointer {

	private final int mAttributeIndex;
	private final int mIndex;
	private RecordPointer mNext;
		
	public RecordPointer( int attributeIndex, int index ) {
		mAttributeIndex = attributeIndex;
		mIndex = index;
	}
	
	public void next( RecordPointer next ) {
		if ( mNext == null ) {
			mNext = next;
			return;
		}
		mNext.next( next );
	}
	
	public ElementValue getElementValue( Record record ) {
		final ElementValue value = record.getValue( mAttributeIndex ).get( mIndex );
		if ( mNext == null ) {
			return value;
		}
		return mNext.getElementValue( (Record)value.getValue() );		
	}
	
	public String getProgram( Record record ) {
		return getElementValue( record ).getProgram();
	}

	public Object getValue( Record record ) {
		return getElementValue( record ).getValue();
	}
	
	public String getUseToken( Record record, int index ) {
		return getElementValue( record ).getUseToken( index );
	}
	
}
