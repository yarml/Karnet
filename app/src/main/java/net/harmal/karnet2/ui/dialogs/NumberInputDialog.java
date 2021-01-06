package net.harmal.karnet2.ui.dialogs;

import android.app.AlertDialog;
import android.os.IBinder;
import android.view.View;
import android.widget.NumberPicker;

import androidx.annotation.StringRes;

import net.harmal.karnet2.R;

public class NumberInputDialog extends KarnetDialogFragment
{
    public interface NumberInputDialogInterface
    {
        void onNumberInputReceived(int input);
    }
    private NumberPicker picker;
    private final int min;
    private final int max;

    public NumberInputDialog(@StringRes int title, int min, int max,
                             NumberInputDialogInterface numberInputDialogInterface,
                             IBinder windowToken)
    {
        super(title, R.layout.dialog_number_input, windowToken);
        this.min = min;
        this.max = max;
        positiveListener(((dialog, which) -> numberInputDialogInterface.onNumberInputReceived(picker.getValue())));
    }

    @Override
    protected void onCreatingDialog(View v, AlertDialog.Builder builder)
    {
        picker = v.findViewById(R.id.number_picker_number_input);
        picker.setMinValue(min);
        picker.setMaxValue(max);
    }
}
