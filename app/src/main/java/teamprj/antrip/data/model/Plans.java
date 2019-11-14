package teamprj.antrip.data.model;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class Plans extends ExpandableGroup<DayPlan> {
    public Plans(String title, List<DayPlan> items) {
        super(title, items);
    }
}
