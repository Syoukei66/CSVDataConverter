package biz.uro.CSVDataConverter.io.csv;

public interface ICSVLineReader {
	
	void reset();
	boolean hasNextToken();
	String nextToken();
	String getOrginalText();
	int countToken();
	int getTokenPointer();
	void createAnchor( Object key );
	void returnAnchor( Object key );
	boolean isExistAnchor( Object key );

}
