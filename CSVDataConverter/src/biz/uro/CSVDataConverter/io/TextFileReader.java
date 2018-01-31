package biz.uro.CSVDataConverter.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.commons.lang3.StringUtils;

public class TextFileReader {

	private final String mFilePath;
	private final String mExtention;
	private BufferedReader mBufferedReader;
	
	private boolean mBomChecked;
	private String mLine;
	
	public TextFileReader( String filePath, String extention ) {
		mFilePath = filePath.split( "\\.", -1 )[0];
		mExtention = extention;
	}
	
	public void open() throws IOException {
		File file = new File( mFilePath + "." + mExtention );
		InputStream is = new FileInputStream( file );
		InputStreamReader isr = new InputStreamReader( is, "UTF-8" );
		mBufferedReader = new BufferedReader( isr );
	}
	
	public void close() throws IOException {
		mBufferedReader.close();
	}

	public boolean hasNext() {
		try {
			do {
				mLine = mBufferedReader.readLine();
				if ( !mBomChecked ) {
					mBomChecked = true;
					if ( Integer.toHexString( mLine.charAt( 0 ) ).equals( "feff" ) ) {
						mLine = StringUtils.right( mLine, mLine.length() - 1 );					
					}
				}
				if ( mLine == null ) {
					return false;
				}
			} while( mLine.length() == 0 || mLine.substring( 0, 1 ).equals("/") );
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public String getLine() {
		return mLine;
	}

	public String getNextLine() {
		if ( !hasNext() ) {
			return null;
		}
		return getLine();
	}
	
	public String getRestText() {
		StringBuilder str = new StringBuilder();
		while ( hasNext() ) {
			str.append( getLine() + "\n" );
		}
		return str.toString();
	}
	
}
