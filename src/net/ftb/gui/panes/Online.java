package net.ftb.gui.panes;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import ru.Repo;
import java.awt.Color;
import java.awt.Desktop;

import javax.swing.JInternalFrame;
import javax.swing.UIManager;
import java.awt.SystemColor;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Online extends JPanel implements ILauncherPane {
	private static final long serialVersionUID = 1L;
	private static JTextArea packInfo;

	private JEditorPane news;
	private JScrollPane plonkers;
	private JEditorPane mojang;
	private JScrollPane mojangz;
	private JButton btnHomePage;
	private JButton btnTechnicForum;
	


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
		
		JPanel lowerHolder = new JPanel();
		lowerHolder.setBackground(SystemColor.textInactiveText);
		lowerHolder.setBounds(300, 166, 511, 42);
		add(lowerHolder);
		
		btnHomePage = new JButton("Chopnix");
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
	
	
		lowerHolder.add(btnHomePage);
		
		btnTechnicForum = new JButton("Technic");
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
		lowerHolder.add(btnTechnicForum);
		
		JButton btnMojang = new JButton("Mojang");
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
		lowerHolder.add(btnMojang);
		
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