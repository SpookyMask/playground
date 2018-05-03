package snake.client.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;

import snake.client.Application;
import snake.client.model.comm.Host;
import snake.client.model.comm.HostsView;
import snake.client.model.comm.Settings;
import snake.client.model.comm.Stats;

@Component
public class LobbyView extends JFrame {
	private static LobbyView lobby = new LobbyView();
	
	private JLabel name, wins, losses;
	private JTable table;
	private JButton host, join;
	
	private Stats stats;
	private String[][] hosts;
	
	private void initGUI() {
		
	}
	
	private LobbyView() {
	    addWindowListener(new WindowAdapter() {
	        public void windowClosing(WindowEvent e) {
	            MenuView v = MenuView.activate();
	            lobby.setVisible(false);
	        }
	      });
		
		ActionAdapter buttonListener = null;
	    buttonListener = new ActionAdapter() {
	    	public void actionPerformed(ActionEvent e) {
				if(e.getActionCommand().equals("host")) {
					HttpEntity<Settings> request = new HttpEntity<>(new Settings());
					Settings foo = Application.restTemplate.postForObject("localhost:8080/host?name="+Application.name, request, Settings.class);
				} else if(e.getActionCommand().equals("join")) {
				}
			}
	    };
	    
		JPanel mainPane = new JPanel(new BorderLayout());
		JPanel statsPane = new JPanel(new GridLayout(3, 1));
		JScrollPane scrollPane;
		JPanel buttonsPane = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JPanel pane;
		
	    name = new JLabel();
	    statsPane.add(name);
	    
	    pane = new JPanel(new FlowLayout(FlowLayout.LEFT));
	    pane.add(new JLabel("Wins:"));
	    wins = new JLabel();
	    pane.add(wins);
	    statsPane.add(pane);
	    
	    pane = new JPanel(new FlowLayout(FlowLayout.LEFT));
	    pane.add(new JLabel("Losses:"));
	    losses = new JLabel();
	    pane.add(losses);
	    statsPane.add(pane);
	    
	    mainPane.add(statsPane, BorderLayout.PAGE_START);
	    
	    pane = new JPanel();
	    String[] columnNames = {"Hosts"};
	    table = new JTable(new String[][] {}, columnNames);
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
	    setVisible(false);
	}
	
	public static LobbyView getInstance() {
		return lobby;
	}
	
	//returns username
	public String getSelectedHost() {
		return hosts[table.getSelectedRow()][0];
	}
	
	public void setStats(Stats s) {
		stats = s;
		name.setText(stats.name);
		wins.setText(Integer.toString(stats.wins));
		losses.setText(Integer.toString(stats.losses));
		repaint();
	}

	public void setHosts(Host[] hosts) {
		String[][] data = new String[hosts.length][];
		int i=0;
		for(Host h: hosts)
			data[i++] = new String[] {
					(new Integer(h.settings.sizeN)).toString(),
					h.name
			};
		DefaultTableModel model = new DefaultTableModel();
        table.setModel(model);
        model.setDataVector(data, new String[]{"Hosts"});
		repaint();
	}
	
	public static LobbyView activate(Stats stats, Host[] hosts) {
		if(lobby == null) lobby = new LobbyView();
		lobby.setStats(stats);
		lobby.setHosts(hosts);
		lobby.setVisible(true);
		return lobby;
	}	

}
