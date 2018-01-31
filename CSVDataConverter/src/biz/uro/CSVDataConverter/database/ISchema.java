package biz.uro.CSVDataConverter.database;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

import biz.uro.CSVDataConverter.database.element.Element;
import biz.uro.CSVDataConverter.io.csv.CSVState;
import biz.uro.CSVDataConverter.io.csv.ICSVLineReader;

public abstract class ISchema<T extends IRecord> {

	private final Map<String,Element> mElementMap = new LinkedHashMap<>();
	
	protected abstract T createRecord();
	
	public Map<String,Element> getElementMap() {
		return mElementMap;
	}
	
	protected void addElement( String name, Element element ) {
		mElementMap.put( name, element );
	}
	
	public Element[] getParserElements() {
		final HashSet<Element> elementSet = new HashSet<>();
		elementSet.addAll( mElementMap.values() );
		for ( Map.Entry<String,Element> e : mElementMap.entrySet() ) {
			final Element element = e.getValue();
			elementSet.addAll( Arrays.asList( element.getStatement().getParserElements() ) );
		}
		return elementSet.toArray( new Element[elementSet.size()] );
	}
	
	public void init() {
		for ( Map.Entry<String,Element> set : mElementMap.entrySet() ) {
			final Element element = set.getValue();
			element.getOption().init();
		}
	}
	
	public T read( Table table, ICSVLineReader reader ) throws RuntimeException {
		final T record = createRecord();
		return read( record, table, reader );
	}
	
	public T read( T record, Table table, ICSVLineReader reader ) throws RuntimeException {
		for ( Map.Entry<String,Element> set : mElementMap.entrySet() ) {
			final String key = set.getKey();
			final Element element = set.getValue();
			final int size = element.getSize();
			if ( size == 1 ) {
				final CSVState csvState = new CSVState( reader ); 
				try {
					record.addValue( key, element.read( table, csvState ) );			
				}
				catch ( RuntimeException e ) {
					//TODO:例外をここでキャッチせず、GUIの方に表示させる
					final RuntimeException e2 = new RuntimeException( e.getClass().getSimpleName() + " " + e.getMessage() + " " + key  );
					e.printStackTrace();
					e2.setStackTrace( e.getStackTrace() );
					throw e2;
				}
			}
			else if ( size > 1 ) {
				for ( int i = 0; i < size; i++ ) {
					final CSVState csvState = new CSVState( reader ); 
					try {
						record.addValue( key, element.read( table, csvState ) );			
					}
					catch ( RuntimeException e ) {
						final RuntimeException e2 = new RuntimeException( e.getClass().getSimpleName() + " " + e.getMessage() + " " + key  );
						e2.setStackTrace( e.getStackTrace() );
						throw e2;
					}
				}				
			}
		}
		return record;
	}
	
	public int getMaxLength() {
		int cnt = 0;
		for ( Map.Entry<String,Element> e : mElementMap.entrySet() ) {
			final Element element = e.getValue();
			cnt += element.getMaxLength();
		}
		return cnt;
	}

}
