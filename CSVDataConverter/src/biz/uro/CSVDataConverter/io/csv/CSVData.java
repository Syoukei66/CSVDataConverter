package biz.uro.CSVDataConverter.io.csv;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import biz.uro.CSVDataConverter.io.TextFileReader;

public class CSVData {
	
	private TextFileReader mLoader;
	private ArrayList<String> mLines;
	
	public CSVData( String fileName ) {
		mLoader = new TextFileReader( fileName, "csv" );
		mLines = new ArrayList<>();
	}

	public void load() {
		try {
			mLoader.open();
			while ( mLoader.hasNext() ) {
				mLines.add( mLoader.getLine() );
			}
			mLoader.close();
		} catch ( FileNotFoundException e ) {
			e.printStackTrace();
		} catch ( IOException e ) {
			e.printStackTrace();
		}
	}

	public void clear() {
		mLoader = null;
		mLines.clear();
	}
	
	public CSVLineReader createTokenizer( int lineIndex ) {
		return new CSVLineReader( mLines.get( lineIndex ) );
	}
	
	public int getSize() {
		return mLines.size();
	}

}
