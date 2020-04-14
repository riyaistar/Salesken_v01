package ai.salesken.v1.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ai.salesken.v1.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CompletedViewHolder extends RecyclerView.ViewHolder {
    private Context context;

    @BindView(R.id.company_name)
    TextView company_name;

    @BindView(R.id.duration)
    TextView duration;
    @BindView(R.id.time)
    TextView time;
    public CompletedViewHolder(Context context,@NonNull View itemView) {
        super(itemView);
        this.context = context;
        ButterKnife.bind(this,itemView);
    }
}
