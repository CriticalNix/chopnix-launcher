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
	private JPanel lowerpanel;
	private JButton btnFtb;
	


	public Online() {
		super();
		this.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setLayout(null);

		news = new JEditorPane();
		news.setEditable(false);
		plonkers = new JScrollPane(news);
		plonkers.setBounds(0, 0, 300, 312);
		plonkers.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		plonkers.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		this.add(plonkers);
		
		mojang = new JEditorPane();
		mojang.setEditable(false);
		mojangz = new JScrollPane(mojang);
		mojangz.setBounds(300, 11, 511, 155);
		mojangz.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		mojangz.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		this.add(mojangz);
		
		JEditorPane mojang = new JEditorPane();
		mojang.setBounds(300, 11, 501, 155);
		add(mojang);
		
		lowerpanel = new JPanel();
		lowerpanel.setBackground(SystemColor.controlDkShadow);
		lowerpanel.setBounds(300, 210, 511, 90);
		add(lowerpanel);
		
		btnHomePage = new JButton("Chopnix");
		lowerpanel.add(btnHomePage);
		btnHomePage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				 try {
			            Desktop.getDesktop().browse(new URI("http://chopnix.info/EsIpb/"));
			        } catch (IOException e) {
			            // TODO Auto-generated catch block
			            e.printStackTrace();
			        } catch (URISyntaxException e) {
			            // TODO Auto-generated catch block
			            e.printStackTrace();
			        }
			}});
		
		btnTechnicForum = new JButton("Technic");
		lowerpanel.add(btnTechnicForum);
		btnTechnicForum.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				 try {
			            Desktop.getDesktop().browse(new URI("http://www.technicpack.net/"));
			        } catch (IOException e) {
			            // TODO Auto-generated catch block
			            e.printStackTrace();
			        } catch (URISyntaxException e) {
			            // TODO Auto-generated catch block
			            e.printStackTrace();
			        }
			}});
		
		JButton btnMojang = new JButton("Mojang");
		lowerpanel.add(btnMojang);
		
		btnFtb = new JButton("FTB");
		btnFtb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				 try {
			            Desktop.getDesktop().browse(new URI("http://feed-the-beast.com"));
			        } catch (IOException e) {
			            // TODO Auto-generated catch block
			            e.printStackTrace();
			        } catch (URISyntaxException e) {
			            // TODO Auto-generated catch block
			            e.printStackTrace();
			        }
			}});
		
				lowerpanel.add(btnFtb);
		btnMojang.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				 try {
			            Desktop.getDesktop().browse(new URI("http://www.minecraft.com/"));
			        } catch (IOException e) {
			            // TODO Auto-generated catch block
			            e.printStackTrace();
			        } catch (URISyntaxException e) {
			            // TODO Auto-generated catch block
			            e.printStackTrace();
			        }
			}});
		
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
}