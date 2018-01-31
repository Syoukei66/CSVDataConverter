package biz.uro.CSVDataConverter.swing.window;

import javax.swing.ImageIcon;
import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.commons.lang3.StringUtils;

import biz.uro.CSVDataConverter.swing.builder.FactoryBuilder;
import biz.uro.CSVDataConverter.swing.builder.StatementBuilder;
import biz.uro.CSVDataConverter.swing.builder.TableBuilder;
import biz.uro.CSVDataConverter.swing.window.tab.DataModelEditTab;
import biz.uro.CSVDataConverter.swing.window.tab.SchemaTreeDataModel;
import biz.uro.CSVDataConverter.swing.window.tab.factory.FactoryPropertyTab;
import biz.uro.CSVDataConverter.swing.window.tab.table.TablePropertyTab;

@SuppressWarnings("serial")
public class ProjectTreeNode extends DefaultMutableTreeNode {
	public enum Type {
		NONE			("不明"),
		TABLE			("テーブル"),
		FACTORY			("ファクトリ"),
		ELEMENT			("型"),
		ELEMENT_LOCKED	("型（ロック）"),
		;
		public static Type[] LIST = values();
		
		final String mHead;
		final String mIconPath;
		ImageIcon mIcon = null;
		
		Type(String head) {
			mHead = head;
			mIconPath = "img/icon_" + StringUtils.lowerCase(name()) + ".png";
		}
		
		public String getHead() {
			return mHead;
		}
				
		public ImageIcon getIcon() {
			if (mIcon == null) {
				mIcon = new ImageIcon(mIconPath);
			}
			return mIcon;
		}
		
	}
	
	private SchemaTreeDataModel.PackageNameTreeNode mPackageNode;
	private Type mNodeType;
	private Object mDataModel;
	
	public ProjectTreeNode(String text) {
		super(text);
		mNodeType = Type.NONE;
	}
	
	public void setNodeType(Type type) {
		mNodeType = type;
	}
	
	public Type getNodeType() {
		return mNodeType;
	}
	
	public void setPackageNode(SchemaTreeDataModel.PackageNameTreeNode packageNode) {
		mPackageNode = packageNode;
	}
	
	public SchemaTreeDataModel.PackageNameTreeNode getPackageNode() {
		return mPackageNode;
	}
	
	public void setDataModel(Object obj) {
		mDataModel = obj;
	}
	
	public Object getDataModel() {
		return mDataModel;
	}
	
	public DataModelEditTab<?> createTab() {
		if (mNodeType == Type.TABLE) {
			return new TablePropertyTab((TableBuilder)mDataModel); 
		}
		if (mNodeType == Type.FACTORY) {
			return new FactoryPropertyTab((FactoryBuilder)mDataModel);
		}
		return null;
	}
}
