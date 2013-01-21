package net.jayantupadhyaya.tictactoe;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class SettingsActivity extends PreferenceActivity
				implements Preference.OnPreferenceChangeListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		PreferenceManager prefMgr = getPreferenceManager();
		prefMgr.setSharedPreferencesName(App.SETTINGS);
		prefMgr.setSharedPreferencesMode(MODE_PRIVATE);

		addPreferencesFromResource(R.xml.settings);

		Preference p = prefMgr.findPreference("vibrate");
		p.setOnPreferenceChangeListener(this);
	}

	public boolean onPreferenceChange(Preference preference, Object newValue) {
		String key = preference.getKey();
		
		if(key.equals("vibrate")) {
			App.CHECK_PREF = (Boolean) newValue;   
		}
		return true;
	}

}
