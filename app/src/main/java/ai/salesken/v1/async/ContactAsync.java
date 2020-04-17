package ai.salesken.v1.async;

import android.content.Context;
import android.os.AsyncTask;

import ai.salesken.v1.activity.ContactActivity;
import ai.salesken.v1.activity.SaleskenActivity;
import ai.salesken.v1.utils.ContactUtil;

public class ContactAsync extends AsyncTask<String, Void, String> {
    private Context context;
    private static final String TAG = "ContactAsync";

    public ContactAsync(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {

    }
    @Override
    protected String doInBackground(String... strings) {
        return new ContactUtil().fetchContacts(context);
    }
    @Override
    protected void onPostExecute(String result) {

    }
}
