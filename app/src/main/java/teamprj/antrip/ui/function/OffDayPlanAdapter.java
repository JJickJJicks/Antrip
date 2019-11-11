package teamprj.antrip.ui.function;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

import teamprj.antrip.R;

public class OffDayPlanAdapter extends ExpandableRecyclerViewAdapter<OffPlansViewHolder, OffDayPlanViewHolder> {

    public OffDayPlanAdapter(List<? extends ExpandableGroup> groups) {
        super(groups);
    }

    @Override
    public OffPlansViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.off_plans,parent,false);
        return new OffPlansViewHolder(v);
    }

    @Override
    public OffDayPlanViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.off_day_plan,parent,false);
        return new OffDayPlanViewHolder(v);
    }

    @Override
    public void onBindChildViewHolder(OffDayPlanViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        final OffDayPlan offDayPlan = (OffDayPlan) group.getItems().get(childIndex);
        holder.bind(offDayPlan);
    }

    @Override
    public void onBindGroupViewHolder(OffPlansViewHolder holder, int flatPosition, ExpandableGroup group) {
        final OffPlans offPlans = (OffPlans) group;
        holder.bind(offPlans);
    }
}
