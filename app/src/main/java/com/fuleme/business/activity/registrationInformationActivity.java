package com.fuleme.business.activity;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fuleme.business.R;
import com.fuleme.business.common.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 注册信息
 */
public class RegistrationInformationActivity extends BaseActivity {
    @Bind(R.id.viewpager)
    ViewPager viewpager;

    private View view1, view2, view3, view4;
    private List<View> viewList = new ArrayList<View>();
    PagerAdapter pagerAdapter = new PagerAdapter() {
        // 实例化一个页卡
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(viewList.get(position));
            return viewList.get(position);
        }

        // 销毁一个页卡
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(viewList.get(position));
        }

        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ButterKnife.bind(this);
        LayoutInflater lf = getLayoutInflater().from(this);
        view1 = lf.inflate(R.layout.activity_registration_information_1, null);
        view2 = lf.inflate(R.layout.activity_registration_information_2, null);
        view3 = lf.inflate(R.layout.activity_registration_information_3, null);
        view4 = lf.inflate(R.layout.activity_registration_information_4, null);

        viewList.add(view1);
        viewList.add(view2);
        viewList.add(view3);
        viewList.add(view4);
        viewpager.setAdapter(pagerAdapter);
    }


}
