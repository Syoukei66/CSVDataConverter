package biz.uro.CSVDataConverter.swing.builder.validator.kind;

import org.apache.commons.lang3.StringUtils;

import biz.uro.CSVDataConverter.swing.builder.TableBuilder;
import biz.uro.CSVDataConverter.swing.builder.validator.Validator;
import biz.uro.CSVDataConverter.swing.builder.validator.condition.ValidatorCondition;
import biz.uro.CSVDataConverter.swing.builder.validator.kind.list.ValidatorKind_Boolean;
import biz.uro.CSVDataConverter.swing.builder.validator.kind.list.ValidatorKind_Table;
import biz.uro.CSVDataConverter.swing.builder.validator.kind.number.ValidatorKind_Compare;
import biz.uro.CSVDataConverter.swing.builder.validator.kind.number.ValidatorKind_CompareEquals;
import biz.uro.CSVDataConverter.swing.builder.validator.kind.number.ValidatorKind_Equals;
import biz.uro.CSVDataConverter.swing.builder.validator.kind.number.ValidatorKind_Range;
import biz.uro.CSVDataConverter.swing.json.IJSONDataModel;
import biz.uro.CSVDataConverter.swing.json.JSONReader;
import biz.uro.CSVDataConverter.swing.json.JSONWriter;

public abstract class ValidatorKind implements IJSONDataModel {
	
	public enum InputKind {
		ALL		( "All" ),
		INTEGER	( "Integer" ),
		FLOAT	( "Float" ),
		LIST	( "List" ),
		STRING	( "String" ),
		;
		static final InputKind[] VALUES = values();
		static InputKind getInstanceByID( String id ) {
			for ( InputKind kind : InputKind.VALUES ) {
				if ( kind.mID.equals( id )) {
					return kind;
				}
			}
			return null;
		}
		final String mID;
		InputKind( String id ) {
			mID = id;
		}
		public String getID() {
			return mID;
		}
	}
	
	private static final String NAME = "Name";
	private static final String INPUT_KIND = "InputKind";
	
	private final InputKind mKind;
	private String mName;
	
	@Override
	public void jsonMapping( JSONWriter writer ) {
		writer.add( NAME, mName );
		writer.add( INPUT_KIND, mKind.mID );
	}
	
	public ValidatorKind( JSONReader reader ) {
		mName = reader.get( NAME ).asText();
		final String id = reader.get( INPUT_KIND ).asText();
		mKind = InputKind.getInstanceByID( id );
	}
	
	public ValidatorKind( String name, InputKind kind ) {
		mName = name;
		mKind = kind;
	}

	@Override
	public boolean isEditable() {
		return false;
	}
	
	protected abstract ValidatorCondition createConditioner();
	
	public Validator create() {
		return new Validator( this, createConditioner() );
	}
	
	public InputKind getInputKind() {
		return mKind;
	}
	
	public void setName( String name ) {
		mName = name;
	}

	@Override
	public String jsonHashCode() {
		return "ValidatorKind_" + mKind.mID + "_" + StringUtils.capitalize( mName );
	}
	
	/* ==============================================================
	 * implements
	 * ============================================================== */

	public static ValidatorKind _Null() {
		return new ValidatorKind_Null();
	}
	
	/* ==============================================================
	 * implements --Number--
	 * ============================================================== */
	
	public static ValidatorKind _Number_InRange( InputKind kind ) {
		return new ValidatorKind_Range._Number( kind, true );
	}

	public static ValidatorKind _Number_OutRange( InputKind kind ) {
		return new ValidatorKind_Range._Number( kind, false );
	}
	
	public static ValidatorKind _Number_Equals( InputKind kind ) {
		return new ValidatorKind_Equals._Number( kind, false );
	}

	public static ValidatorKind _Number_NotEquals( InputKind kind ) {
		return new ValidatorKind_Equals._Number( kind, true );
	}

	public static ValidatorKind _Number_Greater( InputKind kind ) {
		return new ValidatorKind_Compare._Number( kind, true );
	}

	public static ValidatorKind _Number_Less( InputKind kind ) {
		return new ValidatorKind_Compare._Number( kind, false );
	}

	public static ValidatorKind _Number_GreaterEquals( InputKind kind ) {
		return new ValidatorKind_CompareEquals._Number( kind, true );
	}

	public static ValidatorKind _Number_LessEquals( InputKind kind ) {
		return new ValidatorKind_CompareEquals._Number( kind, false );
	}

	/* ==============================================================
	 * implements --String--
	 * ============================================================== */

	public static ValidatorKind _String_InRange() {
		return new ValidatorKind_Range._String( true );
	}

	public static ValidatorKind _String_OutRange() {
		return new ValidatorKind_Range._String( false );
	}
	
	public static ValidatorKind _String_Equals() {
		return new ValidatorKind_Equals._String( false );
	}

	public static ValidatorKind _String_NotEquals() {
		return new ValidatorKind_Equals._String( true );
	}

	public static ValidatorKind _String_Greater() {
		return new ValidatorKind_Compare._String( true );
	}

	public static ValidatorKind _String_Less() {
		return new ValidatorKind_Compare._String( false );
	}

	public static ValidatorKind _String_GreaterEquals() {
		return new ValidatorKind_CompareEquals._String( true );
	}

	public static ValidatorKind _String_LessEquals() {
		return new ValidatorKind_CompareEquals._String( false );
	}

	/* ==============================================================
	 * implements --List--
	 * ============================================================== */

	public static ValidatorKind _Boolean() {
		return new ValidatorKind_Boolean();
	}
	
	public static ValidatorKind _List_Table( TableBuilder table ) {
		return new ValidatorKind_Table( table );
	}

}
