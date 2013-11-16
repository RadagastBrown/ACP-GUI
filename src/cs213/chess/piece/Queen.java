package cs213.chess.piece;

import java.util.LinkedList;
import java.util.List;

import cs213.chess.board.*;

/**
 * Class representing a Queen chess piece.
 * 
 * @author Thomas Travis
 *
 */
public class Queen extends ChessPiece{

	public Queen(String player, String position, Board b){
		super(player, position, b);
		type = "queen";
		firstMove = true;
	}
	
	public Queen(String player, int col, int row, Board b){
		super(player, col, row, b);
		type = "queen";
		firstMove = true;
	}
	
	public List<String> hasAvailableMoves(){
		List<String> a = new LinkedList<String>();
		String s;
		
		//Check for horizontal/linear moves
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
		
		//Check for Diagonal Moves
		m = Math.abs(this.col - 7);
		int n = Math.abs(this.row - 7);
		for( int i = 1; (i <= m) || (i <= n); i++ ){
			s = this.matrixPositionToFileAndRank(this.col+i, this.row+i);
			if( b.isMoveWithinRange(s) && canMoveTo(s) ) a.add(s);	
		}
		
		m = Math.abs(this.col - 7);
		n = Math.abs(this.row - 0);
		for( int i = 1; (i <= m) || (i <= n); i++ ){
			s = this.matrixPositionToFileAndRank(this.col+i, this.row-i);
			if( b.isMoveWithinRange(s) && canMoveTo(s) ) a.add(s);	
		}
		
		m = Math.abs(this.col - 0);
		n = Math.abs(this.row - 0);
		for( int i = 1; (i <= m) || (i <= n); i++ ){
			s = this.matrixPositionToFileAndRank(this.col-i, this.row-i);
			if( b.isMoveWithinRange(s) && canMoveTo(s) ) a.add(s);	
		}
		
		m = Math.abs(this.col - 0);
		n = Math.abs(this.col - 7);
		for( int i = 1; (i <= m) || (i <= n); i++ ){
			s = this.matrixPositionToFileAndRank(this.col-i, this.row+i);
			if( b.isMoveWithinRange(s) && canMoveTo(s) ) a.add(s);	
		}
		
		return a;
	}
	
	public boolean canMoveTo(String position){
		int col = this.fileToColumn(position);
		int row = this.rankToRow(position);
		int distance;
		int a = Math.abs(this.row - row);
		int b = Math.abs(this.col - col);
		
		String s = null;
		ChessPiece q;
		
		if( !this.b.isMoveWithinRange(position) ) return false;
		
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
		else if( col != this.col && row != this.row && a == b ){
			distance = a;
			
			if( col > this.col && row > this.row ){
				for( int i = 1; i < distance; i++ ){
					s = this.matrixPositionToFileAndRank(this.col+i, this.row+i);
					q = this.b.getPieceAtPosition(s);
					if( q != null ) return false;
				}
			}
			if( col > this.col && row < this.row ){
				for( int i = 1; i < distance; i++ ){
					s = this.matrixPositionToFileAndRank(this.col+i, this.row-i);
					q = this.b.getPieceAtPosition(s);
					if( q != null ) return false;
				}
			}
			if( col < this.col && row > this.row ){
				for( int i = 1; i < distance; i++ ){
					s = this.matrixPositionToFileAndRank(this.col-i, this.row+i);
					q = this.b.getPieceAtPosition(s);
					if( q != null ) return false;
				}
			}
			if( col < this.col && row < this.row ){
				for( int i = 1; i < distance; i++ ){
					s = this.matrixPositionToFileAndRank(this.col-i, this.row-i);
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
			if( this.player.equals(q.getPlayer()) ){
				return false;
			}
		}
		
		if( super.moveEndangersKing(position) ) return false;
		return true;
	}
	
	public String toString(){
		if( this.player.equals("white") ) return "Qn-W";
		return "Qn-B";
	}
}
