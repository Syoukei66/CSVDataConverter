package biz.uro.CSVDataConverter.swing;

import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import com.fasterxml.jackson.databind.ObjectMapper;

import biz.uro.CSVDataConverter.database.element.ElementType.Tag;
import biz.uro.CSVDataConverter.io.TextFileReader;
import biz.uro.CSVDataConverter.io.TextFileWriter;
import biz.uro.CSVDataConverter.swing.builder.ElementParserBuilder;
import biz.uro.CSVDataConverter.swing.builder.ElementTypeBuilder;
import biz.uro.CSVDataConverter.swing.builder.PackageBuilder;
import biz.uro.CSVDataConverter.swing.builder.ProjectData;
import biz.uro.CSVDataConverter.swing.builder.StatementBuilder;
import biz.uro.CSVDataConverter.swing.builder.validator.ValidatorKindEnum_Static;
import biz.uro.CSVDataConverter.swing.json.JSONException;
import biz.uro.CSVDataConverter.swing.json.JSONReader;
import biz.uro.CSVDataConverter.swing.json.JSONWriter;
import biz.uro.CSVDataConverter.swing.window.MainMenu;
import biz.uro.CSVDataConverter.swing.test.TestProjectData;

public enum ProjectDataModel {
	INSTANCE;
	
	public interface IProjectDataReloadListener {
		void onReloadProject( ProjectData data );
	}
	
	private static final String PROJECT_DATA = "ProjectData";
	
	private final ArrayList<IProjectDataReloadListener> mListenerList = new ArrayList<>();
	
	private MainMenu mFrame;
	private ProjectData mData;
	private String mNowEditDataPath;
	
	public void testData( MainMenu menu ) {
		mFrame = menu;
		mData = new TestProjectData();
		onProjectReload();
	}

	public void newData( MainMenu menu ) {
		mFrame = menu;
		mData = new ProjectData();
		mData.setupConstants();
		onProjectReload();
	}
	
	private void registerConstatns( JSONReader reader ) {
		for ( Tag tag : Tag.VALUES ) {
			reader.register( tag );
		}
		for ( PackageBuilder packaqe : PackageBuilder.TERMINALS ) {
			reader.register( packaqe );
		}
		for ( ElementParserBuilder parser : ElementParserBuilder.TERMINALS ) {
			reader.register( parser );
		}
		for ( ElementTypeBuilder primitive : ElementTypeBuilder.TERMINALS ) {
			reader.register( primitive );
		}
		for ( StatementBuilder statement : StatementBuilder.TERMINALS ) {
			reader.register( statement );
		}
		for ( ValidatorKindEnum_Static kind : ValidatorKindEnum_Static.VALUES ) {
			reader.register( kind.get() );
		}
	}

	private void setNowEditPath( String nowEditPath ) {
		mNowEditDataPath = nowEditPath;
		if ( mFrame != null ) {
			mFrame.onChangeNowEditPath( nowEditPath );			
		}
	}
	
	public void load( String path ) throws IOException {
		setNowEditPath( path );
		final TextFileReader tfr = new TextFileReader( path, ProjectData.EXTENTION );
		tfr.open();
		final ObjectMapper mapper = new ObjectMapper();		
		final String jsonText = tfr.getRestText();
		final JSONReader reader = new JSONReader( mapper, jsonText );
		registerConstatns( reader );
		
		mData = reader.get( PROJECT_DATA ).asObject( ProjectData.class );	
		
		tfr.close();
		
		onProjectReload();
	}
	
	public void save( String path ) throws IOException {
		setNowEditPath( path );
		final JSONWriter writer = new JSONWriter();
		try {
			writer.add( PROJECT_DATA, mData );			
		}
		catch ( JSONException e ) {
			JOptionPane.showMessageDialog( mFrame, e.getMessage() );
			return;
		}
		final ObjectMapper mapper = new ObjectMapper();
		final String jsonText = writer.build( mapper );
		
		if ( jsonText != null && jsonText.length() != 0 ) {
			final TextFileWriter tfr = new TextFileWriter( path, ProjectData.EXTENTION );
			tfr.open();
			tfr.write( jsonText );			
			tfr.close();
		}
		else {
			JOptionPane.showMessageDialog( mFrame, "データサイズが0バイトになるため保存を中止しました。\nデータの設定にエラーが存在する可能性があります。" );
		}
	}

	public void overrideWrite() throws IOException {
		save( mNowEditDataPath );
	}
	
	public ProjectData getProjectData() {
		return mData;
	}
	
	public void addProjectReloadListener( IProjectDataReloadListener listener ) {
		mListenerList.add( listener );
	}
	
	private void onProjectReload() {
		for ( IProjectDataReloadListener listener : mListenerList ) {
			listener.onReloadProject( mData );
		}
	}

}
