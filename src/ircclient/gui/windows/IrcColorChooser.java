package ircclient.gui.windows;



import ircclient.gui.ChatArea;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class IrcColorChooser extends JPanel implements ActionListener{
	public IrcColorChooser() {
	}

  /**
	 * 
	 */
	private static final long serialVersionUID = -1953230850308806445L;

public static void main(String[] a) {

    final JColorChooser colorChooser = new JColorChooser();
    final JLabel previewLabel = new JLabel("Chopnix", JLabel.CENTER);
    previewLabel.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 38));
    previewLabel.setSize(previewLabel.getPreferredSize());
    previewLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 1, 0));
    colorChooser.setPreviewPanel(previewLabel);

    ActionListener okActionListener = new ActionListener() {
      public void actionPerformed(ActionEvent actionEvent) {
        System.out.println("OK Button");
        System.out.println(colorChooser.getColor());
      }
    };

    ActionListener cancelActionListener = new ActionListener() {
      public void actionPerformed(ActionEvent actionEvent) {
        System.out.println("Cancel Button");
      }
    };

    final JDialog dialog = JColorChooser.createDialog(null, "Change Color", true,
        colorChooser, okActionListener, cancelActionListener);

    dialog.setVisible(true);
  }

@Override
public void actionPerformed(ActionEvent arg0) {
	// TODO Auto-generated method stub
	
}
}
           
  