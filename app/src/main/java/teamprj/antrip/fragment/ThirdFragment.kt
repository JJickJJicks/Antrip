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

class ThirdFragment : Fragment {
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
            iv_circleImage.setImageResource(R.drawable.img_path)
            tv_intro_fragment_title.text = "경로 계산"
            tv_intro_fragment_body.text = "어떻게 가야 할지 막막하신가요? \n 경로를 계산 해드립니다!"
            btn_next.visibility = View.GONE
            ll_rootLinearLayout.setBackgroundResource(R.drawable.frame_round_blue_card)
        }
        return layout
    }
}