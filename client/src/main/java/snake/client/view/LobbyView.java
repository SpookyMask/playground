package snake.client.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import org.springframework.stereotype.Component;

import snake.client.controller.LobbyController;
import snake.client.model.comm.GameInfo;
import snake.client.model.comm.User;

@Component
public class LobbyView extends JFrame {
	private static final String[] columnNames = new String[]{"Hosts", "N", "M", "border", "turnMS", "decrMS"};
	
	private static LobbyView lobby = new LobbyView();
	
	private JLabel name, wins, losses;
	private JTable table;
	private JButton host, join;
	
	private User stats;
	private GameInfo[] hosts;
	
	private LobbyView() {
	    addWindowListener(new WindowAdapter() {
	        public void windowClosing(WindowEvent e) {
	            MenuView.activate();
	            lobby.setVisible(false);
	        }
	      });
		
		ActionAdapter buttonListener = null;
	    buttonListener = new ActionAdapter() {
	    	public void actionPerformed(ActionEvent e) {
				if(e.getActionCommand().equals("host")) {
					LobbyController.getInstance().onHostClick();
				} else if(e.getActionCommand().equals("join")) {
					LobbyController.getInstance().onJoinClick();
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
	    //DefaultTableModel model = new DefaultTableModel(data.length, 6);
	    table = new JTable(new DefaultTableModel(new String[][] {}, columnNames));
	    //table = new JTable(new String[][] {}, columnNames);
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

	    setTitle("Lobby");
	    mainPane.setOpaque(true); //content panes must be opaque
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setContentPane(mainPane);
	    setSize(500, 300);
	    setLocation(200, 200);
	    setVisible(false);
	}
	
	public static LobbyView getInstance() {
		return lobby;
	}
	
	//returns username
	public String getSelectedHost() {

		if(table.getSelectedRow() != -1)
			return hosts[table.getSelectedRow()].hostName;

		return null;
	}
	
	public void setStats(User s) {
		stats = s;
		name.setText(stats.name);
		wins.setText(Integer.toString(stats.wins));
		losses.setText(Integer.toString(stats.losses));
		repaint();
	}

	public void setHosts(GameInfo[] hosts) {
		this.hosts = hosts;
		//String[][] data = new String[hosts.length][6];
		Vector<Vector<String>> data = new Vector<>();
		for(GameInfo h: hosts) {
			Vector<String> row = new Vector<>(6);
			row.add(h.hostName);
			row.add(new Integer(h.sizeN).toString());
            row.add(new Integer(h.sizeM).toString());
            row.add(new Boolean(h.noBorder).toString());
            row.add(new Integer(h.turnTimeMS).toString());
            row.add(new Integer(h.decreaseTimeMS).toString());
            data.add(row);
		}
		
//		for(GameInfo h: hosts)
//			data[i++] = new String[] {
//					h.hostName,
//					(new Integer(h.sizeN)).toString(),
//					(new Integer(h.sizeM)).toString(),
//					(new Boolean(h.noBorder)).toString(),
//					(new Integer(h.turnTimeMS)).toString(),
//					(new Integer(h.decreaseTimeMS)).toString()
//			};
		
//		DefaultTableModel model = new DefaultTableModel(data.length, 6);
//		model.setDataVector(data, columnNames);
//		table.setModel(model);
		
//	    Vector<String> rowOne = new Vector<String>();
//	    rowOne.addElement("Row1-Column1");
//	    rowOne.addElement("Row1-Column2");
//	    rowOne.addElement("Row1-Column3");
//	    
//	    Vector<String> rowTwo = new Vector<String>();
//	    rowTwo.addElement("Row2-Column1");
//	    rowTwo.addElement("Row2-Column2");
//	    rowTwo.addElement("Row2-Column3");
//	    
//	    Vector<Vector<String>> v = new Vector<>();
	    DefaultTableModel model = (DefaultTableModel) table.getModel();
		model.getDataVector().removeAllElements();
		model.getDataVector().addAll(data);
		model.fireTableDataChanged();
		
	    //model.fireTableDataChanged();
	    

        //repaint();
	}
	
	public static LobbyView activate() {
		if(lobby == null) lobby = new LobbyView();
		lobby.setVisible(true);
		return lobby;
	}	

}
