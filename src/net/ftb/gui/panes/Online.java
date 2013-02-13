package net.ftb.gui.panes;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import ru.Repo;

public class Online extends JPanel implements ILauncherPane {
	private static final long serialVersionUID = 1L;
	private static JTextArea packInfo;

	private JEditorPane news;
	private JScrollPane plonkers;

	public Online() {
		super();
		this.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setLayout(null);

		news = new JEditorPane();
		news.setEditable(false);
		plonkers = new JScrollPane(news);
		plonkers.setBounds(420, 0, 410, 200);
		plonkers.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		plonkers.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		plonkers.setBounds(10, 10, 790, 290);
		this.add(plonkers);
	}

	
	@Override
	public void onVisible() {
		try {
			news.setPage("http://31.22.1.42/cc1/lcc.php");
		} catch (IOException ioException) {
	        System.out.println(ioException);
		}
	}
}