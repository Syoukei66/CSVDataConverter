package biz.uro.CSVDataConverter.swing.builder.validator.condition.list;

import biz.uro.CSVDataConverter.swing.builder.TableBuilder;
import biz.uro.CSVDataConverter.swing.json.JSONClassID;
import biz.uro.CSVDataConverter.swing.json.JSONReader;
import biz.uro.CSVDataConverter.swing.json.JSONWriter;
import biz.uro.CSVDataConverter.swing.json.JSONObjectMap.IJSONObjectLoadedListener;

@JSONClassID( "Table" )
public class ValidatorCondition_Table extends ValidatorCondition_List {
	
	private static final String TABLE_HASH_CODE = "TableHashCode";
	
	private TableBuilder mTable;
	
	@Override
	public void jsonMapping( JSONWriter writer ) {
		writer.add( TABLE_HASH_CODE, mTable.jsonHashCode() );
	}

	public ValidatorCondition_Table( JSONReader reader ) {
		super( reader );
		reader.get( TABLE_HASH_CODE ).reserveObject( new IJSONObjectLoadedListener() {
			@Override
			public void onLoaded(Object obj) {
				mTable = (TableBuilder)obj;
			}
		} );
	}
	
	public ValidatorCondition_Table( TableBuilder table ) {
		mTable = table;
	}

	@Override
	public String getComment() {
		return "Table";
	}
	
	public String toString() {
		return mTable.getPackage().getPackagePath();
	}
	
}