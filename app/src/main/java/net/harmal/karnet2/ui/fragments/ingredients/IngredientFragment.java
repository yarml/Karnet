package net.harmal.karnet2.ui.fragments.ingredients;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import net.harmal.karnet2.R;
import net.harmal.karnet2.core.ProductIngredient;
import net.harmal.karnet2.core.Trash;
import net.harmal.karnet2.core.registers.IngredientRegister;
import net.harmal.karnet2.ui.Animations;
import net.harmal.karnet2.ui.adapters.IngredientListAdapter;
import net.harmal.karnet2.ui.fragments.KarnetFragment;
import net.harmal.karnet2.ui.listeners.OnItemInputListener;
import net.harmal.karnet2.utils.Logs;

import org.jetbrains.annotations.NotNull;

import static android.view.View.GONE;

public class IngredientFragment extends KarnetFragment
{
    private TextView                   noIngredientText           ;
    private RecyclerView               ingredientList             ;
    private RecyclerView.LayoutManager ingredientListLayoutManager;
    private IngredientListAdapter      ingredientListAdapter      ;

    public IngredientFragment()
    {
        super(R.layout.fragment_ingredient);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        noIngredientText            = view.findViewById(R.id.text_fragment_ingredient_no_ingredient);
        ingredientList              = view.findViewById(R.id.recycler_ingredient_list              );
        ingredientListLayoutManager = new LinearLayoutManager(getContext(                         ));
        ingredientListAdapter       = new IngredientListAdapter(IngredientRegister.get(           ));

        ingredientListAdapter.setOnItemInputListener(new OnItemInputListener.Builder(
                this::onItemClick, this::onItemLongClick));

        ingredientList.setLayoutManager(ingredientListLayoutManager);
        ingredientList.setAdapter(ingredientListAdapter);
        if(IngredientRegister.size() == 0)
        {
            noIngredientText.setVisibility(View.VISIBLE);
            ingredientList.setVisibility(GONE);
        }
        else
        {
            noIngredientText.setVisibility(GONE);
            ingredientList.setVisibility(View.VISIBLE);
        }
    }

    private void onItemClick(@NotNull View view, int position)
    {
        if(view.getId() == R.id.btn_ingredient_delete)
        {
            ProductIngredient p = ingredientListAdapter.getVisibleItems().get(position);
            IngredientRegister.remove(p.piid());
            ingredientListAdapter.update();
            Snackbar undo = Snackbar.make(requireView(), R.string.ingredient_removed,
                    Snackbar.LENGTH_LONG);
            undo.setAction(R.string.undo, this::onUndoIngredientDeletion);
            undo.show();
        }
        else
        {
            View v = ingredientListLayoutManager.findViewByPosition(position);
            assert v != null;
            ImageButton deleteButton = v.findViewById(R.id.btn_ingredient_delete);
            if(deleteButton.getVisibility() == View.VISIBLE)
            {
                Animations.popOut(deleteButton);
                return;
            }
            ProductIngredient p = ingredientListAdapter.getVisibleItems().get(position);
            NavDirections action = IngredientFragmentDirections
                    .actionIngredientFragmentToIngredientAddModifyFragment(
                            String.format(getString(R.string.modify), p.displayName()), p.piid());
            NavHostFragment.findNavController(this).navigate(action);
        }
    }

    private void onItemLongClick(View view, int position)
    {
        View v = ingredientListLayoutManager.findViewByPosition(position);
        assert v != null;
        ImageButton deleteButton = v.findViewById(R.id.btn_ingredient_delete);
        if(deleteButton.getVisibility() == View.GONE) // should appear
            Animations.popIn(deleteButton);
        else
            Animations.popOut(deleteButton);
    }


    private void onUndoIngredientDeletion(View view)
    {
        int piid = IngredientRegister.add(Trash.popIngredient());
        ProductIngredient p = IngredientRegister.getIngredient(piid);
        ingredientListAdapter.update();
        if(ingredientListAdapter.getVisibleItems().contains(p))
            ingredientListAdapter.notifyItemInserted(
                    ingredientListAdapter.getItemCount() - 1);
    }

    @Override
    public int getOptionsMenu()
    {
        return R.menu.options_menu_ingredient;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if(item.getItemId() == R.id.options_add_ingredient)
        {
            NavDirections action = IngredientFragmentDirections
                    .actionIngredientFragmentToIngredientAddModifyFragment(
                            getString(R.string.new_ingredient), -1);
            NavHostFragment.findNavController(this).navigate(action);
        }
        return true;
    }
}
