package ai.salesken.v1.adapter;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import ai.salesken.v1.fragment.IntroductionOne;
import ai.salesken.v1.fragment.IntroductionThree;
import ai.salesken.v1.fragment.IntroductonTwo;

public class IntroductionAdapter extends FragmentStatePagerAdapter{

    public IntroductionAdapter(Context context, @NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        Bundle bundle = new Bundle();
        switch (position) {
            case 0:
                fragment = new IntroductionOne();
                break;
            case 1:
                fragment = new IntroductonTwo();
                break;
            default:
                fragment = new IntroductionThree();

        }
        fragment.setArguments(bundle);
        return fragment;    }

    @Override
    public int getCount() {
        return 3;
    }
}
