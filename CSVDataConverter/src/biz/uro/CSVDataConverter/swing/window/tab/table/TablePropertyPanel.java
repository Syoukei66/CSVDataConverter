package biz.uro.CSVDataConverter.swing.window.tab.table;

import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.BoxLayout;

import biz.uro.CSVDataConverter.generator.RecordDefinitionType;
import biz.uro.CSVDataConverter.swing.builder.ElementBuilder;
import biz.uro.CSVDataConverter.swing.builder.ElementTypeBuilder;
import biz.uro.CSVDataConverter.swing.builder.ElementTypeBuilder._ForeignTable;
import biz.uro.CSVDataConverter.swing.builder.SchemaBuilder;
import biz.uro.CSVDataConverter.swing.constants.Constants;
import biz.uro.CSVDataConverter.swing.old.JSONListModel;

import javax.swing.JScrollPane;

import java.awt.FlowLayout;

import javax.swing.JList;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.JComboBox;

import java.awt.Component;

@SuppressWarnings("serial")
public class TablePropertyPanel extends JPanel {

	private static class SpecialSentence {
		private final SpecialSentence mParent;
		private final int mId;
		private final int mIndex;
		private final ElementBuilder mType;
		SpecialSentence( SpecialSentence parent, int id, int index, ElementBuilder type ) {
			mParent = parent;
			mId = id;
			mIndex = index;
			mType = type;
		}
		
		public String script() {
			final StringBuilder str = new StringBuilder();
			if ( mParent != null ) {
				str.append( mParent.script() );	
				str.append( "." );
			}
			str.append( mId );
			if ( mType.getSize() > 1 ) {
				str.append( String.format( "[%d]", mIndex ) );
			}
			return str.toString();
		}
		
		public String type() {
			final StringBuilder str = new StringBuilder();
			str.append( mType.getStatement().getType().getPackage().getClassName() );
			return str.toString();			
		}
		
		public String comment() {
			final StringBuilder str = new StringBuilder();
			if ( mParent != null ) {
				str.append( mParent.comment() );	
				str.append( "." );
			}
			str.append( mType.getOutName() );
			if ( mType.getSize() > 1 ) {
				str.append( String.format( "[%d]", mIndex ) );
			}
			return str.toString();
		}
		
		@Override
		public String toString() {
			return String.format( "#%-16s %-16s %-16s", script(), type(), comment() );
		}
	}
	
	protected final JTextField mPackageName;
	protected final JTextField mClassName;
	protected final JComboBox<ElementBuilder> mInterfaceAttribute;
	protected final DefaultListModel<SpecialSentence> mSpecialSentenceModel;
	protected final JTextField mProgram;
	protected final JTextField mComment;

	//protected final JRadioButton[] mEnumAttributeButton = new JRadioButton[RecordDefinitionType.LIST.length];
	//protected final ButtonGroup mEnumAttributeButtonGroup;
	protected final JComboBox<RecordDefinitionType> mRecordDefinitionType;

	

	/**
	 * Create the panel.
	 */
	public TablePropertyPanel( TablePropertyWindow window ) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JPanel panel = new JPanel();
		add(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		JLabel PackageNameLabel = new JLabel("パッケージ*　");
		panel.add(PackageNameLabel);
		
		mPackageName = new JTextField();
		panel.add(mPackageName);
		mPackageName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				window.onUpdateData();
			}
		});
		mPackageName.setColumns(10);
		mPackageName.setMaximumSize( new Dimension( Short.MAX_VALUE, Constants.LABEL_HEIGHT ) );
		
		JLabel TableClassNameLabel = new JLabel("クラス名*　　");
		
		mClassName = new JTextField();
		mClassName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				window.onUpdateData();
			}
		});
		JPanel EditorPanel = new JPanel();
		add(EditorPanel);
		
		mClassName.setColumns(10);
		mClassName.setMaximumSize( new Dimension( Short.MAX_VALUE, Constants.LABEL_HEIGHT ) );
		EditorPanel.setLayout(new BoxLayout(EditorPanel, BoxLayout.X_AXIS));
		EditorPanel.add(TableClassNameLabel);
		EditorPanel.add(mClassName);
		
		JPanel panel_11 = new JPanel();
		add(panel_11);
		panel_11.setLayout(new BoxLayout(panel_11, BoxLayout.X_AXIS));
		
		JLabel lblNewLabel_5 = new JLabel("レコード定義*　");
		panel_11.add(lblNewLabel_5);
		
		mRecordDefinitionType = new JComboBox<>( RecordDefinitionType.LIST );
		mRecordDefinitionType.setMaximumSize( new Dimension( Short.MAX_VALUE, Constants.LABEL_HEIGHT ) );
		panel_11.add( mRecordDefinitionType );

		JPanel panel_6 = new JPanel();
		panel_6.setMinimumSize( new Dimension( Short.MAX_VALUE, Constants.LABEL_HEIGHT ) );
		panel_6.setAlignmentX(Component.RIGHT_ALIGNMENT);
		add(panel_6);
		panel_6.setLayout(new BoxLayout(panel_6, BoxLayout.Y_AXIS));
		
		JPanel panel_9 = new JPanel();
		panel_9.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel_6.add(panel_9);
		panel_9.setLayout(new BoxLayout(panel_9, BoxLayout.X_AXIS));
		
		JLabel lblNewLabel_4 = new JLabel("外部から参照する時に使用するAttribute　");
		panel_9.add(lblNewLabel_4);
		
		JPanel panel_10 = new JPanel();
		panel_10.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel_6.add(panel_10);
		panel_10.setLayout(new BoxLayout(panel_10, BoxLayout.X_AXIS));
		
		mInterfaceAttribute = new JComboBox<>();
		mInterfaceAttribute.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel_10.add(mInterfaceAttribute);
		mInterfaceAttribute.setMaximumSize( new Dimension( Short.MAX_VALUE, Constants.LABEL_HEIGHT ) );
		
		JPanel panel_5 = new JPanel();
		panel_5.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		add(panel_5);
		panel_5.setLayout(new BoxLayout(panel_5, BoxLayout.Y_AXIS));
		
		JPanel panel_4 = new JPanel();
		panel_5.add(panel_4);
		panel_4.setLayout(new BoxLayout(panel_4, BoxLayout.Y_AXIS));
		
		JPanel panel_2 = new JPanel();
		panel_4.add(panel_2);
		FlowLayout fl_panel_2 = new FlowLayout(FlowLayout.LEFT, 0, 5);
		panel_2.setLayout(fl_panel_2);
		panel_2.setMaximumSize( new Dimension( Short.MAX_VALUE, Constants.LABEL_HEIGHT ) );
		
		JLabel lblNewLabel_1 = new JLabel("プログラム上の列挙表記フォーマット**　　　　( 記述例：\"%03d_%02d\",#1,#2 )");
		panel_2.add(lblNewLabel_1);
		
		JPanel panel_8 = new JPanel();
		panel_4.add(panel_8);
		panel_8.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		panel_8.setMaximumSize( new Dimension( Short.MAX_VALUE, Constants.LABEL_HEIGHT ) );
		
		JLabel lblNewLabel_3 = new JLabel("列挙子名");
		panel_8.add(lblNewLabel_3);
		
		mProgram = new JTextField();
		panel_4.add(mProgram);
		mProgram.setColumns(10);
		mProgram.setMaximumSize( new Dimension( Short.MAX_VALUE, Constants.LABEL_HEIGHT ) );
		
		JPanel panel_7 = new JPanel();
		panel_4.add(panel_7);
		panel_7.setMaximumSize( new Dimension( Short.MAX_VALUE, Constants.LABEL_HEIGHT ) );
		panel_7.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		JLabel lblNewLabel_2 = new JLabel("コメント");
		panel_7.add(lblNewLabel_2);
		
		mComment = new JTextField();
		panel_4.add(mComment);
		mComment.setColumns(10);
		mComment.setMaximumSize( new Dimension( Short.MAX_VALUE, Constants.LABEL_HEIGHT ) );
		
		JPanel panel_3 = new JPanel();
		panel_5.add(panel_3);
		panel_3.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 5));
		panel_3.setMaximumSize( new Dimension( Short.MAX_VALUE, Constants.LABEL_HEIGHT ) );
		
		JLabel lblNewLabel = new JLabel("※以下の特殊文字が使用できます　　　 (Attributeが追加されると候補が増えます)");
		panel_3.add(lblNewLabel);
		
		JTextPane txtpnstringformat = new JTextPane();
		panel_5.add(txtpnstringformat);
		txtpnstringformat.setEditable(false);
		txtpnstringformat.setBackground(UIManager.getColor("Button.background"));
		txtpnstringformat.setText("また、特殊文字に末尾に以下のメソッドを追記することで、\r\nString.formatメソッドに与えられる型や値が変化します。\r\n\r\nメソッド名　　  　　戻り値　詳細\r\nなし　　　　　　　　Object　　特殊文字が示すオブジェクト\r\n.toString()　　　　 String　　特殊文字が示すオブジェクトのtoString()の結果\r\n.token(int index)　 String　　特殊文字を読み込む際に使用したトークン\r\n.program()　　　　　String　　特殊文字が示すオブジェクトのデータのプログラム表記");
		txtpnstringformat.setMaximumSize( new Dimension( Short.MAX_VALUE, 20 ) );
		
		JPanel panel_1 = new JPanel();
		panel_5.add(panel_1);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.X_AXIS));
		
		JScrollPane scrollPane = new JScrollPane();
		panel_1.add(scrollPane);
		
		JList<SpecialSentence> list = new JList<>();
		mSpecialSentenceModel = new DefaultListModel<>();
		list.setModel( mSpecialSentenceModel );
		
		scrollPane.setViewportView( list );
	
	}
	
	private void addSpecialSentence( SchemaBuilder root, SpecialSentence parent, int id, int index, ElementBuilder element ) {
		final SpecialSentence sentence = new SpecialSentence( parent, id, index, element );
		mSpecialSentenceModel.addElement( sentence );
		
		final ElementTypeBuilder type = element.getStatement().getType();
		//外部テーブルだった場合は、そのSchemaも特殊構文として追加
		if ( type instanceof ElementTypeBuilder._ForeignTable ) {
			final ElementTypeBuilder._ForeignTable table = (_ForeignTable) type;
			final SchemaBuilder schema = table.getSchemaBuilder();
			//循環参照防止
			if ( schema != root ) {
				addSchemaRoot( sentence, schema );				
			}
		}
		
		if ( index == 0 ) {
			final int childCount = element.getSize();
			for ( int i = 1; i < childCount; i++ ) {
				addSpecialSentence( root, parent, id, i, element );
			}
		}
	}
	
	private void addSchemaRoot( SpecialSentence parent, SchemaBuilder schema ) {
		if ( schema == null ) {
			return;
		}
		//elementを全て特殊構文に追加
		final JSONListModel<ElementBuilder> list = schema.getElementDataModel();
		final int count = list.size();
		for ( int i = 0; i < count; i++ ) {
			addSpecialSentence( schema, parent, i, 0, list.get( i ) );
		}
	}
	
	public void setSchema( SchemaBuilder schema ) {
		mSpecialSentenceModel.clear();
		if ( schema == null ) {
			mInterfaceAttribute.setModel( new DefaultComboBoxModel<>() );
			return;
		}
		else {
			mInterfaceAttribute.setModel( schema.getElementDataModel() );
		}
		addSchemaRoot( null, schema );
	}
	
}
