package teamprj.antrip.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.test.ui_practice.fragment.FirstFragment
import com.test.ui_practice.fragment.FourthFragment
import com.test.ui_practice.fragment.SecondFraagment
import com.test.ui_practice.fragment.ThirdFragment

class PagerAdapter : FragmentPagerAdapter{

    constructor(fm: FragmentManager, behavior: Int) : super(fm, behavior)

    override fun getItem(position: Int): Fragment {
        when(position) {
            0 -> return FirstFragment()
            1 -> return SecondFraagment()
            2 -> return ThirdFragment()
            else -> return FourthFragment()
        }
    }

    override fun getCount(): Int {
        return 4
    }
}