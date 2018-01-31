package biz.uro.CSVDataConverter.nativ;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public enum NativeCodeKind {
	CSV_FILE_READER	( "ICSVFileReader" ),
	CSV_TOKENIZER	( "ICSVTokenizer" ),
	;
	
	static final String NATIVE_CODE_PACKAGE = "biz.uro.CSVDataConverter.nativ";
	final String mClassName;
	
	NativeCodeKind( String fileName ) {
		mClassName = fileName;
	}
	
	String load() throws URISyntaxException, IOException {
		final URL url = this.getClass().getResource( mClassName );
		final URI uri = url.toURI();
		try {
			Path path = Paths.get( uri );
			return Files.lines( path, Charset.forName( "SJIS" ) )
					.collect( Collectors.joining( System.getProperty( "line.separator" ) ) );			
		}
		catch ( FileSystemNotFoundException e ) {
			Map<String, String> env = new HashMap<>();
			env.put( "create", "false" );
			try ( FileSystem zipfs = FileSystems.newFileSystem( uri, env ) ) {
				Path path = Paths.get( uri );
				return Files.lines( path, Charset.forName( "SJIS" ) )
						.collect( Collectors.joining( System.getProperty( "line.separator" ) ) );
			}
		}
	}
	
	public String getPackage() {
		return NATIVE_CODE_PACKAGE + "." + mClassName;
	}

	public String getClassName() {
		return mClassName;
	}
	
}
