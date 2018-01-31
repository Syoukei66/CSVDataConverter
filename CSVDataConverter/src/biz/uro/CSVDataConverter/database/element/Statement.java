package biz.uro.CSVDataConverter.database.element;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import biz.uro.CSVDataConverter.database.Table;
import biz.uro.CSVDataConverter.generator.parser.ParserConstants;
import biz.uro.CSVDataConverter.io.csv.ICSVLineReader;
import biz.uro.CSVDataConverter.swing.builder.StatementBuilder;

public class Statement {
	
	private final String mName;
	private final ElementType mType;
	private final ArrayList<ElementParser> mParsers;

	public Statement( StatementBuilder statement ) {
		mName = statement.getName();
		mType = statement.buildType();
		mParsers = statement.buildParsers();
	}
	
	public ElementType getType() {
		return mType;
	}

	public int getParserCount() {
		return mParsers.size();
	}
	
	public ElementParser getParser( int index ) {
		return mParsers.get( index );
	}
	
	public String generateParserName() {
		return ParserConstants.PARSER_STATEMENT + StringUtils.capitalize( mName );
	}

	public Element[] getParserElements() {
		final HashSet<Element> elementSet = new HashSet<>();
		for ( ElementParser parser : mParsers ) {
			final Element[] parserElements = parser.parserElements();
			if ( parserElements == null ) {
				continue;
			}
			elementSet.addAll( Arrays.asList( parserElements ) );
		}
		return elementSet.toArray( new Element[elementSet.size()] );
	}
	
	public ElementValue parseProgram( Table table, ICSVLineReader reader ) throws ElementException {
		reader.createAnchor( this );
		for ( ElementParser parser : mParsers ) {
			try {
				return parser.parseProgram( table, reader );
			}
			catch ( IllegalArgumentException e ) {
				reader.returnAnchor( this );
				continue;
			}
		}		
		throw new ElementException.UnbreakableElementException( this );
	}
	
	@Override
	public int hashCode() {
		return Objects.hash( mName );
	}
	
	@Override
	public boolean equals( Object other ) {
		if ( !( other instanceof Statement ) ) {
			return false;
		}
		final Statement otherElement = ( Statement )other;
		if ( mName != null && !mName.equals( otherElement.mName ) ) {
			return false;
		}
		return true;
	}

	public int getMaxLength() {
		int maxLength = 0;
		for ( ElementParser parser : mParsers ) {
			maxLength = Math.max( maxLength, parser.getMaxLength() );
		}
		return maxLength;
	}

}
