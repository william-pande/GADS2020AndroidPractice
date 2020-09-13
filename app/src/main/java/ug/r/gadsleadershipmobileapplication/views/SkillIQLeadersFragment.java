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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ug.r.gadsleadershipmobileapplication.R;
import ug.r.gadsleadershipmobileapplication.databinding.FragmentIqLeadersBinding;
import ug.r.gadsleadershipmobileapplication.utils.MyDialogFragment;
import ug.r.gadsleadershipmobileapplication.utils.RetroInterface;
import ug.r.gadsleadershipmobileapplication.utils.RetrofitClient;


public class SkillIQLeadersFragment extends Fragment {
    private FragmentIqLeadersBinding binding;
    private InformationActivity.ItemAdapter adapter;
    private ArrayList<InformationActivity.Item> items = new ArrayList<>();

    public SkillIQLeadersFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.binding = DataBindingUtil.inflate(inflater, R.layout.fragment_iq_leaders, container, false);

        this.adapter = new InformationActivity.ItemAdapter(this.items, R.drawable.top_iq, this.requireActivity());
        this.binding.recyclerView.setLayoutManager(new LinearLayoutManager(this.requireContext()));
        this.binding.recyclerView.setAdapter(this.adapter);

        this.binding.swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                SkillIQLeadersFragment.this.get_data();
            }
        });
        return this.binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.get_data();
    }

    private void get_data() {
        this.binding.swipeLayout.setRefreshing(true);

        RetrofitClient.getClient("https://gadsapi.herokuapp.com").create(RetroInterface.class).iq_leaders()
                .enqueue(new Callback<ArrayList<Leader>>() {
                    @Override
                    public void onResponse(@NonNull Call<ArrayList<Leader>> call, @NonNull Response<ArrayList<Leader>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            ArrayList<Leader> leaders = response.body();
                            Collections.sort(leaders, new Comparator<Leader>() {
                                @Override
                                public int compare(Leader first, Leader second) {
                                    return second.score - first.score;
                                }
                            });

                            SkillIQLeadersFragment.this.items.clear();
                            for (Leader leader : leaders) {
                                SkillIQLeadersFragment.this.items.add(new InformationActivity.Item(
                                        leader.score + " Skill IQ Score, " + leader.country, leader.name, leader.badgeUrl));
                            }
                            SkillIQLeadersFragment.this.adapter.notifyDataSetChanged();
                        } else {
                            MyDialogFragment.newInstance(MyDialogFragment.DialogType.ERROR,
                                    "Could not load Skill IQ Leaders, please retry",
                                    null, SkillIQLeadersFragment.this.requireActivity());
                        }
                        SkillIQLeadersFragment.this.binding.swipeLayout.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(@NonNull Call<ArrayList<Leader>> call, @NonNull Throwable t) {
                        SkillIQLeadersFragment.this.binding.swipeLayout.setRefreshing(false);
                        Log.e("Response", t.getLocalizedMessage(), t);

                        MyDialogFragment.newInstance(MyDialogFragment.DialogType.ERROR,
                                (t instanceof IOException) ? "Network error, please check your connection and then retry"
                                        : t.getLocalizedMessage(),
                                null, SkillIQLeadersFragment.this.requireActivity());
                    }
                });
    }

    public static class Leader {
        String name;
        int score;
        String country;
        String badgeUrl;
    }
}