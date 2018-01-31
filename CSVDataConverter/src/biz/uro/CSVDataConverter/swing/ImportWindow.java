package biz.uro.CSVDataConverter.swing;

import biz.uro.CSVDataConverter.swing.builder.PackageBuilder;
import biz.uro.CSVDataConverter.swing.builder.ProjectData;
import biz.uro.CSVDataConverter.swing.old.DictionaryFrame;
import biz.uro.CSVDataConverter.swing.window.tab.packagePath.PackagePropertyWindow;

@SuppressWarnings("serial")
public class ImportWindow extends DictionaryFrame<PackageBuilder, PackagePropertyWindow> {

	/**
	 * Create the frame.
	 */
	public ImportWindow( String head ) {
		super( head, String.format( "%-42s %-12s", "パッケージ名", "クラス名" ) );
		setBounds( 100, 100, 450, 300 );
	}

	@Override
	protected PackagePropertyWindow createPropertyWindow( String name ) {
		return new PackagePropertyWindow( name, this );
	}

	@Override
	public void onReloadProject( ProjectData data ) {
		mList.setDataModel( data.getPackageModel() );
	}

}
