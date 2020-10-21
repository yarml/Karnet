package net.harmal.karnet2.core.registers;

import net.harmal.karnet2.core.Customer;
import net.harmal.karnet2.core.Date;

import java.util.ArrayList;
import java.util.List;

public class CustomerRegister
{

    public static int customerIdCount = 0;

    private static List<Customer> customerRegister;

    public static void add(String name, String city, String phoneNum, Date creationDate)
    {
        add(customerIdCount++, name, city, phoneNum, creationDate);
    }
    public static void add(int cid, String name, String city, String phoneNum, Date creationDate)
    {
        if(cid < 0) // CID must be positive
        {
            add(0, name, city, phoneNum, creationDate);
            return;
        }
        for(Customer c : customerRegister) // Make sure the CID is unique
            if(c.cid() == cid)
            {
                add(cid + 1, name, city, phoneNum, creationDate);
                return;
            }
        if(customerRegister == null)
            customerRegister = new ArrayList<Customer>();
        customerRegister.add(new Customer(cid, creationDate, name, city, phoneNum));
    }

}
