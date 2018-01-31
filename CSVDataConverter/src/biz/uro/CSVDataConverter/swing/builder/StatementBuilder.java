package biz.uro.CSVDataConverter.swing.builder;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

import biz.uro.CSVDataConverter.database.element.ElementParser;
import biz.uro.CSVDataConverter.database.element.ElementType;
import biz.uro.CSVDataConverter.database.element.ElementType.Tag;
import biz.uro.CSVDataConverter.database.element.Statement;
import biz.uro.CSVDataConverter.swing.builder.PackageBuilder.IPackageChangeListener;
import biz.uro.CSVDataConverter.swing.builder.validator.ValidatorKindEnum_Static;
import biz.uro.CSVDataConverter.swing.builder.validator.kind.ValidatorKind;
import biz.uro.CSVDataConverter.swing.builder.validator.kind.list.ValidatorKind_Factory;
import biz.uro.CSVDataConverter.swing.builder.validator.kind.list.ValidatorKind_Table;
import biz.uro.CSVDataConverter.swing.json.IJSONDataModel;
import biz.uro.CSVDataConverter.swing.json.JSONClassID;
import biz.uro.CSVDataConverter.swing.json.JSONReader;
import biz.uro.CSVDataConverter.swing.json.JSONWriter;
import biz.uro.CSVDataConverter.swing.json.JSONObjectMap.IJSONObjectLoadedListener;
import biz.uro.CSVDataConverter.swing.old.JSONListModel;
import biz.uro.CSVDataConverter.swing.old.JSONListModel.IJsonParseProcess;

public abstract class StatementBuilder implements IJSONDataModel {

	private static final String TAG_HASH_CODE	= "TagHashCode";
	private static final String NAME			= "Name";
	private static final String TYPE_HASH_CODE	= "TypeHashCode";
	private static final String PARSER			= "Parser";
	private static final String VALIDATOR		= "Validator";
	
	private final Tag mTag;
	private String mName;
	private ElementTypeBuilder mType;
	private final JSONListModel<ElementParserBuilder> mParsers;
	protected final JSONListModel<ValidatorKind> mValidatorModel;

	private static JSONListModel<ValidatorKind> newValidatorKindModel() {
		return new JSONListModel<>( ValidatorKind.class, new IJsonParseProcess<ValidatorKind>() {
			@Override
			public ValidatorKind parse( JSONReader reader, Class<ValidatorKind> clazz ) {
				return reader.asAbstractObject( clazz )
						.addImplements( ValidatorKind_Table.class )
						.addImplements( ValidatorKind_Factory.class )
						.build();
			}
			@Override
			public void loadConstants(JSONListModel<ValidatorKind> model) {
			}
		} );
	}
	
	private static JSONListModel<ElementParserBuilder> newElementParserModel() {
		return new JSONListModel<>( ElementParserBuilder.class, new IJsonParseProcess<ElementParserBuilder>() {
			@Override
			public ElementParserBuilder parse( JSONReader reader, Class<ElementParserBuilder> clazz ) {
				return reader.asAbstractObject( clazz )
						.addImplements( ElementParserBuilder._ForeignKey.class )
						.addImplements( ElementParserBuilder._Function.class )
						.build();
			}

			@Override
			public void loadConstants(JSONListModel<ElementParserBuilder> model) {
				
			}
		} );
	}
	
	@Override
	public void jsonMapping( JSONWriter writer ) {
		writer.add( TAG_HASH_CODE, mTag.jsonHashCode() );
		writer.add( NAME, mName );
		writer.add( TYPE_HASH_CODE, mType.jsonHashCode() );
		writer.add( PARSER, mParsers );
		writer.add( VALIDATOR, mValidatorModel );
	}

	public StatementBuilder( JSONReader reader ) {
		mTag = reader.get( TAG_HASH_CODE ).pullObject();
		mName = reader.get( NAME ).asText();
		mType = reader.get( TYPE_HASH_CODE ).pullObject();
		mParsers = reader.get( PARSER ).asList( newElementParserModel() );
		mValidatorModel = reader.get( VALIDATOR ).asList( newValidatorKindModel() );
	}
	
	public StatementBuilder( Tag tag ) {
		this( tag, newElementParserModel() );
	}
	
	public StatementBuilder( Tag tag, JSONListModel<ElementParserBuilder> model ) {
		mTag = tag;
		mParsers = model;
		mValidatorModel = newValidatorKindModel();
	}
	
	public Statement build() {
		return new Statement( this );
	}
	
	public void addValidator( ValidatorKind validator ) {
		mValidatorModel.addElement( validator );
	}
	
	public Tag getTag() {
		return mTag;
	}
	
	public String getName() {
		return mName;
	}
	
	public void setName( String name ) {
		this.mName = name;
	}

	public ElementTypeBuilder getType() {
		return mType;
	}

	public void setType( ElementTypeBuilder type ) {
		this.mType = type;
	}
	
	public JSONListModel<ElementParserBuilder> getParserListModel() {
		return mParsers;
	}
	
	public JSONListModel<ValidatorKind> getValidatorModel() {
		return mValidatorModel;
	}

	@Override
	public String toString() {
		assert mName != null;
		assert mType != null : mName;
		assert mType.getPackage() != null : mName;
		assert mType.getPackage().getClassName() != null : mName;
		return String.format( "%-16s %-16s", mName, mType.getPackage().getClassName() );
	}

	@Override
	public String jsonHashCode() {
		return Integer.toString( hashCode() );
	}

	public ElementType buildType() {
		return mType.build();
	}

	/* =============================================================================== 
	 * implements
	 * =============================================================================== */
	
	@JSONClassID( "Table" )
	public static class _ForeignTable extends StatementBuilder implements IPackageChangeListener {

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
				public void onLoaded(Object obj) {
					mTable = (TableBuilder)obj;
					mTable.getPackage().addPackageChangeListener( _ForeignTable.this );
				}
			} );
		}
		
		public _ForeignTable( final TableBuilder table ) {
			super( Tag.TABLE );
			mTable = table;
			addValidator( new ValidatorKind_Table( mTable ) );
			setName( mTable.getPackage().getClassName() );
			setType( mTable.getType() );
			getParserListModel().addElement( new ElementParserBuilder._ForeignKey( mTable ) );			
			mTable.setStatement( this );
			mTable.getPackage().addPackageChangeListener( this );
		}
		
		public TableBuilder getTable() {
			return mTable;
		}
		
		@Override
		public void onChangePackage( PackageBuilder packaqe ) {
			setName( packaqe.getClassName() );
		}
		
		@Override
		public boolean isEditable() {
			return false;
		}
		
	}

	@JSONClassID( "Factory" )
	public static class _Factory extends StatementBuilder {
		
		private ValidatorKind_Factory mValidator;
		
		public _Factory( JSONReader reader ) {
			super( reader );
			mValidator = (ValidatorKind_Factory) mValidatorModel.get( 0 );
		}
		
		public _Factory( JSONListModel<ElementParserBuilder> dataModel ) {
			super( Tag.FACTORY, dataModel );
			init();
		}

		public _Factory() {
			super( Tag.FACTORY );
			init();
		}
		
		private void init() {
			mValidator = new ValidatorKind_Factory( this );
			addValidator( mValidator );			
		}
		
		@Override
		public void setName( String name ) {
			super.setName( name );
			mValidator.setName( name );
		}

		@Override
		public boolean isEditable() {
			return true;
		}
	}
	/* =============================================================================== 
	 * implements　(Primitive)
	 * =============================================================================== */

	public static final _Terminal _int		= new _Terminal( ElementParserBuilder._int ) {
		{
			for ( ValidatorKindEnum_Static kind : ValidatorKindEnum_Static.INT_VALUES ) {
				addValidator( kind.get() );
			}
		}		
	};
	public static final _Terminal _float	= new _Terminal( ElementParserBuilder._float ) {
		{
			for ( ValidatorKindEnum_Static kind : ValidatorKindEnum_Static.FLOAT_VALUES ) {
				addValidator( kind.get() );
			}
		}
	};
	public static final _Terminal _double	= new _Terminal( ElementParserBuilder._double ) {
		{
			for ( ValidatorKindEnum_Static kind : ValidatorKindEnum_Static.FLOAT_VALUES ) {
				addValidator( kind.get() );
			}
		}
	};
	public static final _Terminal _boolean	= new _Terminal( ElementParserBuilder._boolean ) {
		{
			addValidator( ValidatorKindEnum_Static.BOOLEAN_LIST.get() );
		}
	};
	public static final _Terminal _String	= new _Terminal( ElementParserBuilder._String ) {
		{
			for ( ValidatorKindEnum_Static kind : ValidatorKindEnum_Static.STRING_VALUES ) {
				addValidator( kind.get() );
			}
		}
	};
	
	public static final _Terminal[] TERMINALS = new _Terminal[] {
		_int, _float, _double, _boolean, _String
	};
		
	public ArrayList<ElementParser> buildParsers() {
		final ArrayList<ElementParser> parserList = new ArrayList<>();
		final int parserCount = mParsers.size();
		for ( int i = 0; i < parserCount; i++ ) {
			parserList.add( mParsers.get( i ).build() );
		}
		return parserList;
	}

	public static class _Terminal extends StatementBuilder {
		private final String mHashCode;
		public _Terminal( ElementParserBuilder._Terminal parser ) {
			super( Tag.PRIMITIVE );
			final ElementTypeBuilder type = parser.getType();
			final String className = type.getPackage().getClassName();
			setName( className );
			setType( type );
			getParserListModel().addElement( parser );
			mHashCode = "Statement_" + StringUtils.capitalize( className );
		}
		@Override
		public boolean isEditable() {
			return false;
		}
		@Override
		public String jsonHashCode() {
			return mHashCode;
		}
	}

}
