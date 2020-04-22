package ai.salesken.v1.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;

import ai.salesken.v1.R;
import ai.salesken.v1.viewholder.LanguageViewHolder;
import ai.salesken.v1.viewholder.RecentViewHolder;

public class LanguageAdapter extends  RecyclerView.Adapter<LanguageViewHolder> {
    private List<String> languages;
    String language;
    public LanguageAdapter(List<String> languages, String language) {
        this.languages = languages;
        this.language=language;
    }

    @NonNull
    @Override
    public LanguageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.language_item, parent, false);

        return new LanguageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LanguageViewHolder holder, int position) {
        holder.bind(languages.get(position),language);
    }

    @Override
    public int getItemCount() {
        return languages.size();
    }
}
