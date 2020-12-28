package net.harmal.karnet2.ui.listeners;

import android.view.MenuItem;
import android.widget.SearchView;

public class OnQueryTextListenerBuilder
{
    public interface OnQueryTextChangeListener
    {
        boolean onQueryTextChange(String newText);
    }
    public interface OnQueryTextSubmitListener
    {
        boolean onQueryTextSubmit(String query);
    }

    private OnQueryTextChangeListener textChangeListener;
    private OnQueryTextSubmitListener textSubmitListener;

    public OnQueryTextListenerBuilder(OnQueryTextChangeListener textChangeListener,
                                      OnQueryTextSubmitListener textSubmitListener)
    {
        this.textChangeListener = textChangeListener  ;
        this.textSubmitListener = textSubmitListener;
    }

    public SearchView.OnQueryTextListener build()
    {
        return new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                if(textSubmitListener == null)
                    return false;
                return textSubmitListener.onQueryTextSubmit(query);
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                if(textChangeListener == null)
                    return false;
                return textChangeListener.onQueryTextChange(newText);
            }
        };
    }
}
