package biz.uro.CSVDataConverter.nativ;

public class ICSVTokenizer {
	
	private final String[] mTokens;
	private int mTokenPointer;
	
	public ICSVTokenizer( String csvLine ) {
		mTokens = csvLine.split( ",", -1 );
	}

	public boolean hasNextToken() {
		return mTokenPointer < mTokens.length;
	}
	
	public String nextToken() {
		final String ret = mTokens[mTokenPointer];
		mTokenPointer ++;
		return ret;
	}
	
	public void skip( int num ) {
		mTokenPointer += num;
	}
	
}