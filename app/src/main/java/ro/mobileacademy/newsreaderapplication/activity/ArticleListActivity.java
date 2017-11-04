package ro.mobileacademy.newsreaderapplication.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.android.volley.toolbox.StringRequest;

import ro.mobileacademy.newsreaderapplication.R;
import ro.mobileacademy.newsreaderapplication.fragments.NewStoriesFragment;
import ro.mobileacademy.newsreaderapplication.fragments.TopStoriesFragment;
import ro.mobileacademy.newsreaderapplication.networking.HackerNewsAPI;

/**
 * Created by valerica.plesu on 04/11/2017.
 */

public class ArticleListActivity extends AppCompatActivity {

    private static final String TAG = ArticleListActivity.class.getSimpleName();
    private ViewPager mPager;
    private MyPagerAdapter adapter;
    private TabLayout tabLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_article_list);

        //inflate toolbar and tablayout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Top Stories"));
        tabLayout.addTab(tabLayout.newTab().setText("New Stories"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        mPager = (ViewPager) findViewById(R.id.view_pager);
        adapter = new MyPagerAdapter(getSupportFragmentManager(), 2);
        mPager.setAdapter(adapter);

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "onPageSelected");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // link this tablayout with our viewpager
        tabLayout.setupWithViewPager(mPager);

    }


    // create pager adapter
    public static class MyPagerAdapter extends FragmentPagerAdapter {

        private int tabs;

        private MyPagerAdapter (FragmentManager fragmentManager, int numOfTabs) {
            super(fragmentManager);
            tabs = numOfTabs;
        }

        // returns the fragment we want to display per page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: // fragment o -> this will show TopStoriesFragment
                    return TopStoriesFragment.getInstance(HackerNewsAPI.TOP_STORIES_ENDPOINT);
                case 1: // fragment 1 -> will show NewStoriesFragment
                    return NewStoriesFragment.getInstance(HackerNewsAPI.NEW_STORIES_ENDPOINT);
            }
            return null;
        }

        @Override
        public int getCount() {
            return tabs;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title = "";
            switch (position) {
                case 0:
                    title = "Top Stories";
                    break;
                case 1:
                    title = "New Stories";
                    break;
            }
            return title;
        }
    }
}
