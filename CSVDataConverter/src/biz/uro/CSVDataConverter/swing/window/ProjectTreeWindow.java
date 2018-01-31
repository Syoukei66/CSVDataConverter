package biz.uro.CSVDataConverter.swing.window;

import javax.swing.JFrame;
import javax.swing.JTree;

import java.awt.BorderLayout;
import java.awt.Component;

import biz.uro.CSVDataConverter.swing.ProjectDataModel;
import biz.uro.CSVDataConverter.swing.builder.ElementTypeBuilder;
import biz.uro.CSVDataConverter.swing.builder.FactoryBuilder;
import biz.uro.CSVDataConverter.swing.builder.ProjectData;
import biz.uro.CSVDataConverter.swing.builder.TableBuilder;
import biz.uro.CSVDataConverter.swing.old.PropertyWindow;
import biz.uro.CSVDataConverter.swing.window.tab.SchemaTreeDataModel;
import biz.uro.CSVDataConverter.swing.window.tab.elementType.ElementTypePropertyWindow;
import biz.uro.CSVDataConverter.swing.window.tab.factory.FactoryPropertyWindow;
import biz.uro.CSVDataConverter.swing.window.tab.table.TablePropertyWindow;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.JLabel;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JPopupMenu;

public class ProjectTreeWindow {

	private JFrame frame;
	private JScrollPane mScrollPane;
	private JTree mTree = null;
	private SchemaTreeDataModel mTreeDataModel;
	private JLabel lblNewLabel;
	private JPanel panel_1;
	private JPanel panel_2;
	private JLabel lblNewLabel_1;

	private JPopupMenu popupMenu = new JPopupMenu();
	private ProjectTreeNode mPopupMenuTargetNode = null;

	@SuppressWarnings("serial")
	public class ProjectTreeCellRenderer extends DefaultTreeCellRenderer {
		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
			ProjectTreeNode node = (ProjectTreeNode)value;
			setLeafIcon(node.getNodeType().getIcon());
			return super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		}
	}
	
	protected class ShowObjectHandler extends MouseAdapter implements KeyListener {
		
		//マウスイベント
		@Override
		public void mouseClicked(MouseEvent e) {
			if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2) {
				JTree tree = (JTree) e.getSource();
				TreePath path = tree.getPathForLocation(e.getX(), e.getY());
				openByNewTab(path);
			}
			if (SwingUtilities.isRightMouseButton(e)) {
				JTree tree = (JTree) e.getSource();
				TreePath path = tree.getPathForLocation(e.getX(), e.getY());
				showPopupMenu(e.getX(), e.getY(), path);
			}
		}
		
		//キーイベント
		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				JTree tree = (JTree) e.getSource();
			//	TreePath path = tree.getSelectionPath();	//選択範囲の先頭
				TreePath path = tree.getLeadSelectionPath();	//フォーカスのある要素
				openByNewTab(path);
				e.consume();
			}
		}
		
		@Override
		public void keyReleased(KeyEvent e) {
		}

		@Override
		public void keyTyped(KeyEvent e) {
		}
		
	}
	
	/**
	 * Create the application.
	 */
	public ProjectTreeWindow() {
		initialize();
		frame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 439, 501);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		mScrollPane = new JScrollPane();
		frame.getContentPane().add(mScrollPane, BorderLayout.CENTER);
		mScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		mScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		panel_1 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_1.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		panel.add(panel_1);
		
		lblNewLabel = new JLabel("型の名前");
		panel_1.add(lblNewLabel);
		
		panel_2 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_2.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		panel.add(panel_2);
		
		lblNewLabel_1 = new JLabel("コメント");
		panel_2.add(lblNewLabel_1);
		
		JMenu menu_newItem = new JMenu("新規");
		
		ProjectTreeNode.Type[] types = new ProjectTreeNode.Type[]{
			ProjectTreeNode.Type.TABLE,
			ProjectTreeNode.Type.FACTORY,
			ProjectTreeNode.Type.ELEMENT,
		};
		for (int i = 0; i < types.length; ++i) {
			final ProjectTreeNode.Type type = types[i];
			JMenuItem menuItem_newItem = new JMenuItem(type.getHead());
			menu_newItem.add(menuItem_newItem);
			menuItem_newItem.addActionListener( new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					createByNewTab(type, mPopupMenuTargetNode);
				}
			});			
		}
		
		popupMenu.add(menu_newItem);
	}

	public void setProjectDataModel(ProjectData data)
	{
		mTreeDataModel = new SchemaTreeDataModel(data);
		if (mTree == null)
		{
			mTree = new JTree(mTreeDataModel);
			ShowObjectHandler handler = new ShowObjectHandler();
			mTree.addMouseListener(handler);
			mTree.addKeyListener(handler);
			mTree.setCellRenderer(new ProjectTreeCellRenderer());				
			mScrollPane.setViewportView(mTree);			
		}
		else
		{
			mTree.setModel(mTreeDataModel);			
		}
	}
	
	protected void showPopupMenu(int x, int y, TreePath path) {
		if (path == null) {
			return;
		}
		mPopupMenuTargetNode = (ProjectTreeNode) path.getLastPathComponent();
		popupMenu.show(mTree, x, y);
	}
	
	protected void createByNewTab(ProjectTreeNode.Type type, ProjectTreeNode node) {
		if (type == ProjectTreeNode.Type.TABLE) {
			TablePropertyWindow window = WindowDirector.INSTANCE.getTablePropertyWindow();
			window.reservePackage(node.getPackageNode().getPackage());
			window.newData( 
				new PropertyWindow.IItemBuildListener<TableBuilder>() {
					@Override
					public void onBuild(TableBuilder newItem) {
						ProjectDataModel.INSTANCE.getProjectData().getTableDataModel().addElement(newItem);
					}
				}
			);
		}
		if (type == ProjectTreeNode.Type.FACTORY) {
			FactoryPropertyWindow window = WindowDirector.INSTANCE.getFactoryPropertyWindow();
			window.reservePackage(node.getPackageNode().getPackage());
			window.newData( 
				new PropertyWindow.IItemBuildListener<FactoryBuilder>() {
					@Override
					public void onBuild(FactoryBuilder newItem) {
						ProjectDataModel.INSTANCE.getProjectData().getFactoryModel().addElement(newItem);
					}
				}
			);
		}
		if (type == ProjectTreeNode.Type.ELEMENT) {
			ElementTypePropertyWindow window = WindowDirector.INSTANCE.getElementTypePropertyWindow();
			window.reservePackage(node.getPackageNode().getPackage());
			window.newData( 
				new PropertyWindow.IItemBuildListener<ElementTypeBuilder>() {
					@Override
					public void onBuild(ElementTypeBuilder newItem) {
						ProjectDataModel.INSTANCE.getProjectData().getElementTypeModel().addElement( newItem );
					}
				}
			);
		}
		mTreeDataModel.onUpdateProjectData();
	}

	protected void openByNewTab(TreePath path) {
		if (path == null) {
			return;
		}
		ProjectTreeNode node = (ProjectTreeNode) path.getLastPathComponent();
		if (!node.isLeaf()) {
			return;
		}
		WindowDirector.INSTANCE.getMainMenu().openByNewTab(node.createTab());
	}
	
}
