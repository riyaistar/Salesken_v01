package ai.salesken.v1.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import ai.salesken.v1.fragment.CompletedTask;
import ai.salesken.v1.fragment.RecentTask;
import ai.salesken.v1.fragment.UpcomingTask;

public class TaskAdapter extends FragmentStatePagerAdapter {
    private String[] tabTitles;

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
                return upcomingTask;
            case 1:
                RecentTask recentTask = new RecentTask();
                return  recentTask;
            case 2:
                CompletedTask completedTask = new CompletedTask();
                return completedTask;

        }
        return null;
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }
}
