package biz.uro.CSVDataConverter.swing.window.tab.elementType;

import javax.swing.JFrame;

import biz.uro.CSVDataConverter.swing.ProjectDataModel;
import biz.uro.CSVDataConverter.swing.builder.ElementTypeBuilder;
import biz.uro.CSVDataConverter.swing.old.PropertyWindow;
import biz.uro.CSVDataConverter.swing.util.ValidationUtil;

@SuppressWarnings("serial")
public class ElementTypePropertyWindow extends PropertyWindow<ElementTypeBuilder> {

	private final ElementTypePropertyPanel mPanel;
	private String mReservedPackageName = null;
	
	/**
	 * Create the dialog.
	 */
	public ElementTypePropertyWindow( String name, JFrame parent ) {
		super( name, parent );
		setBounds( 100, 100, 400, 140 );
		mPanel = new ElementTypePropertyPanel( this );
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
		return true;
	}

	@Override
	protected void init() {
		mPanel.mPackageName.setText( mReservedPackageName );
		mReservedPackageName = null;
		mPanel.mClassName.setText( null );
	}

	@Override
	protected void setTarget( ElementTypeBuilder target ) {
		mPanel.mPackageName.setText( target.getPackage().getPackage() );
		mPanel.mClassName.setText( target.getPackage().getClassName() );
	}

	@Override
	protected void edit( ElementTypeBuilder target ) {
		target.getPackage().setPackage( mPanel.mPackageName.getText() );
		target.getPackage().setClassName( mPanel.mClassName.getText() );
	}

	@Override
	protected ElementTypeBuilder build() {
		final ElementTypeBuilder ret = ProjectDataModel.INSTANCE.getProjectData().createType();
		edit( ret );
		return ret;
	}

	public void reservePackage( String packaqe ) {
		mReservedPackageName = packaqe;
	}

}
