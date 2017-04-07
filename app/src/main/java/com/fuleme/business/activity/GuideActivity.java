package com.fuleme.business.activity;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fuleme.business.R;
import com.fuleme.business.common.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class GuideActivity extends BaseActivity {
    @Bind(R.id.m_view_pager)
    ViewPager mViewPager;
    private View view1, view2, view3;
    private TextView click;
    private List<View> viewList = new ArrayList<View>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);
        initViewPager();
    }

    public void initViewPager() {
        LayoutInflater lf = getLayoutInflater().from(this);
        view1 = lf.inflate(R.layout.item_guide, null);
        view2 = lf.inflate(R.layout.item_guide, null);
        view3 = lf.inflate(R.layout.item_guide, null);
        view1.findViewById(R.id.iv_imageView).setBackgroundResource(R.mipmap.guide_1);
        view2.findViewById(R.id.iv_imageView).setBackgroundResource(R.mipmap.guide_2);
        view3.findViewById(R.id.iv_imageView).setBackgroundResource(R.mipmap.guide_3);
        viewList.add(view1);
        viewList.add(view2);
        viewList.add(view3);
        mViewPager.setAdapter(pagerAdapter);
        //init view3
        click = (TextView) view3.findViewById(R.id.tv_click);
        click.setVisibility(View.VISIBLE);
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

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
}
