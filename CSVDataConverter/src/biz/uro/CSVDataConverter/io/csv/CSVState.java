package biz.uro.CSVDataConverter.io.csv;

import java.util.ArrayList;

public class CSVState implements ICSVLineReader {

	private final ICSVLineReader mReader;
	private final ArrayList<String> mUseTokens = new ArrayList<>();
	
	public CSVState( ICSVLineReader reader ) {
		mReader = reader;
	}
	
	public void reset() {
		mReader.reset();
	}
	
	@Override
	public String getOrginalText() {
		return mReader.getOrginalText();
	}

	@Override
	public int countToken() {
		return mReader.countToken();
	}

	@Override
	public int getTokenPointer() {
		return mReader.getTokenPointer();
	}

	@Override
	public boolean hasNextToken() {
		return mReader.hasNextToken();
	}

	@Override
	public String nextToken() {
		final String nextToken = mReader.nextToken();
		mUseTokens.add( nextToken );
		return nextToken;
	}

	@Override
	public void createAnchor(Object key) {
		mReader.createAnchor(key);
	}

	@Override
	public void returnAnchor(Object key) {
		final int beforePointer = mReader.getTokenPointer();
		mReader.returnAnchor(key);
		final int afterPointer = mReader.getTokenPointer();
		for ( int i = beforePointer - 1; i > afterPointer; i-- ) {
			mUseTokens.remove( i );
		}
	}

	@Override
	public boolean isExistAnchor(Object key) {
		return mReader.isExistAnchor(key);
	}

	public String[] getUseTokens() {
		return mUseTokens.toArray( new String[mUseTokens.size()] );
	}
	
}
