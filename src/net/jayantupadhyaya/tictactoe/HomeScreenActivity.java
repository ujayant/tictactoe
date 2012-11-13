package net.jayantupadhyaya.tictactoe;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class HomeScreenActivity extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.homescreen);
	}		

	/*public void onePlayer(View view) {
		Intent intent = new Intent(this, TicTacToeActivity.class);
		intent.putExtra(NO_OF_PLAYERS, 1);
		startActivity(intent);
	}*/

	public void twoPlayer(View view) {
		Intent intent = new Intent(this, TicTacToeActivity.class);
		//intent.putExtra(NO_OF_PLAYERS, 2);
		startActivity(intent);
	}

	public void about(View view) {
		LayoutInflater li = getLayoutInflater();
		new AlertDialog.Builder(this)
			.setTitle(R.string.dialog_about_title)
			.setView((TextView) li.inflate(R.layout.about, null))
			.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.dismiss();
				}
			})
			.show();
	}

	public void settings(View view) {
		Intent intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);
	}
}
