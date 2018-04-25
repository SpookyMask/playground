package client.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import client.controller.ActionAdapter;
import client.model.Status;
import communication.Hosts;
import communication.Stats;

public class LobbyView extends JFrame {
	private static final LobbyView view = new LobbyView();
	
	private JLabel name, wins, losses;
	private JTable table;
	private JButton host, join;
	
	private Stats stats = new Stats();
	private Hosts hosts = new Hosts();
	
	private LobbyView() {
	    addWindowListener(new WindowAdapter() {
	    	//ToDo
	        public void windowClosing(WindowEvent e) {
	            MenuView v = MenuView.getInstance();
	            Status.state = Status.States.ATMENU;
	            Status.conrState = Status.ControllerStates.TOMENU;
	            view.setVisible(false);
	            v.setVisible(true);
	        }
	      });
		
		ActionAdapter buttonListener = null;
	    buttonListener = new ActionAdapter() {
	    	public void actionPerformed(ActionEvent e) {
				if(e.getActionCommand().equals("host")) {
					Status.action = Status.Actions.HOST;
					System.out.println(Status.state + " " + Status.action);
				} else if(e.getActionCommand().equals("join")) {
					Status.action = Status.Actions.JOIN;
					System.out.println(Status.state + " " + Status.action);
				}
			}
	    };
	    
		JPanel mainPane = new JPanel(new BorderLayout());
		JPanel statsPane = new JPanel(new GridLayout(3, 1));
		JScrollPane scrollPane;
		JPanel buttonsPane = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JPanel pane;
		
	    name = new JLabel(stats.name == null? "Incogito" : stats.name);
	    statsPane.add(name);
	    
	    pane = new JPanel(new FlowLayout(FlowLayout.LEFT));
	    pane.add(new JLabel("Wins:"));
	    wins = new JLabel(Integer.toString(stats.wins));
	    pane.add(wins);
	    statsPane.add(pane);
	    
	    pane = new JPanel(new FlowLayout(FlowLayout.LEFT));
	    pane.add(new JLabel("Losses:"));
	    losses = new JLabel(Integer.toString(stats.losses));
	    pane.add(losses);
	    statsPane.add(pane);
	    
	    mainPane.add(statsPane, BorderLayout.PAGE_START);
	    
	    pane = new JPanel();
	    String[] columnNames = {"Hosts"};
	    table = new JTable(hosts.data, columnNames);
	    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    table.setPreferredScrollableViewportSize(new Dimension(100, 70));
	    table.setFillsViewportHeight(true);
	    scrollPane = new JScrollPane(table);
	    mainPane.add(scrollPane, BorderLayout.CENTER);
	    
	    host = new JButton("Host");
	    host.setActionCommand("host");
	    host.addActionListener(buttonListener);
	    join = new JButton("Join");
	    join.setActionCommand("join");
	    join.addActionListener(buttonListener);
	    buttonsPane.add(host);
	    buttonsPane.add(join);
	    mainPane.add(buttonsPane, BorderLayout.PAGE_END);

	    setTitle("Lby");
	    mainPane.setOpaque(true); //content panes must be opaque
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setContentPane(mainPane);
	    setLocation(200, 200);
	    pack();
	    setVisible(true);
	}
	
	public static LobbyView getInstance() {
		return view;
	}
	
	public String getSelectedHost() {
		return hosts.data[table.getSelectedRow()][0];
	}
	
	public void setStats(Stats s) {
		stats = s;
		name.setText(stats.name);
		wins.setText(Integer.toString(stats.wins));
		losses.setText(Integer.toString(stats.losses));
		repaint();
	}
	
	public void setHosts(Hosts h) {
		hosts = h;
		DefaultTableModel model = new DefaultTableModel();
        table.setModel(model);
        model.setDataVector(hosts.data, new String[]{"Hosts"});
		repaint();
	}

}
