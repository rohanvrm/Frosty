package com.example.frosty.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.frosty.Adapters.FeedsAdapter;
import com.example.frosty.Adapters.StoriesAdapter;
import com.example.frosty.Models.PostModel;
import com.example.frosty.Models.StoryModel;
import com.example.frosty.R;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters

    private RecyclerView feeds,stories;

    private StoriesAdapter storiesAdapter;

    private FeedsAdapter feedsAdapter;

    public static List<StoryModel> list;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        init(view);

        LinearLayoutManager storiesLayoutManager=new LinearLayoutManager( getContext());
        storiesLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        stories.setLayoutManager(storiesLayoutManager);

        LinearLayoutManager feedsLayoutManager=new LinearLayoutManager( getContext());
        storiesLayoutManager.setOrientation(RecyclerView.VERTICAL);
        feeds.setLayoutManager(feedsLayoutManager);

        List<String> images= new ArrayList<>();
        images.add("https://static.toiimg.com/thumb/72975551.cms?width=680&height=512&imgsize=881753");
        images.add("data:image/jpeg;base64,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");
        images.add("https://images.unsplash.com/photo-1494548162494-384bba4ab999?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&w=1000&q=80");
        images.add("data:image/jpeg;base64,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");
        images.add("https://cdn.spacetelescope.org/archives/images/wallpaper2/heic2007a.jpg");


        list=new ArrayList<>();
        list.add(new StoryModel(images,"Rohan Verma"));
        list.add(new StoryModel(images,"John Doe"));
        list.add(new StoryModel(images,"John Doe"));
        list.add(new StoryModel(images,"John Doe"));
        list.add(new StoryModel(images,"John Doe"));


        storiesAdapter= new StoriesAdapter(list);
        stories.setAdapter(storiesAdapter);

        List<PostModel> feedslist=new ArrayList<>();
        feedslist.add(new PostModel("","","","","","","",""));
        feedslist.add(new PostModel("","","","","","","",""));
        feedslist.add(new PostModel("","","","","","","",""));
        feedslist.add(new PostModel("","","","","","","",""));
        feedslist.add(new PostModel("","","","","","","",""));
        feedslist.add(new PostModel("","","","","","","",""));
        feedslist.add(new PostModel("","","","","","","",""));
        feedslist.add(new PostModel("","","","","","","",""));
        feedslist.add(new PostModel("","","","","","","",""));
        feedslist.add(new PostModel("","","","","","","",""));

        feedsAdapter=new FeedsAdapter(feedslist);
        feeds.setAdapter(feedsAdapter);



    }
    private void init(View view)
    {
        stories=view.findViewById(R.id.stories);
        feeds=view.findViewById(R.id.feeds);
    }

}
