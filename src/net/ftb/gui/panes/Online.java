package net.ftb.gui.panes;


import java.awt.BorderLayout;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkEvent.EventType;
import javax.swing.event.HyperlinkListener;

import net.ftb.data.Settings;
import net.ftb.gui.LaunchFrame;
import net.ftb.log.Logger;
import net.ftb.util.OSUtils;
import net.ftb.util.OSUtils.OS;

@SuppressWarnings("serial")
public class Online extends JPanel implements ILauncherPane {
	private JEditorPane onlys;
	private JScrollPane Online;

	public Online() {
		super();
		if(OSUtils.getCurrentOS() == OS.WINDOWS) {
			setBorder(new EmptyBorder(-5, -25, -5, 12));
		} else {
			setBorder(new EmptyBorder(-4, -25, -4, -2));
		}
		setLayout(new BorderLayout());

		onlys = new JEditorPane();
		onlys.setEditable(false);
		onlys.addHyperlinkListener(new HyperlinkListener() {
			@Override
			public void hyperlinkUpdate(HyperlinkEvent arg0) {
				if(arg0.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
					try {
						OSUtils.browse(arg0.getURL().toString());
					} catch (Exception e) {
						Logger.logError(e.getMessage(), e);
					}
				}
			}
		});
		Online = new JScrollPane(onlys);
		Online.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		Online.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		add(Online, BorderLayout.CENTER);
	}

	@Override
	public void onVisible() {
		try {
			onlys.setPage("http://www.google.com");
			Settings.getSettings().setNewsDate();
			Settings.getSettings().save();
			//LaunchFrame.getInstance().setNewsIcon();
		} catch (IOException e1) {
			Logger.logError(e1.getMessage(), e1);
		}
	}
}