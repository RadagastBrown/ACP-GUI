package cs213.chess.piece;

import java.util.LinkedList;
import java.util.List;

import cs213.chess.board.*;

/**
 * Class representing a Pawn chess piece.
 * 
 * @author Thomas Travis
 *
 */
public class Pawn extends ChessPiece{
		
	public Pawn(String player, String position, Board b){
		super(player, position, b);
		firstMove = true;
		type = "pawn";
	}
	
	public Pawn(String player, int col, int row, Board b){
		super(player, col, row, b);
		firstMove = true;
		type = "pawn";
	}
	
	public List<String> hasAvailableMoves(){
		List<String> a = new LinkedList<String>();
		String s;
		
		if( this.player.equals("white") ){
			s = this.matrixPositionToFileAndRank(this.col, this.row+1);
			if( b.isMoveWithinRange(s) && canMoveTo(s) ) a.add(s);	
			
			s = this.matrixPositionToFileAndRank(this.col, this.row+2);
			if( b.isMoveWithinRange(s) && canMoveTo(s) ) a.add(s);	
			
			s = this.matrixPositionToFileAndRank(this.col+1, this.row+1);
			if( b.isMoveWithinRange(s) && canMoveTo(s) ) a.add(s);	
			
			s = this.matrixPositionToFileAndRank(this.col-1, this.row+1);
			if( b.isMoveWithinRange(s) && canMoveTo(s) ) a.add(s);	
		}
		else{
			s = this.matrixPositionToFileAndRank(this.col, this.row-1);
			if( b.isMoveWithinRange(s) && canMoveTo(s) ) a.add(s);	
			
			s = this.matrixPositionToFileAndRank(this.col, this.row-2);
			if( b.isMoveWithinRange(s) && canMoveTo(s) ) a.add(s);	
			
			s = this.matrixPositionToFileAndRank(this.col+1, this.row-1);
			if( b.isMoveWithinRange(s) && canMoveTo(s) ) a.add(s);	
			
			s = this.matrixPositionToFileAndRank(this.col-1, this.row-1);
			if( b.isMoveWithinRange(s) && canMoveTo(s) ) a.add(s);	
		}
		
		return a;
	}
	
	public boolean canMoveTo(String position){
		
		if( !this.b.isMoveWithinRange(position) ) return false;
		
		int col = this.fileToColumn(position);
		int row = this.rankToRow(position);
		ChessPiece q;
		boolean legalMove = false;
		
		if( player.equals("white") ){
			
			if( col == this.col && row == this.row+1 ){
				q = this.b.getPieceAtPosition(position);
				if( q == null ){
					legalMove = true;
				}
			}
			if( col == this.col && row == this.row+2 ){
				if( firstMove ){
					String s = this.matrixPositionToFileAndRank(this.col, this.row+1);
					q = this.b.getPieceAtPosition(s);
					if( q == null ){
						q = this.b.getPieceAtPosition(position);
						if( q == null ){
							legalMove = true;
						}
					}
				}
			}
			if( col == this.col+1 && row == this.row+1 ){
				q = this.b.getPieceAtPosition(position);
				if( q != null ){
					if( !this.player.equals( q.getPlayer() ) ){
						legalMove = true;
					}
				}
				else{
					ChessPiece epp = b.getEnPassantPawn();
					if( epp != null ){
						if( !player.equalsIgnoreCase( epp.getPlayer() ) ){
							if( (epp.getCol() == col) && (epp.getRow()+1 == row) ){
								legalMove = true;
							}
						}
					}
				}
			}
			if( col == this.col-1 && row == this.row+1 ){
				q = this.b.getPieceAtPosition(position);
				if( q != null ){
					if( !this.player.equals( q.getPlayer() ) ){
						legalMove = true;
					}
				}
				else{
					ChessPiece epp = b.getEnPassantPawn();
					if( epp != null ){
						if( !player.equalsIgnoreCase( epp.getPlayer() ) ){
							if( (epp.getCol() == col) && (epp.getRow()+1 == row) ){
								legalMove = true;
							}
						}
					}
				}
			}
		}
		else{
			
			if( col == this.col && row == this.row-1 ){
				q = this.b.getPieceAtPosition(position);
				if( q == null ){
					legalMove = true;
				}
			}
			if( col == this.col && row == this.row-2 ){
				if( firstMove ){
					String s = this.matrixPositionToFileAndRank(this.col, this.row-1);
					q = this.b.getPieceAtPosition(s);
					if( q == null ){
						q = this.b.getPieceAtPosition(position);
						if( q == null ){
							legalMove = true;
						}
					}
				}
			}
			if( col == this.col+1 && row == this.row-1 ){
				q = this.b.getPieceAtPosition(position);
				if( q != null ){
					if( !this.player.equals( q.getPlayer() ) ){
						legalMove = true;
					}
				}
				else{
					ChessPiece epp = b.getEnPassantPawn();
					if( epp != null ){
						if( !player.equalsIgnoreCase( epp.getPlayer() ) ){
							if( (epp.getCol() == col) && (epp.getRow()-1 == row) ){
								legalMove = true;
							}
						}
					}
				}
			}
			if( col == this.col-1 && row == this.row-1 ){
				q = this.b.getPieceAtPosition(position);
				if( q != null ){
					if( !this.player.equals( q.getPlayer() ) ){
						legalMove = true;
					}
				}
				else{
					ChessPiece epp = b.getEnPassantPawn();
					if( epp != null ){
						if( !player.equalsIgnoreCase( epp.getPlayer() ) ){
							if( (epp.getCol() == col) && (epp.getRow()-1 == row) ){
								legalMove = true;
							}
						}
					}
				}
			}
		}
		
		if( legalMove == true ){
			
			if( super.moveEndangersKing(position) ){
				return false;
			}
		}
	
		return legalMove;
	}
	
	public String toString(){
		if( this.player.equals("white") ) return "Pn-W";
		return "Pn-B";
	}
}
