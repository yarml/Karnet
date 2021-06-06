package net.harmal.karnet2.ui.dialogs;

import android.app.AlertDialog;
import android.os.IBinder;
import android.view.View;
import android.widget.EditText;

import net.harmal.karnet2.R;
import net.harmal.karnet2.ui.Animations;

import org.jetbrains.annotations.NotNull;

public class StringInputDialog extends KarnetDialogFragment
{

    public interface StringInputDialogInterface
    {
        void onInputReceived(String input);
    }

    private EditText textEdit;

    public StringInputDialog(int title, boolean notEmpty,
                             StringInputDialogInterface stringInputDialogInterface)
    {
        super(title, R.layout.dialog_string_input);
        positiveListener((dialog, which) -> {
            if(notEmpty && textEdit.getText().toString().isEmpty())
                Animations.shake(textEdit);
            else
                stringInputDialogInterface.onInputReceived(textEdit.getText().toString());
        });
    }

    @Override
    protected void onCreatingDialog(@NotNull View v, AlertDialog.Builder builder)
    {
        textEdit = v.findViewById(R.id.edit_text_str_input);
    }
}
