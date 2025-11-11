package dao.daoimpl;

import dao.NewsletterDAO;
import model.Newsletter;
import model.NewsletterView;
import utils.JdbcUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NewsletterDAOImpl implements NewsletterDAO {

    @Override
    public List<Newsletter> findAll() {
        List<Newsletter> list = new ArrayList<>();
        String sql = "SELECT * FROM NEWSLETTERS";
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToNewsletter(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Newsletter findById(String email) {
        return findByEmail(email);
    }

    @Override
    public boolean insert(Newsletter newsletter) {
        String sql = "INSERT INTO NEWSLETTERS (Email, Enabled, Author_Id, Subscribed_Date) VALUES (?, ?, ?, GETDATE())";
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newsletter.getEmail());
            ps.setBoolean(2, newsletter.isEnabled());
            ps.setString(3, newsletter.getAuthorId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(Newsletter newsletter) {
        String sql = "UPDATE NEWSLETTERS SET Enabled = ?, Author_Id = ? WHERE Email = ?";
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, newsletter.isEnabled());
            ps.setString(2, newsletter.getAuthorId());
            ps.setString(3, newsletter.getEmail());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(String email) {
        String sql = "DELETE FROM NEWSLETTERS WHERE Email = ?";
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ✅ Hủy theo dõi (toàn bộ hoặc sau này có thể mở rộng thêm authorId)
    @Override
    public boolean unsubscribe(String email) {
        String sql = "UPDATE NEWSLETTERS SET Enabled = 0 WHERE Email = ?";
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ✅ Theo dõi lại toàn bộ
    @Override
    public boolean subscribe(String email) {
        String sql = "UPDATE NEWSLETTERS SET Enabled = 1 WHERE Email = ?";
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ✅ Hàm tiện ích bật/tắt đăng ký theo dõi cho 1 tác giả
    public boolean toggleSubscription(String email, String authorId, boolean enabled) {
        String sql = "UPDATE NEWSLETTERS SET Enabled = ? WHERE Email = ? AND Author_Id = ?";
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, enabled);
            ps.setString(2, email);
            ps.setString(3, authorId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ✅ Kiểm tra email này có theo dõi tác giả hay chưa
    public boolean isSubscribed(String email, String authorId) {
        String sql = "SELECT Enabled FROM NEWSLETTERS WHERE Email = ? AND Author_Id = ?";
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, authorId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean("Enabled");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ✅ Tìm bản ghi cụ thể theo email + author
    public Newsletter findByEmailAndAuthor(String email, String authorId) {
        String sql = "SELECT * FROM NEWSLETTERS WHERE Email = ? AND Author_Id = ?";
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, authorId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToNewsletter(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ✅ Tạo mới hoặc bật lại theo dõi
    public boolean subscribeOrReactivate(String email, String authorId) {
        Newsletter existing = findByEmailAndAuthor(email, authorId);
        if (existing != null) {
            return toggleSubscription(email, authorId, true);
        } else {
            Newsletter n = new Newsletter(email, true, authorId, new Timestamp(System.currentTimeMillis()));
            return insert(n);
        }
    }

    // ✅ Lấy tất cả email theo dõi 1 tác giả (gửi thông báo khi có bài mới)
    public List<String> findEmailsByAuthor(String authorId) {
        List<String> emails = new ArrayList<>();
        String sql = "SELECT Email FROM NEWSLETTERS WHERE Author_Id = ? AND Enabled = 1";
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, authorId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    emails.add(rs.getString("Email"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return emails;
    }

    @Override
    public Newsletter findByEmail(String email) {
        if (email == null || email.isBlank()) return null;
        String sql = "SELECT * FROM NEWSLETTERS WHERE Email = ?";
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToNewsletter(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ✍️ Editor – danh sách người theo dõi theo tác giả
    public List<Newsletter> findFollowersByAuthor(String authorId) {
        List<Newsletter> followers = new ArrayList<>();
        String sql = "SELECT * FROM NEWSLETTERS WHERE Author_Id = ? AND Enabled = 1";
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, authorId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    followers.add(mapResultSetToNewsletter(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return followers;
    }

    // 📜 Admin – danh sách tất cả đăng ký + tên tác giả
    public List<NewsletterView> findAllWithAuthor() {
        List<NewsletterView> list = new ArrayList<>();
        String sql = "SELECT n.Email, n.Enabled, n.Subscribed_Date, u.Id AS authorId, u.Fullname " +
                "FROM NEWSLETTERS n " +
                "LEFT JOIN USERS u ON n.Author_Id = u.Id";
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                NewsletterView view = new NewsletterView();
                view.setEmail(rs.getString("Email"));
                view.setEnabled(rs.getBoolean("Enabled"));
                view.setSubscribedDate(rs.getTimestamp("Subscribed_Date"));
                view.setAuthorId(rs.getString("authorId"));
                view.setAuthorName(rs.getString("Fullname"));
                list.add(view);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 📜 Admin – lọc theo tác giả
    public List<NewsletterView> findAllWithAuthor(String authorId) {
        List<NewsletterView> list = new ArrayList<>();
        String sql = "SELECT n.Email, n.Enabled, n.Subscribed_Date, u.Id AS authorId, u.Fullname " +
                "FROM NEWSLETTERS n " +
                "LEFT JOIN USERS u ON n.Author_Id = u.Id " +
                "WHERE n.Author_Id = ?";
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, authorId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    NewsletterView view = new NewsletterView();
                    view.setEmail(rs.getString("Email"));
                    view.setEnabled(rs.getBoolean("Enabled"));
                    view.setSubscribedDate(rs.getTimestamp("Subscribed_Date"));
                    view.setAuthorId(rs.getString("authorId"));
                    view.setAuthorName(rs.getString("Fullname"));
                    list.add(view);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 📚 Subscriber – danh sách tác giả email này đang theo dõi
    public List<NewsletterView> findFollowedAuthorsByEmail(String email) {
        List<NewsletterView> list = new ArrayList<>();
        String sql = "SELECT n.Email, n.Enabled, n.Subscribed_Date, u.Id AS authorId, u.Fullname " +
                "FROM NEWSLETTERS n " +
                "LEFT JOIN USERS u ON n.Author_Id = u.Id " +
                "WHERE n.Email = ? AND n.Enabled = 1";
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    NewsletterView view = new NewsletterView();
                    view.setEmail(rs.getString("Email"));
                    view.setEnabled(rs.getBoolean("Enabled"));
                    view.setSubscribedDate(rs.getTimestamp("Subscribed_Date"));
                    view.setAuthorId(rs.getString("authorId"));
                    view.setAuthorName(rs.getString("Fullname"));
                    list.add(view);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 🧰 Hàm tiện ích chung
    private Newsletter mapResultSetToNewsletter(ResultSet rs) throws SQLException {
        Newsletter n = new Newsletter();
        n.setEmail(rs.getString("Email"));
        n.setEnabled(rs.getBoolean("Enabled"));
        n.setAuthorId(rs.getString("Author_Id"));
        n.setSubscribedDate(rs.getTimestamp("Subscribed_Date"));
        return n;
    }
}
