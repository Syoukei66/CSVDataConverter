package biz.uro.CSVDataConverter.swing.builder.validator;

import biz.uro.CSVDataConverter.swing.builder.validator.kind.ValidatorKind;
import biz.uro.CSVDataConverter.swing.builder.validator.kind.ValidatorKind.InputKind;

public enum ValidatorKindEnum_Static {
	NULL						( ValidatorKind._Null() ),
	INT_NUMBER_INRANGE			( ValidatorKind._Number_InRange		( InputKind.INTEGER ) ),
	INT_NUMBER_OUTRANGE			( ValidatorKind._Number_OutRange	( InputKind.INTEGER ) ),
	INT_NUMBER_EQUALS			( ValidatorKind._Number_Equals		( InputKind.INTEGER ) ),
	INT_NUMBER_NOTEQUALS		( ValidatorKind._Number_NotEquals	( InputKind.INTEGER ) ),
	INT_NUMBER_GREATER			( ValidatorKind._Number_Greater		( InputKind.INTEGER ) ),
	INT_NUMBER_LESS				( ValidatorKind._Number_Less		( InputKind.INTEGER ) ),
	INT_NUMBER_GREATER_EQUALS	( ValidatorKind._Number_GreaterEquals( InputKind.INTEGER ) ),
	INT_NUMBER_LESS_EQUALS		( ValidatorKind._Number_LessEquals	( InputKind.INTEGER ) ),
	FLOAT_NUMBER_INRANGE		( ValidatorKind._Number_InRange		( InputKind.FLOAT ) ),
	FLOAT_NUMBER_OUTRANGE		( ValidatorKind._Number_OutRange	( InputKind.FLOAT ) ),
	FLOAT_NUMBER_EQUALS			( ValidatorKind._Number_Equals		( InputKind.FLOAT ) ),
	FLOAT_NUMBER_NOTEQUALS		( ValidatorKind._Number_NotEquals	( InputKind.FLOAT ) ),
	FLOAT_NUMBER_GREATER		( ValidatorKind._Number_Greater		( InputKind.FLOAT ) ),
	FLOAT_NUMBER_LESS			( ValidatorKind._Number_Less		( InputKind.FLOAT ) ),
	FLOAT_NUMBER_GREATER_EQUALS	( ValidatorKind._Number_GreaterEquals( InputKind.FLOAT ) ),
	FLOAT_NUMBER_LESS_EQUALS	( ValidatorKind._Number_LessEquals	( InputKind.FLOAT ) ),
	STRING_NUMBER_INRANGE		( ValidatorKind._String_InRange() ),
	STRING_NUMBER_OUTRANGE		( ValidatorKind._String_OutRange() ),
	STRING_NUMBER_EQUALS		( ValidatorKind._String_Equals() ),
	STRING_NUMBER_NOTEQUALS		( ValidatorKind._String_NotEquals() ),
	STRING_NUMBER_GREATER		( ValidatorKind._String_Greater() ),
	STRING_NUMBER_LESS			( ValidatorKind._String_Less() ),
	STRING_NUMBER_GREATER_EQUALS( ValidatorKind._String_GreaterEquals() ),
	STRING_NUMBER_LESS_EQUALS	( ValidatorKind._String_LessEquals() ),
	BOOLEAN_LIST				( ValidatorKind._Boolean() )
	;
	
	public static final ValidatorKindEnum_Static[] VALUES = values();
	public static final ValidatorKindEnum_Static[] INT_VALUES = new ValidatorKindEnum_Static[] {
		NULL,
		INT_NUMBER_INRANGE,
		INT_NUMBER_OUTRANGE,
		INT_NUMBER_EQUALS,
		INT_NUMBER_NOTEQUALS,
		INT_NUMBER_GREATER,
		INT_NUMBER_LESS,
		INT_NUMBER_GREATER_EQUALS,
		INT_NUMBER_LESS_EQUALS
	};
	public static final ValidatorKindEnum_Static[] FLOAT_VALUES = new ValidatorKindEnum_Static[] {
		NULL,
		FLOAT_NUMBER_INRANGE,
		FLOAT_NUMBER_OUTRANGE,
		FLOAT_NUMBER_EQUALS,
		FLOAT_NUMBER_NOTEQUALS,
		FLOAT_NUMBER_GREATER,
		FLOAT_NUMBER_LESS,
		FLOAT_NUMBER_GREATER_EQUALS,
		FLOAT_NUMBER_LESS_EQUALS		
	};
	public static final ValidatorKindEnum_Static[] STRING_VALUES = new ValidatorKindEnum_Static[] {
		NULL,
		STRING_NUMBER_INRANGE,
		STRING_NUMBER_OUTRANGE,
		STRING_NUMBER_EQUALS,
		STRING_NUMBER_NOTEQUALS,
		STRING_NUMBER_GREATER,
		STRING_NUMBER_LESS,
		STRING_NUMBER_GREATER_EQUALS,
		STRING_NUMBER_LESS_EQUALS		
	};

	
	private ValidatorKind mInstance;
	ValidatorKindEnum_Static( ValidatorKind instance ) {
		mInstance = instance;
	}
	
	public ValidatorKind get() {
		return mInstance;
	}
}
