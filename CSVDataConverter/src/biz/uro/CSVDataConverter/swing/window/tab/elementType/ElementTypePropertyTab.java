package biz.uro.CSVDataConverter.swing.window.tab.elementType;

import biz.uro.CSVDataConverter.swing.builder.ElementTypeBuilder;
import biz.uro.CSVDataConverter.swing.window.ProjectTreeNode.Type;
import biz.uro.CSVDataConverter.swing.window.tab.DataModelEditTab;

@SuppressWarnings("serial")
public class ElementTypePropertyTab extends DataModelEditTab<ElementTypeBuilder>{

	public ElementTypePropertyTab(Type type, ElementTypeBuilder model) {
		super(type, model, model.getPackage().getClassName());
	}

}
