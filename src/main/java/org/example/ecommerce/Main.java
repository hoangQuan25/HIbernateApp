package org.example.ecommerce;

import org.example.ecommerce.model.*;
import org.example.ecommerce.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.HashSet;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            // Create Address
            Address address = new Address();
            address.setStreet("123 Main St");
            address.setCity("Springfield");
            address.setCountry("USA");

            // Create User
            User user = new User();
            user.setName("John Doe");
            user.setAddress(address);

            // Create Products
            Product product1 = new Product();
            product1.setName("Product 1");
            product1.setPrice(10.0);

            Product product2 = new Product();
            product2.setName("Product 2");
            product2.setPrice(20.0);

            // Save Products first
            session.save(product1);
            session.save(product2);

            Set<Product> products = new HashSet<>();
            products.add(product1);
            products.add(product2);

            // Create Order
            Order order = new Order();
            order.setUser(user);
            order.setProducts(products);

            // Add Order to User
            user.getOrders().add(order);

            // Save User (which will cascade and save address, orders, and products)
            session.save(user);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }

        HibernateUtil.shutdown();
    }
}
