package net.harmal.karnet2.utils;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Pair;

import androidx.core.content.ContextCompat;

import net.harmal.karnet2.R;
import net.harmal.karnet2.core.ContactData;
import net.harmal.karnet2.core.Customer;
import net.harmal.karnet2.core.Date;
import net.harmal.karnet2.core.registers.CustomerRegister;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;


/**
 * Functions here must be called from another thread otherwise
 * the UI thread will freeze
 */
public class ExternalActivityInterface
{

    private static final int PICK_CONTACT_REQ = 0;


    private static final String[] CONTACT_DATA_PROJECTION = new String[]{
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
    };

    private static final Map<Integer, Pair<Boolean, Intent>> waitingFor = new HashMap<>();

    @Nullable
    public static ContactData getContactData(@NotNull Activity a)
    {
        Intent pickContactIntent = new Intent("android.intent.action.PICK");
        pickContactIntent.setDataAndType(Uri.parse("content://contacts"),
                "vnd.android.cursor.dir/phone_v2");
        a.startActivityForResult(pickContactIntent, PICK_CONTACT_REQ);

        waitingFor.put(PICK_CONTACT_REQ, new Pair<>(false, null));

        while(!waitingFor.get(PICK_CONTACT_REQ).first);

        Pair<Boolean, Intent> activityState = waitingFor.get(PICK_CONTACT_REQ);
        assert activityState != null;

        Intent data = activityState.second;
        if(data == null)
            return null;
        ContactData contactData = new ContactData();
        assert data.getData() != null;
        Cursor cursor = a.getContentResolver().query(data.getData(), CONTACT_DATA_PROJECTION,
                null, null, null);
        assert cursor != null;
        cursor.moveToFirst();
        int numIndex  = cursor.getColumnIndex("data1"       );
        int nameIndex = cursor.getColumnIndex("display_name");
        contactData.name = cursor.getString(nameIndex);
        contactData.num  = cursor.getString(numIndex );
        cursor.close();
        Logs.debug("Picked contact: " + contactData.name);
        return contactData;
    }

    public static void activityResulted(int reqCode, @Nullable Intent intent)
    {
        synchronized (waitingFor)
        {
            waitingFor.put(reqCode, new Pair<>(true, intent));
        }
    }

    public static void cancel(int reqCode)
    {
        activityResulted(reqCode, null);
    }

    public static void syncCustomers(@NotNull Context context)
    {
        Logs.debug("Syncing");
        for(ContactData c : getContactList(context))
        {
            if(c.isCustomer())
            {
                boolean exists = false;
                for (Customer cu : CustomerRegister.get())
                {
                    if (cu.phoneNum().equalsIgnoreCase(Utils.extractCustomerNum(c)))
                    {
                        exists = true;
                        break;
                    }
                }
                if(!exists)
                {
                    CustomerRegister.add(Utils.extractCustomerName(c),
                            context.getString(R.string.default_city),
                            Utils.extractCustomerNum(c), Date.today());
                }
            }
        }
        Logs.debug("Synchronized");
    }

    @NotNull
    public static List<ContactData> getContactList(@NotNull Context context)
    {
        List<ContactData> contactList = new ArrayList<>();

        ContentResolver cr = context.getContentResolver();

        Cursor cursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                CONTACT_DATA_PROJECTION, null, null, null);
        assert cursor != null;
        HashSet<String> mobileNoSet = new HashSet<>();
        final int nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
        final int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

        String name, number;
        while (cursor.moveToNext()) {
            name = cursor.getString(nameIndex);
            number = cursor.getString(numberIndex);
            number = number.replace(" ", "");
            if (!mobileNoSet.contains(number))
            {
                contactList.add(new ContactData(name, number));
                mobileNoSet.add(number);
            }
        }
        cursor.close();
        return contactList;
    }
}
