package biz.uro.CSVDataConverter.swing.window;

import biz.uro.CSVDataConverter.swing.window.tab.element.ElementPropertyWindow;
import biz.uro.CSVDataConverter.swing.window.tab.elementType.ElementTypePropertyWindow;
import biz.uro.CSVDataConverter.swing.window.tab.factory.FactoryPropertyWindow;
import biz.uro.CSVDataConverter.swing.window.tab.factory.method.FactoryMethodPropertyWindow;
import biz.uro.CSVDataConverter.swing.window.tab.table.TablePropertyWindow;

public enum WindowDirector {
	INSTANCE;
	
	private MainMenu mMainMenu;
	private TablePropertyWindow mTablePropertyWindow;
	private ElementPropertyWindow mElementPropertyWindow;
	private FactoryMethodPropertyWindow mFactoryMethodPropertyWindow;
	private FactoryPropertyWindow mFactoryPropertyWindow;
	private ElementTypePropertyWindow mElementTypePropertyWindow;
	
	public void setMainMenu( MainMenu mainMenu ) {
		mMainMenu = mainMenu;
	}
	
	public MainMenu getMainMenu() {
		return mMainMenu;
	}
	
	public void setTablePropertyWindow( TablePropertyWindow window ) {
		mTablePropertyWindow = window;
	}

	public TablePropertyWindow getTablePropertyWindow() {
		return mTablePropertyWindow;
	}
	
	public void setElementPropertyWindow( ElementPropertyWindow window ) {
		mElementPropertyWindow = window;
	}
	
	public ElementPropertyWindow getElementPropertyWindow() {
		return mElementPropertyWindow;
	}
	
	public void setFactoryMethodPropertyWindow( FactoryMethodPropertyWindow window ) {
		mFactoryMethodPropertyWindow = window;
	}

	public FactoryMethodPropertyWindow getFactoryMethodPropertyWindow() {
		return mFactoryMethodPropertyWindow;
	}
	
	public void setFactoryPropertyWindow( FactoryPropertyWindow window ) {
		mFactoryPropertyWindow = window;
	}

	public FactoryPropertyWindow getFactoryPropertyWindow() {
		return mFactoryPropertyWindow;
	}

	public void setElementTypePropertyWindow( ElementTypePropertyWindow window ) {
		mElementTypePropertyWindow = window;
	}
	
	public ElementTypePropertyWindow getElementTypePropertyWindow() {
		return mElementTypePropertyWindow;
	}
	
}
