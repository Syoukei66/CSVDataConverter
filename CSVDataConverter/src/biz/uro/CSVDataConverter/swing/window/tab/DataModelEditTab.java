package biz.uro.CSVDataConverter.swing.window.tab;

import java.util.Objects;

import javax.swing.JLabel;
import javax.swing.JPanel;

import biz.uro.CSVDataConverter.swing.json.IJSONDataModel;
import biz.uro.CSVDataConverter.swing.window.ProjectTreeNode;

@SuppressWarnings("serial")
public class DataModelEditTab<T extends IJSONDataModel> extends JPanel {

	protected final T mDataModel;
	private final ProjectTreeNode.Type mType;
	private final String mName;
	private final JLabel mLabel;

	private boolean mIsChanged = false;
	
	public DataModelEditTab(ProjectTreeNode.Type type, T dataModel, String name) {
		mType = type;
		mDataModel = dataModel;
		mName = name;
		mLabel = new JLabel(mName);
	}
	
	protected void onValueChanged() {
		mIsChanged = true;
		mLabel.setText("*" + mName);
	}
	
	public ProjectTreeNode.Type getType() {
		return mType;
	}
	
	public String getName() {
		return mName;
	}
	
	public T getDataModel() {
		return mDataModel;
	}
	
	public JLabel getLabel() {
		return mLabel;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(mType, mName, mDataModel);
	}
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof DataModelEditTab)) {
			return false;
		}
		DataModelEditTab<?> tab = (DataModelEditTab<?>)other;
		if (!mType.equals(tab.mType)) {
			return false;
		}
		if (!mName.equals(tab.mName)) {
			return false;
		}
		if (!mDataModel.equals(tab.mDataModel)) {
			return false;
		}
		return true;
	}
	
}
