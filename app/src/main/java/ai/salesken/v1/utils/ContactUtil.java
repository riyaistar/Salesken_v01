package ai.salesken.v1.utils;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.room.Room;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.ref.Reference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import ai.salesken.v1.R;
import ai.salesken.v1.constant.SaleskenSharedPrefKey;
import ai.salesken.v1.pojo.ContactPojo;

public class ContactUtil {
    private static final String TAG_ANDROID_CONTACTS = "FetchAllContactsUtil";

    public AppDatabase db;
    List<ContactPojo> contactPojos;
    public String fetchContacts(Context context) {
        db=AppDatabase.getInstance(context);
        try {
            contactPojos = new ArrayList<>();
            db.contactDao().deleteAll();
            String phoneNumber = null;
            String email = null;

            Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
            String _ID = ContactsContract.Contacts._ID;
            String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
            String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

            Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
            String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
            String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;

            Uri EmailCONTENT_URI = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
            String EmailCONTACT_ID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
            String DATA = ContactsContract.CommonDataKinds.Email.DATA;

            StringBuffer output = new StringBuffer();

            ContentResolver contentResolver = context.getContentResolver();

            Cursor cursor = contentResolver.query(CONTENT_URI, null, null, null, null);

            // Loop for every contact in the phone
            if (cursor.getCount() > 0) {

                while (cursor.moveToNext()) {

                    String contact_id = cursor.getString(cursor.getColumnIndex(_ID));
                    String name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));
                    ContactPojo contactPojo = new ContactPojo();

                    int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(HAS_PHONE_NUMBER)));

                    if (hasPhoneNumber > 0) {
                        output.append("\n First Name:" + name);
                        contactPojo.setName(name);
                        // Query and loop for every phone number of the contact
                        Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[]{contact_id}, "DISPLAY_NAME ASC");

                        while (phoneCursor.moveToNext()) {
                            phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));

                            output.append("\n Phone number:" + phoneNumber);
                            contactPojo.setMobile(phoneNumber);


                        }

                        phoneCursor.close();

                        // Query and loop for every email of the contact
                        Cursor emailCursor = contentResolver.query(EmailCONTENT_URI, null, EmailCONTACT_ID + " = ?", new String[]{contact_id}, null);

                        while (emailCursor.moveToNext()) {

                            email = emailCursor.getString(emailCursor.getColumnIndex(DATA));
                            contactPojo.setEmail(email);
                            output.append("\nEmail:" + email);

                        }

                        emailCursor.close();

                    }
                    if(contactPojo.getName() != null && contactPojo.getMobile() != null){
                        contactPojos.add(contactPojo);

                    }
                    output.append("\n");
                }

                db.contactDao().insertAll(contactPojos);

                cursor.close();

                Log.d(TAG_ANDROID_CONTACTS, "***** " + output);

            }
        }catch (Exception e){
            e.printStackTrace();
            return "fail";

        }

        return "success";
    }




    public List<ContactPojo> removeDuplicates(List<ContactPojo> list) {
        // Set set1 = new LinkedHashSet(list);
        Set set = new TreeSet(new Comparator() {

            @Override
            public int compare(Object o1, Object o2) {
                if (((ContactPojo) o1).getMobile() == (((ContactPojo) o2).getMobile()) /*&&
                    ((Blog)o1).getName().equalsIgnoreCase(((Blog)o2).getName())*/) {
                    return 0;
                }
                return 1;
            }
        });
        set.addAll(list);

      final List<ContactPojo> newList = new ArrayList(set);
        /*Collections.sort(newList, new Comparator() {
            public int compare(Object synchronizedListOne, Object synchronizedListTwo) {
                //use instanceof to verify the references are indeed of the type in question
                return ((ContactPojo) synchronizedListOne).getCompanyName()
                        .compareTo(((ContactPojo) synchronizedListTwo).getCompanyName());
            }
        });*/

        return newList;
    }

    public Boolean insertContact(Context context,String firstname,String lastname,String mobile,String email,String company,String title){
        try {
            ArrayList<ContentProviderOperation> op_list = new ArrayList<ContentProviderOperation>();
            op_list.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                    //.withValue(RawContacts.AGGREGATION_MODE, RawContacts.AGGREGATION_MODE_DEFAULT)
                    .build());

            // first and last names
            op_list.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, firstname)
                    .withValue(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME, lastname)
                    .build());

            op_list.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, mobile)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_HOME)
                    .build());
            op_list.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)

                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Email.DATA, email)
                    .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                    .build());
            op_list.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Organization.COMPANY, company)
                    .withValue(ContactsContract.CommonDataKinds.Organization.TITLE, title)
                    .withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
                    .build());


            try {
                ContentProviderResult[] results = context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, op_list);
                return true;

            } catch (Exception e) {
                e.printStackTrace();
                return false;

            }
        }catch (Exception e){
            e.printStackTrace();
            return false;

        }

    }

}
