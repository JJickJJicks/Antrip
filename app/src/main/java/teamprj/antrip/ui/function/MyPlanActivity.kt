package teamprj.antrip.ui.function

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.test.ui_practice.helper.SwipeAndDragHelper
import kotlinx.android.synthetic.main.activity_my_trip.*
import teamprj.antrip.R
import teamprj.antrip.adapter.RecyclerViewAdapter
import teamprj.antrip.data.model.MyPlan

class MyPlanActivity : AppCompatActivity() {
    lateinit var recyclerAdapter: RecyclerViewAdapter
    lateinit var mContext: Context
    val user = FirebaseAuth.getInstance().currentUser
    private val TAG = "MyPlanActivity"
    private val database = FirebaseDatabase.getInstance()
    private val userName = user!!.email!!.replace(".", "_")
    private val myRef = database.getReference("plan").child(userName)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_trip)
        Glide.with(this).load(R.drawable.ic_camp).into(iv_toolbar_imageView)

        mContext = this

        val data = ArrayList<MyPlan>()

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (i in dataSnapshot.children.iterator()) {
                        var temp = MyPlan()
                        temp.tripName = i.key
                        data.add(temp)
                    }
                }
                recyclerAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
            recyclerAdapter = RecyclerViewAdapter(data)
            var swipeAndDragHelper = SwipeAndDragHelper(recyclerAdapter)
            var touchHelper = ItemTouchHelper(swipeAndDragHelper)
            rv_recyclerView.layoutManager = LinearLayoutManager(this)

            recyclerAdapter.setTouchHelper(touchHelper)
            rv_recyclerView.adapter = recyclerAdapter
            touchHelper.attachToRecyclerView(rv_recyclerView)
    }

    fun createSnackBar(view: View, content: String) {
        Snackbar.make(view, content, Snackbar.LENGTH_SHORT).show()
    }
}
