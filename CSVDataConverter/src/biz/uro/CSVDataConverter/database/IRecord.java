package biz.uro.CSVDataConverter.database;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import biz.uro.CSVDataConverter.database.element.Element;
import biz.uro.CSVDataConverter.database.element.ElementValue;
import biz.uro.CSVDataConverter.string.ProgramBuilder;

public abstract class IRecord {

	private final ISchema<?> mSchema;
	private final LinkedHashMap<String,ArrayList<ElementValue>> mValueMap = new LinkedHashMap<>();

	public IRecord( ISchema<?> schema ) {
		mSchema = schema;
	}
	
	public void addValues( IRecord other ) {
		for ( Map.Entry<String,ArrayList<ElementValue>> e : other.mValueMap.entrySet() ) {
			final String name = e.getKey();
			final ArrayList<ElementValue> values = e.getValue();
			for ( ElementValue value : values ) {
				addValue( name, value );
			}
		}
	}
	
	public void addValue( String name, ElementValue value ) {
		ArrayList<ElementValue> valueList = mValueMap.get( name );
		if ( valueList == null ) {
			valueList = new ArrayList<>();
			mValueMap.put( name, valueList );
		}
		valueList.add( value );
	}

	public void clear() {
		mValueMap.clear();
	}
	
	public boolean isEmpty() {
		return mValueMap.keySet().isEmpty();
	}
	
	public ArrayList<ElementValue> getValue( String name ) {
		return mValueMap.get( name );
	}
	
	public ArrayList<ElementValue> getValue( int index ) {
		return mValueMap.get( mValueMap.keySet().toArray( new String[mValueMap.size()] )[index] );
	}
	
	public String convertProgram() {
		return convertProgram( mSchema );
	}
	
	public String convertProgram( ISchema<?> schema ) {
		final Map<String,Element> elementMap = schema.getElementMap();
		final ProgramBuilder str = new ProgramBuilder();
		boolean isFirst = true;
		for ( Map.Entry<String,Element> e : elementMap.entrySet() ) {
			if ( !isFirst ) {
				str.append( ", " );
			}
			isFirst = false;
			final Element element = e.getValue();
			final ArrayList<ElementValue> valueList = getValue( e.getKey() );
			final int size = element.getSize();
			if ( size == 1 ) {
				str.append( valueList.get( 0 ).getProgram() );
			}
			else if ( size > 1 ) {
				str.append( "new " + element.getStatement().getType().getTypeName() + "[]{" );
				boolean isFirst2 = true;
				for ( int i = 0; i < size; i++ ) {
					if ( !isFirst2 ) {
						str.append( ", " );
					}
					isFirst2 = false;
					str.append( valueList.get( i ).getProgram() );
				}
				str.append( "}" );
			}
			else if ( size == -1 ) {
				boolean isFirst2 = true;
				final int dataSize = valueList.size();
				for ( int i = 0; i < dataSize; i++ ) {
					if ( !isFirst2 ) {
						str.append( ", " );
					}
					isFirst2 = false;
					str.append( valueList.get( i ).getProgram() );
				}				
			}
		}
		return str.toString();
	}
	
	@Override
	public boolean equals( Object other ) {
		if ( !( other instanceof IRecord ) ) {
			return false;
		}
		if ( this == other ) {
			return true;
		}
		final IRecord otherRecord = (IRecord) other;
		for ( Map.Entry<String,ArrayList<ElementValue>> e : mValueMap.entrySet() ) {
			final String key = e.getKey();
			final ArrayList<ElementValue> valueList = e.getValue();
			final ArrayList<ElementValue> otherValueList = otherRecord.mValueMap.get( key );
			final int valueCount = valueList.size();
			if ( valueCount != otherValueList.size() ) {
				return false;
			}
			for ( int i = 0; i < valueCount; i++ ) {
				final Object value = valueList.get( i ).getValue();
				final Object otherValue = otherValueList.get( i ).getValue();
				if ( value == null ) {
					if ( otherValue == null ) {
						continue;						
					}
					return false;
				}
				if ( !value.equals( otherValue ) ) {
					return false;
				}
			}
		}
		return true;
	}

}
