package net.jayantupadhyaya.tictactoe;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class TicTacToeActivity extends Activity {
	private GridView tttGrid;
	private static int MAX_BLOCKS = 9;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		tttGrid = (GridView) findViewById(R.id.tttGrid);
		tttGrid.setAdapter(new BlockAdapter(this));
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

			((TextView) convertView).setText(position % 2 == 0 ? "X" : "O");

			return convertView;
		}
	}
}

