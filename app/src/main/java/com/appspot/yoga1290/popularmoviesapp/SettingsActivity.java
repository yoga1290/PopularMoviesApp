package com.appspot.yoga1290.popularmoviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

import com.appspot.yoga1290.popularmoviesapp.model.SettingsData;
import com.appspot.yoga1290.popularmoviesapp.sync.SyncService;
import com.appspot.yoga1290.popularmoviesapp.tasks.FetchPopularMovies;
import com.appspot.yoga1290.popularmoviesapp.tasks.FetchTopRatedMovies;

/**
 * Created by yoga1290 on 3/25/16.
 */
public class SettingsActivity extends PreferenceActivity
        implements Preference.OnPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add 'general' preferences, defined in the XML file
        // TODO: Add preferences from XML
        addPreferencesFromResource(R.xml.pref_general);

        // For all preferences, attach an OnPreferenceChangeListener so the UI summary can be
        // updated when the preference changes.
        // TODO: Add preferences
        bindPreferenceSummaryToValue(findPreference(getString(R.string.settings_sortby)));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_sync_frequency)));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_max_movie_records_key)));
        //
    }

    /**
     * Attaches a listener so the summary is always updated with the preference value.
     * Also fires the listener once, to initialize the summary (so it shows up before the value
     * is changed.)
     */
    private void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(this);
        // Trigger the listener immediately with the preference's
        // current value.
        onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {
        String stringValue = value.toString();

        if (preference instanceof ListPreference) {
            // For list preferences, look up the correct display value in
            // the preference's 'entries' list (since they have separate labels/values).
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(stringValue);
            if (prefIndex >= 0) {
                preference.setSummary(listPreference.getEntries()[prefIndex]);


                Log.i(SettingsActivity.class.getSimpleName(), listPreference.getEntry()+", "+listPreference.getValue());
                SettingsData settingsData = new SettingsData();
                SettingsData settingsDataT[] = SettingsData.get(getContentResolver());
                if(settingsDataT.length>0) {
                    settingsData = settingsDataT[0];
                }


                if("pref_max_movie_records_key".equals(listPreference.getKey()) ) {
                    settingsData.setMaxMovieRecords( Integer.parseInt(listPreference.getValue()) );
                }

                if("settings_sortby".equals(listPreference.getKey()) ) {
                    String sortBy = listPreference.getValue();
                    settingsData.setSortBy("top_rated".equals(sortBy) ? 0 : 1);
                    // this will be used in the periodic sync (next condition)
                }

                if("pref_sync_frequency".equals(listPreference.getKey()) ) {

                    String taskName = settingsData.getSortBy()==0 ? FetchTopRatedMovies.class.getSimpleName(): FetchPopularMovies.class.getSimpleName();
                    Bundle syncBundle = new Bundle();
                    syncBundle.putString(SyncService.TASK_NAME, taskName);

                    if ("1".equals(listPreference.getValue())) {
                        SyncService.setSyncPeriod(getApplicationContext(), syncBundle, 1 << 30);
                    } else {
                        SyncService.setSyncPeriod(getApplicationContext(), syncBundle, Integer.parseInt(listPreference.getValue()));
                    }
                }


                settingsData.save(getContentResolver());
            }
        } else {
            // For other preferences, set the summary to the value's simple string representation.
            preference.setSummary(stringValue);
        }
        return true;
    }

}