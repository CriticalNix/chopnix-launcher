package net.ftb.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import com.chopnix.log.Logger;
import com.chopnix.panes.OptionsPane;

public class ChooseDir extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;

	private OptionsPane optionsPane;
	private String choosertitle = "Please select an install location";

	public ChooseDir(OptionsPane optionsPane) {
		super();
		this.optionsPane = optionsPane;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JFileChooser chooser = new JFileChooser();
		if (optionsPane != null) {
			chooser.setCurrentDirectory(new java.io.File(optionsPane
					.getInstallFolderText()));
			chooser.setDialogTitle(choosertitle);
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			chooser.setAcceptAllFileFilterUsed(false);

			if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
				Logger.logInfo("getCurrentDirectory(): "
						+ chooser.getCurrentDirectory());
				Logger.logInfo("getSelectedFile() : "
						+ chooser.getSelectedFile());
				optionsPane.setInstallFolderText(chooser.getSelectedFile()
						.getPath());
			} else {
				Logger.logInfo("No Selection ");
			}
		}
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(200, 200);
	}
}