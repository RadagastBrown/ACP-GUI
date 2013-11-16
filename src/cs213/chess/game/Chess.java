package cs213.chess.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import cs213.chess.board.*;
import cs213.chess.piece.*;

/**
 * The command line view for a game of chess between two players.  Contains the main method
 * of this program.
 * 
 * @author Thomas Travis
 */
public class Chess {

	private static Board b;
	private static BufferedReader br;
	private static String line;
	private static boolean whiteIsChecked;
	private static boolean blackIsChecked;
	private static boolean whiteRequestsDraw;
	private static boolean blackRequestsDraw;
	
	/**
	 * Drawn an ASCII chessboard to the terminal.
	 */
	private static void drawBoard(){
		
		System.out.println();
		
		for( int row = 0; row < 8; row ++){
			
			System.out.print((8-row) + " |");
			
			for( int col = 0; col < 8; col++ ){
				
				ChessPiece p = b.getPieceAtPosition(col, 7-row);
				
				if( p == null ){
					if( row % 2 != 0 && col % 2 != 0 ) System.out.print(" ## |");
					else if( row % 2 == 0 && col %2 == 0 ) System.out.print(" ## |");
					else{ System.out.print("    |"); }
				}
				else{ System.out.print(p.toString()+"|"); }
			}
			System.out.println();
		}
		System.out.print("   ");
		
		for( int i = 0; i < 8; i++ ){
			char c = (char)(97+i);
			System.out.print("  "+c+"  ");
		}
		System.out.println();		
	}
	
	/**
	 * Read a line of input from the terminal.
	 * @return a String array of inputs; split on space
	 */
	private static String[] readLine(){
		
		br = new BufferedReader( new InputStreamReader( System.in ) );
		
		try{
			line = br.readLine();
		}
		catch(IOException e){
			System.out.println("There was an error taking your input.");
			System.out.println("Closing program.");
			System.exit(1);
		}
		return line.split(" ");
	}
	
	/**
	 * Determine if the input is a valid promotion piece
	 * @param s
	 * @return true if the input is valid for representing a piece that a pawn may be
	 * promoted to; false otherwise
	 */
	private static boolean isAPromotionPiece(String s){
		
		if( s.equalsIgnoreCase("n")
			|| s.equalsIgnoreCase("knight")
			|| s.equalsIgnoreCase("q")
			|| s.equalsIgnoreCase("queen")
			|| s.equalsIgnoreCase("b")
			|| s.equalsIgnoreCase("bishop") 
			|| s.equalsIgnoreCase("r")
			|| s.equalsIgnoreCase("rook") ){
			
			return true;
		}
		return false;
	}
	
	/**
	 * Play chess between two players.
	 * @return a String representing the end condition of the game: a draw, a resignation,
	 * a stalemate, or a checkmate
	 */
	private static String playChess(){
		String endCondition = "";
		String error;
		String message;
		String [] args;
		String player = "White";
		boolean gameOver = false;
		boolean moved = false;
		
		drawBoard();
		
		while( !gameOver ){
			
			
			if( b.stalemate(player) ){
				endCondition = "Stalemate.  This game is a draw.";
				break;
			}
			
			if( b.check(player) ){
				System.out.println("Check.");
				if( player.equals("White") ) whiteIsChecked = true;
				else{ blackIsChecked = true; }
			}	
			else{
				if( player.equals("White") && whiteIsChecked ) whiteIsChecked = false;
				if( player.equals("Black") && blackIsChecked ) blackIsChecked = false;
			}
			
			error = "";
			message = "";
			
			System.out.print(player+"'s move: ");
			args = readLine();
			
			if( args.length == 1 ){
				
				if( args[0].equalsIgnoreCase("resign") ){
					
					System.out.println(player+" resigns.");
					
					if( player.equals("White") ) endCondition = "Black wins!";
					else{ endCondition =  "White wins!"; }
					gameOver = true;
				}
				else if( args[0].equalsIgnoreCase("draws") || args[0].equalsIgnoreCase("draw") ){
					if( (player.equals("White") && blackRequestsDraw) 
						|| (player.equals("Black") && whiteRequestsDraw) ){
						
						endCondition = player+" accepts.  The game is a draw.";
						gameOver = true;
					}
					else{ error = "Your opponent has not requested a draw."; }
				}
				else{ error = "Incorrect input.  Please try again."; }
			}
			
			if( args.length == 2 || args.length == 3){
				
				if( b.isMoveWithinRange(args[0]) && b.isMoveWithinRange(args[1])){
					
					args[0] = args[0].toLowerCase();
					args[1] = args[1].toLowerCase();
					
					ChessPiece p = b.getPieceAtPosition(args[0]);
					
					if( p != null ){
						
						if( player.equalsIgnoreCase(p.getPlayer()) ){
							
							if( args.length == 3 ){
								
								if( isAPromotionPiece(args[2]) || args[2].equalsIgnoreCase("draw?") ){
								
									if( !p.getType().equals("pawn") && isAPromotionPiece(args[2])){
										error = "You may only promote a pawn.";
									}
									else if( p.getType().equals("pawn") && isAPromotionPiece(args[2])){
										int endRow = b.rankToRow(args[1]);
										if( (p.getPlayer().equals("white") && endRow != 7) 
											|| (p.getPlayer().equals("black") && endRow != 0) ){
											
											error = "You may only promote a Pawn on the farthest rank from its starting position.";
										}
										else{
											moved = b.movePiece(args);
											
											if( moved && args[2].equalsIgnoreCase("draw?") ){
												if( (player.equals("White") && !blackRequestsDraw)
													|| ( player.equals("Black") && !whiteRequestsDraw) ){
													
													if( player.equals("White") ) whiteRequestsDraw = true;
													else{ blackRequestsDraw = true; }
													
													message = player+" requests a draw.  Do you accept?";
												}
												else{ 
													error = "You cannot request a draw after your opponent has already done so."; 
												}
											}
											if( moved && isAPromotionPiece(args[2]) ){
												if( player.equals("White") && blackRequestsDraw ) blackRequestsDraw = false;
												if( player.equals("Black") && whiteRequestsDraw ) whiteRequestsDraw = false;
											}
											
											if( !moved ){
												error = "Cannot move "+b.getPieceAtPosition(args[0])+" to "+args[1]+" from "+args[0]+".";
											}
										}
									}
									else{
										moved = b.movePiece(args);
											
										if( moved && args[2].equalsIgnoreCase("draw?") ){
											if( (player.equals("White") && !blackRequestsDraw)
												|| ( player.equals("Black") && !whiteRequestsDraw) ){
												
												if( player.equals("White") ) whiteRequestsDraw = true;
												else{ blackRequestsDraw = true; }
												
												message = player+" requests a draw.  Do you accept?";
											}
											else{ 
												error = "You cannot request a draw after your opponent has already done so."; 
											}
										}
										if( moved && isAPromotionPiece(args[2]) ){
											if( player.equals("White") && blackRequestsDraw ) blackRequestsDraw = false;
											if( player.equals("Black") && whiteRequestsDraw ) whiteRequestsDraw = false;
										}
										
										if( !moved ){
											error = "Cannot move "+b.getPieceAtPosition(args[0])+" to "+args[1]+" from "+args[0]+".";
										}
									}
								}
								else{ 
									error = "Please enter a valid third argument."; 
								}
							}
							else{
								
								moved = b.movePiece(args);
								
								if( moved ){
									if( player.equals("White") && blackRequestsDraw ) blackRequestsDraw = false;
									if( player.equals("Black") && whiteRequestsDraw ) whiteRequestsDraw = false;
								}
								
								if( !moved ){
									error = "Cannot move "+b.getPieceAtPosition(args[0])+" to "+args[1]+" from "+args[0]+".";
								}
							}
						}
						else{ 
							error = "You may only move your own pieces."; 
						}
					}
					else{ 
						error = "Please choose a piece on the board to move."; 
					}	
				}
				else{ 
					error = "Please choose a valid board position(s)."; 
				}	
			}

			if(args.length < 1 || args.length > 3){ 
				error = "Incorrect number of args.  Please try again."; 
			}
			
			drawBoard();
			
			if( error.length() > 0 ){
				
				System.out.println(error);
				
				if( error.substring(0, 11).equals("Cannot move") ){
					
					ChessPiece p = b.getPieceAtPosition(args[0]);
					
					List<String> a = p.hasAvailableMoves();
					
					if( a.size() == 0 ){
						System.out.println(p.getType()+" at "+p.getPosition()+" has no available moves.");
					}
					else{
						System.out.println(p.getType()+" at "+p.getPosition()+" has the following available moves: ");
						for( int i = 0; i < a.size()-1; i++ ){
							System.out.print(a.get(i)+", ");
						}
						System.out.println(a.get(a.size()-1));
					}
					
					if( (player.equals("White") && whiteIsChecked) 
						|| (player.equals("Black") && blackIsChecked) ){
						System.out.println("You must ensure your king is not checked");
					}
				}
			}
			else{
				
				if( message.length() > 0 ) System.out.println(message);
				
				String s = player;
				
				if( player.equals("White") ) player = "Black";
				else{ player = "White"; }
				
				if( b.checkmate( player ) ){
					endCondition = "Checkmate.  "+s+" wins!";
					gameOver = true;
				}
			}	
		}
		return endCondition;
	}
	
	public static void main( String [] args ){
		
		b = new ChessBoard();
		whiteIsChecked = false;
		blackIsChecked = false;
		whiteRequestsDraw = false;
		blackRequestsDraw = false;
		
		b.startNewGame(b);
		
		System.out.println( playChess() );
	}
}
