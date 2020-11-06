package net.harmal.karnet2.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatDialogFragment;

public abstract class KarnetDialogFragment extends AppCompatDialogFragment
{
    @LayoutRes
    private int layout;
    @StringRes
    private int title ;

    public KarnetDialogFragment(@StringRes int title, @LayoutRes int layout)
    {
        this.title = title;
        this.layout = layout;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title);
        View view = getLayoutInflater().inflate(layout, null);
        builder.setView(view);
        onCreatingDialog(view, builder);
        return builder.create();
    }

    protected abstract void onCreatingDialog(View v, AlertDialog.Builder builder);
}
