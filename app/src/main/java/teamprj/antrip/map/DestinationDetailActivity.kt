package teamprj.antrip.map

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_destination_detail.*
import teamprj.antrip.R
import teamprj.antrip.data.model.PlaceInfo

class DestinationDetailActivity : AppCompatActivity() {
    private val TAG = "DestinationDetail"
    private val MESSAGE_OK = 1
    private lateinit var mContext: Context
    private lateinit var recyclerAdapter: DestinationAdapter
    private val user = FirebaseAuth.getInstance().currentUser
    private val database = FirebaseDatabase.getInstance()
    private val userName = user!!.email!!.replace(".", "_")
    private val myRef = database.getReference("plan").child(userName)
    private lateinit var googleMapFragment: DirectionFragment
    private lateinit var data: ArrayList<PlaceInfo>
    private lateinit var sDate: String
    private lateinit var eDate: String

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
                if (dataSnapshot.child("save").value.toString() == "true") {
                    if (dataSnapshot.exists()) {
                        tv_start_date.text = dataSnapshot.child("start_date").value.toString()
                        tv_end_date.text = dataSnapshot.child("end_date").value.toString()

                        for (i in dataSnapshot.child("travel").children.iterator()) {
                            for (j in i.children.iterator()) {
                                val place = PlaceInfo()
                                place.num = j.key!!.toInt()
                                place.name = j.child("name").value.toString()
                                place.counry = j.child("country").value.toString()
                                place.lat = j.child("latitude").value.toString().toDouble()
                                place.lon = j.child("longitude").value.toString().toDouble()
                                place.accommoodation = j.child("accommoodation").toString()
                                data.add(place)
                            }
                        }
                        Log.d(TAG, "${data.size}")
                        recyclerAdapter.notifyDataSetChanged()
                    }
                } else {
                    ll_date_linear_layout.visibility = View.GONE
                    destination_fail.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
        recyclerAdapter = DestinationAdapter(data, googleMapFragment)
        rv_item_recycler_view.layoutManager = LinearLayoutManager(this)
        rv_item_recycler_view.adapter = recyclerAdapter
    }
}

