package biz.uro.CSVDataConverter.swing.builder;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;

import biz.uro.CSVDataConverter.database.Record;
import biz.uro.CSVDataConverter.database.Table;
import biz.uro.CSVDataConverter.generator.RecordDefinitionType;
import biz.uro.CSVDataConverter.generator.table.TableGeneratorConstants;
import biz.uro.CSVDataConverter.swing.builder.parser.RecordGrammer;
import biz.uro.CSVDataConverter.swing.json.IJSONDataModel;
import biz.uro.CSVDataConverter.swing.json.JSONReader;
import biz.uro.CSVDataConverter.swing.json.JSONWriter;
import biz.uro.CSVDataConverter.swing.old.JSONListModel;

public class TableBuilder implements IJSONDataModel {

	@SuppressWarnings("serial")
	static class TableException extends RuntimeException {
		public TableException( String comment, Throwable cause ) {
			super( comment, cause );
		}
	}
	
	//VBA用
	private static final String NAME 					= "Name";
	
	//プロジェクト用
	private static final String SCHEMA 					= "Schema";
	private static final String LOGIC_ATTRBUTE_INDEX	= "LogicAttributeIndex";
	private static final String PROGRAM_FORMAT 			= "ProgramFormat";
	private static final String COMMENT_FORMAT 			= "CommentFormat";
	private static final String RECORD_DEFINITION_TYPE_ID	= "RecordDefinitionTypeID";

	private static final String PACKAGE_HASH_CODE		= "PackageHashCode";
	private static final String TYPE_HASH_CODE 			= "TypeHashCode";
	private static final String STATEMENT_HASH_CODE		= "StatementHashCode";
	
	private final SchemaBuilder mSchemaBuilder;
	private ElementBuilder mLogicElement;	//外部から見た時に参照されるキー
	
	private String mProgramFormat;
	private String mCommentFormat;
	private RecordDefinitionType mRecordType;
	
	//HashCode群
	private final PackageBuilder mPackage;
	private ElementTypeBuilder mType;
	private StatementBuilder mStatement;
	
	//
	private Table mBuildedInstance;
	private boolean mIsBuildable;
	
	@Override
	public void jsonMapping( JSONWriter writer ) {
		writer.add( NAME, mPackage.getClassName() );
		writer.add( PACKAGE_HASH_CODE, mPackage.jsonHashCode() );
		writer.add( SCHEMA, mSchemaBuilder );
		//RogicElementのIndexをやろうよ
		//RogicElementが何番目のElementなのか調べる
		//nullなら0
		final JSONListModel<ElementBuilder> elementModel = mSchemaBuilder.getElementDataModel();
		final int elementCount = elementModel.size();
		int logicAttributeIndex = 0;
		for ( int i = 0; i < elementCount; i++ ) {
			if ( elementModel.get( i ) == mLogicElement ) {
				logicAttributeIndex = i;
				break;
			}
		}
		writer.add( LOGIC_ATTRBUTE_INDEX, logicAttributeIndex );
		writer.add( PROGRAM_FORMAT, mProgramFormat );
		writer.add( COMMENT_FORMAT, mCommentFormat );
		writer.add( TYPE_HASH_CODE, mType.jsonHashCode() );
		writer.add( STATEMENT_HASH_CODE, mStatement.jsonHashCode() );
		writer.add( RECORD_DEFINITION_TYPE_ID, mRecordType.id );
	}
	
	public TableBuilder( JSONReader reader ) {
		mPackage = reader.get( PACKAGE_HASH_CODE ).pullObject();
		mSchemaBuilder = reader.get( SCHEMA ).asObject( SchemaBuilder.class );
		final int logicAttributeIndex = reader.get( LOGIC_ATTRBUTE_INDEX ).asInt();
		final JSONListModel<ElementBuilder> elementModel = mSchemaBuilder.getElementDataModel();
		if ( elementModel.size() > logicAttributeIndex ) {
			mLogicElement = elementModel.get( logicAttributeIndex );			
		}
		else {
			mLogicElement = null;
		}
		mProgramFormat = reader.get( PROGRAM_FORMAT ).asText();
		mCommentFormat = reader.get( COMMENT_FORMAT ).asText();
		mType = reader.get( TYPE_HASH_CODE ).pullObject();
		mStatement = reader.get( STATEMENT_HASH_CODE ).pullObject();
		mRecordType = RecordDefinitionType.getInstanceByID( reader.get( RECORD_DEFINITION_TYPE_ID ).asText() );
		onUpdateData();
	}

	public TableBuilder( PackageBuilder packaqe ) {
		mPackage = packaqe;
		mSchemaBuilder = new SchemaBuilder();
		mRecordType = RecordDefinitionType.USE_CSV_ALL;
		onUpdateData();
	}
	
	public PackageBuilder getPackage() {
		return mPackage;
	}

	public SchemaBuilder getSchemaBuilder() {
		return mSchemaBuilder;
	}

	public String getProgramFormat() {
		return mProgramFormat;
	}

	public void setProgramFormat( String programFormat ) {
		mProgramFormat = programFormat;
		onUpdateData();
	}

	public String getCommentFormat() {
		return mCommentFormat;
	}

	public void setCommentFormat( String commentFormat ) {
		mCommentFormat = commentFormat;
		onUpdateData();
	}

	public void setType( ElementTypeBuilder._ForeignTable type ) {
		mType = type;
		onUpdateData();
	}
	
	public ElementTypeBuilder getType() {
		return mType;
	}
	
	public void setRecordDefinitionType( RecordDefinitionType type ) {
		mRecordType = type;
		onUpdateData();
	}
	
	public RecordDefinitionType getRecordDefinitionType() {
		return mRecordType;
	}
	
	public void setStatement( StatementBuilder._ForeignTable statement ) {
		mStatement = statement;
		onUpdateData();
	}
	
	public StatementBuilder getStatement() {
		return mStatement;
	}
	
	public void setLogicElement( int index ) {
		mLogicElement = mSchemaBuilder.getElementDataModel().get( index );
		onUpdateData();
	}
	
	public void setLogicElement( ElementBuilder element ) {
		mLogicElement = element;
		onUpdateData();
	}
		
	public ElementBuilder getLogicElement() {
		return mLogicElement;
	}
	
	public boolean isBuildable() {
		return mIsBuildable;
	}
	
	public void onUpdateData() {
		mIsBuildable = false;
		if ( mRecordType == null ) {
			return;
		}
		if ( mRecordType == RecordDefinitionType.NO_CSV ) {
			mIsBuildable = true;
			return;
		}
		if ( mRecordType == RecordDefinitionType.USE_CSV_ALL ) {
			if ( mProgramFormat == null || mProgramFormat.length() == 0 ) {
				return;
			}
			mIsBuildable = true;
			return;
		}
		if ( mRecordType == RecordDefinitionType.USE_CSV_PRIMARY_KEY ) {
			if ( mProgramFormat == null || mProgramFormat.length() == 0 ) {
				return;
			}
			mIsBuildable = true;
			return;			
		}
		if ( mRecordType == RecordDefinitionType.USE_CSV_NULL ) {
			if ( mProgramFormat == null || mProgramFormat.length() == 0 ) {
				return;
			}
			mIsBuildable = true;
			return;			
		}
	}
	
	public Table build() {
		return new Table( this ) {
			@Override
			public String parseRecordName( Record record ) {
				if ( record.isEmpty() ) {
					return null;
				}
				if ( mRecordType == RecordDefinitionType.NO_CSV ) {
					//TODO:recordの中からPrimaryKeyを抜き取って、それをgetするメソッドを生成する
					return TableGeneratorConstants.TABLE_GETTER_METHOD_NAME;
				}
				try {
					RecordGrammer parser = new RecordGrammer( 
							new ByteArrayInputStream( mProgramFormat.getBytes( "utf-8" ) ) );
					return parser.parseRecord( this, record );
				} catch ( UnsupportedEncodingException e ) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			public String parseComment( Record record ) {
				if ( mCommentFormat == null || mCommentFormat.length() == 0 ) {
					return null;
				}
				RecordGrammer parser;
				try {
					parser = new RecordGrammer( 
							new ByteArrayInputStream( mCommentFormat.getBytes( "utf-8" ) ) );
					return parser.parseRecord( this, record );
				} catch ( UnsupportedEncodingException e ) {
					e.printStackTrace();
				}
				return null;
			}
		};
	}
	
	public void setBuildedInstance( Table table ) {
		mBuildedInstance = table;
	}
	
	public Table getBuildedInstance() {
		return mBuildedInstance;
	}
	
	public SchemaBuilder getSchema() {
		return mSchemaBuilder;
	}
	
	@Override
	public String toString() {
		final String buildable = mIsBuildable ? "○" : "×";
		return String.format( "%-32s %-44s %s", mPackage.getClassName(), "[" + mPackage.getPackage() + "]", buildable );
	}

	@Override
	public boolean isEditable() {
		return true;
	}

	@Override
	public String jsonHashCode() {
		return Integer.toString( hashCode() );
	}

}
