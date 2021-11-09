package com.example.mkaktusow.Controller;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.mkaktusow.View.FragKInformacje;
import com.example.mkaktusow.View.FragKMapa;
import com.example.mkaktusow.View.FragKNotatkitekstowe;
import com.example.mkaktusow.View.FragKZdj;

public class FragmentAdapter extends FragmentStateAdapter {

    public FragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch(position){
            case 1:
                return new FragKMapa();
            case 2:
                return new FragKZdj();
            case 3:
                return new FragKNotatkitekstowe();
        }
        return new FragKInformacje();
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
