package cs213.chess.android;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import cs213.chess.utils.RecordGame;
import cs213.chess.utils.RecordGame.GameDateNode;

public class ListGamesActivity extends Activity {

	private ListView listView;
	private RecordGame rg;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_games);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list_games, menu);
		return true;
	}

	protected void onStart(){
		super.onStart();
		
		int id = getResources().getIdentifier("list_games", "id", "cs213.chess.android");
		listView = (ListView)findViewById(id);
		rg = RecordGame.getInstance();
		
		List<GameDateNode> oogaabaagaka = rg.listByName();
		Toast.makeText(this, "There are no games to replay!", Toast.LENGTH_SHORT).show();
		
		//listView.setAdapter( new ArrayAdapter<GameDateNode>( this, R.layout.game_list_element, rg.listByName() ) );
			
		listView.setOnItemClickListener(new OnItemClickListener(){

				@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				GameDateNode gdn = (GameDateNode)listView.getItemAtPosition(position);
				Intent intent = new Intent(getApplicationContext(), PlaybackActivity.class);
				intent.putExtra("fileName", gdn.getFileName());
				startActivity(intent);
			}
			
			});
	}
	
	public void sortByName(View view){
		int id = getResources().getIdentifier("list_games", "id", "cs213.chess.android");
		listView.setAdapter( new ArrayAdapter<GameDateNode>( this, id, rg.listByName() ) );
	}
	
	public void sortByDate(View view){
		int id = getResources().getIdentifier("list_games", "id", "cs213.chess.android");
		listView.setAdapter( new ArrayAdapter<GameDateNode>( this, R.layout.game_list_element, rg.listByDate() ) );
	}
}
