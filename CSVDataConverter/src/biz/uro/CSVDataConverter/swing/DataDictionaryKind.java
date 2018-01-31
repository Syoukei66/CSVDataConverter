package biz.uro.CSVDataConverter.swing;

import java.util.ArrayList;

import biz.uro.CSVDataConverter.swing.old.DictionaryFrame;

public enum DataDictionaryKind {
	IMPORT		( "Import", 	"インポート" ) {
		@Override
		protected DictionaryFrame<?, ?> createWindow( String head ) {
			return new ImportWindow( head );
		}
	},
	TYPE		( "Type", 		"定義済みの型" ) {
		@Override
		protected DictionaryFrame<?, ?> createWindow( String head ) {
			return new ElementTypeWindow( head );
		}
	},
	;
	public static final DataDictionaryKind[] LIST = values();
	
	private static ArrayList<DictionaryFrame<?,?>> VALUES;
	
	public static final ArrayList<DictionaryFrame<?,?>> frameValues() {
		if ( VALUES == null ) {
			VALUES = new ArrayList<>();
			for ( DataDictionaryKind value : LIST ) {
				VALUES.add( value.window );
			}
		}
		return VALUES;
	}

	public final String id;
	public final String head;
	public final DictionaryFrame<?,?> window;
	
	protected abstract DictionaryFrame<?,?> createWindow( String head );
	
	private DataDictionaryKind( String id, String head ) {
		this.id = id;
		this.head = head;
		this.window = createWindow( head );
	}
	
	
}
