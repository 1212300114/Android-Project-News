package com.example.demo.news.fragments.slidingmenu.left;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

import net.xinhuamm.d0403.R;

public class FragmentWebsiteStatement extends Fragment {
    //效果图有但是没用到╮(╯▽╰)╭
    private ImageButton back;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root;
        root = inflater.inflate(R.layout.fragment_statement, container, false);
        back = (ImageButton) root.findViewById(R.id.btnBack);
        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
        return root;

    }
}
