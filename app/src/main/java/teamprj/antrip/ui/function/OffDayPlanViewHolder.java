package teamprj.antrip.ui.function;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

import teamprj.antrip.R;

public class OffDayPlanViewHolder extends ChildViewHolder {
    private TextView mTextView;

    public OffDayPlanViewHolder(View itemView) {
        super(itemView);

        mTextView = itemView.findViewById(R.id.textView);

        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 일차별 텍스트를 클릭시 적용할 메소드 부분
            }
        });
    }

    public void bind(OffDayPlan offDayPlan){
        mTextView.setText(offDayPlan.name);
    }
}
