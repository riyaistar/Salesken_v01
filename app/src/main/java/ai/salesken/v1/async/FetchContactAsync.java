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
            /*Type type = new TypeToken<List<ContactPojo>>() {
            }.getType();
            String stored_leads =  ((SaleskenActivity) context).sharedpreferences.getString(SaleskenSharedPrefKey.LEADS, null);
            List<ContactPojo> contactPojos =((SaleskenActivity) context).gson.fromJson(stored_leads, type);
            ((ContactActivity) context).showContacts(contactPojos);*/
        }else{
            ((SaleskenActivity) context).showToast("Error While fetching contact");

        }
        ((ContactActivity) context).hideProgressBar();

    }
}
