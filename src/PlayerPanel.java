import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.BoxLayout;
import java.awt.GridLayout;
import javax.swing.SwingConstants;

public class PlayerPanel extends JPanel implements ActionListener {
	Frame frame;
	JList playersList;
	JScrollPane scrollPane;
	JLabel lblTag;
	JPanel buttonPanel;
	JButton btnAddPlayer;
	JButton btnRemovePlayer;
	DefaultListModel playersModel;
	MatchPanel mPanel;
	PlayerSearchPanel psPanel;

	/**
	 * Create the panel.
	 */
	public PlayerPanel(Frame frame, MatchPanel mPanel, PlayerSearchPanel psPanel) {
		this.mPanel = mPanel;
		this.psPanel = psPanel;
		this.frame = frame;
		Connection con = frame.getConnection();
		Statement stmt;
		ResultSet rs;
		playersModel = new DefaultListModel();
		try {
			stmt = con.createStatement();
			String sql = "SELECT tag from players";
			rs = stmt.executeQuery(sql);
			while(rs.next()) {
				playersModel.addElement(rs.getString("tag"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		playersList = new JList(playersModel);
		
		setLayout(new BorderLayout(0, 0));

		scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setViewportView(playersList);

		lblTag = new JLabel("Tag");
		lblTag.setFont(new Font("Garamond", Font.PLAIN, 14));
		scrollPane.setColumnHeaderView(lblTag);

		buttonPanel = new JPanel();

		add(scrollPane, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.LINE_END);

		btnAddPlayer = new JButton("Add Player");
		btnAddPlayer.addActionListener(this);
		buttonPanel.setLayout(new GridLayout(8, 1));
		buttonPanel.add(btnAddPlayer);
		
		btnRemovePlayer = new JButton("Remove Player");
		btnRemovePlayer.addActionListener(this);
		buttonPanel.add(btnRemovePlayer);
	}

	void update() {
		psPanel.update();
		playersModel.removeAllElements();
		mPanel.clearPlayers();
		Connection con = frame.getConnection();
		Statement stmt;
		ResultSet rs;
		try {
			stmt = con.createStatement();
			String sql = "SELECT tag from players";
			rs = stmt.executeQuery(sql);
			while(rs.next()) {
				mPanel.addToPlayers(rs.getString("tag"));
				playersModel.addElement(rs.getString("tag"));
			}
			playersList.setModel(playersModel);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	DefaultListModel getPlayers() {
		return this.playersModel;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnAddPlayer) {
			AddPlayer ap = new AddPlayer(frame, this);
			ap.setVisible(true);
		}
		else if (e.getSource() == btnRemovePlayer) {
			if (playersList.getSelectedIndex() == -1) {
				JOptionPane.showMessageDialog(frame.getFrame(), "Must select player to remove", "Error", JOptionPane.WARNING_MESSAGE);
				return;
			}
			Connection con = frame.getConnection();
			Statement stmt = null;
			try {
				stmt = con.createStatement();
				String deleteSQL = "DELETE FROM players " + "WHERE tag = '" + playersModel.getElementAt(playersList.getSelectedIndex()) + "'";
				stmt.executeUpdate(deleteSQL);
				update();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}
}
