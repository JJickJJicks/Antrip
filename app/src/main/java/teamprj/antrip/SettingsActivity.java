package teamprj.antrip;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.google.android.material.snackbar.Snackbar;

import teamprj.antrip.data.Fakedata;
import teamprj.antrip.ui.login.LoginActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            Preference logout = findPreference("logout");
            Preference travel = findPreference("downloaded_travel");

            logout.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("로그아웃").setMessage("로그아웃 하시겠습니까?")
                            .setPositiveButton("로그아웃", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    Intent i = new Intent(getActivity(), LoginActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(i);
                                }
                            })
                            .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {

                                }
                            })
                            .show();
                    return true;
                }
            });

            travel.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                //TODO: 저장한 Travel의 수 Data 로드
                Fakedata fakedata = new Fakedata();
                int travelcnt = fakedata.travelcnt;

                public boolean onPreferenceClick(Preference preference) {
                    if (travelcnt > 0)
                        Snackbar.make(getActivity().findViewById(android.R.id.content), "다운로드한 Travel 출력", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    else
                        Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.empty_travel, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    return true;
                }
            });
        }
    }

}