package net.harmal.karnet2.core;

import java.util.Stack;

public class Trash
{
    private static Stack<Customer> customerTrash; // Sorry for the name
    private static Stack<Order>    orderTrash   ;

    public static void pushCustomer(Customer c)
    {
        if(customerTrash == null)
            customerTrash = new Stack<>();
        customerTrash.push(c);
    }
    public static Customer popCustomer()
    {
        if(customerTrash == null)
            customerTrash =  new Stack<>();
        return customerTrash.pop();
    }

    public static void pushOrder(Order o)
    {
        if(orderTrash == null)
            orderTrash = new Stack<>();
        orderTrash.push(o);
    }
    public static Order popOrder()
    {
        if(orderTrash == null)
            orderTrash = new Stack<>();
        return orderTrash.pop();
    }
}
