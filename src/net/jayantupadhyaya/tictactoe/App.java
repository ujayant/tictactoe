package net.jayantupadhyaya.tictactoe; 

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.SharedPreferences;

public class App {
	private static Context ctx = null;

	public static final String SETTINGS = "settings";

	private App() {}

	public static boolean CHECK_PREF;

	public static synchronized void setContext(Context appContext)
			throws NameNotFoundException {
		if (ctx == null) {
			ctx = appContext;

			SharedPreferences prefs = ctx.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
			CHECK_PREF = prefs.getBoolean("vibrate", true);
		}

	}
}
