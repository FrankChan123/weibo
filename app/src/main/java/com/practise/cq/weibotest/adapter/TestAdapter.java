package com.practise.cq.weibotest.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.practise.cq.weibotest.R;

import java.util.List;

/**
 * Created by CQ on 2016/5/18 0018.
 */
public class TestAdapter extends BaseAdapter {

    private Context mcontext;
    private List<String> datas;

    public TestAdapter(Context context, List<String> datas) {
        mcontext = context;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public String getItem(int i) {
        return datas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null ){
            holder = new ViewHolder();
            convertView = View.inflate(mcontext, R.layout.test_item, null);

            holder.tv = (TextView)convertView.findViewById(R.id.test_tv);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        String s = datas.get(i);
        holder.tv.setText(s);

        return convertView;
    }

    static class ViewHolder{
        private TextView tv;
    }
}
