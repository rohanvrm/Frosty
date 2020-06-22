package com.example.frosty.Fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.frosty.Models.StoryModel;
import com.example.frosty.R;
import com.example.frosty.StoriesActivity;

import java.util.List;
import java.util.Timer;

import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class StoryFragment extends Fragment {


    public StoryFragment() {
        // Required empty public constructor
    }

    private StoryModel storyModel;

    public StoryFragment(StoryModel storyModel,int position, StoriesActivity.StorySlidePagerAdapter.StoryPageListener listener) {
        this.listener=listener;
        this.storyModel = storyModel;
        this.position=position;
    }

    private StoriesActivity.StorySlidePagerAdapter.StoryPageListener listener;
    private int position;

    private ImageView imageview;
    private LinearLayout progresscontainer;
   // public static List<String> storyModel.getImages();
    private ProgressBar defaultprgressbar;
    private Timer timer;
    private TimerTask timerTask;
    private int progresscount=0;
    private int progressbarIndex=0;
    private boolean timerON=true;

    private float    upperlimit,lowerlimit;

    private TextView username;
    private CircleImageView profile_image;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_story, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);
        setProgressbar();
        Glide.with(this).load(storyModel.getImages().get(progressbarIndex)).placeholder(R.drawable.profileplaceholder).into(imageview);

        setControl();
    }

    @Override
    public void onResume() {
        super.onResume();
        setTimer();
    }

    @Override
    public void onPause() {
        super.onPause();
        timer.cancel();
        timerTask.cancel();
    }

    private void reset() {
        if (progresscount == 100 && progressbarIndex == progresscontainer.getChildCount() )
            {    progressbarIndex = 0;
                progresscount = 0;
                for(int i=0;i<progresscontainer.getChildCount();i++)
                {
                    ((ProgressBar)progresscontainer.getChildAt(i)).setProgress(0);
                }
                Glide.with(this).load(storyModel.getImages().get(progressbarIndex)).placeholder(R.drawable.profileplaceholder).into(imageview);

            }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setControl()
    {

        float widthproportion= (getResources().getDisplayMetrics().widthPixels/100.0f)*25;
        upperlimit=getResources().getDisplayMetrics().widthPixels -widthproportion;
        lowerlimit= widthproportion;

        imageview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:

                        if(event.getX()<=lowerlimit && progressbarIndex>0)
                        {
                            //prev image
                            progresscount++;
                            ((ProgressBar) progresscontainer.getChildAt(progressbarIndex)).setProgress(0);
                            progressbarIndex--;
                            progresscount++;
                            ((ProgressBar) progresscontainer.getChildAt(progressbarIndex)).setProgress(0);
                            progresscount=0;

                            Glide.with(getContext()).load(storyModel.getImages().get(progressbarIndex)) .into(imageview);                        }
                        else if(event.getX()>=upperlimit )
                        {       if( progressbarIndex<progresscontainer.getChildCount()-1 )
                            {
                                //next image
                                progresscount++;
                                ((ProgressBar) progresscontainer.getChildAt(progressbarIndex)).setProgress(100);
                                progressbarIndex++;
                                progresscount++;
                                progresscount=0;

                                Glide.with(getContext()).load(storyModel.getImages().get(progressbarIndex)) .into(imageview);
                            }
                            else
                                {
                                    reset();

                                    listener.OnStoryEnd(position);
                                }

                        }
                        else {
                            timerON = false;
                            return true;
                        }
                    case MotionEvent.ACTION_UP:
                        timerON=true;
                        return true;
                }
                return false;
            }
        });
    }



    private void setTimer()
    {
        timer=new Timer();
        timerTask=new TimerTask() {
            @Override
            public void run()
            {   if(timerON)
            {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (progresscount == 100)
                        {   progressbarIndex++;
                            if(progressbarIndex<progresscontainer.getChildCount())
                            {
                                progresscount=0;
                                //change image here
                                Glide.with(getContext()).load(storyModel.getImages().get(progressbarIndex)) .into(imageview);

                            }
                            else
                            {            reset();

                                listener.OnStoryEnd(position);
                            }

                        }
                        else
                        {
                            progresscount++;
                            ((ProgressBar) progresscontainer.getChildAt(progressbarIndex)).setProgress(progresscount);
                        }
                    }
                });

            }}
        };
        timer.scheduleAtFixedRate(timerTask,0,50);
    }

    private void init(View view)
    {
        imageview=view.findViewById(R.id.imageview);
        progresscontainer= view.findViewById(R.id.progress_container);
        defaultprgressbar= ((ProgressBar)progresscontainer.getChildAt(0));
        username=view.findViewById(R.id.username);
        profile_image=view.findViewById(R.id.profile_image);

    }

    private void setProgressbar()
    {
        if(storyModel.getImages()!=null)
        {
            for(int i=1;i<storyModel.getImages().size();i++)
            {
                ProgressBar progressBar = new ProgressBar(getContext(),null,android.R.attr.progressBarStyleHorizontal);
                progressBar.setLayoutParams(defaultprgressbar.getLayoutParams());
                progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.story_progress));
                progresscontainer.addView(progressBar);
            }
        }
    }
}
