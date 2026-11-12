package com.jolliebear.dao;

import com.jolliebear.util.PasswordUtil;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DBConnection {
    // Giá trị mặc định; sẽ được ghi đè bởi file db.properties nếu có (WEB-INF/classes/db.properties)
    private static String DB_USER = "sa";
    private static String DB_PASSWORD = "mincute2k9";
    private static String DB_HOST = "localhost";
    private static String DB_PORT = "1433";
    private static String DB_NAME = "Jolliebear_SQL";

    private static String MASTER_URL;
    private static String APP_DB_URL;

    private static boolean isInitialized = false;

    static {
        try {
            loadConfig();
            // Load driver class
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            initializeDatabase();
        } catch (ClassNotFoundException e) {
            System.err.println("SQL Server JDBC Driver not found!");
            e.printStackTrace();
        }
    }

    // Đọc cấu hình từ db.properties trên classpath; nếu không có thì dùng mặc định.
    private static void loadConfig() {
        try (InputStream is = DBConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (is != null) {
                Properties p = new Properties();
                p.load(is);
                DB_USER = p.getProperty("db.user", DB_USER).trim();
                DB_PASSWORD = p.getProperty("db.password", DB_PASSWORD).trim();
                DB_HOST = p.getProperty("db.host", DB_HOST).trim();
                DB_PORT = p.getProperty("db.port", DB_PORT).trim();
                DB_NAME = p.getProperty("db.name", DB_NAME).trim();
                System.out.println("Đã nạp cấu hình DB từ db.properties.");
            } else {
                System.out.println("Không thấy db.properties, dùng cấu hình mặc định.");
            }
        } catch (Exception e) {
            System.err.println("Lỗi đọc db.properties, dùng mặc định: " + e.getMessage());
        }
        String base = "jdbc:sqlserver://" + DB_HOST + ":" + DB_PORT + ";databaseName=";
        String tail = ";encrypt=true;trustServerCertificate=true;";
        MASTER_URL = base + "master" + tail;
        APP_DB_URL = base + DB_NAME + tail;
    }

    public static synchronized Connection getConnection() throws SQLException {
        if (!isInitialized) {
            initializeDatabase();
        }
        return DriverManager.getConnection(APP_DB_URL, DB_USER, DB_PASSWORD);
    }

    public static synchronized void initializeDatabase() {
        if (isInitialized) return;
        
        System.out.println("Initializing Jolliebear Database...");
        
        // 1. Create database Jolliebear_SQL if not exists
        try (Connection masterConn = DriverManager.getConnection(MASTER_URL, DB_USER, DB_PASSWORD);
             Statement stmt = masterConn.createStatement()) {
            
            String createDbSql = "IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = 'Jolliebear_SQL') " +
                                 "CREATE DATABASE Jolliebear_SQL;";
            stmt.executeUpdate(createDbSql);
            System.out.println("Database 'Jolliebear_SQL' checked/created successfully.");
            
        } catch (SQLException e) {
            System.err.println("Warning: Could not check/create database from master. Attempting direct connection...");
            e.printStackTrace();
        }

        // 2. Connect to Jolliebear_SQL and create tables + seed data
        try (Connection conn = DriverManager.getConnection(APP_DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement()) {
            
            // Create Users Table
            stmt.executeUpdate(
                "IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='users' AND xtype='U') " +
                "CREATE TABLE users (" +
                "   id INT IDENTITY(1,1) PRIMARY KEY," +
                "   name NVARCHAR(100) NOT NULL," +
                "   username VARCHAR(50)," +
                "   phone VARCHAR(20) NOT NULL UNIQUE," +
                "   email VARCHAR(100) NOT NULL UNIQUE," +
                "   password VARCHAR(100) NOT NULL," +
                "   role VARCHAR(20) NOT NULL DEFAULT 'CUSTOMER'" +
                ");"
            );

            // Create News Table
            stmt.executeUpdate(
                "IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='news' AND xtype='U') " +
                "CREATE TABLE news (" +
                "   id INT IDENTITY(1,1) PRIMARY KEY," +
                "   title NVARCHAR(200) NOT NULL," +
                "   summary NVARCHAR(500) NOT NULL," +
                "   content NVARCHAR(MAX) NOT NULL," +
                "   image_url VARCHAR(200)," +
                "   created_at DATETIME NOT NULL DEFAULT GETDATE()" +
                ");"
            );

            // Create Stores Table
            stmt.executeUpdate(
                "IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='stores' AND xtype='U') " +
                "CREATE TABLE stores (" +
                "   id INT IDENTITY(1,1) PRIMARY KEY," +
                "   name NVARCHAR(100) NOT NULL," +
                "   address NVARCHAR(200) NOT NULL," +
                "   phone VARCHAR(20)," +
                "   opening_hours NVARCHAR(50) DEFAULT '09:00 - 22:00'," +
                "   map_url VARCHAR(500)" +
                ");"
            );

            // Create Jobs Table
            stmt.executeUpdate(
                "IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='jobs' AND xtype='U') " +
                "CREATE TABLE jobs (" +
                "   id INT IDENTITY(1,1) PRIMARY KEY," +
                "   title NVARCHAR(200) NOT NULL," +
                "   department NVARCHAR(100) NOT NULL," +
                "   location NVARCHAR(100) NOT NULL," +
                "   salary NVARCHAR(50) DEFAULT N'Thỏa thuận'," +
                "   description NVARCHAR(MAX) NOT NULL," +
                "   requirements NVARCHAR(MAX) NOT NULL," +
                "   deadline DATE," +
                "   created_at DATETIME NOT NULL DEFAULT GETDATE()" +
                ");"
            );

            // Create Contacts Table
            stmt.executeUpdate(
                "IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='contacts' AND xtype='U') " +
                "CREATE TABLE contacts (" +
                "   id INT IDENTITY(1,1) PRIMARY KEY," +
                "   name NVARCHAR(100) NOT NULL," +
                "   email VARCHAR(100) NOT NULL," +
                "   phone VARCHAR(20)," +
                "   subject NVARCHAR(200)," +
                "   message NVARCHAR(MAX) NOT NULL," +
                "   created_at DATETIME NOT NULL DEFAULT GETDATE()," +
                "   is_read BIT NOT NULL DEFAULT 0" +
                ");"
            );

            // Create Menu Items Table
            stmt.executeUpdate(
                "IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='menu_items' AND xtype='U') " +
                "CREATE TABLE menu_items (" +
                "   id INT IDENTITY(1,1) PRIMARY KEY," +
                "   name_vi NVARCHAR(150) NOT NULL," +
                "   name_en NVARCHAR(150) NOT NULL," +
                "   category VARCHAR(50) NOT NULL," +
                "   price FLOAT NOT NULL," +
                "   description_vi NVARCHAR(500)," +
                "   description_en NVARCHAR(500)," +
                "   image_url VARCHAR(300)," +
                "   is_available BIT NOT NULL DEFAULT 1" +
                ");"
            );

            // Create Orders Table
            stmt.executeUpdate(
                "IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='orders' AND xtype='U') " +
                "CREATE TABLE orders (" +
                "   id INT IDENTITY(1,1) PRIMARY KEY," +
                "   user_email VARCHAR(100)," +
                "   customer_name NVARCHAR(100) NOT NULL," +
                "   phone VARCHAR(20) NOT NULL," +
                "   address NVARCHAR(300) NOT NULL," +
                "   payment_method NVARCHAR(50) NOT NULL," +
                "   items NVARCHAR(MAX) NOT NULL," +
                "   total FLOAT NOT NULL," +
                "   note NVARCHAR(500)," +
                "   status NVARCHAR(30) NOT NULL DEFAULT N'Chờ xử lý'," +
                "   created_at DATETIME NOT NULL DEFAULT GETDATE()" +
                ");"
            );

            System.out.println("Tables created or verified.");

            // --- MIGRATION: đảm bảo cột username tồn tại (cho DB đã tạo từ trước) ---
            stmt.executeUpdate(
                "IF NOT EXISTS (SELECT * FROM sys.columns WHERE Name = N'username' AND Object_ID = Object_ID(N'users')) " +
                "ALTER TABLE users ADD username VARCHAR(50) NULL;");
            // Điền username cho các tài khoản mặc định nếu còn trống
            stmt.executeUpdate("UPDATE users SET username = 'admin' WHERE email = 'admin@jolliebear.com' AND username IS NULL;");
            stmt.executeUpdate("UPDATE users SET username = 'staff' WHERE email = 'staff@jolliebear.com' AND username IS NULL;");
            stmt.executeUpdate("UPDATE users SET username = 'testbear' WHERE email = 'test@jolliebear.com' AND username IS NULL;");
            // Chỉ mục duy nhất cho username (lọc NULL để cho phép nhiều dòng chưa có username)
            stmt.executeUpdate(
                "IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'UX_users_username') " +
                "CREATE UNIQUE INDEX UX_users_username ON users(username) WHERE username IS NOT NULL;");

            // MIGRATION: đảm bảo cột user_email tồn tại trong orders (gắn đơn với tài khoản khách)
            stmt.executeUpdate(
                "IF EXISTS (SELECT * FROM sysobjects WHERE name='orders' AND xtype='U') " +
                "AND NOT EXISTS (SELECT * FROM sys.columns WHERE Name = N'user_email' AND Object_ID = Object_ID(N'orders')) " +
                "ALTER TABLE orders ADD user_email VARCHAR(100) NULL;");

            // Seed default users if empty
            try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM users")) {
                if (rs.next() && rs.getInt(1) == 0) {
                    stmt.executeUpdate("INSERT INTO users (name, username, phone, email, password, role) VALUES " +
                        "(N'Admin Jolliebear', 'admin', '0900000001', 'admin@jolliebear.com', 'admin123', 'ADMIN')," +
                        "(N'Staff Jolliebear', 'staff', '0900000002', 'staff@jolliebear.com', 'staff123', 'STAFF')," +
                        "(N'Nguyễn Văn Bear', 'testbear', '0909123456', 'test@jolliebear.com', 'password123', 'CUSTOMER');");
                    System.out.println("Seed users inserted.");
                }
            }

            // MIGRATION BẢO MẬT: băm mọi mật khẩu còn ở dạng plaintext (không chứa ':')
            List<Integer> plainIds = new ArrayList<>();
            List<String> plainPws = new ArrayList<>();
            try (ResultSet rs = stmt.executeQuery("SELECT id, password FROM users WHERE password NOT LIKE '%:%'")) {
                while (rs.next()) {
                    plainIds.add(rs.getInt(1));
                    plainPws.add(rs.getString(2));
                }
            }
            if (!plainIds.isEmpty()) {
                try (PreparedStatement up = conn.prepareStatement("UPDATE users SET password = ? WHERE id = ?")) {
                    for (int i = 0; i < plainIds.size(); i++) {
                        up.setString(1, PasswordUtil.hash(plainPws.get(i)));
                        up.setInt(2, plainIds.get(i));
                        up.executeUpdate();
                    }
                }
                System.out.println("Đã băm " + plainIds.size() + " mật khẩu plaintext.");
            }

            // Seed default news if empty
            try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM news")) {
                if (rs.next() && rs.getInt(1) == 0) {
                    stmt.executeUpdate("INSERT INTO news (title, summary, content, image_url) VALUES " +
                        "(N'Khai Trương Chi Nhánh Quận 1 Mới', N'Jolliebear tưng bừng khai trương chi nhánh thứ 15 tại trung tâm Quận 1 với ngập tràn ưu đãi hấp dẫn.', N'Thương hiệu gà rán vui vẻ Jolliebear chính thức khai trương chi nhánh mới tại số 120 Lê Lai, Quận 1, TP. HCM. Trong tuần đầu khai trương, khách hàng ghé thăm sẽ được giảm giá 30% cho toàn bộ thực đơn và nhận ngay một mascot gấu dễ thương cho hóa đơn từ 200.000đ. Hãy đến và trải nghiệm không gian hiện đại, sang trọng của Jolliebear!', 'https://images.unsplash.com/photo-1555396273-367ea4eb4db5?w=600')," +
                        "(N'Combo Gà Giòn Chào Hè Tiết Kiệm', N'Giải nhiệt mùa hè cùng Combo Gà Giòn Vui Vẻ chỉ từ 59.000đ đi kèm ly nước ngọt siêu to khổng lồ.', N'Chào hè rực rỡ, Jolliebear ra mắt bộ sưu tập Combo Chào Hè với giá cực kỳ tiết kiệm. Combo bao gồm 2 miếng Gà Giòn Vui Vẻ giòn rụm bên ngoài mọng nước bên trong, 1 phần khoai tây chiên vàng giòn và 1 ly Pepsi mát lạnh. Ưu đãi kéo dài từ nay đến hết ngày 31/08/2026 trên toàn bộ hệ thống cửa hàng và app đặt món.', 'https://images.unsplash.com/photo-1569058242253-92a9c755a0ec?w=600')," +
                        "(N'Hành Trình Jolliebear Vì Cộng Đồng 2026', N'Jolliebear Việt Nam đã trao tặng hơn 5.000 suất ăn ấm lòng cho trẻ em nghèo hiếu học vùng cao.', N'Nằm trong chuỗi hoạt động trách nhiệm xã hội thường niên, chương trình \"Hành Trình Chia Sẻ Niềm Vui\" của Jolliebear đã dừng chân tại các tỉnh miền núi phía Bắc. Đội ngũ nhân viên Jolliebear đã tự tay chuẩn bị và trao tặng hàng ngàn suất ăn dinh dưỡng cùng nhiều phần quà học tập ý nghĩa, thắp sáng ước mơ đến trường cho trẻ em hiếu học.', 'https://images.unsplash.com/photo-1488521787991-ed7bbaae773c?w=600');");
                    System.out.println("Seed news inserted.");
                }
            }

            // Seed default stores if empty
            try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM stores")) {
                if (rs.next() && rs.getInt(1) == 0) {
                    stmt.executeUpdate("INSERT INTO stores (name, address, phone, opening_hours, map_url) VALUES " +
                        "(N'Jolliebear Bến Thành', N'120 Lê Lai, Phường Bến Thành, Quận 1, TP. Hồ Chí Minh', '02838241234', '09:00 - 22:30', 'https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3919.4853036662493!2d106.69248437590487!3d10.774092559231604!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x31752f3f501062b1%3A0x67394b9f02931102!2zMTIwIEzDqiBMYWksIELhur9uIFRow6BuaCwgUXXhuq1uIDEsIEjhu5MgQ2jDrSBNaW5oLCBWaeG7h3QgTmFt!5e0!3m2!1svi!2s!4v1689400000000!5m2!1svi!2s')," +
                        "(N'Jolliebear Nguyễn Huệ', N'55 Nguyễn Huệ, Phường Bến Nghé, Quận 1, TP. Hồ Chí Minh', '02838245678', '09:00 - 23:00', 'https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3919.4480211158586!2d106.70155097590497!3d10.774941959178652!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x31752f417f7b3dfd%3A0x7d6f51be0e49ad0a!2zNTUgTmd1eeG7h24gSHXhu4csIEPhu5VuZyB0csaw4budbmcgVHVs4buxY2gsIELhur9uIE5naMOpLCBRdeG6rW4gMSwgSOG7kyBDaMOtIE1pbmgsIFZp4buHdCBOYW0!5e0!3m2!1svi!2s!4v1689400000001!5m2!1svi!2s')," +
                        "(N'Jolliebear Landmark 81', N'L1-02 Vinhomes Landmark 81, Quận Bình Thạnh, TP. Hồ Chí Minh', '02836209999', '09:30 - 22:00', 'https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3919.125124021287!2d106.71960247590518!3d10.797893158718818!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x317527c4bd7f8d67%3A0x58585f67dfbf5d6c!2zVmluaG9tZXMgTGFuZG1hcmsgODEsIDIwOCBOZ3V54buFbiBI4buvdSwgUGjGsOG7nW5nIDIyLCBCw6xuaCBUaOG6oW5oLCBI4buTIENow60gTWluaCwgVmnhu4d0IE5hbQ!5e0!3m2!1svi!2s!4v1689400000002!5m2!1svi!2s');");
                    System.out.println("Seed stores inserted.");
                }
            }

            // Seed default jobs if empty
            try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM jobs")) {
                if (rs.next() && rs.getInt(1) == 0) {
                    stmt.executeUpdate("INSERT INTO jobs (title, department, location, salary, description, requirements, deadline) VALUES " +
                        "(N'Nhân Viên Bán Hàng & Phục Vụ', N'Vận Hành Cửa Hàng', N'TP. Hồ Chí Minh', N'22.000đ - 25.000đ/giờ', N'Chào đón và hướng dẫn khách hàng gọi món; thực hiện thanh toán tại quầy thu ngân; chuẩn bị khay ăn và phục vụ món cho khách; giữ gìn vệ sinh khu vực sảnh ăn uống sạch sẽ thoáng mát.', N'Độ tuổi từ 18-25; nhanh nhẹn, hoạt bát, có kỹ năng giao tiếp tốt; chăm chỉ và có trách nhiệm; ưu tiên làm được xoay ca hoặc ca tối và các ngày lễ Tết.', '2026-09-30')," +
                        "(N'Quản Lý Cửa Hàng (Store Manager)', N'Quản Lý', N'TP. Hồ Chí Minh', N'15.000.000đ - 18.000.000đ', N'Chịu trách nhiệm toàn bộ hoạt động vận hành của cửa hàng; quản lý và phân ca cho nhân viên; đảm bảo doanh số và chất lượng dịch vụ; xử lý các khiếu nại của khách hàng; báo cáo doanh thu định kỳ cho Quản lý vùng.', N'Có ít nhất 2 năm kinh nghiệm quản lý cửa hàng F&B hoặc bán lẻ; khả năng lãnh đạo, giải quyết vấn đề tốt; giao tiếp tiếng Anh cơ bản là một lợi thế.', '2026-08-31')," +
                        "(N'Nhân Viên Bếp Trung Tâm', N'Chế Biến', N'Hà Nội', N'7.000.000đ - 9.000.000đ', N'Thực hiện sơ chế nguyên liệu theo đúng quy trình; tẩm ướp gà và chiên gà giòn vui vẻ theo đúng tiêu chuẩn công thức Jolliebear; giữ vệ sinh khu vực bếp và công cụ dụng cụ sạch sẽ.', N'Không yêu cầu kinh nghiệm (sẽ được đào tạo); chăm chỉ, chịu khó, có sức khỏe tốt; có chứng nhận vệ sinh an toàn thực phẩm là điểm cộng.', '2026-10-15');");
                    System.out.println("Seed jobs inserted.");
                }
            }

            // Seed default menu items if empty
            try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM menu_items")) {
                if (rs.next() && rs.getInt(1) == 0) {
                    stmt.executeUpdate("INSERT INTO menu_items (name_vi, name_en, category, price, description_vi, description_en, image_url, is_available) VALUES " +
                        "(N'Gà Giòn Vui Vẻ (1 Miếng)', N'Chickenjoy (1 Pc)', 'GaGion', 35000, N'1 Miếng Gà Giòn thơm phức, da giòn rụm bên ngoài, thịt mọng nước bên trong.', N'1 Pc Crispy Fried Chicken, crunchy outside and juicy inside.', 'https://images.unsplash.com/photo-1626645738196-c2a7c87a8f58?w=500', 1)," +
                        "(N'Gà Sốt Cay Thượng Hạng', N'Spicy Glazed Chicken', 'GaGion', 39000, N'Gà chiên phủ sốt cay đậm đà kích thích vị giác.', N'Crispy chicken glazed with signature rich spicy sauce.', 'https://images.unsplash.com/photo-1569058242253-92a9c755a0ec?w=500', 1)," +
                        "(N'Mỳ Ý Sốt Bò Băm Jollie Spaghetti', N'Jollie Spaghetti Beef Sauce', 'MyY', 45000, N'Mỳ Ý đượm sốt bò băm cà chua đậm đà kèm pho mát rắc thượng hạng.', N'Spaghetti topped with rich minced beef tomato sauce & cheese.', 'https://images.unsplash.com/photo-1551183053-bf91a1d81141?w=500', 1)," +
                        "(N'Burger Gà Giòn Sốt Mayo', N'Crispy Chicken Burger Mayo', 'Burger', 42000, N'Bánh burger kẹp miếng gà chiên giòn, rau xà lách tươi và sốt mayonnaise.', N'Soft bun with crispy chicken patty, fresh lettuce & mayo.', 'https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=500', 1)," +
                        "(N'Combo Tiết Kiệm Gà & Mỳ Ý', N'Saver Combo Chicken & Spaghetti', 'Combo', 79000, N'Bao gồm 1 Miếng Gà Giòn + 1 Dĩa Mỳ Ý + 1 Ly Pepsi mát lạnh.', N'Includes 1 Pc Chickenjoy + 1 Spaghetti + 1 Cold Pepsi.', 'https://images.unsplash.com/photo-1513104890138-7c749659a591?w=500', 1)," +
                        "(N'Combo Gia Đình Vui Vẻ', N'Family Feast Combo', 'Combo', 199000, N'6 Miếng Gà Giòn + 2 Mỳ Ý + 1 Khoai Tây Chiên Lớn + 3 Pepsi.', N'6 Pcs Chickenjoy + 2 Spaghetti + 1 Large Fries + 3 Pepsi.', 'https://images.unsplash.com/photo-1544025162-d76694265947?w=500', 1)," +
                        "(N'Khoai Tây Chiên Vàng Giòn (L)', N'French Fries Large', 'NuocTrangMieng', 29000, N'Khoai tây chiên vàng ươm, giòn rụm chấm kèm sốt tương cà.', N'Golden crispy french fries served with ketchup sauce.', 'https://images.unsplash.com/photo-1576107232684-1279f3908594?w=500', 1)," +
                        "(N'Pepsi Tươi Mát Lạnh', N'Fresh Cold Pepsi', 'NuocTrangMieng', 18000, N'Nước ngọt sảng khoái giải nhiệt.', N'Refreshing cold carbonated soft drink.', 'https://images.unsplash.com/photo-1622483767028-3f66f32aef97?w=500', 1);");
                    System.out.println("Seed menu items inserted.");
                }
            }

            isInitialized = true;
            System.out.println("Jolliebear Database initialization completed successfully.");
            
        } catch (SQLException e) {
            System.err.println("Fatal Error initializing database tables/seed data:");
            e.printStackTrace();
        }
    }
}
