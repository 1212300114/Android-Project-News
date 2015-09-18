package com.example.demo.news.fragments.slidingmenu.right;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import net.xinhuamm.d0403.R;

public class FragmentReflect extends Fragment {
    //意见反馈也- 没给接口不知道怎么办
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root;
        root = inflater.inflate(R.layout.fragment_settings_reflect, container,
                false);
        ImageButton back;
        back = (ImageButton) root.findViewById(R.id.btnBack);
        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
        Button post;
        post = (Button) root.findViewById(R.id.btnPost);
        post.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();

            }
        });

        return root;
    }

}
