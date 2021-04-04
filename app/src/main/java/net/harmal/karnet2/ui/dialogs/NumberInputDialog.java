package net.harmal.karnet2.ui.dialogs;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.IBinder;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.StringRes;

import net.harmal.karnet2.R;
import net.harmal.karnet2.ui.Animations;

import org.jetbrains.annotations.NotNull;

public class NumberInputDialog extends KarnetDialogFragment
{
    public interface NumberInputDialogInterface
    {
        void onNumberInputReceived(int input);
    }
    private EditText  numEdit;
    private final NumberInputDialogInterface numberInputDialogInterface;

    public NumberInputDialog(@StringRes int title,
                             NumberInputDialogInterface numberInputDialogInterface,
                             IBinder windowToken)
    {
        super(title, R.layout.dialog_number_input, windowToken);
        this.numberInputDialogInterface = numberInputDialogInterface;
        positiveListener(this::onOk);
    }

    private void onOk(DialogInterface dialog, int which)
    {
        try
        {
            int num = Integer.parseInt(numEdit.getText().toString());
            numberInputDialogInterface.onNumberInputReceived(num);
        }
        catch(Exception e)
        {
            Animations.shake(numEdit);
            Toast.makeText(requireContext(), R.string.invalid_number, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreatingDialog(@NotNull View v, AlertDialog.Builder builder)
    {
        numEdit = v.findViewById(R.id.edit_num_input);
    }
}
