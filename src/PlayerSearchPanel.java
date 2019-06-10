import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class PlayerSearchPanel extends JPanel {

	Frame frame;
	JList playersList;
	JLabel lblTag;
	JScrollPane scrollPane;
	JScrollPane matchSP;
	DefaultListModel playersModel;;
	DefaultTableModel matchesModel;
	String[] columnNames = { "Date", "Player 1", "Player 2", "Winner" };
	JTable matchTable;

	/**
	 * Create the panel.
	 */
	public PlayerSearchPanel(Frame frame) {
		setLayout(new GridLayout(1, 2));
		this.frame = frame;
		Connection con = frame.getConnection();
		Statement stmt;
		ResultSet rs;
		playersModel = new DefaultListModel();

		try {
			stmt = con.createStatement();
			String sql = "SELECT tag from players";
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				playersModel.addElement(rs.getString("tag"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		playersList = new JList(playersModel);

		scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setViewportView(playersList);
		
		matchesModel = new DefaultTableModel(1, columnNames.length);
		matchesModel.setColumnIdentifiers(columnNames);
		matchTable = new JTable(matchesModel);
		
		matchSP = new JScrollPane();
		matchSP.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		matchSP.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		matchSP.setViewportView(matchTable);

		add(scrollPane);
		add(matchSP);
		playersList.addListSelectionListener(new ListSelectionListener(){
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    JList list = (JList)e.getSource();
                    String item = (String)list.getSelectedValue();
                    updateTable(item);
                }
            }  
        });

	}

	void update() {
		playersModel.removeAllElements();
		Connection con = frame.getConnection();
		Statement stmt;
		ResultSet rs;
		try {
			stmt = con.createStatement();
			String sql = "SELECT tag from players";
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				playersModel.addElement(rs.getString("tag"));
			}
			playersList.setModel(playersModel);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	void updateTable(String name) {
		int id = frame.getId(name);

		Connection con = frame.getConnection();
		Statement stmt;
		ResultSet rs;
		try {
			stmt = con.createStatement();
			String sql = "SELECT * from matches WHERE player1_id = " + id + " OR player2_id = " + id;
			rs = stmt.executeQuery(sql);
			
			int size = 0;
		    rs.last();
		    size = rs.getRow();
		    rs.beforeFirst();
		    
		    String[][] rowData = new String[size][];
		    int i = 0;
			while(rs.next()) {
				Date date = rs.getDate("t_date");
				String player1_tag = frame.getName(rs.getInt("player1_id"));
				String player2_tag = frame.getName(rs.getInt("player2_id"));
				String winner_tag = frame.getName(rs.getInt("winner_id"));
				rowData[i] = new String[]{date.toString(), player1_tag, player2_tag, winner_tag};
				i++;
			}
			matchTable.setModel(new DefaultTableModel(rowData, columnNames));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
