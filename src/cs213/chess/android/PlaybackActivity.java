package cs213.chess.android;

import java.util.List;

import cs213.chess.board.Board;
import cs213.chess.board.ChessBoard;
import cs213.chess.piece.ChessPiece;
import cs213.chess.utils.RecordGame;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;

public class PlaybackActivity extends Activity {

	private String fileName;
	private RecordGame rg;
	private Board b;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_playback);
		fileName = savedInstanceState.getString("fileName");
		rg = RecordGame.getInstance();
		List<String[]> moves = rg.load(fileName);
		b = new ChessBoard();
		b.setMoves(moves);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.playback, menu);
		return true;
	}

	public void back(View view){
		if( b.hasBack() ){
			b.back();
			drawBoard();
		}
	}
	
	public void next(View view){
		if( b.hasNext() ){
			b.next();
			drawBoard();
		}
	}
	
	public void exit(View view){
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}
	
	private void drawBoard(){
		GridLayout gl = (GridLayout)findViewById(R.layout.activity_playback);
		Button view;
		
		for( int row = 0; row < 8; row ++){			
			for( int col = 0; col < 8; col++ ){
				
				ChessPiece p = b.getPieceAtPosition(col, 7-row);
				
				if( p != null ){
					String pos = p.getPosition();
					view = (Button)gl.findViewWithTag(pos);
					String text = pieceName(p);
					view.setText(text);
				}
				else{
					String pos = b.matrixPositionToFileAndRank(col, row);
					view = (Button)gl.findViewWithTag(pos);
					view.setText("");
				}
				
			}
		}
	}
	
	private String pieceName(ChessPiece p){
		if( p.getPlayer().equalsIgnoreCase("white") ){
			if( p.getType().equalsIgnoreCase("Pawn") ) return "1P";
			else if( p.getType().equalsIgnoreCase("King") ) return "1K";
			else if( p.getType().equalsIgnoreCase("Queen") ) return "1Q";
			else if( p.getType().equalsIgnoreCase("Bishop") ) return "1B";
			else if( p.getType().equalsIgnoreCase("Knight") ) return "1N";
			else{ return "1R";}
		}
		else{
			if( p.getType().equalsIgnoreCase("Pawn") ) return "2P";
			else if( p.getType().equalsIgnoreCase("King") ) return "2K";
			else if( p.getType().equalsIgnoreCase("Queen") ) return "2Q";
			else if( p.getType().equalsIgnoreCase("Bishop") ) return "2B";
			else if( p.getType().equalsIgnoreCase("Knight") ) return "2N";
			else{ return "2R"; }
		}
	}
}
