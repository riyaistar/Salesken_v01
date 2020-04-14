package ai.salesken.v1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SectionIndexer;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ai.salesken.v1.R;
import ai.salesken.v1.pojo.ContactPojo;
import ai.salesken.v1.viewholder.ContactViewHolder;

public class ContactAdapter extends RecyclerView.Adapter<ContactViewHolder> implements SectionIndexer {
    private List<ContactPojo> contactPojos;
    private ArrayList<Integer> mSectionPositions;
    private Context context;

    public ContactAdapter(Context context, List<ContactPojo> contactPojos) {
        this.context = context;
        this.contactPojos = contactPojos;
    }

    @Override
    public Object[] getSections() {
        List<String> sections = new ArrayList<>(26);
        mSectionPositions = new ArrayList<>(26);

        for (int i = 0, size = contactPojos.size(); i < size; i++) {
            try {  String section = String.valueOf(contactPojos.get(i).getName().charAt(0)).toUpperCase();
                if (!sections.contains(section)) {
                    sections.add(section);
                    mSectionPositions.add(i);
                }
            }catch (Exception e){

            }
        }

        return sections.toArray(new String[0]);
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        return mSectionPositions.get(sectionIndex);
    }

    @Override
    public int getSectionForPosition(int position) {
        return 0;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_item, parent, false);

        return new ContactViewHolder(context,itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder contactViewHolder, int position) {
        contactViewHolder.render(contactPojos.get(position),position);

    }

    @Override
    public int getItemCount() {
        return contactPojos.size();
    }

    public void updateList(List<ContactPojo> list){
        contactPojos = list;
        notifyDataSetChanged();
    }
}
