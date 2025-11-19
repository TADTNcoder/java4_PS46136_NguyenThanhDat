package Dao.impl;

import Dao.FavoriteDAO;
import Entity.Favorite;
import Util.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class FavoriteDAOImpl implements FavoriteDAO {

    @Override
    public Favorite findById(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        return em.find(Favorite.class, id);
    }

    @Override
    public List<Favorite> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        return em.createQuery(
            "SELECT f FROM Favorite f JOIN FETCH f.user JOIN FETCH f.video",
            Favorite.class
        ).getResultList();
    }

    @Override
    public List<Favorite> findByUser(String userId) {
        EntityManager em = JPAUtil.getEntityManager();
        return em.createQuery(
            "SELECT f FROM Favorite f JOIN FETCH f.user JOIN FETCH f.video WHERE f.user.id = :uid",
            Favorite.class
        ).setParameter("uid", userId).getResultList();
    }

    @Override
    public void insert(Favorite favorite) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(favorite);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        }
    }

    @Override
    public void update(Favorite favorite) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(favorite);
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
            Favorite f = em.find(Favorite.class, id);
            if (f != null) em.remove(f);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        }
    }
}