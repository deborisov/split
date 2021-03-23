package com.abacaba.ha.rv;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.abacaba.ha.activities.MainActivity;
import com.abacaba.ha.activities.MyFragment;
import com.abacaba.ha.actors.Company;

import java.util.ArrayList;

public class MyPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<Company> companies;
    private ArrayList<MyFragment> fragments;

    public MyPagerAdapter(FragmentManager fragment, ArrayList<Company> companies, MainActivity activity){
        super(fragment);
        fragments = new ArrayList<>();
        for (int i = 0; i < companies.size() + 1; ++i){
            fragments.add((new MyFragment()).newInstance(i, companies, activity));
        }
        this.companies = companies;
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return companies.size() + 1;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == companies.size()){
            return "+";
        }
        else{
            return companies.get(position).getName();
        }
    }

    public void setCompanies(ArrayList<Company> companies, MainActivity activity) {
        fragments.clear();
        this.companies = companies;
        for (int i = 0; i < companies.size() + 1; ++i){
            fragments.add((new MyFragment()).newInstance(i, companies, activity));
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}