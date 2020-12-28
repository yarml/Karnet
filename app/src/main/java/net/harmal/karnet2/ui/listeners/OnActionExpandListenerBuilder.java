package net.harmal.karnet2.ui.listeners;

import android.view.MenuItem;

public class OnActionExpandListenerBuilder
{
    public interface OnActionExpandListener
    {
        boolean onMenuItemActionExpand(MenuItem item);
    }
    public interface OnActionCollapseListener
    {
        boolean onMenuItemActionCollapse(MenuItem item);
    }

    private OnActionExpandListener   expandListener  ;
    private OnActionCollapseListener collapseListener;

    public OnActionExpandListenerBuilder(OnActionExpandListener expandListener, OnActionCollapseListener collapseListener)
    {
        this.expandListener   = expandListener  ;
        this.collapseListener = collapseListener;
    }

    public MenuItem.OnActionExpandListener build()
    {
        return new MenuItem.OnActionExpandListener()
        {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item)
            {
                if(expandListener == null)
                    return true;
                return expandListener.onMenuItemActionExpand(item);
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item)
            {
                if(collapseListener == null)
                    return true;
                return collapseListener.onMenuItemActionCollapse(item);
            }
        };
    }
}
