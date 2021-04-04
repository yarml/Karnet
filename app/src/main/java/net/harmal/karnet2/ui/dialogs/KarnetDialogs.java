package net.harmal.karnet2.ui.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.DialogInterface.OnClickListener;


import net.harmal.karnet2.R;
import net.harmal.karnet2.core.registers.CustomerRegister;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class KarnetDialogs
{
    public interface OnDialogListClearListener
    {
        void onListClear(DialogInterface dialog);
    }

    @NotNull
    public static AlertDialog multiCityChoiceDialog(Context context, String[] cities            ,
                                                    boolean[] citiesChecked                     ,
                                                    OnMultiChoiceClickListener itemClickListener,
                                                    OnClickListener            onCitiesChosen   ,
                                                    OnDialogListClearListener  onClearListener  )
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.select_cities);
        builder.setMultiChoiceItems(cities, citiesChecked, itemClickListener);
        builder.setPositiveButton(R.string.ok, onCitiesChosen);
        builder.setNeutralButton(R.string.clear_selection, null);
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(dialog1 ->
                ((AlertDialog) dialog1).getButton(DialogInterface.BUTTON_NEUTRAL)
                        .setOnClickListener(v -> onClearListener.onListClear(dialog1)));
        return dialog;
    }
}
