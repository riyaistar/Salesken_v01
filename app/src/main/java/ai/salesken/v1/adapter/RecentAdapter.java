package ai.salesken.v1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ai.salesken.v1.R;
import ai.salesken.v1.pojo.Task;
import ai.salesken.v1.viewholder.CompletedViewHolder;
import ai.salesken.v1.viewholder.RecentViewHolder;

public class RecentAdapter extends  RecyclerView.Adapter<RecentViewHolder> {
    private Context context;
    private List<Task> tasks;

    public RecentAdapter(Context context, List<Task> tasks) {
        this.context = context;
        this.tasks = tasks;
    }

    @NonNull
    @Override
    public RecentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recent_item, parent, false);

        return new RecentViewHolder(context,itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentViewHolder holder, int position) {
        holder.bind(tasks.get(position));
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }
}
