package cs213.chess.piece;

import java.util.LinkedList;
import java.util.List;

import cs213.chess.board.*;

/**
 * Class representing a Rook chess piece.
 * 
 * @author Thomas Travis
 *
 */
public class Rook extends ChessPiece{

	public Rook(String player, String position, Board b){
		super(player, position, b);
		firstMove = true;
		type = "rook";
	}
	
	public Rook(String player, int col, int row, Board b){
		super(player, col, row, b);
		firstMove = true;
		type = "rook";
	}
	
	public List<String> hasAvailableMoves(){
		List<String> a = new LinkedList<String>();
		String s;
		
		int m = Math.abs(this.col - 0);
		if( m > 0 ){
			for( int i = 1; i <= m; i++ ){
				s = this.matrixPositionToFileAndRank(this.col-i, this.row);
				if( b.isMoveWithinRange(s) && canMoveTo(s) ) a.add(s);	
			}
		}
		
		m = Math.abs(this.col - 7);
		if( m > 0 ){
			for( int i = 1; i <= m; i++ ){
				s = this.matrixPositionToFileAndRank(this.col+i, this.row);
				if( b.isMoveWithinRange(s) && canMoveTo(s) ) a.add(s);	
			}
		}
		
		m = Math.abs(this.row - 0);
		if( m > 0 ){
			for( int i = 1; i <= m; i++ ){
				s = this.matrixPositionToFileAndRank(this.col, this.row-i);
				if( b.isMoveWithinRange(s) && canMoveTo(s) ) a.add(s);	
			}
		}
		
		m = Math.abs(this.row - 7);
		if( m > 0 ){
			for( int i = 1; i <= m; i++ ){
				s = this.matrixPositionToFileAndRank(this.col, this.row+i);
				if( b.isMoveWithinRange(s) && canMoveTo(s) ) a.add(s);	
			}
		}
		
		return a;
	}
	
	public boolean canMoveTo(String position){
		
		int col = this.fileToColumn(position);
		int row = this.rankToRow(position);
		int distance;
		
		if( !this.b.isMoveWithinRange(position) ) return false;

		String s = null;
		ChessPiece q;
		
		if( col == this.col && row != this.row ){
			distance = Math.abs(this.row-row);
			
			if( row > this.row ){
				for( int i = 1; i < distance; i++ ){
					s = this.matrixPositionToFileAndRank(this.col, this.row+i);
					q = this.b.getPieceAtPosition(s);
					if( q != null ) return false;
				}
			}
			if( row < this.row ){
				for( int i = 1; i < distance; i++ ){
					s = this.matrixPositionToFileAndRank(this.col, this.row-i);
					q = this.b.getPieceAtPosition(s);
					if( q != null ) return false;
				}
			}
		}
		else if( col != this.col && row == this.row ){
			distance = Math.abs(this.col-col);
			
			if( col > this.col ){
				for( int i = 1; i < distance; i++ ){
					s = this.matrixPositionToFileAndRank(this.col+i, this.row);
					q = this.b.getPieceAtPosition(s);
					if( q != null ) return false;
				}
			}
			if( col < this.col ){
				for( int i = 1; i < distance; i++ ){
					s = this.matrixPositionToFileAndRank(this.col-i, this.row);
					q = this.b.getPieceAtPosition(s);
					if( q != null ) return false;
				}
			}
		}
		else{
			return false;
		}
		
		q = this.b.getPieceAtPosition(position);
		if( q != null ){
			if( player.equals(q.getPlayer()) ){
				return false;
			}
		}
		
		if( super.moveEndangersKing(position) ) return false;
		return true;
	}
	
	public String toString(){
		if( this.player.equals("white") ) return "Rk-W";
		return "Rk-B";
	}
}
