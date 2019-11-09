package teamprj.antrip.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import teamprj.antrip.R;
import teamprj.antrip.data.model.Notice;
import teamprj.antrip.ui.function.NoticeArticleActivity;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ViewHolder>{
    private Context context;
    private ArrayList<Notice> noticeList;

    public NoticeAdapter(ArrayList<Notice> noticeList, Context context) {
        this.context = context;
       this.noticeList = noticeList;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView main_title, sub_title, contents;

        public ViewHolder(View itemView) {
            super(itemView);
            main_title = itemView.findViewById(R.id.main_title);
            sub_title = itemView.findViewById(R.id.sub_title);
            contents = itemView.findViewById(R.id.content);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        Intent intent = new Intent(context, NoticeArticleActivity.class);
                        intent.putExtra("main_title", main_title.getText().toString());
                        intent.putExtra("sub_title", sub_title.getText().toString());
                        intent.putExtra("contents", contents.getText().toString());
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
        holder.main_title.setText(noticeList.get(position).getTitle());
        holder.sub_title.setText(noticeList.get(position).getDate());
        holder.contents.setText(noticeList.get(position).getContent());

    }

    @Override
    public int getItemCount() {
        return noticeList.size();
    }
}
