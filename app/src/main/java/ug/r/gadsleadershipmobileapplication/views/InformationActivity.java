package ug.r.gadsleadershipmobileapplication.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import ug.r.gadsleadershipmobileapplication.R;
import ug.r.gadsleadershipmobileapplication.databinding.ListItemsBinding;

public class InformationActivity extends AppCompatActivity {
    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_learners, R.string.tab_iq_learners};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new LearningLeadersFragment();
            } else {
                return new SkillIQLeadersFragment();
            }
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return getResources().getString(TAB_TITLES[position]);
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    class Item {
        public String itemDesc;
        public String itemName;

        public Item(String itemDesc, String itemName) {
            this.itemDesc = itemDesc;
            this.itemName = itemName;
        }
    }

    static class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {
        private ArrayList<Item> items;
        private int drawable;

        public ItemAdapter(ArrayList<Item> items, int drawable) {
            this.drawable = drawable;
            this.items = items;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ListItemsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.list_items, parent, false);
            return new MyViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            Item item = this.items.get(position);
            holder.binding.itemName.setText(item.itemName);
            holder.binding.itemDescription.setText(item.itemDesc);
            holder.binding.itemImage.setBackgroundResource(this.drawable);
        }

        @Override
        public int getItemCount() {
            return this.items.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            ListItemsBinding binding;

            public MyViewHolder(@NonNull ListItemsBinding itemView) {
                super(itemView.getRoot());
                this.binding = itemView;
            }
        }
    }
}