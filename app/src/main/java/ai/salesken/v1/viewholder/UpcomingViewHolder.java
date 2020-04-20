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

public class UpcomingViewHolder extends RecyclerView.ViewHolder {
    private Context context;
    @BindView(R.id.company_name)
     TextView company_name;
    @BindView(R.id.time)
     TextView time;
    @BindView(R.id.type)
     TextView type;

    public UpcomingViewHolder(Context context,@NonNull View itemView) {
        super(itemView);
        this.context = context;
        ButterKnife.bind(this,itemView);
    }

    public void bind(Task task){

        try {
            time.setText(((SaleskenActivity)context).ampmformat.format(((SaleskenActivity)context).dateFormat.parse(task.getUpdatedAt())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        company_name.setText(task.getCompanyName());
        if(task.getDisposition() != null){
            type.setText(task.getDisposition());
        }

    }
}
