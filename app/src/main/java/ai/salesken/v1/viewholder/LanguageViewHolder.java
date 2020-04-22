package ai.salesken.v1.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ai.salesken.v1.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class LanguageViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.lang_text)
    TextView lang;
    public LanguageViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

    }

    public void bind(String language){
        lang.setText(language);
    }
}
