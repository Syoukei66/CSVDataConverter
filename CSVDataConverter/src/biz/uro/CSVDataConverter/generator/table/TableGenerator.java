package biz.uro.CSVDataConverter.generator.table;

import static biz.uro.CSVDataConverter.generator.parser.ParserConstants.*;
import static biz.uro.CSVDataConverter.generator.table.TableGeneratorConstants.*;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import biz.uro.CSVDataConverter.database.ISchema;
import biz.uro.CSVDataConverter.database.PrimaryKey;
import biz.uro.CSVDataConverter.database.Record;
import biz.uro.CSVDataConverter.database.Table;
import biz.uro.CSVDataConverter.database.element.Element;
import biz.uro.CSVDataConverter.database.element.ElementType;
import biz.uro.CSVDataConverter.database.element.ElementType.Tag;
import biz.uro.CSVDataConverter.database.element.Statement;
import biz.uro.CSVDataConverter.generator.ProgramGenerator_UseTable;
import biz.uro.CSVDataConverter.nativ.NativeCodeKind;
import biz.uro.CSVDataConverter.string.ProgramBuilder;
import biz.uro.CSVDataConverter.swing.builder.TableBuilder;

public abstract class TableGenerator extends ProgramGenerator_UseTable {

	private static final String FILEREADER_NAME = "fileReader";
	private static final String FILEREADER_GET_SIZE_NAME = FILEREADER_NAME + ".getSize()";
	private static String FILEREADER_CREATE_TOKENIZER( String index ) {
		return FILEREADER_NAME + ".createTokenizer( " + index + " )";
	}
	
	{
		nativeCode( NativeCodeKind.CSV_FILE_READER );
		nativeCode( NativeCodeKind.CSV_TOKENIZER );
	}

	protected abstract ISchema<?> getConstructorSchema( Table table );
	protected abstract ISchema<?> getSchema( Table table );
	
	@Override
	protected void registerImports( Table table, List<String> list ) {
		final ISchema<?> schema = getSchema( table );
		final ISchema<?> constructorSchema = getConstructorSchema( table );
		if ( constructorSchema != null && schema.getElementMap().size() == constructorSchema.getElementMap().size() ) {
			return;
		}
		list.add( COMMON_PARSER_PACKAGE_PATH );
	};
	
	@Override
	protected String generateFileNameByTable( Table table ) {
		return table.getName();
	}
	
	protected String generateTableName( Table table ) {
		return table.getName();
	}
	
	private String generateElementName( String head, String baseName ) {
		if ( head == null ) {
			return baseName;
		}
		return head + StringUtils.capitalize( baseName );
	}
	
	protected String convertRecord( Table table, Record record ) {
		final ISchema<?> schema = getConstructorSchema( table );
		final String recordName = table.parseRecordName( record );
		if ( recordName == null || recordName.length() == 0 ) {
			return null;
		}
		if ( schema == null ) {
			return recordName + ",";
		}
		return recordName + "( " + record.convertProgram( schema ) + " ),";
	}
	
	protected String generateArgument( Table table, ISchema<?> schema ) {
		return generateArgument( null, table, schema );
	}
	
	protected String generateArgument( String head, Table table, ISchema<?> schema ) {
		final ProgramBuilder str = new ProgramBuilder();
		final Map<String, Element> elementMap = schema.getElementMap();
		boolean isFirst = true;
		for ( Map.Entry<String,Element> e : elementMap.entrySet() ) {
			if ( !isFirst ) {
				str.append( ", " );
			}
			isFirst = false;
			final Element element = e.getValue();
			final String typeName = element.getStatement().getType().getTypeName();
			final String baseName = e.getKey();
			final String name = generateElementName( head, baseName );
			if ( typeName.equals( table.getName() ) ) {
				str.append( generateArgument( name, table, table.getSchema().getPrimaryKey() ) );
				continue;
			}
			final int size = element.getSize();
			if ( size == 1 ) {
				str.append( typeName + " " + name );				
			}
			else if ( size > 1 ) {
				str.append( typeName + "[] " + name );
			}
			else if ( size == -1 ) {
				str.append( typeName + "... " + name );
			}				
		}
		return str.toString();
	}
	
	protected String generatePrimaryKeyCheckCondition( String recordName, PrimaryKey primaryKey ) {
		final ProgramBuilder str = new ProgramBuilder();
		final Map<String,Element> primaryKeys = primaryKey.getElementMap();
		boolean isFirst = true;
		for ( Map.Entry<String,Element> entry : primaryKeys.entrySet() ) {
			final String name = entry.getKey();
			final Element element = entry.getValue();
			final ElementType type = element.getStatement().getType();
			if ( !isFirst ) {
				str.append( " && " );
			}
			isFirst = false;
			if ( type.getTag() == Tag.PRIMITIVE && element.getSize() == 1 ) {
				str.append( name + " == " + recordName + "." + name );						
			}
			else {
				str.append( name + ".equals( " + recordName + "." + name + " )" );
			}
		}
		return str.toString();
	}
	
	protected String generateFunction( String functionName, ISchema<?> schema ) {
		final ProgramBuilder str = new ProgramBuilder();
		str.append( functionName );
		str.append( "(" );
		if ( schema != null && schema.getElementMap().size() != 0 ) {
			boolean first = true;
			final Map<String,Element> elementMap = schema.getElementMap();
			for ( Map.Entry<String,Element> e : elementMap.entrySet() ) {
				final String elementName = e.getKey();
				if ( !first ) {
					str.append( "," );
				}
				str.append( " " );
				str.append( elementName );
				first = false;
			}
			str.append( " " );
		}
		str.append( ")" );
		return str.toString();
	}
		
	private String generateFieldsProgram( Table table, ISchema<?> schema, ISchema<?> finalFieldSchema ) {
		return generateFieldsProgram( null, table, schema, finalFieldSchema );
	}
	
	private String generateFieldsProgram( String head, Table table, ISchema<?> schema, ISchema<?> finalFieldSchema ) {
		final ProgramBuilder str = new ProgramBuilder();
		final Map<String,Element> elementMap = schema.getElementMap();
		final Map<String,Element> finalFieldMap = finalFieldSchema == null ? null : finalFieldSchema.getElementMap();
		for ( Map.Entry<String,Element> e : elementMap.entrySet() ) {
			final String baseName = e.getKey();
			final Element element = e.getValue();
			final int size = element.getSize();
			final String typeName = element.getStatement().getType().getTypeName();
			final String name = generateElementName( head, baseName );
			if ( typeName.equals( table.getName() ) ) {
				final ISchema<?> primaryKey = table.getSchema().getPrimaryKey();
				str.append( generateFieldsProgram( name, table, primaryKey, primaryKey ) );
				continue;
			}
			String fieldName;
			if ( size == 1 ) {
				fieldName = typeName + " " + name;
			}
			else {
				fieldName = typeName + "[] " + name;
			}
			if ( finalFieldMap != null && finalFieldMap.containsKey( baseName ) ) {
				str.append( "final " + fieldName + ";\n" );				
			}
			else {
				str.append( fieldName + ";\n" );				
			}
		}
		return str.toString();
	}
	
	protected String generateFields( Table table ) {
		final ISchema<?> schema = getSchema( table );
		final ISchema<?> constructorSchema = getConstructorSchema( table );
		return generateFieldsProgram( table, schema, constructorSchema );
	}

	private String generateConstructorProgram( Table table, ISchema<?> schema ) {
		return generateConstructorProgram( null, table, schema);
	}
	
	private String generateConstructorProgram( String head, Table table, ISchema<?> schema ) {
		final ProgramBuilder str = new ProgramBuilder();
		final Map<String,Element> elementMap = schema.getElementMap();
		for ( Map.Entry<String,Element> e : elementMap.entrySet() ) {
			final String typeName = e.getValue().getStatement().getType().getTypeName();
			final String baseName = e.getKey();
			final String name = generateElementName( head, baseName );
			if ( typeName.equals( table.getName() ) ) {
				str.append( generateConstructorProgram( name, table, table.getSchema().getPrimaryKey() ) );
				continue;
			}
			str.append( "this." + name + " = " + name + ";\n" );				
		}
		return str.toString();
	}
	
	protected String generateConstructor( Table table ) {
		final ProgramBuilder str = new ProgramBuilder();
		final ISchema<?> schema = getConstructorSchema( table );
		if ( schema == null ) {
			return str.toString();
		}
		str.append( table.getName() + "( " + generateArgument( table, schema ) + " ) {\n" );
		str.addTab();
			str.append( generateConstructorProgram( table, schema ) );
		str.subTab();	
		str.append( "}\n" );
		return str.toString();
	}
	
	private String generateCallArgument( String head, ISchema<?> schema, String index ) {
		final ProgramBuilder str = new ProgramBuilder();
		boolean isFirst = true;
		final Map<String,Element> elementMap = schema.getElementMap();
		for ( Map.Entry<String,Element> e : elementMap.entrySet() ) {
			final String baseName = e.getKey();
			final String name = generateElementName( head, baseName );
			if ( !isFirst ) {
				str.append( ", " );
			}
			isFirst = false;
			str.append( "this." + name );
			if ( index != null ) {
				str.append( "[" + index + "]" );
			}
		}
		return str.toString();
	}
	
	private String generateGetterProgram( Table table, ISchema<?> schema ) {
		return generateGetterProgram( null, table, schema );
	}
	
	private String generateGetterProgram_SelfGetter( String head, int size, Table table, ISchema<?> schema ) {
		final ProgramBuilder str = new ProgramBuilder();
		final String typeName = table.getName();
		if ( size == 1 ) {
			str.append( "public " + typeName + " get" + StringUtils.capitalize( head ) + "() {\n" );
			str.addTab();
				str.append( "return " + TABLE_GETTER_METHOD_NAME + "(" + generateCallArgument( head, schema, null ) + ");\n" );
			str.subTab();
			str.append( "}\n" );				
		}
		else if ( size > 1 || size == -1 ) {
			final Map<String,Element> elementMap = schema.getElementMap();
			final Element firstElement = elementMap.get( elementMap.keySet().toArray()[0] );
			str.append( "public int get" + StringUtils.capitalize( head ) + "Count() {\n" );
			str.addTab();
				str.append( "return this." + generateElementName( head, firstElement.getName() ) + ".length;\n" );
			str.subTab();
			str.append( "}\n" );	
			str.changeBlock();
			final String indexName = "index";
			str.append( "public " + typeName + " get" + StringUtils.capitalize( head ) + "( int " + indexName + " ) {\n" );
			str.addTab();
				str.append( "return " + TABLE_GETTER_METHOD_NAME + "(" + generateCallArgument( head, schema, indexName ) + ");\n" );
			str.subTab();
			str.append( "}\n" );								
		}
		return str.toString();
	}
	
	private String generateGetterProgram( String head, Table table, ISchema<?> schema ) {
		final ProgramBuilder str = new ProgramBuilder();
		final Map<String,Element> elementMap = schema.getElementMap();
		for ( Map.Entry<String,Element> e : elementMap.entrySet() ) {
			final String baseName = e.getKey();
			final Element element = e.getValue();
			final int size = element.getSize();
			final String name = generateElementName( head, baseName );
			final String typeName = element.getStatement().getType().getTypeName();
			str.changeBlock();
			if ( typeName.equals( table.getName() ) ) {
				str.append( generateGetterProgram_SelfGetter( name, size, table, table.getSchema().getPrimaryKey() ) );
				continue;
			}
			if ( size == 1 ) {
				str.append( "public " + typeName + " get" + StringUtils.capitalize( name ) + "() {\n" );
				str.addTab();
					str.append( "return this." + name + ";\n" );
				str.subTab();
				str.append( "}\n" );				
			}
			else if ( size > 1 || size == -1 ) {
				str.append( "public int get" + StringUtils.capitalize( name ) + "Count() {\n" );
				str.addTab();
					str.append( "return this." + name + ".length;\n" );
				str.subTab();
				str.append( "}\n" );	
				str.changeBlock();
				final String indexName = "index";
				str.append( "public " + typeName + " get" + StringUtils.capitalize( name ) + "( int " + indexName + " ) {\n" );
				str.addTab();
					str.append( "return this." + name + "[" + indexName + "];\n" );
				str.subTab();
				str.append( "}\n" );								
			}
		}
		return str.toString();	
	}
	
	protected String generateGetter( Table table ) {
		final ISchema<?> schema = getSchema( table );
		return generateGetterProgram( table, schema );
	}

	protected String generateParseMainMethodProcess( Table table, TableBuilder tableBuilder ) {
		final ProgramBuilder str = new ProgramBuilder();
		final String tokenizerCountName = "tokenizerCount";
		final String indexName = "i";
		final String tableName = table.getName();
		final String recordName = "record";
		final Statement statement = tableBuilder.getStatement().build();
		final Map<String,Element> elementMap = getSchema( table ).getElementMap();
		final ISchema<?> constructorSchema = getConstructorSchema( table );
		final Map<String,Element> constructorElementMap = constructorSchema == null ? null : constructorSchema.getElementMap();
		str.var( null, true, "int", tokenizerCountName, FILEREADER_GET_SIZE_NAME );
		str.forBegin( indexName, "0", tokenizerCountName );
			str.var( null, true, CSV_TOKENIZER, TOKENIZER_NAME, FILEREADER_CREATE_TOKENIZER( indexName ) );
			//TODO: ここが間違い、ふさわしいプログラムに修正だ。parseElementのStatementバージョンもstaticにしては？
			str.var( null, false, tableName, recordName, COMMON_PARSER_NAME + "." + parseStatement( NEXT_TOKEN_NAME, statement.generateParserName() ) );
			for ( Map.Entry<String,Element> e : elementMap.entrySet() ) {
				final String name = e.getKey();
				final Element element = e.getValue();
				final String targetElementname = recordName + "." + name;
				if ( constructorElementMap != null && constructorElementMap.containsKey( name ) ) {
					continue;
				}
				str.append( targetElementname + " = " + COMMON_PARSER_NAME + "." + parseElement( NEXT_TOKEN_NAME, element ) + ";\n" );
			}
		str.forEnd();
		return str.toString();		
	}
	
	protected String generateParseMainMethod( Table table, TableBuilder tableBuilder ) {
		final ISchema<?> schema = getSchema( table );
		final ISchema<?> constructorSchema = getConstructorSchema( table );
		final ProgramBuilder str = new ProgramBuilder();
		if ( constructorSchema != null && schema.getElementMap().size() == constructorSchema.getElementMap().size() ) {
			return null;
		}
		str.funcBegin( "public static", "void", TABLE_LOAD_METHOD_NAME, CSV_FILE_READER + " " + FILEREADER_NAME );
			str.append( generateParseMainMethodProcess( table, tableBuilder ) );
		str.funcEnd();
		return str.toString();
	}
	
}
