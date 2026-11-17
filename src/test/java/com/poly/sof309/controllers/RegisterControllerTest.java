package com.poly.sof309.controllers;

import com.jolliebear.controller.RegisterServlet;
import com.jolliebear.model.User;
import com.jolliebear.util.PasswordUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class RegisterControllerTest {

    // Helper tạo nhanh đối tượng User
    private User createUser(String name, String username, String phone, String email, String password) {
        User u = new User();
        u.setName(name);
        u.setUsername(username);
        u.setPhone(phone);
        u.setEmail(email);
        u.setPassword(password);
        return u;
    }

    // =========================================================================
    // 1. KIỂM THỬ BIÊN FORM BẰNG @ParameterizedTest + @CsvSource
    // =========================================================================

    @ParameterizedTest(name = "[{index}] Username biên hợp lệ: ''{0}'' -> Pass")
    @CsvSource({
        "abc",                    // Biên dưới: đúng 3 ký tự
        "tuan_2026",              // Giữa khoảng: chữ + gạch dưới + số
        "user_123456789012345"    // Biên trên: đúng 20 ký tự
    })
    @DisplayName("Kiểm tra biên Username hợp lệ (3 - 20 ký tự)")
    void testValidUsernameBoundary(String validUsername) {
        User user = createUser("Anh Tuan", validUsername, "0901234567", "tuan@gmail.com", "123456");
        assertDoesNotThrow(() -> RegisterServlet.validateRegistration(user));
    }

    @ParameterizedTest(name = "[{index}] Dữ liệu biên không hợp lệ ({0}) -> Ném IllegalArgumentException")
    @CsvSource({
        "'', tuan01, 0901234567, tuan@gmail.com, 123456",              // Họ tên rỗng
        "Anh Tuan, ab, 0901234567, tuan@gmail.com, 123456",            // Username 2 ký tự (dưới biên 3)
        "Anh Tuan, user_1234567890123456, 0901234567, tuan@gmail.com, 123456", // Username 21 ký tự (vượt biên 20)
        "Anh Tuan, tuan@01, 0901234567, tuan@gmail.com, 123456",       // Username chứa ký tự đặc biệt
        "Anh Tuan, tuan01, 1234567890, tuan@gmail.com, 123456",        // SĐT không bắt đầu bằng số 0
        "Anh Tuan, tuan01, 090123456, tuan@gmail.com, 123456",         // SĐT thiếu số (9 số)
        "Anh Tuan, tuan01, 0901234567, tuan_sai_email, 123456",        // Email sai định dạng
        "Anh Tuan, tuan01, 0901234567, tuan@gmail.com, 12345"          // Mật khẩu 5 ký tự (dưới biên 6)
    })
    @DisplayName("Kiểm tra biên Form đăng ký không hợp lệ với @CsvSource")
    void testInvalidRegistrationFormBoundary(String name, String username, String phone, String email, String password) {
        User user = createUser(name, username, phone, email, password);
        assertThrows(IllegalArgumentException.class, () -> RegisterServlet.validateRegistration(user));
    }

    // =========================================================================
    // 2. KIỂM THỬ NGOẠI LỆ BẮT LỖI NGHIỆP VỤ BẰNG assertThrows()
    // =========================================================================

    @Test
    @DisplayName("Ném ngoại lệ khi đối tượng User bị null")
    void testNullUserThrowsException() {
        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> RegisterServlet.validateRegistration(null)
        );
        assertEquals("Dữ liệu đăng ký không hợp lệ.", ex.getMessage());
    }

    @Test
    @DisplayName("Ném ngoại lệ khi thiếu trường bắt buộc (password = null)")
    void testMissingRequiredFieldThrowsException() {
        User user = createUser("Anh Tuan", "tuan01", "0901234567", "tuan@gmail.com", null);
        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> RegisterServlet.validateRegistration(user)
        );
        assertEquals("Dữ liệu đăng ký không hợp lệ.", ex.getMessage());
    }

    // =========================================================================
    // 3. KIỂM THỬ LOGIC BĂM & XÁC THỰC MẬT KHẨU (PasswordUtil)
    // =========================================================================

    @Test
    @DisplayName("Kiểm tra mã hóa SHA-256 + Salt và xác thực mật khẩu thành công")
    void testPasswordHashAndVerify() {
        String rawPass = "Jolliebear@2026";
        String hashed = PasswordUtil.hash(rawPass);

        assertTrue(PasswordUtil.isHashed(hashed), "Chuỗi băm phải chứa dấu ':' phân tách salt và hash");
        assertTrue(PasswordUtil.verify(rawPass, hashed), "Mật khẩu đúng phải xác thực thành công");
        assertFalse(PasswordUtil.verify("SaiMatKhau", hashed), "Mật khẩu sai phải trả về false");
        assertFalse(PasswordUtil.verify(null, hashed), "Mật khẩu null phải trả về false");
    }
}