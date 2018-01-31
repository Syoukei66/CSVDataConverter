package biz.uro.CSVDataConverter.swing;

import biz.uro.CSVDataConverter.swing.builder.ElementTypeBuilder;
import biz.uro.CSVDataConverter.swing.builder.ProjectData;
import biz.uro.CSVDataConverter.swing.old.DictionaryFrame;
import biz.uro.CSVDataConverter.swing.window.tab.elementType.ElementTypePropertyWindow;

@SuppressWarnings("serial")
public class ElementTypeWindow extends DictionaryFrame<ElementTypeBuilder,ElementTypePropertyWindow> {

	/**
	 * Create the frame.
	 */
	public ElementTypeWindow( String head ) {
		super( head, String.format( "%-14s%-11s%-13s", "型名", "パッケージ", "タイプ" ) );
		setBounds( 100, 100, 380, 250 );
	}
	
	@Override
	protected ElementTypePropertyWindow createPropertyWindow( String name) {
		return new ElementTypePropertyWindow( name, ElementTypeWindow.this );
	}

	@Override
	public void onReloadProject( ProjectData data ) {
		mList.setDataModel( data.getElementTypeModel() );
	}

}
