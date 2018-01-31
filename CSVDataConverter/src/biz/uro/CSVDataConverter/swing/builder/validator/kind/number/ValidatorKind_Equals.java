package biz.uro.CSVDataConverter.swing.builder.validator.kind.number;

import biz.uro.CSVDataConverter.swing.builder.validator.condition.ValidatorCondition;
import biz.uro.CSVDataConverter.swing.builder.validator.condition.number.ValidatorCondition_Equals;
import biz.uro.CSVDataConverter.swing.builder.validator.kind.ValidatorKind;
import biz.uro.CSVDataConverter.swing.json.JSONReader;
import biz.uro.CSVDataConverter.swing.json.JSONWriter;

public abstract class ValidatorKind_Equals extends ValidatorKind {

	private static final String REVERSE = "Reverse";
	
	protected final boolean mReverse;
	
	@Override
	public void jsonMapping( JSONWriter writer ) {
		super.jsonMapping( writer );
		writer.add( REVERSE, mReverse );
	}
	
	public ValidatorKind_Equals( JSONReader reader ) {
		super( reader );
		mReverse = reader.get( REVERSE ).asBoolean();
	}
	
	public ValidatorKind_Equals( InputKind kind, boolean reverse ) {
		super( ( reverse ? "Not" : "" ) + "Equals", kind );
		mReverse = reverse;
	}

	@Override
	public String toString() {
		if ( mReverse ) {
			return "次の値に等しくない";
		}
		return "次の値に等しい";
	}

	/* ==============================================================
	 * implements --Number--
	 * ============================================================== */
	public static class _Number extends ValidatorKind_Equals {

		public _Number( InputKind kind, boolean reverse ) {
			super( kind, reverse );
		}

		@Override
		protected ValidatorCondition createConditioner() {
			return new ValidatorCondition_Equals._Number( mReverse );
		}
		
	}
	
	/* ==============================================================
	 * implements --String--
	 * ============================================================== */

	public static class _String extends ValidatorKind_Equals {

		public _String( boolean reverse ) {
			super( InputKind.STRING, reverse );
		}

		@Override
		protected ValidatorCondition createConditioner() {
			return new ValidatorCondition_Equals._String( mReverse );
		}
		
	}
	
}
