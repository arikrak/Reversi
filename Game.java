import java.util.Scanner;

public class Game {
	
	int[][] board;
	final int black = 1; //X
	final int white = -1; //O
	int white_score;
	int black_score;
	
	Scanner scan;
	
	int current_player;
	int moves=0;
	final int max_moves = 60;
	
	public Game(){
		scan = new Scanner(System.in);
		current_player = black;
		
		//[row y ][col x]
		board = new int[8][8];
		board[3][3]= black;
		board[4][4]= black;
		board[3][4]= white;
		board[4][3]= white;
		
		//test 
//		board[3][7]=white;	
//		board[4][7]=black;
		start();		
	}
	
	public void start()
	{
		System.out.print("Welcome to Reversi \n"); 
		
		while(moves<max_moves){		
			boolean canMove = printBoard();			
			//swap player if no legal move. **FIXME: If impossible for either, end game
			if(! canMove)
				current_player = -current_player;
						
			char current = getPlayerChar();
			System.out.println( "Score: Black:"+black_score + " White:" +white_score);
			System.out.print("Player"+ current +" Enter move Y X \n");  //-1**
			getMove();
		}
		
		
		System.out.println("Game Over!");
		printBoard();
		if(black_score>white_score){
			System.out.println("Black wins!"); 
		}
		else if(black_score<white_score){
			System.out.println("White wins!"); 
		}
		else{
			System.out.println("It's a tie!");
		}
		
		
	}
	
	//or use X and O
	private char getPlayerChar(){
		if(current_player==1)
			return '1';		
		else
			return '2';		
	}	
	
	//printBoard and other stuff once going through it. 
	public boolean printBoard()
	{
		black_score =0;
		white_score =0;
		boolean canMove = false;
		
		System.out.print(" _01234567\n");
		for(int y=0;y<board.length;y++){
			System.out.print(y+"|");
			for(int x=0;x<board.length;x++){
				if(board[y][x]== black){
					System.out.print("X");
					black_score++;
				}
				else if(board[y][x]== white){
					System.out.print("O"); 
					white_score++;
				}
				else{
					System.out.print(".");
					//simpler to just check  here if next player can move. no need to check if already true	
					if(!canMove){
						if(isLegal(y, x, false)){
							canMove= true;
						}
					}
				}
			}
			System.out.print("|\n");
		}
		return canMove;
	}
	
	public void getMove()
	{
		int y = scan.nextInt();
		int x = scan.nextInt();
		
		if(isLegal(y,x, true)){
			moves++;
			current_player = -current_player;
		}
		else{
			System.out.println("Illegal Move! Try Again.");
			getMove();
		}
	}
	
	/*check if move is legal by seeing if opposites nearby that lead into own color*	
		yax, xax - initial move
		y,x checks all surroundings
	 	ty - continues in that direc.	 
	 */	
	private boolean isLegal(int yax, int xax, boolean do_something)
	{
		//check that spot's open. or just check null
		if(board[yax][xax] == 1 || board[yax][xax] == -1){
			return false;
		}
		
		boolean legal=false;
		
		
		for(int y = yax-1; y<= yax+1; y++){
			for(int x = xax-1; x <= xax+1; x++){
				
				if(! inBounds(y, x))
					continue;
				
				if(board[y][x] == -current_player){
					//continue in that direction
					int dx = x - xax;
					int dy = y - yax;
					
					int ty = y;
					int tx = x;
					
					while(inBounds(ty,tx) && board[ty][tx] == -current_player){						
						ty += dy;
						tx += dx;								
						//System.out.println("ty:"+ty+"tx:"+tx+" yax:"+yax+" xax:"+xax);
					}
					//need checks everywhere?
					if(inBounds(ty,tx) && board[ty][tx] == current_player){
						legal = true;
						
						//go back. this would be nicer in ruby
						if(do_something){
						while(! (ty == yax && tx == xax)){
							ty -= dy;
							tx -= dx;
							board[ty][tx] = current_player;								
						}					
						board[yax][xax] = current_player;
						}
						
					}					
				}
			}
		}
		return legal;	
	}
	
	private boolean inBounds(int y, int x)
	{
		return ! (y<0 || y > 7 || x < 0 || x > 7);
	}
	

}
