package ai.salesken.v1.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import ai.salesken.v1.fragment.CompletedTask;
import ai.salesken.v1.fragment.RecentTask;
import ai.salesken.v1.fragment.UpcomingTask;

public class TaskAdapter extends FragmentStatePagerAdapter {
    private String[] tabTitles;
    private SparseArray<Fragment> registeredFragments = new SparseArray<>();

    public TaskAdapter(Context context, FragmentManager fm, String[] tabTitles) {
        super(fm);
        this.tabTitles = tabTitles;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                UpcomingTask upcomingTask = new UpcomingTask();
                registeredFragments.put(position, upcomingTask);

                return upcomingTask;
            case 1:
                RecentTask recentTask = new RecentTask();
                registeredFragments.put(position, recentTask);

                return  recentTask;
            case 2:
                CompletedTask completedTask = new CompletedTask();
                registeredFragments.put(position, completedTask);

                return completedTask;

        }
        return null;
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }
}
