package biz.uro.CSVDataConverter.swing.window;

import java.awt.EventQueue;
import java.awt.MouseInfo;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import biz.uro.CSVDataConverter.swing.DataDictionaryKind;
import biz.uro.CSVDataConverter.swing.ProjectDataModel;
import biz.uro.CSVDataConverter.swing.builder.ProjectData;
import biz.uro.CSVDataConverter.swing.dialog.MessageDialog;
import biz.uro.CSVDataConverter.swing.dialog.SaveCheckDialog;
import biz.uro.CSVDataConverter.swing.dialog.SaveCheckDialog.ISaveCheckDialogProcess;
import biz.uro.CSVDataConverter.swing.old.DictionaryFrame;
import biz.uro.CSVDataConverter.swing.util.FontUtil;
import biz.uro.CSVDataConverter.swing.window.ProjectTreeWindow;
import biz.uro.CSVDataConverter.swing.window.create.GenerateProgramWindow;
import biz.uro.CSVDataConverter.swing.window.tab.DataModelEditTab;
import biz.uro.CSVDataConverter.swing.window.tab.DataModelEditTabbedPane;
import biz.uro.CSVDataConverter.swing.window.tab.element.ElementPropertyWindow;
import biz.uro.CSVDataConverter.swing.window.tab.elementType.ElementTypePropertyWindow;
import biz.uro.CSVDataConverter.swing.window.tab.factory.FactoryPropertyWindow;
import biz.uro.CSVDataConverter.swing.window.tab.factory.method.FactoryMethodPropertyWindow;
import biz.uro.CSVDataConverter.swing.window.tab.table.TablePropertyWindow;

import java.awt.Font;

import javax.swing.JFileChooser;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JCheckBoxMenuItem;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.filechooser.FileFilter;
import javax.swing.BoxLayout;

@SuppressWarnings("serial")
public class MainMenu extends JFrame {
	
	private JPanel contentPane;
	private DataModelEditTabbedPane mTabbedPane;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		System.setProperty("file.encoding", "UTF-8");
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainMenu frame = new MainMenu();
					final Point mousePoint = MouseInfo.getPointerInfo().getLocation();
					frame.setLocation( mousePoint );
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private static final String TITLE = "CSVSchemaEditor";
	
	private ProjectTreeWindow mProjectTreeWindow;
	private GenerateProgramWindow mGenerateProgramWindow;
	
	private final JCheckBoxMenuItem mWindowMoveSync;
	
	private int mOldX = Integer.MIN_VALUE;
	private int	mOldY = Integer.MIN_VALUE;
	
	/**
	 * Create the frame.
	 */
	private MainMenu() {

		FontUtil.setFont( new Font( "ＭＳ ゴシック", Font.PLAIN, 14 ) );
	
		WindowDirector.INSTANCE.setMainMenu( this );
		WindowDirector.INSTANCE.setTablePropertyWindow( new TablePropertyWindow( "Table", this ) );
		WindowDirector.INSTANCE.setElementPropertyWindow( new ElementPropertyWindow( "Attribute", this ) );
		WindowDirector.INSTANCE.setFactoryMethodPropertyWindow( new FactoryMethodPropertyWindow( "FactoryMethod", this ) );
		WindowDirector.INSTANCE.setFactoryPropertyWindow( new FactoryPropertyWindow( "Factory", this ) );
		WindowDirector.INSTANCE.setElementTypePropertyWindow( new ElementTypePropertyWindow( "Type", this) );

		setTitle( TITLE );
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 640, 480);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnNewMenu = new JMenu("ファイル");
		mnNewMenu.setFont(new Font("Yu Gothic UI", Font.PLAIN, 12));
		menuBar.add(mnNewMenu);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("新規作成");
		mntmNewMenuItem.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent e ) {
				showCheckSaveDialog( new ISaveCheckDialogProcess() {
					@Override
					public void saveProcess() {
						saveProcess();
					}
					@Override
					public void process() {
						ProjectDataModel.INSTANCE.newData( MainMenu.this );						
					}
				} );
			}
		} );
		mnNewMenu.add(mntmNewMenuItem);
		
		JMenuItem menuItem = new JMenuItem("開く");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showCheckSaveDialog( new ISaveCheckDialogProcess() {
					@Override
					public void saveProcess() {
						MainMenu.this.saveProcess();
					}
					@Override
					public void process() {
						loadProcess();
					}
				} );
			}
		});
		mnNewMenu.add(menuItem);
		
		JMenuItem mntmNewMenuItem_1 = new JMenuItem("名前を付けて保存");
		mntmNewMenuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveProcess();
			}
		});
		mnNewMenu.add(mntmNewMenuItem_1);
		
		JMenuItem menuItem_1 = new JMenuItem("上書き保存");
		menuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					ProjectDataModel.INSTANCE.overrideWrite();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		mnNewMenu.add(menuItem_1);
		
		JMenu mnNewMenu_1 = new JMenu("ウィンドウ");
		menuBar.add(mnNewMenu_1);
		
		JMenu mnNewMenu_2 = new JMenu("表示");
		mnNewMenu_1.add(mnNewMenu_2);
		
		for ( DictionaryFrame<?, ?> frame : DataDictionaryKind.frameValues() ) {
			final JCheckBoxMenuItem checkBox = new JCheckBoxMenuItem( frame.getTitle() );
			checkBox.setModel( frame.getShowModel() );
			mnNewMenu_2.add( checkBox );
		}
		
		mWindowMoveSync = new JCheckBoxMenuItem("ウィンドウの動きを同期する");
		mWindowMoveSync.setState( true );
		
		JMenuItem menuItem_2 = new JMenuItem("全てのウィンドウを表示");
		menuItem_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for ( DictionaryFrame<?,?> window : DataDictionaryKind.frameValues() ) {					
					window.setVisible( true );
				}
			}
		});
		mnNewMenu_1.add(menuItem_2);
		
		JMenuItem menuItem_3 = new JMenuItem("全てのウィンドウを非表示");
		menuItem_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for ( DictionaryFrame<?,?> window : DataDictionaryKind.frameValues() ) {
					window.setVisible( false );
				}
			}
		});
		mnNewMenu_1.add(menuItem_3);
		mnNewMenu_1.add(mWindowMoveSync);
		
		JMenu menu = new JMenu("プログラム生成");
		menuBar.add(menu);
		
		JMenuItem menuItem_4 = new JMenuItem("プログラム生成");
		menu.add(menuItem_4);
		menuItem_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ( mGenerateProgramWindow == null ) {
					mGenerateProgramWindow = new GenerateProgramWindow();					
				}
				mGenerateProgramWindow.run( MainMenu.this );
			}
		});
				
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		final WindowAdapter adapter =  new WindowAdapter() {			
			@Override
			public void windowDeiconified(WindowEvent e) {
				for ( JFrame window : DataDictionaryKind.frameValues() ) {
					window.toFront();
				}
				toFront();
			}
		};
		
		for ( JFrame window : DataDictionaryKind.frameValues() ) {
			window.addWindowListener( adapter );
		}
		addWindowListener( adapter );
		
		addComponentListener( new ComponentAdapter() {
			@Override
			public void componentMoved( ComponentEvent e ) {
				final int x = (int)getLocation().getX();
				final int y = (int)getLocation().getY();
				if ( mWindowMoveSync.getState() ) {
					for ( JFrame window : DataDictionaryKind.frameValues() ) {
						int moveX, moveY;
						if ( mOldX == Integer.MIN_VALUE ) {
							moveX = x - window.getWidth();
						}
						else {
							moveX = window.getX() - (mOldX - x);
						}
						if ( mOldY == Integer.MIN_VALUE ) {
							moveY = y;
						}
						else {
							moveY = window.getY() - (mOldY - y);
						}
						window.setLocation( moveX, moveY );					
					}
				}
				mOldX = x;
				mOldY = y;
			}
		});
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		
		mTabbedPane = new DataModelEditTabbedPane();
		contentPane.add(mTabbedPane);

		for ( DictionaryFrame<?,?> window : DataDictionaryKind.frameValues() ) {
			ProjectDataModel.INSTANCE.addProjectReloadListener( window );
		}
		
		ProjectDataModel.INSTANCE.newData( this );

		mProjectTreeWindow = new ProjectTreeWindow();
		mProjectTreeWindow.setProjectDataModel(ProjectDataModel.INSTANCE.getProjectData());

	}
	
	private void showCheckSaveDialog( ISaveCheckDialogProcess process ) {
		final MessageDialog dialog = new SaveCheckDialog( process );
		dialog.run( MainMenu.this );
	}
	
	private JFileChooser getFileChooser() {
		final JFileChooser filechooser = new JFileChooser();
		final FileFilter filter = new FileFilter() {
			
			@Override
			public String getDescription() {
				return "csvスキーマファイル(*." + ProjectData.EXTENTION + ")";
			}
			
			@Override
			public boolean accept( File f ) {
				if ( f.isDirectory() ) {
					return true;
				}
				final String extention = getExtension( f );
				if ( extention == null ) {
					return false;
				}
				if ( extention.equals( ProjectData.EXTENTION ) ) {
					return true;
				}
				return false;
			}
			
			private String getExtension(File f){
				String fileName = f.getName();
				String[] filePaths = fileName.split( "\\." );
				return filePaths[filePaths.length - 1];
			}
		};
		filechooser.addChoosableFileFilter( filter );
		filechooser.setAcceptAllFileFilterUsed( false );
		return filechooser;
	}
	
	private void saveProcess() {
		JFileChooser filechooser = getFileChooser();
		int selected = filechooser.showSaveDialog( MainMenu.this );
		if ( selected == JFileChooser.APPROVE_OPTION ) {
			final File file = filechooser.getSelectedFile();
			try {
				ProjectDataModel.INSTANCE.save( file.getAbsolutePath() );
			} catch ( IOException e1 ) {
				e1.printStackTrace();
			}
		}		
	}
	
	private void loadProcess() {
		JFileChooser filechooser = getFileChooser();
		int selected = filechooser.showOpenDialog( MainMenu.this );
		if ( selected == JFileChooser.APPROVE_OPTION ) {
			final File file = filechooser.getSelectedFile();
			try {
				ProjectDataModel.INSTANCE.load( file.getAbsolutePath() );
				mProjectTreeWindow.setProjectDataModel(ProjectDataModel.INSTANCE.getProjectData());
			} catch ( IOException e1 ) {
				e1.printStackTrace();
			}
		}		
	}

	public void onChangeNowEditPath( String nowEditPath ) {
		setTitle( TITLE + " [" + nowEditPath + "]" );
	}
	
	public void openByNewTab(DataModelEditTab<?> tab) {
		mTabbedPane.openByNewTab(tab);
	}
	
}
