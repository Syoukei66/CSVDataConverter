package biz.uro.CSVDataConverter.swing.window.tab.packagePath;

import java.awt.Component;

import biz.uro.CSVDataConverter.database.element.ElementType.Tag;
import biz.uro.CSVDataConverter.swing.builder.PackageBuilder;
import biz.uro.CSVDataConverter.swing.util.ValidationUtil;
import biz.uro.CSVDataConverter.swing.window.PropertyWindow;

@SuppressWarnings("serial")
public class PackagePropertyWindow extends PropertyWindow<PackageBuilder> {

	private final PackagePropertyPanel mPanel;
	
	public PackagePropertyWindow( String name, Component parent ) {
		super( name, parent );
		setBounds( 100, 100, 400, 140 );
		mPanel = new PackagePropertyPanel( this );
		mContentPane.add( mPanel );
	}

	@Override
	protected boolean validation() {
		if ( ValidationUtil.INSTANCE.varidationJText( mPanel.mPackagePath ) ) {
			return false;
		}
		if ( ValidationUtil.INSTANCE.varidationJText( mPanel.mClassName ) ) {
			return false;
		}
		return true;
	}

	@Override
	protected void init() {
		mPanel.mPackagePath.setText( null );
		mPanel.mClassName.setText( null );		
	}

	@Override
	protected void setTarget( PackageBuilder target ) {
		mPanel.mPackagePath.setText( target.getPackage() );
		mPanel.mClassName.setText( target.getClassName() );
	}

	@Override
	protected void edit( PackageBuilder target ) {
		target.setClassName( mPanel.mClassName.getText() );
		target.setPackage( mPanel.mPackagePath.getText() );
	}

	@Override
	protected PackageBuilder build() {
		final PackageBuilder builder = new PackageBuilder( Tag.USER );
		edit( builder );
		return builder;
	}

}
