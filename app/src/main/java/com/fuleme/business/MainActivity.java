//package com.fuleme.business;
//
//import android.annotation.TargetApi;
//import android.app.ActivityOptions;
//import android.content.Intent;
//import android.os.Build;
//import android.os.Bundle;
//
//import com.fuleme.business.common.BaseActivity;
//import com.fuleme.business.fragment.FragmentActivity;
//
//import butterknife.ButterKnife;
//
//public class MainActivity extends BaseActivity {
//
//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        startActivity(new Intent(MainActivity.this, FragmentActivity.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
//        finish();
//    }
//
//}
