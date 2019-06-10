
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;

import java.awt.CardLayout;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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

public class SearchData extends JPanel {

	Frame frame;
	JTabbedPane tabbedPane;

	PlayerSearchPanel pPanel;
	TournamentPanel tPanel;

  /**
   * Create the panel.
   */
  public SearchData(Frame frame, PlayerSearchPanel pPanel, TournamentPanel tPanel) {
	this.pPanel = pPanel;
	this.tPanel = tPanel;
	this.frame = frame;
  	setLayout(new BorderLayout(0, 0));
  	
  	
  	
  	tabbedPane = new JTabbedPane(JTabbedPane.TOP);
    tabbedPane.addTab("Players", null, pPanel, "Serach player data");
  	tabbedPane.addTab("Tournaments", null, tPanel, "Search tournament data");
    add(tabbedPane);

  }

}
