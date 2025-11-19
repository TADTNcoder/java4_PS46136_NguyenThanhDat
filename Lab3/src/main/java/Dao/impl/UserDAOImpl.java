package Dao.impl;

import Dao.UserDAO;
import Entity.User;
import Util.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class UserDAOImpl implements UserDAO {

    @Override
    public User findById(String id) {
        EntityManager em = JPAUtil.getEntityManager();
        return em.find(User.class, id);
    }

    @Override
    public List<User> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        return em.createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    @Override
    public void insert(User user) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(user);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        }
    }

    @Override
    public void update(User user) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(user);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        }
    }

    @Override
    public void delete(String id) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            User u = em.find(User.class, id);
            if (u != null) em.remove(u);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        }
    }
}