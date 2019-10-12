package teamprj.antrip.ui.function;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

import teamprj.antrip.R;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ViewHolder>{
    private Context context;
    private ArrayList<HashMap<String, String>> noticeList;

    public NoticeAdapter(ArrayList<HashMap<String, String>> noticeList, Context context) {
        this.noticeList = noticeList;
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView main_title, sub_title, notice_id;

        public ViewHolder(View itemView) {
            super(itemView);
            main_title = itemView.findViewById(R.id.main_title);
            sub_title = itemView.findViewById(R.id.sub_title);
            notice_id = itemView.findViewById(R.id.notice_id);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        HashMap<String, String> notice = noticeList.get(position);
                        Intent intent = new Intent(context, NoticeArticleActivity.class);
                        intent.putExtra("notice_id", notice.get("id"));
                        intent.putExtra("main_title", notice.get("title"));
                        intent.putExtra("sub_title", notice.get("date"));
                        intent.putExtra("content", notice.get("content"));
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
        HashMap<String, String> notice = noticeList.get(position);
        holder.main_title.setText(notice.get("title"));
        holder.sub_title.setText(notice.get("date"));
        holder.notice_id.setText(notice.get("id"));

    }

    @Override
    public int getItemCount() {
        return noticeList.size();
    }
}
