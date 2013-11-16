package cs213.chess.piece;

import java.util.LinkedList;
import java.util.List;

import cs213.chess.board.*;

/**
 * An abstract class representing a chess piece.
 * 
 * @author Thomas Travis
 */
public abstract class ChessPiece {

	protected String player;
	protected String type;
	protected int col;
	protected int row;
	protected Board b;
	protected boolean firstMove;
	
	/**
	 * ChessPiece constructor
	 * @param player
	 * @param position
	 * @param b
	 */
	protected ChessPiece(String player, String position, Board b){
		this.player = player;
		this.col = fileToColumn(position);
		this.row = rankToRow(position);
		this.b = b;
		this.firstMove = true;
	}
	
	/**
	 * ChessPiece constructor
	 * @param player
	 * @param col
	 * @param row
	 * @param b
	 */
	protected ChessPiece(String player, int col, int row, Board b){
		this.player = player;
		this.col = col;
		this.row = row;
		this.b = b;
		this.firstMove = true;
	}
	
	/**
	 * Set the board this piece is on
	 * @param b
	 */
	public void setBoard(Board b){
		this.b = b;
	}
	
	/**
	 * Get the board this piece is on
	 * @return Instance of Board that this piece resides on
	 */
	public Board getBoard(){
		return b;
	}
	
	/**
	 * Set the player owning this piece
	 * @param player
	 */
	public void setPlayer(String player){
		this.player = player;
	}
	
	/**
	 * Get the player owning this piece
	 * @return String representing the player owning this piece
	 */
	public String getPlayer(){
		return player;
	}
	
	/**
	 * Get this piece's type.
	 * @param type
	 */
	public void setType(String type){
		this.type = type;
	}
	
	/**
	 * Set this piece's type
	 * @return this piece's type
	 */
	public String getType(){
		return type;
	}
	
	/**
	 * Set this piece's position to the given position.  Converts the string position,
	 * representing the board FileRank position, to the corresponding 2D array coordinates.
	 * 
	 * @param position
	 */
	public void setPosition(String position){
		this.col = fileToColumn(position);
		this.row = rankToRow(position);
	}
	
	/**
	 * Get this piece's FileRank position.
	 * 
	 * @return String representing the piece's position on the board
	 */
	public String getPosition(){
		return matrixPositionToFileAndRank(this.col, this.row);
	}
	
	/**
	 * Set this piece's 2D array column position
	 * 
	 * @param col
	 */
	public void setCol(int col){
		this.col = col;
	}
	
	/**
	 * Get this piece's 2D array column position
	 * 
	 * @return int column
	 */
	public int getCol(){
		return col;
	}
	
	/**
	 * Set this piece's 2D array row position
	 * 
	 * @param row
	 */
	public void setRow(int row){
		this.row = row;
	}
	
	/**
	 * Get this piece's 2D array row position
	 * 
	 * @return int row
	 */
	public int getRow(){
		return row;
	}
	
	/**
	 * Set this piece's firstMove.  firstMove should equal true if this piece has not yet
	 * made a move.
	 * 
	 * @param firstMove
	 */
	public void setFirstMove(boolean firstMove){
		this.firstMove = firstMove;
	}
	/**
	 * Get this piece's firstMove.
	 * 
	 * @return true if the piece has not yet made a move; false otherwise
	 */
	public boolean getFirstMove(){
		return this.firstMove;
	}
	
	/**
	 * State whether the piece has already made its first move.
	 * 
	 * @return true if the piece has previously made a move; false otherwise
	 */
	public boolean hasMoved(){
		if( this.firstMove == true ) return false;
		return true;
	}
	
	/**
	 * State whether this piece's King will be checked after this piece has moved to position
	 * @param position
	 * @return true if this piece's King will be checked after it moves to position; false otherwise
	 */
	protected boolean moveEndangersKing(String position){
		
		King k;
		List<ChessPiece> cp;
		boolean legalMove = false;
		
		if( player.equals("white") ){
			k = (King)this.b.getWhiteKing();
			cp = new LinkedList<ChessPiece>(this.b.getPieces("black"));
		}
		else{ 
			k = (King)this.b.getBlackKing(); 
			cp = new LinkedList<ChessPiece>(this.b.getPieces("white"));
		}
		
		ChessPiece[][] board = this.b.getBoard();
		ChessPiece thisPiece = board[this.col][this.row];
		
		int startCol = this.col;
		int startRow = this.row;
		int endCol = this.fileToColumn(position);
		int endRow = this.rankToRow(position);
		
		ChessPiece q = board[endCol][endRow];
		if( q != null ) cp.remove(q);
	
		board[startCol][startRow] = null;
		board[endCol][endRow] = thisPiece;
		
		if( k.isChecked(k.getPosition(), cp) ) legalMove =  true;
		
		board[startCol][startRow] = thisPiece;
		board[endCol][endRow] = q;
		if( q != null ) cp.add(q);
		
		return legalMove;
	}
	
	/**
	 * State whether the piece can legally move to the given position, as described
	 * by its inherent moveset.
	 * 
	 * @param position
	 * @return true if the piece can move to the given position; false otherwise
	 */
	public abstract boolean canMoveTo(String position);
	
	/**
	 * Obtain a list of all moves this ChessPiece can make from its current position.
	 * 
	 * @return List of Strings representing the board locations this piece can move to
	 */
	public abstract List<String> hasAvailableMoves();
	
	/**
	 * Return a short, four character long String representation of this ChessPiece.
	 * 
	 * @return String representing this ChessPiece (Ex. White King returns "Kg-W",
	 * Black Pawn returns "Pn-B", etc.)
	 */
	public abstract String toString();
	
	/**
	 * Convert from a numeric matrix position to a traditional Rank & File position
	 * representation.
	 * @param col
	 * @param row
	 * @return A String representing the Rank&File corresponding to the numeric
	 * matrix position
	 */
	protected String matrixPositionToFileAndRank(int col, int row){
		
		char c = (char)(col+97);
		return new StringBuilder().append(c).append(row+1).toString();
	}
	
	/**
	 * Convert the File position to the matrix column position
	 * @param position
	 * @return int representing the matrix column corresponding to the file position
	 */
	protected int fileToColumn(String position){
		char c = position.charAt(0);
		int i = (int)c-97;
		return i;
	}
	
	/**
	 * Convert the Rank position to the matrix row column position
	 * @param position
	 * @return int representing the matrix row corresponding to the rank position
	 */
	protected int rankToRow(String position){
		
		
		String s = position.substring(1);
		int i = Integer.parseInt(s);
		return (i-1);
	}
}
