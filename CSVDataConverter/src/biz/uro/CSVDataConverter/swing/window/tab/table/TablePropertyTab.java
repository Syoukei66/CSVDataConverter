package biz.uro.CSVDataConverter.swing.window.tab.table;

import biz.uro.CSVDataConverter.swing.builder.ElementBuilder;
import biz.uro.CSVDataConverter.swing.builder.TableBuilder;
import biz.uro.CSVDataConverter.swing.window.ProjectTreeNode;
import biz.uro.CSVDataConverter.swing.window.component.EditableList;
import biz.uro.CSVDataConverter.swing.window.component.EditableListFactory;
import biz.uro.CSVDataConverter.swing.window.tab.DataModelEditTab;

import javax.swing.BoxLayout;

@SuppressWarnings("serial")
public class TablePropertyTab extends DataModelEditTab<TableBuilder> {

	final EditableList<ElementBuilder> mElementList;
	
	public TablePropertyTab(TableBuilder model) {
		super(ProjectTreeNode.Type.TABLE, model, model.getType().getPackage().getClassName());
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		mElementList = EditableListFactory.INSTANCE.createElementBuilderEditableList();
		mElementList.setDataModel(model.getSchema().getElementDataModel());
		add(mElementList);
	}
	
}
