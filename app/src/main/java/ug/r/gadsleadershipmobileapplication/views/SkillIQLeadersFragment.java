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

import org.json.JSONException;

import java.util.ArrayList;

import ug.r.gadsleadershipmobileapplication.R;
import ug.r.gadsleadershipmobileapplication.databinding.FragmentIqLeadersBinding;
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

        this.adapter = new InformationActivity.ItemAdapter(items, R.drawable.top_iq);
        this.binding.recyclerView.setLayoutManager(new LinearLayoutManager(this.requireContext()));
        this.binding.recyclerView.setAdapter(this.adapter);

        this.binding.swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                get_data();
            }
        });

        this.get_data();
        return binding.getRoot();
    }

    private void get_data() {
        this.binding.swipeLayout.setRefreshing(true);
        new RetrofitClient(
                RetrofitClient.getClient("https://gadsapi.herokuapp.com").create(RetroInterface.class).iq_leaders(),
                null, null,
                new RetrofitClient.Response() {
                    @Override
                    public void response(String response, Integer code) throws JSONException {
                        binding.swipeLayout.setRefreshing(false);

                        Log.e("Response", response.toString());
                    }
                }
        );
    }
}