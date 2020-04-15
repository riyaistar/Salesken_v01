package ai.salesken.v1.viewholder;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import ai.salesken.v1.R;
import ai.salesken.v1.pojo.ContactPojo;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ContactViewHolder  extends RecyclerView.ViewHolder  {
    private static final String POSITION = "position";
    private Context context;
    @BindView(R.id.title)
    TextView scpname;
    @BindView(R.id.scpname)
    TextView title;
    @BindView(R.id.companyalphabet)
    Button companyAlphabet;
    Drawable drawable;

    public ContactViewHolder(Context context,@NonNull View itemView) {
        super(itemView);
        this.context = context;
        ButterKnife.bind(this,itemView);
    }
    public void render(final ContactPojo contactPojo, final int position) {
        String name = contactPojo.getName();



        companyAlphabet.setText(name.charAt(0) + "");
        if(name.startsWith("#")){
            name=  name.replaceFirst("#","");
        }
        scpname.setText(name);
        drawable = ContextCompat.getDrawable(context, R.drawable.circle_button_solid).mutate();
        if(position%4 ==0){
            drawable.setColorFilter(context.getResources().getColor(R.color.bg_purple), PorterDuff.Mode.SRC_ATOP);
        }else if(position %3 ==0){
            drawable.setColorFilter(context.getResources().getColor(R.color.bg_darkgreen), PorterDuff.Mode.SRC_ATOP);
        }else if(position %2==0){
            drawable.setColorFilter(context.getResources().getColor(R.color.bg_orange), PorterDuff.Mode.SRC_ATOP);
        }else{
            drawable.setColorFilter(context.getResources().getColor(R.color.bg_green), PorterDuff.Mode.SRC_ATOP);

        }
        companyAlphabet.setBackgroundDrawable(drawable);
    }
    }
