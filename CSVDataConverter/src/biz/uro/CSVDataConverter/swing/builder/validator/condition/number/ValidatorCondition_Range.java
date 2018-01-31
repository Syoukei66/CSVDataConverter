package biz.uro.CSVDataConverter.swing.builder.validator.condition.number;

import biz.uro.CSVDataConverter.swing.builder.validator.condition.ValidatorCondition;
import biz.uro.CSVDataConverter.swing.json.JSONClassID;
import biz.uro.CSVDataConverter.swing.json.JSONReader;
import biz.uro.CSVDataConverter.swing.json.JSONWriter;

public abstract class ValidatorCondition_Range extends ValidatorCondition {
	
	private static final String MIN_TEXT = "最小値";
	private static final String MAX_TEXT = "最大値";
	
	private static final String MIN = "Min";
	private static final String MAX = "Max";
	private static final String IN = "IsIn";
	
	protected ValidatorParmModel mMin, mMax;
	protected final boolean mIn;
	
	@Override
	public void jsonMapping( JSONWriter writer ) {
		writer.add( MIN, mMin );
		writer.add( MAX, mMax );
		writer.add( IN, mIn );
	}
	
	public ValidatorCondition_Range( JSONReader reader ) {
		super( reader );
		mMin = reader.get( MIN ).asObject( ValidatorParmModel.class );
		mMax = reader.get( MAX ).asObject( ValidatorParmModel.class );
		mIn = reader.get( IN ).asBoolean();
	}
	
	public ValidatorCondition_Range( boolean in ) {
		mIn = in;
	}

	public double min() {
		return Double.parseDouble( model1().toString() );
	}
	
	public double max() {
		return Double.parseDouble( model2().toString() );
	}
	
	public ValidatorParmModel model1() {
		if ( mMin == null ) {
			mMin = new ValidatorParmModel();
			mMin.setName( MIN_TEXT );
			mMin.setValue( 0 );
		}
		return mMin;
	};
	
	public ValidatorParmModel model2() {
		if ( mMax == null ) {
			mMax = new ValidatorParmModel();
			mMax.setName( MAX_TEXT );
			mMax.setValue( 0 );
		}
		return mMax;
	};
	
	@Override
	public void test( int input ) {
		model1().setValue( input );
		model2().setValue( Integer.toString( input * 2 + 1 ) );
	}
	
	/* ==============================================================
	 * implements --Number--
	 * ============================================================== */

	@JSONClassID( "Number_Range" )
	public static final class _Number extends ValidatorCondition_Range {
		
		public _Number( JSONReader reader ) {
			super( reader );
		}
		
		public _Number( boolean in ) {
			super( in );
		}

		@Override
		public final boolean validation( Object value ) {
			if ( !( value instanceof Number ) ) {
				return false;
			}
			final double doubleValue = ((Number) value).doubleValue();
			if ( min() < doubleValue && doubleValue < max() ) {
				return mIn;
			}
			return !mIn;
		}
		
		@Override
		public String getComment() {
			if ( mIn ) {
				return format( min() ) + "<=x<=" + format( max() );			
			}
			return "x<" + format( min() ) + "|x>"+ format( max() );	
		}
		
	}
	
	/* ==============================================================
	 * implements --String--
	 * ============================================================== */

	@JSONClassID( "String_Range" )
	public static final class _String extends ValidatorCondition_Range {
		
		public _String( JSONReader reader ) {
			super( reader );
		}
		
		public _String( boolean in ) {
			super( in );
		}

		@Override
		public boolean validation( Object value ) {
			if ( !( value instanceof String ) ) {
				return false;
			}
			final int length = ((String) value).length();
			if ( min() < length && length < max() ) {
				return mIn;
			}
			return !mIn;
		}
		
		@Override
		public String getComment() {
			if ( mIn ) {
				return format( min() ) + "<=size<=" + format( max() );			
			}
			return "size<" + format( min() ) + "|size>"+ format( max() );	
		}
		
	}

	
}