package ai.salesken.v1.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ai.salesken.v1.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CueViewHolder  extends RecyclerView.ViewHolder  {
    @BindView(R.id.cuetext)
    TextView cuetext;
    private Context context;

    public CueViewHolder(Context context, @NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.context = context;
    }

    public void setData(final String cue) {
        cuetext.setText(cue);
    }
}