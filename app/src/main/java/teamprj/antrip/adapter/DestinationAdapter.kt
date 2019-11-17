package teamprj.antrip.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.item_card_view.view.*
import teamprj.antrip.R
import teamprj.antrip.data.model.PlaceInfo

class DestinationAdapter(private val data: ArrayList<PlaceInfo>) : RecyclerView.Adapter<DestinationAdapter.ViewHolder>() {
    private val TAG = "MyPlanRecyclerView"
    private lateinit var mContext: Context
    private lateinit var touchHelper: ItemTouchHelper
    val user = FirebaseAuth.getInstance().currentUser
    private val database = FirebaseDatabase.getInstance()
    private val userName = user!!.email!!.replace(".", "_")
    private val myRef = database.getReference("plan").child(userName)

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

            }
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
        }
    }
}