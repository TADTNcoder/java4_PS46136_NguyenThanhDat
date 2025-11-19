package Dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class XJPA {

    private static EntityManagerFactory factory;

    static {
        try {
            factory = Persistence.createEntityManagerFactory("PolyOE");
            System.out.println(">>> JPA loaded successfully!");
        } catch (Exception e) {
            System.err.println(">>> ERROR loading JPA EntityManagerFactory");
            e.printStackTrace();
            throw e; // giữ cho servlet biết lỗi đúng
        }
    }

    public static EntityManager getEntityManager() {
        return factory.createEntityManager();
    }
}
