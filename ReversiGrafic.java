import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.MenuBar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;


public class ReversiGrafic implements ActionListener{
	JMenuBar menuBar;
	JFrame frame = new JFrame();
	JMenu settingMenu,viewMenu,helpMenu;
	JMenuItem avlabelItem,exitItem,undoItem,redoItem,scoreItem,newGame;
	static JButton buttons [][] = new JButton[Reversi.row][Reversi.col];
	JPanel title_panel = new JPanel();
	JPanel button_panel = new JPanel();
	static JLabel text = new JLabel();
	boolean turn = true; // true - white turn , false - red turn
	boolean showAvilabel = true;
	boolean type = true;  // true - the game is against CPU , false - 2 player game
	
	Peaces undoBoard[][] = new Peaces[buttons.length][buttons[0].length];
	LinkedList<Peaces> undoWhitePeaces = new LinkedList<Peaces>();
	LinkedList<Peaces> undoRedPeaces = new LinkedList<Peaces>();
	
	Peaces redoBoard[][] = new Peaces[buttons.length][buttons[0].length];
	LinkedList<Peaces> redoWhitePeaces = new LinkedList<Peaces>();
	LinkedList<Peaces> redoRedPeaces = new LinkedList<Peaces>();
	
	public ReversiGrafic() {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // exit out of application
		frame.setSize(700,600); // sets the size
		frame.getContentPane().setBackground(new Color(75,75,75));
		frame.setLayout(new BorderLayout());
		
		title_panel.setBackground(new Color(20,20,20));
		text.setBackground(new Color(20,20,20));
		text.setForeground(new Color(25,250,0));
		text.setFont(new Font("INk",Font.BOLD,50));
		text.setHorizontalAlignment(JLabel.CENTER);
		text.setText(" Reversi");
		text.setOpaque(true);
		
		menuBar = new JMenuBar();
		settingMenu = new JMenu("Setting");
		viewMenu = new JMenu("View");
		menuBar.add(settingMenu);
		menuBar.add(viewMenu);
		
		avlabelItem = new JMenuItem("Hide avilabel places");
		exitItem = new JMenuItem("Exit");
		undoItem = new JMenuItem("Undo");
		redoItem = new JMenuItem("Redo");
		scoreItem = new JMenuItem("show cuurrent score");
		newGame = new JMenuItem("new Game");

		avlabelItem.addActionListener(this);
		exitItem.addActionListener(this);
		undoItem.addActionListener(this);
		undoItem.setBackground(Color.lightGray);
		redoItem.setBackground(Color.lightGray);
		scoreItem.addActionListener(this);
		newGame.addActionListener(this);

		settingMenu.setMnemonic(KeyEvent.VK_S); //ALT+ s for file
		
		settingMenu.add(undoItem);
		settingMenu.add(redoItem);
		settingMenu.add(newGame);
		settingMenu.add(exitItem);
		viewMenu.add(avlabelItem);
		viewMenu.add(scoreItem);

		button_panel.setLayout(new GridLayout(Reversi.row,Reversi.col));
		button_panel.setForeground(new Color(25,250,0));
		for(int i = 0; i< buttons.length; i++)
			for(int j =0; j<  buttons[0].length; j++)
			{
				buttons[i][j] = new JButton();
				buttons[i][j].setFont(new Font("INK pink",Font.BOLD,50));
				buttons[i][j].setForeground(Color.GREEN);
				buttons[i][j].setFocusable(false);
				buttons[i][j].addActionListener(this);
				buttons[i][j].setBackground(new Color( 51,  100 ,175 ));
				button_panel.add(buttons[i][j]);
			}
		int half = Reversi.row/2 -1;
		buttons[half][half].setBackground(Color.white);
		buttons[half+1][half+1].setBackground(Color.white);
		buttons[half][half+1].setBackground(Color.RED);
		buttons[half+1][half].setBackground(Color.RED);
		markAvilabels(turn);
		frame.add(button_panel);
		title_panel.add(text);
		
		
		
		frame.add(title_panel,BorderLayout.NORTH);
		frame.setJMenuBar(menuBar);
		frame.setVisible(true); // make frame visible constructor stub
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == newGame)
			newGame();
		if(e.getSource() == exitItem)
			System.exit(0);;
		if(e.getSource() == scoreItem)
			text.setText("White - " + Reversi.whitePeaces.size() + " " + "red - " + Reversi.redPeaces.size());
		if(e.getSource() == redoItem)
		{
			Reversi.whitePeaces = (LinkedList<Peaces>) redoWhitePeaces.clone();
			Reversi.redPeaces = (LinkedList<Peaces>) redoRedPeaces.clone();
			Reversi.copyBoard(Reversi.board, redoBoard);
			for(int i =0; i< buttons.length; i ++)
				for(int j =0; j< buttons[0].length; j ++)
				{
					if(redoBoard[i][j] != null)				
					{
						if(redoBoard[i][j].getColor())
							buttons[i][j].setBackground(Color.WHITE);
						else
							buttons[i][j].setBackground(Color.RED);
					}
					else
						buttons[i][j].setBackground(new Color( 51,  100 ,175 ));

				}
			markAvilabels(turn);
			redoItem.setBackground(Color.lightGray);
			redoItem.removeActionListener(this);
			undoItem.setBackground(exitItem.getBackground());

		}
		if(e.getSource() == undoItem)
		{
			Reversi.copyBoard(redoBoard, Reversi.board);
			redoWhitePeaces = (LinkedList<Peaces>) Reversi.whitePeaces.clone();
			redoRedPeaces = (LinkedList<Peaces>) Reversi.redPeaces.clone();
			Reversi.whitePeaces = (LinkedList<Peaces>) undoWhitePeaces.clone();
			Reversi.redPeaces = (LinkedList<Peaces>) undoRedPeaces.clone();
			Reversi.copyBoard(Reversi.board, undoBoard);
			for(int i =0; i< buttons.length; i ++)
				for(int j =0; j< buttons[0].length; j ++)
				{
					if(undoBoard[i][j] != null)				
					{
						if(undoBoard[i][j].getColor())
							buttons[i][j].setBackground(Color.WHITE);
						else
							buttons[i][j].setBackground(Color.RED);
					}
					else
						buttons[i][j].setBackground(new Color( 51,  100 ,175 ));

				}
			markAvilabels(turn);
			undoItem.setBackground(Color.lightGray);
			redoItem.addActionListener(this);
			redoItem.setBackground(exitItem.getBackground());

		}
		if(e.getSource() == avlabelItem)
		{			
					if(showAvilabel)
					{
						for(int i =0; i< buttons.length; i ++)
							for(int j =0; j< buttons[0].length; j ++)
								buttons[i][j].setText("");
						showAvilabel = false;
						avlabelItem.setText("Show avilabel places");
					}
					else
					{
						showAvilabel = true;
						markAvilabels(turn);
						avlabelItem.setText("Hide avilabel places");

					}
				
		}
		for(int i =0; i< buttons.length; i ++)
			for(int j =0; j< buttons[0].length; j ++)
			{
				buttons[i][j].setText("");
				if(e.getSource() == buttons[i][j] && Reversi.board[i][j] == null)
				{
					Reversi.copyBoard(undoBoard, Reversi.board);
					undoWhitePeaces = (LinkedList<Peaces>) Reversi.whitePeaces.clone();
					undoRedPeaces = (LinkedList<Peaces>) Reversi.redPeaces.clone();
					if(Reversi.putPeaces(new Peaces(i, j, turn),turn,Reversi.state.REAL))
					{
						undoItem.setBackground(exitItem.getBackground());
						redoItem.setBackground(Color.LIGHT_GRAY);

						turn= !turn;
						if(turn)		
							text.setText("This is white turn");
						else
							text.setText("This is Red turn");
						Reversi.isWinner(Reversi.state.REAL);
						if(type)
						{
							Reversi.putCpuPeace(turn);
							turn = !turn ;
							if(Reversi.isWinner(Reversi.state.REAL) == Reversi.state.NOONEWON)
								text.setText("This is your turn ");
							
						}
					}
				}
						
			}
//		for (int i = 0; i < buttons.length; i++) {
//			for (int j = 0; j < buttons.length; j++) {
//				if(Reversi.board[i][j] != null)
//					System.out.print(" " + Reversi.board[i][j].getColor());
//				else
//					System.out.print(" n ");
//			}
//			System.out.println();
//		}	
//		System.out.println(Reversi.whitePeaces);
//		System.out.println(Reversi.redPeaces);
//		System.out.println();
		if(Reversi.whitePeaces.size() + Reversi.redPeaces.size() !=  buttons.length * buttons[0].length)
			markAvilabels(turn);
		
	}

	

	private void markAvilabels(boolean turn) {
		int count = 0;
		for(int i = 0; i< buttons.length; i++)
			for(int j = 0; j < buttons[0].length;j++)
			{
				if(Reversi.board[i][j] == null && Reversi.putPeaces(new Peaces(i, j, turn),turn,Reversi.state.DEMO))
					{
						if(showAvilabel)
							buttons[i][j].setText("•");
						count++;
					}
			}
		if(count == 0)
		{
			String color;
			if(turn)
				color = "White";
			else
				color = "Red";
			if(Reversi.whitePeaces.size() > Reversi.redPeaces.size())
				text.setText("White won there is no avilabel place for the "+ color + " " + Reversi.whitePeaces.size() + " " + Reversi.redPeaces.size() );
			else if(Reversi.whitePeaces.size() < Reversi.redPeaces.size())
				text.setText("Red won there is no avilabel place for the " + color + " " +  Reversi.redPeaces.size() + "  " + Reversi.whitePeaces.size());
			else
				ReversiGrafic.text.setText("This is a Tie, there is no avilabel place for the " + color + Reversi.redPeaces.size() + " " + Reversi.whitePeaces.size());

		}
	}
	private void newGame() {
		turn = true;
		Reversi.redPeaces.clear();
		Reversi.whitePeaces.clear();
		redoRedPeaces.clear();
		redoWhitePeaces.clear();
		for(int i = 0; i< buttons.length; i++)
			for(int j = 0; j < buttons[0].length;j++)
			{
				Reversi.board[i][j] = null;
				buttons[i][j].setText("");
				buttons[i][j].setBackground(new Color( 51,  100 ,175 ));
			}
		int half = Reversi.row/2 -1;
		buttons[half][half].setBackground(Color.white);
		buttons[half+1][half+1].setBackground(Color.white);
		buttons[half][half+1].setBackground(Color.RED);
		buttons[half+1][half].setBackground(Color.RED);
		Reversi.board[half][half] = new Peaces(half, half, true); // true - white, false - red
		Reversi.whitePeaces.add(new Peaces(half, half, true));
		Reversi.board[half+1][half+1] = new Peaces(half+1, half+1, true); 
		Reversi.whitePeaces.add( new Peaces(half+1, half+1, true));	
		Reversi.board[half][half+1] = new Peaces(half, half+1, false); 
		Reversi.redPeaces.add( new Peaces(half, half+1, false));
		Reversi.board[half+1][half] = new Peaces(half+1, half, false);
		Reversi.redPeaces.add( new Peaces(half+1, half, false));
		markAvilabels(turn);
		text.setText("Reversi");
		
	}
}
