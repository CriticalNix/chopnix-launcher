package com.chopnix.panes;

import java.awt.Desktop;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.Color;
import javax.swing.border.EtchedBorder;

public class Online extends JPanel implements ILauncherPane {
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private static JTextArea packInfo;

	private JEditorPane news;
	private JScrollPane plonkers;
	private JEditorPane mojang;
	private JScrollPane mojangz;
	private JButton btnHomePage;
	private JButton btnTechnicForum;
	private JButton btnFtb;

	public Online() {
		super();
		this.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setBackground(new Color(0, 0, 0, 30));
		setLayout(null);

		news = new JEditorPane();
		news.setEditable(false);
		plonkers = new JScrollPane(news);
		plonkers.setBounds(10, 11, 322, 238);
		plonkers.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		plonkers.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		plonkers.setBackground(new Color(0, 0, 0, 64));
		this.add(plonkers);

		mojang = new JEditorPane();
		mojang.setEditable(false);
		mojangz = new JScrollPane(mojang);
		mojangz.setBounds(446, 11, 365, 155);
		mojangz.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		mojangz.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		mojangz.setBackground(new Color(0, 0, 0, 64));
		this.add(mojangz);

		JEditorPane mojang = new JEditorPane();
		mojang.setBounds(446, 11, 355, 155);
		add(mojang);

		btnFtb = new JButton("FTB");
		btnFtb.setBounds(645, 238, 73, 64);
		add(btnFtb);
		btnFtb.setToolTipText("Opens a webpage to Feed The Beast webpage.");

		JButton btnMojang = new JButton("Mojang");
		btnMojang.setBounds(558, 238, 73, 64);
		add(btnMojang);
		btnMojang.setToolTipText("Opens a webpage to minecraft's website.");
		// panel.setBackground(Color.GRAY);

		btnHomePage = new JButton("Chopnix");
		btnHomePage.setBounds(475, 238, 73, 64);
		add(btnHomePage);
		btnHomePage.setToolTipText("Opens a webpage to Chopnix.info");

		btnTechnicForum = new JButton("Technic");
		btnTechnicForum.setBounds(728, 238, 73, 64);
		add(btnTechnicForum);
		btnTechnicForum
				.setToolTipText("Opens a webpage to TechnicPack webpage.");
		btnTechnicForum.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Desktop.getDesktop().browse(
							new URI("http://www.technicpack.net/"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnHomePage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Desktop.getDesktop().browse(
							new URI("http://chopnix.info/EsIpb/"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnMojang.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Desktop.getDesktop().browse(
							new URI("http://www.minecraft.com/"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnFtb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Desktop.getDesktop().browse(
							new URI("http://feed-the-beast.com"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}

	@Override
	public void onVisible() {
		try {
			news.setPage("http://31.22.1.42/cc1/lcc1.php");
			mojang.setPage("http://31.22.1.42/cc1/moj.php");
		} catch (IOException ioException) {
			System.out.println(ioException);
		}
	}

	@Override
	public void onVisible(String[] args) {
		// TODO Auto-generated method stub

	}
}