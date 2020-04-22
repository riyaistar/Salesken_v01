package ai.salesken.v1.viewholder;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;

import ai.salesken.v1.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class LanguageViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.lang_text)
    TextView lang;
    @BindView(R.id.checkBox)
    CheckBox checkBox;
    public LanguageViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

    }

    public void bind(String language, String selected_language, HashMap<String, String> language_map){
        lang.setText(language);

        for(String lang:selected_language.split(",")){
            if(language_map.get(lang)!=null){
               if( language_map.get(lang).equalsIgnoreCase(language)){
                   checkBox.setChecked(true);
               }
            }

        }
    }
}
