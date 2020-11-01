package net.harmal.karnet2.ui.fragments.product;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import net.harmal.karnet2.R;
import net.harmal.karnet2.core.Product;
import net.harmal.karnet2.core.registers.ProductRegister;
import net.harmal.karnet2.ui.fragments.KarnetFragment;

public class ProductAddModifyFragment extends KarnetFragment
{

    private int      pid     ;

    private EditText nameEdit;
    private Button   baseBtn ;
    private Button   fatBtn  ;
    private Button   shapeBtn;
    private Button   typeBtn ;
    private Button   extraBtn;

    public ProductAddModifyFragment() { super(R.layout.fragment_add_modify_product); }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        assert getArguments() != null;
        ProductAddModifyFragmentArgs args = ProductAddModifyFragmentArgs.fromBundle(getArguments());

        pid = args.getPid();

        nameEdit = view.findViewById(R.id.edit_text_add_product_name);
        baseBtn  = view.findViewById(R.id.btn_select_base           );
        fatBtn   = view.findViewById(R.id.btn_select_fat            );
        shapeBtn = view.findViewById(R.id.btn_select_shape          );
        typeBtn  = view.findViewById(R.id.btn_select_type           );
        extraBtn = view.findViewById(R.id.btn_select_extra          );

        // TODO: add the lists here
        if(pid < 0)
        {
            nameEdit.setText("");
        }
        else
        {
            Product p = ProductRegister.getProduct(pid);
            nameEdit.setText(p.name());
        }

        // Setting buttons behavior
        baseBtn.setOnClickListener(this::onBaseSelectButtonClick);
    }

    private void onBaseSelectButtonClick(View v)
    {

    }
}
