package net.harmal.karnet2.ui.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;

import net.harmal.karnet2.R;
import net.harmal.karnet2.core.IngredientBundle;
import net.harmal.karnet2.core.ProductIngredient;
import net.harmal.karnet2.core.registers.OrderRegister;
import net.harmal.karnet2.core.registers.Stock;
import net.harmal.karnet2.ui.listeners.OnItemInputListener;
import net.harmal.karnet2.utils.Logs;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class StockTableAdapter extends KarnetRecyclerTableAdapter<
        StockTableAdapter.StockTableViewHolder,
        StockTableAdapter.StockTableViewHolder,
        StockTableAdapter.StockTableViewHolder,
        StockTableAdapter.StockTableViewHolder >
{
    public static class StockTableViewHolder extends KarnetRecyclerViewHolder
    {
        TextView text;
        public StockTableViewHolder(@NonNull View itemView, KarnetRecyclerAdapter<? extends KarnetRecyclerViewHolder> adapter)
        {
            super(itemView, adapter);
            text = itemView.findViewById(R.id.text_layout_text);
        }
    }

    private final List<ProductIngredient> columnElements;
    private final List<Pair<ProductIngredient, ProductIngredient>> rowElements;
    private ProductIngredient baseIngredient;
    private ProductIngredient fatIngredient ;

    public StockTableAdapter(int columns, int rows, ProductIngredient baseIngredient,
                             ProductIngredient fatIngredient ,
                             List<ProductIngredient> shapes  ,
                             @NotNull List<ProductIngredient> tastes  ,
                             List<ProductIngredient> extras)
    {
        super(columns, rows);
        this.baseIngredient = baseIngredient;
        this.fatIngredient = fatIngredient;
        columnElements = new ArrayList<>(shapes);
        rowElements = new ArrayList<>();
        for(ProductIngredient taste : tastes)
        {
            rowElements.add(new Pair<>(taste, null));
            for (ProductIngredient extra : extras)
                rowElements.add(new Pair<>(taste, extra));
        }
    }

    @Override
    public StockTableViewHolder onCreateColumnHeaderViewHolder(@NonNull ViewGroup parent)
    {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.table_column_header_stock, parent, false);
        return new StockTableViewHolder(v, this);
    }

    @Override
    public StockTableViewHolder onCreateRowHeaderViewHolder(@NonNull ViewGroup parent)
    {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.table_row_header_stock, parent, false);
        return new StockTableViewHolder(v, this);
    }

    @Override
    public StockTableViewHolder onCreateCellViewHolder(@NonNull ViewGroup parent)
    {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.table_cell_stock, parent, false);
        return new StockTableViewHolder(v, this);
    }

    @Override
    public StockTableViewHolder onCreateCornerViewHolder(@NonNull ViewGroup parent)
    {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.table_corner_stock, parent, false);
        return new StockTableViewHolder(v, this);
    }

    @Override
    public void onBindColumnHeaderViewHolder(@NonNull StockTableViewHolder holder, int column)
    {
        Logs.debug("BINDING COLUMN_HEADER");
        holder.text.setText(columnElements.get(column - 1).displayName());
    }

    @Override
    public void onBindRowHeaderViewHolder(@NonNull StockTableViewHolder holder, int row)
    {
        Logs.debug("BINDING ROW_HEADER");
        Pair<ProductIngredient, ProductIngredient> current = rowElements.get(row - 1);
        assert current.first != null;

        if(current.second != null)
            holder.text.setText(String.format("%s(+%s)", current.first.displayName(), current.second.displayName()));
        else
            holder.text.setText(current.first.displayName());
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindCellViewHolder(@NonNull StockTableViewHolder holder, int column, int row)
    {
        Logs.debug("BINDING CELL");
        ProductIngredient shapeIngredient = columnElements.get(column - 1);
        Pair<ProductIngredient, ProductIngredient> rowEl = rowElements.get(row - 1);
        assert rowEl.first != null;
        ProductIngredient taste = rowEl.first;
        List<Integer> extras = new ArrayList<>();
        if(rowEl.second != null)
            extras.add(rowEl.second.piid());
        IngredientBundle bundle = new IngredientBundle(baseIngredient.piid(), fatIngredient.piid(),
                shapeIngredient.piid(), taste.piid(), extras);
        int stock = Stock.countOf(bundle);
        int count = OrderRegister.countOf(bundle);
        int sum = stock - count;
        holder.text.setText(String.format("%d / %d", Stock.countOf(bundle), sum));
    }

    @Override
    public void onBindCornerViewHolder(@NonNull StockTableViewHolder holder)
    {
        Logs.debug("BINDING CORNER");
    }

    public void setBaseIngredient(ProductIngredient base)
    {
        this.baseIngredient = base;
        update();
    }

    public void setFatIngredient(ProductIngredient fat)
    {
        this.fatIngredient = fat;
        update();
    }


    public int shapeAt(int column)
    {
        return columnElements.get(column - 1).piid();
    }

    public int tasteAt(int row)
    {
        ProductIngredient taste = rowElements.get(row - 1).first;
        assert taste != null;
        return taste.piid();
    }

    public List<Integer> extrasAt(int row)
    {
        ProductIngredient extra = rowElements.get(row - 1).second;
        if(extra == null)
            return new ArrayList<>();
        List<Integer> extras = new ArrayList<>();
        extras.add(extra.piid());
        return extras;
    }

    public void update()
    {
        notifyDataSetChanged();
    }

}
