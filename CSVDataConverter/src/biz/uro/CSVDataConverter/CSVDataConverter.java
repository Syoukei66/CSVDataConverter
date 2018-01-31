package biz.uro.CSVDataConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import biz.uro.CSVDataConverter.database.Table;
import biz.uro.CSVDataConverter.database.element.ElementType.Tag;
import biz.uro.CSVDataConverter.generator.ProgramGenerator_UseTable;
import biz.uro.CSVDataConverter.generator.parser.StatementParserGenerator;
import biz.uro.CSVDataConverter.io.csv.CSVData;
import biz.uro.CSVDataConverter.nativ.NativeCodeWriter;
import biz.uro.CSVDataConverter.swing.builder.PackageBuilder;
import biz.uro.CSVDataConverter.swing.builder.StatementBuilder;
import biz.uro.CSVDataConverter.swing.builder.TableBuilder;

public class CSVDataConverter {
	
	private final List<StatementBuilder> mStatementList;
	private final HashSet<TableBuilder> mTableList = new HashSet<>();
	private final HashSet<PackageBuilder> mPackageList = new HashSet<>();
	private final NativeCodeWriter mNativeCodeWriter = new NativeCodeWriter();
	
	public CSVDataConverter( List<StatementBuilder> list ) {
		mStatementList = list;
	}
	
	public void registerTable( TableBuilder table ) {
		mTableList.add( table );
	}
	
	public void registerPackage( PackageBuilder packaqe ) {
		if ( packaqe.getTag() == Tag.PRIMITIVE ) {
			return;
		}
		mPackageList.add( packaqe );
	}
	
	public void convert( String inputPath, String outputPath ) throws Exception {
		final List<Table> tableList = new ArrayList<>();
		for ( TableBuilder tableBuilder : mTableList ) {
			final Table table = tableBuilder.build();
			tableList.add( table );
		}
		final Map<Table,CSVData> csvMap = new HashMap<>();
		for ( Table table : tableList ) {
			table.init();
		}
		for ( Table table : tableList ) {
			final CSVData data = new CSVData( inputPath + "\\" + table.getName() );
			data.load();
			table.firstLoad( data );
			csvMap.put( table, data );
		}
		for ( Map.Entry<Table, CSVData> e : csvMap.entrySet() ) {
			final Table table = e.getKey();
			final CSVData csvData = e.getValue();
			table.secondload( csvData );
		}
		for ( TableBuilder tableBuilder : mTableList ) {
			final Table table = tableBuilder.getBuildedInstance();
			final ProgramGenerator_UseTable generator = table.getGenerator();
			generator.generate( table, tableBuilder, outputPath );
			mNativeCodeWriter.addAll( generator.getUseNativeCodes() );
		}
		final StatementParserGenerator statementGenerator = new StatementParserGenerator();
		statementGenerator.generate( outputPath, mStatementList, tableList, mPackageList );
		mNativeCodeWriter.write( outputPath );
	}
	
}
