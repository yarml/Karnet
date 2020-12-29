package net.harmal.karnet2.utils;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Pair;

import net.harmal.karnet2.core.ContactData;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;


/**
 * Functions here must be called from another thread otherwise
 * the UI thread will freeze
 */
public class ExternalActivityInterface
{

    private static final int PICK_CONTACT_REQ = 0;

    private static final Map<Integer, Pair<Boolean, Intent>> waitingFor = new HashMap<>();

    @Nullable
    public static ContactData getContactData(@NotNull Activity a)
    {
        Intent pickContactIntent = new Intent("android.intent.action.PICK");
        pickContactIntent.setDataAndType(Uri.parse("content://contacts"),
                "vnd.android.cursor.dir/phone_v2");
        a.startActivityForResult(pickContactIntent, PICK_CONTACT_REQ);

        Pair<Boolean, Intent> activityState = waitingFor.get(PICK_CONTACT_REQ);
        assert activityState != null;
        waitingFor.put(PICK_CONTACT_REQ, new Pair<>(false, null));

        while(!activityState.first);

        Intent data = activityState.second;
        if(data == null)
            return null;
        ContactData contactData = new ContactData();
        assert data.getData() != null;
        Cursor cursor = a.getContentResolver().query(data.getData(),
                new String[]{"data1", "display_name"},
                null, null, null);
        assert cursor != null;
        cursor.moveToFirst();
        int numIndex  = cursor.getColumnIndex("data1"       );
        int nameIndex = cursor.getColumnIndex("display_name");
        contactData.name = cursor.getString(nameIndex);
        contactData.num  = cursor.getString(numIndex );
        cursor.close();
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
}
