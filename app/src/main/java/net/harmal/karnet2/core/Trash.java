package net.harmal.karnet2.core;

import java.util.Stack;

public class Trash
{
    private static Stack<Customer>          customerTrash  ; // Sorry for the name
    private static Stack<Order>             orderTrash     ;
    private static Stack<ProductIngredient> ingredientTrash;

    public static void pushIngredient(ProductIngredient p)
    {
        if(ingredientTrash == null)
            ingredientTrash = new Stack<>();
        ingredientTrash.push(p);
    }

    public static ProductIngredient popIngredient()
    {
        if(ingredientTrash == null)
            ingredientTrash = new Stack<>();
        return ingredientTrash.pop();
    }

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
