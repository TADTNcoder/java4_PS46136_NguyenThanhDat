package dao.daoimpl;

import dao.NewsDAO;
import model.News;
import utils.JdbcUtils;

import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NewsDAOImpl implements NewsDAO {

    private static final Logger LOGGER = Logger.getLogger(NewsDAOImpl.class.getName());

    // ===== CRUD cơ bản =====

    @Override
    public List<News> findAll() {
        String sql = "SELECT Id, Title, Content, Image, PostedDate, Author, ViewCount, CategoryId, Home " +
                     "FROM NEWS ORDER BY PostedDate DESC";
        List<News> list = new ArrayList<>();
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "findAll failed", e);
        }
        return list;
    }

    @Override
    public Optional<News> findById(String id) {
        String sql = "SELECT Id, Title, Content, Image, PostedDate, Author, ViewCount, CategoryId, Home " +
                     "FROM NEWS WHERE Id = ?";
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(map(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "findById failed: " + id, e);
        }
        return Optional.empty();
    }

    @Override
    public boolean insert(News news) {
        String sql = "INSERT INTO NEWS (Id, Title, Content, Image, PostedDate, Author, ViewCount, CategoryId, Home) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            bindUpsert(ps, news, true);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "insert failed: " + news.getId(), e);
            return false;
        }
    }

    @Override
    public boolean update(News news) {
        String sql = "UPDATE NEWS SET Title=?, Content=?, Image=?, PostedDate=?, Author=?, ViewCount=?, CategoryId=?, Home=? " +
                     "WHERE Id=?";
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            bindUpsert(ps, news, false);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "update failed: " + news.getId(), e);
            return false;
        }
    }

    @Override
    public boolean delete(String id) {
        String sql = "DELETE FROM NEWS WHERE Id = ?";
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "delete failed: " + id, e);
            return false;
        }
    }

    // ===== Phân trang & đếm =====

    @Override
    public List<News> findAllPaged(int page, int size) {
        int offset = Math.max(0, (page - 1) * size);
        String sql = "SELECT Id, Title, Content, Image, PostedDate, Author, ViewCount, CategoryId, Home " +
                     "FROM NEWS ORDER BY PostedDate DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        List<News> list = new ArrayList<>();
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, offset);
            ps.setInt(2, size);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "findAllPaged failed", e);
        }
        return list;
    }

    @Override
    public long countAll() {
        String sql = "SELECT COUNT(*) FROM NEWS";
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getLong(1);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "countAll failed", e);
        }
        return 0;
    }

    // ===== Người đọc =====

    @Override
    public List<News> findLatest(int limit) {
        String sql = "SELECT Id, Title, Content, Image, PostedDate, Author, ViewCount, CategoryId, Home " +
                     "FROM NEWS ORDER BY PostedDate DESC OFFSET 0 ROWS FETCH NEXT ? ROWS ONLY";
        List<News> list = new ArrayList<>();
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "findLatest failed", e);
        }
        return list;
    }

    @Override
    public List<News> findRelated(String categoryId, String excludeId, int limit) {
        String sql = "SELECT Id, Title, Content, Image, PostedDate, Author, ViewCount, CategoryId, Home " +
                     "FROM NEWS WHERE CategoryId = ? AND Id <> ? " +
                     "ORDER BY PostedDate DESC OFFSET 0 ROWS FETCH NEXT ? ROWS ONLY";
        List<News> list = new ArrayList<>();
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, categoryId);
            ps.setString(2, excludeId);
            ps.setInt(3, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "findRelated failed", e);
        }
        return list;
    }

    @Override
    public boolean incrementViewCount(String id) {
        String sql = "UPDATE NEWS SET ViewCount = ViewCount + 1 WHERE Id = ?";
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "incrementViewCount failed: " + id, e);
            return false;
        }
    }

    // ===== Theo chuyên mục =====

    @Override
    public List<News> findByCategory(String categoryId, int page, int size) {
        int offset = Math.max(0, (page - 1) * size);
        String sql = "SELECT Id, Title, Content, Image, PostedDate, Author, ViewCount, CategoryId, Home " +
                     "FROM NEWS WHERE CategoryId = ? " +
                     "ORDER BY PostedDate DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        List<News> list = new ArrayList<>();
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, categoryId);
            ps.setInt(2, offset);
            ps.setInt(3, size);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "findByCategory failed", e);
        }
        return list;
    }

    @Override
    public long countByCategory(String categoryId) {
        String sql = "SELECT COUNT(*) FROM NEWS WHERE CategoryId = ?";
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, categoryId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getLong(1);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "countByCategory failed", e);
        }
        return 0;
    }

    // ===== Theo tác giả =====

    @Override
    public List<News> findByAuthor(String authorId, int page, int size) {
        int offset = Math.max(0, (page - 1) * size);
        String sql = "SELECT Id, Title, Content, Image, PostedDate, Author, ViewCount, CategoryId, Home " +
                     "FROM NEWS WHERE Author = ? " +
                     "ORDER BY PostedDate DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        List<News> list = new ArrayList<>();
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, authorId);
            ps.setInt(2, offset);
            ps.setInt(3, size);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "findByAuthor failed", e);
        }
        return list;
    }

    @Override
    public long countByAuthor(String authorId) {
        String sql = "SELECT COUNT(*) FROM NEWS WHERE Author = ?";
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, authorId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getLong(1);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "countByAuthor failed", e);
        }
        return 0;
    }

    @Override
    public List<News> findByAuthorAndCategory(String authorId, String categoryId, int page, int size) {
        int offset = Math.max(0, (page - 1) * size);
        String sql = "SELECT Id, Title, Content, Image, PostedDate, Author, ViewCount, CategoryId, Home " +
                     "FROM NEWS WHERE Author = ? AND CategoryId = ? " +
                     "ORDER BY PostedDate DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        List<News> list = new ArrayList<>();
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, authorId);
            ps.setString(2, categoryId);
            ps.setInt(3, offset);
            ps.setInt(4, size);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "findByAuthorAndCategory failed", e);
        }
        return list;
    }

    @Override
    public long countByAuthorAndCategory(String authorId, String categoryId) {
        String sql = "SELECT COUNT(*) FROM NEWS WHERE Author = ? AND CategoryId = ?";
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, authorId);
            ps.setString(2, categoryId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getLong(1);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "countByAuthorAndCategory failed", e);
        }
        return 0;
    }

    // ===== Tìm kiếm =====

    @Override
    public List<News> searchByKeyword(String keyword, int page, int size) {
        int offset = Math.max(0, (page - 1) * size);
        String sql = "SELECT Id, Title, Content, Image, PostedDate, Author, ViewCount, CategoryId, Home " +
                     "FROM NEWS WHERE Title LIKE ? OR Content LIKE ? " +
                     "ORDER BY PostedDate DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        List<News> list = new ArrayList<>();
        String q = "%" + (keyword == null ? "" : keyword) + "%";
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, q);
            ps.setString(2, q);
            ps.setInt(3, offset);
            ps.setInt(4, size);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "searchByKeyword failed", e);
        }
        return list;
    }

    @Override
    public long countSearchByKeyword(String keyword) {
        String sql = "SELECT COUNT(*) FROM NEWS WHERE Title LIKE ? OR Content LIKE ?";
        String q = "%" + (keyword == null ? "" : keyword) + "%";
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, q);
            ps.setString(2, q);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getLong(1);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "countSearchByKeyword failed", e);
        }
        return 0;
    }

    @Override
    public List<News> searchByTitleOrAuthor(String keyword, int page, int size) {
        int offset = Math.max(0, (page - 1) * size);
        String sql = "SELECT n.Id, n.Title, n.Content, n.Image, n.PostedDate, n.Author, n.ViewCount, n.CategoryId, n.Home " +
                     "FROM NEWS n LEFT JOIN USERS u ON u.Id = n.Author " +
                     "WHERE n.Title LIKE ? OR u.Full_Name LIKE ? " +
                     "ORDER BY n.PostedDate DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        List<News> list = new ArrayList<>();
        String q = "%" + (keyword == null ? "" : keyword) + "%";
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, q);
            ps.setString(2, q);
            ps.setInt(3, offset);
            ps.setInt(4, size);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "searchByTitleOrAuthor failed", e);
        }
        return list;
    }

    @Override
    public long countSearchByTitleOrAuthor(String keyword) {
        String sql = "SELECT COUNT(*) " +
                     "FROM NEWS n LEFT JOIN USERS u ON u.Id = n.Author " +
                     "WHERE n.Title LIKE ? OR u.Full_Name LIKE ?";
        String q = "%" + (keyword == null ? "" : keyword) + "%";
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, q);
            ps.setString(2, q);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getLong(1);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "countSearchByTitleOrAuthor failed", e);
        }
        return 0;
    }

    // ===== Lọc nâng cao (admin) =====

    @Override
    public List<News> filterNews(String title, String authorId, String categoryId, Boolean home,
                                 Date fromDate, Date toDate, Integer minViews, Integer maxViews,
                                 int page, int size) {
        int offset = Math.max(0, (page - 1) * size);

        StringBuilder sql = new StringBuilder(
            "SELECT Id, Title, Content, Image, PostedDate, Author, ViewCount, CategoryId, Home FROM NEWS WHERE 1=1"
        );
        List<Object> params = new ArrayList<>();
        buildFilterWhere(sql, params, title, authorId, categoryId, home, fromDate, toDate, minViews, maxViews);
        sql.append(" ORDER BY PostedDate DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
        params.add(offset);
        params.add(size);

        List<News> list = new ArrayList<>();
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement ps = prepare(conn, sql.toString(), params);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "filterNews failed", e);
        }
        return list;
    }

    @Override
    public long countFilterNews(String title, String authorId, String categoryId, Boolean home,
                                Date fromDate, Date toDate, Integer minViews, Integer maxViews) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM NEWS WHERE 1=1");
        List<Object> params = new ArrayList<>();
        buildFilterWhere(sql, params, title, authorId, categoryId, home, fromDate, toDate, minViews, maxViews);

        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement ps = prepare(conn, sql.toString(), params);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getLong(1);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "countFilterNews failed", e);
        }
        return 0;
    }

    private void buildFilterWhere(StringBuilder sql, List<Object> params,
                                  String title, String authorId, String categoryId, Boolean home,
                                  Date fromDate, Date toDate, Integer minViews, Integer maxViews) {
        if (title != null && !title.isEmpty()) {
            sql.append(" AND Title LIKE ?");
            params.add("%" + title + "%");
        }
        if (authorId != null && !authorId.isEmpty()) {
            sql.append(" AND Author = ?");
            params.add(authorId);
        }
        if (categoryId != null && !categoryId.isEmpty()) {
            sql.append(" AND CategoryId = ?");
            params.add(categoryId);
        }
        if (home != null) {
            sql.append(" AND Home = ?");
            params.add(home);
        }
        if (fromDate != null) {
            sql.append(" AND PostedDate >= ?");
            params.add(new Timestamp(fromDate.getTime()));
        }
        if (toDate != null) {
            sql.append(" AND PostedDate <= ?");
            params.add(new Timestamp(toDate.getTime()));
        }
        if (minViews != null) {
            sql.append(" AND ViewCount >= ?");
            params.add(minViews);
        }
        if (maxViews != null) {
            sql.append(" AND ViewCount <= ?");
            params.add(maxViews);
        }
    }

    // ===== Kiểm tra quyền =====

    @Override
    public boolean isAuthorOf(String newsId, String authorId) {
        String sql = "SELECT COUNT(*) FROM NEWS WHERE Id = ? AND Author = ?";
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newsId);
            ps.setString(2, authorId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "isAuthorOf failed", e);
        }
        return false;
    }

    // ===== Newsletter support (tuỳ chọn) =====

    @Override
    public List<String> getAllSubscriberEmails() {
        // Bảng NEWSLETTERS(Email, AuthorId, Enabled) — lấy toàn bộ email Enabled=1 (distinct)
        String sql = "SELECT DISTINCT Email FROM NEWSLETTERS WHERE Enabled = 1";
        List<String> emails = new ArrayList<>();
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) emails.add(rs.getString(1));
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "getAllSubscriberEmails failed", e);
        }
        return emails;
    }

    // ===== Helpers =====

    private void bindUpsert(PreparedStatement ps, News n, boolean insert) throws SQLException {
        // Insert: 1..9, Update: 1..8 then 9=Id
        ps.setString(1, n.getId());
        ps.setString(2, n.getTitle());
        ps.setString(3, n.getContent());
        ps.setString(4, n.getImage());
        // Ưu tiên Timestamp để không mất time component
        if (n.getPostedDate() != null) {
            ps.setTimestamp(5, new Timestamp(n.getPostedDate().getTime()));
        } else {
            ps.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
        }
        ps.setString(6, n.getAuthor());
        ps.setInt(7, n.getViewCount());
        ps.setString(8, n.getCategoryId());
        ps.setBoolean(9, n.isHome());

        if (!insert) {
            // với UPDATE: dịch chuyển tham số, nên set lại theo thứ tự của câu UPDATE
            // đã viết câu UPDATE theo thứ tự fields, nên ràng buộc lại:
            ps.clearParameters();
            ps.setString(1, n.getTitle());
            ps.setString(2, n.getContent());
            ps.setString(3, n.getImage());
            ps.setTimestamp(4, n.getPostedDate() != null
                    ? new Timestamp(n.getPostedDate().getTime())
                    : new Timestamp(System.currentTimeMillis()));
            ps.setString(5, n.getAuthor());
            ps.setInt(6, n.getViewCount());
            ps.setString(7, n.getCategoryId());
            ps.setBoolean(8, n.isHome());
            ps.setString(9, n.getId());
        }
    }

    private News map(ResultSet rs) throws SQLException {
        News n = new News();
        n.setId(rs.getString("Id"));
        n.setTitle(rs.getString("Title"));
        n.setContent(rs.getString("Content"));
        n.setImage(rs.getString("Image"));

        // Lấy đầy đủ time component
        Timestamp ts = rs.getTimestamp("PostedDate");
        n.setPostedDate(ts != null ? new Date(ts.getTime()) : null);

        n.setAuthor(rs.getString("Author"));
        n.setViewCount(rs.getInt("ViewCount"));
        n.setCategoryId(rs.getString("CategoryId"));
        n.setHome(rs.getBoolean("Home"));
        return n;
    }

    private PreparedStatement prepare(Connection conn, String sql, List<Object> params) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(sql);
        for (int i = 0; i < params.size(); i++) {
            Object v = params.get(i);
            int idx = i + 1;
            if (v instanceof String) ps.setString(idx, (String) v);
            else if (v instanceof Integer) ps.setInt(idx, (Integer) v);
            else if (v instanceof Long) ps.setLong(idx, (Long) v);
            else if (v instanceof Boolean) ps.setBoolean(idx, (Boolean) v);
            else if (v instanceof Timestamp) ps.setTimestamp(idx, (Timestamp) v);
            else if (v instanceof java.sql.Date) ps.setDate(idx, (java.sql.Date) v);
            else if (v instanceof java.util.Date) ps.setTimestamp(idx, new Timestamp(((Date) v).getTime()));
            else if (v == null) ps.setObject(idx, null);
            else ps.setObject(idx, v);
        }
        return ps;
    }
    
    public List<News> searchByAuthorAndKeyword(String authorId, String keyword, int page, int size) {
        int offset = Math.max(0, (page - 1) * size);
        String sql = "SELECT Id, Title, Content, Image, PostedDate, Author, ViewCount, CategoryId, Home " +
                     "FROM NEWS WHERE Author = ? AND (Title LIKE ? OR Content LIKE ?) " +
                     "ORDER BY PostedDate DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        List<News> list = new ArrayList<>();
        String q = "%" + (keyword == null ? "" : keyword) + "%";
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, authorId);
            ps.setString(2, q);
            ps.setString(3, q);
            ps.setInt(4, offset);
            ps.setInt(5, size);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "searchByAuthorAndKeyword failed", e);
        }
        return list;
    }

    public long countSearchByAuthorAndKeyword(String authorId, String keyword) {
        String sql = "SELECT COUNT(*) FROM NEWS WHERE Author = ? AND (Title LIKE ? OR Content LIKE ?)";
        String q = "%" + (keyword == null ? "" : keyword) + "%";
        try (Connection conn = JdbcUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, authorId);
            ps.setString(2, q);
            ps.setString(3, q);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getLong(1);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "countSearchByAuthorAndKeyword failed", e);
        }
        return 0;
    }

}
