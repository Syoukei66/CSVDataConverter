package biz.uro.CSVDataConverter.swing.window.tab.table;

import javax.swing.JFrame;

import biz.uro.CSVDataConverter.generator.RecordDefinitionType;
import biz.uro.CSVDataConverter.swing.ProjectDataModel;
import biz.uro.CSVDataConverter.swing.builder.ElementBuilder;
import biz.uro.CSVDataConverter.swing.builder.TableBuilder;
import biz.uro.CSVDataConverter.swing.old.PropertyWindow;
import biz.uro.CSVDataConverter.swing.util.ValidationUtil;

@SuppressWarnings("serial")
public class TablePropertyWindow extends PropertyWindow<TableBuilder> {
	
	private final TablePropertyPanel mPanel;
	protected String mReservedPackageName = null;
	
	/**
	 * Create the frame.
	 */
	public TablePropertyWindow( String name, JFrame parent ) {
		super( name, parent );
		setBounds(100, 100, 675, 600);
		mPanel = new TablePropertyPanel( this );
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
		mPanel.mProgram.setText( null );
		mPanel.mComment.setText( null );
		mPanel.mInterfaceAttribute.setSelectedIndex( -1 );
		mPanel.setSchema( null );
		mPanel.mRecordDefinitionType.setSelectedItem( RecordDefinitionType.USE_CSV_ALL );
	}

	@Override
	protected void setTarget( TableBuilder target ) {
		mPanel.mPackageName.setText( target.getPackage().getPackage() );
		mPanel.mClassName.setText( target.getPackage().getClassName() );
		mPanel.mProgram.setText( target.getProgramFormat() );
		mPanel.mComment.setText( target.getCommentFormat() );
		mPanel.setSchema( target.getSchema() );
		mPanel.mInterfaceAttribute.setSelectedItem( target.getLogicElement() );
		mPanel.mRecordDefinitionType.setSelectedItem( target.getRecordDefinitionType() );
	}

	@Override
	protected void edit( TableBuilder target ) {
		target.getPackage().setPackage( mPanel.mPackageName.getText() );
		target.getPackage().setClassName( mPanel.mClassName.getText() );
		target.setProgramFormat( mPanel.mProgram.getText() );
		target.setCommentFormat( mPanel.mComment.getText() );
		target.setLogicElement( (ElementBuilder)mPanel.mInterfaceAttribute.getSelectedItem() );
		target.setRecordDefinitionType( (RecordDefinitionType)mPanel.mRecordDefinitionType.getSelectedItem() );
	}

	@Override
	protected TableBuilder build() {		
		final TableBuilder builder = ProjectDataModel.INSTANCE.getProjectData().createTable();
		edit( builder );
		return builder;
	}

	public void reservePackage(String packaqe) {
		mReservedPackageName = packaqe;
	}

}
