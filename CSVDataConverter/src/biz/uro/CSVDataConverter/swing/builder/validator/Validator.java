package biz.uro.CSVDataConverter.swing.builder.validator;

import biz.uro.CSVDataConverter.swing.builder.validator.condition.ValidatorCondition;
import biz.uro.CSVDataConverter.swing.builder.validator.condition.ValidatorCondition_Null;
import biz.uro.CSVDataConverter.swing.builder.validator.condition.list.ValidatorCondition_Boolean;
import biz.uro.CSVDataConverter.swing.builder.validator.condition.list.ValidatorCondition_Factory;
import biz.uro.CSVDataConverter.swing.builder.validator.condition.list.ValidatorCondition_Table;
import biz.uro.CSVDataConverter.swing.builder.validator.condition.number.ValidatorCondition_Compare;
import biz.uro.CSVDataConverter.swing.builder.validator.condition.number.ValidatorCondition_CompareEquals;
import biz.uro.CSVDataConverter.swing.builder.validator.condition.number.ValidatorCondition_Equals;
import biz.uro.CSVDataConverter.swing.builder.validator.condition.number.ValidatorCondition_Range;
import biz.uro.CSVDataConverter.swing.builder.validator.kind.ValidatorKind;
import biz.uro.CSVDataConverter.swing.json.IJSONObject;
import biz.uro.CSVDataConverter.swing.json.JSONReader;
import biz.uro.CSVDataConverter.swing.json.JSONWriter;
import biz.uro.CSVDataConverter.swing.json.JSONObjectMap.IJSONObjectLoadedListener;

public class Validator implements IJSONObject {

	private static final String KIND_HASHCODE = "ValidatorKindHashCode";
	private static final String INPUT_KIND = "InputKind";
	private static final String CONDITION = "Condition";
	
	private ValidatorKind mKind;
	private final ValidatorCondition mCondition;

	@Override
	public void jsonMapping( JSONWriter writer ) {
		writer.add( KIND_HASHCODE, mKind.jsonHashCode() );
		writer.add( INPUT_KIND, mKind.getInputKind().getID() );	//ExcelAdd-Inにて使用
		writer.add( CONDITION, mCondition );
	}
	
	public Validator( JSONReader reader ) {
		reader.get( KIND_HASHCODE ).reserveObject( new IJSONObjectLoadedListener() {
			@Override
			public void onLoaded( Object obj ) {
				mKind = (ValidatorKind) obj;
			}
		});
		mCondition = reader.get( CONDITION ).asAbstractObject( ValidatorCondition.class )
				.addImplements( ValidatorCondition_Null.class )
				.addImplements( ValidatorCondition_Range._Number.class )
				.addImplements( ValidatorCondition_Range._String.class )
				.addImplements( ValidatorCondition_Equals._Number.class )
				.addImplements( ValidatorCondition_Equals._String.class )
				.addImplements( ValidatorCondition_Compare._Number.class )
				.addImplements( ValidatorCondition_Compare._String.class )
				.addImplements( ValidatorCondition_CompareEquals._Number.class )
				.addImplements( ValidatorCondition_CompareEquals._String.class )
				.addImplements( ValidatorCondition_Boolean.class )
				.addImplements( ValidatorCondition_Table.class )
				.addImplements( ValidatorCondition_Factory.class )
				.build();
	}

	public Validator( ValidatorKind kind, ValidatorCondition conditioner ) {
		mKind = kind;
		mCondition = conditioner;
	}
	
	public ValidatorKind getKind() {
		return mKind;
	}
	
	public ValidatorCondition getCondition() {
		return mCondition;
	}
	
	public String getComment() {
		try {
			return mCondition.getComment();			
		}
		catch ( IllegalArgumentException e ) {
			return "Error";
		}		
	}
	
	@Override
	public String toString() {
		try {
			return mCondition.toString();
		}
		catch ( IllegalArgumentException e ) {
			return "Error";
		}				
	}

	@Override
	public String jsonHashCode() {
		return null;
	}

}
