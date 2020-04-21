package ai.salesken.v1.async;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import ai.salesken.v1.activity.ContactActivity;
import ai.salesken.v1.activity.SaleskenActivity;
import ai.salesken.v1.constant.SaleskenSharedPrefKey;
import ai.salesken.v1.pojo.ContactPojo;
import ai.salesken.v1.utils.AppDatabase;
import ai.salesken.v1.utils.ContactUtil;

public class FetchContactAsync  extends AsyncTask<String, Void, List<ContactPojo>> {
    private Context context;
    private AppDatabase db;
    private static final String TAG = "FetchContactAsync";

    public FetchContactAsync(Context context, AppDatabase db) {
        this.context = context;
        this.db = db;
    }

    @Override
    protected void onPreExecute() {
        ((ContactActivity) context).showProgressBar();

    }
    @Override
    protected List<ContactPojo> doInBackground(String... strings) {
        return db.contactDao().getAll();
    }
    @Override
    protected void onPostExecute(List<ContactPojo> result) {
        ((ContactActivity) context).contactPojos=result;
        ((ContactActivity) context).showContacts();
        ((ContactActivity) context).hideProgressBar();

    }
}
