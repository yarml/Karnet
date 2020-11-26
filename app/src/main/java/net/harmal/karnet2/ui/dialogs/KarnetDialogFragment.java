package net.harmal.karnet2.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatDialogFragment;

import net.harmal.karnet2.R;
import net.harmal.karnet2.utils.Logs;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class KarnetDialogFragment extends AppCompatDialogFragment
{

    public interface CustomDismissEvent
    {
        void onDismiss(@NotNull DialogInterface dialog);
    }

    @LayoutRes
    private int layout;
    @StringRes
    private int title ;
    @StringRes
    private int positive = R.string.dialog_positive;
    @StringRes
    private int negative = R.string.dialog_negative;
    @StringRes
    private int neutral  = R.string.dialog_neutral;
    private final IBinder windowToken;

    private View view;

    private DialogInterface.OnClickListener positiveListener;
    private DialogInterface.OnClickListener negativeListener;
    private DialogInterface.OnClickListener neutralListener ;

    private final List<CustomDismissEvent> customDismissEvents;


    public KarnetDialogFragment(@StringRes int title, @LayoutRes int layout, IBinder windowToken)
    {
        this.title = title;
        this.layout = layout;
        this.windowToken = windowToken;
        customDismissEvents = new ArrayList<>();
    }

    public void addOnDismissEvent(@NotNull CustomDismissEvent e)
    {
        customDismissEvents.add(e);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title);
        View view = getLayoutInflater().inflate(layout, null);

        if(positiveListener != null)
            builder.setPositiveButton(positive, positiveListener);
        if(negativeListener != null)
            builder.setNegativeButton(negative, negativeListener);
        if(neutralListener != null)
            builder.setNeutralButton (neutral , neutralListener );

        onCreatingDialog(view, builder);
        builder.setView(view);
        this.view = view;
        return builder.create();
    }

    protected abstract void onCreatingDialog(View v, AlertDialog.Builder builder);

    @Override
    public void onDismiss(@NonNull DialogInterface dialog)
    {
        super.onDismiss(dialog);
        Logs.debug("Dismissing dialog");
        ((InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(windowToken, 0);
        for(CustomDismissEvent e : customDismissEvents)
            e.onDismiss(dialog);
    }


    @LayoutRes
    public int layout()
    {
        return layout;
    }

    public void layout(@LayoutRes int layout)
    {
        this.layout = layout;
    }

    @StringRes
    public int title()
    {
        return title;
    }

    public void title(@StringRes int title)
    {
        this.title = title;
    }

    @StringRes
    public int positive()
    {
        return positive;
    }

    public void positive(@StringRes int positive)
    {
        this.positive = positive;
    }

    @StringRes
    public int negative() {
        return negative;
    }

    public void negative(@StringRes int negative)
    {
        this.negative = negative;
    }

    @StringRes
    public int neutral() {
        return neutral;
    }

    public void neutral(@StringRes int neutral)
    {
        this.neutral = neutral;
    }

    public DialogInterface.OnClickListener positiveListener()
    {
        return positiveListener;
    }

    public void positiveListener(DialogInterface.OnClickListener positiveListener)
    {
        this.positiveListener = positiveListener;
    }

    public DialogInterface.OnClickListener negativeListener()
    {
        return negativeListener;
    }

    public void negativeListener(DialogInterface.OnClickListener negativeListener)
    {
        this.negativeListener = negativeListener;
    }

    public DialogInterface.OnClickListener neutralListener()
    {
        return neutralListener;
    }

    public void neutralListener(DialogInterface.OnClickListener neutralListener)
    {
        this.neutralListener = neutralListener;
    }
}
