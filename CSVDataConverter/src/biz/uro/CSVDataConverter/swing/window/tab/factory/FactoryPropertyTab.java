package biz.uro.CSVDataConverter.swing.window.tab.factory;

import javax.swing.BoxLayout;

import biz.uro.CSVDataConverter.swing.builder.FactoryBuilder;
import biz.uro.CSVDataConverter.swing.builder.FactoryMethodBuilder;
import biz.uro.CSVDataConverter.swing.window.ProjectTreeNode;
import biz.uro.CSVDataConverter.swing.window.component.EditableList;
import biz.uro.CSVDataConverter.swing.window.component.EditableListFactory;
import biz.uro.CSVDataConverter.swing.window.tab.DataModelEditTab;

@SuppressWarnings("serial")
public class FactoryPropertyTab extends DataModelEditTab<FactoryBuilder> {
	
	final EditableList<FactoryMethodBuilder> mElementList;
	
	public FactoryPropertyTab(FactoryBuilder model) {
		super(ProjectTreeNode.Type.FACTORY, model, model.getPackage().getClassName());
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		mElementList = EditableListFactory.INSTANCE.createFactoryMethodBuilderEditableList();
		mElementList.setDataModel(model.getMethodListModel());
		add(mElementList);
	}
	
}
