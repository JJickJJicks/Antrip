package teamprj.antrip.ui.function;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

import teamprj.antrip.R;

public class ArticleFragment extends Fragment {
    private Context context;
    private String title, content;
    View view;

    public ArticleFragment(){

    }

    @Override
    public void onAttach(@NonNull Context context) {
        this.context = context;
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static ArticleFragment newInstance(){
        ArticleFragment articleFragment = new ArticleFragment();
        return articleFragment;
    }

    public static ArticleFragment newInstance(String title, String content){
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("content", content);

        ArticleFragment articleFragment = new ArticleFragment();
        articleFragment.setArguments(args);
        return articleFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_article_write_edit, container, false);
        if(getArguments() != null){
            String title = getArguments().getString("title");
            String content = getArguments().getString("content");
            EditText edit_title = view.findViewById(R.id.input_main_title);
            EditText edit_content = view.findViewById(R.id.input_content);
            edit_title.setText(title);
            edit_content.setText(content);
        }
        return view;
    }
}
