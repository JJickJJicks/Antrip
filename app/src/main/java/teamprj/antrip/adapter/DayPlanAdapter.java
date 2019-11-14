package teamprj.antrip.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

import teamprj.antrip.R;
import teamprj.antrip.data.model.DayPlan;
import teamprj.antrip.data.model.Plans;
import teamprj.antrip.ui.function.DayPlanViewHolder;
import teamprj.antrip.ui.function.PlansViewHolder;

public class DayPlanAdapter extends ExpandableRecyclerViewAdapter<PlansViewHolder, DayPlanViewHolder> {

    public DayPlanAdapter(List<? extends ExpandableGroup> groups) {
        super(groups);
    }

    @Override
    public PlansViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.plans,parent,false);
        return new PlansViewHolder(v);
    }

    @Override
    public DayPlanViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.day_plan,parent,false);
        return new DayPlanViewHolder(v);
    }

    @Override
    public void onBindChildViewHolder(DayPlanViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        final DayPlan dayPlan = (DayPlan) group.getItems().get(childIndex);
        holder.bind(dayPlan);
    }

    @Override
    public void onBindGroupViewHolder(PlansViewHolder holder, int flatPosition, ExpandableGroup group) {
        final Plans plans = (Plans) group;
        holder.bind(plans);
    }
}
