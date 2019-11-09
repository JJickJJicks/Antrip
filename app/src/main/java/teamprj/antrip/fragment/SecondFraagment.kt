package com.test.ui_practice.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_frame.view.*
import teamprj.antrip.R

class SecondFraagment : Fragment {
    constructor() : super()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var layout: LinearLayout = inflater.inflate(R.layout.fragment_frame, container, false) as LinearLayout

        with(layout) {
            iv_circleImage.setImageResource(R.drawable.img_schedule)
            tv_intro_fragment_title.text = "일정 작성"
            tv_intro_fragment_body.text = "자신만의 일정을 저장하세요. \n 그리고 친구들과 일정을 공유 하세요!"
            btn_next.visibility = View.GONE
            ll_rootLinearLayout.setBackgroundResource(R.drawable.frame_round_darkblue_card)
        }
        return layout
    }
}