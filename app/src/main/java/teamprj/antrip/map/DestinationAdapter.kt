package teamprj.antrip.map

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.item_card_view_plan.view.*
import teamprj.antrip.R
import teamprj.antrip.data.model.PlaceInfo

class DestinationAdapter(private val data: ArrayList<PlaceInfo>, var googleMapFragment: DirectionFragment) : RecyclerView.Adapter<DestinationAdapter.ViewHolder>() {
    private val TAG = "MyPlanRecyclerView"
    private lateinit var mContext: Context
    private lateinit var touchHelper: ItemTouchHelper
    val user = FirebaseAuth.getInstance().currentUser
    private val database = FirebaseDatabase.getInstance()
    private val userName = user!!.email!!.replace(".", "_")
    private val myRef = database.getReference("plan").child(userName)
    private var dayCount = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_card_view_plan, parent, false))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        data[position].let { item ->
            with(holder.itemView) {
                if (item.num == 0) {
                    ll_day.visibility = View.VISIBLE
                    tv_day.text = "${dayCount}Day"
                    dayCount++
                } else {
                    ll_day.visibility = View.GONE
                }
                tv_name.text = item.name
                tv_country.text = item.counry

                setOnClickListener {
                    val origin = LatLng(item.lat, item.lon)
                    if(position == data.size - 1) {
                        val destination = LatLng(data[0].lat, data[0].lon)
                        Log.d(TAG, "${item.name}, ${data[0].name}, ${item.lat}, ${item.lon}, ${data[0].lat}, ${data[0].lon}")
                        googleMapFragment.draw(origin, destination, item.name, data[0].name)

                    } else {
                        val destination = LatLng(data[position+1].lat, data[position+1].lon)
                        Log.d(TAG, "${item.name}, ${data[position+1].name}, ${item.lat}, ${item.lon}, ${data[position+1].lat}, ${data[position+1].lon}")
                        googleMapFragment.draw(origin, destination, item.name, data[position+1].name)
                    }
                }
            }
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
        }
    }
}