package net.harmal.karnet2.core.registers;

import net.harmal.karnet2.core.Customer;
import net.harmal.karnet2.core.Date;
import net.harmal.karnet2.core.Order;
import net.harmal.karnet2.core.Trash;
import net.harmal.karnet2.utils.Logs;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CustomerRegister
{

    public static int customerIdCount = 0;

    private static List<Customer> customerRegister;

    public static int add(@NotNull Customer c)
    {
        return add(c.cid(), c.name(), c.city(), c.phoneNum(), c.creationDate());
    }
    // Returns CID
    public static int add(@NotNull String name, @NotNull String city, @NotNull String phoneNum, @NotNull Date creationDate)
    {
        return add(customerIdCount++, name, city, phoneNum, creationDate);
    }
    // Returns CID (can be different from the given one)
    public static int add(int cid, @NotNull String name, @NotNull String city, @NotNull String phoneNum, @NotNull Date creationDate)
    {
        if(customerRegister == null)
            customerRegister = new ArrayList<>();
        if(cid < 0) // CID must be positive
        {
            return add(0, name, city, phoneNum, creationDate);
        }
        for(Customer c : customerRegister) // Make sure the CID is unique
            if(c.cid() == cid)
            {
                return add(cid + 1, name, city, phoneNum, creationDate);
            }
        customerRegister.add(new Customer(cid, creationDate, name, phoneNum, city));
        return cid;
    }

    public static void remove(int cid)
    {
        if(customerRegister == null)
            customerRegister = new ArrayList<>();

        for(Customer c : customerRegister)
            if(c.cid() == cid)
            {
                Trash.pushCustomer(c);
                customerRegister.remove(c);
                break;
            }
        for(Order o : OrderRegister.get())
            if(o.cid() == cid)
                OrderRegister.remove(o.oid());
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
    @NotNull
    public static List<String> allCities()
    {
        List<String> cities = new ArrayList<>();
        CUSTOMER_LOOP:
        for(Customer c : customerRegister)
        {
            for(String city : cities)
                if(city.equalsIgnoreCase(c.city()))
                    continue CUSTOMER_LOOP;
            cities.add(c.city());
        }
        return cities;
    }

    public static int size()
    {
        return get().size();
    }
    public static void clear()
    {
        if(customerRegister == null)
            customerRegister = new ArrayList<>();
        customerRegister.clear();
    }
}
