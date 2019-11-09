package teamprj.antrip.ui.function

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.test.ui_practice.helper.SwipeAndDragHelper
import kotlinx.android.synthetic.main.activity_my_trip.*
import teamprj.antrip.R
import teamprj.antrip.adapter.RecyclerViewAdapter
import teamprj.antrip.data.model.MyPlan

class MyPlanActivity : AppCompatActivity() {
    lateinit var recyclerAdapter: RecyclerViewAdapter
    lateinit var mContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_trip)
        Glide.with(this).load(R.drawable.ic_camp).into(iv_toolbar_imageView)

        mContext = this

        var data = arrayListOf<MyPlan>()
        val tempData = MyPlan("Test", "Test Data")

        for (i in 0 until 10) {
            data.add(tempData)
        }

        recyclerAdapter = RecyclerViewAdapter(data)
        var swipeAndDragHelper = SwipeAndDragHelper(recyclerAdapter)
        var touchHelper = ItemTouchHelper(swipeAndDragHelper)
        rv_recyclerView.layoutManager = LinearLayoutManager(this)

        recyclerAdapter.setTouchHelper(touchHelper)
        rv_recyclerView.adapter = recyclerAdapter
        touchHelper.attachToRecyclerView(rv_recyclerView)

       /* fb_fab.setOnClickListener { v ->
            startActivity(Intent(mContext, ChangeActivity::class.java))
            overridePendingTransition(R.anim.fade_in_and_scale_up, R.anim.fade_out_and_scale_down)
        }*/
    }

    fun createSnackBar(view: View, content: String) {
        Snackbar.make(view, content, Snackbar.LENGTH_SHORT).show()
    }
}
