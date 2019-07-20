package teamprj.antrip.ui.function;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import teamprj.antrip.R;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ViewHolder>{
    private Context context;
    private String[] main_title;
    private String[] sub_title;

    public NoticeAdapter(String[] main_title, String[] sub_title, Context context) {
        this.context = context;
        this.main_title = main_title;
        this.sub_title = sub_title;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView main_title, sub_title;

        public ViewHolder(View itemView) {
            super(itemView);
            main_title = itemView.findViewById(R.id.main_title);
            sub_title = itemView.findViewById(R.id.sub_title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        String parameter_first = main_title.getText().toString();
                        String parameter_second = sub_title.getText().toString();
                        Intent intent = new Intent(context, NoticeArticleActivity.class);
                        intent.putExtra("main_title", main_title.getText().toString());
                        intent.putExtra("sub_title", sub_title.getText().toString());
                        context.startActivity(intent);
                    }
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) { //notice_item을 뷰홀더로 생성!
        View view = LayoutInflater.from(context).inflate(R.layout.activity_notice_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NoticeAdapter.ViewHolder holder, int position) {
        holder.main_title.setText(this.main_title[position]);
        holder.sub_title.setText(this.sub_title[position]);

    }

    @Override
    public int getItemCount() {
        return main_title.length;
    }
}
