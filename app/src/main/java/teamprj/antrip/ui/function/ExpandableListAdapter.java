package teamprj.antrip.ui.function;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import teamprj.antrip.R;

public class ExpandableListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ItemTouchHelperCallback.OnItemMoveListener {
    static final int HEADER = 0;
    static final int CHILD = 1;
    static final int DATA = 2;

    private List<Item> data;
    private OnStartDragListner mStartDragListener;

    private void show(RecyclerView.ViewHolder holder) {
        Context context = holder.itemView.getContext();
        int index = holder.getAdapterPosition();

        Intent intent = new Intent(context, InputPlanActivity.class);
        intent.putExtra("position", index);
        context.startActivity(intent);
    }

    public interface OnStartDragListner {
        void onStartDrag(RecyclerView.ViewHolder holder);
    }

    public ExpandableListAdapter(List<Item> data, OnStartDragListner startDragListner) {
        this.data = data;
        mStartDragListener = startDragListner;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        View view;
        final Context context = parent.getContext();
        float dp = context.getResources().getDisplayMetrics().density;
        int subItemPaddingLeft = (int) (18 * dp);
        int subItemPaddingTopAndBottom = (int) (5 * dp);
        switch (type) {
            case HEADER:
                LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.list_header, parent, false);
                return new ListHeaderViewHolder(view);
            case CHILD:
                final Button itemButton = new Button(context);
                itemButton.setTextColor(0x88000000);
                itemButton.setTextSize(20);
                itemButton.setPadding(subItemPaddingLeft, subItemPaddingTopAndBottom, 0, subItemPaddingTopAndBottom);
                itemButton.setLayoutParams(
                        new ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT));
                return new RecyclerView.ViewHolder(itemButton) {
                };
            case DATA:
                LayoutInflater dataInflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = dataInflater.inflate(R.layout.list_data, parent, false);
                return new ListHeaderViewHolder(view);
        }
        return null;
    }

    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        final Item item = data.get(position);
        switch (item.type) {
            case HEADER:
                final ListHeaderViewHolder itemController = (ListHeaderViewHolder) holder;
                itemController.refferalItem = item;
                itemController.header_title.setText(item.name);
                if (item.invisibleChildren == null) {
                    itemController.btn_expand_toggle.setImageResource(R.drawable.circle_minus);
                } else {
                    itemController.btn_expand_toggle.setImageResource(R.drawable.circle_plus);
                }
                itemController.btn_expand_toggle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (item.invisibleChildren == null) {
                            item.invisibleChildren = new ArrayList<>();
                            int count = 0;
                            int pos = data.indexOf(itemController.refferalItem);
                            while (data.size() > pos + 1 && (data.get(pos + 1).type == CHILD || data.get(pos + 1).type == DATA)) {
                                item.invisibleChildren.add(data.remove(pos + 1));
                                count++;
                            }
                            notifyItemRangeRemoved(pos + 1, count);
                            itemController.btn_expand_toggle.setImageResource(R.drawable.circle_plus);
                        } else {
                            int pos = data.indexOf(itemController.refferalItem);
                            int index = pos + 1;
                            for (Item i : item.invisibleChildren) {
                                data.add(index, i);
                                index++;
                            }
                            notifyItemRangeInserted(pos + 1, index - pos - 1);
                            itemController.btn_expand_toggle.setImageResource(R.drawable.circle_minus);
                            item.invisibleChildren = null;
                        }
                    }
                });
                break;
            case CHILD:
                Button itemButton = (Button) holder.itemView;
                itemButton.setText(data.get(position).name);
                itemButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        show(holder);
                    }
                });
                break;
            case DATA:
                RelativeLayout tempLayout = (RelativeLayout) holder.itemView;
                TextView name = tempLayout.findViewById(R.id.data_name);
                name.setText(data.get(position).name);
                CheckBox checkBox = tempLayout.findViewById(R.id.data_accommodation);
                if (item.accommodation) {
                    checkBox.setVisibility(View.VISIBLE);
                    checkBox.setChecked(true);
                    checkBox.setEnabled(false);
                }

                holder.itemView.setOnLongClickListener(new View.OnLongClickListener(){
                    @Override
                    public boolean onLongClick(View v) {
                        mStartDragListener.onStartDrag(holder);
                        return false;
                    }
                });
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).type;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private static class ListHeaderViewHolder extends RecyclerView.ViewHolder {
        TextView header_title;
        ImageView btn_expand_toggle;
        Item refferalItem;

        public ListHeaderViewHolder(View itemView) {
            super(itemView);
            header_title = itemView.findViewById(R.id.header_title);
            btn_expand_toggle = itemView.findViewById(R.id.btn_expand_toggle);
        }
    }

    public static class Item {
        int type;
        public String name;
        LatLng latLng;
        boolean accommodation = false;
        List<Item> invisibleChildren;

        public Item(int type, String name) {
            this.type = type;
            this.name = name;
        }

        public Item(int type, String name, LatLng latLng, boolean accommodation) {
            this.type = type;
            this.name = name;
            this.latLng = latLng;
            this.accommodation = accommodation;
        }
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition >= toPosition) {
            int temp = fromPosition;
            fromPosition = toPosition;
            toPosition = temp;
        }
        for (int i = fromPosition; i <= toPosition; i++) {
            if (data.get(i).type != DATA || data.get(i).accommodation) {
                return;
            }
        }
        if (data.get(toPosition).type == DATA) {
            Collections.swap(data, fromPosition, toPosition);
            notifyItemMoved(fromPosition, toPosition);
        }
    }

    @Override
    public void onItemRemove(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    public void moveAccommodation(int position) {
        int firstPosition = 0;
        for (int i = position; i >= 0; i--) {
            if (data.get(i).type != DATA) {
                firstPosition = i + 1;
                break;
            }
        }
        Collections.swap(data, position, firstPosition);
        notifyItemMoved(position, firstPosition);
    }
}