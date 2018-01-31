package biz.uro.CSVDataConverter.swing.builder;

import biz.uro.CSVDataConverter.database.Schema;
import biz.uro.CSVDataConverter.database.factory.FactoryMethod;
import biz.uro.CSVDataConverter.swing.json.IJSONDataModel;
import biz.uro.CSVDataConverter.swing.json.JSONReader;
import biz.uro.CSVDataConverter.swing.json.JSONWriter;
import biz.uro.CSVDataConverter.swing.old.JSONListModel;

public class FactoryMethodBuilder implements IJSONDataModel {

	private static final String FACTORY_HASH_CODE	= "FactoryHashCode";
	private static final String IDENTIFIER 			= "Identifier";
	private static final String LOGICAL_NAME		= "LogicalName";
	private static final String COMMENT 			= "Comment";
	private static final String SCHEMA 				= "Schema";
	
	//HashCode群
	private FactoryBuilder mFactory;

	private String mIdentifer;
	private String mLogicalName;
	private String mComment;	//TODO:フォームに追加
	private final SchemaBuilder mSchema;
	
	@Override
	public void jsonMapping(JSONWriter writer) {
		writer.add( FACTORY_HASH_CODE, mFactory.jsonHashCode() );		
		writer.add( IDENTIFIER, mIdentifer );
		writer.add( LOGICAL_NAME, mLogicalName );
		writer.add( COMMENT, mComment );
		writer.add( SCHEMA, mSchema );
	}
	
	public FactoryMethodBuilder( JSONReader reader ) {
		mFactory = reader.get( FACTORY_HASH_CODE ).pullObject();
		mIdentifer = reader.get( IDENTIFIER ).asText();
		mLogicalName = reader.get( LOGICAL_NAME ).asText();
		mComment = reader.get( COMMENT ).asText();
		mSchema = reader.get( SCHEMA ).asObject( SchemaBuilder.class );
	}
	
	public FactoryMethodBuilder( JSONListModel<ElementBuilder> model ) {
		mSchema = new SchemaBuilder( model );
	}

	@Override
	public boolean isEditable() {
		return true;
	}

	@Override
	public String jsonHashCode() {
		return Integer.toString( hashCode() );
	}

	public void setIdentifer( String text ) {
		mIdentifer = text;
	}

	public String getIdentifer() {
		return mIdentifer;
	}
	
	public void setLogicalName( String name ) {
		mLogicalName = name;
	}
	
	public String getLogicalName() {
		return mLogicalName;
	}

	public void setComment( String text ) {
		mComment = text;
	}

	public String getComment() {
		return mComment;
	}

	public SchemaBuilder getSchemaBuilder() {
		return mSchema;
	}

	public FactoryMethod build() {
		return new FactoryMethod( this );
	}

	public Schema buildSchema() {
		return mSchema.build();
	}

}
