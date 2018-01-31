package biz.uro.CSVDataConverter.swing.window.tab;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

@SuppressWarnings("serial")
public class DataModelEditTabbedPane extends JTabbedPane {

	private static final String CLOSE_ICON_PATH = "img/icon_close.png";
	
	private final ImageIcon mIcon;
	private final Dimension mButtonSize;
	private final HashSet<Component> mTabSet = new HashSet<>();
	
	public DataModelEditTabbedPane() {
		mIcon = new ImageIcon(CLOSE_ICON_PATH);
		mButtonSize = new Dimension(mIcon.getIconWidth(), mIcon.getIconHeight());
	}
	
	public void openByNewTab(DataModelEditTab<?> tab) {
		if (tab == null) {
			return;
		}
		if (mTabSet.contains(tab))
		{
			setSelectedComponent(tab);
			return;
		}
		addTab(tab.getName(), tab);
	}
	
	@Override 
	public void addTab(String title, Component content) {
		DataModelEditTab<?> tab = (DataModelEditTab<?>)content;
		JPanel tabPanel = new JPanel(new BorderLayout());
		tabPanel.setOpaque(false);
		JLabel icon = new JLabel(tab.getType().getIcon());
		icon.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 4));
		JLabel label = tab.getLabel();
		label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 2));
		JButton button = new JButton(mIcon);
		button.setPreferredSize(mButtonSize);
		button.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				removeTabAt(indexOfComponent(content));
				mTabSet.remove(content);
			}
	    });
		tabPanel.add(icon, BorderLayout.WEST);
		tabPanel.add(label,  BorderLayout.CENTER);
		tabPanel.add(button, BorderLayout.EAST);
		tabPanel.setBorder(BorderFactory.createEmptyBorder(2, 1, 1, 1));
		super.addTab(null, content);
		setSelectedComponent(content);
		mTabSet.add(content);
		setTabComponentAt(getTabCount() - 1, tabPanel);
	}
}
