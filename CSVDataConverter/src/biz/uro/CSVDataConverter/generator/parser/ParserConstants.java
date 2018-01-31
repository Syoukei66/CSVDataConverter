package biz.uro.CSVDataConverter.generator.parser;

import java.util.Map;

import biz.uro.CSVDataConverter.database.ISchema;
import biz.uro.CSVDataConverter.database.element.Element;
import biz.uro.CSVDataConverter.nativ.NativeCodeKind;
import biz.uro.CSVDataConverter.string.ProgramBuilder;

public class ParserConstants {
	
	public static final String COMMON_PARSER_PACKAGE = "biz.uro.CSVDataConverter.parser";
	public static final String COMMON_PARSER_NAME = "CommonParser";
	public static final String COMMON_PARSER_PACKAGE_PATH = COMMON_PARSER_PACKAGE + "." + COMMON_PARSER_NAME;

	public static final String IDENTIFIER = "identifier";
	public static final String TOKENIZER_NAME = "tokenizer";
	public static final String SIZE_NAME = "size";
	public static final String NEXT_TOKEN_NAME = TOKENIZER_NAME + ".nextToken()";
	
	public static final String CSV_TOKENIZER = NativeCodeKind.CSV_TOKENIZER.getClassName();
	public static final String CSV_FILE_READER = NativeCodeKind.CSV_FILE_READER.getClassName();
	
	public static final String PARSER_STATEMENT = "parseStatement";
	public static final String PARSER_ELEMENT = "parse";

	public static final String TOKENIZER_SKIP = "skip";
	
	public static final String parseElement( String tokenName, Element element ) {
		return parseElement( element.getSize(), tokenName, element.generateParserName() );
	}
	
	public static final String parseElement( int size, String tokenName, String parserName ) {
		final ProgramBuilder str = new ProgramBuilder();
		str.append( parserName + "( " + size + ", " + tokenName + ", " + TOKENIZER_NAME + " )" );
		if ( size == 1 ) {
			str.append( "[0]" );
		}
		return str.toString();
	}
	
	public static String generateParserArgument( ISchema<?> schema, String tokenName ) {
		final ProgramBuilder str = new ProgramBuilder();
		final Map<String,Element> elementMap = schema.getElementMap();
		boolean isFirst = true;
		if ( elementMap.size() == 0 ) {
			return str.toString();
		}
		str.append( " " );
		for ( Map.Entry<String,Element> e : elementMap.entrySet() ) {
			final Element element = e.getValue();
			if ( !isFirst ) {
				str.append( ", " );
			}
			isFirst = false;
			str.append( ParserConstants.parseElement( tokenName, element ) );
		}
		str.append( " " );
		return str.toString();
	}

	public static String tryCatchIllegalArgumentException( String program ) {
		final ProgramBuilder str = new ProgramBuilder();
		str.append( "try {\n" );
		str.addTab();
			str.append( program );
		str.subTab();	
		str.append( "} catch ( IllegalArgumentException e ) {\n" );
		str.append( "}\n" );
		return str.toString();
	}

	public static final String parseStatement( String tokenName, String parserName ) {
		final ProgramBuilder str = new ProgramBuilder();
		str.append( parserName + "( " + tokenName + ", " + TOKENIZER_NAME + " )" );
		return str.toString();
	}

}
