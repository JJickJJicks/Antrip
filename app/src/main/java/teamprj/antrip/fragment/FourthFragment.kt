package com.test.ui_practice.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.test.ui_practice.interpolator.BounceInterpolator
import kotlinx.android.synthetic.main.fragment_frame.view.*
import teamprj.antrip.R
import teamprj.antrip.ui.login.LoginActivity

class FourthFragment : Fragment {
    private lateinit var mContext: Context
    private lateinit var mLayout: LinearLayout
    private lateinit var bounceAnimation: Animation
    private lateinit var interpolator: BounceInterpolator

    constructor() : super()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        bounceAnimation = AnimationUtils.loadAnimation(mContext, R.anim.bounce)
        interpolator = BounceInterpolator(0.2, 20.0)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mLayout = inflater.inflate(R.layout.fragment_frame, container, false) as LinearLayout
        bounceAnimation.interpolator = interpolator

        with(mLayout) {
            iv_circleImage.setImageResource(R.drawable.img_travel_world)
            tv_intro_fragment_title.text = "시작하기"
            tv_intro_fragment_body.text = "이제 자신만의 멋진 여행 계획을 작성 해 보세요."
            btn_next.visibility = View.VISIBLE
            ll_rootLinearLayout.setBackgroundResource(R.drawable.frame_round_yellow_card)

            btn_next.setOnClickListener { v ->
                startActivity(Intent(mContext, LoginActivity::class.java))
                activity?.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            }
        }
        return mLayout
    }

    override fun onResume() {
        super.onResume()

        with(mLayout) {
            btn_next.startAnimation(bounceAnimation)
            //iv_circleImage.startAnimation(bounceAnimation)
        }

    }
}