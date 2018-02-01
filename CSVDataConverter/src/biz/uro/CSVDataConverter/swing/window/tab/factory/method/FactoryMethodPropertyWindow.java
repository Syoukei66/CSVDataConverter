package biz.uro.CSVDataConverter.swing.window.tab.factory.method;

import java.awt.Component;

import biz.uro.CSVDataConverter.swing.builder.ElementBuilder;
import biz.uro.CSVDataConverter.swing.builder.FactoryMethodBuilder;
import biz.uro.CSVDataConverter.swing.old.JSONListModel;
import biz.uro.CSVDataConverter.swing.util.ValidationUtil;
import biz.uro.CSVDataConverter.swing.window.PropertyWindow;

@SuppressWarnings("serial")
public class FactoryMethodPropertyWindow extends PropertyWindow<FactoryMethodBuilder> {

	private final FactoryMethodPropertyPanel mPanel;

	public FactoryMethodPropertyWindow( String name, Component parent ) {
		super( name, parent );
		setBounds( 100, 100, 640, 450 );
		mPanel = new FactoryMethodPropertyPanel( this );
		mContentPane.add( mPanel );
	}

	@Override
	protected boolean validation() {
		if ( ValidationUtil.INSTANCE.varidationJText( mPanel.mIdentifier ) ) {
			return false;
		}
		return true;
	}

	@Override
	protected void init() {
		mPanel.mIdentifier.setText( null );
		mPanel.mComment.setText( null );
		mPanel.mElementList.setDataModel( new JSONListModel<>( ElementBuilder.class ) );
	}

	@Override
	protected void setTarget( FactoryMethodBuilder target ) {
		mPanel.mIdentifier.setText( target.getIdentifer() );
		mPanel.mComment.setText( target.getComment() );
		mPanel.mElementList.setDataModel( target.getSchemaBuilder().getElementDataModel() );
	}

	@Override
	protected void edit( FactoryMethodBuilder target ) {
		target.setIdentifer( mPanel.mIdentifier.getText() );
		target.setComment( mPanel.mComment.getText() );
	}

	@Override
	protected FactoryMethodBuilder build() {
		final FactoryMethodBuilder ret = new FactoryMethodBuilder( mPanel.mElementList.getDataModel() );
		edit( ret );
		return ret;
	}

}
