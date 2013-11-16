package cs213.chess.board;

import java.util.List;

import cs213.chess.piece.*;

/**
 * The interface through which Chess.java accesses a chess board implementation.
 * @author Thomas Travis
 *
 */
public interface Board {
	
	/**
	 * Begin a new game, setting all pieces to their initial positions
	 */
	void startNewGame(Board b);
	
	/**
	 * Get this board.
	 * @return A 2D array of ChessPieces representing the current game board.
	 */
	ChessPiece[][] getBoard();
	
	ChessPiece getEnPassantPawn();
	
	/**
	 * Obtain the piece at the given location
	 * @param position
	 * @return the Piece object at the given position; null if there is no
	 * Piece at that position
	 */
	ChessPiece getPieceAtPosition(String position);
	
	/**
	 * Obtain the piece at the given location
	 * @param col
	 * @param row
	 * @return the Piece object at the given col, row coordinates; null if there
	 * is not Piece at that position
	 */
	ChessPiece getPieceAtPosition(int col, int row);
	
	/**
	 * Obtain a list of the pieces currently owned by the given player.
	 * @param player
	 * @return A List of ChessPiece objects owned by the given player
	 */
	List<ChessPiece> getPieces(String player);
	
	/**
	 * Obtain the black King chess piece
	 * @return ChessPiece object representing black's King
	 */
	ChessPiece getBlackKing();
	
	/**
	 * Obtain the white King chess piece
	 * @return ChessPiece object representing white's King
	 */
	ChessPiece getWhiteKing();
		
	/**
	 * Obtain a list of all moves made thus far, in order.
	 * @return a List of String arrays.  Each array is two elements; the first being the origin and
	 * the second being the destination.
	 * @author Thomas Travis
	 */
	List<String[]> getMoves();
	
	void setMoves(List<String[]> moves);
	
	/**
	 * Go back one turn (for use with game replays only)
	 * @author Thomas Travis
	 */
	void back();
	
	/**
	 * Check to see if the game can be regressed any further.  Specifically, checks to see if
	 * the index pointing into the list of moves is less than 0.
	 * @return true if cannot go back any more moves; false otherwise
	 * @author Thomas Travis
	 */
	boolean hasBack();
	
	/**
	 * Go forward one turn (for use with game replays only)
	 * @author Thomas Travis
	 */
	void next();
	
	/**
	 * Check to see if the game can be progressed any further.  Specifically, checks to see if 
	 * the index pointing into the list of moves is greater than or equal to the size of said
	 * list.
	 * @return true if there are still remaining moves; false otherwise
	 * @author Thomas Travis
	 */
	boolean hasNext();
	
	/**
	 * Check whether the given player's king is checked
	 * @param player
	 * @return true if any one of the opponent's pieces can directly attack this player's King;
	 * false otherwise
	 */
	boolean check(String player);
	
	/**
	 * Check whether the given player is in stalemate
	 * @param player
	 * @return true if this player's King is not in check, and, for all of this player's 
	 * active pieces, no piece may make a legal move.  Otherwise, return false.
	 */
	boolean stalemate(String player);
	
	/**
	 * Check whether the given player is in checkmate
	 * @param player
	 * @return true if this player's King is in check, and, for all of this player's 
	 * active pieces, no piece may make a legal move.  Otherwise, return false.
	 */
	boolean checkmate(String player);
	
	/**
	 * Move a piece according to the given arguments
	 * @param args
	 * @return true if piece was successfully moved; false if the piece cannot
	 * move to the given position
	 */
	boolean movePiece(String [] args);
	
	/**
	 * Check to see if the given position is within the range of the board
	 * @param position
	 * @return true if move is within the legal boundaries of the board; false
	 * otherwise
	 */
	boolean isMoveWithinRange(String position);
	
	/**
	 * Undo the last move.
	 * @return true if the last move is successfully undone; false if no moves have been made.
	 */
	boolean undo();
	
	/**
	 * Convert from a numeric matrix position to a traditional Rank & File position
	 * representation.
	 * @param col
	 * @param row
	 * @return A String representing the Rank&File corresponding to the numeric
	 * matrix position
	 */
	public String matrixPositionToFileAndRank(int col, int row);
	
	/**
	 * Convert the File position to the matrix column position
	 * @param position
	 * @return int representing the matrix column corresponding to the file position
	 */
	public int fileToColumn(String position);
	
	/**
	 * Convert the Rank position to the matrix row column position
	 * @param position
	 * @return int representing the matrix row corresponding to the rank position;
	 * return -1 if the rank cannot be successfully converted to a row.
	 */
	public int rankToRow(String position);
	
	public List<ChessPiece[][]> getBoardLayouts();
}