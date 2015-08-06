import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class MainFrame extends JFrame{
	public ArrayList <String> ips = new ArrayList<String>();
	public ArrayList <String> names = new ArrayList<String>();
    public JPanel ipPanel = new JPanel();
    public JPanel reagentPanel = new JPanel();
    public JPanel purgePanel = new JPanel();
    public JPanel movePanel = new JPanel();
    public JPanel syringePanel = new JPanel();
    public JPanel volumePanel = new JPanel();
    public JPanel percentPanel = new JPanel();
	
	public static JTextArea field = new JTextArea(10, 43);
	public static JScrollPane sp = new JScrollPane(field);
	
    private void disenablePanels (JPanel[] panels, boolean enable, int numPanels){
		for (int i = 0; i < numPanels; i++){
			int y = panels[i].getComponentCount();
			for (int j = 0; j < y; j++){
				panels[i].getComponent(j).setEnabled(enable);
			}
		}
    }
    
    public void refreshMainFrame(ArrayList<String> ips1, ArrayList<String> names1){
    	ips = ips1;
    	names = names1;
    	setVisible(true);
    	((JComboBox) (ipPanel.getComponent(1))).setModel(new DefaultComboBoxModel(names.toArray()));
    }
    
	public MainFrame (){
		this.ips = ips;
		this.names = names;
		
    	ipPanel = new JPanel();
    	ipPanel.setPreferredSize(new Dimension(550, 50));
    	JComboBox buretSelector = new JComboBox(names.toArray());
    	buretSelector.addActionListener(new ActionListener(){
    			public void actionPerformed(ActionEvent e){
    		    	try{
    			    	App.buret.socket.close();
    			    	App.buret.out.close();
    			    	App.buret.in.close();
    			    	App.buret.socket = null;
    			    	App.buret.out = null;
    			    	App.buret.in = null;
    		        	JOptionPane.showMessageDialog(null, "Disconnected from " + App.buret.name);
    		        	JPanel [] panels = {reagentPanel, purgePanel, movePanel, syringePanel, volumePanel, percentPanel}; 
    					disenablePanels(panels, false, 6);
    		    	}
    		    	catch(Exception f){
    		    	}
    			}
    	});
		JLabel ipLabel = new JLabel("Select a buret name: ");
		JButton connect = new JButton("Connect");
		JButton disconnect = new JButton("Disconnect");
		disconnect.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
		    	try{
			    	App.buret.socket.close();
			    	App.buret.out.close();
			    	App.buret.in.close();
			    	App.buret.socket = null;
			    	App.buret.out = null;
			    	App.buret.in = null;
		        	JOptionPane.showMessageDialog(null, "Disconnected successfully");
		        	JPanel [] panels = {reagentPanel, purgePanel, movePanel, syringePanel, volumePanel, percentPanel}; 
					disenablePanels(panels, false, 6);
		    	}
		    	catch(Exception f){
		        	JOptionPane.showMessageDialog(null, "You cannot disconnect if you have not yet connected");
		    	}
			}
		});
    	ipPanel.add(ipLabel);
    	ipPanel.add(buretSelector);
    	ipPanel.add(connect);
    	ipPanel.add(disconnect);

    	reagentPanel = new JPanel();
    	reagentPanel.setPreferredSize(new Dimension(550, 50));
    	JLabel reagentLabel = new JLabel("Enter a reagent name: ");
    	JTextField enterReagent = new JTextField(30);
    	reagentPanel.add(reagentLabel);
    	reagentPanel.add(enterReagent);
    	
    	purgePanel = new JPanel();
    	purgePanel.setPreferredSize(new Dimension(550, 30));
    	JLabel purgeLabel = new JLabel("Purge: ");
    	JTextField purgeTimes = new JTextField(3);
    	JLabel purgeLabel2 = new JLabel (" times");
    	JButton goPurge = new JButton("Start Purges");
    	goPurge.addActionListener(new ActionListener(){
    		public void actionPerformed(ActionEvent ae){
    			try{
					int num = Integer.parseInt(purgeTimes.getText());
					for (int x = 0; x < num; x ++){
						App.buret.purgeBuret();
					}
    			}
    			catch(NumberFormatException e){
		        	JOptionPane.showMessageDialog(null, "You must enter a number!");
    			}
    		}
    	});
    	purgePanel.add(purgeLabel);
    	purgePanel.add(purgeTimes);
    	purgePanel.add(purgeLabel2);
    	purgePanel.add(goPurge);
    	
    	movePanel = new JPanel();
    	movePanel.setPreferredSize(new Dimension(550, 50));
    	JButton goToTop = new JButton("Move Buret to the Top");
    	JButton goToBottom = new JButton("Move Buret to the Bottom");
    	goToTop.addActionListener(new ActionListener(){
    		public void actionPerformed(ActionEvent ae){
				App.buret.goToTop();
    		}
    	});
    	goToBottom.addActionListener(new ActionListener(){
    		public void actionPerformed(ActionEvent ae){
				App.buret.home();
    		}
    	});
    	movePanel.add(goToTop);
    	movePanel.add(goToBottom);
    	
    	syringePanel = new JPanel();
    	syringePanel.setPreferredSize(new Dimension(550, 30));
    	JLabel syringeLabel = new JLabel("Select a syringe size: ");
    	String [] syringeVolumes = {"", "5", "10", "25"};
    	JComboBox syringeSelect = new JComboBox(syringeVolumes);
    	syringeSelect.addActionListener(new ActionListener(){
    		public void actionPerformed(ActionEvent ae){
    			boolean enable = true;
    			try{
    				int num = Integer.parseInt((String) (syringeSelect.getSelectedItem()));
    				App.buret.setSyringe(num);
    			}
    			catch(NumberFormatException e){
    				enable = false;
    			}
    			JPanel [] panels = {volumePanel, percentPanel}; 
				disenablePanels(panels, enable, 2);
    		}
    	});
		connect.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				App.buret = new Buret ((String) ips.get(names.indexOf((String) buretSelector.getSelectedItem())), (String) buretSelector.getSelectedItem());
				JPanel [] panels = {reagentPanel, purgePanel, movePanel, syringePanel, volumePanel, percentPanel}; 
				disenablePanels(panels, true, 6);
    			boolean enable = true;
    			try{
    				int num = Integer.parseInt((String) (syringeSelect.getSelectedItem()));
    				App.buret.setSyringe(num);
    			}
    			catch(NumberFormatException ae){
    				enable = false;
    			}
    			JPanel [] panels1 = {volumePanel, percentPanel}; 
				disenablePanels(panels1, enable, 2);
			}
		});
    	syringePanel.add(syringeLabel);
    	syringePanel.add(syringeSelect);
    	
    	volumePanel = new JPanel();
    	volumePanel.setPreferredSize(new Dimension(550, 30));
    	JLabel volumeLabel = new JLabel("Enter a volume to dispense (mL): ");
    	JTextField volumeEnter = new JTextField(4);
    	JButton goVolume = new JButton("Dispense");
    	goVolume.addActionListener(new ActionListener(){
    		public void actionPerformed(ActionEvent ae){
    			try{
    				double num = Double.parseDouble(volumeEnter.getText());
    				App.buret.volume(num);
    			}
				catch(NumberFormatException e){
					e.printStackTrace(System.out);
		        	JOptionPane.showMessageDialog(null, "You must enter a number!");
				}
    		}
    	});
    	volumePanel.add(volumeLabel);
    	volumePanel.add(volumeEnter);
    	volumePanel.add(goVolume);
    	
    	percentPanel = new JPanel();
    	percentPanel.setPreferredSize(new Dimension(550, 50));
    	String [] percentNumbers = {"", "0.1", "1", "10", "25", "50"};
    	JComboBox percentSelect = new JComboBox(percentNumbers);
    	JLabel percentLabel = new JLabel("Enter a % of the buret to dispense: ");
    	JButton goPercent = new JButton("Dispense");
    	goPercent.addActionListener(new ActionListener(){
    		public void actionPerformed(ActionEvent ae){
				try{
					double per = Double.parseDouble((String)(percentSelect.getSelectedItem()));
					if (!App.buret.percent(per/100)){
			        	JOptionPane.showMessageDialog(null, "The buret cannot go that high!");
					}
				}
				catch(NumberFormatException e){
		        	JOptionPane.showMessageDialog(null, "You must select a number!");
				}
    		}
    	});
    	percentPanel.add(percentLabel);
    	percentPanel.add(percentSelect);
    	percentPanel.add(goPercent);
    	
		setLayout(new FlowLayout());
		getContentPane().add(ipPanel);
		getContentPane().add(reagentPanel);
		getContentPane().add(purgePanel);
		getContentPane().add(movePanel);
		getContentPane().add(syringePanel);
		getContentPane().add(volumePanel);
		getContentPane().add(percentPanel);
    	getContentPane().add(sp);
		setTitle("MANTECH Buret Communication");
		setSize(550,550);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable (false);
		addWindowListener(new WindowAdapter()
		{
		    public void windowClosing(WindowEvent e)
		    {
		    	try{
			    	App.buret.socket.close();
			    	App.buret.out.close();
			    	App.buret.in.close();
		    	}
		    	catch(Exception f){
		    		System.exit(0);
		    	}
		    }
		});
    	JPanel [] panels = {reagentPanel, purgePanel, movePanel, syringePanel, volumePanel, percentPanel}; 
		disenablePanels(panels, false, 6);
		JButton reinitialize = new JButton("Reinitialize IPs and Names");
		getContentPane().add(reinitialize);
		reinitialize.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				buretSelector.getActionListeners() [0].actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null)); 
				new Initialize(ips, names);
			}
		});
	}
}
