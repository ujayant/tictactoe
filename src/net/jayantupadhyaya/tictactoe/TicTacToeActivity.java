package net.jayantupadhyaya.tictactoe; 

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;

import net.jayantupadhyaya.tictactoe.App;

public class TicTacToeActivity extends Activity {
	private static int MAX_BLOCKS = 9;

	//For saving the states
	private SharedPreferences states;
	private String statePref = "StatesPref";

	private static String MOVE_COUNT;
	private static String MOVES_ARRAY;
	private static String MOVES_ARRAY_LENGTH;
	private static String CURRENT_PLAYER;
	private static String CURRENT_MARKER;
	private static String SUMS_ARRAY;
	private static String SUMS_ARRAY_LENGTH;
	private static String GAME_OVER;
	private static String CHECK_PREF;

	private GridView tttGrid;
	private TextView gameStatus;

	private int moveCount = 0;
	private int[] moves = new int[MAX_BLOCKS];
	private int curPlayer = 2;
	private String curMarker = "X";
	private int[] sums = new int[8];
	private int r, c;
	private boolean gameOver = false;

	public void reset(View view) {
		moveCount = 0;
		int i;
		TextView t;
		for (i = 0; i < MAX_BLOCKS; ++i) {
			moves[i] = 0;
			t = (TextView) tttGrid.getChildAt(i);
			t.setBackgroundColor(Color.WHITE);
			t.setText("");
		}
		curPlayer = 2;
		curMarker = "X";
		for (i = 0; i < 8; ++i) {
			sums[i] = 0;
		}
		gameOver = false;
		gameStatus.setText(curMarker + "'s turn");
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		savedInstanceState.putInt(MOVE_COUNT, moveCount);
		savedInstanceState.putIntArray(MOVES_ARRAY, moves);
		savedInstanceState.putInt(CURRENT_PLAYER, curPlayer);
		savedInstanceState.putString(CURRENT_MARKER, curMarker);
		savedInstanceState.putIntArray(SUMS_ARRAY, sums);
		savedInstanceState.putBoolean(GAME_OVER, gameOver);
		savedInstanceState.putBoolean(CHECK_PREF, App.CHECK_PREF);

		super.onSaveInstanceState(savedInstanceState);
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);

		moveCount = savedInstanceState.getInt(MOVE_COUNT);
		moves = savedInstanceState.getIntArray(MOVES_ARRAY);
		curPlayer = savedInstanceState.getInt(CURRENT_PLAYER);
		curMarker = savedInstanceState.getString(CURRENT_MARKER);
		sums = savedInstanceState.getIntArray(SUMS_ARRAY);
		gameOver = savedInstanceState.getBoolean(GAME_OVER);
		App.CHECK_PREF = savedInstanceState.getBoolean(CHECK_PREF);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.options, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.settings_option:
				 startActivity(new Intent(this, SettingsActivity.class));
				 return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		tttGrid = (GridView) findViewById(R.id.tttGrid);
		tttGrid.setAdapter(new BlockAdapter(this));

		gameStatus = (TextView) findViewById(R.id.gameStatus);
		gameStatus.setText(curMarker + "'s turn");
		gameStatus.setBackgroundColor(Color.LTGRAY);
		gameStatus.setTextColor(Color.BLACK);

		tttGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (!gameOver) {
					if (moves[position] == 0) {
						moves[position] = curPlayer;
						Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
						if(App.CHECK_PREF == true) {
							vibrator.vibrate(150);
						} else {
							vibrator.vibrate(0);
						}
						((TextView) view).setTextColor(curMarker == "X" ? Color.BLACK : Color.RED);
						((TextView) view).setText(curMarker);

						r = position / 3;
						c = position % 3;
						sums[r] += curPlayer;
						sums[3 + c] += curPlayer;
						if (r == c) {
							sums[6] += curPlayer;
						}
						if (r == 2 - c) {
							sums[7] += curPlayer;
						}

						++moveCount;
						if (moveCount > 2) {
							for (int i = 0; i < 8; ++i) {
								if (sums[i] == 6 || sums[i] == 15) {
									gameOver = true;
									vibrator.vibrate(300);
									gameStatus.setText(curMarker + " wins!");
									int start, incr;
									if (i < 3) {
										start = i * 3;
										incr = 1;
									} else if (i < 6) {
										start = (i - 3);
										incr = 3;
									} else if (i == 6) {
										start = 0;
										incr = 4;
									} else {
										start = 2;
										incr = 2;
									}
									for (int j = 0; j < 3; ++j) {
										tttGrid.getChildAt(start + incr * j).setBackgroundColor(Color.LTGRAY);
									}

									return;
								}
							}

							if (moveCount == 9 && !gameOver) {   
								gameStatus.setText("Nobody wins!");
								return;
							}
						}

						// Change current player
						curPlayer = (curPlayer == 2 ? 5 : 2);
						curMarker = (curPlayer == 2 ? "X" : "O");
						gameStatus.setText(curMarker + "'s turn");
					}
				}
			}
		});
	}

	@Override
	public void onPause() {
		super.onPause();

		states = getSharedPreferences(statePref, MODE_PRIVATE);
		SharedPreferences.Editor editor = states.edit();
		editor.putInt("MOVE_COUNT", moveCount);
		editor.putInt("MOVES_ARRAY_LENGTH", moves.length);
		editor.putString("MOVES_ARRAY", arraytoString(moves));
		editor.putInt("CURRENT_PLAYER", curPlayer);
		editor.putString("CURRENT_MARKER", curMarker);
		editor.putInt("SUMS_ARRAY_LENGTH", sums.length);
		editor.putString("SUMS_ARRAY", arraytoString(sums));
		editor.putBoolean("GAME_OVER", gameOver);

		editor.commit();
	}

	@Override
	public void onResume() {
		super.onResume();

		String str;

		states = getSharedPreferences(statePref, MODE_PRIVATE);
		moveCount = states.getInt("MOVE_COUNT", 0);
		str = states.getString("MOVES_ARRAY", arraytoString(moves));
		moves = toIntArray(str);
		curPlayer = states.getInt("CURRENT_PLAYER", 2);
		curMarker = states.getString("CURRENT_MARKER", "X");
		str = states.getString("SUMS_ARRAY", arraytoString(sums));
		sums = toIntArray(str);
		gameOver = states.getBoolean("GAME_OVER", false);
	}

	public String arraytoString(int[] array) {
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < array.length; i++) {
			str.append(array[i]).append(",");
		}

		return str.toString();
	}

	public int[] toIntArray(String str) {
		String[] sp =  str.split(",");
		int[] array = new int[sp.length];
		int i = 0;
		for (String s : sp) {
			array[i++] = Integer.parseInt(s);
		}

		return array;
	}

	public class BlockAdapter extends ArrayAdapter<TextView> {
		private LayoutInflater mInflater;

		public BlockAdapter(Context context) {
			super(context, R.layout.block);

			mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return MAX_BLOCKS;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.block, null);
			}

			return convertView;
		}
	}
}
