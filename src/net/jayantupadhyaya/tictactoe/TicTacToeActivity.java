package net.jayantupadhyaya.tictactoe;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.jayantupadhyaya.tictactoe.App;

public class TicTacToeActivity extends Activity {
	private static int MAX_BLOCKS = 9;

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
