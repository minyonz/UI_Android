package com.kh.ui_android;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class StudentAdapter extends BaseAdapter {

    Context context;
    int info_view;
    ArrayList<StudentVo> data;

    public StudentAdapter(Context context, int info_view, ArrayList<StudentVo> vo) {
        this.context = context;
        this.info_view = info_view;
        this.data = vo;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            convertView = inflater.inflate(info_view, null);
        }
        // 아이디 구하기
        TextView infoNum = convertView.findViewById(R.id.infoNum);
        TextView infoName = convertView.findViewById(R.id.infoName);
        TextView infoYear = convertView.findViewById(R.id.infoYear);

        StudentVo vo = data.get(position);

        // vo에서 가져온 값으로 설정
        infoNum.setText(vo.getSno());
        infoName.setText(vo.getSname());
        infoYear.setText(String.valueOf(vo.getSyear()));

        return convertView;
    }


}
