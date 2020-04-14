package ai.salesken.v1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ai.salesken.v1.R;
import ai.salesken.v1.viewholder.CompletedViewHolder;
import ai.salesken.v1.viewholder.ContactViewHolder;
import ai.salesken.v1.viewholder.UpcomingViewHolder;

public class CompletedAdapter extends  RecyclerView.Adapter<CompletedViewHolder> {
    private Context context;

    @NonNull
    @Override
    public CompletedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.completed_item, parent, false);

        return new CompletedViewHolder(context,itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CompletedViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }
}
