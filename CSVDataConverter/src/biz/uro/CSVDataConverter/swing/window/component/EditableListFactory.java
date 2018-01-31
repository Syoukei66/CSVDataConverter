package biz.uro.CSVDataConverter.swing.window.component;

import biz.uro.CSVDataConverter.swing.builder.ElementBuilder;
import biz.uro.CSVDataConverter.swing.builder.ElementParserBuilder;
import biz.uro.CSVDataConverter.swing.builder.FactoryMethodBuilder;
import biz.uro.CSVDataConverter.swing.window.WindowDirector;

public enum EditableListFactory {
	INSTANCE;
	
	public EditableList<ElementBuilder> createElementBuilderEditableList() {
		return new EditableList<ElementBuilder>( 
				WindowDirector.INSTANCE.getElementPropertyWindow(),
				String.format( "%-24s %-14s %-10s %-13s", "型[サイズ]{入力規則}", "備考", "フィールド名", "論理名" )
				);
	}
	
	public EditableList<FactoryMethodBuilder> createFactoryMethodBuilderEditableList() {
		return new EditableList<FactoryMethodBuilder>(
				WindowDirector.INSTANCE.getFactoryMethodPropertyWindow(),
				""
				);
	}
	
}
