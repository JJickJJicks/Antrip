package teamprj.antrip.data.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Plan {
    private HashMap<Integer, ArrayList<String>> travel;
    private DatabaseReference mDatabase, mTravelDB;
    private long maxid;

    public Plan() {
        travel = new HashMap<>();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Plan");
    }

    public void insertDatePlan(int date, ArrayList<String> list) {
        travel.put(date, list);
    }

    public ArrayList<String> getDatePlan(int date) {
        return travel.get(Integer.valueOf(date));
    }

    public void sort() {
        //TODO: 정렬하는 함수
    }

    public void insertDB(String userId, String travelName) {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                    maxid = dataSnapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mTravelDB = mDatabase.child(String.valueOf(maxid + 1));
        mTravelDB.child("userId").setValue(userId);
        mTravelDB.child("name").setValue(travelName);
        mTravelDB.child("plan").setValue(travel);
    }

//    void updateDB(int key, String travelName) {
//        mDatabase.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists())
//                    maxid = dataSnapshot.getChildrenCount();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//        mTravelDB = mDatabase.child(String.valueOf(maxid + 1));
//        mTravelDB.child("name").setValue(travelName);
//        mTravelDB.child("plan").setValue(travel);
//    }

    void readDB(long key) {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Plan");
        myRef.orderByKey().equalTo(Long.toString(key)).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            Plan member = data.getValue(Plan.class);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("MyApp", "getUser:onCancelled", databaseError.toException());
                    }
                }
        );
    }

    void removeDB(int key) {
        mTravelDB = mDatabase.child(Long.toString(key));
        mTravelDB.removeValue();
    }
}
