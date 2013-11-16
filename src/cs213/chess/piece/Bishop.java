package cs213.chess.piece;

import java.util.LinkedList;
import java.util.List;

import cs213.chess.board.*;

/**
 * Class representing a Bishop chess piece.
 * 
 * @author Thomas Travis
 *
 */
public class Bishop extends ChessPiece{

	public Bishop(String player, String position, Board b){
		super(player, position, b);
		type = "bishop";
		firstMove = true;
	}
	
	public Bishop(String player, int col, int row, Board b){
		super(player, col, row, b);
		type = "bishop";
		firstMove = true;
	}
	
	public List<String> hasAvailableMoves(){
		List<String> a = new LinkedList<String>();
		String s;
		
		int m = Math.abs(this.col - 7);
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
		String s = null;
		ChessPiece q;
		boolean legalMove = true;
		
		if( !this.b.isMoveWithinRange(position) ) return false;
		
		int a = Math.abs(this.row - row);
		int b = Math.abs(this.col - col);
		
		if( col != this.col && row != this.row && a == b ){
			
			if( col > this.col && row > this.row ){
				for( int i = 1; i < a; i++ ){
					s = this.matrixPositionToFileAndRank(this.col+i, this.row+i);
					q = this.b.getPieceAtPosition(s);
					if( q != null ) return false;
				}
			}
			if( col > this.col && row < this.row ){
				for( int i = 1; i < a; i++ ){
					s = this.matrixPositionToFileAndRank(this.col+i, this.row-i);
					q = this.b.getPieceAtPosition(s);
					if( q != null ) return false;
				}
			}
			if( col < this.col && row > this.row ){
				for( int i = 1; i < a; i++ ){
					s = this.matrixPositionToFileAndRank(this.col-i, this.row+i);
					q = this.b.getPieceAtPosition(s);
					if( q != null ) return  false;
				}
			}
			if( col < this.col && row < this.row ){
				for( int i = 1; i < a; i++ ){
					s = this.matrixPositionToFileAndRank(this.col-i, this.row-i);
					q = this.b.getPieceAtPosition(s);
					if( q != null ) return false;
				}
			}
			
			q = this.b.getPieceAtPosition(position);
			if( q != null ){
				if( player.equals(q.getPlayer()) ){
					return false;
				}
			}
			
			if( super.moveEndangersKing(position) ){
				return false;
			}
		}
		else{ legalMove = false; }
		
		return legalMove;
	}
	
	public String toString(){
		if( this.player.equals("white") ) return "Bi-W";
		return "Bi-B";
			
	}
}
