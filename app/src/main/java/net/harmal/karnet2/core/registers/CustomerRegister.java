package net.harmal.karnet2.core.registers;

import net.harmal.karnet2.core.Customer;
import net.harmal.karnet2.core.Date;
import net.harmal.karnet2.core.Order;
import net.harmal.karnet2.utils.Logs;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CustomerRegister
{

    public static int customerIdCount = 0;

    private static List<Customer> customerRegister;

    // Returns CID
    public static int add(@NotNull String name, @NotNull String city, @NotNull String phoneNum, @NotNull Date creationDate)
    {
        return add(customerIdCount++, name, city, phoneNum, creationDate);
    }
    // Returns CID (can be different from the given one)
    public static int add(int cid, @NotNull String name, @NotNull String city, @NotNull String phoneNum, @NotNull Date creationDate)
    {
        if(customerRegister == null)
            customerRegister = new ArrayList<Customer>();
        if(cid < 0) // CID must be positive
        {
            return add(0, name, city, phoneNum, creationDate);
        }
        for(Customer c : customerRegister) // Make sure the CID is unique
            if(c.cid() == cid)
            {
                return add(cid + 1, name, city, phoneNum, creationDate);
            }
        customerRegister.add(new Customer(cid, creationDate, name, city, phoneNum));
        return cid;
    }

    public static void remove(int cid)
    {
        if(customerRegister == null)
            customerRegister = new ArrayList<>();

        for(Customer c : customerRegister)
            if(c.cid() == cid)
            {
                customerRegister.remove(c);
                break;
            }
        for(Order o : OrderRegister.get())
            if(o.cid() == cid)
                OrderRegister.get().remove(o);
    }

    @Nullable
    public static Customer getCustomer(int cid)
    {
        for(Customer c : customerRegister)
            if(c.cid() == cid)
                return c;
        Logs.error("No customer with id: " + cid);
        return null;
    }

    @NotNull
    public static List<Customer> get()
    {
        if(customerRegister == null)
            customerRegister = new ArrayList<>();
        return customerRegister;
    }

    public static int size()
    {
        return get().size();
    }

}
