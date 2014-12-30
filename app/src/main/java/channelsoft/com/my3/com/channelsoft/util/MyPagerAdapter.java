package channelsoft.com.my3.com.channelsoft.util;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by yuanshun on 2014/12/30.
 */
public class MyPagerAdapter extends PagerAdapter {

    private ArrayList<View> views;
    private ArrayList<String> titles;

    public MyPagerAdapter(ArrayList<View> views, ArrayList<String> titles) {
        this.views = views;
        this.titles = titles;
    }

    @Override
    public int getCount() {
        return this.views.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
        ((ViewPager)container).removeView(views.get(position));
    }

    @Override
    public CharSequence getPageTitle(int position) {
//        return super.getPageTitle(position);
        return titles.get(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
//        return super.instantiateItem(container, position);
        ((ViewPager)container).addView(views.get(position));
        return views.get(position);
    }

}
