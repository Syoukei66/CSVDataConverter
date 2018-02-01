package biz.uro.CSVDataConverter.generator;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import biz.uro.CSVDataConverter.io.TextFileWriter;
import biz.uro.CSVDataConverter.nativ.NativeCodeKind;
import biz.uro.CSVDataConverter.string.ProgramBuilder;

public abstract class ProgramGenerator {
	
	public interface IProgramGenerateProcess {
		String generateFileName();
		String generatePackage();
		String generateImports();
		String generateClassDefinition();
	}
	
	private final HashSet<NativeCodeKind> mUseNativeCode = new HashSet<>();
	
	protected void nativeCode( NativeCodeKind kind ) {
		mUseNativeCode.add( kind );
	}
	
	protected void nativeCodes( Collection<NativeCodeKind> kind ) {
		mUseNativeCode.addAll( kind );
	}
	
	public Set<NativeCodeKind> getUseNativeCodes() {
		return mUseNativeCode;
	}
	
	protected void generate( String directoryPath, IProgramGenerateProcess process ) throws Exception {
		final TextFileWriter writer = TextFileWriter.CraeateWithPackage( process.generateFileName(), process.generatePackage(), directoryPath, "java" );
		writer.open();
		writer.write( generateProgram( process ) );
		writer.close();
	}
	
	private String generateProgram( IProgramGenerateProcess process ) {
		final ProgramBuilder str = new ProgramBuilder();
		//package
		str.append( generatePackage( process.generatePackage() ) );
		str.changeBlock();
		//import
		str.append( process.generateImports() );
		str.changeBlock();
		str.append( generateNativeCodeImports() );
		str.changeBlock();
		//enum definition
		str.append( process.generateClassDefinition() );
		return str.toString();
	}
	
	protected String generatePackage( String packagePath ) {
		return "package " + packagePath + ";\n";
	}
	
	protected String generateImport( String packagePath ) {
		return "import " + packagePath + ";\n";
	}
	
	private String generateNativeCodeImports() {
		final ProgramBuilder str = new ProgramBuilder();
		for ( NativeCodeKind kind : mUseNativeCode ) {
			str.append( generateImport( kind.getPackage() ) );
		}
		return str.toString();
	}
	
}
