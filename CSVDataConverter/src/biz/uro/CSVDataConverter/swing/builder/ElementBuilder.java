package biz.uro.CSVDataConverter.swing.builder;

import biz.uro.CSVDataConverter.database.element.Element;
import biz.uro.CSVDataConverter.swing.builder.validator.Validator;
import biz.uro.CSVDataConverter.swing.json.IJSONDataModel;
import biz.uro.CSVDataConverter.swing.json.JSONReader;
import biz.uro.CSVDataConverter.swing.json.JSONWriter;

public class ElementBuilder implements IJSONDataModel {

	private static final String SIZE = "Size";
	private static final String OUT_NAME = "OutName";
	private static final String FIELD_NAME = "FieldName";
	private static final String STATEMENT_HASH_CODE = "StatementHashCode";
	private static final String IS_PRIMARY_KEY = "IsPrimaryKey";
	private static final String IS_NOT_NULL = "IsNotNull";
	private static final String VALIDATOR = "Validator";

	private int mSize;
	private String mOutName;
	private String mFieldName;
	private StatementBuilder mStatement;
	private Validator mValidator;
	private boolean mIsPrimaryKey;
	private boolean mIsNotNull;

	@Override
	public void jsonMapping( JSONWriter writer ) {
		writer.add( SIZE,			mSize );
		writer.add( OUT_NAME,		mOutName );
		writer.add( FIELD_NAME,		mFieldName );
		writer.add( STATEMENT_HASH_CODE,	mStatement.jsonHashCode() );
		writer.add( IS_PRIMARY_KEY, mIsPrimaryKey );
		writer.add( IS_NOT_NULL,	mIsNotNull );
		writer.add( VALIDATOR,		mValidator );
	}
	
	public ElementBuilder( JSONReader reader ) {
		mSize = reader.get( SIZE ).asInt();
		mOutName = reader.get( OUT_NAME ).asText();
		mFieldName = reader.get( FIELD_NAME ).asText();
		mStatement = reader.get( STATEMENT_HASH_CODE ).pullObject();
//		reader.get( STATEMENT_HASH_CODE ).reserveObject( new IJSONObjectLoadedListener() {
//			@Override
//			public void onLoaded(Object obj) {
//				mStatement = (StatementBuilder)obj;
//			}
//		} );
		mIsPrimaryKey = reader.get( IS_PRIMARY_KEY ).asBoolean();
		mIsNotNull = reader.get( IS_NOT_NULL ).asBoolean();
		mValidator = reader.get( VALIDATOR ).asObject( Validator.class );		
	}

	public ElementBuilder() {
		
	}
	
	public Element build() {
		return new Element( this );
	}

	@Override
	public boolean isEditable() {
		return true;
	}

	public String getOutName() {
		return mOutName;
	}
	
	public void setOutName( String outName ) {
		mOutName = outName;
	}
	
	public String getFieldName() {
		return mFieldName;
	}
	
	public void setFieldName( String name ) {
		this.mFieldName = name;
	}

	public int getSize() {
		return mSize;
	}

	public void setSize( int size ) {
		this.mSize = size;
	}
	
	public StatementBuilder getStatement() {
		return mStatement;
	}
	
	public void setStatement( StatementBuilder statement ) {
		mStatement = statement;
	}
	
	public boolean isPrimaryKey() {
		return mIsPrimaryKey;
	}
	
	public void setIsPrimaryKey( boolean isPrimaryKey ) {
		this.mIsPrimaryKey = isPrimaryKey;
	}

	public boolean isNotNull() {
		return mIsNotNull;
	}
	
	public void setIsNotNull( boolean isNotNull ) {
		this.mIsNotNull = isNotNull;
	}
	
	public Validator getValidator() {
		return mValidator;
	}

	public void setValidator( Validator validator ) {
		mValidator = validator;
	}

	@Override
	public String toString() {
		final StringBuilder typeName = new StringBuilder();
		typeName.append( mStatement.getName() );
		if ( mSize > 1 ) {
			typeName.append( String.format( "[%d]", mSize ) );			
		}
		final String validatorComment = mValidator.getComment();
		if ( validatorComment != null && validatorComment.length() > 0 ) {
			typeName.append( "{" + validatorComment + "}" );
		}
		final StringBuilder options = new StringBuilder();
		if ( mIsPrimaryKey ) {
			options.append( "-PK " );
		}
		if ( mIsNotNull ) {
			options.append( "-NotNull " );
		}
		return String.format( "%-32s %-16s %-16s %-16s", typeName, options.toString(), mFieldName, mOutName );
	}

	@Override
	public String jsonHashCode() {
		return null;
	}
	
}
