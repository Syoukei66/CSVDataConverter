package biz.uro.CSVDataConverter.swing.builder.validator.kind.number;

import biz.uro.CSVDataConverter.swing.builder.validator.condition.ValidatorCondition;
import biz.uro.CSVDataConverter.swing.builder.validator.condition.number.ValidatorCondition_Compare;
import biz.uro.CSVDataConverter.swing.builder.validator.kind.ValidatorKind;
import biz.uro.CSVDataConverter.swing.json.JSONReader;
import biz.uro.CSVDataConverter.swing.json.JSONWriter;

public abstract class ValidatorKind_Compare extends ValidatorKind {

	private static final String GREATER = "Greater";
	
	protected final boolean mGreater;

	@Override
	public void jsonMapping( JSONWriter writer ) {
		super.jsonMapping( writer );
		writer.add( GREATER, mGreater );
	}
	
	public ValidatorKind_Compare( JSONReader reader ) {
		super( reader );
		mGreater = reader.get( GREATER ).asBoolean();
	}
	
	public ValidatorKind_Compare( InputKind kind, boolean greater ) {
		super( greater ? "Greater" : "Less", kind );
		mGreater = greater;
	}
	
	@Override
	public String toString() {
		if ( mGreater ) {
			return "次の値より大きい";
		}
		return "次の値より小さい";
	}
	
	/* ==============================================================
	 * implements --Number--
	 * ============================================================== */

	public static class _Number extends ValidatorKind_Compare {

		public _Number( InputKind kind, boolean greater ) {
			super( kind, greater );
		}

		@Override
		public ValidatorCondition createConditioner() {
			return new ValidatorCondition_Compare._Number( mGreater );
		}

	}
	
	/* ==============================================================
	 * implements --String--
	 * ============================================================== */

	public static class _String extends ValidatorKind_Compare {

		public _String( boolean greater ) {
			super( InputKind.STRING, greater );
		}

		@Override
		public ValidatorCondition createConditioner() {
			return new ValidatorCondition_Compare._String( mGreater );
		}

	}

}
