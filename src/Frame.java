import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;

import java.awt.GridLayout;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.BorderLayout;

public class Frame {

	private JFrame frame;
	private JTabbedPane tabbedPane;
	private EnterData edPanel;
	private SearchData sdPanel;
	private LoginPanel lPanel;
	private Connection con;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Frame window = new Frame();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Frame() {
		initialize();
	}

	public JFrame getFrame() {
		return this.frame;
	}

	public Connection getConnection() {
		return this.con;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 800, 600);
		frame.setResizable(false);
		Connection con;

		lPanel = new LoginPanel(this);

		frame.getContentPane().setLayout(new BorderLayout(0, 0));

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(lPanel, BorderLayout.CENTER);

	}

	public void login(Connection con) {
		this.con = con;

		String edTooltip = "Enter tournament data into the database";
		String sdTooltip = "Search data about players or tournaments";
		PlayerSearchPanel psPanel = new PlayerSearchPanel(this);
		TournamentPanel tPanel = new TournamentPanel(this);
		edPanel = new EnterData(this, psPanel, tPanel);
		sdPanel = new SearchData(this, psPanel, tPanel);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.addTab("Enter Data", new ImageIcon(Frame.class.getResource("/images/enter-data-icon.png")), edPanel,
				edTooltip);
		tabbedPane.addTab("Search Data", new ImageIcon(Frame.class.getResource("/images/search-data-icon.png")), sdPanel,
				sdTooltip);
		
		lPanel.setVisible(false);
		frame.getContentPane().add(tabbedPane);
	}
	public String getName(int id) {
		Statement stmt;
		ResultSet rs;
		try {
			stmt = con.createStatement();
			String sql = "SELECT * from players";
			rs = stmt.executeQuery(sql);		
			while(rs.next()) {
				if (rs.getInt("player_id") == id) {
					return rs.getString("tag");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public int getId(String name) {
		Statement stmt;
		ResultSet rs;
		try {
			stmt = con.createStatement();
			String sql = "SELECT * from players";
			rs = stmt.executeQuery(sql);		
			while(rs.next()) {
				if (rs.getString("tag").equals(name)) {
					return rs.getInt("player_id");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}
}
