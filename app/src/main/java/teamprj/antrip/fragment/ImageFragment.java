package teamprj.antrip.fragment;

import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import teamprj.antrip.R;
import teamprj.antrip.ui.MainActivity;
import teamprj.antrip.ui.function.CustomTextView;
import teamprj.antrip.ui.function.TravelInfoActivity;

public class ImageFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image, container, false);

        ImageView imageView = view.findViewById(R.id.iv_card_imageView_recommend);
        TextView textView = view.findViewById(R.id.tv_title_recommend);

        if (getArguments() != null) {
            Bundle args = getArguments();
            final String tripName = args.getString("text");
            // MainActivity에서 받아온 Resource를 ImageView에 셋팅
            imageView.setImageResource(args.getInt("imgRes"));
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), TravelInfoActivity.class);
                    intent.putExtra("name", tripName);
                    startActivity(intent);
                }
            });
            textView.setText(tripName);
            textView.setTextSize(40);
        }

        return view;
    }
}