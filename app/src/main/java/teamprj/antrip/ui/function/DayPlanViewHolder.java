package teamprj.antrip.ui.function;

import android.view.View;
import android.widget.TextView;

import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

import teamprj.antrip.R;
import teamprj.antrip.data.model.DayPlan;

public class DayPlanViewHolder extends ChildViewHolder {
    private TextView mTextView;

    public DayPlanViewHolder(View itemView) {
        super(itemView);

        mTextView = itemView.findViewById(R.id.textView);

        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 일차별 텍스트를 클릭시 적용할 메소드 부분
            }
        });
    }

    public void bind(DayPlan dayPlan){
        mTextView.setText(dayPlan.name);
    }
}
