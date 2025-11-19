package Dao.impl;

import Dao.ShareDAO;
import Entity.Share;
import Util.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class ShareDAOImpl implements ShareDAO {

    @Override
    public Share findById(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        return em.find(Share.class, id);
    }

    @Override
    public List<Share> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        return em.createQuery(
            "SELECT s FROM Share s JOIN FETCH s.user JOIN FETCH s.video",
            Share.class
        ).getResultList();
    }

    @Override
    public List<Share> findByUser(String userId) {
        EntityManager em = JPAUtil.getEntityManager();
        return em.createQuery(
            "SELECT s FROM Share s JOIN FETCH s.user JOIN FETCH s.video WHERE s.user.id = :uid",
            Share.class
        ).setParameter("uid", userId).getResultList();
    }

    @Override
    public void insert(Share share) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(share);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        }
    }

    @Override
    public void update(Share share) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(share);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        }
    }

    @Override
    public void delete(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Share s = em.find(Share.class, id);
            if (s != null) em.remove(s);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        }
    }
}