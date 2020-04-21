package ai.salesken.v1.viewholder;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
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
    TextView title;
    @BindView(R.id.scpname)
    TextView scpname;
    @BindView(R.id.companyalphabet)
    Button companyAlphabet;
    Drawable drawable;

    public ContactViewHolder(Context context,@NonNull View itemView) {
        super(itemView);
        this.context = context;
        ButterKnife.bind(this,itemView);
    }
    public void render(final ContactPojo contactPojo, final int position, String query) {
        String name = contactPojo.getName();



        companyAlphabet.setText(name.charAt(0) + "");
        if(name.startsWith("#")){
            name=  name.replaceFirst("#","");
        }
        title.setText(name);
        scpname.setText(contactPojo.getMobile());
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

        if (query != null && query.length() > 0) {
            searchInWords(query, title.getText().toString(), context, title);
            searchInWords(query, scpname.getText().toString(), context, scpname);

        }
    }
    public void searchInWords(String search, String words, Context context, TextView textView) {
        Spannable wordtoSpan = new SpannableString(words);

        int index = words.toLowerCase().indexOf(search);
        if (index >= 0) {
            wordtoSpan.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.theme_color)), index, index + search.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.setText(wordtoSpan);
        } else {
            textView.setText(words);
        }
    }
    }
