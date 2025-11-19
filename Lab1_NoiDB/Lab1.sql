USE master
GO
CREATE DATABASE PolyOE
GO
USE PolyOE
go
CREATE TABLE USERS(
	ID NVARCHAR(20) NOT NULL,
	PASSWORD NVARCHAR(50) NOT NULL,
	FULLNAME NVARCHAR(50) NOT NULL,
	EMAIL NVARCHAR(50) NOT NULL,
	ADMIN BIT NOT NULL,
	PRIMARY KEY (ID)
)
INSERT INTO USERS (ID, PASSWORD, FULLNAME, EMAIL, ADMIN)
VALUES
('admin01', '123456', N'Nguyễn Văn A', 'admin01@example.com', 1),
('user01', 'user123', N'Trần Thị B', 'tranthib@example.com', 0),
('user02', 'pass456', N'Lê Hoàng C', 'lehoangc@example.com', 0),
('user03', 'matkhau789', N'Phạm Duy D', 'phamduyd@example.com', 0),
('mod01', 'mod123', N'Võ Minh E', 'vominhe@example.com', 1);
INSERT INTO Users VALUES
('U01', '123', 'Nguyễn Văn Tèo', 'teo@gmail.com', 0),
('U02', '456', 'Trần Thị Bưởi', 'buoi@fpt.edu.vn', 0),
('U03', '789', 'Lê Văn Admin', 'admin@fpt.edu.vn', 1);

