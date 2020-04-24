package ai.salesken.v1.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;

import ai.salesken.v1.R;
import ai.salesken.v1.activity.SaleskenActivity;
import ai.salesken.v1.pojo.Task;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RecentViewHolder extends RecyclerView.ViewHolder {
    private Context context;
    @BindView(R.id.company_name)
     TextView company_name;
    @BindView(R.id.type)
     TextView type;
    @BindView(R.id.time)
     TextView time;


    public RecentViewHolder(Context context, @NonNull View itemView) {
        super(itemView);
        this.context = context;
        ButterKnife.bind(this,itemView);
    }

    public void bind(Task task){
        try {
            time.setText(task.getUpdatedAtTime());
            company_name.setText(task.getContactName());
            type.setText(task.getCallDuration());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
