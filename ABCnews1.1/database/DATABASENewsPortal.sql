-- Tạo database
USE master;
GO
IF DB_ID('NewsPortal') IS NOT NULL
    DROP DATABASE NewsPortal;
GO
CREATE DATABASE NewsPortal;
GO

USE NewsPortal;
GO

-- ================= USERS =================
CREATE TABLE USERS (
    Id NVARCHAR(50) PRIMARY KEY,         -- Tên đăng nhập (username)
    Password NVARCHAR(100) NOT NULL,     -- Mật khẩu (nên hash trước khi lưu)
    Fullname NVARCHAR(100) NOT NULL,     -- Họ và tên
    Birthday DATE NULL,                  -- Ngày sinh
    Gender BIT NULL,                     -- 1 = Nam, 0 = Nữ
    Mobile NVARCHAR(20) NULL,            -- Số điện thoại
    Email NVARCHAR(100) NULL,            -- Email người dùng
    Role TINYINT NOT NULL                -- 0 = Admin, 1 = Tác giả, 2 = Độc giả
);
GO

-- ================= CATEGORIES =================
CREATE TABLE CATEGORIES (
    Id NVARCHAR(50) PRIMARY KEY,
    Name NVARCHAR(100) NOT NULL
);
GO

-- ================= NEWS =================
CREATE TABLE NEWS (
    Id NVARCHAR(50) PRIMARY KEY,
    Title NVARCHAR(200) NOT NULL,
    Content NVARCHAR(MAX) NOT NULL,
    Image NVARCHAR(255),                 
    PostedDate DATE NOT NULL,
    Author NVARCHAR(50) NOT NULL,
    ViewCount INT DEFAULT 0,
    CategoryId NVARCHAR(50) NOT NULL,
    Home BIT DEFAULT 0,                  

    CONSTRAINT FK_NEWS_AUTHOR FOREIGN KEY (Author) REFERENCES USERS(Id),
    CONSTRAINT FK_NEWS_CATEGORY FOREIGN KEY (CategoryId) REFERENCES CATEGORIES(Id)
);
GO

-- ================= NEWSLETTERS =================
DROP TABLE IF EXISTS NEWSLETTERS;
GO
CREATE TABLE NEWSLETTERS (
    Email NVARCHAR(100) PRIMARY KEY,       -- Email người đăng ký
    Enabled BIT DEFAULT 1,                 -- 1 = đang theo dõi, 0 = đã hủy
    Author_Id NVARCHAR(50) NULL,           -- ID tác giả mà người này theo dõi
    Subscribed_Date DATETIME DEFAULT GETDATE(),

    CONSTRAINT FK_NEWSLETTER_AUTHOR FOREIGN KEY (Author_Id) REFERENCES USERS(Id)
);
GO

-- ================= DỮ LIỆU MẪU =================

-- 👤 USERS
INSERT INTO USERS (Id, Password, Fullname, Birthday, Gender, Mobile, Email, Role) VALUES
('admin01', '123456', N'Nguyễn Văn Admin', '1985-03-15', 1, '0905123456', 'admin@newsportal.vn', 0),
('reporter01', '123456', N'Lê Thị Phóng Viên', '1990-07-22', 0, '0912345678', 'reporter1@newsportal.vn', 1),
('reporter02', '123456', N'Trần Minh Tâm', '1993-11-05', 1, '0987654321', 'reporter2@newsportal.vn', 1),
('subscriber01', '123456', N'Nguyễn Thị Đọc Giả', '2000-01-01', 0, '0909123456', 'user1@example.com', 2);

-- 📂 CATEGORIES
INSERT INTO CATEGORIES (Id, Name) VALUES
('politics', N'Chính trị'),
('tech', N'Công nghệ'),
('sports', N'Thể thao'),
('entertainment', N'Giải trí');

-- 📰 NEWS
INSERT INTO NEWS (Id, Title, Content, Image, PostedDate, Author, ViewCount, CategoryId, Home) VALUES
('news001', N'Thủ tướng phát biểu tại hội nghị quốc tế', N'Nội dung chi tiết...', 'thutuong.jpg', '2025-10-10', 'reporter01', 120, 'politics', 1),
('news002', N'Apple ra mắt iPhone mới với AI đột phá', N'Apple vừa công bố...', 'iphone.jpg', '2025-10-11', 'reporter02', 300, 'tech', 1),
('news003', N'Việt Nam thắng đậm trong trận giao hữu', N'Đội tuyển Việt Nam...', 'vietnam_win.jpg', '2025-10-12', 'reporter01', 450, 'sports', 1),
('news004', N'Bom tấn điện ảnh tháng 10 chính thức khởi chiếu', N'Bộ phim hành động...', 'movie.jpg', '2025-10-13', 'reporter02', 250, 'entertainment', 0),
('news005', N'Chính phủ thông qua dự án luật mới', N'Chi tiết về dự án luật...', 'law.jpg', '2025-10-14', 'reporter01', 150, 'politics', 1),
('news006', N'Hội nghị ASEAN 2025 diễn ra tại Hà Nội', N'Sự kiện ngoại giao...', 'asean2025.jpg', '2025-10-15', 'reporter01', 200, 'politics', 0),
('news007', N'Google công bố hệ điều hành mới', N'Hệ điều hành mới...', 'google_os.jpg', '2025-10-14', 'reporter02', 320, 'tech', 1),
('news008', N'AI vượt trội giúp y học chẩn đoán sớm bệnh ung thư', N'Công nghệ trí tuệ...', 'ai_health.jpg', '2025-10-15', 'reporter02', 410, 'tech', 1),
('news009', N'Startup Việt gọi vốn thành công 10 triệu USD', N'Một startup công nghệ...', 'startup.jpg', '2025-10-16', 'reporter01', 500, 'tech', 0),
('news010', N'Giải bóng đá quốc gia 2025 chính thức khởi tranh', N'Giải đấu bóng đá...', 'vleague.jpg', '2025-10-14', 'reporter01', 280, 'sports', 1),
('news011', N'Huy chương vàng Olympic đầu tiên cho Việt Nam', N'Vận động viên Việt Nam...', 'olympic_gold.jpg', '2025-10-15', 'reporter02', 700, 'sports', 1),
('news012', N'Lễ trao giải âm nhạc lớn nhất năm diễn ra tại TP.HCM', N'Sự kiện quy tụ...', 'music_awards.jpg', '2025-10-14', 'reporter01', 350, 'entertainment', 1),
('news013', N'Sao Hollywood đến Việt Nam quảng bá phim mới', N'Một ngôi sao nổi tiếng...', 'hollywood.jpg', '2025-10-15', 'reporter02', 480, 'entertainment', 0),
('news014', N'Làn sóng phim Việt chiếm lĩnh phòng vé tháng 10', N'Nhiều bộ phim Việt...', 'vietnam_movies.jpg', '2025-10-16', 'reporter01', 520, 'entertainment', 1);

-- 📬 NEWSLETTERS – dữ liệu mẫu chuẩn (đầy đủ cột)
INSERT INTO NEWSLETTERS (Email, Enabled, Author_Id) VALUES
('user1@example.com', 1, 'reporter01'),
('user2@example.com', 1, 'reporter02'),
('user3@example.com', 0, 'reporter01');
GO

-- ✅ Kiểm tra dữ liệu
SELECT COUNT(*) AS TotalNews FROM NEWS;
SELECT * FROM NEWSLETTERS;
SELECT n.Email, n.Enabled, n.Author_Id, n.Subscribed_Date, u.Fullname 
FROM NEWSLETTERS n
LEFT JOIN USERS u ON n.Author_Id = u.Id;
