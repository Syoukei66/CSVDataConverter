package biz.uro.CSVDataConverter.swing.builder;

import biz.uro.CSVDataConverter.CSVDataConverter;
import biz.uro.CSVDataConverter.database.element.ElementType.Tag;
import biz.uro.CSVDataConverter.swing.ProjectDataModel;
import biz.uro.CSVDataConverter.swing.json.IJSONObject;
import biz.uro.CSVDataConverter.swing.json.JSONReader;
import biz.uro.CSVDataConverter.swing.json.JSONWriter;
import biz.uro.CSVDataConverter.swing.json.JSONObjectBuilder.IJSONObjectBuilderPatch;
import biz.uro.CSVDataConverter.swing.old.DataListModel;
import biz.uro.CSVDataConverter.swing.old.JSONListModel;
import biz.uro.CSVDataConverter.swing.old.JSONListModel.IJsonParseProcess;
import biz.uro.CSVDataConverter.swing.project.ProjectSettingData;

public class ProjectData implements IJSONObject {
	
	public static final String EXTENTION = "csvSchema";
	
	private static final String PACKAGE		= "Package";
	private static final String TYPE		= "Type";
	private static final String STATEMENT	= "Statement";
	private static final String TABLE		= "Table";
	private static final String FACTORY		= "Factory";
	private static final String SETTING		= "Setting";
	
	protected final JSONListModel<PackageBuilder> mPackageDataModel;
	protected final JSONListModel<ElementTypeBuilder> mTypeDataModel;
	protected final JSONListModel<StatementBuilder> mStatementDataModel;
	protected final JSONListModel<TableBuilder> mTableDataModel;
	protected final JSONListModel<FactoryBuilder> mFactoryDataModel;
	
	protected final ProjectSettingData mSetting;
	
	private static JSONListModel<PackageBuilder> newPackageDataModel() {
		return new JSONListModel<>( PackageBuilder.class, new IJsonParseProcess<PackageBuilder>() {
			@Override
			public PackageBuilder parse( JSONReader reader, Class<PackageBuilder> clazz ) {
				return reader.asObject( clazz );
			}

			@Override
			public void loadConstants(JSONListModel<PackageBuilder> model) {
				for ( PackageBuilder primitive : PackageBuilder.TERMINALS ) {
					model.addConstants( primitive );
				}
			}
		});
	}
	
	private static JSONListModel<ElementTypeBuilder> newElementTypeDataModel() {
		return new JSONListModel<>( ElementTypeBuilder.class, new IJsonParseProcess<ElementTypeBuilder>() {
			@Override
			public ElementTypeBuilder parse( JSONReader reader, Class<ElementTypeBuilder> clazz ) {
				return reader.asAbstractObject( clazz )
						.addBase( clazz )
						.addImplements( ElementTypeBuilder._ForeignTable.class )
						.build();
			}

			@Override
			public void loadConstants( JSONListModel<ElementTypeBuilder> model ) {
				for ( ElementTypeBuilder primitive : ElementTypeBuilder.TERMINALS ) {
					model.addConstants( primitive );
				}				
			}
		});
	}
	
	private static JSONListModel<StatementBuilder> newStatementDataModel() {
		return new JSONListModel<>( StatementBuilder.class, new IJsonParseProcess<StatementBuilder>() {
			@Override
			public StatementBuilder parse( JSONReader reader, Class<StatementBuilder> clazz ) {
				return reader.asAbstractObject( clazz )
						.addImplements( StatementBuilder._Factory.class )
						.addImplements( StatementBuilder._ForeignTable.class )
						.build();
			}

			@Override
			public void loadConstants( JSONListModel<StatementBuilder> model ) {
				for ( StatementBuilder statement : StatementBuilder.TERMINALS ) {
					model.addConstants( statement );
				}	
			}
		});
	}
	
	private static JSONListModel<TableBuilder> newTableDataModel() {
		return new JSONListModel<>( TableBuilder.class, new IJsonParseProcess<TableBuilder>() {
			@Override
			public TableBuilder parse( JSONReader reader, Class<TableBuilder> clazz ) {
				return reader.asObject( clazz );
			}

			@Override
			public void loadConstants( JSONListModel<TableBuilder> model ) {
				
			}
		});
	}

	private static JSONListModel<FactoryBuilder> newFactoryDataModel() {
		return new JSONListModel<>( FactoryBuilder.class, new IJsonParseProcess<FactoryBuilder>() {
			@Override
			public FactoryBuilder parse( JSONReader reader, Class<FactoryBuilder> clazz ) {
				return reader.asObject( clazz );
			}

			@Override
			public void loadConstants( JSONListModel<FactoryBuilder> model ) {
				
			}
		});
	}
	
	@Override
	public void jsonMapping( JSONWriter writer ) {
		writer.add( PACKAGE, 	mPackageDataModel );
		writer.add( TYPE, 		mTypeDataModel );
		writer.add( STATEMENT, 	mStatementDataModel );
		writer.add( TABLE,		mTableDataModel );
		writer.add( FACTORY, 	mFactoryDataModel );
		writer.add( SETTING,	mSetting );
	}
	
	public ProjectData( JSONReader reader ) {
		mPackageDataModel = reader.get( PACKAGE ).asList( newPackageDataModel() );
		mTypeDataModel = reader.get( TYPE ).asList( newElementTypeDataModel() );
		mStatementDataModel = reader.get( STATEMENT ).asList( newStatementDataModel() );
		mTableDataModel = reader.get( TABLE ).asList( newTableDataModel() );
		mFactoryDataModel = reader.get( FACTORY ).asList( newFactoryDataModel() );
		mSetting = reader.get( SETTING ).asObject( ProjectSettingData.class );
		init();
	}
	
	public ProjectData() {
		mPackageDataModel = newPackageDataModel();
		mTypeDataModel = newElementTypeDataModel();
		mStatementDataModel = newStatementDataModel();
		mTableDataModel = newTableDataModel();
		mFactoryDataModel = newFactoryDataModel();
		mSetting = new ProjectSettingData();
		init();
	}
	
//	//テーブルが追加された時、テーブルのクラスを自動的に用意するメソッド
//	@SuppressWarnings("unchecked")
//	private <T1 extends IJSONDataModel, T2> void onTableChanged( JSONListModel<T1> model, Class<T2> tableClass, DataListModel<TableBuilder> tableModel ) throws Exception {
//		//テーブルのクラスのみを収集
//		final int typeCount = model.size();
//		final ArrayList<T2> tableTypes = new ArrayList<>();
//		int tableLastIndex = typeCount;
//		for ( int i = typeCount - 1; i >= 0; i-- ) {
//			final T1 type = model.get( i );
//			if ( (Tag)type.getClass().getMethod( "getTag" ).invoke( type ) != Tag.TABLE ) {
//				continue;
//			}
//			tableTypes.add( tableClass.cast( type ) );
//			model.remove( i );
//			tableLastIndex = i;
//		}
//		//現存するテーブルのクラスと現在のテーブルのデータモデルを比較して、足りない物があれば生成して追加する
//		final int tableCount = tableModel.getSize();
//		final int tableTypeCount = tableTypes.size();
//		for ( int i = 0; i < tableCount; i++ ) {
//			final TableBuilder table = tableModel.get( i );
//			T1 type = null;
//			for ( int j = 0; j < tableTypeCount; j++ ) {
//				final T2 tableType = tableTypes.get( j );
//				//データモデルの指定したテーブルのクラスが存在すればアップデート
//				final TableBuilder targetTable = (TableBuilder)tableType.getClass().getMethod( "getTable" ).invoke( tableType );
//				if ( targetTable == table ) {
//					type = (T1)tableType;
//					break;
//				}
//			}
//			if ( type == null ) {
//				type = (T1)tableClass.getConstructor( TableBuilder.class ).newInstance( table );
//			}
//			model.add( tableLastIndex + i, type );
//		}		
//	}
	
	private void init() {
//		mTableDataModel.addListChangedListener( new IOnListChangedListener<TableBuilder>() {
//			@Override
//			public void onListChanged( DataListModel<TableBuilder> model ) {
//				try {
//					onTableChanged( mTypeDataModel, ElementTypeBuilder._ForeignTable.class, model );
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				try {
//					onTableChanged( mStatementDataModel, StatementBuilder._ForeignTable.class, model );
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
	}
	
	public void generateProgram( String inputPath, String outputPath ) throws Exception {
		final CSVDataConverter converter = new CSVDataConverter( mStatementDataModel.asList( new StatementBuilder[mStatementDataModel.size()] ) );
		final int tableCount = mTableDataModel.getSize();
		for ( int i = 0; i < tableCount; i++ ) {
			converter.registerTable( mTableDataModel.get( i ) );
		}
		final int packageCount = mPackageDataModel.getSize();
		for ( int i = 0; i < packageCount; i++ ) {
			converter.registerPackage( mPackageDataModel.get( i ) );
		}
		converter.convert( inputPath, outputPath );
	}
	
	public JSONListModel<TableBuilder> getTableDataModel() {
		return mTableDataModel;
	}

	public JSONListModel<StatementBuilder> getStatementModel() {
		return mStatementDataModel;
	}
	
	public JSONListModel<ElementTypeBuilder> getElementTypeModel() {
		return mTypeDataModel;
	}
	
	public JSONListModel<PackageBuilder> getPackageModel() {
		return mPackageDataModel;
	}

	public DataListModel<FactoryBuilder> getFactoryModel() {
		return mFactoryDataModel;
	}
	
	public void setupConstants() {
		mPackageDataModel.loadConstants();
		mStatementDataModel.loadConstants();
		mTypeDataModel.loadConstants();
		mTableDataModel.loadConstants();
		mFactoryDataModel.loadConstants();
	}

	@Override
	public String jsonHashCode() {
		return null;
	}
	
	public boolean useCSV() {
		final int tableCount = mTableDataModel.size();
		for ( int i = 0; i < tableCount; i++ ) {
			final TableBuilder table = mTableDataModel.get( i );
			if ( table.getRecordDefinitionType().useCSV ) {
				return true;
			}
		}
		return false;
	}

	public TableBuilder createTable() {
		final PackageBuilder packaqe = new PackageBuilder( Tag.TABLE );
		mPackageDataModel.addElement( packaqe );
		final TableBuilder tableBuilder = new TableBuilder( packaqe );

		final ElementTypeBuilder._ForeignTable type = new ElementTypeBuilder._ForeignTable( tableBuilder );
		mTypeDataModel.addElement( type );
		
		final StatementBuilder._ForeignTable statement = new StatementBuilder._ForeignTable( tableBuilder );
		mStatementDataModel.addElement( statement );
		
		return tableBuilder;
	}
	
	public FactoryBuilder createFactory() {
		final PackageBuilder packaqe = new PackageBuilder( Tag.FACTORY );
		mPackageDataModel.addElement( packaqe );
		final FactoryBuilder factoryBuilder = new FactoryBuilder( packaqe );
		
		final ElementTypeBuilder._Factory type = new ElementTypeBuilder._Factory( factoryBuilder );
		mTypeDataModel.addElement( type );
		
		final StatementBuilder._Factory statement = new StatementBuilder._Factory();
		mStatementDataModel.addElement( statement );
		
		return factoryBuilder;
	}
	
	public ElementTypeBuilder createType() {
		final PackageBuilder packaqe = new PackageBuilder( Tag.USER );
		ProjectDataModel.INSTANCE.getProjectData().getPackageModel().addElement( packaqe );
		
		final ElementTypeBuilder type = new ElementTypeBuilder( Tag.USER, packaqe );
		
		return type;
	}

}
