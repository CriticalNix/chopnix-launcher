package com.chopnix.dialogs;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.chopnix.panes.ModpacksPane;

import net.ftb.gui.LaunchFrame;

public class FilterDialog extends JDialog {
	private static final long serialVersionUID = -7355234763252916809L;
	private JPanel panel = new JPanel();
	private JLabel typeLbl = new JLabel("Mod Pack Type:"),
			originLbl = new JLabel("Mod Pack Origin:");
	private JComboBox<Object> typeBox = new JComboBox<Object>(new String[] {
			"Client", "Server" }), originBox = new JComboBox<Object>(
			new String[] { "All", "ChopNix", "FTB", "3rd Party" });
	private JButton applyButton = new JButton("Apply Filter"),
			cancelButton = new JButton("Cancel"), searchButton = new JButton(
					"Search Packs");

	public FilterDialog(final ModpacksPane instance) {
		super(LaunchFrame.getInstance(), true);
		setupGui();
		typeBox.setSelectedItem(ModpacksPane.type);
		originBox.setSelectedItem(ModpacksPane.origin);
		applyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String type = "", origin = "";
				switch (typeBox.getSelectedIndex()) {
				case 0:
					type = "Client";
					break;
				case 1:
					type = "Server";
					break;
				}
				switch (originBox.getSelectedIndex()) {
				case 0:
					origin = "All";
					break;
				case 1:
					origin = "ChopNix";
					break;
				case 2:
					origin = "FTB";
					break;
				case 3:
					origin = "3rd Party";
					break;
				}
				ModpacksPane.type = type;
				ModpacksPane.origin = origin;
				ModpacksPane.updateFilter();
				setVisible(false);
			}
		});
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		searchButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				SearchDialog sd = new SearchDialog(instance);
				sd.setVisible(true);
			}
		});
	}

	private void setupGui() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				this.getClass().getResource("/home/home.png")));
		setTitle("Filter");
		setBounds(300, 300, 230, 175);
		setResizable(false);
		setLocationRelativeTo(LaunchFrame.getInstance());
		panel.setBounds(0, 0, 230, 175);
		panel.setLayout(null);
		setContentPane(panel);
		typeLbl.setBounds(10, 10, 100, 30);
		typeBox.setBounds(120, 10, 100, 30);
		originLbl.setBounds(10, 40, 100, 30);
		originBox.setBounds(120, 40, 100, 30);
		applyButton.setBounds(10, 80, 100, 25);
		searchButton.setBounds(10, 105, 210, 25);
		getRootPane().setDefaultButton(applyButton);
		cancelButton.setBounds(120, 80, 100, 25);
		panel.add(typeLbl);
		panel.add(typeBox);
		panel.add(originLbl);
		panel.add(originBox);
		panel.add(applyButton);
		panel.add(cancelButton);
		panel.add(searchButton);
	}
}
