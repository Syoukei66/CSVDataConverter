package biz.uro.CSVDataConverter.swing.builder.validator.condition.number;

import biz.uro.CSVDataConverter.swing.json.JSONClassID;
import biz.uro.CSVDataConverter.swing.json.JSONReader;
import biz.uro.CSVDataConverter.swing.json.JSONWriter;


public abstract class ValidatorCondition_CompareEquals extends ValidatorCondition_Value {

	private static final String GREATER = "Greater";
	
	protected final boolean mGreater;

	@Override
	public void jsonMapping( JSONWriter writer ) {
		super.jsonMapping( writer );
		writer.add( GREATER, mGreater );
	}

	public ValidatorCondition_CompareEquals( JSONReader reader ) {
		super( reader );
		mGreater = reader.get( GREATER ).asBoolean();
	}
	
	public ValidatorCondition_CompareEquals( boolean greater ) {
		mGreater = greater;
	}
	
	/* ==============================================================
	 * implements --Number--
	 * ============================================================== */

	@JSONClassID( "Number_CompareEquals" )
	public static final class _Number extends ValidatorCondition_CompareEquals {

		public _Number( JSONReader reader ) {
			super( reader );
		}

		public _Number( boolean greater ) {
			super( greater );
		}
		
		@Override
		public boolean validation( Object value ) {
			if ( !( value instanceof Number ) ) {
				return false;
			}
			final double doubleValue = ((Number) value).doubleValue();
			if ( mGreater ) {
				if ( value() <= doubleValue ) {
					return true;
				}
				return false;				
			}
			if ( value() >= doubleValue ) {
				return true;
			}
			return false;	
		}
		
		@Override
		public String getComment() {
			if ( mGreater ) {
				return "x>=" + format( value() );
			}
			return "x<=" + format( value() );
		}
		
	}
	
	/* ==============================================================
	 * implements --String--
	 * ============================================================== */

	@JSONClassID( "String_CompareEquals" )
	public static final class _String extends ValidatorCondition_CompareEquals {

		public _String( JSONReader reader ) {
			super( reader );
		}

		public _String( boolean greater ) {
			super( greater );
		}
		
		@Override
		public boolean validation( Object value ) {
			if ( !( value instanceof String ) ) {
				return false;
			}
			final int length = ((String) value).length();
			if ( mGreater ) {
				if ( value() <= length ) {
					return true;
				}
				return false;				
			}
			if ( value() >= length ) {
				return true;
			}
			return false;	
		}
		
		@Override
		public String getComment() {
			if ( mGreater ) {
				return "size>=" + format( value() );
			}
			return "size<=" + format( value() );
		}
		
	}
}