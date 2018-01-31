package biz.uro.CSVDataConverter.swing.json;

public interface IJSONList {

	void jsonParse( JSONReader reader );
	void jsonMapping( String name, JSONWriter writer );

}
