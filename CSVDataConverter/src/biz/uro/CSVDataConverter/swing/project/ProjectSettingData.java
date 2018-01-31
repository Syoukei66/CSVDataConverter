package biz.uro.CSVDataConverter.swing.project;

import javax.swing.JFrame;

import biz.uro.CSVDataConverter.swing.DataDictionaryKind;
import biz.uro.CSVDataConverter.swing.json.IJSONObject;
import biz.uro.CSVDataConverter.swing.json.JSONReader;
import biz.uro.CSVDataConverter.swing.json.JSONWriter;
import biz.uro.CSVDataConverter.swing.window.MainMenu;
import biz.uro.CSVDataConverter.swing.window.WindowDirector;

public class ProjectSettingData implements IJSONObject {

	public static class WindowTransform implements IJSONObject {

		private static final String X = "x";
		private static final String Y = "y";
		private static final String WIDTH = "width";
		private static final String HEIGHT = "height";
		
		private final int x, y, width, height;
		
		@Override
		public void jsonMapping( JSONWriter writer ) {
			writer.add( X, this.x );
			writer.add( Y, this.y );
			writer.add( WIDTH, this.width );
			writer.add( HEIGHT, this.height );			
		}

		public WindowTransform( JSONReader reader ) {
			this.x = reader.get( X ).asInt();
			this.y = reader.get( Y ).asInt();
			this.width = reader.get( WIDTH ).asInt();
			this.height = reader.get( HEIGHT ).asInt();
		}
		
		public WindowTransform( int x, int y, int width, int height ) {
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
		}
			
		public void apply( JFrame frame, JFrame offset ) {
			frame.setLocation( offset.getX() + this.x, offset.getY() + this.y );
			if ( this.width == 0 || this.height == 0 ) {
				return;
			}
			frame.setSize( this.width, this.height );			
		}
		
		@Override
		public String jsonHashCode() {
			return null;
		}
		
	}
	
	public static class WindowSetting implements IJSONObject {

		private static final String TRANSFORM = "Transform";
		
		private final WindowTransform transform;
		
		@Override
		public void jsonMapping( JSONWriter writer ) {
			writer.add( TRANSFORM, this.transform );
		}
		
		public WindowSetting( JSONReader reader ) {
			this.transform = reader.get( TRANSFORM ).asObject( WindowTransform.class );
		}
		
		public WindowSetting( JFrame frame, JFrame offset ) {
			final int localX = frame.getX() - offset.getX();
			final int localY = frame.getY() - offset.getY();
			this.transform = new WindowTransform( localX, localY, frame.getWidth(), frame.getHeight() );
		}
		
		public void apply( JFrame frame, JFrame offset ) {
			this.transform.apply( frame, offset );
		}

		@Override
		public String jsonHashCode() {
			return null;
		}
		
	}
	
	private static final String WINDOW = "Window";
	
	@Override
	public void jsonMapping( JSONWriter writer ) {
		final MainMenu mainMenu = WindowDirector.INSTANCE.getMainMenu();
		writer.add( WINDOW, new WindowSetting( mainMenu, mainMenu ) );
		for ( DataDictionaryKind kind : DataDictionaryKind.LIST ) {
			writer.add( kind.id + WINDOW, new WindowSetting( kind.window, mainMenu ) );
		}
	}

	public ProjectSettingData( JSONReader reader ) {
		final MainMenu mainMenu = WindowDirector.INSTANCE.getMainMenu();
		{
			final WindowSetting setting = reader.get( WINDOW ).asObject( WindowSetting.class );
			setting.apply( mainMenu, mainMenu );				
		}
		for ( DataDictionaryKind kind : DataDictionaryKind.LIST ) {
			final WindowSetting setting = reader.get( kind.id + WINDOW ).asObject( WindowSetting.class );
			setting.apply( kind.window, mainMenu );
		}		
	}
	
	public ProjectSettingData() {
		
	}
	
	@Override
	public String jsonHashCode() {
		return null;
	}

}
