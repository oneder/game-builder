package oneder.terminal.main;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class TMain extends JFrame{
	
	private static final long serialVersionUID = 4129706201269283324L;
	
	private static final String title = "Terminal: A Web-Crossed Love Story";
	private static final String version = "0.0.4";
	public static final int WIDTH = 800, HEIGHT = 608;
	
	private GameWindow window;
	
	public TMain(){
		setTitle(title + " - V" + version);
		System.out.println(title + " - V" + version);
		System.out.println("\tGame Dimensions: " + WIDTH + "x" + HEIGHT);
		
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setMaximumSize(new Dimension(WIDTH, HEIGHT));
		setMinimumSize(new Dimension(WIDTH, HEIGHT));
		
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		window = new GameWindow();
		add(window);
		
		setVisible(true);
		window.Start();
	}
	
	public static void main(String[] args){
		TMain game = new TMain();
	}
	
}
