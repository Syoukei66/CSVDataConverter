package biz.uro.CSVDataConverter.database.factory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import biz.uro.CSVDataConverter.database.Table;
import biz.uro.CSVDataConverter.database.element.Element;
import biz.uro.CSVDataConverter.database.element.ElementType;
import biz.uro.CSVDataConverter.database.element.ElementValue;
import biz.uro.CSVDataConverter.database.element.Statement;
import biz.uro.CSVDataConverter.io.csv.ICSVLineReader;
import biz.uro.CSVDataConverter.string.ProgramBuilder;
import biz.uro.CSVDataConverter.swing.builder.FactoryBuilder;

public class Factory {

	private final String mName;
	private final ElementType mType;
	private final ArrayList<FactoryMethod> mMethods;
	
	public Factory( FactoryBuilder builder ) {
		mName = builder.getPackage().getClassName();
		mType = builder.buildType();
		mMethods = builder.buildMethods();
	}

	public ElementValue parseProgram( Table table, ICSVLineReader reader ) {
		ElementValue ret = null;
		for ( FactoryMethod method : mMethods ) {
			try {
				ret = method.parseProgram( mName, table, reader );
				break;
			}
			catch ( IllegalArgumentException e ) {
				
			}
		}
		return ret;
	}

	public String generateParserProgram( Statement statement, String identifier, String tokenizer, String firstToken, String nextToken ) {
		final ProgramBuilder str = new ProgramBuilder();
		for ( FactoryMethod method : mMethods ) {
			try {
				str.append( method.generateParserProgram( mName, statement, identifier, tokenizer, firstToken, nextToken ) );
				break;
			}
			catch ( IllegalArgumentException e ) {
				
			}
			str.changeBlock();
		}
		return str.toString();
	}

	public int getMaxLength() {
		int ret = 0;
		for ( FactoryMethod method : mMethods ) {
			ret = Math.max(ret, method.getMaxLength());
		}
		return ret;
	}

	public Element[] parserElements() {
		final HashSet<Element> elementSet = new HashSet<>();
		for ( FactoryMethod method : mMethods ) {
			final Element[] parserElements = method.parserElements();
			if ( parserElements == null ) {
				continue;
			}
			elementSet.addAll( Arrays.asList( parserElements ) );
		}
		return elementSet.toArray( new Element[elementSet.size()] );
	}
	
}
