package com.example.followup.ui.home;

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

import com.example.followup.NewsAdapter;
import com.example.followup.R;
import com.example.followup.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment implements MenuProvider, LifecycleOwner {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //add menu
        MenuHost menuHost = requireActivity();
        LifecycleOwner owner = getViewLifecycleOwner();
        menuHost.addMenuProvider(this, owner, Lifecycle.State.RESUMED);

        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //add list
        ListView mListView = getView().findViewById(R.id.news_list);
        mListView.setAdapter(new NewsAdapter(this.getContext()));

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView arg0, View arg1, int
                    position,long arg3) {
                Log.i("click", String.valueOf(position));
//                Intent i = new Intent(getActivity().getApplicationContext(), NewsDetail.class);
//                i.putExtra("position", position);
//                startActivity(i);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.main_menu, menu);
    }

    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_all:
//                addSomething();
                return true;
            case R.id.action_world:
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
            default:
                return false;
        }
    }
}