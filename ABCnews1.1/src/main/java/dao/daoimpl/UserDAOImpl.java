package dao.daoimpl;

import dao.UserDAO;
import model.User;
import utils.JdbcUtils;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO {

    @Override
    public List<User> findAll() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM USERS ORDER BY Fullname ASC";
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public User findById(String id) {
        String sql = "SELECT * FROM USERS WHERE Id = ?";
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User findByEmail(String email) {
        String sql = "SELECT * FROM USERS WHERE Email = ?";
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean insert(User user) {
        String sql = "INSERT INTO USERS(Id, Password, Fullname, Birthday, Gender, Mobile, Email, Role) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getId());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getFullname());

            if (user.getBirthday() != null) {
                ps.setDate(4, new java.sql.Date(user.getBirthday().getTime()));
            } else {
                ps.setNull(4, Types.DATE);
            }

            ps.setBoolean(5, user.isGender());
            ps.setString(6, user.getMobile());
            ps.setString(7, user.getEmail());
            ps.setInt(8, user.getRole());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(User user) {
        String sql = "UPDATE USERS SET Password=?, Fullname=?, Birthday=?, Gender=?, Mobile=?, Email=?, Role=? WHERE Id=?";
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getPassword());
            ps.setString(2, user.getFullname());

            if (user.getBirthday() != null) {
                ps.setDate(3, new java.sql.Date(user.getBirthday().getTime()));
            } else {
                ps.setNull(3, Types.DATE);
            }

            ps.setBoolean(4, user.isGender());
            ps.setString(5, user.getMobile());
            ps.setString(6, user.getEmail());
            ps.setInt(7, user.getRole());
            ps.setString(8, user.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(String id) {
        String sql = "DELETE FROM USERS WHERE Id = ?";
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updatePassword(String id, String newPassword) {
        String sql = "UPDATE USERS SET Password = ? WHERE Id = ?";
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newPassword);
            ps.setString(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * ✅ Đăng nhập bằng email và kiểm tra BCrypt
     */
    @Override
    public User login(String email, String rawPassword) {
        String sql = "SELECT * FROM USERS WHERE Email = ?";
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = mapResultSetToUser(rs);
                    if (BCrypt.checkpw(rawPassword, user.getPassword())) {
                        return user;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<User> searchByName(String keyword) {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM USERS WHERE Fullname LIKE ?";
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToUser(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getString("Id"));
        user.setPassword(rs.getString("Password"));
        user.setFullname(rs.getString("Fullname"));
        user.setBirthday(rs.getDate("Birthday"));
        user.setGender(rs.getBoolean("Gender"));
        user.setMobile(rs.getString("Mobile"));
        user.setEmail(rs.getString("Email"));
        user.setRole(rs.getInt("Role"));
        return user;
    }

    public List<User> findAllAuthors() throws SQLException {
        String sql = "SELECT * FROM USERS WHERE Role = 1";
        List<User> list = new ArrayList<>();
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                User u = new User();
                u.setId(rs.getString("Id"));
                u.setFullname(rs.getString("Fullname"));
                list.add(u);
            }
        }
        return list;
    }
}
