package dao;

import model.User;
import java.util.List;

public interface UserDAO {
    List<User> findAll();
    User findById(String id);
    User findByEmail(String email);
    boolean insert(User user);
    boolean update(User user);
    boolean delete(String id);
    boolean updatePassword(String id, String newPassword);
    User login(String id, String password);
    List<User> searchByName(String keyword); // ✅ thêm chức năng tìm kiếm
}
