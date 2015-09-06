package com.example.demo.news.fragments.slidingmenu.left;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import net.xinhuamm.d0403.R;

import com.example.demo.news.activity.DynamicDetailActivity;
import com.example.demo.news.activity.MainActivity;
import com.example.demo.news.databeans.dynamic.DynamicData;
import com.example.demo.news.dataloaders.DynamicLoader;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class FragmentDynamic extends Fragment implements OnClickListener {

    SlidingMenu slidingMenu1;
    SlidingMenu slidingMenu2;
    private DynamicLoader loader;
    AsyncTask<String, Void, DynamicData> task;
    private DynamicData data;
    private Button[] buttons = new Button[18];
    private int cateSize = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root;
        root = inflater.inflate(R.layout.fragment_changes, container, false);
        ((MainActivity) getActivity()).getSlidingMenu2().setTouchModeAbove(
                SlidingMenu.TOUCHMODE_FULLSCREEN);
        slidingMenu1 = ((MainActivity) getActivity()).getSlidingMenu1();
        slidingMenu2 = ((MainActivity) getActivity()).getSlidingMenu2();
        ImageButton showLeft;
        ImageButton showRight;
        showLeft = (ImageButton) root.findViewById(R.id.btnShowLeft);
        showLeft.setOnClickListener(this);
        showRight = (ImageButton) root.findViewById(R.id.btnShowRight);
        showRight.setOnClickListener(this);
        buttons[0] = (Button) root.findViewById(R.id.btnKM);
        buttons[1] = (Button) root.findViewById(R.id.btnZT);
        buttons[2] = (Button) root.findViewById(R.id.btnQJ);
        buttons[3] = (Button) root.findViewById(R.id.btnYX);
        buttons[4] = (Button) root.findViewById(R.id.btnBS);
        buttons[5] = (Button) root.findViewById(R.id.btnCX);
        buttons[6] = (Button) root.findViewById(R.id.btnHH);
        buttons[7] = (Button) root.findViewById(R.id.btnWS);
        buttons[8] = (Button) root.findViewById(R.id.btnPE);
        buttons[9] = (Button) root.findViewById(R.id.btnXSPN);
        buttons[10] = (Button) root.findViewById(R.id.btnDL);
        buttons[11] = (Button) root.findViewById(R.id.btnDH);
        buttons[12] = (Button) root.findViewById(R.id.btnLJ);
        buttons[13] = (Button) root.findViewById(R.id.btnNJ);
        buttons[14] = (Button) root.findViewById(R.id.btnDQ);
        buttons[15] = (Button) root.findViewById(R.id.btnLC);
        buttons[16] = (Button) root.findViewById(R.id.btnMore1);
        buttons[17] = (Button) root.findViewById(R.id.btnMore2);
        if (MainActivity.isNetworkConnected(getActivity())) {
            loader = new DynamicLoader();

            task = new AsyncTask<String, Void, DynamicData>() {

                @Override
                protected DynamicData doInBackground(String... params) {
                    try {
                        data = getResource();

                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    return data;
                }
            };
            task.execute();
            try {
                data = task.get();
            } catch (InterruptedException | ExecutionException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            cateSize = data.getData().getCate().size();
            Log.e("size", String.valueOf(cateSize));
            if (cateSize < 18) {
                for (int i = 17; i >= cateSize; i--) {
                    buttons[i].setVisibility(View.GONE);
                }
            }
            for (int i = 0; i < cateSize; i++) {
                buttons[i].setOnClickListener(this);
                buttons[i].setText(data.getData().getCate().get(i).getName());
            }
        } else {
            Toast.makeText(getActivity(), "请检查您的网络后重试", Toast.LENGTH_SHORT).show();
        }
        return root;
    }

    private DynamicData getResource() throws IOException {
        String JSON = loader
                .readURL("http://api.jjjc.yn.gov.cn//jwapp//?service=Dynamic.index");
        DynamicData data = loader.getJSONDate(JSON);
        return data;

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), DynamicDetailActivity.class);
        switch (v.getId()) {
            case R.id.btnShowLeft:
                slidingMenu1.toggle();
                break;
            case R.id.btnShowRight:
                slidingMenu2.toggle();
                break;
            case R.id.btnKM:
                intent.putExtra("link", data.getData().getCate().get(0)
                        .getCate_link());
                startActivity(intent);
                break;
            case R.id.btnZT:
                intent.putExtra("link", data.getData().getCate().get(1)
                        .getCate_link());
                startActivity(intent);
                break;
            case R.id.btnQJ:
                intent.putExtra("link", data.getData().getCate().get(2)
                        .getCate_link());
                startActivity(intent);
                break;
            case R.id.btnYX:
                intent.putExtra("link", data.getData().getCate().get(3)
                        .getCate_link());
                startActivity(intent);
                break;
            case R.id.btnBS:
                intent.putExtra("link", data.getData().getCate().get(4)
                        .getCate_link());
                startActivity(intent);
                break;
            case R.id.btnCX:
                intent.putExtra("link", data.getData().getCate().get(5)
                        .getCate_link());
                startActivity(intent);
                break;
            case R.id.btnHH:
                intent.putExtra("link", data.getData().getCate().get(6)
                        .getCate_link());
                startActivity(intent);
                break;
            case R.id.btnWS:
                intent.putExtra("link", data.getData().getCate().get(7)
                        .getCate_link());
                startActivity(intent);
                break;
            case R.id.btnPE:
                intent.putExtra("link", data.getData().getCate().get(8)
                        .getCate_link());
                startActivity(intent);
                break;
            case R.id.btnXSPN:
                intent.putExtra("link", data.getData().getCate().get(9)
                        .getCate_link());
                startActivity(intent);
                break;
            case R.id.btnDL:
                intent.putExtra("link", data.getData().getCate().get(10)
                        .getCate_link());
                startActivity(intent);
                break;
            case R.id.btnDH:
                intent.putExtra("link", data.getData().getCate().get(11)
                        .getCate_link());
                startActivity(intent);
                break;
            case R.id.btnLJ:
                intent.putExtra("link", data.getData().getCate().get(12)
                        .getCate_link());
                startActivity(intent);
                break;
            case R.id.btnNJ:
                intent.putExtra("link", data.getData().getCate().get(13)
                        .getCate_link());
                startActivity(intent);
                break;
            case R.id.btnDQ:
                intent.putExtra("link", data.getData().getCate().get(14)
                        .getCate_link());
                startActivity(intent);
                break;
            case R.id.btnLC:
                intent.putExtra("link", data.getData().getCate().get(15)
                        .getCate_link());
                startActivity(intent);
                break;

            default:
                break;
        }
    }
}
