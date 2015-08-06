import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Initialize extends JFrame{
	public Initialize(ArrayList<String> ips1, ArrayList<String> names1){
		setLayout(new FlowLayout());
		setSize(680,400);
		
		JPanel titlePanel = new JPanel();
		JLabel title = new JLabel("Enter up to 10 ip addresses and associated buret names to use");
		JButton submitIPs = new JButton("Next");
		JLabel [] nameLabels = new JLabel [10]; 
		JLabel [] ipLabels = new JLabel [10]; 
		JTextField [] ipAddresses = new JTextField[10];
		JTextField [] names = new JTextField[10];
		
		titlePanel.setPreferredSize(new Dimension(680, 30));
		titlePanel.add(title);
		titlePanel.add(submitIPs);
		getContentPane().add(titlePanel);
		for (int x = 0; x < 10; x++){
			nameLabels [x] = new JLabel((x + 1) + ". Enter a name for the buret: ");
			ipLabels [x] = new JLabel("Associated I.P. Address: ");
			ipAddresses [x] = new JTextField(10);
			if (x < App.frame.ips.size()){
				ipAddresses [x].setText(App.frame.ips.get(x));
			}
			names[x] = new JTextField(12);
			if (x < App.frame.ips.size()){
				names [x].setText(App.frame.names.get(x));
			}
	    	getContentPane().add(nameLabels[x]);
	    	getContentPane().add(names[x]);
	    	getContentPane().add(ipLabels[x]);
	    	getContentPane().add(ipAddresses[x]);
		}
		setTitle("Initialization");
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable (false);

		submitIPs.addActionListener(new ActionListener()
		{
		    public void actionPerformed(ActionEvent e)
		    { 
		    	ArrayList <String> nameList = new ArrayList <String> ();
		    	ArrayList <String> ipList = new ArrayList <String> ();
		    	for (int x = 0; x < 10; x ++){
			    	if(!names[x].getText().equals("") && !ipAddresses[x].getText().equals("")){
				    	nameList.add(names[x].getText());
				    	ipList.add(ipAddresses[x].getText());
			    	}
		    	}
		    	if (!nameList.isEmpty()){
			    	dispose();
			    	App.frame.refreshMainFrame(ipList, nameList);
		    	}
		    	else{
		        	JOptionPane.showMessageDialog(null, "You must enter at least one name/ip combo!");
		    	}
		    }
		});
	}
}
