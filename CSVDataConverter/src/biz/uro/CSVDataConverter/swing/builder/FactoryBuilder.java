package biz.uro.CSVDataConverter.swing.builder;

import java.util.ArrayList;

import biz.uro.CSVDataConverter.database.element.ElementType;
import biz.uro.CSVDataConverter.database.factory.Factory;
import biz.uro.CSVDataConverter.database.factory.FactoryMethod;
import biz.uro.CSVDataConverter.swing.json.IJSONDataModel;
import biz.uro.CSVDataConverter.swing.json.JSONReader;
import biz.uro.CSVDataConverter.swing.json.JSONWriter;
import biz.uro.CSVDataConverter.swing.old.JSONListModel;
import biz.uro.CSVDataConverter.swing.old.JSONListModel.IJsonParseProcess;

public class FactoryBuilder implements IJSONDataModel {

	private static final String PACKAGE_HASH_CODE	= "PackageHashCode";
	private static final String TYPE_HASH_CODE 		= "TypeHashCode";

	private static final String METHOD	= "Method";
	
	private static JSONListModel<FactoryMethodBuilder> newFactoryMethodModel() {
		return new JSONListModel<>( FactoryMethodBuilder.class, new IJsonParseProcess<FactoryMethodBuilder>() {
			@Override
			public FactoryMethodBuilder parse( JSONReader reader, Class<FactoryMethodBuilder> clazz ) {
				return reader.asObject( clazz );
			}

			@Override
			public void loadConstants(JSONListModel<FactoryMethodBuilder> model) {
				
			}
		} );
	}

	private final JSONListModel<FactoryMethodBuilder> mFactoryMethodModel;
	
	//HashCode群
	private final PackageBuilder mPackage;
	private ElementTypeBuilder mType;
	
	@Override
	public void jsonMapping( JSONWriter writer ) {
		writer.add( PACKAGE_HASH_CODE, mPackage.jsonHashCode() );
		writer.add( TYPE_HASH_CODE, mType.jsonHashCode() );
		writer.add( METHOD, mFactoryMethodModel );
	}
	
	public FactoryBuilder( JSONReader reader ) {
		mPackage = reader.get( PACKAGE_HASH_CODE ).pullObject();
		mType = reader.get( TYPE_HASH_CODE ).pullObject();
		mFactoryMethodModel = reader.get( METHOD ).asList( newFactoryMethodModel() );
	}
	
	public FactoryBuilder( PackageBuilder packaqe ) {
		mPackage = packaqe;	
		mFactoryMethodModel = newFactoryMethodModel();
	}

	public PackageBuilder getPackage() {
		return mPackage;
	}

	public void setType( ElementTypeBuilder type ) {
		mType = type;
	}

	public ElementTypeBuilder getType() {
		return mType;
	}

	public JSONListModel<FactoryMethodBuilder> getMethodListModel() {
		return mFactoryMethodModel;
	}
	
	@Override
	public boolean isEditable() {
		return true;
	}
	
	@Override
	public String jsonHashCode() {
		return Integer.toString( hashCode() );
	}

	public Factory build() {
		return new Factory( this );
	}

	public ElementType buildType() {
		return mType.build();
	}

	public ArrayList<FactoryMethod> buildMethods() {
		final ArrayList<FactoryMethod> list = new ArrayList<>();
		final int factoryMethodCount = mFactoryMethodModel.size();
		for (int i = 0; i < factoryMethodCount; ++i) {
			list.add( mFactoryMethodModel.get(i).build() );
		}
		return list;
	}

}
