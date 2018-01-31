package biz.uro.CSVDataConverter.swing.builder.validator.condition;

import java.text.DecimalFormat;

import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import javax.swing.text.SimpleAttributeSet;

import biz.uro.CSVDataConverter.swing.json.IJSONObject;
import biz.uro.CSVDataConverter.swing.json.JSONReader;
import biz.uro.CSVDataConverter.swing.json.JSONWriter;

public abstract class ValidatorCondition implements IJSONObject {
	
	public static class ValidatorParmModel implements IJSONObject {

		private static final String DOCUMENT = "Document";
		
		public final PlainDocument model;
		public String name;
		
		@Override
		public void jsonMapping( JSONWriter writer ) {
			writer.add( DOCUMENT, Integer.parseInt( toString() ) );
		}
		
		public ValidatorParmModel( JSONReader reader ) {
			this.model = new PlainDocument();
			final String document = reader.get( DOCUMENT ).asText();
			setValue( document );
		}
		
		public ValidatorParmModel() {
			this.model = new PlainDocument();
		}
		
		public void setName( String name ) {
			this.name = name;
		}
		
		public void setValue( Object value ) {
			try {
				model.remove( 0, model.getLength() );
				model.insertString( 0, value.toString(), new SimpleAttributeSet() );
			} catch ( BadLocationException e ) {
				e.printStackTrace();
			}
		}
		
		@Override
		public String toString() {
			try {
				return model.getText( 0, model.getLength() );
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		public String jsonHashCode() {
			return null;
		}
	}
	
	private static final DecimalFormat FORMAT = new DecimalFormat( "#.#" );

	public abstract boolean validation( Object value ) ;
	public abstract ValidatorParmModel model1();
	public abstract ValidatorParmModel model2();
	public abstract String getComment();
	public abstract void test( int input );
	
	public ValidatorCondition( JSONReader reader ) {
		
	}

	public ValidatorCondition() {
		
	}
	
	public String format( double value ) {
		return FORMAT.format( value );
	}

	@Override
	public String jsonHashCode() {
		return null;
	}
	
	public String toString() {
		return getComment();
	}
}
