package net.harmal.karnet2.ui.dialogs;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.IBinder;
import android.view.View;


import net.harmal.karnet2.R;

public class ConfirmationDialog extends KarnetDialogFragment
{
    private final String text;
    public ConfirmationDialog(int title, String text, DialogInterface.OnClickListener ok, IBinder windowToken)
    {
        super(title, R.layout.layout_simple, windowToken);
        positiveListener(ok);
        negativeListener((dialog, which) -> dismiss());
        this.text = text;
    }

    @Override
    protected void onCreatingDialog(View v, AlertDialog.Builder builder)
    {
        builder.setMessage(text);
    }
}
