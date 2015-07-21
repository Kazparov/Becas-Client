package com.example.che.cliente.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.che.cliente.fragments.BecaFragment;
import com.example.che.cliente.fragments.EntidadFragment;
import com.example.che.cliente.fragments.PaisFragment;

/**
 * Created by Che on 04/06/2015.
 */
public class TabsAdapter extends FragmentPagerAdapter{
    private Context mContext;
    private String[] titles = {"ALFABETICO","PAISES","OFERENTES"};
    //private String[] titles = {"ALFABETICA","PAISES"};

    public TabsAdapter(FragmentManager fm, Context c) {
        super(fm);

        mContext = c;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment frag = null;

        if (position == 0) {
            frag = new BecaFragment();
        }else if(position == 1){
            frag = new PaisFragment();
        }else{
            frag = new EntidadFragment();
        }

        Bundle b = new Bundle();
        b.putInt("position", position);

        frag.setArguments(b);

        return frag;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
