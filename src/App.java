
import java.io.*;
import java.net.*;
import java.util.ArrayList;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class App {
	public static Buret buret;
	public static MainFrame frame = new MainFrame (); 
	
    public static void main(String[] args){
		new Initialize(frame.ips, frame.names);
    } 
} 

