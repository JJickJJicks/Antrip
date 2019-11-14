package teamprj.antrip.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import teamprj.antrip.R;
import teamprj.antrip.data.model.Member;

public class ProfileActivity extends AppCompatActivity {
    private final int PROFILE_CHANGE = 10;
    ImageView ivProfileImg;
    private TextView tvHeaderName, tvHeaderEmail, tvContentName, tvContentEmail, btnEditProfile;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        tvHeaderName = findViewById(R.id.tvHeaderName);
        tvHeaderEmail = findViewById(R.id.tvHeaderEmail);
        tvContentName = findViewById(R.id.tvContentName);
        tvContentEmail = findViewById(R.id.tvContentEmail);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        ivProfileImg = findViewById(R.id.ivProfileImg);

        setProfile(user.getEmail(), user.getDisplayName());

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProfileCorrectActivity.class);
                startActivityForResult(intent, PROFILE_CHANGE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PROFILE_CHANGE && resultCode == RESULT_OK) {
            setProfile(data.getStringExtra("email"), data.getStringExtra("name"));
        }
    }

    private void setProfile(String email, String name) {
        tvContentEmail.setText(email);
        tvHeaderEmail.setText(email);

        tvHeaderName.setText(name);
        tvContentName.setText(name);

        final String userKey = email.replace(".", "_");
        myRef = FirebaseDatabase.getInstance().getReference("users").child(userKey);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Member member = dataSnapshot.getValue(Member.class);
                String img = member.getProfile();
                switch (img) {
                    default: // 오류 떠도 일단 1번 사진으로..
                        ivProfileImg.setImageResource(R.drawable.img_sample1);
                        break;
                    case "img_sample2":
                        ivProfileImg.setImageResource(R.drawable.img_sample2);
                        break;
                    case "img_sample3":
                        ivProfileImg.setImageResource(R.drawable.img_sample3);
                        break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
