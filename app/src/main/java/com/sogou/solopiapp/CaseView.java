package com.sogou.solopiapp;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

class CaseView extends LinearLayout {

    TextView name, module, project;
    ImageView file;

    public CaseView(Context context) {
        this(context, null);
    }

    public CaseView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.case_item, this);
        name = findViewById(R.id.name);
        module = findViewById(R.id.module);
        project = findViewById(R.id.project);
        file = findViewById(R.id.file);
    }

    public void bind(Case aCase) {
        name.setText(aCase.name);
        module.setText(aCase.module);
        project.setText(aCase.project);
        if (aCase.exists){
            file.setVisibility(VISIBLE);
        }else{
            file.setVisibility(INVISIBLE);
        }
    }
}
