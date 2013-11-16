package cs213.chess.piece;

import java.util.LinkedList;
import java.util.List;

import cs213.chess.board.*;

/**
 * Class representing a Knight chess piece.
 * 
 * @author Thomas Travis
 *
 */
public class Knight extends ChessPiece{

	public Knight(String player, String position, Board b){
		super(player, position, b);
		type = "knight";
		firstMove = true;
	}
	
	public Knight(String player, int col, int row, Board b){
		super(player, col, row, b);
		type = "knight";
		firstMove = true;
	}
	
	public List<String> hasAvailableMoves(){
		List<String> a = new LinkedList<String>();
		String s;
		
		s = this.matrixPositionToFileAndRank(this.col+2, this.row+1);
		if( b.isMoveWithinRange(s) && canMoveTo(s) ) a.add(s);
		
		s = this.matrixPositionToFileAndRank(this.col+2, this.row-1);
		if( b.isMoveWithinRange(s) && canMoveTo(s) ) a.add(s);	
		
		s = this.matrixPositionToFileAndRank(this.col-2, this.row+1);
		if( b.isMoveWithinRange(s) && canMoveTo(s) ) a.add(s);	
		
		s = this.matrixPositionToFileAndRank(this.col-2, this.row-1);
		if( b.isMoveWithinRange(s) && canMoveTo(s) ) a.add(s);	
		
		s = this.matrixPositionToFileAndRank(this.col+1, this.row+2);
		if( b.isMoveWithinRange(s) && canMoveTo(s) ) a.add(s);		
		
		s = this.matrixPositionToFileAndRank(this.col-1, this.row+2);
		if( b.isMoveWithinRange(s) && canMoveTo(s) ) a.add(s);		
		
		s = this.matrixPositionToFileAndRank(this.col+1, this.row-2);
		if( b.isMoveWithinRange(s) && canMoveTo(s) ) a.add(s);		
		
		s = this.matrixPositionToFileAndRank(this.col-1, this.row-2);
		if( b.isMoveWithinRange(s) && canMoveTo(s) ) a.add(s);	
		
		return a;
	}
	
	public boolean canMoveTo(String position){
		
		int col = this.fileToColumn(position);
		int row = this.rankToRow(position);
		
		int a = Math.abs(this.col-col);
		int b = Math.abs(this.row-row);
		
		boolean legalMove = false;
		
		if( !this.b.isMoveWithinRange(position) ) return false;
		
		ChessPiece q = this.b.getPieceAtPosition(position);
		
		if( a == 2 && b == 1 ){
			if( q != null ){
				if( !player.equals(q.getPlayer()) ){
					legalMove = true;
				}
			}
			else{ legalMove = true; }
		}
		if( a == 1 && b == 2 ){
			if( q != null ){
				if( !player.equals(q.getPlayer()) ){
					legalMove = true;
				}
			}
			else{ legalMove = true; }
		}
		
		if( legalMove == true ){
			
			if( super.moveEndangersKing(position) ){
				return false;
			}
		}
		
		return legalMove;
	}
	
	public String toString(){
		if( this.player.equals("white") ) return "Kn-W";
		return "Kn-B";
	}
}
