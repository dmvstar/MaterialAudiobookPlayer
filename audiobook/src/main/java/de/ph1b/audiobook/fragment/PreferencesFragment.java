package de.ph1b.audiobook.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

import com.afollestad.materialdialogs.prefs.MaterialListPreference;

import de.ph1b.audiobook.R;
import de.ph1b.audiobook.dialog.AudioFolderOverviewDialog;
import de.ph1b.audiobook.dialog.SeekPreferenceDialog;
import de.ph1b.audiobook.dialog.SleepPreferenceDialog;
import de.ph1b.audiobook.utils.PrefsManager;

public class PreferencesFragment extends PreferenceFragment {

    private static final String TAG = PreferencesFragment.class.getSimpleName();
    private final SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
            initValues();
        }
    };
    private PrefsManager prefs;
    private SharedPreferences sp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        ActionBar actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        prefs = new PrefsManager(getActivity());
        sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

    private void initValues() {

        // seek pref
        int seekAmount = prefs.getSeekTime();
        String seekSummary = String.valueOf(seekAmount) + " " + getString(R.string.seconds);
        SeekPreferenceDialog seekPreferenceDialog = (SeekPreferenceDialog) findPreference(getString(R.string.pref_key_seek_time));
        seekPreferenceDialog.setSummary(seekSummary);

        // sleep pref
        int sleepAmount = prefs.getSleepTime();
        String sleepSummary = String.valueOf(sleepAmount) + " " + getString(R.string.minutes);
        SleepPreferenceDialog sleepPreferenceDialog = (SleepPreferenceDialog) findPreference(getString(R.string.pref_key_sleep_time));
        sleepPreferenceDialog.setSummary(sleepSummary);

        // folder pref
        Preference folderPreference = findPreference(getString(R.string.pref_key_audiobook_folders));
        folderPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                AudioFolderOverviewDialog dialog = new AudioFolderOverviewDialog();
                dialog.show(getFragmentManager(), TAG);
                return true;
            }
        });

        // theme pref
        MaterialListPreference themePreference = (MaterialListPreference) findPreference(getString(R.string.pref_key_theme));
        String themeSummary = sp.getString(getString(R.string.pref_key_theme), getString(R.string.pref_theme_light));
        themePreference.setSummary(themeSummary);
    }

    @Override
    public void onResume() {
        super.onResume();
        initValues();
        sp.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        sp.unregisterOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }

}