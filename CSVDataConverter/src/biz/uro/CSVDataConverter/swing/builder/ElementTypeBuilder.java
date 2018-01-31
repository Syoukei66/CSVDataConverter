package biz.uro.CSVDataConverter.swing.builder;

import org.apache.commons.lang3.StringUtils;

import biz.uro.CSVDataConverter.database.element.ElementType;
import biz.uro.CSVDataConverter.database.element.ElementType.Tag;
import biz.uro.CSVDataConverter.swing.json.IJSONDataModel;
import biz.uro.CSVDataConverter.swing.json.JSONClassID;
import biz.uro.CSVDataConverter.swing.json.JSONReader;
import biz.uro.CSVDataConverter.swing.json.JSONWriter;
import biz.uro.CSVDataConverter.swing.json.JSONObjectMap.IJSONObjectLoadedListener;

public class ElementTypeBuilder implements IJSONDataModel {

	private static final String TAG_HASH_CODE		= "TagHashCode";
	private static final String PACKAGE_HASH_CODE	= "PackageHashCode";
	
	private final Tag mTag;
	private PackageBuilder mPackage;
	
	@Override
	public void jsonMapping( JSONWriter writer ) {
		writer.add( TAG_HASH_CODE, mTag.jsonHashCode() );
		writer.add( PACKAGE_HASH_CODE, mPackage.jsonHashCode() );
	}
	
	public ElementTypeBuilder( JSONReader reader ) {
		mTag = reader.get( TAG_HASH_CODE ).pullObject();
		mPackage = reader.get( PACKAGE_HASH_CODE ).pullObject();
	}
	
	public ElementTypeBuilder( Tag tag, PackageBuilder packaqe ) {
		mTag = tag;
		mPackage = packaqe;
	}

	public ElementType build() {
		return new ElementType( this );
	}

	public Tag getTag() {
		return mTag;
	}
		
	public PackageBuilder getPackage() {
		return mPackage;
	}
		
	public boolean isTerminal() {
		return false;
	}
	
	@Override
	public boolean isEditable() {
		return mTag.isEditable();
	}
	
	@Override
	public String toString() {
		final StringBuilder str = new StringBuilder();
		str.append( String.format( "%-16s", mPackage.getClassName() ) );
		if ( mPackage.getPackage() != null ) {
			final String[] packagePaths = mPackage.getPackage().split( "\\.", 0 );
			str.append( String.format( "%-16s", "[" + packagePaths[packagePaths.length - 1] + "]" ) );
		}
		else {
			str.append( String.format( "%-16s", "" ) );
		}
		str.append( String.format( "%-16s", StringUtils.capitalize( StringUtils.lowerCase( mTag.name() ) ) ) );
		return str.toString();
	}

	@Override
	public String jsonHashCode() {
		return Integer.toString( hashCode() );
	}
	
	/* =============================================================================== 
	 * implements
	 * =============================================================================== */
	
	@JSONClassID( "Table" )
	public static class _ForeignTable extends ElementTypeBuilder {

		private static final String TABLE_HASH_CODE = "TableHashCode";
		
		private TableBuilder mTable;
		
		@Override
		public void jsonMapping( JSONWriter writer ) {
			super.jsonMapping( writer );
			writer.add( TABLE_HASH_CODE, mTable.jsonHashCode() );
		}
		
		public _ForeignTable( JSONReader reader ) {
			super( reader );
			reader.get( TABLE_HASH_CODE ).reserveObject( new IJSONObjectLoadedListener() {
				@Override
				public void onLoaded( Object obj ) {
					mTable = (TableBuilder)obj;
				}
			} );
		}
		
		public _ForeignTable( TableBuilder table ) {
			super( Tag.TABLE, table.getPackage() );
			mTable = table;
			mTable.setType( this );
		}
		
		public SchemaBuilder getSchemaBuilder() {
			return mTable.getSchema();
		}
		
		@Override
		public boolean isTerminal() {
			return false;
		}

	}
	
	@JSONClassID( "Factory" )
	public static class _Factory extends ElementTypeBuilder {

		private static final String FACTORY_HASH_CODE = "FactoryHashCode";
		
		private FactoryBuilder mFactory;
		
		@Override
		public void jsonMapping( JSONWriter writer ) {
			super.jsonMapping( writer );
			writer.add( FACTORY_HASH_CODE, mFactory.jsonHashCode() );
		}
		
		public _Factory( JSONReader reader ) {
			super( reader );
//			reader.get( FACTORY_HASH_CODE ).reserveObject( new IJSONObjectLoadedListener() {
//				@Override
//				public void onLoaded( Object obj ) {
//					mFactory = (FactoryBuilder)obj;
//				}
//			} );
		}
		
		public _Factory( FactoryBuilder factory ) {
			super( Tag.FACTORY, factory.getPackage() );
			mFactory = factory;
			mFactory.setType( this );
		}
		
		@Override
		public boolean isTerminal() {
			return false;
		}

	}	
	
	/* =============================================================================== 
	 * implements (Primitive)
	 * =============================================================================== */

	public static final ElementTypeBuilder _int	= new _Primitive( PackageBuilder._int, "Integer" );
	public static final ElementTypeBuilder _float = new _Primitive( PackageBuilder._float, "Float" );
	public static final ElementTypeBuilder _double = new _Primitive( PackageBuilder._double, "Double" );
	public static final ElementTypeBuilder _boolean = new _Primitive( PackageBuilder._boolean, "Boolean" );
	public static final ElementTypeBuilder _String = new _Terminal( Tag.OBJECT, PackageBuilder._String );
	
	public static final ElementTypeBuilder[] TERMINALS = new ElementTypeBuilder[] {
		_int,
		_float,
		_double,
		_boolean,
		_String,
	};
	
	private static class _Primitive extends _Terminal {
		private String mWrapperName;
		public _Primitive( PackageBuilder packaqe, String wrapperName ) {
			super( Tag.PRIMITIVE, packaqe );
			mWrapperName = wrapperName;
		}
		
		public ElementType build() {
			return new ElementType( this, mWrapperName );
		}

	}
	
	private static class _Terminal extends ElementTypeBuilder {
		public _Terminal( Tag tag, PackageBuilder packaqe ) {
			super( tag, packaqe );
		}
		@Override
		public boolean isTerminal() {
			return true;
		}
		@Override
		public String jsonHashCode() {
			return "ElementType_" + StringUtils.capitalize( getPackage().getClassName() );
		}
	}
	
}
