package teamprj.antrip.ui.function

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_destination_detail.*
import teamprj.antrip.R
import teamprj.antrip.adapter.DestinationAdapter
import teamprj.antrip.data.model.MyPlan
import teamprj.antrip.data.model.PlaceInfo
import teamprj.antrip.map.DirectionFragment

class DestinationDetailActivity : AppCompatActivity() {
    private val TAG = "DestinationDetail"
    private lateinit var mContext: Context
    lateinit var recyclerAdapter: DestinationAdapter
    val user = FirebaseAuth.getInstance().currentUser
    private val database = FirebaseDatabase.getInstance()
    private val userName = user!!.email!!.replace(".", "_")
    private val myRef = database.getReference("plan").child(userName)
    private lateinit var googleMapFragment: DirectionFragment
    private lateinit var data: ArrayList<PlaceInfo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_destination_detail)

        if (savedInstanceState == null) {
            googleMapFragment = DirectionFragment()
            supportFragmentManager.beginTransaction()
                    .replace(R.id.fm_google_map, googleMapFragment, "main")
                    .commit()
        }

        var intent = intent
        val tripName = intent.extras.getString("TripName")

        data = ArrayList()

        myRef.child(tripName).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (i in dataSnapshot.child("travel").children.iterator()) {
                        for(j in i.children.iterator()) {
                            val place  = PlaceInfo()
                            place.num = j.key!!.toInt()
                            place.name = j.child("name").toString()
                            place.counry = j.child("counry").toString()
                            place.lat = j.child("latitude").toString().toDouble()
                            place.lon = j.child("longitude").toString().toDouble()
                            place.accommoodation = j.child("accommoodation").toString()
                            data.add(place)
                        }
                    }
                    Log.d(TAG, "${data.size}")
                    recyclerAdapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
        recyclerAdapter = DestinationAdapter(data)
        rv_item_recycler_view.layoutManager = LinearLayoutManager(this)
        rv_item_recycler_view.adapter = recyclerAdapter
    }
}

