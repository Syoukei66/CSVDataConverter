package biz.uro.CSVDataConverter.swing.builder.validator.kind.list;

import biz.uro.CSVDataConverter.swing.builder.TableBuilder;
import biz.uro.CSVDataConverter.swing.builder.validator.condition.ValidatorCondition;
import biz.uro.CSVDataConverter.swing.builder.validator.condition.list.ValidatorCondition_Table;
import biz.uro.CSVDataConverter.swing.json.JSONClassID;
import biz.uro.CSVDataConverter.swing.json.JSONReader;
import biz.uro.CSVDataConverter.swing.json.JSONWriter;
import biz.uro.CSVDataConverter.swing.json.JSONObjectMap.IJSONObjectLoadedListener;

@JSONClassID( "Table" )
public class ValidatorKind_Table extends ValidatorKind_List {

	private static final String TABLE_HASH_CODE = "TableHashCode";
	
	private TableBuilder mTable;
	
	@Override
	public void jsonMapping( JSONWriter writer ) {
		super.jsonMapping( writer );
		writer.add( TABLE_HASH_CODE, mTable.jsonHashCode() );
	}

	public ValidatorKind_Table( JSONReader reader ) {
		super( reader );
		reader.get( TABLE_HASH_CODE ).reserveObject( new IJSONObjectLoadedListener() {
			@Override
			public void onLoaded(Object obj) {
				mTable = (TableBuilder)obj;
			}
		} );
	}
	
	public ValidatorKind_Table( TableBuilder table ) {
		super( "Table_" + table.getPackage().getClassName() );
		mTable = table;
	}
	
	@Override
	protected ValidatorCondition createConditioner() {
		return new ValidatorCondition_Table( mTable );
	}
	
	@Override
	public String toString() {
		return "テーブル[" + mTable.getPackage().getClassName() + "]を参照";
	}
	
	@Override
	public String jsonHashCode() {
		return Integer.toString( hashCode() );
	}
	
}
