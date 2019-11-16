package teamprj.antrip.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.test.ui_practice.helper.SwipeAndDragHelper
import kotlinx.android.synthetic.main.item_card_view.view.*
import teamprj.antrip.R
import teamprj.antrip.data.model.MyPlan

class RecyclerViewAdapter(private val data: ArrayList<MyPlan>) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>(), SwipeAndDragHelper.ActionCompletionContract {
    private val TAG = "MyPlanRecyclerView"
    private lateinit var mContext: Context
    private lateinit var touchHelper: ItemTouchHelper
    val user = FirebaseAuth.getInstance().currentUser
    private val database = FirebaseDatabase.getInstance()
    private val userName = user!!.email!!.replace(".", "_")
    private val myRef = database.getReference("plan").child(userName)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_card_view, parent, false))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        data[position].let { item ->
            with(holder.itemView) {
                  tv_title.text = item.tripName

                if (position % 2 == 0) {
                    iv_card_imageView.setBackgroundResource(R.drawable.img_card_background_first)
                } else {
                    iv_card_imageView.setBackgroundResource(R.drawable.img_card_backgorund_second)
                }

                setOnLongClickListener {
                    touchHelper.startDrag(holder)
                    true
                }
            }
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
        }
    }

    override fun onViewSwiped(position: Int) {
        data.removeAt(position)
        myRef.child(data[position].tripName).removeValue()
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, data.size)
    }

    fun setTouchHelper(touchHelper: ItemTouchHelper) {
        this.touchHelper = touchHelper
    }
}