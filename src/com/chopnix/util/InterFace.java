package com.chopnix.util;

import javax.swing.*;
import java.awt.*;

public class InterFace extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -156766354135168418L;
	JPanel mainPanel;
	JPanel left;
	JPanel right;
	JButton start;
	JButton pause;
	JLabel title;
	JLabel duration;
	JLabel WPM;
	JLabel errors;
	JTextArea toWrite;
	JTextArea input;

	public InterFace() {
		super("Typing Teacher");

		final Image textBg = Toolkit.getDefaultToolkit().createImage(
				"textBg.jpg");

		toWrite = new JTextArea() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 6315890334194329735L;

			{
				setOpaque(false);
			}

			public void paintComponent(Graphics g) {

				g.drawImage(textBg, 0, 0, 400, 400, this);
				super.paintComponent(g);
			}
		};

		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(toWrite);

	}

}
