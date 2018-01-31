package biz.uro.CSVDataConverter.swing.builder.validator.condition.number;

import biz.uro.CSVDataConverter.swing.json.JSONClassID;
import biz.uro.CSVDataConverter.swing.json.JSONReader;
import biz.uro.CSVDataConverter.swing.json.JSONWriter;


public abstract class ValidatorCondition_Equals extends ValidatorCondition_Value {
	
	private static final String REVERSE = "Reverce";
	
	protected final boolean mReverse;
	
	@Override
	public void jsonMapping( JSONWriter writer ) {
		super.jsonMapping( writer );
		writer.add( REVERSE, mReverse );
	}

	public ValidatorCondition_Equals( JSONReader reader ) {
		super( reader );
		mReverse = reader.get( REVERSE ).asBoolean();
	}
	
	private ValidatorCondition_Equals( boolean reverse ) {
		mReverse = reverse;
	}

	/* ==============================================================
	 * implements --Number--
	 * ============================================================== */
	
	@JSONClassID( "Number_Equals" )
	public static final class _Number extends ValidatorCondition_Equals {

		public _Number( JSONReader reader ) {
			super( reader );
		}
		
		public _Number( boolean reverce ) {
			super( reverce );
		}

		@Override
		public boolean validation( Object value ) {
			if ( !( value instanceof Number ) ) {
				return false;
			}
			final double doubleValue = ((Number) value).doubleValue();
			if ( value() == doubleValue ) {
				return !mReverse;
			}
			return mReverse;
		}

		@Override
		public String getComment() {
			if ( mReverse ) {
				return "x!=" + format( value() );
			}
			return "x=" + format( value() );
		}
	}
	
	/* ==============================================================
	 * implements --String--
	 * ============================================================== */
	
	@JSONClassID( "String_Equals" )
	public static final class _String extends ValidatorCondition_Equals {

		public _String( JSONReader reader ) {
			super( reader );
		}
		
		public _String( boolean reverce ) {
			super( reverce );
		}

		@Override
		public boolean validation( Object value ) {
			if ( !( value instanceof String ) ) {
				return false;
			}
			final int length = ((String) value).length();
			if ( value() == length ) {
				return !mReverse;
			}
			return mReverse;
		}
		
		@Override
		public String getComment() {
			if ( mReverse ) {
				return "size!=" + format( value() );
			}
			return "size=" + format( value() );
		}
	}

}