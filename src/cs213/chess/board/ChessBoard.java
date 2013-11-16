package cs213.chess.board;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import cs213.chess.piece.*;
import cs213.chess.utils.*;

/**
 * Class representing a chess board.  Stores ChessPiece objects on a static
 * 8 by 8 array, representing the board itself.
 * 
 * @author Thomas Travis
 */
public class ChessBoard implements Board{

	private static ChessPiece board[][];
	private static List<ChessPiece> blackPieces;
	private static List<ChessPiece> whitePieces;
	private King blackKing;
	private King whiteKing;
	private ChessPiece enPassantPawn = null;
	private List<String[]> moves;
	private int index;
	private ChessPiece capturedPiece = null;
	private List<ChessPiece[][]> boardLayouts;
	
	/**
	 * no arg Constructor
	 */
	public ChessBoard(){
		
		board = new ChessPiece[8][8];
		blackPieces = new LinkedList<ChessPiece>();
		whitePieces = new LinkedList<ChessPiece>();
		moves = new LinkedList<String[]>();
		index = -1;
		boardLayouts = new LinkedList<ChessPiece[][]>();
	}
	
	public void startNewGame(Board b){
		
		initializeBoard("white", b);
		initializeBoard("black", b);
	}
	
	public ChessPiece[][] getBoard(){
		return board;
	}
	
	public ChessPiece getEnPassantPawn(){
		return enPassantPawn;
	}
	
	public ChessPiece getPieceAtPosition(String position){
		
		int col = fileToColumn(position);
		int row = rankToRow(position);
		ChessPiece p = board[col][row];
		if( p != null ){
			return p;
		}
		return null;
	}
	
	public ChessPiece getPieceAtPosition(int col, int row){
		
		ChessPiece p = board[col][row];
		if( p != null ){
			return p;
		}
		return null;
	}
	
	public List<ChessPiece> getPieces(String player){
		if(player.equalsIgnoreCase("white") ) return whitePieces;
		return blackPieces;
	}
	
	public ChessPiece getBlackKing(){
		return blackKing;
	}
	
	public ChessPiece getWhiteKing(){
		return whiteKing;
	}
	
	public List<String[]> getMoves(){
		return moves;
	}
	
	public void setMoves(List<String[]> moves){
		this.moves = moves;
	}
	
	public void back(){
		
		this.undo();
		index--;
	}
	
	public boolean hasBack(){
		if( index < 0 ){
			return false;
		}
		return true;
	}
	
	public void next(){
		
		index++;
		
		if( index >= moves.size() ){
			
		}
		
		String [] args = moves.get( index );
		
		ChessPiece p = this.getPieceAtPosition( args[0] );
		
		move(p, args[1]);
	}
	
	public boolean hasNext(){
		
		if( index >= moves.size() - 1 ){
			return false;
		}
		return true;
	}	
	
	public boolean check(String player){
		
		King k;
		List<ChessPiece> cp;
		if( player.equalsIgnoreCase("white") ){
			k = whiteKing;
			cp = new LinkedList<ChessPiece>(blackPieces);
		}
		else{ 
			k = blackKing; 
			cp = new LinkedList<ChessPiece>(whitePieces);
		}
		
		if( k.isChecked(k.getPosition(), cp) ) return true;
		return false;
	}
	
	public boolean stalemate(String player){
		
		King k;
		if( player.equalsIgnoreCase("white") ) k = whiteKing;
		else{ k = blackKing; }
		
		List<ChessPiece> cp;
		if( player.equalsIgnoreCase("white") ) cp = new LinkedList<ChessPiece>(blackPieces);
		else{ cp = new LinkedList<ChessPiece>(whitePieces); }
		
		if( k.isChecked(k.getPosition(), cp) ) return false;
		
		if( player.equalsIgnoreCase("white") ) cp = new LinkedList<ChessPiece>(whitePieces);
		else{ cp = new LinkedList<ChessPiece>(blackPieces); }
		
		Iterator<ChessPiece> i = cp.iterator();
		ChessPiece p;
		List<String> a;
		
		while( i.hasNext() ){
			p = i.next();
			a = p.hasAvailableMoves();
			if( a.size() > 0 ) return false;
		}
		
		return true;
	}
	
	public boolean checkmate(String player){
		King k;
		if( player.equalsIgnoreCase("white") ) k = whiteKing;
		else{ k = blackKing; }
		
		List<ChessPiece> cp;
		if( player.equalsIgnoreCase("white") ) cp = new LinkedList<ChessPiece>(blackPieces);
		else{ cp = new LinkedList<ChessPiece>(whitePieces); }
		
		if( !k.isChecked(k.getPosition(), cp) ) return false;
		
		if( player.equalsIgnoreCase("white") ) cp = new LinkedList<ChessPiece>(whitePieces);
		else{ cp = new LinkedList<ChessPiece>(blackPieces); }
		
		Iterator<ChessPiece> i = cp.iterator();
		ChessPiece p;
		List<String> a;
		
		while( i.hasNext() ){
			p = i.next();
			a = p.hasAvailableMoves();
			if( a.size() > 0 ) return false;
		}
		
		return true;
	}
	
	/**
	 * Move a pawn according to the given arguments.
	 * @param args
	 * @return true if moved successfully; false otherwise
	 */
	private boolean pawnMoves(String [] args){
		
		String start = args[0];
		String end = args[1];
		
		int endRow = rankToRow(end);
		
		ChessPiece p = getPieceAtPosition(start);
		ChessPiece q = getPieceAtPosition(end);
		String s;
		
		if( p.getPlayer().equals("white") && !p.hasMoved() && endRow == 3 ){
			
			if( !p.canMoveTo(end) ) return false;
			
			move( p, end );
			
			s = matrixPositionToFileAndRank( p.getCol()-1, endRow );
			if( this.isMoveWithinRange(s) ){
				q = getPieceAtPosition( s );
				if( q != null ){
					if( q.getPlayer().equals("black") && q.getType().equals("pawn") ){
						enPassantPawn = p;
					}
				}
			}
			
			s = matrixPositionToFileAndRank( p.getCol()+1, endRow );
			if( this.isMoveWithinRange(s) ){
				q = getPieceAtPosition( s );
				if( q != null ){
					if( q.getPlayer().equals("black") && q.getType().equals("pawn") ){
						enPassantPawn = p;
					}
				}
			}
			return true;
		}
		
		if( p.getPlayer().equals("black") && !p.hasMoved() && endRow == 4 ){
			
			if( !p.canMoveTo(end) ) return false;
			
			move( p, end );
			
			s = matrixPositionToFileAndRank( p.getCol()-1, endRow );
			
			if( this.isMoveWithinRange(s) ){
				q = getPieceAtPosition( s );
				if( q != null ){
					if( q.getPlayer().equals("white") && q.getType().equals("pawn") ){
						enPassantPawn = p;
					}
				}
			}
			
			s = matrixPositionToFileAndRank( p.getCol()+1, endRow );
			
			if( this.isMoveWithinRange(s) ){
				q = getPieceAtPosition( s );
				if( q != null ){
					if( q.getPlayer().equals("white") && q.getType().equals("pawn") ){
						enPassantPawn = p;
					}
				}
			}
			return true;			
		}
		
		if( enPassantPawn != null ){
			if( q == null ){
				if( !p.getPlayer().equals(enPassantPawn.getPlayer()) ){
					if( enPassantPawn.getPlayer().equals("white") ){
						s = matrixPositionToFileAndRank(enPassantPawn.getCol(), enPassantPawn.getRow()-1);
						if( s.equals(end) ){
							
							move( enPassantPawn, end );
							move( p, end );
							enPassantPawn = null;
							return true;
						}
					}
					else{
						s = matrixPositionToFileAndRank(enPassantPawn.getCol(), enPassantPawn.getRow()+1);
						if( s.equals(end) ){
							
							move( enPassantPawn, end );
							move( p, end );
							enPassantPawn = null;
							return true;
						}
					}
				}
			}
			
		}
	
		if( (p.getPlayer().equals("white") && endRow == 7) 
			|| (p.getPlayer().equals("black") && endRow == 0) ){
			
			if( !p.canMoveTo(end) ) return false;
			promotion(p, args);
			return true;
		}
		
		if( !p.canMoveTo(end) ) return false;
		move( p, end );
		return true;
	}
	
	/**
	 * Moves a king according to the given arguments.
	 * @param args
	 * @return true if moved successfully
	 */
	private boolean kingMoves(String [] args){
		
		String start = args[0];
		String end = args[1];
		
		ChessPiece p = getPieceAtPosition(start);
		ChessPiece q = getPieceAtPosition(end);
		
		int endCol = fileToColumn(args[1]);
		if( Math.abs(p.getCol()-endCol) == 2 ){
			String pos;
			int pRow = p.getRow();
			
			if( endCol == 6){
				pos = matrixPositionToFileAndRank(7, pRow);
				q = getPieceAtPosition(pos);
								
				pos = matrixPositionToFileAndRank(endCol, pRow);
				move(p, pos);
				
				pos = matrixPositionToFileAndRank(endCol-1, pRow);
				move(q, pos);
				return true;
			}
			if( endCol == 2 ){
				pos = matrixPositionToFileAndRank(0, pRow);
				q = getPieceAtPosition(pos);
								
				pos = matrixPositionToFileAndRank(endCol, pRow);
				move(p, pos);
				
				pos = matrixPositionToFileAndRank(endCol+1, pRow);
				move(q, pos);
				return true;
			}
		}
		
		move( p, end );
		return true;
	}
	
	public boolean movePiece(String [] args){
		
		ChessPiece p = getPieceAtPosition(args[0]);
				
		if( enPassantPawn != null ){
			if( p.getPlayer().equals(enPassantPawn.getPlayer()) ){
				enPassantPawn = null;
			}
		}
		
		if( !isMoveWithinRange(args[1]) ) return false;
		
		if( p.getType().equals("pawn") ) return pawnMoves(args);
		
		if( !p.canMoveTo(args[1]) ) return false;
		
		if( p.getType().equals("king") ) return kingMoves(args);
		
		move(p, args[1]);
				
		return true;
	}
	
	public boolean isMoveWithinRange(String endPosition){
		int col = fileToColumn(endPosition);
		int row = rankToRow(endPosition);
		
		if( col < 0 || col > 7 ) return false;
		if( row < 0 || row > 7 ) return false;
		
		return true;
	}
	
	/**
	 * Move the given piece to the given position
	 * @param p
	 * @param position
	 */
	private void move(ChessPiece p, String position){
		int pCol = p.getCol();
		int pRow = p.getRow();
			
		capturedPiece = null;
		
		ChessPiece q = getPieceAtPosition(position);
		if( q != null ){
			if( q.getPlayer().equals("white") ) whitePieces.remove(q);
			else{ blackPieces.remove(q); }
			capturedPiece = q;
		}
		
		String[] s = new String[2];
		s[0] = p.getPosition();
		s[1] = position;
		moves.add(s);
		
		p.setPosition(position);
		
		if( !p.hasMoved() ) p.setFirstMove(false);
		
		board[p.getCol()][p.getRow()] = p;
		board[pCol][pRow] = null;
		
		addLayout();
	}
	
	public boolean undo(){
		
		int end = moves.size() - 1;
		
		if( end < 1 ){
			return false;
		}
		else{
			String [] args = moves.remove(end);
			ChessPiece p = getPieceAtPosition(args[1]);
			move(p, args[0]);
			
			if( capturedPiece != null ){
				
				capturedPiece.setPosition(args[1]);
				int col = fileToColumn(args[1]);
				int row = rankToRow(args[1]);
				board[col][row] = capturedPiece;
				
				if( capturedPiece.getPlayer().equalsIgnoreCase("white") ){
					whitePieces.add(capturedPiece);
				}
				else{
					blackPieces.add(capturedPiece);
				}
				capturedPiece = null;
			}
			
			boardLayouts.remove(boardLayouts.size()-1);
			return true;
		}
	}
	
	/**
	 * Promote a pawn to a new ChessPiece (Queen by default, unless otherwise
	 * specified by the given args)
	 * @param p
	 * @param args
	 */
	private void promotion(ChessPiece p, String [] args){
		int endCol = fileToColumn(args[1]);
		int endRow = rankToRow(args[1]);
		
		move(p, args[1]);
		
		String player = p.getPlayer();
		String position = p.getPosition();
		Board b = p.getBoard();
		
		if( player.equals("white") ) whitePieces.remove(p);
		else{ blackPieces.remove(p); }

		if( args.length == 2 ){
			p = new Queen(player, position, b);
		}
		if( args.length == 3 ){
			String t = args[2];
			if( t.equals("") || t.equalsIgnoreCase("q") || t.equalsIgnoreCase("queen") ){
				p = new Queen(player, position, b);
			}
			if( t.equalsIgnoreCase("b") || t.equalsIgnoreCase("bishop") ){
				p = new Bishop(player, position, b);
			}
			if( t.equalsIgnoreCase("n") || t.equalsIgnoreCase("knight") ){
				p = new Knight(player, position, b);
			}
			if( t.equalsIgnoreCase("r") || t.equalsIgnoreCase("rook") ){
				p = new Rook(player, position, b);
			}
		}
		board[endCol][endRow] = p;
		
		if( player.equals("white") ) whitePieces.add(p);
		else{ blackPieces.add(p); }
	}
	
	/**
	 * Set up the pieces of the given color on the board in their initial positions
	 * @param color
	 */
	private void initializeBoard(String color, Board b){
		
		int row = 1;
		if( color.equals("black") ) row = 6;
		
		for( int col = 0; col < 8; col++ ){
			board[col][row] = new Pawn(color, col, row, b);
			if( color.equals("white") ) whitePieces.add(board[col][row]);
			else{ blackPieces.add(board[col][row]); }
		}
		
		row = 0;
		if( color.equals("black") ) row = 7;
		
		board[0][row] = new Rook(color, 0, row, b);
		if( color.equals("white") ) whitePieces.add(board[0][row]);
		else{ blackPieces.add(board[0][row]); }
		
		board[1][row] = new Knight(color, 1, row, b);
		if( color.equals("white") ) whitePieces.add(board[1][row]);
		else{ blackPieces.add(board[1][row]); }
		
		board[2][row] = new Bishop(color, 2, row, b);
		if( color.equals("white") ) whitePieces.add(board[2][row]);
		else{ blackPieces.add(board[2][row]); }
		
		board[3][row] = new Queen(color, 3, row, b);
		if( color.equals("white") ) whitePieces.add(board[3][row]);
		else{ blackPieces.add(board[3][row]); }
		
		board[4][row] = new King(color, 4, row, b);
		if( color.equals("white") ){
			whitePieces.add(board[4][row]);
			whiteKing = (King)board[4][row];
		}
		else{ 
			blackPieces.add(board[4][row]);
			blackKing = (King)board[4][row];
		}
				
		board[5][row] = new Bishop(color, 5, row, b);
		if( color.equals("white") ) whitePieces.add(board[5][row]);
		else{ blackPieces.add(board[5][row]); }
		
		board[6][row] = new Knight(color, 6, row, b);
		if( color.equals("white") ) whitePieces.add(board[6][row]);
		else{ blackPieces.add(board[6][row]); }
		
		board[7][row] = new Rook(color, 7, row, b);
		if( color.equals("white") ) whitePieces.add(board[7][row]);
		else{ blackPieces.add(board[7][row]); }
	}
	
	public String matrixPositionToFileAndRank(int col, int row){
		
		char c = (char)(col + 97);
		return new StringBuilder().append(c).append(row+1).toString();
	}
	
	public int fileToColumn(String position){
		if( position.length() < 2 ) return -1;
		position = position.toLowerCase();
		char c = position.charAt(0);
		if( Character.isDigit(c) ) return -1;
		int i = (int)(c-97);
		return i;
	}
	
	public int rankToRow(String position){
		if( position.length() < 2 ) return -1;
		String s = position.substring(1);
		if( s.length() > 1 ) return -1;
		if( !Character.isDigit(s.charAt(0)) ) return -1;
		int i = Integer.parseInt(s);
		return (i-1);
	}

	public List<ChessPiece[][]> getBoardLayouts(){
		return boardLayouts;
	}
	
	private void addLayout(){
		ChessPiece [][] cpa = new ChessPiece[board.length][];
		for( int i = 0; i < board.length; i++ ){
			cpa[i] = board[i].clone();
		}
		boardLayouts.add(cpa);
	}
}
