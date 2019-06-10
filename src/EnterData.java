

import javax.swing.JPanel;
import javax.swing.BoxLayout;
import java.awt.CardLayout;
import java.sql.Connection;

import javax.swing.JList;
import net.miginfocom.swing.MigLayout;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.ScrollPaneConstants;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class EnterData extends JPanel {

	Frame frame;
	JTabbedPane tabbedPane;

	PlayerPanel pPanel;
	MatchPanel mPanel;
  /**
   * Create the panel.
   */
  public EnterData(Frame frame, PlayerSearchPanel psPanel, TournamentPanel tPanel) {

	this.frame = frame;
  	setLayout(new BorderLayout(0, 0));
  	
  	mPanel = new MatchPanel(frame, tPanel);
  	pPanel = new PlayerPanel(frame, mPanel, psPanel);

  	
  	tabbedPane = new JTabbedPane(JTabbedPane.TOP);
    tabbedPane.addTab("Players", null, pPanel, "Enter player data");
  	tabbedPane.addTab("Matches", null, mPanel, "Enter match data");
    add(tabbedPane);

  }
}
