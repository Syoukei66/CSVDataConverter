package biz.uro.CSVDataConverter.database.element;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import biz.uro.CSVDataConverter.database.Table;
import biz.uro.CSVDataConverter.generator.parser.ParserConstants;
import biz.uro.CSVDataConverter.io.csv.ICSVLineReader;
import biz.uro.CSVDataConverter.swing.builder.ElementBuilder;

public class Element {

	private final int mSize;
	private final String mName;
	private final Statement mStatement;
	private final ElementOption mOption;
	private final boolean mIsPrimaryKey;
	
	public Element( ElementBuilder builder ) {
		mSize = builder.getSize();
		mName = builder.getFieldName();
		mStatement = builder.getStatement().build();
		mOption = builder.isNotNull() ? ElementOption._Throw() : ElementOption._Nullable();
		mIsPrimaryKey = builder.isPrimaryKey();
	}
	
	public int getSize() {
		return mSize;
	}
	
	public String getName() {
		return mName;
	}
	
	public Statement getStatement() {
		return mStatement;
	}
	
	public ElementOption getOption() {
		return mOption;
	}
	
	public boolean isPrimaryKey() {
		return mIsPrimaryKey;
	}
	
	public String generateParserName() {
		return mOption.generateName( ParserConstants.PARSER_ELEMENT + StringUtils.capitalize( getStatement().getType().getTypeName() ) );
	}

	public ElementValue read( Table table, ICSVLineReader reader ) throws RuntimeException {
		return mOption.read( table, this, reader );
	}

	public ElementValue parseProgram( Table table, ICSVLineReader reader ) throws ElementException {
		reader.createAnchor( this );
		final String identifier = reader.nextToken();
		if ( identifier.length() == 0 ) {
			throw new ElementException.NullElementException( this );
		}
		reader.returnAnchor( this );
		return mStatement.parseProgram( table, reader );
	}
	
	@Override
	public int hashCode() {
		return Objects.hash( mStatement, mOption );
	}
	
	@Override
	public boolean equals( Object other ) {
		if ( !( other instanceof Element ) ) {
			return false;
		}
		final Element otherElement = ( Element )other;
		if ( mStatement != null && !mStatement.equals( otherElement.mStatement ) ) {
			return false;
		}
		if ( mOption != null && !mOption.equals( otherElement.mOption ) ) {
			return false;
		}
		return true;
	}

	public int getMaxLength() {
		return mStatement.getMaxLength() * mSize;
	}
	
}
