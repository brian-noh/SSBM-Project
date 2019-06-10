import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.event.ActionEvent;

public class AddPlayer extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private Frame frame;

	/**
	 * Create the dialog.
	 */
	public AddPlayer(Frame frame, PlayerPanel pPanel) {
		this.frame = frame;
		setBounds(100, 100, 250, 125);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			JLabel lblPlayerTag = new JLabel("Player Tag:");
			contentPanel.add(lblPlayerTag);
		}
		{
			textField = new JTextField();
			contentPanel.add(textField);
			textField.setColumns(10);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (pPanel.getPlayers().contains(textField.getText())) {
							JOptionPane.showMessageDialog(frame.getFrame(), "Player already in database", "Error", JOptionPane.ERROR_MESSAGE);
							dispose();
						}
						else {
							Connection con = frame.getConnection();
							Statement stmt = null;
							try {
								stmt = con.createStatement();
								String selectSQL = "INSERT INTO players (tag) " + "VALUES ('" + textField.getText() + "')";
								dispose();
								stmt.executeUpdate(selectSQL);
								pPanel.update();
							} catch (SQLException ex) {
								ex.printStackTrace();
							}
						}
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
}
