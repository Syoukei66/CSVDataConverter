package biz.uro.CSVDataConverter.swing.window.create;

import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import biz.uro.CSVDataConverter.swing.ProjectDataModel;
import biz.uro.CSVDataConverter.swing.old.TransitionWindow;

@SuppressWarnings("serial")
public class GenerateProgramWindow extends TransitionWindow {

	private GenerateProgramTopPanel mTopPanel;
	
	public GenerateProgramWindow() {
		super( "プログラム生成", "生成開始" );
	}

	@Override
	protected IStatePanel initialPanel() {
		if ( mTopPanel == null ) {
			mTopPanel = new GenerateProgramTopPanel( this );
		}
		return mTopPanel;
	}

	public JFileChooser getCSVFolderChooser() {
		final JFileChooser filechooser = new JFileChooser();
		filechooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );
		filechooser.setAcceptAllFileFilterUsed( false );
		return filechooser;
	}
	
	private static String generateNowDatePath() {
		Date date = Calendar.getInstance().getTime();
		SimpleDateFormat sdf1 = new SimpleDateFormat( "yyyy_MM_dd" );
		SimpleDateFormat sdf2 = new SimpleDateFormat( "HHmmss" );
		return sdf1.format( date ) + "_" + sdf2.format( date );
	}

	public static String generatePath( String outputPath, boolean outputDate ) {
		if ( !outputDate ) {
			return outputPath;
		}
		return outputPath + "\\" + generateNowDatePath();
	}
	
	public static void generateProgram( Component parent, String inputPath, String outputPath, boolean outputDate ) {
		try {
			final String path = generatePath( outputPath, outputDate );
			ProjectDataModel.INSTANCE.getProjectData().generateProgram( inputPath, path );
		} catch ( Exception e ) {
			JOptionPane.showMessageDialog( parent, e.getMessage() );
			e.printStackTrace();
		}
	}
	
}
