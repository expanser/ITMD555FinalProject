package com.example.followup.ui.dashboard;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.followup.CollectionActivity;
import com.example.followup.EventAdapter;
import com.example.followup.EventBookmark;
import com.example.followup.EventItem;
import com.example.followup.HomeActivity;
import com.example.followup.R;
import com.example.followup.TimelineActivity;
import com.example.followup.databinding.FragmentDashboardBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private FirebaseFirestore db;
    SharedPreferences sharedPreferences;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        db = FirebaseFirestore.getInstance();

        //add goto collection list button listener
        FloatingActionButton CollectionButton = binding.CollectionButton;

        CollectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity().getApplicationContext(), CollectionActivity.class));
            }
        });
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getMyEventCollections();
        //add list
        ListView mListView = getView().findViewById(R.id.event_list);
        mListView.setAdapter(new EventAdapter(this.getContext(), new ArrayList<>()));
        //click to go to timeline
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView arg0, View arg1, int
                    position,long arg3) {
                Intent i = new Intent(getActivity().getApplicationContext(), TimelineActivity.class);
                i.putExtra("position", position);
                startActivity(i);
            }
        });
        //long click to delete
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                new AlertDialog.Builder(getActivity()).setTitle("Confirm Delete")
                        .setMessage("Are you sure to delete this event?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.i("YES" , String.valueOf(position));
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.i("NO" , String.valueOf(position));
                            }
                        }).show();
                return true;
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void getMyEventCollections() {
        sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("id","");

        db.collection("eventmarklist")
                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<EventBookmark> list = new ArrayList<>();
                            List<String> ids = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ids.add(document.getId());
                                list.add(document.toObject(EventBookmark.class));
                            }
                            getEventList(list, ids);
                        } else {
                            Log.w("onFailure", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    public void getEventList (List<EventBookmark> bookmarks, List<String> bookmarkIds) {
        //due to the restriction of google firebase, whereIn clause can only return 10 records most.
        List<String> eventIds = new ArrayList<>();
        for (EventBookmark item : bookmarks) {
            eventIds.add(item.getEventId());
        }
        if (eventIds.size() == 0) {
            Toast.makeText(getActivity().getApplicationContext(), "No result", Toast.LENGTH_LONG).show();
            return;
        }
        db.collection("eventlist")
                .whereIn("id", eventIds)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<EventItem> eventList = new ArrayList<>();
                            List<String> eventIds = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                eventIds.add(document.getId());
                                eventList.add(document.toObject(EventItem.class));
                            }
                            ListView mListView = getActivity().findViewById(R.id.event_list);
                            EventAdapter eventAdapter = new EventAdapter(getActivity().getApplicationContext(), eventList);
                            mListView.setAdapter(eventAdapter);
                            //click to go to article
                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView arg0, View arg1, int
                                        position,long arg3) {
                                    Intent i = new Intent(getActivity().getApplicationContext(), TimelineActivity.class);
                                    i.putExtra("id", eventIds.get(position));
                                    startActivity(i);
                                }
                            });
                            //long click to delete
                            mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                @Override
                                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                                    new AlertDialog.Builder(getActivity()).setTitle("Confirm Delete")
                                            .setMessage("Are you sure to delete this event?")
                                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    //find id position in bookmarkIds
                                                    String deleteId = "";
                                                    for (int i = 0; i < bookmarks.size() ;i++) {
                                                        if (bookmarks.get(i).getEventId().equals(eventList.get(position).getId())) {
                                                            deleteId = bookmarkIds.get(i);
                                                        }
                                                    }
                                                    db.collection("eventmarklist").document(deleteId)
                                                            .delete()
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    Log.d("delete", "DocumentSnapshot successfully deleted!");
                                                                    Toast.makeText(getActivity().getApplicationContext(), "Delete Success!", Toast.LENGTH_LONG).show();
                                                                    eventList.remove(position);
                                                                    eventAdapter.notifyDataSetChanged();
                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Log.w("delete", "Error deleting document", e);
                                                                }
                                                            });
                                                }
                                            })
                                            .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Log.i("NO" , String.valueOf(position));
                                                }
                                            }).show();
                                    return true;
                                }
                            });
                        } else {
                            Log.w("onFailure", "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}