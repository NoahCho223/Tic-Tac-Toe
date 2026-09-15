package main;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class TicTacToe implements ActionListener {
	
	private final int WIDTH = 800;
	private final int HEIGHT = 800;
	
	//how large the square is
	private int dimension = 3;
	
	private Random random = new Random();
	private JFrame frame = new JFrame();
	JPanel titlePanel = new JPanel();
	JPanel buttonPanel = new JPanel();
	JLabel textfield = new JLabel();
	JButton[] buttons = new JButton[dimension*dimension];
	boolean player1_turn;
	
	String player1Symbol = "X";
	String player2Symbol = "O";
	
	public TicTacToe() {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(WIDTH, HEIGHT);
		frame.getContentPane().setBackground(new Color(50,50,50));
		frame.setLayout(new BorderLayout());
		frame.setTitle("Tic-Tac-Toe");
		frame.setVisible(true);
		
		textfield.setBackground(new Color(25,25,25));
		textfield.setForeground(new Color(25,255,0));
		textfield.setFont(new Font("Ink Free", Font.BOLD, 75));
		textfield.setHorizontalAlignment(JLabel.CENTER);
		textfield.setText("Tic-Tac-Toe");
		textfield.setOpaque(true);
		
		//button panel
		buttonPanel.setLayout(new GridLayout(dimension,dimension));
		buttonPanel.setBackground(new Color(150,150,150));
		
		//title panel
		titlePanel.setLayout(new BorderLayout());
		titlePanel.setBounds(0,0,WIDTH, 100);
		
		for(int i = 0; i<dimension*dimension; i++) {
			buttons[i] = new JButton();
			buttonPanel.add(buttons[i]);
			buttons[i].setFont(new Font("MV Boli", Font.BOLD, 120));
			buttons[i].setFocusable(false);
			buttons[i].addActionListener(this);
		}
		titlePanel.add(textfield);
		frame.add(titlePanel, BorderLayout.NORTH);
		frame.add(buttonPanel);
		
		firstTurn();
	}
	
	//activates each time a button is pressed
	public void actionPerformed(ActionEvent e) {
		for(int i=0 ; i<buttons.length; i++) {
			if(e.getSource() == buttons[i]) {
				if(player1_turn) {
					if(buttons[i].getText().isEmpty()) { //if empty square
						buttons[i].setForeground(Color.RED);
						buttons[i].setText(player1Symbol);
						player1_turn = false;
						textfield.setText(String.format("%s's turn", player2Symbol));
						check();
					}
				}
				else { //player 2's turn
					if(buttons[i].getText().isEmpty()) { 
						buttons[i].setForeground(Color.BLUE);
						buttons[i].setText(player2Symbol);
						player1_turn = true;
						textfield.setText(String.format("%s's turn", player1Symbol));
						check();
					}
				}
				break;
			}
		}
	}
	
	//determines who gets to move first in the game randomly
	public void firstTurn() {
		if(random.nextInt(2) == 0) {
			player1_turn = true;
			textfield.setText(String.format("%s turn", player1Symbol));
		}
		else {
			player1_turn = false;
			textfield.setText(String.format("%s turn", player2Symbol));
		}
	}
	
	//checks to see if there is a row matched
	public void check() {
		
		int[] winningLineCoordinates = new int[dimension];
		//check all horizontal matches
		for(int i = 0 ; i<dimension; i++) {
			String currentSymbol = "";
			boolean lineFound = true;
			
			for(int j=0; j<dimension && lineFound; j++) {
				
				int index = i*dimension+j;
				JButton currentButton = buttons[index];
				winningLineCoordinates[j] = index;
				if(j == 0) { //find out the first symbol of the line
					currentSymbol = currentButton.getText();
					
				}
				else {
					if(!currentButton.getText().equals(currentSymbol)) {
						lineFound = false;
						break;
					}
				}
			}
			if(lineFound && !currentSymbol.isEmpty()) {
				playerWins(winningLineCoordinates, currentSymbol.equals(player1Symbol) ? true: false);
				return;
			}
		}
		
		//check all vertical matches
		for(int i = 0 ; i<dimension; i++) {
			String currentSymbol = "";
			boolean lineFound = true;
			
			for(int j=0; j<dimension; j++) {
				
				int index = i+j*dimension;
				winningLineCoordinates[j] = index;
				JButton currentButton = buttons[index];
				if(j == 0) { //find out the first symbol of the line
					currentSymbol = currentButton.getText();
					
				}
				else {
					if(!currentButton.getText().equals(currentSymbol)) {
						lineFound = false;
						break;
					}
				}
			}
			if(lineFound && !currentSymbol.isEmpty()) {
				playerWins(winningLineCoordinates, currentSymbol.equals(player1Symbol) ? true: false);
				return;
			}
		}
		
		//check top-left to bottom-right
		String currentSymbol = "";
		boolean lineFound = true;
		
		for(int i = 0 ; i<dimension; i++) {
			
			int index = i+i*dimension;
			winningLineCoordinates[i] = index;
			JButton currentButton = buttons[index];
			if(i == 0) { //find out the first symbol of the line
				currentSymbol = currentButton.getText();

			}
			else {
				if(!currentButton.getText().equals(currentSymbol)) {
					lineFound = false;
					break;
				}
			}
		}
		
		if(lineFound && !currentSymbol.isEmpty()) {
			playerWins(winningLineCoordinates, currentSymbol.equals(player1Symbol));
			return;
		}
		
		//check top-right to bottom-left
		currentSymbol = "";
		lineFound = true;
		
		for(int i = dimension-1 ; i>=0; i--) {
			
			int index = i+(dimension-i-1)*dimension;
			winningLineCoordinates[i] = index;
			JButton currentButton = buttons[i+(dimension-i-1)*dimension];
			if(i == dimension-1) { //find out the first symbol of the line
				currentSymbol = currentButton.getText();

			}
			else {
				if(!currentButton.getText().equals(currentSymbol)) {
					lineFound = false;
					break;
				}
			}
		}
		
		if(lineFound && !currentSymbol.isEmpty()) {
			playerWins(winningLineCoordinates, currentSymbol.equals(player1Symbol));
			return;
		}
		
		//if there is no winner found, check if a draw occured
		drawDetection();
	}
	
	public void playerWins(int[] winningLineCoordinates, boolean player1Wins) {
		for(int i=0; i<winningLineCoordinates.length; i++) { //turn winning line green
			buttons[winningLineCoordinates[i]].setBackground(Color.green);
		}
		for(int i=0 ; i<dimension*dimension; i++) {
			buttons[i].setEnabled(false);
		}
		if(player1Wins) {
			textfield.setText(String.format("%s wins", player1Symbol));
		}
		else {
			textfield.setText(String.format("%s wins", player2Symbol));
		}
	}
	
	public void drawDetection() {
		boolean filledIn = true;
		for(int i=0 ; i<dimension*dimension; i++) {
			if(buttons[i].getText().isEmpty()){
				filledIn = false;
			}
		}
		if(filledIn) {
			for(int i=0 ; i<dimension*dimension; i++) {
				buttons[i].setEnabled(false);
			}
			textfield.setText("Draw!");
		}
	}
	
}
