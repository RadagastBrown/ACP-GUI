package cs213.chess.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;

import cs213.chess.piece.ChessPiece;

public class RecordGame extends Activity{

	private static String path;
	private static SaveGame sg;
	private static RecordGame rg = null;
	
	private RecordGame(){}
	
	/**
	 * Obtain the class's singleton instance.
	 * @return the RecordGame instance.  If RecordGame has yet to be created, this call creates it.
	 * @author Thomas Travis
	 */
	public static RecordGame getInstance(){
		if( rg == null ){
			rg = new RecordGame();
		}
		return rg;
	}
	
	/**
	 * Save a a List of moves defining the turns of a chess game with the given fileName
	 * @param fileName
	 * @param moves
	 * @return true is saves successfully; false otherwise.
	 * @author Thomas Travis
	 */
	public boolean save(String fileName, List<ChessPiece[][]> boardLayouts){
		
		sg = new SaveGame(fileName, boardLayouts);
		
		File f = new File(fileName);
		if( f.exists() ){
			return false;
		}
		ObjectOutputStream oos;
		try{
			oos = new ObjectOutputStream( openFileOutput(fileName, Context.MODE_PRIVATE));
			oos.writeObject(sg);
			oos.close();
		}
		catch( IOException e ){
			return false;
		}
		
		return true;
	}
	
	/**
	 * Obtain the list of moves of a recorded game.
	 * @param fileName
	 * @return a List of String arrays; each array has length two.  Index 0 is a piece's starting
	 * position, and index 1 is a piece's destination.  The arrays are in order of the turns 
	 * taken in the original game.  If the file does not exist, or if there
	 * is an error accessing the file, return null.
	 * @author Thomas Travis
	 */
	public List<ChessPiece[][]> load(String fileName){
		
		File f = new File( fileName );
		if( !f.exists() ){
			return null;
		}
		
		ObjectInputStream ois;
		
		try{
			ois = new ObjectInputStream( openFileInput( fileName ));
			sg = (SaveGame)ois.readObject();
			ois.close();
		}
		catch( IOException e ){
			return null;
		}
		catch( ClassNotFoundException e ){
			return null;
		}
		
		return sg.getMoves();
	}
	
	/**
	 * Obtain a List of all games in name order.
	 * @return a List of GameDateNode objects containing the file names and dates of the saved games.
	 * @author Thomas Travis
	 */
	public List<GameDateNode> listByName(){
		
		String [] files = fileList();
		List<GameDateNode> fileList = null;
		
		if( files == null ){
			return fileList;
		}
		
		for( int i = 0; i < files.length; i++ ){
			files[i] = files[i].substring(0, files[i].lastIndexOf(".chess"));
		}
		
		Arrays.sort(files, String.CASE_INSENSITIVE_ORDER);
		fileList = list(files);
		return fileList;
	}
	
	/**
	 * Obtain a list of all games in date order
	 * @return a List of GameDateNode objects containing the file names and dates of the saved games.
	 */
	public List<GameDateNode> listByDate(){

		String [] files = fileList();
		if( files == null ){
			return null;
		}
		List<GameDateNode> l = list( files );
		GameDateNode [] gdna = l.toArray(new GameDateNode[0]);
		Arrays.sort(gdna);
		
		l.clear();
		
		l = Arrays.asList(gdna);
		return l;
	}
	
	private List<GameDateNode> list(String [] files){
		
		File f;
		List<GameDateNode> l = new LinkedList<GameDateNode>();
		GameDateNode gdn = null;
		for( int i = 0; i < files.length; i++ ){
			f = new File( files[i] );
			try{
				
				ObjectInputStream ois = new ObjectInputStream( new FileInputStream( f ) );
				sg = (SaveGame)ois.readObject();
				ois.close();
			}
			catch( IOException e ){
				return null;
			}
			catch( ClassNotFoundException e ){
				return null;
			}
			
			gdn = new GameDateNode(sg.getFileName(), sg.getDate());
			l.add(gdn);
		}
		return l;
	}
	
	/**
	 * Serializable inner class acting as a savable container for a games list of moves.
	 * @author Thomas Travis
	 *
	 */
	protected class SaveGame{
		
		private String fileName;
		private List<ChessPiece[][]> moves;
		private Calendar cal;
		
		/**
		 * Create a SaveGame object with the given list of moves.
		 * @param moves
		 * @author Thomas Travis
		 */
		protected SaveGame(String fileName, List<ChessPiece[][]> moves){
			this.fileName = fileName;
			this.moves = moves;
			cal = new GregorianCalendar();
            cal.set(Calendar.MILLISECOND,0);
            cal.setTime(cal.getTime());
		}
		
		/**
		 * Obtain this SaveGame object's list of moves.
		 * @return a List of String arrays representing a game's sequence of moves.
		 * @author Thomas Travis
		 */
		protected List<ChessPiece[][]> getMoves(){
			return moves;
		}
		
		/**
		 * Obtain this SaveGame object's file name.
		 * @return String
		 */
		protected String getFileName(){
			return fileName;
		}
		
		/**
         * Obtain the text representation of this Photo's date
         * @return String representation of this Photo's calendar object.  Formatted
         * to MM/DD/YY-HH:MM:SS
         * @author Thomas Travis
         */
        protected String getDate(){
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy-HH:mm:ss", Locale.ROOT);
                String date = df.format(cal.getTime());
                return date;
        }
        
        public byte[] getBytes(){
        	return this.getBytes();
        }
	}
	
    /**
     * A node for use in listByDate containing the fileName and date strings of a game.
     * @author Thomas Travis
     */
    public class GameDateNode implements Comparable<GameDateNode>{
	    String fileName;
	    String date;
	    
	    public GameDateNode(String fileName, String date){
	            this.fileName = fileName;
	            this.date = date;
	    }
	    
	    public String getFileName(){
	            return this.fileName;
	    }
	    
	    public String getDate(){
	            return this.date;
	    }
	    
	    public int compareTo(GameDateNode g){
	            return this.date.compareTo(g.getDate());
	    }
	    
	    public String toString(){
	            return this.fileName;
	    }
    }
}
