package biz.uro.CSVDataConverter.generator.parser;

import static biz.uro.CSVDataConverter.generator.parser.ParserConstants.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import biz.uro.CSVDataConverter.database.Table;
import biz.uro.CSVDataConverter.database.element.Element;
import biz.uro.CSVDataConverter.database.element.ElementOption;
import biz.uro.CSVDataConverter.database.element.ElementParser;
import biz.uro.CSVDataConverter.database.element.ElementType;
import biz.uro.CSVDataConverter.database.element.Statement;
import biz.uro.CSVDataConverter.generator.ProgramGenerator;
import biz.uro.CSVDataConverter.nativ.NativeCodeKind;
import biz.uro.CSVDataConverter.string.ProgramBuilder;
import biz.uro.CSVDataConverter.swing.builder.ElementParserBuilder;
import biz.uro.CSVDataConverter.swing.builder.PackageBuilder;
import biz.uro.CSVDataConverter.swing.builder.StatementBuilder;
import biz.uro.CSVDataConverter.swing.old.JSONListModel;

public class StatementParserGenerator extends ProgramGenerator {

	{
		nativeCode( NativeCodeKind.CSV_TOKENIZER );
	}

	public void generate( String directoryPath, List<StatementBuilder> statementList, List<Table> tableList, HashSet<PackageBuilder> packaqeList ) throws Exception {
		super.generate( directoryPath, new IProgramGenerateProcess() {
			
			@Override
			public String generateFileName() {
				return ParserConstants.COMMON_PARSER_NAME;
			}
			
			@Override
			public String generatePackage() {
				return ParserConstants.COMMON_PARSER_PACKAGE;
			}
			
			@Override
			public String generateImports() {
				final StringBuilder str = new StringBuilder();
				final HashSet<PackageBuilder> importSet = new HashSet<>();
				importSet.addAll( packaqeList );
				for ( StatementBuilder statement : statementList ) {
					final JSONListModel<ElementParserBuilder> model = statement.getParserListModel();
					final List<ElementParserBuilder> parsers = model.asList( new ElementParserBuilder[model.size()] );
					for ( ElementParserBuilder parser : parsers ) {
						PackageBuilder i = parser.getImport();
						if (i != null)
						{							
							importSet.add( i );
						}
					}
				}
				final List<PackageBuilder> importList = new ArrayList<>();
				importList.addAll( importSet );
				Collections.sort( importList );
				
				for ( PackageBuilder packaqe : importList ) {
					if ( packaqe == null ) {
						continue;
					}
					str.append( generateImport( packaqe.getPackagePath() ) );
				}
				return str.toString();
			}
			
			@Override
			public String generateClassDefinition() {
				final ProgramBuilder str = new ProgramBuilder();
				str.append( "public class " + generateFileName() + " {\n" );
				str.changeBlock();
				str.addTab();
					final HashSet<Element> parserElements = new HashSet<>();
					//final HashSet<Statement> parserStatements = new HashSet<>();
					for ( Table table : tableList ) {
						final Element[] elements = table.getSchema().getParserElements();
						for ( Element element : elements ) {
							parserElements.add( element );
					//		parserStatements.add( element.getStatement() );
						}
					};
					for ( StatementBuilder statement : statementList ) {
						if ( !statement.getType().isTerminal() ) {
							continue;
						}
						str.append( generateParserStatement( statement.build() ) );
						str.changeBlock();
					}
					for ( Element element : parserElements ) {
						if ( !element.getStatement().getType().isTerminal() ) {
							continue;
						}
						str.append( generateParserElement( element ) );
						str.changeBlock();
					}
					for ( StatementBuilder statement : statementList ) {
						if ( statement.getType().isTerminal() ) {
							continue;
						}
						str.append( generateParserStatement( statement.build() ) );
						str.changeBlock();
					}
					for ( Element element : parserElements ) {
						if ( element.getStatement().getType().isTerminal() ) {
							continue;
						}
						str.append( generateParserElement( element ) );
						str.changeBlock();
					}
				str.subTab();
				str.append( "}\n" );
				return str.toString();
			}
		});
	}

	private static String generateParserStatementProcess( Statement statement ) {
		final ProgramBuilder str = new ProgramBuilder();
		final int parserCount = statement.getParserCount();
		if ( parserCount > 1 ) {
			for ( int i = 0; i < parserCount; i++ ) {
				final ElementParser parser = statement.getParser( i );
				str.append( parser.generateParserProgram( statement, IDENTIFIER, TOKENIZER_NAME, NEXT_TOKEN_NAME, NEXT_TOKEN_NAME ) );
			}
		}
		else if ( parserCount == 1 ) {
			str.append( statement.getParser( 0 ).generateParserProgram( statement, IDENTIFIER, TOKENIZER_NAME, IDENTIFIER, NEXT_TOKEN_NAME ) );
		}
		str.append( "throw new IllegalArgumentException();" );
		return str.toString();
	}

	private static String generateParserStatement( Statement statement ) {
		final ProgramBuilder str = new ProgramBuilder();
		final ElementType type = statement.getType();
		final String typeName = type.getTypeName();
		final String parserStatementName = statement.generateParserName();
		str.funcBegin( "public static", typeName, parserStatementName, "String " + IDENTIFIER, CSV_TOKENIZER + " " + TOKENIZER_NAME );
			str.append( generateParserStatementProcess( statement ) );
		str.funcEnd();
		return str.toString();
	}

	private static String generateParserElement( Element element ) {
		final ProgramBuilder str = new ProgramBuilder();
		final ElementOption option = element.getOption();
		final Statement statement = element.getStatement();
		final String typeName = statement.getType().getTypeName();
		final String parserElementName = element.generateParserName();
		final String parserStatementName = statement.generateParserName();
		final String retName = "ret";
		final String indexName = "i";
		str.funcBegin( "public static", typeName + "[]", parserElementName, "int " + SIZE_NAME, "String " + IDENTIFIER, CSV_TOKENIZER + " " + TOKENIZER_NAME );
			str.ifBegin( IDENTIFIER + ".length() == 0" );
				str.append( option.nullProgram() );
			str.ifEnd();
			str.var_Array( null, true, typeName, retName, SIZE_NAME );
			str.assignArray( retName, "0", parserStatementName + "( " + IDENTIFIER + ", " + TOKENIZER_NAME + " )" );
			str.forBegin( indexName, "1", SIZE_NAME );
				str.assignArray( retName, indexName, parserStatementName + "( " + NEXT_TOKEN_NAME + ", " + TOKENIZER_NAME + " )" );
			str.forEnd();
		str.funcReturn( retName );
		str.funcEnd();
		return str.toString();
	}
	
}
