package teamprj.antrip.ui.settings;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import teamprj.antrip.R;
import teamprj.antrip.ui.function.OffActivity;
import teamprj.antrip.ui.login.LoginActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
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
            Preference contact = findPreference("contact");
            Preference delete = findPreference("delete");
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
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent(getActivity(), OffActivity.class);
                    startActivity(intent);
                    return true;
                }
            });

            contact.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:ilovejava@kakao.com"));
                    startActivity(intent);
                    return true;
                }
            });

            delete.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("회원 탈퇴").setMessage("탈퇴하시겠습니까? 재가입의 제한은 없습니다.")
                            .setPositiveButton("탈퇴", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                    user.delete()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        // 자동 로그인 해제
                                                        setting = getActivity().getSharedPreferences("setting", 0);
                                                        editor = setting.edit();

                                                        if (setting.getBoolean("Auto_Login_enabled", false)) {
                                                            editor.remove("ID");
                                                            editor.remove("PW");
                                                            editor.remove("Auto_Login_enabled");
                                                            editor.commit();
                                                        }

                                                        // 초기 화면 전환
                                                        Toast.makeText(getContext(), "탈퇴 되었습니다.", Toast.LENGTH_SHORT).show();
                                                        Intent i = new Intent(getActivity(), LoginActivity.class);
                                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        startActivity(i);
                                                    }
                                                }
                                            });
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
        }
    }

}