package net.harmal.karnet2.ui.fragments.statistics;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.harmal.karnet2.R;
import net.harmal.karnet2.core.Date;
import net.harmal.karnet2.core.registers.IngredientRegister;
import net.harmal.karnet2.ui.adapters.StatisticsAdapter;
import net.harmal.karnet2.ui.fragments.KarnetFragment;
import net.harmal.karnet2.utils.Logs;

public class StatisticsFragment extends KarnetFragment
{
    private RecyclerView               statisticsList             ;
    private RecyclerView.LayoutManager statisticsListLayoutManager;
    private StatisticsAdapter          statisticsListAdapter      ;

    public StatisticsFragment()
    {
        super(R.layout.fragment_statistics);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        Logs.debug("View created STATISTICS");

        statisticsList              = view.findViewById(R.id.recycler_statistics);
        statisticsListLayoutManager = new LinearLayoutManager(requireContext(  ));
        statisticsListAdapter       = new StatisticsAdapter(IngredientRegister
                .allPossibleIngredients(), new Date(0, 0, 0  ));

        statisticsList.setLayoutManager(statisticsListLayoutManager);
        statisticsList.setAdapter(statisticsListAdapter            );
    }

    @Override
    public int getOptionsMenu()
    {
        return R.menu.options_menu_statistics;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        Date newMonitoredDate;
        int id = item.getItemId();
        if(id == R.id.option_stats_all)
            newMonitoredDate = new Date(0, 0, 0);
        else if(id == R.id.option_stats_today)
            newMonitoredDate = Date.today();
        else if(id == R.id.option_stats_week)
            newMonitoredDate = Date.beforeDays(7);
        else if(id == R.id.option_stats_month)
            newMonitoredDate = Date.beforeDays(30);
        else if(id == R.id.option_stats_custom)
        {
            Date today = Date.today();
            DatePickerDialog dialog = new DatePickerDialog(
                    requireContext(),
                    (view, year, month, dayOfMonth) -> statisticsListAdapter
                            .monitoredDate(new Date(dayOfMonth, month + 1, year)),
                    today.year(), today.month() - 1, today.day());
            dialog.show();
            return true;
        }
        else // Impossible state
            newMonitoredDate = new Date(0, 0, 0);
        statisticsListAdapter.monitoredDate(newMonitoredDate);
        return true;
    }
}
