package dao;

import model.Newsletter;
import java.util.List;

public interface NewsletterDAO {

    // 📌 CRUD cơ bản
    List<Newsletter> findAll();
    Newsletter findById(String email);
    boolean insert(Newsletter newsletter);
    boolean update(Newsletter newsletter);
    boolean delete(String email);

    // 📩 Đăng ký / hủy đăng ký nhận tin
    boolean subscribe(String email);     // kích hoạt lại đăng ký
    boolean unsubscribe(String email);   // hủy đăng ký (Enabled = false)

    // 📬 Kiểm tra thông tin đăng ký
    Newsletter findByEmail(String email);
}
