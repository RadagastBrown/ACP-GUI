package cs213.chess.piece;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import cs213.chess.board.*;

/**
 * Class representing a King chess piece.
 * 
 * @author Thomas Travis
 *
 */
public class King extends ChessPiece{
		
	public King(String player, String position, Board b){
		super(player, position, b);
		firstMove = true;
		type = "king";
	}
	
	public King(String player, int col, int row, Board b){
		super(player, col, row, b);
		firstMove = true;
		type = "king";
	}
	
	/**
	 * State whether this King will be checked if it is in the given position
	 * 
	 * @param position
	 * @return true if this King will be checked in the given position;
	 * false otherwise
	 */
	public boolean isChecked(String position, List<ChessPiece> cp){
		//List<ChessPiece> cp;
		//if( player.equals("white") ) cp = new LinkedList<ChessPiece>( b.getPieces("black") );
		//else{ cp = new LinkedList<ChessPiece>( b.getPieces("white") ); }
		
		int startCol = this.col;
		int startRow = this.row;
		int endCol = this.fileToColumn(position);
		int endRow = this.rankToRow(position);
		
		ChessPiece thisPiece = this.b.getPieceAtPosition(this.getPosition());
		ChessPiece q = this.b.getPieceAtPosition(position);
		
		ChessPiece [][] board = this.b.getBoard();
		board[startCol][startRow] = null;
		board[endCol][endRow] = thisPiece;
		
		Iterator<ChessPiece> i = cp.iterator();
		ChessPiece p;
		boolean b = false;
		
		while( i.hasNext() ){
			p = i.next();
			if( p.canMoveTo(position) ) b = true;
		}
		
		board[startCol][startRow] = thisPiece;
		board[endCol][endRow] = q;
		
		return b;
	}
	
	public List<String> hasAvailableMoves(){
		List<String> a = new LinkedList<String>();
		String s;
		
		s = this.matrixPositionToFileAndRank(this.col, this.row+1);
		if( b.isMoveWithinRange(s) && canMoveTo(s) ) a.add(s);	
		
		s = this.matrixPositionToFileAndRank(this.col, this.row-1);
		if( b.isMoveWithinRange(s) && canMoveTo(s) ) a.add(s);	
		
		s = this.matrixPositionToFileAndRank(this.col+1, this.row);
		if( b.isMoveWithinRange(s) && canMoveTo(s) ) a.add(s);	
		
		s = this.matrixPositionToFileAndRank(this.col-1, this.row);
		if( b.isMoveWithinRange(s) && canMoveTo(s) ) a.add(s);	
		
		s = this.matrixPositionToFileAndRank(this.col+1, this.row+1);
		if( b.isMoveWithinRange(s) && canMoveTo(s) ) a.add(s);	
		
		s = this.matrixPositionToFileAndRank(this.col+1, this.row-1);
		if( b.isMoveWithinRange(s) && canMoveTo(s) ) a.add(s);	
		
		s = this.matrixPositionToFileAndRank(this.col-1, this.row+1);
		if( b.isMoveWithinRange(s) && canMoveTo(s) ) a.add(s);	
		
		s = this.matrixPositionToFileAndRank(this.col-1, this.row-1);
		if( b.isMoveWithinRange(s) && canMoveTo(s) ) a.add(s);	
		
		s = this.matrixPositionToFileAndRank(this.col-2, this.row);
		if( b.isMoveWithinRange(s) && canMoveTo(s) ) a.add(s);	
		
		s = this.matrixPositionToFileAndRank(this.col+2, this.row);
		if( b.isMoveWithinRange(s) && canMoveTo(s) ) a.add(s);	
		
		return a;
	}
	
	public boolean canMoveTo(String position){
		int col = this.fileToColumn(position);
		int row = this.rankToRow(position);
		int a = Math.abs(this.col - col);
		int b = Math.abs(this.row - row);
		
		if( !this.b.isMoveWithinRange(position) ) return false;
		
		ChessPiece q = this.b.getPieceAtPosition(position);
		
		List<ChessPiece> cp;
		if( player.equals("white") ){
			cp = new LinkedList<ChessPiece>( this.b.getPieces("black"));
		}
		else{
			cp = new LinkedList<ChessPiece>( this.b.getPieces("white") );
		}
		
		if( q != null ){
			if( (a == 0 && b == 1) || (a == 1 && b == 0) || (a == 1 && b == 1) ){
				if( !player.equals(q.getPlayer()) ){
					if( !this.isChecked(position, cp) ){
						return true;
					}
				}
			}
		}
		else{
			if( (a == 0 && b == 1) || (a == 1 && b == 0) || (a == 1 && b == 1) ){
				if( !this.isChecked(position, cp) ){
					return true;
				}
			}
			if( col == this.col-2 && row == this.row ){
				if( this.isChecked(this.getPosition(), cp) ) return false;
				if( firstMove ){
					String t;
					for( int i = 1; i<= 3; i++ ){
						t = this.matrixPositionToFileAndRank(this.col-i, row);
						if( this.b.getPieceAtPosition(t) != null ) return false;
					}
					t = this.matrixPositionToFileAndRank(this.col-4, row);
					ChessPiece p = this.b.getPieceAtPosition(t);
					if( p != null && p.getType().equals("rook") ){
						if( player.equals(p.getPlayer()) ){
							if( !p.hasMoved() ){
								if( !this.isChecked(position, cp) ){
									return true;
								}
							}
						}
					}
				}
			}
			if( col == this.col+2 && row == this.row ){
				if( this.isChecked(this.getPosition(), cp) ) return false;
				if( firstMove ){
					String t;
					for( int i = 1; i <= 2; i++ ){
						t = this.matrixPositionToFileAndRank(this.col+i, row);
						if( this.b.getPieceAtPosition(t) != null ) return false;
					}
					t = this.matrixPositionToFileAndRank(this.col+3, row);
					ChessPiece p = this.b.getPieceAtPosition(t);
					if( p != null && p.getType().equals("rook") ){
						if( player.equals(p.getPlayer()) ){
							if( !p.hasMoved() ){
								if( !this.isChecked(position, cp) ){
									return true;
								}
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	public String toString(){
		if( this.player.equals("white") ) return "Kg-W";
		return "Kg-B";
	}
}
