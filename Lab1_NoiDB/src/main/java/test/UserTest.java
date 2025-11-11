package test;

import dao.UserManager;

public class UserTest {
    public static void main(String[] args) {
        UserManager um = new UserManager();

        System.out.println("=== Danh sách tất cả user ===");
        um.findAll();

        System.out.println("\n=== Tìm user theo ID ===");
        um.findById("U02");

        System.out.println("\n=== Thêm mới user ===");
        um.create();

        System.out.println("\n=== Cập nhật user ===");
        um.update();

        System.out.println("\n=== Xóa user ===");
        um.deleteById();

        System.out.println("\n=== Tìm user có email @fpt.edu.vn không phải admin ===");
        um.findUserByEmailAndRole();

        System.out.println("\n=== Phân trang user (trang 1, size 5) ===");
        um.findByPage();
    }
}
