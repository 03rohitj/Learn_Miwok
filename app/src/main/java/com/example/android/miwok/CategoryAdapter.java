package com.example.android.miwok;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


public class CategoryAdapter extends FragmentPagerAdapter {

    String titles[]={"Numbers","Colors","Family","Phrases"};
    Context context;
    public CategoryAdapter(FragmentManager fm,Context context)
    {
        super(fm);
        this.context=context;
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0)        return new NumbersFragment();
        else if(position == 1)   return new ColorsFragment();
        else if(position == 2)   return new FamilyFragment();
        else                     return new PhrasesFragment();
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return context.getString(R.string.category_numbers);
        } else if (position == 1) {
            return context.getString(R.string.category_family);
        } else if (position == 2) {
            return context.getString(R.string.category_colors);
        } else {
            return context.getString(R.string.category_phrases);
        }
    }

}
