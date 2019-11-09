package teamprj.antrip.ui.intro

import android.animation.ArgbEvaluator
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.test.ui_practice.transformer.ViewPagerTransformer
import kotlinx.android.synthetic.main.activity_intro.*
import teamprj.antrip.R
import teamprj.antrip.adapter.PagerAdapter

class IntroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        vp_viewPager.adapter = PagerAdapter(supportFragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
        vp_viewPager.setPageTransformer(false,  ViewPagerTransformer())
        vp_viewPager.currentItem = 0

        vp_viewPager.addOnPageChangeListener(mOnPageChangeListener)
    }

    var mOnPageChangeListener = object : ViewPager.OnPageChangeListener {
        private var color = -1
        private val argbEvaluator = ArgbEvaluator()
        override fun onPageScrollStateChanged(state: Int) {
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
          /*  color = argbEvaluator.evaluate(positionOffset, vp_viewPager[position].rootView.background.,  vp_viewPager[position + 1].rootView.background) as Int
            cl_rootConstraintLayout.setBackgroundColor(color)*/
        }

        override fun onPageSelected(position: Int) {
            when(position) {
                0 -> {
                    firstDot.setImageResource(R.drawable.tab_indicator_selected)
                    secodDot.setImageResource(R.drawable.tab_indicator_default)
                    thirdDot.setImageResource(R.drawable.tab_indicator_default)
                    fourthDot.setImageResource(R.drawable.tab_indicator_default)
                }
                1 -> {
                    firstDot.setImageResource(R.drawable.tab_indicator_default)
                    secodDot.setImageResource(R.drawable.tab_indicator_selected)
                    thirdDot.setImageResource(R.drawable.tab_indicator_default)
                    fourthDot.setImageResource(R.drawable.tab_indicator_default)
                }
                2 ->{
                    firstDot.setImageResource(R.drawable.tab_indicator_default)
                    secodDot.setImageResource(R.drawable.tab_indicator_default)
                    thirdDot.setImageResource(R.drawable.tab_indicator_selected)
                }
                3 ->{
                    firstDot.setImageResource(R.drawable.tab_indicator_default)
                    secodDot.setImageResource(R.drawable.tab_indicator_default)
                    thirdDot.setImageResource(R.drawable.tab_indicator_default)
                    fourthDot.setImageResource(R.drawable.tab_indicator_selected)
                }
            }
        }
    }


}
