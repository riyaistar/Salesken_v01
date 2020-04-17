package ai.salesken.v1.async;

import android.content.Context;
import android.os.AsyncTask;

import ai.salesken.v1.activity.ContactActivity;
import ai.salesken.v1.activity.SaleskenActivity;
import ai.salesken.v1.utils.ContactUtil;

public class FetchContactAsync  extends AsyncTask<String, Void, String> {
    private Context context;
    private static final String TAG = "FetchContactAsync";

    public FetchContactAsync(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        ((ContactActivity) context).showProgressBar();

    }
    @Override
    protected String doInBackground(String... strings) {
        return new ContactUtil().fetchContacts(context);
    }
    @Override
    protected void onPostExecute(String result) {
        if(result.equalsIgnoreCase("success")){
            ((ContactActivity) context).showContacts();
        }else{
            ((SaleskenActivity) context).showToast("Error While fetching contact");

        }
        ((ContactActivity) context).hideProgressBar();

    }
}
