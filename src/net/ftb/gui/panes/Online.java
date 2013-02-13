package net.ftb.gui.panes;

import java.io.IOException;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import ru.Repo;

public class Online extends JPanel implements ILauncherPane {
	private static final long serialVersionUID = 1L;

	private JEditorPane news;
	private JScrollPane online;

	public Online() {
		super();
		this.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setLayout(null);

		news = new JEditorPane();
		news.setEditable(false);
		online = new JScrollPane(news);
		online.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		online.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		online.setBounds(10, 10, 790, 290);
		this.add(online);
	}

	@Override
	public void onVisible() {
		try {
			news.setPage(Repo.ONLINE_FILE);
		} catch (IOException e1) { }
	}
}