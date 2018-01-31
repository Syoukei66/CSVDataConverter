package biz.uro.CSVDataConverter.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import org.apache.commons.lang3.StringUtils;

public class TextFileWriter {
	
	private final String mFilePath;
	private final String mExtention;
	private BufferedWriter mBufferedWriter;
	
	public TextFileWriter( String fileName, String packaqe, String directoryPath ) {
		this( directoryPath + "\\" + StringUtils.join( packaqe.split( "\\.", -1 ), "\\" ) + "\\" + fileName, "java" );
	}
	
	public TextFileWriter( String filePath, String extention ) {
		mFilePath = filePath.split( "\\.", -1 )[0];
		mExtention = extention;
	}
	
	public void checkDirectory( String directoryPath ) {
		final String[] directories = directoryPath.split( "\\\\" );
		String path = "";
		for ( String directory : directories ) {
			path += directory;
			final File file = new File( path );
			if ( !file.exists() ) {
				file.mkdir();
			}			
			path += "\\";
		}
	}
	
	public void open() throws IOException {
		final String[] paths = mFilePath.split( "\\\\", -1 );
		String directory = "";
		for ( int i = 0; i < paths.length - 1; i++ ) {
			if ( i != 0 ) {
				directory += "\\";
			}
			directory += paths[i];
		}
		checkDirectory( directory );
		File file = new File( mFilePath + "." + mExtention );
		OutputStream os = new FileOutputStream( file );
		OutputStreamWriter osw = new OutputStreamWriter( os, "UTF-8" );
		mBufferedWriter = new BufferedWriter( osw );
	}
	
	public void close() throws IOException {
		mBufferedWriter.close();
	}

	public void write( String text ) throws IOException {
		mBufferedWriter.write( text );
	}
	
	
}
