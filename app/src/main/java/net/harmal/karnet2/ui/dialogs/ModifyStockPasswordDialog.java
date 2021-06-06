package net.harmal.karnet2.ui.dialogs;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import net.harmal.karnet2.R;
import net.harmal.karnet2.core.registers.Stock;
import net.harmal.karnet2.ui.Animations;
import net.harmal.karnet2.utils.Logs;

import org.jetbrains.annotations.NotNull;

public class ModifyStockPasswordDialog extends KarnetDialogFragment
{
    private EditText oldPasswordEdit;
    private EditText newPasswordEdit;
    private EditText confirmPasswordEdit;

    public ModifyStockPasswordDialog()
    {
        super(R.string.modify_stock_password_title, R.layout.dialog_modify_stock_password);
        positive(R.string.ok);
        positiveListener((dialog, which) -> {});
    }

    @Override
    protected void onCreatingDialog(@NotNull View v, AlertDialog.Builder builder)
    {
        oldPasswordEdit = v.findViewById(R.id.edit_old_stock_password);
        newPasswordEdit = v.findViewById(R.id.edit_new_stock_password);
        confirmPasswordEdit = v.findViewById(R.id.edit_confirm_stock_password);
    }

    @Override
    protected void onDialogCreated(AlertDialog dialog)
    {
        super.onDialogCreated(dialog);
        dialog.setOnShowListener(dialog1 -> {
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(v -> {
                if(!oldPasswordEdit.getText().toString().equals(Stock.password))
                {
                    Animations.shake(oldPasswordEdit);
                    Toast.makeText(requireContext(), R.string.invalid_password, Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!newPasswordEdit.getText().toString().equals(confirmPasswordEdit.getText().toString()))
                {
                    Animations.shake(confirmPasswordEdit);
                    Toast.makeText(requireContext(), R.string.confirm_not_math, Toast.LENGTH_SHORT).show();
                    return;
                }

                Stock.password = newPasswordEdit.getText().toString();
                Logs.debug(Stock.password);
                dismiss();
            });
        });
    }
}
