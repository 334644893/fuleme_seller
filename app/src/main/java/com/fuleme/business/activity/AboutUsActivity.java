package com.fuleme.business.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fuleme.business.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 关于我们
 */
public class AboutUsActivity extends AppCompatActivity {

    @Bind(R.id.tv_left)
    ImageView tvLeft;
    @Bind(R.id.tv_title)
    TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        ButterKnife.bind(this);
        tvTitle.setText("关于我们");
    }

    @OnClick({R.id.tv_left, R.id.tv_title})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_left:
                finish();
                break;
        }
    }
}
