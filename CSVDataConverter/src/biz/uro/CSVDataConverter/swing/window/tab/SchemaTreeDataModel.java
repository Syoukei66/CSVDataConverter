package biz.uro.CSVDataConverter.swing.window.tab;

import java.util.HashMap;
import java.util.Map;

import javax.swing.tree.DefaultTreeModel;

import biz.uro.CSVDataConverter.swing.builder.ElementTypeBuilder;
import biz.uro.CSVDataConverter.swing.builder.FactoryBuilder;
import biz.uro.CSVDataConverter.swing.builder.PackageBuilder;
import biz.uro.CSVDataConverter.swing.builder.ProjectData;
import biz.uro.CSVDataConverter.swing.builder.TableBuilder;
import biz.uro.CSVDataConverter.swing.window.ProjectTreeNode;

@SuppressWarnings("serial")
public class SchemaTreeDataModel extends DefaultTreeModel {

	public class PackageNameTreeNode {
		final String mPackage;
		final ProjectTreeNode mNode;
		Map<String,PackageNameTreeNode> mChildren = new HashMap<>();
		
		PackageNameTreeNode( String packaqe, ProjectTreeNode node ) {
			mPackage = packaqe;
			mNode = node;
		}
		
		public String getPackage() {
			if ( mPackage == null ) {
				return "";
			}
			return mPackage;
		}
	}

	private ProjectData mProjectData;
	private PackageNameTreeNode mPackageRoot;
	
	public SchemaTreeDataModel( ProjectData data ) {
		super( new ProjectTreeNode( "パッケージ" ) );
		mProjectData = data;
		onUpdateProjectData();
	}

	private void addPackgePathForTree( PackageNameTreeNode root, PackageBuilder packaqe ) {
		final String[] packagePaths = packaqe.getPackagePaths();
		StringBuilder packageName = new StringBuilder();
		PackageNameTreeNode packageNode = root;
		for ( int i = 0; i < packagePaths.length; ++i ) {
			packageName.append( packagePaths[i] );
			PackageNameTreeNode checkNode = packageNode.mChildren.get( packagePaths[i] );
			if ( checkNode == null ) {
				ProjectTreeNode node = new ProjectTreeNode( packagePaths[i] );
				checkNode = new PackageNameTreeNode( packageName.toString(), node );
				checkNode.mNode.setPackageNode( checkNode );
				packageNode.mNode.add( node );
				packageNode.mChildren.put( packagePaths[i], checkNode );
			}
			packageNode = checkNode;
			packageName.append( "." );
		}
	}
	
	private PackageNameTreeNode getTreeNodeWithPackage( PackageNameTreeNode root, PackageBuilder packaqe ) {
		final String[] packagePaths = packaqe.getPackagePaths();
		PackageNameTreeNode node = root;
		for ( int i = 0; i < packagePaths.length; ++i ) {
			node = node.mChildren.get( packagePaths[i] );
		}
		return node;
	}
	
	public void onUpdateProjectData() {
		ProjectTreeNode root = (ProjectTreeNode)getRoot();
		root.removeAllChildren();
		mPackageRoot = new PackageNameTreeNode( null, root );
		root.setPackageNode( mPackageRoot );
		//パッケージツリーを作成
		final int package_count = mProjectData.getPackageModel().getSize();
		for ( int i = 0; i < package_count; ++i ) {
			final PackageBuilder packaqe = mProjectData.getPackageModel().get( i );
			addPackgePathForTree( mPackageRoot, packaqe );
		}
		//パッケージツリーに属性を付加していく
		//処理には優先度があるので順番は変更しないように
		final int element_count = mProjectData.getElementTypeModel().getSize();
		for ( int i = 0; i < element_count; ++i ) {
			final ElementTypeBuilder element = mProjectData.getElementTypeModel().get( i );
			PackageNameTreeNode node = getTreeNodeWithPackage( mPackageRoot, element.getPackage() );
			if ( element.isEditable() ) {
				node.mNode.setNodeType( ProjectTreeNode.Type.ELEMENT );				
			}
			else {
				node.mNode.setNodeType( ProjectTreeNode.Type.ELEMENT_LOCKED );
			}
			node.mNode.setDataModel( element );
		}
		final int table_count = mProjectData.getTableDataModel().getSize();
		for ( int i = 0; i < table_count; ++i ) {
			final TableBuilder table = mProjectData.getTableDataModel().get( i );
			PackageNameTreeNode node = getTreeNodeWithPackage( mPackageRoot, table.getPackage() );
			node.mNode.setNodeType( ProjectTreeNode.Type.TABLE );
			node.mNode.setDataModel( table );
		}
		final int factory_count = mProjectData.getFactoryModel().getSize();
		for ( int i = 0; i < factory_count; ++i ) {
			final FactoryBuilder factory = mProjectData.getFactoryModel().get( i );
			PackageNameTreeNode node = getTreeNodeWithPackage( mPackageRoot, factory.getPackage() );
			node.mNode.setNodeType( ProjectTreeNode.Type.FACTORY );		
			node.mNode.setDataModel( factory );
		}
		reload();
	}

	
}
