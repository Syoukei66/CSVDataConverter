package biz.uro.CSVDataConverter.swing.util;

import javax.swing.text.JTextComponent;

public enum ValidationUtil {
	INSTANCE;
	
	public boolean varidationJText( JTextComponent component ) {
		final String text = component.getText();
		return text == null || text.length() == 0;
	}

}
