package biz.uro.CSVDataConverter.swing.window.tab.element;

import java.awt.Component;

import biz.uro.CSVDataConverter.swing.builder.ElementBuilder;
import biz.uro.CSVDataConverter.swing.builder.StatementBuilder;
import biz.uro.CSVDataConverter.swing.util.ValidationUtil;
import biz.uro.CSVDataConverter.swing.window.PropertyWindow;

public class ElementPropertyWindow extends PropertyWindow<ElementBuilder> {

	private static final long serialVersionUID = 1L;

	private final ElementPropertyPanel mPanel;
	
	/**
	 * Create the dialog.
	 */
	public ElementPropertyWindow( String title, Component parent ) {
		super( title, parent );
		setBounds( 100, 100, 380, 245 );
		
		mPanel = new ElementPropertyPanel( this );
		
		mContentPane.add( mPanel );
	}

	@Override
	protected boolean validation() {
		final StatementBuilder type = (StatementBuilder) mPanel.mStatementComboBox.getSelectedItem();
		if ( ValidationUtil.INSTANCE.varidationJText( mPanel.mFieldName ) ) {
			return false;
		}
		if ( type == null ) {
			return false;
		}
		return true;
	}

	@Override
	protected void init() {
		mPanel.mFieldName.setText( "" );
		mPanel.mOutName.setText( "" );
		mPanel.mLength.setValue( 1 );
		mPanel.mIsPrimaryKey.setSelected( false );
		mPanel.mIsNotNull.setSelected( false );
		mPanel.mIsNotNull.setEnabled( true );
		mPanel.setStatement( null );
		mPanel.setValidator( null );
	}

	@Override
	protected void setTarget( ElementBuilder target ) {
		mPanel.mFieldName.setText( target.getFieldName() );
		mPanel.mOutName.setText( target.getOutName() );
		mPanel.mLength.setValue( target.getSize() );
		mPanel.mIsPrimaryKey.setSelected( target.isPrimaryKey() );
		mPanel.mIsNotNull.setSelected( target.isNotNull() );
		mPanel.setStatement( target.getStatement() );
		mPanel.setValidator( target.getValidator() );
	}

	@Override
	protected void edit( ElementBuilder target ) {
		target.setFieldName( mPanel.mFieldName.getText() );
		target.setOutName( mPanel.mOutName.getText() );
		target.setStatement( (StatementBuilder)mPanel.mStatementComboBox.getSelectedItem() );
		target.setSize( (int)mPanel.mLength.getValue() );
		target.setIsPrimaryKey( mPanel.mIsPrimaryKey.isSelected() );
		target.setIsNotNull( mPanel.mIsNotNull.isSelected() );
		target.setValidator( mPanel.mValidator );
	}

	@Override
	protected ElementBuilder build() {
		final ElementBuilder builder = new ElementBuilder();
		edit( builder );
		return builder;
	}

}
