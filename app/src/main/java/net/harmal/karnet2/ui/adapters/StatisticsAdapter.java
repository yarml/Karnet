package net.harmal.karnet2.ui.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import net.harmal.karnet2.R;
import net.harmal.karnet2.core.Date;
import net.harmal.karnet2.core.IngredientBundle;
import net.harmal.karnet2.core.registers.OrdersLog;
import net.harmal.karnet2.ui.listeners.OnItemInputListener;

import java.util.List;

public class StatisticsAdapter extends KarnetRecyclerAdapter<StatisticsAdapter.StatisticsViewHolder>
{
    public static class StatisticsViewHolder extends KarnetRecyclerViewHolder
    {
        TextView productName ;
        TextView sellingsView;
        public StatisticsViewHolder(@NonNull View itemView, OnItemInputListener listener)
        {
            super(itemView, listener);
            productName  = itemView.findViewById(R.id.text_stats_product_name  );
            sellingsView = itemView.findViewById(R.id.text_stats_sellings_total);
        }
    }

    private Date                   monitoredDate   ;
    private List<IngredientBundle> monitoredBundles;

    public StatisticsAdapter(@NonNull List<IngredientBundle> monitoredBundles, Date monitoredDate)
    {
        this.monitoredDate    = monitoredDate   ;
        this.monitoredBundles = monitoredBundles;
    }

    @NonNull
    @Override
    public StatisticsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_statistics, parent, false);
        return new StatisticsViewHolder(v, onItemInputListener);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull StatisticsViewHolder holder, int position)
    {
        IngredientBundle current = monitoredBundles.get(position);
        holder.productName.setText(current.name());
        holder.sellingsView.setText(String.format("%d",
                OrdersLog.getCountOf(current, monitoredDate)));
    }

    public void update()
    {
        notifyDataSetChanged();
    }

    public void monitoredDate(@NonNull Date date)
    {
        this.monitoredDate = date;
        update();
    }

    public void monitoredBundles(@NonNull List<IngredientBundle> monitoredBundles)
    {
        this.monitoredBundles = monitoredBundles;
        update();
    }

    @Override
    public int getItemCount()
    {
        return monitoredBundles.size();
    }

}
