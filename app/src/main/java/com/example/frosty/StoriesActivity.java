package com.example.frosty;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.frosty.Adapters.StoryPageTransition;
import com.example.frosty.Fragment.HomeFragment;
import com.example.frosty.Fragment.StoryFragment;
import com.google.api.Distribution;

import java.sql.Time;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class StoriesActivity extends FragmentActivity {



    private ViewPager2 viewPager;
    public static int initposition=0;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private FragmentStateAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stories);

        // Instantiate a ViewPager2 and a PagerAdapter.
        viewPager = findViewById(R.id.pager);
        viewPager.setPageTransformer(new StoryPageTransition());
        pagerAdapter = new StorySlidePagerAdapter(this, new StorySlidePagerAdapter.StoryPageListener() {
            @Override
            public void OnStoryEnd(int position)

            {   position=position+1;
                    if(position<HomeFragment.list.size()) {
                viewPager.setCurrentItem(position);
                }
                else
                    { finish();}
                }
        });
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(initposition, false);
    }

    public static class StorySlidePagerAdapter extends FragmentStateAdapter {
        private StoryPageListener listener;
        public StorySlidePagerAdapter(FragmentActivity fa,StoryPageListener listener) {

            super(fa);
            this.listener=listener;
        }

        @Override
        public Fragment createFragment(int position) {
            return new StoryFragment(HomeFragment.list.get(position),position,listener);
        }

        @Override
        public int getItemCount() {
            return HomeFragment.list.size();
        }

        public interface StoryPageListener
        {
            void OnStoryEnd(int position);
        }


    }

}
