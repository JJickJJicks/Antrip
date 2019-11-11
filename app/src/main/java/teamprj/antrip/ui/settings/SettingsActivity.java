package teamprj.antrip.ui.settings;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.google.android.material.snackbar.Snackbar;

import teamprj.antrip.R;
import teamprj.antrip.ui.function.OffActivity;
import teamprj.antrip.ui.login.LoginActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_Layout, new SettingsFragment())
                .commit();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { // 뒤로 가기
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }


    public static class SettingsFragment extends PreferenceFragmentCompat {
        SharedPreferences setting;
        SharedPreferences.Editor editor;

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            Preference logout = findPreference("logout");
            Preference travel = findPreference("downloaded_travel");
            Preference appVersion = findPreference("app_version");

            logout.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("로그아웃").setMessage("로그아웃 하시겠습니까? 자동 로그인이 해제됩니다.")
                            .setPositiveButton("로그아웃", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    // 자동 로그인 해제
                                    setting = getActivity().getSharedPreferences("setting", 0);
                                    editor = setting.edit();

                                    if (setting.getBoolean("Auto_Login_enabled", false)) {
                                        editor.remove("ID");
                                        editor.remove("PW");
                                        editor.remove("Auto_Login_enabled");
                                        editor.commit();
                                    }

                                    // 초기 로그인 화면으로 돌아감
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
                int travelcnt = 0;

                public boolean onPreferenceClick(Preference preference) {
                    /*
                    if (travelcnt > 0)
                        Snackbar.make(getActivity().findViewById(android.R.id.content), "다운로드한 Travel 출력", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    else
                        Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.empty_travel, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    return true;

                    */
                    Intent intent = new Intent(getActivity(), OffActivity.class);
                    startActivity(intent);
                    return true;
                }
            });
        }
    }

}