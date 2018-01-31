package biz.uro.CSVDataConverter.swing.builder;

import org.apache.commons.lang3.StringUtils;

import biz.uro.CSVDataConverter.database.Table;
import biz.uro.CSVDataConverter.database.element.ElementParser;
import biz.uro.CSVDataConverter.swing.json.IJSONDataModel;
import biz.uro.CSVDataConverter.swing.json.JSONClassID;
import biz.uro.CSVDataConverter.swing.json.JSONReader;
import biz.uro.CSVDataConverter.swing.json.JSONWriter;
import biz.uro.CSVDataConverter.swing.json.JSONObjectMap.IJSONObjectLoadedListener;

public abstract class ElementParserBuilder implements IJSONDataModel {

	public abstract ElementParser build();

	@Override
	public void jsonMapping( JSONWriter writer ) {
	}
	
	public ElementParserBuilder( JSONReader reader ) {

	}

	public ElementParserBuilder() {
	}
	
	public PackageBuilder getImport() {
		return null;
	}
	
	@Override
	public String jsonHashCode() {
		return null;
	}
	
	/* ========================================================================= 
	 * implements
	 * ========================================================================= */
	
	@JSONClassID( "Table" )
	public static class _ForeignKey extends ElementParserBuilder {
		
		private static final String TABLE_HASH_CODE = "TableHashCode";
		
		private TableBuilder mTable;
		
		@Override
		public void jsonMapping( JSONWriter writer ) {
			super.jsonMapping( writer );
			writer.add( TABLE_HASH_CODE, mTable.jsonHashCode() );
		}
		
		public _ForeignKey( JSONReader reader ) {
			super( reader );
			reader.get( TABLE_HASH_CODE ).reserveObject( new IJSONObjectLoadedListener() {
				@Override
				public void onLoaded( Object obj ) {
					mTable = (TableBuilder)obj;
				}
			} );
		}
		
		public _ForeignKey( TableBuilder table ) {
			mTable = table;
		}
		
		@Override
		public boolean isEditable() {
			return false;
		}
		
		@Override
		public ElementParser build() {
			final Table table = mTable.getBuildedInstance();
			if ( table == null ) {
				throw new RuntimeException( mTable.getPackage().getClassName() + " is not builded." );
			}
			return ElementParser._ForeignKey( table );
		}
		
	}
	
	//FunctionをFactoryという新しい概念に切り替える
	//
	@JSONClassID( "Function" )
	public static class _Function extends ElementParserBuilder {
		
		private static final String FACTORY_HASH_CODE = "FactoryHashCode";
		
		private FactoryBuilder mFactory;
		
		@Override
		public void jsonMapping( JSONWriter writer ) {
			super.jsonMapping( writer );
			writer.add( FACTORY_HASH_CODE, mFactory.jsonHashCode() );
		}
		
		public _Function( JSONReader reader ) {
			super( reader );
			mFactory = reader.get( FACTORY_HASH_CODE ).pullObject();
		}
		
		public _Function( FactoryBuilder factory ) {
			mFactory = factory;
		}
		
		public void SetFactoryBuilder( FactoryBuilder factory ) {
			mFactory = factory;
		}
		
		public FactoryBuilder getFactoryBuilder() {
			return mFactory;
		}
		
		@Override
		public boolean isEditable() {
			return true;
		}
		
		@Override
		public ElementParser build() {
			return new ElementParser._Factory( mFactory );
		}
		
		@Override
		public String toString() {
			return String.format( "Factory::%s", mFactory.getPackage().getClassName() );
		}
		
	}
	
	/* =============================================================================== 
	 * implements (Primitive)
	 * =============================================================================== */

	public static abstract class _Terminal extends ElementParserBuilder {
		private final ElementTypeBuilder mType;
		public abstract ElementParser._Terminal build();
		
		@Override
		public void jsonMapping( JSONWriter writer ) {
			/* do nothing */
		}
		
		public _Terminal( ElementTypeBuilder type ) {
			mType = type;
		}
		
		public ElementTypeBuilder getType() {
			return mType;
		}
		
		@Override
		public boolean isEditable() {
			return false;
		}
		
		@Override
		public String toString() {
			return String.format( "%-16s", mType.getPackage().getClassName() );
		}

		@Override
		public String jsonHashCode() {
			return "Parser_" + StringUtils.capitalize( mType.getPackage().getClassName() );
		}

	}

	public static abstract class _Primitive extends _Terminal {
		public _Primitive( ElementTypeBuilder type ) {
			super( type );
		}

		public abstract ElementParser._Primitive build();
	}

	public static final _Primitive _int = new _Primitive( ElementTypeBuilder._int ) {
		@Override
		public ElementParser._Primitive build() {
			return ElementParser._int;
		}
	};
	
	public static final _Primitive _float = new _Primitive( ElementTypeBuilder._float ) {
		@Override
		public ElementParser._Primitive build() {
			return ElementParser._float;
		}
	};
	
	public static final _Primitive _double = new _Primitive( ElementTypeBuilder._double ) {
		@Override
		public ElementParser._Primitive build() {
			return ElementParser._double;
		}
	};
	
	public static final _Primitive _boolean = new _Primitive( ElementTypeBuilder._boolean ) {
		@Override
		public ElementParser._Primitive build() {
			return ElementParser._boolean;
		}
	};
	
	public static final _Terminal _String = new _Terminal( ElementTypeBuilder._String ) {
		@Override
		public ElementParser._Terminal build() {
			return ElementParser._String;
		}
	};
	
	public static final _Terminal[] TERMINALS = new _Terminal[] {
		_int, _float, _double, _boolean, _String
	};

}
