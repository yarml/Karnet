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

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import net.harmal.karnet2.R;
import net.harmal.karnet2.core.ContactData;
import net.harmal.karnet2.core.Customer;
import net.harmal.karnet2.core.Date;
import net.harmal.karnet2.core.registers.CustomerRegister;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * Functions here must be called from another thread otherwise
 * the UI thread will freeze
 */
public class ExternalActivityInterface
{

    private static final int PICK_CONTACT_REQ = 0;

    private static int permRequestCount = 0;

    private static final String[] CONTACT_DATA_PROJECTION = new String[]{
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
    };

    private static final Map<Integer, Pair<Boolean, Intent>> waitingFor = new HashMap<>();

    private static final Map<Integer, String[]> requestedPermMap = new HashMap<>();
    private static final Map<Integer, PermissionGrantInterface> permissionInterfaceMap = new HashMap<>();

    @Nullable
    public static ContactData getContactData(@NotNull Activity a)
    {

        Intent pickContactIntent = new Intent(Intent.ACTION_PICK);
        pickContactIntent.setDataAndType(Uri.parse("content://contacts"),
                "vnd.android.cursor.dir/phone_v2");
        a.startActivityForResult(pickContactIntent, PICK_CONTACT_REQ);

        waitingFor.put(PICK_CONTACT_REQ, new Pair<>(false, null));

        while (!Objects.requireNonNull(waitingFor.get(PICK_CONTACT_REQ)).first) ;
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

    public static void grantPermissions(@NonNull Activity a, @NotNull String[] perms, @NonNull PermissionGrantInterface grantInterface)
    {
        List<String> permToGrant = new ArrayList<>();
        for(String perm : perms)
        {
            int permissionCheck = ContextCompat.checkSelfPermission(a, perm);
            if(permissionCheck != PackageManager.PERMISSION_GRANTED)
                permToGrant.add(perm);
        }
        if(permToGrant.size() != 0)
        {
            String[] requestedPerms = permToGrant.toArray(new String[0]);
            ActivityCompat.requestPermissions(a, requestedPerms, permRequestCount);
            permissionInterfaceMap.put(permRequestCount, grantInterface);
            requestedPermMap.put(permRequestCount++, requestedPerms);
        }
        else
            grantInterface.resulted(new String[0], new String[0], new String[0]);
    }

    public static void requestedPermissionResult(int reqCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        Logs.debug("Requested permission resulted");
        List<String> granted = new ArrayList<>();
        List<String> denied  = new ArrayList<>();

        for(int i = 0; i < permissions.length; i++)
        {
            if(grantResults[i] == PackageManager.PERMISSION_GRANTED)
                granted.add(permissions[i]);
            else
                denied.add(permissions[i]);
        }
        String[] grantedArr = granted.toArray(new String[0]);
        String[] deniedArr = denied.toArray(new String[0]);
        String[] requestedArr = requestedPermMap.remove(reqCode);
        PermissionGrantInterface grantInterface = permissionInterfaceMap.remove(reqCode);
        assert grantInterface != null && requestedArr != null;
        grantInterface.resulted(requestedArr, grantedArr, deniedArr);
    }
    public static void activityResulted(int reqCode, @Nullable Intent intent)
    {
        Logs.debug("External Activity resulted");
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
        Logs.debug("Getting contact list");
        List<ContactData> contactList = new ArrayList<>();

        ContentResolver cr = context.getContentResolver();

        Cursor cursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                CONTACT_DATA_PROJECTION, null, null, null);
        assert cursor != null;
        HashSet<String> mobileNoSet = new HashSet<>();
        final int nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
        final int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

        String name, number;
        Logs.debug("Iterating over contact list");
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
        Logs.debug("Closing contacts cursor");
        cursor.close();
        return contactList;
    }

    public interface PermissionGrantInterface
    {
        void resulted(@NonNull String[] requested, @NonNull String[] granted, @NonNull String[] denied);
    }
    public interface ContactDataInterface
    {
        void resulted(ContactData data);
    }
}
