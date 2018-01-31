package biz.uro.CSVDataConverter.database.element;

import org.apache.commons.lang3.StringUtils;

import biz.uro.CSVDataConverter.database.PrimaryKey;
import biz.uro.CSVDataConverter.database.PrimaryKeyRecord;
import biz.uro.CSVDataConverter.database.Record;
import biz.uro.CSVDataConverter.database.Table;
import biz.uro.CSVDataConverter.database.factory.Factory;
import biz.uro.CSVDataConverter.generator.parser.ParserConstants;
import biz.uro.CSVDataConverter.io.csv.CSVState;
import biz.uro.CSVDataConverter.io.csv.ICSVLineReader;
import biz.uro.CSVDataConverter.swing.builder.FactoryBuilder;
import static biz.uro.CSVDataConverter.generator.table.TableGeneratorConstants.*;

public abstract class ElementParser {
	
	//csvで読み込んだ文字列をプログラム上での文字列に変換
	public abstract ElementValue parseProgram( Table table, ICSVLineReader reader ) throws RuntimeException;
	
	//String型の入力armgentを引数で受け取ってElementTypeのTypeに変換するメソッドを生成
	public abstract String generateParserProgram( Statement statement, String identifer, String tokenizer, String firstToken, String nextToken );
	public abstract Element[] parserElements();
	public abstract int getMaxLength();
	
	public static _ForeignKey _ForeignKey( final Table table ) {
		return new _ForeignKey( table );
	}
	
	public static class _ForeignKey extends ElementParser {
		
		private final Table mTable;
		
		public _ForeignKey( Table table ) {
			mTable = table;
			table.setDefaultParser( this );
		}

		/* ====================================================== *
		 * <foreignTableName>.<recordName>;
		 * ====================================================== */
		@Override
		public ElementValue parseProgram( Table table, ICSVLineReader reader ) throws RuntimeException {
			final CSVState state = new CSVState( reader );
			final Table foreignTable = mTable;
			final PrimaryKey primaryKey = foreignTable.getSchema().getPrimaryKey();
			final PrimaryKeyRecord primaryKeyRecord = primaryKey.read( table, state );
			final Record record = foreignTable.getRecord( primaryKeyRecord );
			final ElementValue value = new ElementValue( record, state.getUseTokens() ) {

				@Override
				public String getProgram() {
					if ( foreignTable == table ) {
						return primaryKeyRecord.convertProgram();
					}
					return foreignTable.getName() + "." + foreignTable.parseRecordName( record );				
				}
				
			};
			return value;
		}
		
		@Override
		public String generateParserProgram( Statement statement, String identifer, String tokenizer, String firstToken, String nextToken ) {
			final String typeName = statement.getType().getTypeName();
			final String funcName = typeName + "." + TABLE_GETTER_METHOD_NAME;
			final PrimaryKey primaryKey = mTable.getSchema().getPrimaryKey();
			return ParserConstants.tryCatchIllegalArgumentException( "return " + funcName + "(" + ParserConstants.generateParserArgument( primaryKey, identifer ) + ");\n" );
		}

		@Override
		public Element[] parserElements() {
			return mTable.getSchema().getPrimaryKey().getParserElements();
		}
		
		@Override
		public int getMaxLength() {
			return mTable.getSchema().getPrimaryKey().getMaxLength();
		}
	
	}
	
	public static class _Factory extends ElementParser {
		
		protected final Factory mFactory;
		
		public _Factory( FactoryBuilder factoryBuilder ) {
			mFactory = factoryBuilder.build();
		}
		
		@Override
		public ElementValue parseProgram( Table table, ICSVLineReader reader ) throws RuntimeException {
			return mFactory.parseProgram( table, reader );
		}
		
		@Override
		public String generateParserProgram( Statement statement, String identifier, String tokenizer, String firstToken, String nextToken ) {
			return mFactory.generateParserProgram( statement, identifier, tokenizer, firstToken, nextToken );
		}
		
		@Override
		public Element[] parserElements() {
			return mFactory.parserElements();
		}
		
		@Override
		public int getMaxLength() {
			return mFactory.getMaxLength();
		}

	}
	
	
	public static abstract class _Terminal extends ElementParser {
		protected abstract Object parseValue( String input ) throws IllegalArgumentException;
		protected abstract String parseProgram( String input );
		@Override
		public ElementValue parseProgram( Table table, ICSVLineReader reader ) throws IllegalArgumentException {
			try {
				final String useToken = reader.nextToken();
				return new ElementValue( parseValue( useToken ), useToken ) {
					@Override
					public String getProgram() {
						return parseProgram( useToken );
					}					
				};				
			}
			catch ( IllegalArgumentException e ) {
				throw new IllegalArgumentException( getClass().getSimpleName() + "=>" + e.getClass().getSimpleName() );
			}
		}
		@Override
		public Element[] parserElements() {
			return null;
		}		
		@Override
		public int getMaxLength() {
			return 1;
		}
	}
	
	public static abstract class _Primitive extends _Terminal {
		@Override
		public String generateParserProgram( Statement statement, String identifer, String tokenizer, String firstToken, String nextToken ) {
			final ElementType type = statement.getType();
			final String typeName = type.getTypeName();
			final String wrapperName = type.getWrapperName();
			return ParserConstants.tryCatchIllegalArgumentException( "return " + wrapperName + ".parse" + StringUtils.capitalize( typeName ) + "( " + firstToken + " );\n" );
		}
	}

	public static final _Primitive _int = new _Primitive() {
		@Override
		protected Object parseValue( String input ) throws IllegalArgumentException {
			return Integer.parseInt( input );
		}
		@Override
		public String parseProgram( String input ) {
			return input;
		}
	};
	
	public static final _Primitive _float = new _Primitive() {
		@Override
		protected Object parseValue( String input ) throws IllegalArgumentException {
			return Float.parseFloat( input );
		}
		@Override
		public String parseProgram( String input ) {
			return input + "f";
		}
	};

	public static final _Primitive _double = new _Primitive() {
		@Override
		protected Object parseValue( String input ) throws IllegalArgumentException {
			return Double.parseDouble( input );
		}
		@Override
		public String parseProgram( String input ) {
			return input;
		}
	};

	public static final _Primitive _boolean = new _Primitive() {
		@Override
		protected Object parseValue( String input ) throws IllegalArgumentException {
			return Boolean.parseBoolean( input );
		}
		@Override
		public String parseProgram( String input ) {
			return StringUtils.lowerCase( input );
		}
	};
	
	public static final _Terminal _String = new _Terminal() {
		@Override
		protected Object parseValue( String input ) throws IllegalArgumentException {
			return input;
		}
		@Override
		protected String parseProgram( String input ) {
			return "\"" + input + "\"";
		}
		@Override
		public String generateParserProgram( Statement statement, String identifer, String tokenizer, String firstToken, String nextToken ) {
			return ParserConstants.tryCatchIllegalArgumentException( "return " + firstToken + ";\n" );
		}
	};

	
}
