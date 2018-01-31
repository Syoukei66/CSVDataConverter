package biz.uro.CSVDataConverter.swing.builder;

import java.util.ArrayList;

import biz.uro.CSVDataConverter.database.Schema;
import biz.uro.CSVDataConverter.swing.json.IJSONDataModel;
import biz.uro.CSVDataConverter.swing.json.JSONReader;
import biz.uro.CSVDataConverter.swing.json.JSONWriter;
import biz.uro.CSVDataConverter.swing.old.JSONListModel;
import biz.uro.CSVDataConverter.swing.old.JSONListModel.IJsonParseProcess;

public class SchemaBuilder implements IJSONDataModel {

	private static final String ELEMENT = "Element";
	
	private final JSONListModel<ElementBuilder> mElementsModel;

	private static JSONListModel<ElementBuilder> newElementModel() {
		return new JSONListModel<>( ElementBuilder.class, new IJsonParseProcess<ElementBuilder>() {
			@Override
			public ElementBuilder parse( JSONReader reader, Class<ElementBuilder> clazz ) {
				return reader.asObject( clazz );
			}

			@Override
			public void loadConstants(JSONListModel<ElementBuilder> model) {
				
			}
		} );
	}
	
	@Override
	public void jsonMapping( JSONWriter writer ) {
		writer.add( ELEMENT, mElementsModel );
	}
	
	public SchemaBuilder( JSONReader reader ) {
		mElementsModel = reader.get( ELEMENT ).asList( newElementModel() );
	}
	
	public SchemaBuilder() {
		this( newElementModel() );
	}

	public SchemaBuilder( JSONListModel<ElementBuilder> model ) {
		mElementsModel = model;
	}
	
	public Schema build() {
		final ArrayList<String> primaryKeys = new ArrayList<>();
		final int elementsCount = mElementsModel.size();
		for ( int i = 0; i < elementsCount; i++ ) {
			final ElementBuilder element = mElementsModel.get( i );
			if ( element.isPrimaryKey() ) {
				primaryKeys.add( element.getFieldName() );
			}
		}
		final Schema schema = new Schema( primaryKeys.toArray( new String[primaryKeys.size()] ) )
		{{
			final int elementCount = mElementsModel.size();
			for ( int i = 0; i < elementCount; i++ ) {
				element( mElementsModel.get( i ).build() );
			}
		}};
		return schema;
	}

	public JSONListModel<ElementBuilder> getElementDataModel() {
		return mElementsModel;
	}
	
	@Override
	public String toString() {
		final StringBuilder str = new StringBuilder();
		final int elementCount = mElementsModel.size();
		for ( int i = 0; i < elementCount; i++ ) {
			if ( i != 0 ) {
				str.append( ", " );
			}
			str.append( mElementsModel.get( i ).getStatement().getType().getPackage().getClassName() );
		}
		return str.toString();
	}

	@Override
	public boolean isEditable() {
		return true;
	}

	@Override
	public String jsonHashCode() {
		return null;
	}

}
