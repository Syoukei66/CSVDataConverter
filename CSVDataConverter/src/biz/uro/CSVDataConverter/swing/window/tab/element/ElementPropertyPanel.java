package biz.uro.CSVDataConverter.swing.window.tab.element;

import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;
import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;

import biz.uro.CSVDataConverter.swing.ProjectDataModel;
import biz.uro.CSVDataConverter.swing.ProjectDataModel.IProjectDataReloadListener;
import biz.uro.CSVDataConverter.swing.builder.ProjectData;
import biz.uro.CSVDataConverter.swing.builder.StatementBuilder;
import biz.uro.CSVDataConverter.swing.builder.validator.Validator;
import biz.uro.CSVDataConverter.swing.builder.validator.condition.ValidatorCondition.ValidatorParmModel;
import biz.uro.CSVDataConverter.swing.builder.validator.kind.ValidatorKind;
import biz.uro.CSVDataConverter.swing.constants.Constants;
import biz.uro.CSVDataConverter.swing.old.JSONListModel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.BoxLayout;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.border.EtchedBorder;

import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class ElementPropertyPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	protected final JTextField mOutName;
	protected final JTextField mFieldName;
	protected final JSpinner mLength;
	protected final JCheckBox mIsPrimaryKey;
	protected final JCheckBox mIsNotNull;
	
	protected final JComboBox<StatementBuilder> mStatementComboBox;
	protected StatementBuilder mStatement;
	protected final JComboBox<ValidatorKind> mValidatorKind;
	protected Validator mValidator;
	
	private final JPanel mPanel1;
	private final JPanel mPanel2;
	private final JLabel mLabel1;
	private final JLabel mLabel2;
	private final JFormattedTextField mTextField1;
	private final JFormattedTextField mTextField2;

	private JPanel panel_1;
	private JPanel panel_2;
	private JLabel lblNewLabel_1;
	private JPanel panel_3;
	private JPanel panel_4;
	private JPanel panel_5;
	private JPanel panel_6;
	private JPanel panel_7;
	private JPanel panel_9;
	private JLabel label_1;
	private JPanel panel_10;
	private JPanel panel_11;

	/**
	 * Create the panel.
	 */
	public ElementPropertyPanel( ElementPropertyWindow window ) {
		setBorder(new EmptyBorder(5, 5, 5, 5));
		
		panel_3 = new JPanel();
		
		panel_5 = new JPanel();
		panel_5.setLayout(new BoxLayout(panel_5, BoxLayout.X_AXIS));
		
		JLabel label_2 = new JLabel("型名　　 ");
		panel_5.add(label_2);
		
		mStatementComboBox = new JComboBox<>();
		mStatementComboBox.setMaximumSize( new Dimension( Short.MAX_VALUE, Constants.LABEL_HEIGHT ) );
		panel_5.add(mStatementComboBox);
		mStatementComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				window.onUpdateData();
				final StatementBuilder statement = (StatementBuilder)mStatementComboBox.getSelectedItem();
				setStatement( statement );
			}
		});
		panel_3.setLayout(new BoxLayout(panel_3, BoxLayout.Y_AXIS));
		
		panel_1 = new JPanel();
		panel_3.add(panel_1);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.X_AXIS));
		
		JLabel lblNewLabel = new JLabel("フィールド名　");
		panel_1.add(lblNewLabel);
		
		mFieldName = new JTextField();
		panel_1.add(mFieldName);
		mFieldName.setColumns(10);
		mFieldName.setMaximumSize( new Dimension( Short.MAX_VALUE, Constants.LABEL_HEIGHT ) );
		mFieldName.addKeyListener( new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				window.onUpdateData();
			}
		} );
		
		panel_2 = new JPanel();
		panel_3.add(panel_2);
		panel_2.setLayout(new BoxLayout(panel_2, BoxLayout.X_AXIS));
		
		lblNewLabel_1 = new JLabel("論理名　　　 ");
		panel_2.add(lblNewLabel_1);
		
		mOutName = new JTextField();
		panel_2.add(mOutName);
		mOutName.setColumns(10);
		mOutName.setMaximumSize( new Dimension( Short.MAX_VALUE, Constants.LABEL_HEIGHT ) );
		
		panel_4 = new JPanel();
		panel_2.add(panel_4);
		panel_4.setLayout(new BoxLayout(panel_4, BoxLayout.X_AXIS));
		
		JLabel label = new JLabel("サイズ");
		panel_4.add(label);
		
		mLength = new JSpinner();
		mLength.setModel(new SpinnerNumberModel(1, 1, 1024, 1));
		mLength.setMaximumSize( new Dimension( Short.MAX_VALUE, Constants.LABEL_HEIGHT ) );
		panel_4.add(mLength);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(panel_3);
		add(panel_5);
		
		panel_7 = new JPanel();
		panel_7.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		add(panel_7);
		panel_7.setLayout(new BoxLayout(panel_7, BoxLayout.Y_AXIS));
		
		panel_9 = new JPanel();
		panel_7.add(panel_9);
		panel_9.setLayout(new BoxLayout(panel_9, BoxLayout.X_AXIS));
		
		label_1 = new JLabel("入力規則　");
		panel_9.add(label_1);
		
		mValidatorKind = new JComboBox<>();
		mValidatorKind.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				final ValidatorKind kind = (ValidatorKind) mValidatorKind.getSelectedItem();
				if ( kind == null ) {
					return;
				}
				setValidator( kind.create() );
			}
		});
		mValidatorKind.setMaximumSize( new Dimension( Short.MAX_VALUE, Constants.LABEL_HEIGHT ) );
		panel_9.add(mValidatorKind);
		
		mPanel1 = new JPanel();
		mPanel1.setVisible( false );
		mPanel1.setMaximumSize( new Dimension( Short.MAX_VALUE, Constants.LABEL_HEIGHT ) );
		panel_7.add(mPanel1);
		mPanel1.setLayout(new BoxLayout(mPanel1, BoxLayout.X_AXIS));
		
		panel_10 = new JPanel();
		mPanel1.add(panel_10);
		panel_10.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		mLabel1 = new JLabel("Label1");
		panel_10.add(mLabel1);
		mTextField1 = new JFormattedTextField();
		mPanel1.add(mTextField1);
		
		mPanel2 = new JPanel();
		mPanel2.setVisible( false );
		mPanel2.setMaximumSize( new Dimension( Short.MAX_VALUE, Constants.LABEL_HEIGHT ) );
		panel_7.add(mPanel2);
		mPanel2.setLayout(new BoxLayout(mPanel2, BoxLayout.X_AXIS));
		
		panel_11 = new JPanel();
		mPanel2.add(panel_11);
		panel_11.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		mLabel2 = new JLabel("Label2");
		panel_11.add(mLabel2);
		
		mTextField2 = new JFormattedTextField();
		mPanel2.add(mTextField2);
		
		panel_6 = new JPanel();
		add(panel_6);
		panel_6.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		
		JPanel panel = new JPanel();
		panel_6.add(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		mIsPrimaryKey = new JCheckBox("主キー");
		mIsPrimaryKey.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				final boolean selected = mIsPrimaryKey.isSelected();
				if ( selected ) {
					mIsNotNull.setSelected( true );
				}
				mIsNotNull.setEnabled( !selected );
			}
		});
		mIsPrimaryKey.setMaximumSize( new Dimension( Short.MAX_VALUE, Constants.LABEL_HEIGHT ) );
		panel.add(mIsPrimaryKey);
		
		mIsNotNull = new JCheckBox("NotNull");
		mIsNotNull.setMaximumSize( new Dimension( Short.MAX_VALUE, Constants.LABEL_HEIGHT ) );
		panel.add(mIsNotNull);
		
		ProjectDataModel.INSTANCE.addProjectReloadListener( new IProjectDataReloadListener() {
			@Override
			public void onReloadProject( ProjectData data ) {
				mStatementComboBox.setModel( data.getStatementModel() );
			}
		} );
	}

	public void setValidator( Validator validator ) {
		final Validator before = mValidator;
		if ( validator == null ) {
			mValidator = null;
			mValidatorKind.setSelectedIndex( -1 );
			mPanel1.setVisible( false );
			mPanel2.setVisible( false );
			return;
		}
		final ValidatorKind nextKind = validator.getKind();
		if ( before == null || nextKind != before.getKind() ) {
			mValidator = validator;
			mValidatorKind.setSelectedItem( nextKind );
		}
		final ValidatorParmModel model1 = mValidator.getCondition().model1();
		final ValidatorParmModel model2 = mValidator.getCondition().model2();
		if ( model1 != null ) {
			mLabel1.setText( model1.name );
			mTextField1.setDocument( model1.model );
			mPanel1.setVisible( true );
		}
		else {
			mPanel1.setVisible( false );
		}
		if ( model2 != null ) {
			mLabel2.setText( model2.name );
			mTextField2.setDocument( model2.model );
			mPanel2.setVisible( true );
		}
		else {
			mPanel2.setVisible( false );
		}
	}

	public void setStatement( StatementBuilder statement ) {				
		final StatementBuilder before = mStatement;
		mStatement = statement;
		if ( statement != null ) {
			if ( statement == before ) {
				return;
			}
			mStatementComboBox.setSelectedItem( statement );
			final JSONListModel<ValidatorKind> model = statement.getValidatorModel();
			mValidatorKind.setSelectedIndex( -1 );
			mValidatorKind.setModel( model );
			mValidatorKind.setSelectedIndex( 0 );
			mValidator = model.get( 0 ).create();
		}
		else {
			mStatementComboBox.setSelectedIndex( -1 );
		}	
	}
	
}
