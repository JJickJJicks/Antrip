package teamprj.antrip.data.model;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class OffPlans extends ExpandableGroup<OffDayPlan> {
    public OffPlans(String title, List<OffDayPlan> items) {
        super(title, items);
    }
}
