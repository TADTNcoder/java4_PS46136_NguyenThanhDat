package Dao.impl;

import Dao.VideoDAO;
import Entity.Video;
import Util.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class VideoDAOImpl implements VideoDAO {

    @Override
    public Video findById(String id) {
        EntityManager em = JPAUtil.getEntityManager();
        return em.find(Video.class, id);
    }

    @Override
    public List<Video> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        return em.createQuery("SELECT v FROM Video v", Video.class).getResultList();
    }

    @Override
    public void insert(Video video) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(video);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        }
    }

    @Override
    public void update(Video video) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(video);
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
            Video v = em.find(Video.class, id);
            if (v != null) em.remove(v);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        }
    }
}