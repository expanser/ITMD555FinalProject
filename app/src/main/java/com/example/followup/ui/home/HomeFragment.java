package com.example.followup.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import com.example.followup.ArticleActivity;
import com.example.followup.NewsAdapter;
import com.example.followup.NewsItem;
import com.example.followup.R;
import com.example.followup.SearchActivity;
import com.example.followup.databinding.FragmentHomeBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements MenuProvider, LifecycleOwner {

    private FragmentHomeBinding binding;
    private FirebaseFirestore db;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        db = FirebaseFirestore.getInstance();
        //add menu
        MenuHost menuHost = requireActivity();
        LifecycleOwner owner = getViewLifecycleOwner();
        menuHost.addMenuProvider(this, owner, Lifecycle.State.RESUMED);

        //add search button listener
        FloatingActionButton searchButton = binding.searchButton;

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity().getApplicationContext(), SearchActivity.class));
            }
        });

        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        addNews();
        getNewsList();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void addNews() {
        //get timestamp from LocalDateTime
        LocalDateTime ldt = LocalDateTime.of(2022, 12, 1, 14, 20, 0);
        ZonedDateTime zdt = ldt.atZone(ZoneId.of("America/Chicago"));
        long millis = zdt.toInstant().toEpochMilli();

        NewsItem newsItem = new NewsItem(
                "Biden and Macron present united front against Russia in first state visit",
                "politics",
                "CBS",
                "https://www.cbsnews.com/live-updates/biden-macron-russia-ukraine-state-visit-white-house/",
                "https://assets2.cbsnewsstatic.com/hub/i/r/2022/12/01/7dfa66ca-ba40-4e77-9d57-0528b8b1854c/thumbnail/620x413/de1ccc615e22713813bf731a1c8b6baf/gettyimages-1245278397.jpg",
                null,
                millis
        );

        db.collection("newslist").add(newsItem).
                addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("onSuccess", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("onFailure", "Error adding document", e);
                    }
                });
    }

    public void getNewsList() {
        db.collection("newslist")
//                .whereEqualTo("source", "")
//                .whereEqualTo("type", "")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<NewsItem> list = new ArrayList<>(task.getResult().toObjects(NewsItem.class));
                            ListView mListView = getView().findViewById(R.id.news_list);
                            mListView.setAdapter(new NewsAdapter(getActivity().getApplicationContext(), list));
                            //click to go to article
                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView arg0, View arg1, int
                                        position,long arg3) {
                                    Intent i = new Intent(getActivity().getApplicationContext(), ArticleActivity.class);
                                    i.putExtra("position", position);
                                    startActivity(i);
                                }
                            });
                        } else {
                            Log.w("onFailure", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.main_menu, menu);
    }

    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            //type
            case R.id.action_all_type:
//                addSomething();
                return true;
            case R.id.action_world:
                return true;
            case R.id.action_politics:
                return true;
            case R.id.action_business:
                return true;
            case R.id.action_tech:
                return true;
            case R.id.action_science:
                return true;
            case R.id.action_health:
                return true;
            case R.id.action_entertainment:
                return true;
            case R.id.action_sport:
                return true;
            //source
            case R.id.action_all_source:
                return true;
            case R.id.action_cbs:
                return true;
            case R.id.action_nbc:
                return true;
            case R.id.action_fox:
                return true;
            case R.id.action_cnn:
                return true;
            case R.id.action_bbc:
                return true;
            default:
                return false;
        }
    }
}