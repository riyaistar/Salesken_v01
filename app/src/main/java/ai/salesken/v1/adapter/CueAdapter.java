package ai.salesken.v1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ai.salesken.v1.R;
import ai.salesken.v1.viewholder.ContactViewHolder;
import ai.salesken.v1.viewholder.CueViewHolder;

public class CueAdapter extends RecyclerView.Adapter<CueViewHolder> {
    private List<String> cues;
    private Context context;

    public CueAdapter(Context context,List<String> cues) {
        this.cues = cues;
        this.context = context;
    }

    @NonNull
    @Override
    public CueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cue_item, parent, false);

        return new CueViewHolder(context,itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CueViewHolder holder, int position) {
        holder.setData(cues.get(position));
    }

    @Override
    public int getItemCount() {
        return cues.size();
    }
}
