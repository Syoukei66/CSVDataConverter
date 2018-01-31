package biz.uro.CSVDataConverter.swing.window.tab.factory;

import java.awt.Component;

import biz.uro.CSVDataConverter.swing.ProjectDataModel;
import biz.uro.CSVDataConverter.swing.builder.ElementTypeBuilder;
import biz.uro.CSVDataConverter.swing.builder.FactoryBuilder;
import biz.uro.CSVDataConverter.swing.builder.FactoryMethodBuilder;
import biz.uro.CSVDataConverter.swing.old.JSONListModel;
import biz.uro.CSVDataConverter.swing.old.PropertyWindow;
import biz.uro.CSVDataConverter.swing.util.ValidationUtil;

@SuppressWarnings("serial")
public class FactoryPropertyWindow extends PropertyWindow<FactoryBuilder> {

	private final FactoryPropertyPanel mPanel;
	private String mReservedPackageName = null;
	
	public FactoryPropertyWindow( String name, Component parent ) {
		super( name, parent );
		setBounds( 100, 100, 485, 350 );
		mPanel = new FactoryPropertyPanel( this );
		mContentPane.add( mPanel );
	}
	
	@Override
	protected boolean validation() {
		if ( ValidationUtil.INSTANCE.varidationJText( mPanel.mPackageName ) ) {
			return false;
		}
		if ( ValidationUtil.INSTANCE.varidationJText( mPanel.mClassName ) ) {
			return false;
		}
		final Object selectedItem = mPanel.mTypeComboBox.getSelectedItem();
		if ( selectedItem == null ) {
			return false;
		}
		return true;
	}

	@Override
	protected void init() {
		mPanel.mPackageName.setText( mReservedPackageName );
		mReservedPackageName = null;
		mPanel.mClassName.setText( null );
		mPanel.mTypeComboBox.setSelectedItem( null );
		mPanel.mMethodList.setDataModel( new JSONListModel<>( FactoryMethodBuilder.class ) );
	}

	@Override
	protected void setTarget( FactoryBuilder target ) {
		mPanel.mPackageName.setText( target.getPackage().getPackage() );
		mPanel.mClassName.setText( target.getPackage().getClassName() );
		mPanel.mTypeComboBox.setSelectedItem( target.getType() );
		mPanel.mMethodList.setDataModel( target.getMethodListModel() );
	}

	@Override
	protected void edit( FactoryBuilder target ) {
		target.getPackage().setPackage( mPanel.mPackageName.getText() );
		target.getPackage().setClassName( mPanel.mClassName.getText() );
		target.setType( (ElementTypeBuilder) mPanel.mTypeComboBox.getSelectedItem() );
	}

	@Override
	protected FactoryBuilder build() {
		final FactoryBuilder ret = ProjectDataModel.INSTANCE.getProjectData().createFactory();
		edit( ret );
		return ret;
	}

	public void reservePackage(String packaqe) {
		mReservedPackageName = packaqe;
	}
}
