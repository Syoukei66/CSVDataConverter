package biz.uro.CSVDataConverter.generator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import biz.uro.CSVDataConverter.database.Table;
import biz.uro.CSVDataConverter.database.element.Element;
import biz.uro.CSVDataConverter.database.element.ElementType;
import biz.uro.CSVDataConverter.string.ProgramBuilder;
import biz.uro.CSVDataConverter.swing.builder.TableBuilder;

public abstract class ProgramGenerator_UseTable extends ProgramGenerator {

	protected abstract void registerImports( Table table, List<String> list );
	protected abstract String generateFileNameByTable( Table table );
	protected abstract String generateClassDefinitionByTable( Table table, TableBuilder tableBuilder );
	
	public void generate( Table table, TableBuilder tableBuilder, String directoryPath ) throws Exception {
		super.generate( directoryPath, new IProgramGenerateProcess() {
			
			@Override
			public String generatePackage() {
				return generatePackagePathByTable( table );
			}
			
			@Override
			public String generateImports() {
				return generateImportsByTable( table );
			}
			
			@Override
			public String generateFileName() {
				return generateFileNameByTable( table );
			}
			
			@Override
			public String generateClassDefinition() {
				return generateClassDefinitionByTable( table, tableBuilder );
			}
		} );
	}
	
	protected String generatePackagePathByTable( Table table ) {
		return table.getPackagePath();
	};
	
	protected String generateImportsByTable( Table table ) {
		final ProgramBuilder str = new ProgramBuilder();
		final Map<String,Element> elementMap = table.getSchema().getElementMap();
		final HashSet<String> importPackageNames = new HashSet<>();
		final List<String> importPackages = new ArrayList<String>();
		registerImports( table, importPackages );
		for ( String packageName : importPackages ) {
			importPackageNames.add( packageName );
		}
		for ( Map.Entry<String,Element> e : elementMap.entrySet() ) {
			final ElementType type = e.getValue().getStatement().getType();
			final String packageName = type.getPackageName();
			if ( packageName == null || packageName.length() == 0 ) {
				continue;
			}
			importPackageNames.add( packageName + "." + type.getTypeName() );
		}
		for ( String importPackageName : importPackageNames ) {
			if ( importPackageName != null ) {
				str.append( generateImport( importPackageName ) );
			}
		}
		return str.toString();
	}

}
