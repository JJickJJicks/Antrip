package teamprj.antrip.ui.function;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import teamprj.antrip.R;


public class TravelPlanActivity extends AppCompatActivity implements ExpandableListAdapter.OnStartDragListner {

    static RecyclerView recyclerview;
    ItemTouchHelper mItemTouchHelper;
    static List<ExpandableListAdapter.Item> data;
    static ExpandableListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_plan);

        if (savedInstanceState == null) {

            GoogleMapFragment googleMapFragment = new GoogleMapFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.googleMapFragment, googleMapFragment, "main")
                    .commit();
        }

        Intent intent = getIntent();
        String inputValue = intent.getExtras().getString("inputValue");

        recyclerview = findViewById(R.id.recyclerView_travel_plan);
        recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        data = new ArrayList<>();

        String title = "0일차";
        for (int i = 0; i < Integer.parseInt(inputValue); i++)
        {
            title = title.replace(Integer.toString(i), Integer.toString(i + 1));
            data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, title));
            data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "추가"));
        }

        mAdapter = new ExpandableListAdapter(data, this);
        ItemTouchHelperCallback mCallback = new ItemTouchHelperCallback(mAdapter);
        mItemTouchHelper = new ItemTouchHelper(mCallback);
        mItemTouchHelper.attachToRecyclerView(recyclerview);
        recyclerview.setAdapter(mAdapter);

        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                jsonCreate();
            }
        });
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder holder) {
        mItemTouchHelper.startDrag(holder);
    }

    public static void addItem(int index, String name, LatLng latLng, boolean accommodation){
        data.add(index, new ExpandableListAdapter.Item(ExpandableListAdapter.DATA, name, latLng, accommodation));
        recyclerview.setAdapter(mAdapter);
        if (accommodation) {
            mAdapter.moveAccommodation(index);
        }
        GoogleMapFragment.selectPlace(latLng, name);
    }

//    private String getJsonString()
//    {
//        String json = "";
//
//        try {
//            InputStream is = getAssets().open("Export.json");
//            int fileSize = is.available();
//
//            byte[] buffer = new byte[fileSize];
//            is.read(buffer);
//            is.close();
//
//            json = new String(buffer, StandardCharsets.UTF_8);
//        }
//        catch (IOException ex)
//        {
//            ex.printStackTrace();
//        }
//
//        return json;
//    }
//
//    private void jsonParsing(String json)
//    {
//        try{
//            JSONObject jsonObject = new JSONObject(json);
//
//            JSONArray jsonArray = jsonObject.getJSONArray("Items");
//
//            for(int i = 0; i < jsonArray.length(); i++)
//            {
//                JSONObject itemObject = jsonArray.getJSONObject(i);
//
//                int type = itemObject.getInt("type");
//                String name = itemObject.getString("name");
//                ExpandableListAdapter.Item item = new ExpandableListAdapter.Item(type, name);
//                data.add(item);
//            }
//        }catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

    private void jsonCreate()
    {
        JSONObject obj = new JSONObject();
        try {
            JSONArray jArray = new JSONArray();
            for (int i = 0; i < data.size(); i++)
            {
                JSONObject sObject = new JSONObject();
                int type = data.get(i).type;
                String name = data.get(i).name;

                if (type == ExpandableListAdapter.CHILD) {
                    continue;
                }
                sObject.put("type", type);
                sObject.put("name", name);
                if (type == ExpandableListAdapter.DATA) {
                    LatLng latLng = data.get(i).latLng;
                    sObject.put("lat", latLng.latitude);
                    sObject.put("lng", latLng.longitude);
                }
                jArray.put(sObject);
            }
            obj.put("tempJson", "temp");
            obj.put("item", jArray);

            System.out.println(obj.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

