package dao;

import model.News;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface NewsDAO {

    // ===== CRUD cơ bản =====
    List<News> findAll();
    Optional<News> findById(String id);
    boolean insert(News news);
    boolean update(News news);
    boolean delete(String id);

    // ===== Phân trang & đếm =====
    List<News> findAllPaged(int page, int size);
    long countAll();

    // ===== Người đọc =====
    List<News> findLatest(int limit);
    List<News> findRelated(String categoryId, String excludeId, int limit);
    boolean incrementViewCount(String id);

    // ===== Theo chuyên mục =====
    List<News> findByCategory(String categoryId, int page, int size);
    long countByCategory(String categoryId);

    // ===== Theo tác giả (Editor/Admin) =====
    List<News> findByAuthor(String authorId, int page, int size);
    long countByAuthor(String authorId);

    List<News> findByAuthorAndCategory(String authorId, String categoryId, int page, int size);
    long countByAuthorAndCategory(String authorId, String categoryId);

    // ===== Tìm kiếm =====
    List<News> searchByKeyword(String keyword, int page, int size);
    long countSearchByKeyword(String keyword);

    List<News> searchByTitleOrAuthor(String keyword, int page, int size);
    long countSearchByTitleOrAuthor(String keyword);

    // ===== Lọc nâng cao (dành cho admin dashboard) =====
    List<News> filterNews(String title, String authorId, String categoryId, Boolean home,
                          Date fromDate, Date toDate, Integer minViews, Integer maxViews,
                          int page, int size);

    long countFilterNews(String title, String authorId, String categoryId, Boolean home,
                         Date fromDate, Date toDate, Integer minViews, Integer maxViews);

    // ===== Kiểm tra quyền =====
    boolean isAuthorOf(String newsId, String authorId);

    // (Tuỳ chọn) Dịch vụ newsletter cần gửi mail hàng loạt
    // Có thể tách sang NewsletterDAO nếu bạn muốn
    List<String> getAllSubscriberEmails();
}
