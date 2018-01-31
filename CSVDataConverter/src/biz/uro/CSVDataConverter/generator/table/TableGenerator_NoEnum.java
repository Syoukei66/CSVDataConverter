package biz.uro.CSVDataConverter.generator.table;

import static biz.uro.CSVDataConverter.generator.table.TableGeneratorConstants.ENUM_VALUES;
import static biz.uro.CSVDataConverter.generator.table.TableGeneratorConstants.TABLE_GETTER_METHOD_NAME;

import java.util.List;

import biz.uro.CSVDataConverter.database.ISchema;
import biz.uro.CSVDataConverter.database.PrimaryKey;
import biz.uro.CSVDataConverter.database.Table;
import biz.uro.CSVDataConverter.string.ProgramBuilder;
import biz.uro.CSVDataConverter.swing.builder.TableBuilder;

public class TableGenerator_NoEnum extends TableGenerator {

	private static final String PRIVATE_LIST_NAME = "PRIVATE_LIST";
	private static final String IS_LOADED_NAME = "IS_LOADED";

	private static final String PRE_LOAD_METHOD_NAME = "preLoad";
	private static final String POST_LOAD_METHOD_NAME = "postLoad";
	

	@Override
	protected void registerImports( Table table, List<String> list ) {
		list.add( "java.util.ArrayList" );
		list.add( "java.util.List" );
		super.registerImports( table, list );
	}

	@Override
	protected ISchema<?> getConstructorSchema( Table table ) {
		return table.getSchema().getPrimaryKey();
	}

	@Override
	protected ISchema<?> getSchema( Table table ) {
		return table.getSchema();
	}

	@Override
	protected String generateClassDefinitionByTable( Table table, TableBuilder tableBuilder ) {
		final ProgramBuilder str = new ProgramBuilder();
		str.append( "public class " + generateTableName( table ) + " {\n" );
		str.addTab();
			str.append( "\n" );
			//statics
			str.append( generateStatics( table ) );
			str.changeBlock();
			//recordGetter
			str.append( generateRecordGetter( table ) );
			str.changeBlock();
			//preLoad
			str.append( generatePreLoad( table ) );
			str.changeBlock();
			//postLoad
			str.append( generatePostLoad( table ) );
			str.changeBlock();
			//fields
			str.append( generateFields( table ) );
			str.changeBlock();
			//constructor
			str.append( generateConstructor( table ) );
			str.changeBlock();
			//getter
			str.append( generateGetter( table ) );
			str.changeBlock();
			//parserMethod
			str.append( generateParseMainMethod( table, tableBuilder ) );
			str.changeBlock();
		str.subTab();
		str.append( "}\n" );
		return str.toString();
	}

	private String generateStatics( Table table ) {
		final ProgramBuilder str = new ProgramBuilder();
		final String tableName = generateTableName( table );
		str.var( "private static", true, "List<" + tableName + ">", PRIVATE_LIST_NAME, "new ArrayList<>()" );
		str.var( "private static", false, "boolean", IS_LOADED_NAME, null );
		str.var_Array( "public static", false, tableName, ENUM_VALUES, null );
		return str.toString();
	}
	
	protected String generateCreateProcess( Table table ) {
		final ProgramBuilder str = new ProgramBuilder();
		//final PrimaryKey primaryKey = table.getSchema().getPrimaryKey();
		//TODO:new Table名(primaryKeys);
		final ISchema<?> constructorSchema = getConstructorSchema( table );
		str.append( "new " + generateFunction( generateTableName( table ), constructorSchema ) );
		return str.toString();
	}

	protected String generateRecordGetter( Table table ) {
		final ProgramBuilder str = new ProgramBuilder();
		final String tableName = generateTableName( table );
		final String recordName = "record";
		final String dataName = "data";
		final PrimaryKey primaryKey = table.getSchema().getPrimaryKey();
		if ( primaryKey.getElementMap().size() == 0 ) {
			return null;
		}
		str.funcBegin( "public static", tableName, TABLE_GETTER_METHOD_NAME, generateArgument( table, primaryKey ) );
			str.forEachBegin( tableName, recordName, ENUM_VALUES );
				str.ifBegin( generatePrimaryKeyCheckCondition( recordName, primaryKey ) );
					str.append( "return " + recordName + ";\n" );
				str.ifEnd();
			str.forEachEnd();
			str.ifBegin( "!" + IS_LOADED_NAME );
				str.var( null, true, tableName, dataName, generateCreateProcess( table ) );
				str.callFunc( PRIVATE_LIST_NAME, "add", dataName );
				str.funcReturn( dataName );
			str.ifEnd();
			str.append( "throw new IllegalArgumentException();\n" );
		str.funcEnd();
		return str.toString();
	}

	private String generatePreLoad( Table table ) {
		final ProgramBuilder str = new ProgramBuilder();
		str.funcBegin( "private static", "void", PRE_LOAD_METHOD_NAME, null );
			str.callFunc( PRIVATE_LIST_NAME, "clear", null );
			str.assign( ENUM_VALUES, "null" );
			str.assign( IS_LOADED_NAME, "false" );
		str.funcEnd();
		return str.toString();
	}

	private String generatePostLoad( Table table ) {
		final ProgramBuilder str = new ProgramBuilder();
		final String tableName = generateTableName( table );
		str.funcBegin( "private static", "void", POST_LOAD_METHOD_NAME, null );
			str.assign( ENUM_VALUES, PRIVATE_LIST_NAME + ".toArray( new " + tableName + "[" + PRIVATE_LIST_NAME + ".size()] )" );
			str.assign( IS_LOADED_NAME, "true" );
		str.funcEnd();
		return str.toString();
	}
	
	@Override
	protected String generateParseMainMethodProcess( Table table, TableBuilder tableBuilder ) {
		final ProgramBuilder str = new ProgramBuilder();
		str.callFunc( null, PRE_LOAD_METHOD_NAME, null );
		str.append( super.generateParseMainMethodProcess( table, tableBuilder ) );
		str.callFunc( null, POST_LOAD_METHOD_NAME, null );
		return str.toString();
	}

}
