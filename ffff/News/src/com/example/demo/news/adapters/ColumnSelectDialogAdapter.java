package com.example.demo.news.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import com.example.demo.news.databeans.ColumnEntity;
import com.example.demo.news.utils.Constants;

import net.xinhuamm.d0403.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 123456 on 2015/9/17.
 */
public class ColumnSelectDialogAdapter extends BaseAdapter {


    //栏目选择列表的适配器--算法可能不够好。。
    private List<ColumnEntity.DataEntity.CateEntity> entities;//传入数据用于创建view
    private Context context;
    private List<ColumnEntity.DataEntity.CateEntity> entitiesResponse;//返回的数据用于向外部放回被选取的栏目的数据
    private ArrayList<String> names;//被选取的栏目标题

    public ColumnSelectDialogAdapter(Context context) {
        this.context = context;
        this.entities = new ArrayList<>();
        this.names = new ArrayList<>();
        this.entitiesResponse = new ArrayList<>();
    }

    public void setChoice(ArrayList<String> names) {
        //设置被选择栏目的内容
        this.names = names;
        this.entitiesResponse.clear();
        for (int i = 0; i < names.size(); i++) {
            for (int j = 0; j < entities.size(); j++) {
                if (names.get(i).equals(entities.get(j).getName())) {
                    entitiesResponse.add(entities.get(j));
                }
            }
        }
        notifyDataSetChanged();
    }

    public void setData(List<ColumnEntity.DataEntity.CateEntity> entities) {
        //设置所以栏目的数据
        this.entities = entities;
        for (int i = 0; i < entities.size(); i++) {
            this.entitiesResponse.add(entities.get(i));
            //不能直接entitiesResponse = entities 因为给的是地址。。。
        }
        notifyDataSetChanged();
    }

    public List<ColumnEntity.DataEntity.CateEntity> getEntitiesResponse() {
        return entitiesResponse;
        //返回被选择的数据
    }

    @Override
    public int getCount() {
        //返回列表size
        if (entities.size() % 2 == 0) {
            return entities.size() / 2;
        } else {
            return (entities.size() + 1) / 2;
        }
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //初始化view
        final ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_dialog_column_select, null);
            viewHolder = new ViewHolder();
            viewHolder.left = (CheckBox) convertView.findViewById(R.id.cbLeft);
            viewHolder.right = (CheckBox) convertView.findViewById(R.id.cbRight);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        initView(position, viewHolder);

        return convertView;
    }

    private void initView(final int position, final ViewHolder viewHolder) {
        viewHolder.left.setText(entities.get(position * 2).getName());
        viewHolder.left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewHolder.left.isChecked()) {
                    viewHolder.left.setTextColor(context.getResources().getColor(R.color.dialog));
                    entitiesResponse.add(entities.get(position * 2));
                    names.add(entities.get(position * 2).getName());
                } else {
                    viewHolder.left.setTextColor(context.getResources().getColor(R.color.black));
                    entitiesResponse.remove(entities.get(position * 2));
                    names.remove(entities.get(position * 2).getName());
                }

            }
        });
        //如果给的数据是奇数 的话右侧按钮数据就会不存在所以要设置成gone
        if (null != entities.get(position * 2 + 1).getName()) {
            viewHolder.right.setText(entities.get(position * 2 + 1).getName());
            viewHolder.right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (viewHolder.right.isChecked()) {
                        viewHolder.right.setTextColor(context.getResources().getColor(R.color.dialog));
                        //click 后处理返回数据
                        entitiesResponse.add(entities.get(position * 2 + 1));
                        names.add(entities.get(position * 2 + 1).getName());
                    } else {
                        viewHolder.right.setTextColor(context.getResources().getColor(R.color.black));
                        entitiesResponse.remove(entities.get(position * 2 + 1));
                        names.remove(entities.get(position * 2 + 1).getName());
                    }

                }
            });
        } else {
            viewHolder.right.setVisibility(View.GONE);
        }
        String left = entities.get(position * 2).getName();
        String right = "";
        boolean flagLeft = false;
        boolean flagRight = false;
        if (null != entities.get(position * 2 + 1).getName()) {
            right = entities.get(position * 2 + 1).getName();
        }
        //获取到当前项左右2个button的name并且与选择的names数据进行比对如果存在就给他true
        if (names.size() > 0) {
            for (int i = 0; i < names.size(); i++) {
                if (!names.get(i).equals(Constants.FIRST)) {
                    if (left.equals(names.get(i))) {
                        flagLeft = true;
                    }
                    if (right.equals(names.get(i))) {
                        flagRight = true;
                    }
                }
            }
            Log.e("----", left + "-----" + right + "-------" + position);
            //根据flag 来设置每个button的状态
            if (!flagLeft) {
                viewHolder.left.setChecked(false);
                viewHolder.left.setTextColor(context.getResources().getColor(R.color.black));
            } else {
                viewHolder.left.setChecked(true);
                viewHolder.left.setTextColor(context.getResources().getColor(R.color.dialog));
            }
            if (!flagRight) {
                viewHolder.right.setChecked(false);
                viewHolder.right.setTextColor(context.getResources().getColor(R.color.black));
            } else {
                viewHolder.right.setChecked(true);
                viewHolder.right.setTextColor(context.getResources().getColor(R.color.dialog));
            }
            //首页固定。
            if (entities.get(position).getName().equals(Constants.FIRST)) {
                viewHolder.left.setClickable(false);
                viewHolder.left.setChecked(true);
                viewHolder.left.setTextColor(context.getResources().getColor(R.color.dialog));
            }
        }
    }

    private static class ViewHolder {
        private CheckBox left;
        private CheckBox right;
    }
}
