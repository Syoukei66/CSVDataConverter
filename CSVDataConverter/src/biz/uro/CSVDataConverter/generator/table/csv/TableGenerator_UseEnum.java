package biz.uro.CSVDataConverter.generator.table.csv;

import static biz.uro.CSVDataConverter.generator.table.TableGeneratorConstants.ENUM_VALUES;
import static biz.uro.CSVDataConverter.generator.table.TableGeneratorConstants.TABLE_GETTER_METHOD_NAME;

import biz.uro.CSVDataConverter.database.PrimaryKey;
import biz.uro.CSVDataConverter.database.Record;
import biz.uro.CSVDataConverter.database.Table;
import biz.uro.CSVDataConverter.generator.table.TableGenerator;
import biz.uro.CSVDataConverter.string.ProgramBuilder;
import biz.uro.CSVDataConverter.swing.builder.TableBuilder;

public abstract class TableGenerator_UseEnum extends TableGenerator {
	
	@Override
	protected String generateClassDefinitionByTable( Table table, TableBuilder tableBuilder ) {
		final ProgramBuilder str = new ProgramBuilder();
		str.append( "public enum " + generateTableName( table ) + " {\n" );
		str.addTab();
			//enum elements
			str.append( generateEnums( table ) );
			str.changeBlock();
			//utils
			str.append( generateUtils( table ) );
			str.changeBlock();
			//recordGetter
			str.append( generateRecordGetter( table ) );
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
	
	private String generateEnums( Table table ) {
		final ProgramBuilder str = new ProgramBuilder();
		final int recordsCount = table.getRecordsCount();
		for ( int i = 0; i < recordsCount; i++ ) {
			final Record record = table.getRecord( i );
			final String recordProgram = convertRecord( table, record );
			if ( recordProgram == null || recordProgram.length() == 0 ) {
				continue;
			}
			str.append( recordProgram + "\n" );
		}
		str.append( ";\n" );
		return str.toString();
	}

	private String generateUtils( Table table ) {
		final ProgramBuilder str = new ProgramBuilder();
		final String tableName = table.getName();
		str.append( "public static final " + tableName + "[] " + ENUM_VALUES +  " = values();\n" );
		return str.toString();
	}
	
	protected String generateRecordGetter( Table table ) {
		final ProgramBuilder str = new ProgramBuilder();
		final String tableName = table.getName();
		final String recordName = "record";
		final PrimaryKey primaryKey = table.getSchema().getPrimaryKey();
		if ( primaryKey.getElementMap().size() == 0 ) {
			return null;
		}
		str.funcBegin( "public static", tableName, TABLE_GETTER_METHOD_NAME, generateArgument( table, primaryKey ) );
			str.forEachBegin( tableName, recordName, ENUM_VALUES );
				str.ifBegin( generatePrimaryKeyCheckCondition( recordName, primaryKey ) );
					str.funcReturn( recordName );
				str.ifEnd();
			str.forEachEnd();
			str.funcReturn( "null" );
		str.funcEnd();
		return str.toString();
	}

	@Override
	protected String convertRecord( Table table, Record record ) {
		final String comment = table.parseComment( record );
		String ret = super.convertRecord( table, record );
		if ( ret == null || ret.length() == 0 ) {
			return null;
		}
		if ( comment != null ) {
			ret += "//" + comment;
		}
		return ret;
	}

}
