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
import com.example.followup.EventItem;
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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements MenuProvider, LifecycleOwner {

    private FragmentHomeBinding binding;
    private FirebaseFirestore db;
    private String querySource = "";
    private String queryType = "";

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
//        addEvent();
//        addNews();
        getNewsList();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void addEvent() {
        //get timestamp from LocalDateTime
        LocalDateTime ldt = LocalDateTime.of(2022, 11, 30, 15, 55, 0);
        ZonedDateTime zdt = ldt.atZone(ZoneId.of("America/Chicago"));
        long millis = zdt.toInstant().toEpochMilli();

        EventItem eventItem = new EventItem(
                "Test event timeline title 3",
                millis,
                "https://media-cldnry.s-nbcnews.com/image/upload/t_focal-200x100,f_auto,q_auto:best/rockcms/2022-09/220915-mar-a-lago-TrumpFBI-ac-723p-6e4e5f.jpg"
                );

        db.collection("eventlist").add(eventItem).
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

    public void addNews() {
        //get timestamp from LocalDateTime
        LocalDateTime ldt = LocalDateTime.of(2022, 12, 1, 20, 0, 0);
        ZonedDateTime zdt = ldt.atZone(ZoneId.of("America/Chicago"));
        long millis = zdt.toInstant().toEpochMilli();

        NewsItem newsItem = new NewsItem(
                "US appeals court denies Trump 'special master' request in documents case",
                "politics",
                "BBC",
                "https://www.bbc.com/news/world-us-canada-63829125",
                "https://ichef.bbci.co.uk/news/976/cpsprodpb/78C4/production/_127861903_gettyimages-1439879814.jpg",
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
        Query query = null;
        if (querySource.equals("") && queryType.equals("")) {
            query = db.collection("newslist");
        } else if (!querySource.equals("") && !queryType.equals("")) {
            query = db.collection("newslist")
                    .whereEqualTo("source", querySource)
                    .whereEqualTo("type", queryType);
        } else if (!querySource.equals("")) {
            query = db.collection("newslist")
                    .whereEqualTo("source", querySource);
        } else {
            query = db.collection("newslist")
                    .whereEqualTo("type", queryType);
        }
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<NewsItem> list = new ArrayList<>();
                            List<String> ids = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ids.add(document.getId());
                                list.add(document.toObject(NewsItem.class));
                            }
                            ListView mListView = getView().findViewById(R.id.news_list);
                            mListView.setAdapter(new NewsAdapter(getActivity().getApplicationContext(), list));
                            //click to go to article
                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView arg0, View arg1, int
                                        position,long arg3) {
                                    Intent i = new Intent(getActivity().getApplicationContext(), ArticleActivity.class);
                                    i.putExtra("id", ids.get(position));
                                    i.putExtra("link", list.get(position).getLink());
                                    i.putExtra("eventId", list.get(position).getEventId());
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
                queryType = "";
                break;
            case R.id.action_world:
                queryType = "world";
                break;
            case R.id.action_politics:
                queryType = "politics";
                break;
            case R.id.action_business:
                queryType = "business";
                break;
            case R.id.action_technology:
                queryType = "technology";
                break;
            case R.id.action_science:
                queryType = "science";
                break;
            case R.id.action_health:
                queryType = "health";
                break;
            case R.id.action_entertainment:
                queryType = "entertainment";
                break;
            case R.id.action_sport:
                queryType = "sport";
                break;
            //source
            case R.id.action_all_source:
                querySource= "";
                break;
            case R.id.action_cbs:
                querySource= "CBS";
                break;
            case R.id.action_nbc:
                querySource= "NBC";
                break;
            case R.id.action_fox:
                querySource= "FOX";
                break;
            case R.id.action_cnn:
                querySource= "CNN";
                break;
            case R.id.action_bbc:
                querySource= "BBC";
                break;
            default:
                return false;
        }
        getNewsList();
        return true;
    }
}