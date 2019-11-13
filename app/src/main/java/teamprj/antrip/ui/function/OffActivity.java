package teamprj.antrip.ui.function;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import teamprj.antrip.R;
import teamprj.antrip.adapter.OffDayPlanAdapter;
import teamprj.antrip.data.model.OffDayPlan;
import teamprj.antrip.data.model.OffPlans;

public class OffActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.off_plan_list);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<OffPlans> offPlansArrayList = new ArrayList<>();

        ArrayList<OffDayPlan> offDayPlanArrayList = new ArrayList<>();
        offDayPlanArrayList.add(new OffDayPlan("1일차 ( A ~ )"));
        offDayPlanArrayList.add(new OffDayPlan("2일차 ( B ~ )"));
        offDayPlanArrayList.add(new OffDayPlan("3일차 ( C ~ )"));
        offDayPlanArrayList.add(new OffDayPlan("4일차 ( D ~ )"));

        offPlansArrayList.add(new OffPlans("파리 ( 3박 4일 0000-00-01 ~ 04 )", (List<OffDayPlan>) offDayPlanArrayList.clone()));

        offDayPlanArrayList.clear();

        offDayPlanArrayList.add(new OffDayPlan("1일차 ( E ~ )"));
        offDayPlanArrayList.add(new OffDayPlan("2일차 ( F ~ )"));
        offDayPlanArrayList.add(new OffDayPlan("3일차 ( G ~ )"));

        offPlansArrayList.add(new OffPlans("릴 ( 2박 3일 0000-00-04 ~ 06 )", (List<OffDayPlan>) offDayPlanArrayList.clone()));

        OffDayPlanAdapter adapter = new OffDayPlanAdapter(offPlansArrayList);
        recyclerView.setAdapter(adapter);
    }
}
