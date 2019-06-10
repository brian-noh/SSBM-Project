import javax.swing.JPanel;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.SpringLayout;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class MatchPanel extends JPanel implements ActionListener {

	Frame frame;

	/**
	 * Create the panel.
	 */
	DefaultComboBoxModel playersModel;
	DefaultComboBoxModel playersModel2;
	JComboBox player1;
	JComboBox player2;
	JLabel lblDate;
	JLabel lblWinner;
	JRadioButton rdbtnPlayer1;
	JRadioButton rdbtnPlayer2;
	ButtonGroup bg;
	JButton btnAddMatch;
	JButton btnRemoveMatch;
	private JTextField dateField;
	JTable matchTable;
	JScrollPane tablePane;
	Object[][] rowData;
	String[] columnNames = { "Date", "Player 1", "Player 2", "Winner" };
	Object[][] rowDataIDs;
	TournamentPanel tPanel;

	/**
	 * Create the panel.
	 */
	public MatchPanel(Frame frame, TournamentPanel tPanel) {
		this.tPanel = tPanel;
		this.setSize(new Dimension(800, 600));
		this.frame = frame;
		Connection con = frame.getConnection();
		Statement stmt;
		ResultSet rs;
		playersModel = new DefaultComboBoxModel();
		playersModel2 = new DefaultComboBoxModel();
		try {
			stmt = con.createStatement();
			String sql = "SELECT tag from players";
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				playersModel.addElement(rs.getString("tag"));
				playersModel2.addElement(rs.getString("tag"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		player1 = new JComboBox(playersModel);
		player2 = new JComboBox(playersModel2);
		player1.setPreferredSize(new Dimension(100, player1.getPreferredSize().height));
		player2.setPreferredSize(new Dimension(100, player2.getPreferredSize().height));
		SpringLayout springLayout = new SpringLayout();

		setLayout(springLayout);

		JLabel lblPlayer1 = new JLabel("Player 1");
		springLayout.putConstraint(SpringLayout.NORTH, lblPlayer1, 5, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, lblPlayer1, 5, SpringLayout.WEST, this);
		add(lblPlayer1);

		JLabel lblPlayer2 = new JLabel("Player 2");

		springLayout.putConstraint(SpringLayout.NORTH, player1, 15, SpringLayout.SOUTH, lblPlayer1);
		springLayout.putConstraint(SpringLayout.WEST, player1, 5, SpringLayout.WEST, this);
		add(player1);

		springLayout.putConstraint(SpringLayout.WEST, player2, 5, SpringLayout.EAST, player1);
		springLayout.putConstraint(SpringLayout.NORTH, player2, 0, SpringLayout.NORTH, player1);
		add(player2);

		springLayout.putConstraint(SpringLayout.SOUTH, lblPlayer2, -15, SpringLayout.NORTH, player2);
		springLayout.putConstraint(SpringLayout.WEST, lblPlayer2, 0, SpringLayout.WEST, player2);
		// springLayout.putConstraint(SpringLayout.WEST, lblPlayer2, 117,
		// SpringLayout.WEST, this);
		add(lblPlayer2);

		dateField = new JTextField();
		springLayout.putConstraint(SpringLayout.NORTH, dateField, 0, SpringLayout.NORTH, player1);
		springLayout.putConstraint(SpringLayout.WEST, dateField, 5, SpringLayout.EAST, player2);
		springLayout.putConstraint(SpringLayout.SOUTH, dateField, 0, SpringLayout.SOUTH, player2);
		add(dateField);
		dateField.setColumns(10);

		lblDate = new JLabel("Date (YYYY-MM-DD)");
		springLayout.putConstraint(SpringLayout.NORTH, lblDate, 0, SpringLayout.NORTH, lblPlayer1);
		springLayout.putConstraint(SpringLayout.WEST, lblDate, 0, SpringLayout.WEST, dateField);

		add(lblDate);

		rdbtnPlayer1 = new JRadioButton("Player 1");
		springLayout.putConstraint(SpringLayout.NORTH, rdbtnPlayer1, 0, SpringLayout.NORTH, dateField);
		springLayout.putConstraint(SpringLayout.WEST, rdbtnPlayer1, 5, SpringLayout.EAST, dateField);
		add(rdbtnPlayer1);

		rdbtnPlayer2 = new JRadioButton("Player 2");
		springLayout.putConstraint(SpringLayout.NORTH, rdbtnPlayer2, 0, SpringLayout.NORTH, dateField);
		springLayout.putConstraint(SpringLayout.WEST, rdbtnPlayer2, 3, SpringLayout.EAST, rdbtnPlayer1);
		add(rdbtnPlayer2);

		lblWinner = new JLabel("Winner");
		springLayout.putConstraint(SpringLayout.NORTH, lblWinner, 0, SpringLayout.NORTH, lblPlayer1);
		springLayout.putConstraint(SpringLayout.WEST, lblWinner, 53, SpringLayout.WEST, rdbtnPlayer1);
		add(lblWinner);

		bg = new ButtonGroup();
		bg.add(rdbtnPlayer1);
		bg.add(rdbtnPlayer2);

		btnRemoveMatch = new JButton("Remove Match");
		springLayout.putConstraint(SpringLayout.NORTH, btnRemoveMatch, 0, SpringLayout.NORTH, player1);
		springLayout.putConstraint(SpringLayout.SOUTH, btnRemoveMatch, 0, SpringLayout.SOUTH, player1);
		springLayout.putConstraint(SpringLayout.EAST, btnRemoveMatch, -5, SpringLayout.EAST, this);
		btnRemoveMatch.addActionListener(this);
		btnRemoveMatch.setPreferredSize(new Dimension(135, (int) btnRemoveMatch.getPreferredSize().getHeight()));
		add(btnRemoveMatch);

		
		btnAddMatch = new JButton("Add Match");
		springLayout.putConstraint(SpringLayout.NORTH, btnAddMatch, 0, SpringLayout.NORTH, player1);
		springLayout.putConstraint(SpringLayout.SOUTH, btnAddMatch, 0, SpringLayout.SOUTH, player1);
		springLayout.putConstraint(SpringLayout.WEST, btnAddMatch, 10, SpringLayout.EAST, rdbtnPlayer2);
		//springLayout.putConstraint(SpringLayout.EAST, btnAddMatch, -20, SpringLayout.WEST, btnRemoveMatch);
		btnAddMatch.addActionListener(this);
		btnAddMatch.setPreferredSize(new Dimension(135, (int) btnAddMatch.getPreferredSize().getHeight()));
		add(btnAddMatch);
		

		
		int numRows = 1;
		DefaultTableModel model = new DefaultTableModel(numRows, columnNames.length);
		model.setColumnIdentifiers(columnNames);
		matchTable = new JTable(model);
		update();
		tablePane = new JScrollPane();
		tablePane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		tablePane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		tablePane.setViewportView(matchTable);
		springLayout.putConstraint(SpringLayout.NORTH, tablePane, 10, SpringLayout.SOUTH, player1);
		springLayout.putConstraint(SpringLayout.SOUTH, tablePane, 0, SpringLayout.SOUTH, this);
		springLayout.putConstraint(SpringLayout.WEST, tablePane, 0, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.EAST, tablePane, 0, SpringLayout.EAST, this);
		add(tablePane);

		
		

	}

	public void clearPlayers() {
		this.playersModel.removeAllElements();
		this.playersModel2.removeAllElements();

	}

	public void addToPlayers(String s) {
		this.playersModel.addElement(s);
		this.playersModel2.addElement(s);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnAddMatch) {
			int player1_id = -1;
			int player2_id = -1;
			int winner_id = -1;
			String t_date;
			Connection con = frame.getConnection();
			Statement stmt;
			ResultSet rs;
			player1_id = frame.getId((String) player1.getSelectedItem());
			player2_id = frame.getId((String) player2.getSelectedItem());
			if (player1_id == -1 || player2_id == -1) {
				JOptionPane.showMessageDialog(frame.getFrame(),
						"Error getting player id: " + player1_id + ", " + player2_id, "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (player1_id == player2_id) {
				JOptionPane.showMessageDialog(frame.getFrame(),
						"Must select two different players", "Error",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			if (rdbtnPlayer1.isSelected()) {
				winner_id = player1_id;
			} else if (rdbtnPlayer2.isSelected()) {
				winner_id = player2_id;
			} else {
				JOptionPane.showMessageDialog(frame.getFrame(), "No winner selected", "Error",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			t_date = dateField.getText();
			Date utilDate = null;
			try {
				utilDate = new SimpleDateFormat("yyyy-MM-dd").parse(t_date);
				if (utilDate == null) {
					JOptionPane.showMessageDialog(frame.getFrame(), "Incorrect date format", "Error",
							JOptionPane.WARNING_MESSAGE);
					return;
				}
			} catch (ParseException ex) {
				JOptionPane.showMessageDialog(frame.getFrame(), "Incorrect date format", "Error",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
			try {
				stmt = con.createStatement();
				String selectSQL = "INSERT INTO matches (player1_id, player2_id, winner_id, t_date) " + "VALUES ("
						+ player1_id + ", " + player2_id + ", " + winner_id + ", " + "'" + t_date + "')";
				stmt.executeUpdate(selectSQL);
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			update();
		}
		else if (e.getSource() == btnRemoveMatch) {
			if (matchTable.getSelectedRow() == -1) {
				JOptionPane.showMessageDialog(frame.getFrame(), "Must select match to remove", "Error", JOptionPane.WARNING_MESSAGE);
				return;
			}
			else {
				Connection con = frame.getConnection();
				Statement stmt = null;
				try {
					stmt = con.createStatement();
					String deleteSQL = "DELETE FROM matches " + "WHERE match_id = " + rowDataIDs[matchTable.getSelectedRow()][0];
					stmt.executeUpdate(deleteSQL);
					update();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
		}
	}
	
	public void update() {
		tPanel.update();
		Connection con = frame.getConnection();
		Statement stmt;
		ResultSet rs;
		try {

			stmt = con.createStatement();
			String sql = "SELECT * from matches";
			rs = stmt.executeQuery(sql);
			
		    int size = 0;
	        rs.last();
	        size = rs.getRow();
	        rs.beforeFirst();
			Object[][] newRowData = new Object[size][];
			rowDataIDs = new Object[size][];
			int i = 0;
			while(rs.next()) {
				Date date = rs.getDate("t_date");
				String player1_tag = frame.getName(rs.getInt("player1_id"));
				String player2_tag = frame.getName(rs.getInt("player2_id"));
				String winner_tag = frame.getName(rs.getInt("winner_id"));
				newRowData[i] = new String[]{date.toString(), player1_tag, player2_tag, winner_tag};
				rowDataIDs[i] = new Object[]{rs.getInt("match_id"), date.toString(), player1_tag, player2_tag, winner_tag};
				i++;
			}
			rowData = newRowData;
			matchTable.setModel(new DefaultTableModel(rowData, columnNames));
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
}