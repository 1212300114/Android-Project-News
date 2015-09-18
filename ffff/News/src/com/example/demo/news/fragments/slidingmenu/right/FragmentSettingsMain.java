package com.example.demo.news.fragments.slidingmenu.right;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.demo.news.utils.ClearCache;
import com.nostra13.universalimageloader.utils.StorageUtils;

import net.xinhuamm.d0403.R;

import java.io.File;

public class FragmentSettingsMain extends Fragment implements OnClickListener {
    //设置页内容
    private View root;
    private Dialog dialogClear;
    private Dialog dialogUpstate;
    private TextView tvCache;

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_settings, container, false);
        initView();
        initDialog();
        getCacheSize();
        return root;
    }

    //初始化view
    private void initView() {
        root.findViewById(R.id.btnBack).setOnClickListener(this);
        root.findViewById(R.id.checkUpdate).setOnClickListener(this);
        root.findViewById(R.id.clear).setOnClickListener(this);
        root.findViewById(R.id.reflect).setOnClickListener(this);
        tvCache = (TextView) root.findViewById(R.id.tvCacheSize);
    }

    //初始化2个dialog
    private void initDialog() {


        dialogClear = new Dialog(getActivity(), R.style.MyDialog1);
        dialogUpstate = new Dialog(getActivity(), R.style.MyDialog1);
        dialogClear.setContentView(R.layout.clear_dialog_layout);
        dialogUpstate.setContentView(R.layout.check_update_dialog_layout);
        dialogClear.findViewById(R.id.dialogClearNo).setOnClickListener(
                this);
        dialogClear.findViewById(R.id.dialogClearYes).setOnClickListener(
                this);
        dialogUpstate.findViewById(R.id.dialogUpstateNo).setOnClickListener(
                this);
        dialogUpstate.findViewById(R.id.dialogUpstateYes).setOnClickListener(
                this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                getActivity().finish();
                break;
            case R.id.checkUpdate:
                dialogUpstate.show();
                break;
            case R.id.clear:
                dialogClear.show();
                break;
            case R.id.reflect:
                getFragmentManager().beginTransaction().replace(R.id.container,
                        new FragmentReflect()).addToBackStack(null).commit();
                break;
            case R.id.dialogClearNo:
                dialogClear.cancel();
                break;
            case R.id.dialogClearYes:
                Log.e("", ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                clearCache();
                dialogClear.cancel();
                break;
            case R.id.dialogUpstateNo:
                dialogUpstate.cancel();
                break;
            case R.id.dialogUpstateYes:
                dialogUpstate.cancel();
                break;
        }

    }

    //清理缓存的方法
    private void clearCache() {

        ClearCache.cleanApplicationData(getActivity());
        File imageCacheDir = StorageUtils.getOwnCacheDirectory(getActivity(),
                "JW/Cache");
        ClearCache.cleanCustomCache(String.valueOf(imageCacheDir));
        getCacheSize();
    }

    //获取缓存大小的办法--包含imageloader 缓存的图片以及整个app的数据-
    private void getCacheSize() {

        @SuppressLint("SdCardPath")
        File packageDir = new File("/data/data/" + getActivity().getPackageName());
        File imageCacheDir = StorageUtils.getOwnCacheDirectory(getActivity(),
                "JW/Cache");
        long imageCacheSize = 0;
        long packageCacheSize = 0;
        try {
            imageCacheSize = ClearCache.getFolderSize(imageCacheDir);
            Log.e("image", String.valueOf(imageCacheSize));
            packageCacheSize = ClearCache.getFolderSize(packageDir);
            Log.e("page", String.valueOf(packageCacheSize));
        } catch (Exception e) {
            e.printStackTrace();
        }
        String cacheText = ClearCache.getFormatSize(imageCacheSize + packageCacheSize);
        if (null != cacheText) {
            tvCache.setText(cacheText);
        }

    }
}
