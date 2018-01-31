package biz.uro.CSVDataConverter.nativ;

import java.util.Collection;
import java.util.HashSet;

import biz.uro.CSVDataConverter.io.TextFileWriter;

public class NativeCodeWriter {
	
	private final HashSet<NativeCodeKind> mOutputNativeCodes = new HashSet<>();
	
	public void add( NativeCodeKind kind ) {
		mOutputNativeCodes.add( kind );
	}
	
	public void addAll( Collection<NativeCodeKind> kind ) {
		mOutputNativeCodes.addAll( kind );
	}
	
	public void write( String directoryPath ) throws Exception {
		for ( NativeCodeKind kind : mOutputNativeCodes ) {
			final TextFileWriter writer = new TextFileWriter( kind.mClassName, NativeCodeKind.NATIVE_CODE_PACKAGE, directoryPath );
			writer.open();
			writer.write( kind.load() );
			writer.close();
		}
	}

}
