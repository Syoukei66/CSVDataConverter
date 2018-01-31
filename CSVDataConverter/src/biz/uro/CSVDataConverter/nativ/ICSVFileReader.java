package biz.uro.CSVDataConverter.nativ;

import java.util.ArrayList;

public abstract class ICSVFileReader {
	
	protected ArrayList<String> mLines = new ArrayList<>();
	
	public abstract void load();
	
	public ICSVTokenizer createTokenizer( int lineIndex ) {
		return new ICSVTokenizer( mLines.get( lineIndex ) );
	}
	
	public int getSize() {
		return mLines.size();
	}
	
}