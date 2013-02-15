package com.chopnix.dialogs;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.chopnix.ru.Repo;


import net.ftb.gui.LaunchFrame;
import net.ftb.updater.UpdateChecker;

public class LauncherUpdateDialog extends JDialog {
	private static final long serialVersionUID = 1L;

	private JPanel panel = new JPanel();
	private JLabel textOne = new JLabel("Version " + UpdateChecker.verString + " of the launcher is available.");
	private JLabel textTwo = new JLabel("Do you wish to update?");
	private JButton changelog = new JButton("View Changelog");
	private JButton yesButton = new JButton("Yes");
	private JButton noButton = new JButton("No");

	public LauncherUpdateDialog(final UpdateChecker updateChecker) {
		super(LaunchFrame.getInstance(), true);

		setIconImage(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/home/home.png")));
		setTitle("Launcher Update Available");
		setBounds(300, 300, 300, 150);
		setResizable(false);
		setLocationRelativeTo(LaunchFrame.getInstance());

		panel.setLayout(null);
		panel.setBounds(0, 0, 300, 150);
		setContentPane(panel);

		textOne.setBounds(0, 0, 300, 30);
		textOne.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(textOne);

		textTwo.setBounds(0, 20, 300, 30);
		textTwo.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(textTwo);

		changelog.setBounds(65, 55, 170, 25);
		changelog.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					LaunchFrame.getInstance().hLink(new URI(Repo.CHANGELOG_FILE));
				} catch (URISyntaxException e) { }
			}
		});
		panel.add(changelog);

		yesButton.setBounds(65, 90, 80, 25);
		yesButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
				updateChecker.update();
			}
		});
		panel.add(yesButton);

		noButton.setBounds(155, 90, 80, 25);
		noButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
			}
		});
		panel.add(noButton);
	}
}