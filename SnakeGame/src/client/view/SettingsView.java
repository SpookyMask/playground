package client.view;

import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import client.controller.ActionAdapter;
import client.model.Status;
import communication.Settings;

public class SettingsView extends JFrame {
	private static SettingsView settingsView = new SettingsView();
	private static Settings settings;
	private JTextField n,m,noborder,time,decr;
	
	public  static JButton start;
	
	private SettingsView(){
		settings = new Settings();
		
		ActionAdapter buttonListener = null;
	    buttonListener = new ActionAdapter() {
	    	public void actionPerformed(ActionEvent e) {
				if(e.getActionCommand().equals("start")) {
					Status.action = Status.Actions.START;
					System.out.println(Status.state + " " + Status.action);
				}
			}
	    };

		this.setTitle("SnakeGame>GameSettings");
		
	    JPanel mainPane = new JPanel(new GridLayout(5, 1));
	    
	    JPanel pane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	    pane.add(new JLabel("grid dimensions:"));
	    n = new JTextField(3); n.setEditable(true);
	    n.setText((new Integer(settings.sizeN)).toString());
	    pane.add(n);
	    m = new JTextField(3); m.setEditable(true);
	    m.setText((new Integer(settings.sizeM)).toString());
	    pane.add(m);    
	    mainPane.add(pane);
	    
	    pane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	    pane.add(new JLabel("no borders:"));
	    noborder = new JTextField(3); noborder.setEditable(true);
	    noborder.setText((new Boolean(settings.noBorder)).toString());
	    pane.add(noborder);
	    mainPane.add(pane);
	    
	    pane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	    pane.add(new JLabel("turn time:"));
	    time = new JTextField(3); time.setEditable(true);
	    time.setText((new Integer(settings.turnTime)).toString());
	    pane.add(time);
	    mainPane.add(pane);
	    
	    pane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	    pane.add(new JLabel("decrease multiplier:"));
	    decr = new JTextField(3); time.setEditable(true);
	    decr.setText((new Double(settings.decreaseTime)).toString());
	    pane.add(decr);
	    mainPane.add(pane);
	    
	    start = new JButton("Start Game");
	    start.setActionCommand("start");
	    start.addActionListener(buttonListener);
	    mainPane.add(start);
	    
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setContentPane(mainPane);
	    setLocation(200, 200);
	    pack();
	    setVisible(true);
	}
	
	public Settings getSettings() {
		settings.sizeN = Integer.parseInt(n.getText());
		settings.sizeM = Integer.parseInt(m.getText());
		settings.noBorder = Boolean.parseBoolean(noborder.getText());
		settings.turnTime = Integer.parseInt(time.getText());
		settings.decreaseTime = Double.parseDouble(decr.getText());
		return settings;
	}
		
		
	public static SettingsView getInstance() {
		return settingsView;
	}


	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		
	}
}
