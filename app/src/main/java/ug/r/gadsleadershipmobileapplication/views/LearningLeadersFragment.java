package ug.r.gadsleadershipmobileapplication.views;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ug.r.gadsleadershipmobileapplication.R;
import ug.r.gadsleadershipmobileapplication.databinding.FragmentLearningLeadersBinding;
import ug.r.gadsleadershipmobileapplication.utils.RetroInterface;
import ug.r.gadsleadershipmobileapplication.utils.RetrofitClient;

public class LearningLeadersFragment extends Fragment {

    private FragmentLearningLeadersBinding binding;
    private InformationActivity.ItemAdapter adapter;
    private ArrayList<InformationActivity.Item> items = new ArrayList<>();

    public LearningLeadersFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.binding = DataBindingUtil.inflate(inflater, R.layout.fragment_learning_leaders, container, false);


        this.adapter = new InformationActivity.ItemAdapter(items, R.drawable.top_leaners, this.requireActivity());
        this.binding.recyclerView.setLayoutManager(new LinearLayoutManager(this.requireContext()));
        this.binding.recyclerView.setAdapter(this.adapter);

        this.binding.swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                get_data();
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.get_data();
    }

    private void get_data() {
        this.binding.swipeLayout.setRefreshing(true);

        RetrofitClient.getClient("https://gadsapi.herokuapp.com").create(RetroInterface.class).learning()
                .enqueue(new Callback<ArrayList<Leader>>() {
                    @Override
                    public void onResponse(@NonNull Call<ArrayList<Leader>> call, @NonNull Response<ArrayList<Leader>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            ArrayList<Leader> leaders = response.body();
                            Collections.sort(leaders, new Comparator<Leader>() {
                                @Override
                                public int compare(Leader first, Leader second) {
                                    return second.hours - first.hours;
                                }
                            });

                            items.clear();
                            for (Leader leader : leaders) {
                                items.add(new InformationActivity.Item(
                                        leader.hours + " learning hours, " + leader.country, leader.name, leader.badgeUrl));
                            }
                            adapter.notifyDataSetChanged();

                        } else {

                        }
                        binding.swipeLayout.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(@NonNull Call<ArrayList<Leader>> call, @NonNull Throwable t) {
                        binding.swipeLayout.setRefreshing(false);
                        Log.e("Response", t.getLocalizedMessage(), t);
                    }
                });
    }

    public static class Leader {
        String name;
        int hours;
        String country;
        String badgeUrl;
    }
}