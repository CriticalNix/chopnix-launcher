package ircclient.gui.windows;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import ircclient.gui.ChatArea;

public class IrcColorChooser extends JFrame implements ChangeListener {
    /**
	 * 
	 */
	private static final long serialVersionUID = -8517233160664259822L;
	private JColorChooser colorChooser = null;

    public IrcColorChooser() throws HeadlessException {
        initUI();
    }

    private void initUI() {
        //
        // Set title and default close operation of this JFrame.
        //
        setTitle("JColorChooser Demo");
       // setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        //
        // Creates an instance of JColorChooser component and
        // adds it to the frame's content.
        //
        colorChooser = new JColorChooser();
        getContentPane().add(colorChooser, BorderLayout.PAGE_END);

        //
        // Add a change listener to get the selected color in this
        // JColorChooser component.
        //
        colorChooser.getSelectionModel().addChangeListener(this);
        this.pack();
    }

    /**
     * Handles color selection in the JColorChooser component.
     *
     * @param e the ChangeEvent
     */
    public void stateChanged(ChangeEvent e) {
        //
        // Get the selected color in the JColorChooser component
        // and print the color in RGB format to the console.
        //
        Color colors = colorChooser.getColor();
        System.out.println("color = " + colors);
        ChatArea.setMessageColor(colors);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new IrcColorChooser().setVisible(true);
            }
        });
    }
}