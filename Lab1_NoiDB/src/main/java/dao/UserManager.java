package dao;

import entity.User;
import javax.persistence.*;
import java.util.List;

public class UserManager {

    EntityManagerFactory factory = Persistence.createEntityManagerFactory("PolyOE");
    EntityManager em = factory.createEntityManager();

    public void findAll() {
        String jpql = "SELECT o FROM User o";
        TypedQuery<User> query = em.createQuery(jpql, User.class);
        List<User> list = query.getResultList();
        list.forEach(u -> System.out.println(u.getFullname() + " - Admin: " + u.getAdmin()));
    }

    public void findById(String userId) {
        User user = em.find(User.class, userId);
        if (user != null)
            System.out.println(user.getFullname() + " - " + user.getEmail());
    }

    public void create() {
        User user = new User("U11", "abc", "new@fpt.edu.vn", "Nguyễn Thị Mới", false);
        try {
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        }
    }

    public void update() {
        User user = em.find(User.class, "U01");
        user.setFullname("Nguyễn Văn Tèo");
        user.setEmail("teonv@gmail.com");
        try {
            em.getTransaction().begin();
            em.merge(user);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteById() {
        User user = em.find(User.class, "U04");
        try {
            em.getTransaction().begin();
            em.remove(user);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Bài 3: Tìm user theo email @fpt.edu.vn và role != admin
    public void findUserByEmailAndRole() {
        String jpql = "SELECT o FROM User o WHERE o.email LIKE :search AND o.admin = :role";
        TypedQuery<User> query = em.createQuery(jpql, User.class);
        query.setParameter("search", "%@fpt.edu.vn");
        query.setParameter("role", false);
        List<User> list = query.getResultList();
        list.forEach(u -> System.out.println(u.getFullname() + " - " + u.getEmail()));
    }

    // Bài 4: Phân trang
    public void findByPage() {
        int pageNumber = 0; // Trang thứ 3 (bắt đầu từ 0)
        int pageSize = 5;

        String jpql = "SELECT o FROM User o";
        TypedQuery<User> query = em.createQuery(jpql, User.class);
        query.setFirstResult(pageNumber * pageSize);
        query.setMaxResults(pageSize);

        List<User> list = query.getResultList();
        list.forEach(u -> System.out.println(u.getId() + " - " + u.getFullname()));
    }
}
