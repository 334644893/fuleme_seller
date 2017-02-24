package com.fuleme.business.common;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;


import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/1/10.
 */

public class BaseActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        ButterKnife.bind(this);

    }

}
