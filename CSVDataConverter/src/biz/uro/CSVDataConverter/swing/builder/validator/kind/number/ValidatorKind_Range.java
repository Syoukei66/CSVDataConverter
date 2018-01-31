package biz.uro.CSVDataConverter.swing.builder.validator.kind.number;

import biz.uro.CSVDataConverter.swing.builder.validator.condition.ValidatorCondition;
import biz.uro.CSVDataConverter.swing.builder.validator.condition.number.ValidatorCondition_Range;
import biz.uro.CSVDataConverter.swing.builder.validator.kind.ValidatorKind;
import biz.uro.CSVDataConverter.swing.json.JSONReader;
import biz.uro.CSVDataConverter.swing.json.JSONWriter;

public abstract class ValidatorKind_Range extends ValidatorKind {

	private static final String IN = "In";
	
	protected boolean mIn;
	
	@Override
	public void jsonMapping( JSONWriter writer ) {
		super.jsonMapping( writer );
		writer.add( IN, mIn );
	}
	
	public ValidatorKind_Range( JSONReader reader ) {
		super( reader );
		mIn = reader.get( IN ).asBoolean();
	}
	
	public ValidatorKind_Range( InputKind kind, boolean in ) {
		super( ( in ? "In" : "Out" ) + "Range", kind );
		mIn = in;
	}
	
	@Override
	public String toString() {
		if ( mIn ) {
			return "次の値の間";
		}
		return "次の値の間以外";
	}

	/* ==============================================================
	 * implements --Number--
	 * ============================================================== */
	
	public static class _Number extends ValidatorKind_Range {

		public _Number( InputKind kind, boolean in ) {
			super( kind, in );
		}
		
		@Override
		public ValidatorCondition createConditioner() {
			return new ValidatorCondition_Range._Number( mIn );
		}
	
	}
	
	/* ==============================================================
	 * implements --String--
	 * ============================================================== */

	public static class _String extends ValidatorKind_Range {

		public _String( boolean in ) {
			super( InputKind.STRING, in );
		}
		
		@Override
		public ValidatorCondition createConditioner() {
			return new ValidatorCondition_Range._String( mIn );
		}
	
	}

}
