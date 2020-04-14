package ai.salesken.v1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ai.salesken.v1.R;
import ai.salesken.v1.viewholder.RecentViewHolder;
import ai.salesken.v1.viewholder.UpcomingViewHolder;

public class UpcomingAdapter extends  RecyclerView.Adapter<UpcomingViewHolder> {
    private Context context;

    @NonNull
    @Override
    public UpcomingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.upcoming_item, parent, false);

        return new UpcomingViewHolder(context,itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UpcomingViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }
}
