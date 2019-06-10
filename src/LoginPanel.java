import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.border.Border;

import net.miginfocom.swing.MigLayout;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class LoginPanel extends JPanel {
	private JPasswordField passwordField;
	private JTextField usernameField;
	private Frame frame;
	Connection con;

	/**
	 * Create the panel.
	 */
	public LoginPanel(Frame frame) {
		this.frame = frame;
		setSize(800, 600);
		SpringLayout layout = new SpringLayout();
		setLayout(layout);;
		
		JPanel panel = new JPanel();
		layout.putConstraint(SpringLayout.NORTH, panel, 50, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.SOUTH, panel, -50, SpringLayout.SOUTH, this);
		layout.putConstraint(SpringLayout.EAST, panel, -200, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.WEST, panel, 200, SpringLayout.WEST, this);
		panel.setBackground(new Color(244, 244, 244, 80));
		panel.setBorder(BorderFactory.createLineBorder(Color.black));
		add(panel);
		
		SpringLayout sl_panel = new SpringLayout();
		panel.setLayout(sl_panel);
		

		
		usernameField = new HintTextField("Username");
		sl_panel.putConstraint(SpringLayout.WEST, usernameField, 75, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.EAST, usernameField, -75, SpringLayout.EAST, panel);
		sl_panel.putConstraint(SpringLayout.NORTH, usernameField, 200, SpringLayout.NORTH, panel);
		usernameField.setBorder(BorderFactory.createLineBorder(Color.white));

		panel.add(usernameField);
		
		passwordField = new HintPasswordField("Password");
		sl_panel.putConstraint(SpringLayout.WEST, passwordField, 75, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.EAST, passwordField, -75, SpringLayout.EAST, panel);
		sl_panel.putConstraint(SpringLayout.NORTH, passwordField, 10, SpringLayout.SOUTH, usernameField);
		passwordField.setBorder(BorderFactory.createLineBorder(Color.white));

		panel.add(passwordField);
		
		JButton loginBtn = new JButton("Login");
		loginBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			    try {
			        con = DriverManager
			        .getConnection("jdbc:mysql://localhost:3306/melee", usernameField.getText(), new String(passwordField.getPassword()));
			        frame.login(con);
					JOptionPane.showMessageDialog(frame.getFrame(), "Successfully connected to database server!", "Connected", JOptionPane.PLAIN_MESSAGE);
			      } catch (SQLException ex) {
			        JOptionPane.showMessageDialog(frame.getFrame(), "Unable to connect to server", "Error", JOptionPane.ERROR_MESSAGE);
			      }
			}
		});
		sl_panel.putConstraint(SpringLayout.WEST, loginBtn, 75, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.EAST, loginBtn, -75, SpringLayout.EAST, panel);
		sl_panel.putConstraint(SpringLayout.NORTH, loginBtn, 10, SpringLayout.SOUTH, passwordField);
		panel.add(loginBtn);
		
		//usernameField.setUI(new JTextFieldHintUI("Username", Color.gray));
		//passwordField.setUI(new JTextFieldHintUI("Password", Color.gray));

	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		String str = "/images/fdbg.PNG";
		Image bg;
		try {
			bg = ImageIO.read(new File(str));
			g.drawImage(bg, 0, 0, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
