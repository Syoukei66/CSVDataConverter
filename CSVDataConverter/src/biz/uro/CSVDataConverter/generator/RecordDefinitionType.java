package biz.uro.CSVDataConverter.generator;

import biz.uro.CSVDataConverter.generator.table.TableGenerator_NoEnum;
import biz.uro.CSVDataConverter.generator.table.csv.TableGenerator_Master;
import biz.uro.CSVDataConverter.generator.table.csv.TableGenerator_Null;
import biz.uro.CSVDataConverter.generator.table.csv.TableGenerator_PrimaryKey;

public enum RecordDefinitionType {
	NO_CSV				( "NoCSV", "レコードをEnumで定義しない", false ) {
		@Override
		public ProgramGenerator_UseTable build() {
			return new TableGenerator_NoEnum();
		}
	},
	USE_CSV_NULL		( "UseCSVNull", "レコードをEnumで定義し、データをEnumのコンストラクタに記述しない", true ) {
		@Override
		public ProgramGenerator_UseTable build() {
			return new TableGenerator_Null();
		}
	},
	USE_CSV_PRIMARY_KEY	( "UseCSVPrimaryKey", "レコードをEnumで定義し、主キーをEnumのコンストラクタに記述する", true ) {
		@Override
		public ProgramGenerator_UseTable build() {
			return new TableGenerator_PrimaryKey();
		}
	},
	USE_CSV_ALL			( "UseCSVAll", "レコードをEnumで定義し、全てのデータをEnumのコンストラクタに記述する", true ) {
		@Override
		public ProgramGenerator_UseTable build() {
			return new TableGenerator_Master();
		}
	},
	;
	public static RecordDefinitionType getInstanceByID( String id ) {
		for ( RecordDefinitionType type : LIST ) {
			if ( type.id.equals( id ) ) {
				return type;
			}
		}
		return null;
	}
	
	public static final RecordDefinitionType[] LIST = values();
	public final String id;
	public final String comment;
	public final boolean useCSV;
	
	public abstract ProgramGenerator_UseTable build();

	private RecordDefinitionType( String id, String comment, boolean useCSV ) {
		this.id = id;
		this.comment = comment;
		this.useCSV = useCSV;
	}
	
	@Override
	public String toString() {
		return this.comment;
	}
	
}
	