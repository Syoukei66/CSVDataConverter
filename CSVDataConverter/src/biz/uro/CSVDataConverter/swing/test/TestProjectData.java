package biz.uro.CSVDataConverter.swing.test;

import biz.uro.CSVDataConverter.database.element.ElementType.Tag;
import biz.uro.CSVDataConverter.swing.ProjectDataModel;
import biz.uro.CSVDataConverter.swing.builder.ElementParserBuilder;
import biz.uro.CSVDataConverter.swing.builder.ElementBuilder;
import biz.uro.CSVDataConverter.swing.builder.ElementTypeBuilder;
import biz.uro.CSVDataConverter.swing.builder.PackageBuilder;
import biz.uro.CSVDataConverter.swing.builder.ProjectData;
import biz.uro.CSVDataConverter.swing.builder.StatementBuilder;
import biz.uro.CSVDataConverter.swing.builder.TableBuilder;
import biz.uro.CSVDataConverter.swing.builder.validator.Validator;
import biz.uro.CSVDataConverter.swing.builder.validator.ValidatorKindEnum_Static;
import biz.uro.CSVDataConverter.swing.builder.validator.kind.ValidatorKind;
import biz.uro.CSVDataConverter.swing.old.JSONListModel;

public class TestProjectData extends ProjectData {

	public TestProjectData() {
		super();
		setupConstants();
		createTestStatement();
		createTestTable1();
	}
	
	//1.各Primi
	//2.
	//3.
	private void createTestStatement() {
		
	}
	
	//Table以外のelementを持った要素
	private void createTestTable1() {
		//package
		final PackageBuilder package1 = createPackage1();
		final ElementTypeBuilder type1 = createType1( package1 );
		final StatementBuilder._Factory statement1 = createStatement1( type1 );		
		final TableBuilder table1 = createTable1();
		StatementBuilder._ForeignTable tableStatement = null;
		final int statementCount = mStatementDataModel.size();
		for ( int i = 0; i < statementCount; i++ ) {
			final StatementBuilder statement = mStatementDataModel.get( i );
			if ( !(statement instanceof StatementBuilder._ForeignTable) ) {
				continue;
			}
			if ( statement.getType() == table1.getType() ) {
				tableStatement = (StatementBuilder._ForeignTable)statement;
			}
		}
		createTable2( tableStatement, statement1 );
	}
	
	private PackageBuilder createPackage1() { 
		final PackageBuilder packaqe = new PackageBuilder( Tag.USER );			
		packaqe.setPackage( "test.testPpackage" );
		packaqe.setClassName( "Test_Package" );
		mPackageDataModel.addElement( packaqe );		
		return packaqe;
	}

	private ElementTypeBuilder createType1( PackageBuilder packaqe ) {
		final ElementTypeBuilder type = new ElementTypeBuilder( Tag.USER, packaqe );
		mTypeDataModel.addElement( type );
		return type;
	}
	
	private StatementBuilder._Factory createStatement1( ElementTypeBuilder type ) {
		final StatementBuilder._Factory statement = new StatementBuilder._Factory();
		statement.setName( "Test_Function" );
		statement.setType( type );
		final JSONListModel<ElementParserBuilder> parserList = statement.getParserListModel();
		for ( int j = 0; j < 5; j++ ) {
			final JSONListModel<ElementBuilder> elements = new JSONListModel<>( ElementBuilder.class );
			for ( int i = 0; i < 8; i++ ) {
				final ElementBuilder element = new ElementBuilder();
				element.setFieldName( "arg" + i );
				element.setIsPrimaryKey( i == 0 );
				element.setIsNotNull( i < 4 );
				element.setOutName( "引数" + i );
				element.setSize( i == 5 ? 3 : 1 );
				final StatementBuilder[] statements = StatementBuilder.TERMINALS;
				final StatementBuilder funcStatement = statements[i % statements.length];
				final JSONListModel<ValidatorKind> validatorKindModel = funcStatement.getValidatorModel();
				element.setStatement( funcStatement );
				final ValidatorKind validatorKind = validatorKindModel.get( i % validatorKindModel.size() );
				final Validator validator = validatorKind.create();
				validator.getCondition().test( i );
				element.setValidator( validator );
				elements.addElement( element );
			}
//			final ElementParserBuilder._Function parser = new ElementParserBuilder._Function( elements );
//			parser.setGetterProgram( "Getter" );
//			parser.setComment( "テスト用関数その" + j );
//			parser.setIdentifer( "identifier" + j );
//			parserList.addElement( parser );
		}
		mStatementDataModel.addElement( statement );
		return statement;
	}
	
	private TableBuilder createTable1() {
		final TableBuilder table = ProjectDataModel.INSTANCE.getProjectData().createTable();
		table.getPackage().setClassName( "TestTable_Chara" );
		table.getPackage().setPackage( "test.table.chara" );
		table.setProgramFormat( "\"%03d\",#0" );
		final JSONListModel<ElementBuilder> tableElementModel = table.getSchema().getElementDataModel();

		final ElementBuilder element_id = new ElementBuilder();
		element_id.setFieldName( "id" );
		element_id.setOutName( "id" );
		element_id.setIsNotNull( true );
		element_id.setIsPrimaryKey( true );
		element_id.setSize( 1 );
		element_id.setStatement( StatementBuilder._int );
		final Validator element_id_validator = ValidatorKindEnum_Static.INT_NUMBER_INRANGE.get().create();
		element_id_validator.getCondition().model1().setValue( 1 );
		element_id_validator.getCondition().model2().setValue( 999 );
		element_id.setValidator( element_id_validator );
		tableElementModel.addElement( element_id );

		final ElementBuilder element_name = new ElementBuilder();
		element_name.setFieldName( "name" );
		element_name.setOutName( "名前" );
		element_name.setIsNotNull( false );
		element_name.setIsPrimaryKey( false );
		element_name.setSize( 1 );
		element_name.setStatement( StatementBuilder._String );
		final Validator element_name_validator = ValidatorKindEnum_Static.STRING_NUMBER_INRANGE.get().create();
		element_name_validator.getCondition().model1().setValue( 1 );
		element_name_validator.getCondition().model2().setValue( 16 );
		element_name.setValidator( element_name_validator );
		tableElementModel.addElement( element_name );

		table.setLogicElement( element_name );
		
		mTableDataModel.addElement( table );			
		return table;
	}
	
	private TableBuilder createTable2( StatementBuilder tableStatement, StatementBuilder._Factory func ) {
		final TableBuilder table = ProjectDataModel.INSTANCE.getProjectData().createTable();
		table.getPackage().setClassName( "TestTable2" );
		table.getPackage().setPackage( "test.table2" );
		table.setProgramFormat( "\"%03d_%02d\",#0.0,#1" );
		final JSONListModel<ElementBuilder> tableElementModel = table.getSchema().getElementDataModel();

		final ElementBuilder element_id = new ElementBuilder();
		element_id.setFieldName( "charaID" );
		element_id.setOutName( "キャラID" );
		element_id.setIsNotNull( true );
		element_id.setIsPrimaryKey( true );
		element_id.setSize( 1 );
		element_id.setStatement( tableStatement );
		final Validator element_id_validator = tableStatement.getValidatorModel().get(0).create();
		element_id.setValidator( element_id_validator );
		tableElementModel.addElement( element_id );

		final ElementBuilder element_spellID = new ElementBuilder();
		element_spellID.setFieldName( "spellID" );
		element_spellID.setOutName( "スペルID" );
		element_spellID.setIsNotNull( true );
		element_spellID.setIsPrimaryKey( true );
		element_spellID.setSize( 1 );
		element_spellID.setStatement( StatementBuilder._int );
		final Validator element_spellID_validator = ValidatorKindEnum_Static.INT_NUMBER_INRANGE.get().create();
		element_spellID_validator.getCondition().model1().setValue( 1 );
		element_spellID_validator.getCondition().model2().setValue( 99 );
		element_spellID.setValidator( element_spellID_validator );
		tableElementModel.addElement( element_spellID );
				
		final ElementBuilder element_name = new ElementBuilder();
		element_name.setFieldName( "name" );
		element_name.setOutName( "名前" );
		element_name.setIsNotNull( false );
		element_name.setIsPrimaryKey( false );
		element_name.setSize( 1 );
		element_name.setStatement( StatementBuilder._String );
		final Validator element_name_validator = ValidatorKindEnum_Static.STRING_NUMBER_INRANGE.get().create();
		element_name_validator.getCondition().model1().setValue( 1 );
		element_name_validator.getCondition().model2().setValue( 16 );
		element_name.setValidator( element_name_validator );
		tableElementModel.addElement( element_name );

		final ElementBuilder element_func = new ElementBuilder();
		element_func.setFieldName( "func" );
		element_func.setOutName( "関数" );
		element_func.setIsNotNull( false );
		element_func.setIsPrimaryKey( false );
		element_func.setSize( 1 );
		element_func.setStatement( func );
		final Validator element_func_validator = func.getValidatorModel().get( 0 ).create();
		element_func.setValidator( element_func_validator );
		tableElementModel.addElement( element_func );

		mTableDataModel.addElement( table );			
		return table;
	}
}
