import java.awt.Color;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;


public class Reversi {
	final static int row = 8;
	final static int col = 8;
	static Peaces board[][] = new Peaces[row][col];
	static LinkedList<Peaces> whitePeaces = new LinkedList<Peaces>();
	static LinkedList<Peaces> redPeaces = new LinkedList<Peaces>();
	enum Direction {
		UP,UPRIGHT,RIGHT,DOWNRIGHT,DOWN,DOWNLEFT,LEFT,UPLEFT
	}
	enum state {
		WHITEWON,REDWON,TIE,NOONEWON,REAL,DEMO,CPUCHECK
	}
	
	public static void main(String[] args) {

		intalizeGame();
		new ReversiGrafic();
	}
	
	public static Boolean putPeaces(Peaces peace ,  boolean turn,state state)
	{
		int i = peace.getI();
		int j = peace.getJ();
		board[i][j] = new Peaces(i, j,turn);
		boolean up,upright,right,downright,down,downleft,left,upleft;
		up = flipPeaces(i,j,peace,turn, Direction.UP,state);
		upright = flipPeaces(i,j,peace,turn, Direction.UPRIGHT,state);
		right = flipPeaces(i,j,peace,turn, Direction.RIGHT,state);
		downright = flipPeaces(i,j,peace,turn, Direction.DOWNRIGHT,state);
		down = flipPeaces(i,j,peace,turn, Direction.DOWN,state);
		downleft = flipPeaces(i,j,peace,turn, Direction.DOWNLEFT,state);
		left = flipPeaces(i,j,peace,turn, Direction.LEFT,state);
		upleft = flipPeaces(i,j,peace,turn, Direction.UPLEFT,state);
	

		if(  up || upright || right || downright || down || downleft || left || upleft)
			{
			if(state ==  state.DEMO)
				board[i][j] = null;
				return true;
			}
		board[i][j] = null;
		return false;
			
	}
	private static Boolean flipPeaces(int iOrginal, int jOrginal ,Peaces peace, boolean turn, Reversi.Direction direction,state state) {
		int i = peace.getI();
		int j = peace.getJ();
		if(i < 0 || i > row -1 || j < 0 || j > col -1)
				return false;
		if(board[i][j] == null)
			return false;
		if(board[i][j].getColor() == turn)
			if(Math.abs(iOrginal - i) >= 2 || Math.abs(jOrginal - j) >= 2  )
				return true;
			else if(iOrginal != i || jOrginal != j)
				return false;
		switch(direction)
		{
			case UP:
				if(flipPeaces(iOrginal, jOrginal , new Peaces(i -1, j,turn), turn , direction,state))
				{
					if(state != state.DEMO)
						removeAddPeace(peace,turn,state);
					return true;
				}
				break;
			
			case UPRIGHT:
				if(flipPeaces(iOrginal, jOrginal , new Peaces(i -1, j+1,turn), turn , direction,state))
				{
					if(state != state.DEMO)
						removeAddPeace(peace,turn,state);
					return true;
				}
				break;
			case RIGHT:
				if(flipPeaces(iOrginal, jOrginal , new Peaces(i , j+1,turn), turn , direction,state))
				{
					if(state != state.DEMO)
						removeAddPeace(peace,turn,state);
					return true;
				}
				break;
			case DOWNRIGHT:
				if(flipPeaces(iOrginal, jOrginal , new Peaces(i +1, j+1,turn), turn , direction,state) )
				{
					if(state != state.DEMO)
						removeAddPeace(peace,turn,state);
					return true;
				}
				break;
			case DOWN:
				if(flipPeaces(iOrginal, jOrginal , new Peaces(i +1, j,turn), turn , direction,state))
				{
					if(state != state.DEMO)
						removeAddPeace(peace,turn,state);
					return true;
				}
				break;
			case DOWNLEFT:
				if(flipPeaces(iOrginal, jOrginal , new Peaces(i +1, j-1,turn), turn , direction,state))
				{
					if(state != state.DEMO)
						removeAddPeace(peace,turn,state);
					return true;
				}
				break;
			case LEFT:
				if(flipPeaces(iOrginal, jOrginal , new Peaces(i , j-1,turn), turn , direction,state))
				{
					if(state != state.DEMO)
						removeAddPeace(peace,turn,state);
					return true;
				}
				break;
			case UPLEFT:
				if(flipPeaces(iOrginal, jOrginal , new Peaces(i -1, j-1,turn), turn , direction,state))
				{
					if(state != state.DEMO)
						removeAddPeace(peace,turn,state);
					return true;
				}
				break;
		}
		return false;
	}
	private static void removeAddPeace(Peaces peace, boolean turn, Reversi.state state)
	{
		int i = peace.getI();
		int j = peace.getJ();
		if(turn)
		{
			if(!whitePeaces.contains(peace))
				whitePeaces.add(peace);
			for(int k =0; k< redPeaces.size(); k++)
			{
				if(redPeaces.get(k).getI() == i && redPeaces.get(k).getJ() == j)
					redPeaces.remove(k);
			}
			if(state == state.REAL)
				ReversiGrafic.buttons[i][j].setBackground(Color.WHITE);
			board[i][j].setColor(turn);
		}
		else
		{
			if(!redPeaces.contains(peace))
				redPeaces.add(peace);
			for(int k =0; k< whitePeaces.size(); k ++)
			{
				if(whitePeaces.get(k).getI() == i && whitePeaces.get(k).getJ() == j)
					whitePeaces.remove(k);
			}
			if(state == state.REAL)
				ReversiGrafic.buttons[i][j].setBackground(Color.RED);
			board[i][j].setColor(turn);
		}

			
		
		
	}
	public static void putCpuPeace(boolean turn) {
		Peaces tempBoard[][] = new Peaces[row][col];
		int score = Integer.MIN_VALUE;
		LinkedList<Peaces> tempWhitePeaces = new LinkedList<Peaces>();
		LinkedList<Peaces> tempRedPeaces = new LinkedList<Peaces>();
		tempWhitePeaces = (LinkedList<Peaces>) whitePeaces.clone();
		tempRedPeaces = (LinkedList<Peaces>) redPeaces.clone();
		int bestScore = Integer.MIN_VALUE ;
		Peaces result = null;
		copyBoard(tempBoard,board);
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				if(board[i][j] == null && putPeaces(new Peaces(i,j,turn), turn, state.CPUCHECK))
				{
					score = minMax(7,false,Integer.MIN_VALUE, Integer.MAX_VALUE, (!turn));
					if(i == 0 || i == board.length -1)
					{
						if(j == board[0].length -1  || j == 0)
							score += 10;
						else if(j != board[0].length -2 && j != 1)
							score +=5 ;
							
					}
					if(j == board[0].length -1  || j == 0)
					{
						if(i != board.length -2 && i != 1)
							score +=5 ;
					}
					if(score > bestScore)
					{
						result = new Peaces(i,j,turn);
						bestScore = score;
					}
					copyBoard(board,tempBoard);
					whitePeaces = (LinkedList<Peaces>) tempWhitePeaces.clone();
					redPeaces = (LinkedList<Peaces>) tempRedPeaces.clone();
				}
			}
		}
		
//		System.out.println(bestScore);
//		
//		System.out.println(result);
		if(result != null)	
			putPeaces(result,turn,state.REAL);
		else
			isWinner(state.REAL);
	}
	// CPU - false - the CPU did  the last move now its player turn, true -the player did  the last move now its CPU turn
	
	private static int minMax(int depth, boolean CPU, int alpha, int beta , boolean turn) {
		
		Reversi.state result = Reversi.isWinner(state.DEMO);
		if(result != state.NOONEWON)
		{
			if(result == state.WHITEWON)
				return -100 * depth;
			else if(result == state.REDWON)
				return 100 * depth;
			else 
				return 0;
		}
		if(depth == 1)
			return redPeaces.size() - whitePeaces.size();
		
		Peaces tempBoard[][] = new Peaces[row][col];
		LinkedList<Peaces> tempWhitePeaces = new LinkedList<Peaces>();
		LinkedList<Peaces> tempRedPeaces = new LinkedList<Peaces>();
		tempWhitePeaces = (LinkedList<Peaces>) whitePeaces.clone();
		tempRedPeaces = (LinkedList<Peaces>) redPeaces.clone();
		if(CPU)
		{
			int bestScore = Integer.MIN_VALUE ;
			copyBoard(tempBoard,board);
			for (int i = 0; i < board.length; i++) {
				for (int j = 0; j < board[0].length; j++) {
					if(board[i][j] == null && putPeaces(new Peaces(i,j,turn), turn, state.CPUCHECK))
					{
						int score = minMax(depth-1 , false, alpha, beta, (!turn));
						alpha = Math.max(alpha, score);
						{
							if(j == board[0].length -1  || j == 0)
								score += 10;
							else if(j != board[0].length -2 && j != 1)
								score +=5 ;
								
						}
						if(j == board[0].length -1  || j == 0)
						{
							if(i != board.length -2 && i != 1)
								score +=5 ;
						}
						bestScore = Math.max(score, bestScore);
						copyBoard(board,tempBoard);
						whitePeaces = (LinkedList<Peaces>) tempWhitePeaces.clone();
						redPeaces = (LinkedList<Peaces>) tempRedPeaces.clone();
						if(beta <= alpha)
							break;

					}
					
				}
			}
			return bestScore;
		}else {
			int bestScore = Integer.MAX_VALUE;
			copyBoard(tempBoard,board);
			for (int i = 0; i < board.length; i++) {
				for (int j = 0; j < board[0].length; j++) {
					if(board[i][j] == null && putPeaces(new Peaces(i,j,turn), turn, state.CPUCHECK))
					{
						int score = minMax(depth-1 , true, alpha, beta, (!turn));
						beta = Math.min(beta, score);
						{
							if(j == board[0].length -1  || j == 0)
								score -= 10;
							else if(j != board[0].length -2 && j != 1)
								score -=5 ;
								
						}
						if(j == board[0].length -1  || j == 0)
						{
							if(i != board.length -2 && i != 1)
								score -=5 ;
						}
						bestScore = Math.min(score, bestScore);
						copyBoard(board,tempBoard);
						whitePeaces = (LinkedList<Peaces>) tempWhitePeaces.clone();
						redPeaces = (LinkedList<Peaces>) tempRedPeaces.clone();
						if(beta <= alpha)
							break;

					}
					
				}
			}
			
			return bestScore;
			
		}
	}
	public static void copyBoard(Peaces[][] paste, Peaces[][] copy) {
		for (int i = 0; i < copy.length; i++) {
			for (int j = 0; j < copy[0].length; j++) {
				if(copy[i][j] != null)
					paste[i][j] = new Peaces(copy[i][j]);
				else
					paste[i][j] = null;
			}
		}
	}


	public static state isWinner(state state)
	{
		if(whitePeaces.size() + redPeaces.size() ==  row * col)
			{
				if(whitePeaces.size() > redPeaces.size())
				{
					if(state == state.REAL)
						ReversiGrafic.text.setText("White won " + " " + whitePeaces.size() + " - " + redPeaces.size() );
					return state.WHITEWON;
				}
				else if(whitePeaces.size() < redPeaces.size())
				{
					if(state == state.REAL)
						ReversiGrafic.text.setText("Red won " + " " + redPeaces.size() + " - " + whitePeaces.size() );
					return state.REDWON;
				}
				else
				{
					if(state == state.REAL)
						ReversiGrafic.text.setText("This is a Tie " +redPeaces.size() + " - " + whitePeaces.size() );
					return state.TIE;
				}

			}
		if(whitePeaces.size() == 0)
		{
			if(state == state.REAL)
				ReversiGrafic.text.setText("Red won  " + whitePeaces.size() + " - " + redPeaces.size() );
			return state.REDWON;
		}
		else if(redPeaces.size() == 0)
		{
			if(state == state.REAL)
				ReversiGrafic.text.setText("White won  " + whitePeaces.size() + " - " + redPeaces.size() );
			return state.WHITEWON;
		}
		return state.NOONEWON;

	}
	private static void intalizeGame() {
		int half = row/2 -1;
		board[half][half] = new Peaces(half, half, true); // true - white, false - red
		whitePeaces.add(new Peaces(half, half, true));
		board[half+1][half+1] = new Peaces(half+1, half+1, true); 
		whitePeaces.add( new Peaces(half+1, half+1, true));	
		board[half][half+1] = new Peaces(half, half+1, false); 
		redPeaces.add( new Peaces(half, half+1, false));
		board[half+1][half] = new Peaces(half+1, half, false);
		redPeaces.add( new Peaces(half+1, half, false));
	}

	
}
