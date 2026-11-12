IF DB_ID('Jolliebear_SQL') IS NULL
    CREATE DATABASE Jolliebear_SQL;
GO
USE Jolliebear_SQL;
GO

SET QUOTED_IDENTIFIER ON;
SET ANSI_NULLS ON;
GO

/* ---------- Xoá bảng cũ nếu chạy lại (đúng thứ tự) ---------- */
IF OBJECT_ID('orders','U')     IS NOT NULL DROP TABLE orders;
IF OBJECT_ID('contacts','U')   IS NOT NULL DROP TABLE contacts;
IF OBJECT_ID('jobs','U')       IS NOT NULL DROP TABLE jobs;
IF OBJECT_ID('news','U')       IS NOT NULL DROP TABLE news;
IF OBJECT_ID('stores','U')     IS NOT NULL DROP TABLE stores;
IF OBJECT_ID('menu_items','U') IS NOT NULL DROP TABLE menu_items;
IF OBJECT_ID('users','U')      IS NOT NULL DROP TABLE users;
GO

/* ---------- USERS ---------- */
CREATE TABLE users (
    id       INT IDENTITY(1,1) PRIMARY KEY,
    name     NVARCHAR(100) NOT NULL,
    username VARCHAR(50) NULL,
    phone    VARCHAR(20)  NOT NULL UNIQUE,
    email    VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,          -- lưu dạng SHA-256:salt (app tự băm)
    role     VARCHAR(20)  NOT NULL DEFAULT 'CUSTOMER'   -- CUSTOMER / STAFF / ADMIN
);
CREATE UNIQUE INDEX UX_users_username ON users(username) WHERE username IS NOT NULL;
GO

/* ---------- MENU_ITEMS ---------- */
CREATE TABLE menu_items (
    id             INT IDENTITY(1,1) PRIMARY KEY,
    name_vi        NVARCHAR(150) NOT NULL,
    name_en        NVARCHAR(150) NOT NULL,
    category       VARCHAR(50)   NOT NULL,
    price          FLOAT         NOT NULL,
    description_vi NVARCHAR(500) NULL,
    description_en NVARCHAR(500) NULL,
    image_url      VARCHAR(300)  NULL,
    is_available   BIT           NOT NULL DEFAULT 1
);
GO

/* ---------- NEWS ---------- */
CREATE TABLE news (
    id         INT IDENTITY(1,1) PRIMARY KEY,
    title      NVARCHAR(200) NOT NULL,
    summary    NVARCHAR(500) NOT NULL,
    content    NVARCHAR(MAX) NOT NULL,
    image_url  VARCHAR(200)  NULL,
    created_at DATETIME NOT NULL DEFAULT GETDATE()
);
GO

/* ---------- STORES ---------- */
CREATE TABLE stores (
    id            INT IDENTITY(1,1) PRIMARY KEY,
    name          NVARCHAR(100) NOT NULL,
    address       NVARCHAR(200) NOT NULL,
    phone         VARCHAR(20)   NULL,
    opening_hours NVARCHAR(50)  NULL DEFAULT '09:00 - 22:00',
    map_url       VARCHAR(500)  NULL
);
GO

/* ---------- JOBS ---------- */
CREATE TABLE jobs (
    id           INT IDENTITY(1,1) PRIMARY KEY,
    title        NVARCHAR(200) NOT NULL,
    department   NVARCHAR(100) NOT NULL,
    location     NVARCHAR(100) NOT NULL,
    salary       NVARCHAR(50)  NULL DEFAULT N'Thỏa thuận',
    description  NVARCHAR(MAX) NOT NULL,
    requirements NVARCHAR(MAX) NOT NULL,
    deadline     DATE NULL,
    created_at   DATETIME NOT NULL DEFAULT GETDATE()
);
GO

/* ---------- CONTACTS ---------- */
CREATE TABLE contacts (
    id         INT IDENTITY(1,1) PRIMARY KEY,
    name       NVARCHAR(100) NOT NULL,
    email      VARCHAR(100)  NOT NULL,
    phone      VARCHAR(20)   NULL,
    subject    NVARCHAR(200) NULL,
    message    NVARCHAR(MAX) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT GETDATE(),
    is_read    BIT NOT NULL DEFAULT 0
);
GO

/* ---------- ORDERS ---------- */
CREATE TABLE orders (
    id             INT IDENTITY(1,1) PRIMARY KEY,
    user_email     VARCHAR(100)  NULL,           -- liên kết tài khoản đặt đơn (users.email)
    customer_name  NVARCHAR(100) NOT NULL,
    phone          VARCHAR(20)   NOT NULL,
    address        NVARCHAR(300) NOT NULL,
    payment_method NVARCHAR(50)  NOT NULL,
    items          NVARCHAR(MAX) NOT NULL,
    total          FLOAT         NOT NULL,
    note           NVARCHAR(500) NULL,
    status         NVARCHAR(30)  NOT NULL DEFAULT N'Chờ xử lý',
    created_at     DATETIME NOT NULL DEFAULT GETDATE()
);
GO

/* =====================================================================
   DỮ LIỆU MẪU
   ===================================================================== */

/* ---------- USERS (mật khẩu app sẽ tự băm ở lần chạy đầu) ---------- */
SET IDENTITY_INSERT users OFF;
INSERT INTO users (name, username, phone, email, password, role) VALUES
(N'Admin Jolliebear', 'admin',    '0900000001', 'admin@jolliebear.com', 'admin123',    'ADMIN'),
(N'Staff Jolliebear', 'staff',    '0900000002', 'staff@jolliebear.com', 'staff123',    'STAFF'),
(N'Nguyễn Văn Bear',  'testbear', '0909123456', 'test@jolliebear.com',  'password123', 'CUSTOMER');
GO

/* ---------- MENU_ITEMS ---------- */
INSERT INTO menu_items (name_vi,name_en,category,price,description_vi,description_en,image_url,is_available) VALUES (N'Gà Giòn Vui Vẻ (1 Miếng)', N'Chickenjoy (1 Pc)', 'GaGion', 35000, N'1 Miếng Gà Giòn thơm phức, da giòn rụm bên ngoài, thịt mọng nước bên trong.', N'1 Pc Crispy Fried Chicken, crunchy outside and juicy inside.', 'https://images.unsplash.com/photo-1626645738196-c2a7c87a8f58?w=500', 1);
INSERT INTO menu_items (name_vi,name_en,category,price,description_vi,description_en,image_url,is_available) VALUES (N'Gà Sốt Cay Thượng Hạng', N'Spicy Glazed Chicken', 'GaGion', 39000, N'Gà chiên phủ sốt cay đậm đà kích thích vị giác.', N'Crispy chicken glazed with signature rich spicy sauce.', 'https://images.unsplash.com/photo-1569058242253-92a9c755a0ec?w=500', 1);
INSERT INTO menu_items (name_vi,name_en,category,price,description_vi,description_en,image_url,is_available) VALUES (N'Mỳ Ý Sốt Bò Băm Jollie Spaghetti', N'Jollie Spaghetti Beef Sauce', 'MyY', 45000, N'Mỳ Ý đượm sốt bò băm cà chua đậm đà kèm pho mát rắc thượng hạng.', N'Spaghetti topped with rich minced beef tomato sauce & cheese.', 'https://images.unsplash.com/photo-1551183053-bf91a1d81141?w=500', 1);
INSERT INTO menu_items (name_vi,name_en,category,price,description_vi,description_en,image_url,is_available) VALUES (N'Burger Gà Giòn Sốt Mayo', N'Crispy Chicken Burger Mayo', 'Burger', 42000, N'Bánh burger kẹp miếng gà chiên giòn, rau xà lách tươi và sốt mayonnaise.', N'Soft bun with crispy chicken patty, fresh lettuce & mayo.', 'https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=500', 1);
INSERT INTO menu_items (name_vi,name_en,category,price,description_vi,description_en,image_url,is_available) VALUES (N'Combo Tiết Kiệm Gà & Mỳ Ý', N'Saver Combo Chicken & Spaghetti', 'Combo', 79000, N'Bao gồm 1 Miếng Gà Giòn + 1 Dĩa Mỳ Ý + 1 Ly Pepsi mát lạnh.', N'Includes 1 Pc Chickenjoy + 1 Spaghetti + 1 Cold Pepsi.', 'https://images.unsplash.com/photo-1513104890138-7c749659a591?w=500', 1);
INSERT INTO menu_items (name_vi,name_en,category,price,description_vi,description_en,image_url,is_available) VALUES (N'Combo Gia Đình Vui Vẻ', N'Family Feast Combo', 'Combo', 199000, N'6 Miếng Gà Giòn + 2 Mỳ Ý + 1 Khoai Tây Chiên Lớn + 3 Pepsi.', N'6 Pcs Chickenjoy + 2 Spaghetti + 1 Large Fries + 3 Pepsi.', 'https://images.unsplash.com/photo-1544025162-d76694265947?w=500', 1);
INSERT INTO menu_items (name_vi,name_en,category,price,description_vi,description_en,image_url,is_available) VALUES (N'Khoai Tây Chiên Vàng Giòn (L)', N'French Fries Large', 'NuocTrangMieng', 29000, N'Khoai tây chiên vàng ươm, giòn rụm chấm kèm sốt tương cà.', N'Golden crispy french fries served with ketchup sauce.', 'https://images.unsplash.com/photo-1576107232684-1279f3908594?w=500', 1);
INSERT INTO menu_items (name_vi,name_en,category,price,description_vi,description_en,image_url,is_available) VALUES (N'Pepsi Tươi Mát Lạnh', N'Fresh Cold Pepsi', 'NuocTrangMieng', 18000, N'Nước ngọt sảng khoái giải nhiệt.', N'Refreshing cold carbonated soft drink.', 'https://images.unsplash.com/photo-1622483767028-3f66f32aef97?w=500', 1);
GO
/* ---------- NEWS ---------- */
INSERT INTO news (title,summary,content,image_url) VALUES (N'Khai Trương Chi Nhánh Quận 1 Mới', N'Jolliebear tưng bừng khai trương chi nhánh thứ 15 tại trung tâm Quận 1 với ngập tràn ưu đãi hấp dẫn.', N'Thương hiệu gà rán vui vẻ Jolliebear chính thức khai trương chi nhánh mới tại số 120 Lê Lai, Quận 1, TP. HCM. Trong tuần đầu khai trương, khách hàng ghé thăm sẽ được giảm giá 30% cho toàn bộ thực đơn và nhận ngay một mascot gấu dễ thương cho hóa đơn từ 200.000đ. Hãy đến và trải nghiệm không gian hiện đại, sang trọng của Jolliebear!', 'https://images.unsplash.com/photo-1555396273-367ea4eb4db5?w=600');
INSERT INTO news (title,summary,content,image_url) VALUES (N'Combo Gà Giòn Chào Hè Tiết Kiệm', N'Giải nhiệt mùa hè cùng Combo Gà Giòn Vui Vẻ chỉ từ 59.000đ đi kèm ly nước ngọt siêu to khổng lồ.', N'Chào hè rực rỡ, Jolliebear ra mắt bộ sưu tập Combo Chào Hè với giá cực kỳ tiết kiệm. Combo bao gồm 2 miếng Gà Giòn Vui Vẻ giòn rụm bên ngoài mọng nước bên trong, 1 phần khoai tây chiên vàng giòn và 1 ly Pepsi mát lạnh. Ưu đãi kéo dài từ nay đến hết ngày 31/08/2026 trên toàn bộ hệ thống cửa hàng và app đặt món.', 'https://images.unsplash.com/photo-1569058242253-92a9c755a0ec?w=600');
INSERT INTO news (title,summary,content,image_url) VALUES (N'Hành Trình Jolliebear Vì Cộng Đồng 2026', N'Jolliebear Việt Nam đã trao tặng hơn 5.000 suất ăn ấm lòng cho trẻ em nghèo hiếu học vùng cao.', N'Nằm trong chuỗi hoạt động trách nhiệm xã hội thường niên, chương trình "Hành Trình Chia Sẻ Niềm Vui" của Jolliebear đã dừng chân tại các tỉnh miền núi phía Bắc. Đội ngũ nhân viên Jolliebear đã tự tay chuẩn bị và trao tặng hàng ngàn suất ăn dinh dưỡng cùng nhiều phần quà học tập ý nghĩa, thắp sáng ước mơ đến trường cho trẻ em hiếu học.', 'https://images.unsplash.com/photo-1488521787991-ed7bbaae773c?w=600');
GO
/* ---------- STORES ---------- */
INSERT INTO stores (name,address,phone,opening_hours,map_url) VALUES (N'Jolliebear Bến Thành', N'120 Lê Lai, Phường Bến Thành, Quận 1, TP. Hồ Chí Minh', '02838241234', N'09:00 - 22:30', 'https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3919.4853036662493!2d106.69248437590487!3d10.774092559231604!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x31752f3f501062b1%3A0x67394b9f02931102!2zMTIwIEzDqiBMYWksIELhur9uIFRow6BuaCwgUXXhuq1uIDEsIEjhu5MgQ2jDrSBNaW5oLCBWaeG7h3QgTmFt!5e0!3m2!1svi!2s!4v1689400000000!5m2!1svi!2s');
INSERT INTO stores (name,address,phone,opening_hours,map_url) VALUES (N'Jolliebear Nguyễn Huệ', N'55 Nguyễn Huệ, Phường Bến Nghé, Quận 1, TP. Hồ Chí Minh', '02838245678', N'09:00 - 23:00', 'https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3919.4480211158586!2d106.70155097590497!3d10.774941959178652!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x31752f417f7b3dfd%3A0x7d6f51be0e49ad0a!2zNTUgTmd1eeG7h24gSHXhu4csIEPhu5VuZyB0csaw4budbmcgVHVs4buxY2gsIELhur9uIE5naMOpLCBRdeG6rW4gMSwgSOG7kyBDaMOtIE1pbmgsIFZp4buHdCBOYW0!5e0!3m2!1svi!2s!4v1689400000001!5m2!1svi!2s');
INSERT INTO stores (name,address,phone,opening_hours,map_url) VALUES (N'Jolliebear Landmark 81', N'L1-02 Vinhomes Landmark 81, Quận Bình Thạnh, TP. Hồ Chí Minh', '02836209999', N'09:30 - 22:00', 'https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3919.125124021287!2d106.71960247590518!3d10.797893158718818!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x317527c4bd7f8d67%3A0x58585f67dfbf5d6c!2zVmluaG9tZXMgTGFuZG1hcmsgODEsIDIwOCBOZ3V54buFbiBI4buvdSwgUGjGsOG7nW5nIDIyLCBCw6xuaCBUaOG6oW5oLCBI4buTIENow60gTWluaCwgVmnhu4d0IE5hbQ!5e0!3m2!1svi!2s!4v1689400000002!5m2!1svi!2s');
GO
/* ---------- JOBS ---------- */
INSERT INTO jobs (title,department,location,salary,description,requirements,deadline) VALUES (N'Nhân Viên Bán Hàng & Phục Vụ', N'Vận Hành Cửa Hàng', N'TP. Hồ Chí Minh', N'22.000đ - 25.000đ/giờ', N'Chào đón và hướng dẫn khách hàng gọi món; thực hiện thanh toán tại quầy thu ngân; chuẩn bị khay ăn và phục vụ món cho khách; giữ gìn vệ sinh khu vực sảnh ăn uống sạch sẽ thoáng mát.', N'Độ tuổi từ 18-25; nhanh nhẹn, hoạt bát, có kỹ năng giao tiếp tốt; chăm chỉ và có trách nhiệm; ưu tiên làm được xoay ca hoặc ca tối và các ngày lễ Tết.', '2026-09-30');
INSERT INTO jobs (title,department,location,salary,description,requirements,deadline) VALUES (N'Quản Lý Cửa Hàng (Store Manager)', N'Quản Lý', N'TP. Hồ Chí Minh', N'15.000.000đ - 18.000.000đ', N'Chịu trách nhiệm toàn bộ hoạt động vận hành của cửa hàng; quản lý và phân ca cho nhân viên; đảm bảo doanh số và chất lượng dịch vụ; xử lý các khiếu nại của khách hàng; báo cáo doanh thu định kỳ cho Quản lý vùng.', N'Có ít nhất 2 năm kinh nghiệm quản lý cửa hàng F&B hoặc bán lẻ; khả năng lãnh đạo, giải quyết vấn đề tốt; giao tiếp tiếng Anh cơ bản là một lợi thế.', '2026-08-31');
INSERT INTO jobs (title,department,location,salary,description,requirements,deadline) VALUES (N'Nhân Viên Bếp Trung Tâm', N'Chế Biến', N'Hà Nội', N'7.000.000đ - 9.000.000đ', N'Thực hiện sơ chế nguyên liệu theo đúng quy trình; tẩm ướp gà và chiên gà giòn vui vẻ theo đúng tiêu chuẩn công thức Jolliebear; giữ vệ sinh khu vực bếp và công cụ dụng cụ sạch sẽ.', N'Không yêu cầu kinh nghiệm (sẽ được đào tạo); chăm chỉ, chịu khó, có sức khỏe tốt; có chứng nhận vệ sinh an toàn thực phẩm là điểm cộng.', '2026-10-15');
GO
