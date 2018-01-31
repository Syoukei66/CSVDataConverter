package biz.uro.CSVDataConverter.string;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProgramBuilder {

	private static String TAB = "\t";
	private final StringBuilder mStringBuilder = new StringBuilder();
	
	private int mDepth;
	private boolean mIsNullBlock = true;
	private boolean mIsNewLine;
	
	public void addTab() {
		mIsNullBlock = true;
		mDepth ++;
	}
	
	public void subTab() {
		mIsNullBlock = true;
		mDepth --;
	}
	
	public void changeBlock() {
		if ( !mIsNullBlock ) {
			mStringBuilder.append( "\n" );
		}
		mIsNullBlock = true;
		mIsNewLine = true;
	}
	
	private void delegateAppend( String str ) {
		mIsNullBlock = false;
		mStringBuilder.append( str );
	}
	
	private void appendEnter() {
		delegateAppend( "\n" );
		mIsNewLine = true;
	}
	
	private void appendTab() {
		if ( mIsNewLine ) {
			for ( int i = 0; i < mDepth; i++ ) {
				delegateAppend( TAB );
			}
		}		
		mIsNewLine = false;
	}
	
	public void append( String string ) {
		if ( string == null || string.length() == 0 ) {
			return;
		}
		if ( string.equals( "\n" ) ) {
			appendEnter();
			return;
		}
		if ( !string.contains( "\n" ) ) {
			appendTab();
			delegateAppend( string );
			return;
		}
		final String[] lines = string.split( "\n" );
		for ( String line : lines ) {
			appendTab();
			delegateAppend( line );
			appendEnter();
		}
	}
	
	public void assign( String name, String value ) {
		append( name + " = " + value + ";\n" );
	}
	
	public void assignArray( String name, String index, String value ) {
		append( name + "[" + index + "]" + " = " + value + ";\n" );
	}

	public void var( String access, boolean isFinal, String typeName, String name, String initValue ) {
		if ( access != null && access.length() != 0 ) {
			append( access + " " );			
		}
		if ( isFinal ) {
			append( "final " );
		}
		if ( initValue == null ) {
			append( typeName + " " + name + ";\n" );
			return;
		}
		append( typeName + " " + name + " = " + initValue + ";\n" );
	}
	
	public void var_Array( String access, boolean isFinal, String typeName, String name, String sizeName ) {
		if ( sizeName == null ) {
			var( access, isFinal, typeName + "[]", name, null );			
			return;
		}
		var( access, isFinal, typeName + "[]", name, "new " + typeName + "[" + sizeName + "]" );
	}
	
	private String generateArgments( String first, String second, String... rest ) {
		final StringBuilder str = new StringBuilder();
		final List<String> argments = new ArrayList<>();
		argments.addAll( Arrays.asList( first, second ) );
		if ( rest.length > 0 ) {
			argments.addAll( Arrays.asList( rest ) );			
		}
		boolean isFirst = true;
		for ( String argment : argments ) {
			if ( !isFirst ) {
				str.append( ", " );
			}
			isFirst = false;
			str.append( argment );
		}
		return str.toString();
	}
	
	public void callFunc( String clazz, String method, String argments ) {
		if ( clazz != null && clazz.length() != 0 ) {
			append( clazz + "." );
		}
		append( method );
		append( "(" );
		if ( argments != null && argments.length() != 0 ) {
			append( " " );
			append( argments );
			append( " " );
		}
		append( ");\n" );
	}

	public void callFunc( String clazz, String method, String firstArgment, String secondArgment, String... rest ) {
		callFunc( clazz, method, generateArgments( firstArgment, secondArgment, rest ) );
	}

	public void funcBegin( String access, String typeName, String funcName, String argments ) {
		append( access + " " + typeName + " " + funcName + "(" );
		if ( argments != null && argments.length() != 0 ) {
			append( " " );
			append( argments );
			append( " " );
		}
		append( ") {\n" );
		addTab();
	}
	
	public void funcBegin( String access, String typeName, String funcName, String firstArgment, String secondArgment, String... rest ) {
		funcBegin( access, typeName, funcName, generateArgments( firstArgment, secondArgment, rest ) );
	}
	
	public void funcEnd() {
		subTab();
		append( "}\n" );
	}
	
	public void funcReturn( String retName ) {
		append( "return " + retName + ";\n" );		
	}
	
	public void ifBegin( String condition ) {
		append( "if ( " + condition + " ) {\n" );
		addTab();
	}

	public void elseBegin() {
		subTab();
		append( "else {\n" );
		addTab();
	}
	
	public void elseIfBegin( String condition ) {
		subTab();
		append( "else if ( " + condition + " ) {\n" );
		addTab();
	}

	public void ifEnd() {
		subTab();
		append( "}\n" );
	}

	public void forBegin( String indexName, String init, String size ) {
		append( "for ( int " + indexName + " = " + init + "; " + indexName + " < " + size + "; " + indexName + "++ ){\n" );
		addTab();
	}	
	
	public void forEnd() {
		subTab();
		append( "}\n" );
	}
	
	public void forEachBegin( String typeName, String elementName, String listName ) {
		append( "for ( " + typeName + " " + elementName + " : " + listName + " ){\n" );
		addTab();
	}	
	
	public void forEachEnd() {
		subTab();
		append( "}\n" );
	}	
	
	@Override
	public String toString() {
		return mStringBuilder.toString();
	}
	
}
