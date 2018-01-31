package biz.uro.CSVDataConverter.database;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import biz.uro.CSVDataConverter.database.element.ElementParser;
import biz.uro.CSVDataConverter.generator.ProgramGenerator_UseTable;
import biz.uro.CSVDataConverter.io.csv.CSVData;
import biz.uro.CSVDataConverter.io.csv.CSVState;
import biz.uro.CSVDataConverter.swing.builder.TableBuilder;

public abstract class Table {

	private final TableBuilder mBuilder;
	private final String mPackagePath;
	private final String mName;
	private final ProgramGenerator_UseTable mGenerator;
	private final LinkedHashMap<PrimaryKeyRecord,Record> mRecordMap = new LinkedHashMap<>();
	private final ArrayList<Record> mRecordList = new ArrayList<>();
	private Schema mSchema;

	private ElementParser._ForeignKey mDefaultParser;
	
	public abstract String parseRecordName( Record record );
	public abstract String parseComment( Record record );
	
	public Table( TableBuilder builder ) {
		mBuilder = builder;
		mPackagePath = mBuilder.getPackage().getPackage();
		mName = mBuilder.getPackage().getClassName();
		mGenerator = mBuilder.getRecordDefinitionType().build();
		builder.setBuildedInstance( this );
	}
	
	public String getPackagePath() {
		return mPackagePath;
	}
	
	public String getName() {
		return mName;
	}
	
	public Schema getSchema() {
		return mSchema;
	}
	
	public int getRecordsCount() {
		return mRecordMap.size();
	}
	
	public Record getRecord( int index ) {
		return mRecordList.get( index );
	}
	
	public ProgramGenerator_UseTable getGenerator() {
		return mGenerator;
	}
	
	public Record getRecord( PrimaryKeyRecord primaryKeyRecord ) {
		for ( Map.Entry<PrimaryKeyRecord,Record> entry : mRecordMap.entrySet() ) {
			final PrimaryKeyRecord key = entry.getKey();
			if ( key.equals( primaryKeyRecord ) ) {
				return entry.getValue();
			}
		}
		return null;
	}
	
	protected void addRecord( Record record ) {
		mRecordMap.put( record.getPrimaryRecord(), record );
		mRecordList.add( record );
	}
	
	protected void clearRecord() {
		mRecordMap.clear();
		mRecordList.clear();
	}
	
	public void init() {
		mSchema = mBuilder.getSchema().build();		
	}
	
	public void firstLoad( CSVData csvData ) {
		clearRecord();
		final Schema schema = getSchema();
		final int csvLineCount = csvData.getSize();
		for ( int i = 0; i < csvLineCount; i++ ) {
			final CSVState csvState = new CSVState( csvData.createTokenizer( i ) );
			try {
				//primaryKeyのみロード
				final Record record = new Record( schema );
				final PrimaryKeyRecord primaryKey = schema.getPrimaryKey().read( this, csvState );
				record.addValues( primaryKey );
				addRecord( record );
			}
			catch ( RuntimeException e ) {
				final String message = mName + "(" + i + "," + csvState.getTokenPointer() + ") " + e.getMessage();
				Logger.getLogger( getClass().getSimpleName() ).severe( message );
				throw new RuntimeException( message );
			}
		}
	}

	public void secondload( CSVData csvData ) {
		final Schema schema = getSchema();
		final int csvLineCount = csvData.getSize();
		for ( int i = 0; i < csvLineCount; i++ ) {
			final CSVState csvState = new CSVState( csvData.createTokenizer( i ) );
			try {
				final Record record = getRecord( i );
				record.clear();
				csvState.reset();
				schema.read( record, this, csvState );
			}
			catch ( RuntimeException e ) {
				final String message = mName + "(" + i + "," + csvState.getTokenPointer() + ") " + e.getMessage();
				Logger.getLogger( getClass().getSimpleName() ).severe( message );
				throw new RuntimeException( message );
			}
		}
	}
	
	@Override
	public int hashCode() {
		return Objects.hash( mPackagePath, mName );
	};
	
	@Override
	public boolean equals( Object obj ) {
		if ( !( obj instanceof Table ) ) {
			return false;
		}
		final Table other = ( Table )obj;
		if ( mPackagePath != null ) {
			if ( !mPackagePath.equals( other.mPackagePath ) ) {
				return false;
			}
		}
		else if ( other.mPackagePath != null ) {
			return false;
		}
		if ( mName != null ) {
			if( !mName.equals( other.mName ) ) {
				return false;
			}
		}
		else if ( other.mName != null ) {
			return false;
		}
		return true;
	}
	
	public void setDefaultParser( ElementParser._ForeignKey parser ) {
		mDefaultParser = parser;
	}
	
	public ElementParser._ForeignKey getDefaultParser() {
		return mDefaultParser;
	}
	
}
