package biz.uro.CSVDataConverter.database.factory;

import biz.uro.CSVDataConverter.database.IRecord;
import biz.uro.CSVDataConverter.database.Schema;
import biz.uro.CSVDataConverter.database.Table;
import biz.uro.CSVDataConverter.database.element.Element;
import biz.uro.CSVDataConverter.database.element.ElementValue;
import biz.uro.CSVDataConverter.database.element.Statement;
import biz.uro.CSVDataConverter.generator.parser.ParserConstants;
import biz.uro.CSVDataConverter.io.csv.CSVState;
import biz.uro.CSVDataConverter.io.csv.ICSVLineReader;
import biz.uro.CSVDataConverter.string.ProgramBuilder;
import biz.uro.CSVDataConverter.swing.builder.FactoryMethodBuilder;

public class FactoryMethod {

	private final String mIdentifier;
	private final Schema mSchema;
	
	public FactoryMethod( FactoryMethodBuilder builder ) {
		mIdentifier = builder.getIdentifer();
		mSchema = builder.buildSchema();
	}

	/* ====================================================== *
	 * <factoryName>.[mIdentifier](<arguments>);
	 * ====================================================== */
	public ElementValue parseProgram( String factoryName, Table table, ICSVLineReader reader ) {
		final CSVState state = new CSVState( reader );
		final String identifier = state.nextToken();
		if ( !mIdentifier.equals( identifier ) ) {
			throw new IllegalArgumentException();
		}
		final IRecord record = mSchema.read( table, state );
		return new ElementValue( record, state.getUseTokens() ) {
			@Override
			public String getProgram() {
				return factoryName + "." + mIdentifier + "( " + record.convertProgram() + " )";
			}
		};
	}

	/* ====================================================== *
	 * if (<identifier>.equals([mIdentifier])) {
	 *   #if maxLength == length
	 *   return <factoryName>.[mIdentifier](<arguments>);
	 *   #else
	 *   final <typeName> <retName> = <factoryName>.[mIdentifier](<arguments>);
	 *   <tokenizer>.skip(maxLength - length);
	 *   return <retName>;
	 * }
	 * ====================================================== */
	public String generateParserProgram( String factoryName, Statement statement, String identifier, String tokenizer, String firstToken, String nextToken ) {
		final ProgramBuilder str = new ProgramBuilder();
		final String funcName = factoryName + "." + mIdentifier;
		str.append( "if ( " + identifier + ".equals( \"" + mIdentifier + "\" ) ) {\n" );
		str.addTab();
			final int maxLength = statement.getMaxLength();
			final int length = mSchema.getMaxLength();
			final String func = funcName + "(" + ParserConstants.generateParserArgument( mSchema, nextToken ) + ")";
			if ( maxLength == length ) {
				str.append( "return " + func + ";\n" );					
			}
			else {
				final String retName = "ret";
				str.append( "final " + statement.getType().getTypeName() + " " + retName + " = " + func + ";\n" );
				str.append( tokenizer + "." + ParserConstants.TOKENIZER_SKIP + "( " + ( maxLength - length ) + " );\n" );
				str.append( "return " + retName + ";\n" );
			}
		str.subTab();
		str.append( "}\n" );
		return str.toString();
	}

	public int getMaxLength() {
		return mSchema.getMaxLength() + 1;
	}

	public Element[] parserElements() {
		return mSchema.getParserElements();
	}

}
