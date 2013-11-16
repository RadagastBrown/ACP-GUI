package cs213.chess.android;

import java.util.List;

import cs213.chess.board.*;
import cs213.chess.piece.ChessPiece;
import cs213.chess.utils.RecordGame;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PlayActivity extends Activity {

	private static Board b = null;
	private static boolean firstSelection;
	private static View [] location;
	private static String player;
	private static boolean madeMove;
	private static String textHolder;
	private static TextView status;
	private static boolean gameOver;
	private static String winner;
	
	private static boolean blackKingMoved;
	private static boolean whiteKingMoved;
	private static boolean castleIndication;
	private static boolean enPassantOccurred;
	private static String enPassantLocation;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.play, menu);
		return true;
	}
	
	@Override
	protected void onStart(){
		super.onStart();
		
		b = new ChessBoard();
		b.startNewGame(b);
		firstSelection = true;
		location = new View[2];
		player = "white";
		
		gameOver = false;
		blackKingMoved = false;
		whiteKingMoved = false;
		castleIndication = false;
		madeMove = false;
		textHolder = null;
		
		int statusID = getResources().getIdentifier("text", "id", "cs213.chess.android");
		status = (TextView) findViewById(statusID);
		status.setText("Turn: Player 1       Piece Selected: ");
	}
	
	@Override
	protected void onResume(){
		super.onResume();
	}

	@Override
	protected void onPause(){
		super.onPause();
	}
	
	@Override
	protected void onStop(){
		super.onStop();		
	}
	
	public void onSelect(View view){
		
		if (!gameOver){
			String position = (String)view.getTag();
			
			if(b.getPieceAtPosition(position) == null && firstSelection){
				return;
			}
			
			if(firstSelection){
				ChessPiece currentPiece = b.getPieceAtPosition(position);

				if (player.equals("white") && currentPiece.getPlayer().equals("black")){
					Toast.makeText(PlayActivity.this, "It is currently Player 1's turn.  You may only move your own pieces.", Toast.LENGTH_SHORT).show();
					return;
				}
				else if(player.equals("black") && currentPiece.getPlayer().equals("white")){
					Toast.makeText(PlayActivity.this, "It is currently Player 2's turn.  You may only move your own pieces.", Toast.LENGTH_SHORT).show();
					return;
				}
				
				location[0] = view;
				firstSelection = false;
				
				if (player.equals("white")){
					status.setText("Turn: Player 1       Piece Selected: " + position);
				}
				else{
					status.setText("Turn: Player 2       Piece Selected: " + position);
				}
			}
			else{
				location[1] = view;
				move();
				firstSelection = true;
			}
		}
	}
	
	/**
	 * Visually move pieces on the board without committing any changes.
	 * @param args
	 */
	private void move(){
		String firstLocation = (String) location[0].getTag();
		String secondLocation = (String) location[1].getTag();
		
		if (b.getPieceAtPosition(firstLocation).canMoveTo(secondLocation)){
			int firstID = location[0].getId();
			int secondID = location[1].getId();
			
			Button originalLocation = (Button)findViewById(firstID);
			Button desiredLocation = (Button)findViewById(secondID);
			
			textHolder = (String)desiredLocation.getText();
			desiredLocation.setText(originalLocation.getText());
			originalLocation.setText(null);
			madeMove = true;
			
			//Checks for castling
			checkCastling(firstLocation, secondLocation);
			
			//Checks for en Passant
			checkEnPassant(firstLocation, secondLocation);
		}
		else{
			List<String> availableMoves = b.getPieceAtPosition(firstLocation).hasAvailableMoves();
			String moves = "";
			
			for (int i = 0; i < availableMoves.size(); i++){
				moves = moves + availableMoves.get(i) + " ";
			}
			
			moves = moves.trim();
			
			if (availableMoves.size() == 0){
				if (b.check(player)){
					Toast.makeText(PlayActivity.this, "Your king is currently checked!", Toast.LENGTH_SHORT).show();
				}
				else{				
					Toast.makeText(PlayActivity.this, "There are no available moves for the piece at " + firstLocation + ".", Toast.LENGTH_SHORT).show();
				}
					
				if (player.equalsIgnoreCase("white")){
					status.setText("Turn: Player 1       Piece Selected: ");
				}
				else{
					status.setText("Turn: Player 2       Piece Selected: ");
				}
				return;
			}
			else{
				Toast.makeText(PlayActivity.this, "Invalid move for piece at " + firstLocation + ".  Available moves: " + moves + ".", Toast.LENGTH_SHORT).show();
				if (player.equalsIgnoreCase("white")){
					status.setText("Turn: Player 1       Piece Selected: ");
				}
				else{
					status.setText("Turn: Player 2       Piece Selected: ");
				}
				return;
			}
		}
	}
	
	private void checkEnPassant(String firstLocation, String secondLocation){
		
		if (player.equalsIgnoreCase("white") && firstLocation.charAt(1) == '5' &&
				b.getPieceAtPosition(firstLocation).getType().equalsIgnoreCase("pawn") &&
				b.getPieceAtPosition(secondLocation) == null){
			if (firstLocation.charAt(0) != secondLocation.charAt(0)){
				String file = Character.toString(secondLocation.charAt(0));
				int rank = 5;
				String enPassant = file + rank;
				
				if (b.getPieceAtPosition(enPassant).getType().equalsIgnoreCase("pawn")){
					enPassantOccurred = true;
					enPassantLocation = enPassant;
					int id = getResources().getIdentifier(enPassant, "id", "cs213.chess.android");
					Button enPassantPawn = (Button) findViewById(id);
					enPassantPawn.setText(null);
				}
								
			}
		}
		else if (player.equalsIgnoreCase("black") && firstLocation.charAt(1) == '4' &&
				b.getPieceAtPosition(firstLocation).getType().equalsIgnoreCase("pawn") &&
				b.getPieceAtPosition(secondLocation) == null){
			if (firstLocation.charAt(0) != secondLocation.charAt(0)){
				String file = Character.toString(secondLocation.charAt(0));
				int rank = 4;
				String enPassant = file + rank;
				
				if (b.getPieceAtPosition(enPassant).getType().equalsIgnoreCase("pawn")){
					enPassantOccurred = true;
					enPassantLocation = enPassant;
					int id = getResources().getIdentifier(enPassant, "id", "cs213.chess.android");
					Button enPassantPawn = (Button) findViewById(id);
					enPassantPawn.setText(null);
				}	
			}
		}
	}
	
	private void checkCastling(String firstLocation, String secondLocation){
		if (firstLocation.equalsIgnoreCase("e8") && blackKingMoved == false){
			blackKingMoved = true;
			if (secondLocation.equalsIgnoreCase("c8")){
				int id1 = getResources().getIdentifier("a8", "id", "cs213.chess.android");
				int id2 = getResources().getIdentifier("d8", "id", "cs213.chess.android");
				
				Button uncastledRook = (Button) findViewById(id1);
				Button castledRook = (Button) findViewById(id2);
				
				uncastledRook.setText(null);
				castledRook.setText("2R");
			}
			else if (secondLocation.equalsIgnoreCase("g8")){
				int id1 = getResources().getIdentifier("h8", "id", "cs213.chess.android");
				int id2 = getResources().getIdentifier("f8", "id", "cs213.chess.android");
				
				Button uncastledRook = (Button) findViewById(id1);
				Button castledRook = (Button) findViewById(id2);
				
				uncastledRook.setText(null);
				castledRook.setText("2R");
			}
			castleIndication = true;
		}
		else if (firstLocation.equalsIgnoreCase("e1") && whiteKingMoved == false){
			whiteKingMoved = true;
			if (secondLocation.equalsIgnoreCase("c1")){
				int id1 = getResources().getIdentifier("a1", "id", "cs213.chess.android");
				int id2 = getResources().getIdentifier("d1", "id", "cs213.chess.android");
				
				Button uncastledRook = (Button) findViewById(id1);
				Button castledRook = (Button) findViewById(id2);
				
				uncastledRook.setText(null);
				castledRook.setText("1R");
			}
			else if (secondLocation.equalsIgnoreCase("g1")){
				int id1 = getResources().getIdentifier("h1", "id", "cs213.chess.android");
				int id2 = getResources().getIdentifier("f1", "id", "cs213.chess.android");
				
				Button uncastledRook = (Button) findViewById(id1);
				Button castledRook = (Button) findViewById(id2);
				
				uncastledRook.setText(null);
				castledRook.setText("1R");
			}
			castleIndication = true;
		}
	}
	
	/**
	 * Commit the current move, and proceed to the next player's round.
	 */
	public void endTurn(View view){
		if (!gameOver){
			if (madeMove == true){
				String firstLocation;
				String secondLocation;
				
				String[] twoLocations = new String[2];
				firstLocation = (String)location[0].getTag();
				secondLocation = (String)location[1].getTag();
				
				twoLocations[0] = firstLocation;
				twoLocations[1] = secondLocation;
				
				b.movePiece(twoLocations);
				
				location[0] = null;
				location[1] = null;
				madeMove = false;
				textHolder = null;
	
				if(player.equalsIgnoreCase("white") ){
					
					// Checks for promotion for white piece
					checkPromotion(firstLocation, secondLocation);					
					
					player = "black";
					status.setText("Turn: Player 2       Piece Selected: ");
					
					if (b.checkmate(player)){
						status.setText("Player 2 checkmated!  Player 1 wins.");
						Toast.makeText(PlayActivity.this,  "Player 2 checkmated!  Player 1 wins.", Toast.LENGTH_SHORT).show();
						winner = "Player 1";
						gameOver = true;
						saveAsk();
						return;
					}
					if(b.check(player)){
						status.setText("Player 2 checked!");
						Toast.makeText(PlayActivity.this,  "Player 2 checked!", Toast.LENGTH_SHORT).show();
						return;
					}
					if(b.stalemate(player)){
						status.setText("Player 2 stalemated!  Player 1 wins.");
						Toast.makeText(PlayActivity.this,  "Player 2 stalemated!  Player 1 wins.", Toast.LENGTH_SHORT).show();
						winner = "Player 1";
						gameOver = true;
						saveAsk();
						return;
					}		
				}
				else{
					
					// Checks for promotion for black piece
					checkPromotion(firstLocation, secondLocation);
					player = "white";
					status.setText("Turn: Player 1       Piece Selected: ");
					
					if (b.checkmate(player)){
						status.setText("Player 1 checkmated!  Player 2 wins.");
						Toast.makeText(PlayActivity.this,  "Player 1 checkmated!  Player 2 wins.", Toast.LENGTH_SHORT).show();
						winner = "Player 2";
						gameOver = true;
						saveAsk();
						return;
					}
					if(b.check(player)){
						status.setText("Player 1 checked!");
						Toast.makeText(PlayActivity.this,  "Player 1 checked!", Toast.LENGTH_SHORT).show();
						return;
					}
					if(b.stalemate(player)){
						status.setText("Player 1 stalemated!  Player 2 wins.");
						Toast.makeText(PlayActivity.this,  "Player 1 stalemated!  Player 2 wins.", Toast.LENGTH_SHORT).show();
						winner = "Player 2";
						gameOver = true;
						saveAsk();
						return;
					}		
				}
			}
			else{
				if (player.equalsIgnoreCase("white")){
					Toast.makeText(PlayActivity.this, "Player 1 has not made a move!", Toast.LENGTH_SHORT).show();
				}
				else{
					Toast.makeText(PlayActivity.this, "Player 2 has not made a move!", Toast.LENGTH_SHORT).show();
				}
				return;
			}
		}
	}
	
	private void checkPromotion(String firstLocation, String secondLocation){
		if (b.getPieceAtPosition(secondLocation).getType().equalsIgnoreCase("pawn")){
			int id = getResources().getIdentifier(secondLocation, "id", "cs213.chess.android");
			final Button pawnPromotion = (Button) findViewById(id);
			
			final CharSequence[] pieces = new CharSequence[4];
			pieces[0] = "Queen";
			pieces[1] = "Rook";
			pieces[2] = "Bishop";
			pieces[3] = "Knight";
			
			if (secondLocation.charAt(1) == '8'){
				AlertDialog.Builder promotionChoices = new AlertDialog.Builder(this);
				promotionChoices.setCancelable(false);
				promotionChoices.setTitle("Player 1 Pawn Promotion");
				promotionChoices.setItems(pieces, new DialogInterface.OnClickListener() {
		               public void onClick(DialogInterface dialog, int which) {
		            	   if (pieces[which].toString().equalsIgnoreCase("queen")){
		            		   pawnPromotion.setText("1Q");
		            	   }
		            	   else if(pieces[which].toString().equalsIgnoreCase("rook")){
		            		   pawnPromotion.setText("1R");
		            	   }
		            	   else if(pieces[which].toString().equalsIgnoreCase("bishop")){
		            		   pawnPromotion.setText("1B");
		            	   }
		            	   else{
		            		   pawnPromotion.setText("1N");
		            	   }
		               }
				});
				
			}
			else if (secondLocation.charAt(1) == '1'){
				AlertDialog.Builder promotionChoices = new AlertDialog.Builder(this);
				promotionChoices.setCancelable(false);
				promotionChoices.setTitle("Player 2 Pawn Promotion");
				promotionChoices.setItems(pieces, new DialogInterface.OnClickListener() {
		               public void onClick(DialogInterface dialog, int which) {
		            	   if (pieces[which].toString().equalsIgnoreCase("queen")){
		            		   pawnPromotion.setText("2Q");
		            	   }
		            	   else if(pieces[which].toString().equalsIgnoreCase("rook")){
		            		   pawnPromotion.setText("2R");
		            	   }
		            	   else if(pieces[which].toString().equalsIgnoreCase("bishop")){
		            		   pawnPromotion.setText("2B");
		            	   }
		            	   else{
		            		   pawnPromotion.setText("2N");
		            	   }
		               }
				});
			}
			
			return;
		}
	}
	
	public void AI(View view){
		if (!gameOver){
			if (player.equalsIgnoreCase("white") && madeMove == false){
				List<ChessPiece> whitePieces = b.getPieces(player);
				
				int min = 0;
				int max = whitePieces.size()-1;
				int randomSelection = min + (int)(Math.random() * ((max - min) + 1));			
				ChessPiece randomPiece = whitePieces.get(randomSelection);
				
				while (randomPiece.hasAvailableMoves().size() == 0){
					min = 0;
					max = whitePieces.size()-1;
					randomSelection = min + (int)(Math.random() * ((max - min) + 1));
					
					randomPiece = whitePieces.get(randomSelection);
				}
				
				String randomPiecePosition = randomPiece.getPosition();
				List<String> availableMoves = randomPiece.hasAvailableMoves();
				
				int min2 = 0;
				int max2 = availableMoves.size()-1;
				randomSelection = min2 + (int)(Math.random() * ((max2 - min2) + 1));			
				String randomMove = availableMoves.get(randomSelection);
				
				String firstLocation = randomPiecePosition;
				String secondLocation = randomMove;
				
				int id1 = getResources().getIdentifier(firstLocation, "id", "cs213.chess.android");
				int id2 = getResources().getIdentifier(secondLocation, "id", "cs213.chess.android");
				
				Button location1 = (Button) findViewById(id1);
				Button location2 = (Button) findViewById(id2);
				
				location2.setText(location1.getText());
				location1.setText(null);
				
				//Checks for AI castling
				checkCastling(firstLocation, secondLocation);
				
				//Checks for enPassant
				checkEnPassant(firstLocation, secondLocation);
				
				madeMove = true;
				location[0] = location1;
				location[1] = location2;
				
				int endTurnID = getResources().getIdentifier("end_turn", "id", "cs213.chess.android");
				Button endTurnButton = (Button) findViewById(endTurnID);
				endTurn(endTurnButton);
				return;
			}
			else if (player.equalsIgnoreCase("black") && madeMove == false){
				List<ChessPiece> blackPieces = b.getPieces(player);
	
				int min = 0;
				int max = blackPieces.size()-1;
				int randomSelection = min + (int)(Math.random() * ((max - min) + 1));			
				ChessPiece randomPiece = blackPieces.get(randomSelection);
				
				while (randomPiece.hasAvailableMoves().size() == 0){
					min = 0;
					max = blackPieces.size();
					randomSelection = min + (int)(Math.random() * ((max - min) + 1));
					
					randomPiece = blackPieces.get(randomSelection);				
				}
				
				String randomPiecePosition = randomPiece.getPosition();
				List<String> availableMoves = randomPiece.hasAvailableMoves();
				
				int min2 = 0;
				int max2 = availableMoves.size()-1;
				randomSelection = min2 + (int)(Math.random() * ((max2 - min2) + 1));			
				String randomMove = availableMoves.get(randomSelection);
				
				String firstLocation = randomPiecePosition;
				String secondLocation = randomMove;
				
				int id1 = getResources().getIdentifier(firstLocation, "id", "cs213.chess.android");
				int id2 = getResources().getIdentifier(secondLocation, "id", "cs213.chess.android");
				
				Button location1 = (Button) findViewById(id1);
				Button location2 = (Button) findViewById(id2);
				
				location2.setText(location1.getText());
				location1.setText(null);
				
				//Checks for castling
				checkCastling(firstLocation, secondLocation);
				
				//Checks for enPassant
				checkEnPassant(firstLocation, secondLocation);
				
				madeMove = true;
				location[0] = location1;
				location[1] = location2;
				
				int endTurnID = getResources().getIdentifier("end_turn", "id", "cs213.chess.android");
				Button endTurnButton = (Button) findViewById(endTurnID);
				endTurn(endTurnButton);
				return;
			}
			else{
				Toast.makeText(PlayActivity.this, "The AI button may only be used when a move has not yet been made.", Toast.LENGTH_LONG).show();
				return;
			}
		}
	}
	
	/**
	 * Visually update the board to before the last uncommitted move.
	 */
	public void undo(View view){
		if (!gameOver){
			if (madeMove == true){
				int firstID = location[0].getId();
				int secondID = location[1].getId();
				
				Button originalLocation = (Button)findViewById(firstID);
				Button currentLocation = (Button)findViewById(secondID);
				
				originalLocation.setText(currentLocation.getText());
				currentLocation.setText(textHolder);
				
				// Undo castling
				if (castleIndication == true){
					String location = (String)currentLocation.getTag();
					if (location.equalsIgnoreCase("c8")){
						int id1 = getResources().getIdentifier("d8", "id", "cs213.chess.android");
						int id2 = getResources().getIdentifier("a8", "id", "cs213.chess.android");
						
						Button castledRook = (Button) findViewById(id1);
						Button uncastledRook = (Button) findViewById(id2);
						
						castledRook.setText(null);
						uncastledRook.setText("2R");
						blackKingMoved = false;
					}
					else if(location.equalsIgnoreCase("g8")){
						int id1 = getResources().getIdentifier("f8", "id", "cs213.chess.android");
						int id2 = getResources().getIdentifier("h8", "id", "cs213.chess.android");
						
						Button castledRook = (Button) findViewById(id1);
						Button uncastledRook = (Button) findViewById(id2);
						
						castledRook.setText(null);
						uncastledRook.setText("2R");
						blackKingMoved = false;
					}
					else if(location.equalsIgnoreCase("c1")){
						int id1 = getResources().getIdentifier("d1", "id", "cs213.chess.android");
						int id2 = getResources().getIdentifier("a1", "id", "cs213.chess.android");
						
						Button castledRook = (Button) findViewById(id1);
						Button uncastledRook = (Button) findViewById(id2);
						
						castledRook.setText(null);
						uncastledRook.setText("1R");
						whiteKingMoved = false;
					}
					else{
						int id1 = getResources().getIdentifier("f1", "id", "cs213.chess.android");
						int id2 = getResources().getIdentifier("h1", "id", "cs213.chess.android");
						
						Button castledRook = (Button) findViewById(id1);
						Button uncastledRook = (Button) findViewById(id2);
						
						castledRook.setText(null);
						uncastledRook.setText("1R");
						whiteKingMoved = false;
					}
					castleIndication = false;
					return;
				}
				
				//Undo enPassant
				if (enPassantOccurred == true){
					int passantID = getResources().getIdentifier(enPassantLocation, "id", "cs213.chess.android");
					Button passantButton = (Button) findViewById(passantID);
					if (player.equalsIgnoreCase("white")){
						passantButton.setText("2P");
					}
					else{
						passantButton.setText("1P");
					}					
					enPassantOccurred = false;
					return;
				}
				
				madeMove = false;
				if (player.equalsIgnoreCase("white")){
					status.setText("Turn: Player 1       Piece Selected: ");
				}
				else{
					status.setText("Turn: Player 2       Piece Selected: ");
				}
				return;
			}
			else{
				if (player.equalsIgnoreCase("white")){
					Toast.makeText(PlayActivity.this, "Player 1 has not made a move yet!  " +
							"You may undo after making a move and before touching End Turn.", Toast.LENGTH_LONG).show();
				}
				else{
					Toast.makeText(PlayActivity.this, "Player 2 has not made a move yet!  " +
							"You may undo after making a move and before touching End Turn.", Toast.LENGTH_LONG).show();
				}
				return;
			}
		}
	}
	
	public void resign(View view){
		if (!gameOver){
			AlertDialog.Builder resignAlert = new AlertDialog.Builder(this);
			
			if (player.equalsIgnoreCase("white")){
				resignAlert.setTitle("Player 1");
			}
			else{
				resignAlert.setTitle("Player 2");
			}
			
			resignAlert.setNegativeButton("Cancel", null);		
			resignAlert.setPositiveButton("Ok",
					new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int which){
					if (player.equalsIgnoreCase("white")){
						status.setText("Player 1 resigns.  Player 2 wins!");
						winner = "Player 2";
						gameOver = true;
						saveAsk();
					}
					else{
						status.setText("Player 2 resigns.  Player 1 wins!");
						winner = "Player 1";
						gameOver = true;
						saveAsk();
					}
				}
			});
			
			resignAlert.setMessage("Are you sure that you want to resign?");
			
			resignAlert.create().show();
		}
	}
	
	public void draw(View view){
		if (!gameOver){
			AlertDialog.Builder drawAlert = new AlertDialog.Builder(this);
			
			drawAlert.setNegativeButton("Cancel", null);		
			drawAlert.setPositiveButton("Ok",
				new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dialog, int which){
						status.setText("The game ends in a draw!");
						winner = "Draw";
						gameOver = true;
						saveAsk();
					}
			});
					
			drawAlert.setCancelable(true);
			
			if (player.equalsIgnoreCase("white")){
				drawAlert.setMessage("Player 1 requests a draw.  Do you accept?");
				drawAlert.setTitle("Player 2");
			}
			else{
				drawAlert.setMessage("Player 2 requests a draw.  Do you accept?");
				drawAlert.setTitle("Player 1");
			}
			
			drawAlert.create().show();
		}
	}
	
	private void saveAsk(){
		AlertDialog.Builder saveAlert = new AlertDialog.Builder(this);
		saveAlert.setCancelable(true);
		saveAlert.setTitle("Save Game?");
		saveAlert.setMessage("Do you wish to save a record of this game?");
		
		saveAlert.setNegativeButton("Cancel", null);
		saveAlert.setPositiveButton("Ok",
			new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int which){
					saveGame();
				}
		});
		
		saveAlert.create().show();
	}

	private void saveGame(){
		AlertDialog.Builder saveSettings = new AlertDialog.Builder(this);

		saveSettings.setTitle("Save");
		saveSettings.setMessage("Please enter the title of this replay.");

		// Set an EditText view to get user input 
		final EditText input = new EditText(this);
		saveSettings.setView(input);

		saveSettings.setNegativeButton("Cancel", null);
		saveSettings.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
			String saveName = input.getText().toString();
			RecordGame rg = RecordGame.getInstance();
			List<ChessPiece[][]> boardLayouts = b.getBoardLayouts();
			rg.save(saveName, boardLayouts); }
		});


		saveSettings.create().show();
	}
}
