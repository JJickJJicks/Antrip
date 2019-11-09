package teamprj.antrip.ui.function;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import teamprj.antrip.R;

public class ItemTouchHelperCallback extends ItemTouchHelper.Callback {

    public interface OnItemMoveListener {
        void onItemMove(int fromPosition, int toPosition);
        void onItemRemove(int Position);
    }

    private final OnItemMoveListener mItemMoveListener;
    public ItemTouchHelperCallback(OnItemMoveListener listener) {
        mItemMoveListener = listener;
    }


    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        if (viewHolder.getItemViewType() == ExpandableListAdapter.DATA) {
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            int swipeFlags = ItemTouchHelper.END;
            return makeMovementFlags(dragFlags, swipeFlags);
        }
        return 0;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        mItemMoveListener.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int swipeDir) {
        mItemMoveListener.onItemRemove(viewHolder.getAdapterPosition());
    }
}
