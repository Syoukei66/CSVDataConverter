package biz.uro.CSVDataConverter.io.csv;

import java.util.HashMap;

public class CSVLineReader implements ICSVLineReader {

	private final String mOrginalText;
	private final String[] mTokens;
	private final HashMap<Object, Integer> mAnchorMap = new HashMap<>();
	
	private int mTokenPointer;
	
	public CSVLineReader( String csvLine ) {
		mOrginalText = csvLine;
		mTokens = csvLine.split( ",", -1 );
	}
	
	@Override
	public void reset() {
		mTokenPointer = 0;
		mAnchorMap.clear();
	}
	
	@Override
	public String getOrginalText() {
		return mOrginalText;
	}
	
	@Override
	public int countToken() {
		return mTokens.length;
	}
	
	@Override
	public int getTokenPointer() {
		return mTokenPointer;
	}
	
	@Override
	public boolean hasNextToken() {
		return mTokenPointer < mTokens.length;
	}
	
	@Override
	public String nextToken() {
		final String ret = mTokens[mTokenPointer];
		mTokenPointer ++;
		return ret;
	}
	
	@Override
	public void createAnchor( Object key ) {
		mAnchorMap.put( key, mTokenPointer );
	}
	
	@Override
	public void returnAnchor( Object key ) {
		final Integer pointer = mAnchorMap.get( key );
		if ( pointer == null ) {
			return;
		}
		mTokenPointer = pointer;
	}
	
	@Override
	public boolean isExistAnchor( Object key ) {
		return mAnchorMap.containsKey( key );
	}
	
}
