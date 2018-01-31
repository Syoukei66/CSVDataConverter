package biz.uro.CSVDataConverter.database.element;

import biz.uro.CSVDataConverter.database.Table;
import biz.uro.CSVDataConverter.io.csv.ICSVLineReader;

public abstract class ElementOption {

	public abstract void init();
	public final ElementValue read( Table table, Element element, ICSVLineReader reader ) {
		try {
			return element.parseProgram( table, reader );
		}
		catch ( ElementException.NullElementException e ) {
			return nullProcess( e );
		}
	}

	protected abstract ElementValue nullProcess( ElementException.NullElementException e );
	
	public abstract String generateName( String name );
	public abstract String nullProgram();
	
	/* ================================================================
	 * 	空欄があるとRuntimeExceptionが発生。
	 * ================================================================ */
	private static final ElementOption THROW = new ElementOption() {	
		@Override
		public void init() {
			
		}
		@Override
		protected ElementValue nullProcess( ElementException.NullElementException e ) {
			throw e;
		}
		@Override
		public String generateName( String name ) {
			return name + "_Throw";
		}
		@Override
		public String nullProgram() {
			return "throw new IllegalArgumentException();\n";
		}
	};
	
	public static ElementOption _Throw() {
		return THROW;
	}
	
	/* ================================================================
	 * 	空欄に"null"を入力
	 * ================================================================ */
	private static final ElementOption NULLABLE = new ElementOption() {	
		@Override
		public void init() {
			
		}
		@Override
		protected ElementValue nullProcess( ElementException.NullElementException e ) {
			return ElementValue.NULL;
		}
		@Override
		public String generateName( String name ) {
			return name + "_Nullable";
		}
		@Override
		public String nullProgram() {
			return "return null;\n";
		}
	};
	public static ElementOption _Nullable() {
		return NULLABLE;
	}

}
