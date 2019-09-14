package com.sogou.solopiapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

class CaseAdapter extends BaseAdapter {

    Context context;
    List<Case> data;

    CaseAdapter(Context context){
        data = new ArrayList<>();
        this.context = context;
    }

    public void setData(List<Case> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Case getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        CaseView caseView;
        Case _case = data.get(i);

        if (view == null){
            caseView = new CaseView(context);
        }else{
            caseView = (CaseView) view;
        }

        caseView.bind(_case);

        return caseView;
    }
}
