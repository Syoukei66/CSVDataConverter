package biz.uro.CSVDataConverter.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.apache.commons.lang3.StringUtils;

public class TextFileWriter {

	private final String mFileName;
	private final String mExtention;
	private BufferedWriter mBufferedWriter;

	public static TextFileWriter CraeateWithPackage(String fileName, String packaqe, String directoryPath, String extention) {
		return new TextFileWriter( directoryPath + "\\" + StringUtils.join( packaqe.split( "\\.", -1 ), "\\" ) + "\\" + fileName, extention );
	}

	public TextFileWriter( String filePath ) {
		int point = filePath.lastIndexOf(".");
	    mFileName = filePath.substring(0, point);
		mExtention = filePath.substring(point + 1, filePath.length());
	}

	public TextFileWriter( String fileName, String extention ) {
	    mFileName = fileName;
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
		final String[] paths = mFileName.split( "\\\\", -1 );
		String directory = "";
		for ( int i = 0; i < paths.length - 1; i++ ) {
			if ( i != 0 ) {
				directory += "\\";
			}
			directory += paths[i];
		}
		checkDirectory( directory );
		File file = new File( mFileName + "." + mExtention );
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
