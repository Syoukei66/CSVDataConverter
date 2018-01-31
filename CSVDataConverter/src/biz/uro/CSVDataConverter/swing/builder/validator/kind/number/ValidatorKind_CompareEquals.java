package biz.uro.CSVDataConverter.swing.builder.validator.kind.number;

import biz.uro.CSVDataConverter.swing.builder.validator.condition.ValidatorCondition;
import biz.uro.CSVDataConverter.swing.builder.validator.condition.number.ValidatorCondition_CompareEquals;
import biz.uro.CSVDataConverter.swing.builder.validator.kind.ValidatorKind;
import biz.uro.CSVDataConverter.swing.json.JSONReader;
import biz.uro.CSVDataConverter.swing.json.JSONWriter;

public abstract class ValidatorKind_CompareEquals extends ValidatorKind {

	private static final String GREATER = "Greater";
	
	protected final boolean mGreater;
	
	@Override
	public void jsonMapping( JSONWriter writer ) {
		super.jsonMapping( writer );
		writer.add( GREATER, mGreater );
	}
	
	public ValidatorKind_CompareEquals( JSONReader reader ) {
		super( reader );
		mGreater = reader.get( GREATER ).asBoolean();
	}
	
	public ValidatorKind_CompareEquals( InputKind kind, boolean greater ) {
		super( ( greater ? "Greater" : "Less" ) + "Equals", kind );
		mGreater = greater;
	}
	
	@Override
	public String toString() {
		if ( mGreater ) {
			return "次の値以上";
		}
		return "次の値以下";
	}

	/* ==============================================================
	 * implements --Number--
	 * ============================================================== */
	
	public static class _Number extends ValidatorKind_CompareEquals {

		public _Number( InputKind kind, boolean greater ) {
			super( kind, greater );
		}
		
		@Override
		public ValidatorCondition createConditioner() {
			return new ValidatorCondition_CompareEquals._Number( mGreater );
		}
		
	}
	
	/* ==============================================================
	 * implements --String--
	 * ============================================================== */
	
	public static class _String extends ValidatorKind_CompareEquals {

		public _String( boolean greater ) {
			super( InputKind.STRING, greater );
		}
		
		@Override
		public ValidatorCondition createConditioner() {
			return new ValidatorCondition_CompareEquals._String( mGreater );
		}
		
	}

}
