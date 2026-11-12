/**
 * Jolliebear Web Layout Interaction Script
 */

// Chống XSS: escape ký tự HTML trước khi nhét vào innerHTML
window.escapeHtml = function (s) {
    if (s === null || s === undefined) return '';
    return String(s)
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;')
        .replace(/'/g, '&#39;');
};

// ==========================================
// GIỎ HÀNG (CART) - dùng chung cho mọi trang
// Lưu trong localStorage, key: 'jolliebear-cart'
// ==========================================
window.JBCart = (function () {
    const KEY = 'jolliebear-cart';

    const get = () => {
        try { return JSON.parse(localStorage.getItem(KEY)) || []; }
        catch (e) { return []; }
    };
    const save = (cart) => localStorage.setItem(KEY, JSON.stringify(cart));
    const count = () => get().reduce((sum, i) => sum + (i.qty || 0), 0);
    const total = () => get().reduce((sum, i) => sum + (i.price || 0) * (i.qty || 0), 0);

    const updateBadge = () => {
        const el = document.getElementById('cart-count');
        if (!el) return;
        const n = count();
        el.textContent = n;
        el.style.display = n > 0 ? 'flex' : 'none';
    };

    const add = (item) => {
        const cart = get();
        const found = cart.find(i => String(i.id) === String(item.id));
        if (found) {
            found.qty += 1;
        } else {
            cart.push({
                id: item.id,
                name: item.name,
                price: Number(item.price) || 0,
                image: item.image || '',
                qty: 1
            });
        }
        save(cart);
        updateBadge();
        const b = document.getElementById('cart-count');
        if (b) { b.classList.remove('pop'); void b.offsetWidth; b.classList.add('pop'); }
    };

    const setQty = (id, qty) => {
        let cart = get();
        const it = cart.find(i => String(i.id) === String(id));
        if (it) {
            it.qty = qty;
            if (it.qty <= 0) cart = cart.filter(i => String(i.id) !== String(id));
        }
        save(cart);
        updateBadge();
    };

    const remove = (id) => {
        save(get().filter(i => String(i.id) !== String(id)));
        updateBadge();
    };

    const clear = () => { save([]); updateBadge(); };

    return { get, save, count, total, updateBadge, add, setQty, remove, clear };
})();

document.addEventListener('DOMContentLoaded', () => {

    // Đồng bộ số lượng trên icon giỏ hàng ngay khi tải trang
    window.JBCart.updateBadge();
    
    // ==========================================
    // 1. MOBILE NAVIGATION TOGGLE
    // ==========================================
    const navToggle = document.querySelector('.mobile-nav-toggle');
    const navbar = document.querySelector('.navbar');
    const navLinks = document.querySelectorAll('.nav-link');

    if (navToggle && navbar) {
        navToggle.addEventListener('click', (e) => {
            e.stopPropagation();
            navbar.classList.toggle('open');
            const isOpen = navbar.classList.contains('open');
            navToggle.innerHTML = isOpen ? '<i class="fa-solid fa-xmark"></i>' : '<i class="fa-solid fa-bars"></i>';
        });

        // Close menu on link click
        navLinks.forEach(link => {
            link.addEventListener('click', () => {
                navbar.classList.remove('open');
                navToggle.innerHTML = '<i class="fa-solid fa-bars"></i>';
            });
        });

        // Close menu when clicking outside
        document.addEventListener('click', (e) => {
            if (!navbar.contains(e.target) && !navToggle.contains(e.target)) {
                navbar.classList.remove('open');
                navToggle.innerHTML = '<i class="fa-solid fa-bars"></i>';
            }
        });
    }

    // ==========================================
    // 2. HEADER SCROLL EFFECT
    // ==========================================
    const header = document.querySelector('.main-header');
    
    if (header) {
        window.addEventListener('scroll', () => {
            if (window.scrollY > 50) {
                header.classList.add('scrolled');
            } else {
                header.classList.remove('scrolled');
            }
        });
    }

    // ==========================================
    // 3. HERO CAROUSEL LOGIC
    // ==========================================
    const track = document.querySelector('.carousel-track');
    const slides = Array.from(document.querySelectorAll('.carousel-slide'));
    const indicators = Array.from(document.querySelectorAll('.carousel-indicator'));
    
    let currentSlideIndex = 0;
    let carouselInterval;
    const slideDuration = 5000; // 5 seconds

    const moveToSlide = (targetIndex) => {
        if (!slides.length) return;
        
        // Remove current classes
        slides[currentSlideIndex].classList.remove('current-slide');
        indicators[currentSlideIndex].classList.remove('current-slide-indicator');
        
        // Add classes to target
        slides[targetIndex].classList.add('current-slide');
        indicators[targetIndex].classList.add('current-slide-indicator');
        
        currentSlideIndex = targetIndex;
    };

    const startCarousel = () => {
        stopCarousel();
        carouselInterval = setInterval(() => {
            let nextIndex = (currentSlideIndex + 1) % slides.length;
            moveToSlide(nextIndex);
        }, slideDuration);
    };

    const stopCarousel = () => {
        if (carouselInterval) clearInterval(carouselInterval);
    };

    // Add click listeners to indicators
    indicators.forEach((indicator, index) => {
        indicator.addEventListener('click', () => {
            moveToSlide(index);
            startCarousel(); // Reset timer on manual navigation
        });
    });

    // Start auto slide
    if (slides.length > 0) {
        startCarousel();
        
        // Pause carousel when user hovers over the slider
        const heroSection = document.querySelector('.hero-carousel');
        if (heroSection) {
            heroSection.addEventListener('mouseenter', stopCarousel);
            heroSection.addEventListener('mouseleave', startCarousel);
        }
    }

    // ==========================================
    // 4. ORDER METHOD TOGGLE (Giao hàng / Đặt đến lấy)
    // ==========================================
    const deliveryToggle = document.getElementById('delivery-toggle');
    const pickupToggle = document.getElementById('pickup-toggle');
    const addressInput = document.getElementById('address-input');

    if (deliveryToggle && pickupToggle && addressInput) {
        deliveryToggle.addEventListener('click', () => {
            deliveryToggle.classList.add('active');
            pickupToggle.classList.remove('active');
            addressInput.placeholder = 'Nhập địa chỉ giao hàng của bạn...';
            addressInput.focus();
        });

        pickupToggle.addEventListener('click', () => {
            pickupToggle.classList.add('active');
            deliveryToggle.classList.remove('active');
            addressInput.placeholder = 'Nhập địa chỉ cửa hàng hoặc khu vực muốn lấy hàng...';
            addressInput.focus();
        });
    }

    // ==========================================
    // 5. LANGUAGE SELECTOR
    // ==========================================
    const langVi = document.getElementById('lang-vi');
    const langEn = document.getElementById('lang-en');

    if (langVi && langEn) {
        langVi.addEventListener('click', () => {
            langVi.classList.add('active');
            langEn.classList.remove('active');
            console.log('Language switched to Vietnamese');
        });

        langEn.addEventListener('click', () => {
            langEn.classList.add('active');
            langVi.classList.remove('active');
            console.log('Language switched to English');
        });
    }

    // ==========================================
    // 6. COOKIE CONSENT BANNER
    // ==========================================
    const cookieBanner = document.getElementById('cookie-banner');
    const acceptBtn = document.getElementById('cookie-accept');
    const declineBtn = document.getElementById('cookie-decline');
    const settingsBtn = document.getElementById('cookie-settings');

    if (cookieBanner && acceptBtn && declineBtn) {
        const isConsentGiven = localStorage.getItem('cookie-consent');
        if (!isConsentGiven) {
            setTimeout(() => {
                cookieBanner.classList.add('show');
            }, 1500);
        }

        acceptBtn.addEventListener('click', () => {
            localStorage.setItem('cookie-consent', 'accepted');
            cookieBanner.classList.remove('show');
        });

        declineBtn.addEventListener('click', () => {
            localStorage.setItem('cookie-consent', 'declined');
            cookieBanner.classList.remove('show');
        });

        if (settingsBtn) {
            settingsBtn.addEventListener('click', () => {
                alert('Tùy chỉnh cookie: Hệ thống sẽ tự động cấu hình các tùy chọn tối ưu nhất.');
                localStorage.setItem('cookie-consent', 'customized');
                cookieBanner.classList.remove('show');
            });
        }
    }

    // ==========================================
    // 7. AUTHENTICATION SYSTEM (MOCK DB & SESSION)
    // ==========================================
    
    // Seed default user for testing if users database is empty
    const seedMockUser = () => {
        let users = JSON.parse(localStorage.getItem('jolliebear-users') || '[]');
        const testUserExists = users.some(u => u.email === 'test@jolliebear.com');
        if (!testUserExists) {
            users.push({
                name: "Nguyễn Văn Bear",
                phone: "0909123456",
                email: "test@jolliebear.com",
                password: "password123"
            });
            localStorage.setItem('jolliebear-users', JSON.stringify(users));
        }
    };
    seedMockUser();

    // TOAST NOTIFICATIONS
    const showToast = (message, type = 'success') => {
        const container = document.getElementById('toast-container');
        if (!container) return;

        const toast = document.createElement('div');
        toast.className = `toast toast-${type}`;
        
        let icon = '<i class="fa-solid fa-circle-check"></i>';
        if (type === 'error') {
            icon = '<i class="fa-solid fa-circle-exclamation"></i>';
        } else if (type === 'warning') {
            icon = '<i class="fa-solid fa-triangle-exclamation"></i>';
        }

        toast.innerHTML = `
            ${icon}
            <div class="toast-content">${message}</div>
            <button class="toast-close-btn" aria-label="Close toast">&times;</button>
        `;

        container.appendChild(toast);
        
        // Trigger show animation
        setTimeout(() => toast.classList.add('show'), 10);

        // Auto close toast
        const autoClose = setTimeout(() => {
            closeToast(toast);
        }, 4000);

        // Manual close click
        toast.querySelector('.toast-close-btn').addEventListener('click', () => {
            clearTimeout(autoClose);
            closeToast(toast);
        });
    };

    const closeToast = (toast) => {
        toast.classList.remove('show');
        toast.classList.add('hide');
        toast.addEventListener('transitionend', () => {
            toast.remove();
        });
    };

    // SYNC AUTH STATE HEADER
    const syncAuthState = () => {
        const loggedOutGroup = document.querySelector('.auth-logged-out');
        const loggedInGroup = document.querySelector('.auth-logged-in');
        const userDisplayName = document.getElementById('user-display-name');

        const sessionUser = sessionStorage.getItem('currentUser');
        const localUser = localStorage.getItem('currentUser');
        const currentUser = JSON.parse(sessionUser || localUser);

        if (currentUser) {
            if (loggedOutGroup) loggedOutGroup.classList.add('hidden');
            if (loggedInGroup) loggedInGroup.classList.remove('hidden');
            if (userDisplayName) userDisplayName.textContent = `Chào, ${currentUser.name}`;
            
            const dashboardLink = document.getElementById('dashboard-link');
            if (dashboardLink) {
                if (currentUser.role === 'STAFF' || currentUser.role === 'ADMIN') {
                    dashboardLink.classList.remove('hidden');
                } else {
                    dashboardLink.classList.add('hidden');
                }
            }
        } else {
            if (loggedOutGroup) loggedOutGroup.classList.remove('hidden');
            if (loggedInGroup) loggedInGroup.classList.add('hidden');
            const dashboardLink = document.getElementById('dashboard-link');
            if (dashboardLink) dashboardLink.classList.add('hidden');
        }
    };
    // Init status check
    syncAuthState();

    // MODAL CONTROL ELEMENTS
    const authOverlay = document.getElementById('auth-modal-overlay');
    const loginModal = document.getElementById('login-modal');
    const registerModal = document.getElementById('register-modal');
    const forgotModal = document.getElementById('forgot-modal');
    const changepwModal = document.getElementById('changepw-modal');
    const loginTrigger = document.getElementById('login-trigger');
    const registerTrigger = document.getElementById('register-trigger');
    const closeLogin = document.getElementById('close-login-btn');
    const closeRegister = document.getElementById('close-register-btn');
    const switchToRegister = document.getElementById('switch-to-register');
    const switchToLogin = document.getElementById('switch-to-login');
    const forgotTrigger = document.getElementById('forgot-trigger');
    const forgotSwitchToLogin = document.getElementById('forgot-switch-to-login');
    const closeForgot = document.getElementById('close-forgot-btn');
    const changepwTrigger = document.getElementById('changepw-trigger');
    const closeChangepw = document.getElementById('close-changepw-btn');

    const allAuthCards = [loginModal, registerModal, forgotModal, changepwModal];
    const showOnlyCard = (card) => {
        if (!authOverlay || !card) return;
        authOverlay.classList.add('show');
        allAuthCards.forEach(c => { if (c) c.classList.add('hidden'); });
        card.classList.remove('hidden');
        document.body.style.overflow = 'hidden'; // Stop background scrolling
    };

    const openLoginModal = () => showOnlyCard(loginModal);
    const openRegisterModal = () => showOnlyCard(registerModal);
    const openForgotModal = () => showOnlyCard(forgotModal);
    const openChangePwModal = () => showOnlyCard(changepwModal);

    const closeAllModals = () => {
        if (!authOverlay) return;
        authOverlay.classList.remove('show');
        document.body.style.overflow = '';

        // Reset forms
        ['login-form', 'register-form', 'forgot-form', 'changepw-form'].forEach(id => {
            const f = document.getElementById(id);
            if (f) f.reset();
        });

        // Clear errors
        document.querySelectorAll('.error-msg').forEach(el => el.textContent = '');
    };

    // Attach Modal Listeners
    if (loginTrigger) loginTrigger.addEventListener('click', (e) => { e.preventDefault(); openLoginModal(); });
    if (registerTrigger) registerTrigger.addEventListener('click', (e) => { e.preventDefault(); openRegisterModal(); });
    if (closeLogin) closeLogin.addEventListener('click', closeAllModals);
    if (closeRegister) closeRegister.addEventListener('click', closeAllModals);
    if (switchToRegister) switchToRegister.addEventListener('click', (e) => { e.preventDefault(); openRegisterModal(); });
    if (switchToLogin) switchToLogin.addEventListener('click', (e) => { e.preventDefault(); openLoginModal(); });
    if (forgotTrigger) forgotTrigger.addEventListener('click', (e) => { e.preventDefault(); openForgotModal(); });
    if (forgotSwitchToLogin) forgotSwitchToLogin.addEventListener('click', (e) => { e.preventDefault(); openLoginModal(); });
    if (closeForgot) closeForgot.addEventListener('click', closeAllModals);
    if (changepwTrigger) changepwTrigger.addEventListener('click', (e) => { e.preventDefault(); openChangePwModal(); });
    if (closeChangepw) closeChangepw.addEventListener('click', closeAllModals);
    
    if (authOverlay) {
        authOverlay.addEventListener('click', (e) => {
            if (e.target === authOverlay) {
                closeAllModals();
            }
        });
    }

    // PASSWORD VISIBILITY TOGGLER
    const passwordToggles = document.querySelectorAll('.toggle-password-btn');
    passwordToggles.forEach(toggle => {
        toggle.addEventListener('click', () => {
            const targetId = toggle.getAttribute('data-target');
            const input = document.getElementById(targetId);
            if (input) {
                if (input.type === 'password') {
                    input.type = 'text';
                    toggle.classList.remove('fa-eye-slash');
                    toggle.classList.add('fa-eye');
                } else {
                    input.type = 'password';
                    toggle.classList.remove('fa-eye');
                    toggle.classList.add('fa-eye-slash');
                }
            }
        });
    });

    // SUBMIT REGISTER FORM
    const registerForm = document.getElementById('register-form');
    if (registerForm) {
        registerForm.addEventListener('submit', (e) => {
            e.preventDefault();
            
            // Reset errors
            document.querySelectorAll('.error-msg').forEach(el => el.textContent = '');

            const name = document.getElementById('register-name').value.trim();
            const username = document.getElementById('register-username').value.trim();
            const phone = document.getElementById('register-phone').value.trim();
            const email = document.getElementById('register-email').value.trim();
            const password = document.getElementById('register-password').value;
            const confirmPassword = document.getElementById('register-confirm-password').value;

            let hasErrors = false;

            // Name validation
            if (name.length < 2) {
                document.getElementById('register-name-error').textContent = 'Họ và tên phải có ít nhất 2 ký tự.';
                hasErrors = true;
            }

            // Username validation: 3-20 ký tự, chỉ chữ/số/gạch dưới
            if (!/^[a-zA-Z0-9_]{3,20}$/.test(username)) {
                document.getElementById('register-username-error').textContent = 'Tên đăng nhập 3-20 ký tự, chỉ gồm chữ, số hoặc gạch dưới.';
                hasErrors = true;
            }

            // Phone validation (Vietnamese format: 9-11 digits)
            if (!/^[0-9]{9,11}$/.test(phone)) {
                document.getElementById('register-phone-error').textContent = 'Số điện thoại không hợp lệ (9 - 11 chữ số).';
                hasErrors = true;
            }

            // Password length validation
            if (password.length < 6) {
                document.getElementById('register-password-error').textContent = 'Mật khẩu phải chứa ít nhất 6 ký tự.';
                hasErrors = true;
            }

            // Passwords mismatch
            if (password !== confirmPassword) {
                document.getElementById('register-confirm-password-error').textContent = 'Mật khẩu xác nhận không chính xác.';
                hasErrors = true;
            }

            // Check email address format
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            if (!emailRegex.test(email)) {
                document.getElementById('register-email-error').textContent = 'Địa chỉ email không đúng định dạng.';
                hasErrors = true;
            }

            if (hasErrors) {
                showToast('Vui lòng kiểm tra lại thông tin đăng ký!', 'error');
                return;
            }

            // Send registration data to Java Servlet
            const userData = { name, username, phone, email, password };
            fetch('register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json; charset=UTF-8'
                },
                body: JSON.stringify(userData)
            })
            .then(response => {
                if (!response.ok) {
                    return response.json().then(err => { throw new Error(err.message || 'Đăng ký thất bại!') });
                }
                return response.json();
            })
            .then(data => {
                showToast(data.message || 'Đăng ký tài khoản Jolliebear thành công!', 'success');
                // Switch to Login modal
                setTimeout(() => {
                    openLoginModal();
                    // Prefill tên đăng nhập vừa tạo
                    const loginEmailInput = document.getElementById('login-email');
                    if (loginEmailInput) loginEmailInput.value = username;
                }, 1000);
            })
            .catch(error => {
                showToast(error.message, 'error');
            });
        });
    }

    // SUBMIT LOGIN FORM
    const loginForm = document.getElementById('login-form');
    if (loginForm) {
        loginForm.addEventListener('submit', (e) => {
            e.preventDefault();
            
            document.querySelectorAll('.error-msg').forEach(el => el.textContent = '');

            const emailOrPhone = document.getElementById('login-email').value.trim();
            const password = document.getElementById('login-password').value;
            const rememberMe = document.getElementById('login-remember').checked;

            if (!emailOrPhone || !password) {
                showToast('Vui lòng điền đầy đủ thông tin!', 'error');
                return;
            }

            // Send credentials to Java Servlet
            fetch('login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json; charset=UTF-8'
                },
                body: JSON.stringify({ email: emailOrPhone, password: password })
            })
            .then(response => {
                if (!response.ok) {
                    return response.json().then(err => { throw new Error(err.message || 'Sai thông tin đăng nhập!') });
                }
                return response.json();
            })
            .then(data => {
                showToast(data.message || 'Đăng nhập thành công!', 'success');
                
                // Store user session in front-end
                if (rememberMe) {
                    localStorage.setItem('currentUser', JSON.stringify(data.user));
                } else {
                    sessionStorage.setItem('currentUser', JSON.stringify(data.user));
                }

                // Nếu là tài khoản quản trị -> chuyển thẳng qua trang Admin
                const role = data.user ? data.user.role : null;
                if (role === 'ADMIN' || role === 'STAFF') {
                    closeAllModals();
                    setTimeout(() => { window.location.href = 'dashboard.jsp'; }, 600);
                    return;
                }

                syncAuthState();
                closeAllModals();
            })
            .catch(error => {
                showToast(error.message, 'error');
                document.getElementById('login-password-error').textContent = error.message;
            });
        });
    }

    // SUBMIT FORGOT PASSWORD FORM
    const forgotForm = document.getElementById('forgot-form');
    if (forgotForm) {
        forgotForm.addEventListener('submit', (e) => {
            e.preventDefault();
            document.querySelectorAll('.error-msg').forEach(el => el.textContent = '');

            const email = document.getElementById('forgot-email').value.trim();
            const username = document.getElementById('forgot-username').value.trim();
            const newPassword = document.getElementById('forgot-new-password').value;

            if (newPassword.length < 6) {
                document.getElementById('forgot-new-password-error').textContent = 'Mật khẩu mới phải có ít nhất 6 ký tự.';
                return;
            }

            fetch('api/forgot-password', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json; charset=UTF-8' },
                body: JSON.stringify({ email, username, newPassword })
            })
            .then(res => res.json().then(data => ({ ok: res.ok, data })))
            .then(({ ok, data }) => {
                if (!ok) {
                    showToast(data.message || 'Không thể đặt lại mật khẩu.', 'error');
                    return;
                }
                showToast(data.message || 'Đặt lại mật khẩu thành công!', 'success');
                setTimeout(() => {
                    openLoginModal();
                    const loginEmailInput = document.getElementById('login-email');
                    if (loginEmailInput) loginEmailInput.value = username;
                }, 900);
            })
            .catch(() => showToast('Lỗi kết nối máy chủ. Vui lòng thử lại.', 'error'));
        });
    }

    // SUBMIT CHANGE PASSWORD FORM
    const changepwForm = document.getElementById('changepw-form');
    if (changepwForm) {
        changepwForm.addEventListener('submit', (e) => {
            e.preventDefault();
            document.querySelectorAll('.error-msg').forEach(el => el.textContent = '');

            const oldPassword = document.getElementById('changepw-old').value;
            const newPassword = document.getElementById('changepw-new').value;
            const confirmPassword = document.getElementById('changepw-confirm').value;

            if (newPassword.length < 6) {
                document.getElementById('changepw-new-error').textContent = 'Mật khẩu mới phải có ít nhất 6 ký tự.';
                return;
            }
            if (newPassword !== confirmPassword) {
                document.getElementById('changepw-confirm-error').textContent = 'Xác nhận mật khẩu không khớp.';
                return;
            }

            fetch('api/change-password', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json; charset=UTF-8' },
                body: JSON.stringify({ oldPassword, newPassword })
            })
            .then(res => res.json().then(data => ({ ok: res.ok, data })))
            .then(({ ok, data }) => {
                if (!ok) {
                    showToast(data.message || 'Không thể đổi mật khẩu.', 'error');
                    if (data.message && data.message.indexOf('hiện tại') !== -1) {
                        document.getElementById('changepw-old-error').textContent = data.message;
                    }
                    return;
                }
                showToast(data.message || 'Đổi mật khẩu thành công!', 'success');
                closeAllModals();
            })
            .catch(() => showToast('Lỗi kết nối máy chủ. Vui lòng thử lại.', 'error'));
        });
    }

    // LOGOUT TRIGGER ACTION
    const logoutTrigger = document.getElementById('logout-trigger');
    if (logoutTrigger) {
        logoutTrigger.addEventListener('click', (e) => {
            e.preventDefault();
            
            // Clear session from Java Servlet backend
            fetch('logout', {
                method: 'POST'
            })
            .then(() => {
                sessionStorage.removeItem('currentUser');
                localStorage.removeItem('currentUser');
                showToast('Bạn đã đăng xuất tài khoản.', 'warning');
                syncAuthState();
            })
            .catch(error => {
                console.error('Logout error:', error);
                // Clear front-end state anyway in case of server connection issue
                sessionStorage.removeItem('currentUser');
                localStorage.removeItem('currentUser');
                showToast('Bạn đã đăng xuất tài khoản.', 'warning');
                syncAuthState();
            });
        });
    }

    // ==========================================
    // 8. FLASH SALE COUNTDOWN TIMER
    // ==========================================
    const initFlashSaleCountdown = () => {
        const hoursEl = document.getElementById('hours');
        const minutesEl = document.getElementById('minutes');
        const secondsEl = document.getElementById('seconds');

        if (!hoursEl || !minutesEl || !secondsEl) return;

        // Count down from 52 hours, 5 minutes, 48 seconds
        let totalSeconds = (52 * 3600) + (5 * 60) + 48;

        const updateTimer = () => {
            if (totalSeconds <= 0) {
                hoursEl.textContent = '00';
                minutesEl.textContent = '00';
                secondsEl.textContent = '00';
                clearInterval(timerInterval);
                return;
            }

            const h = Math.floor(totalSeconds / 3600);
            const m = Math.floor((totalSeconds % 3600) / 60);
            const s = totalSeconds % 60;

            hoursEl.textContent = String(h).padStart(2, '0');
            minutesEl.textContent = String(m).padStart(2, '0');
            secondsEl.textContent = String(s).padStart(2, '0');

            totalSeconds--;
        };

        updateTimer();
        const timerInterval = setInterval(updateTimer, 1000);
    };
    initFlashSaleCountdown();

    // ==========================================
    // 9. DYNAMIC SECTIONS: NEWS, STORES, CONTACT, JOBS
    // ==========================================

    // --- MODAL CONTROLS HELPER ---
    const setupModalClose = (modalId, closeBtnId) => {
        const modal = document.getElementById(modalId);
        const closeBtn = document.getElementById(closeBtnId);
        if (modal && closeBtn) {
            closeBtn.addEventListener('click', () => {
                modal.classList.remove('show');
                document.body.style.overflow = '';
            });
            modal.addEventListener('click', (e) => {
                if (e.target === modal) {
                    modal.classList.remove('show');
                    document.body.style.overflow = '';
                }
            });
        }
    };
    setupModalClose('news-details-modal', 'close-news-details');
    setupModalClose('job-details-modal', 'close-job-details');

    // --- A. TIN TỨC & KHUYẾN MÃI (NEWS) ---
    const loadNews = () => {
        const grid = document.getElementById('news-grid-container');
        if (!grid) return;

        grid.innerHTML = '<div style="grid-column: 1/-1; text-align: center; color: rgba(255,255,255,0.6); padding: 40px;"><i class="fa-solid fa-spinner fa-spin" style="font-size: 2rem; margin-bottom: 12px; display: block; color: var(--secondary);"></i> Đang tải tin tức...</div>';

        fetch('api/news')
            .then(res => res.json())
            .then(data => {
                if (!data || data.length === 0) {
                    grid.innerHTML = '<div style="grid-column: 1/-1; text-align: center; color: rgba(255,255,255,0.6); padding: 40px;">Hiện chưa có tin tức nào.</div>';
                    return;
                }

                grid.innerHTML = '';
                data.forEach(item => {
                    const dateStr = item.createdAt ? new Date(item.createdAt).toLocaleDateString('vi-VN') : '16/07/2026';
                    const card = document.createElement('div');
                    card.className = 'news-card';
                    card.innerHTML = `
                        <div class="news-img-wrapper">
                            <img src="${item.imageUrl || 'https://images.unsplash.com/photo-1555396273-367ea4eb4db5?w=600'}" alt="${item.title}" onerror="this.src='https://images.unsplash.com/photo-1555396273-367ea4eb4db5?w=600'">
                        </div>
                        <div class="news-card-body">
                            <span class="news-card-date">${dateStr}</span>
                            <h3>${item.title}</h3>
                            <p>${item.summary}</p>
                            <a href="#news" class="news-card-link">Đọc chi tiết <i class="fa-solid fa-chevron-right" style="font-size: 0.75rem;"></i></a>
                        </div>
                    `;

                    card.addEventListener('click', (e) => {
                        e.preventDefault();
                        showNewsDetails(item.id);
                    });

                    grid.appendChild(card);
                });
            })
            .catch(err => {
                console.error('Error loading news:', err);
                grid.innerHTML = '<div style="grid-column: 1/-1; text-align: center; color: #ff5252; padding: 40px;">Lỗi tải tin tức. Vui lòng tải lại trang.</div>';
            });
    };

    const showNewsDetails = (id) => {
        const modal = document.getElementById('news-details-modal');
        const img = document.getElementById('news-modal-img');
        const date = document.getElementById('news-modal-date');
        const title = document.getElementById('news-modal-title');
        const text = document.getElementById('news-modal-text');

        if (!modal) return;

        fetch(`api/news?id=${id}`)
            .then(res => res.json())
            .then(item => {
                const dateStr = item.createdAt ? new Date(item.createdAt).toLocaleDateString('vi-VN') : '16/07/2026';
                if (img) img.src = item.imageUrl || 'https://images.unsplash.com/photo-1555396273-367ea4eb4db5?w=600';
                if (date) date.textContent = dateStr;
                if (title) title.textContent = item.title;
                
                if (text) {
                    // Split content by newline to create paragraphs
                    const paragraphs = item.content.split('\n');
                    text.innerHTML = paragraphs.map(p => p.trim() ? `<p>${p}</p>` : '').join('');
                }

                modal.classList.add('show');
                document.body.style.overflow = 'hidden';
            })
            .catch(err => {
                console.error('Error fetching news details:', err);
                showToast('Không thể tải thông tin chi tiết bài viết!', 'error');
            });
    };

    // --- B. HỆ THỐNG CỬA HÀNG (STORES) ---
    let storesList = [];
    const loadStores = () => {
        const listContainer = document.getElementById('store-list-container');
        if (!listContainer) return;

        listContainer.innerHTML = '<div style="text-align: center; color: rgba(255,255,255,0.6); padding: 20px;"><i class="fa-solid fa-spinner fa-spin" style="color: var(--secondary);"></i> Đang tải cửa hàng...</div>';

        fetch('api/stores')
            .then(res => res.json())
            .then(data => {
                storesList = data;
                renderStores(storesList);
            })
            .catch(err => {
                console.error('Error loading stores:', err);
                listContainer.innerHTML = '<div style="text-align: center; color: #ff5252; padding: 20px;">Lỗi tải hệ thống cửa hàng.</div>';
            });
    };

    const renderStores = (stores) => {
        const listContainer = document.getElementById('store-list-container');
        const iframe = document.getElementById('store-map-iframe');
        if (!listContainer) return;

        if (!stores || stores.length === 0) {
            listContainer.innerHTML = '<div style="text-align: center; color: rgba(255,255,255,0.6); padding: 20px;">Không tìm thấy cửa hàng nào.</div>';
            return;
        }

        listContainer.innerHTML = '';
        stores.forEach((store, index) => {
            const card = document.createElement('div');
            card.className = `store-card ${index === 0 ? 'active' : ''}`;
            card.innerHTML = `
                <h3>${store.name}</h3>
                <p>${store.address}</p>
                <div class="store-card-details">
                    <span><i class="fa-solid fa-phone"></i> ${store.phone || 'Chưa cập nhật'}</span>
                    <span><i class="fa-solid fa-clock"></i> Giờ mở cửa: ${store.openingHours || '09:00 - 22:00'}</span>
                </div>
            `;

            card.addEventListener('click', () => {
                document.querySelectorAll('.store-card').forEach(c => c.classList.remove('active'));
                card.classList.add('active');
                if (iframe && store.mapUrl) {
                    iframe.src = store.mapUrl;
                }
            });

            listContainer.appendChild(card);
        });

        // Set default map for first store on initial load
        if (stores.length > 0 && iframe && stores[0].mapUrl) {
            iframe.src = stores[0].mapUrl;
        }
    };

    const storeSearchInput = document.getElementById('store-search-input');
    if (storeSearchInput) {
        storeSearchInput.addEventListener('input', (e) => {
            const query = e.target.value.toLowerCase().trim();
            const filtered = storesList.filter(s => 
                s.name.toLowerCase().includes(query) || 
                s.address.toLowerCase().includes(query)
            );
            renderStores(filtered);
        });
    }

    // --- C. LIÊN HỆ (CONTACT FORM) ---
    const contactForm = document.getElementById('contact-form');
    if (contactForm) {
        contactForm.addEventListener('submit', (e) => {
            e.preventDefault();

            const name = document.getElementById('contact-name').value.trim();
            const email = document.getElementById('contact-email').value.trim();
            const phone = document.getElementById('contact-phone').value.trim();
            const subject = document.getElementById('contact-subject').value.trim();
            const message = document.getElementById('contact-message').value.trim();

            if (!name || !email || !subject || !message) {
                showToast('Vui lòng nhập đầy đủ các trường bắt buộc (*)', 'error');
                return;
            }

            const payload = { name, email, phone, subject, message };

            const submitBtn = contactForm.querySelector('button[type="submit"]');
            const originalBtnHtml = submitBtn.innerHTML;
            submitBtn.disabled = true;
            submitBtn.innerHTML = '<i class="fa-solid fa-spinner fa-spin"></i> Đang gửi...';

            fetch('api/contacts', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json; charset=UTF-8' },
                body: JSON.stringify(payload)
            })
            .then(res => res.json())
            .then(data => {
                showToast(data.message || 'Gửi liên hệ thành công!', 'success');
                contactForm.reset();
            })
            .catch(err => {
                console.error('Error submitting contact form:', err);
                showToast('Lỗi kết nối máy chủ. Vui lòng gửi lại sau.', 'error');
            })
            .finally(() => {
                submitBtn.disabled = false;
                submitBtn.innerHTML = originalBtnHtml;
            });
        });
    }

    // --- D. TUYỂN DỤNG (RECRUITMENT/JOBS) ---
    let jobsList = [];
    const loadJobs = () => {
        const grid = document.getElementById('jobs-grid-container');
        if (!grid) return;

        grid.innerHTML = '<div style="grid-column: 1/-1; text-align: center; color: rgba(255,255,255,0.6); padding: 40px;"><i class="fa-solid fa-spinner fa-spin" style="font-size: 2rem; margin-bottom: 12px; display: block; color: var(--secondary);"></i> Đang tải thông tin tuyển dụng...</div>';

        fetch('api/jobs')
            .then(res => res.json())
            .then(data => {
                jobsList = data;
                renderJobs(jobsList);
            })
            .catch(err => {
                console.error('Error loading jobs:', err);
                grid.innerHTML = '<div style="grid-column: 1/-1; text-align: center; color: #ff5252; padding: 40px;">Lỗi tải tin tuyển dụng.</div>';
            });
    };

    const renderJobs = (jobs) => {
        const grid = document.getElementById('jobs-grid-container');
        if (!grid) return;

        const filterLoc = document.getElementById('filter-location').value;
        const filterDept = document.getElementById('filter-dept').value;

        // Apply filtering
        const filtered = jobs.filter(job => {
            const matchLoc = (filterLoc === 'all' || job.location.includes(filterLoc));
            const matchDept = (filterDept === 'all' || job.department === filterDept);
            return matchLoc && matchDept;
        });

        if (filtered.length === 0) {
            grid.innerHTML = '<div style="grid-column: 1/-1; text-align: center; color: rgba(255,255,255,0.6); padding: 40px;">Không tìm thấy vị trí tuyển dụng nào phù hợp.</div>';
            return;
        }

        grid.innerHTML = '';
        filtered.forEach(job => {
            const deadlineStr = job.deadline ? new Date(job.deadline).toLocaleDateString('vi-VN') : 'Thương lượng';
            const card = document.createElement('div');
            card.className = 'job-card';
            card.innerHTML = `
                <div class="job-card-header">
                    <span class="job-card-dept">${job.department}</span>
                    <h3>${job.title}</h3>
                    <div class="job-card-meta">
                        <span><i class="fa-solid fa-location-dot"></i> ${job.location}</span>
                        <span><i class="fa-solid fa-calendar-days"></i> Hạn nộp: ${deadlineStr}</span>
                    </div>
                </div>
                <p class="job-card-desc">${job.description}</p>
                <div class="job-card-footer">
                    <span class="job-card-salary"><i class="fa-solid fa-money-bill-wave" style="color: var(--secondary); margin-right: 6px;"></i> ${job.salary || 'Thỏa thuận'}</span>
                    <button class="job-card-btn">Chi tiết</button>
                </div>
            `;

            card.addEventListener('click', (e) => {
                e.preventDefault();
                showJobDetails(job.id);
            });

            grid.appendChild(card);
        });
    };

    const showJobDetails = (id) => {
        const modal = document.getElementById('job-details-modal');
        const title = document.getElementById('job-modal-title');
        const dept = document.getElementById('job-modal-dept');
        const loc = document.getElementById('job-modal-loc');
        const salary = document.getElementById('job-modal-salary');
        const deadline = document.getElementById('job-modal-deadline');
        const desc = document.getElementById('job-modal-desc');
        const reqs = document.getElementById('job-modal-reqs');
        const applyBtn = document.getElementById('apply-job-btn');

        if (!modal) return;

        fetch(`api/jobs?id=${id}`)
            .then(res => res.json())
            .then(job => {
                const deadlineStr = job.deadline ? new Date(job.deadline).toLocaleDateString('vi-VN') : 'Thương lượng';
                if (title) title.textContent = job.title;
                if (dept) dept.textContent = job.department;
                if (loc) loc.textContent = job.location;
                if (salary) salary.textContent = job.salary || 'Thỏa thuận';
                if (deadline) deadline.textContent = deadlineStr;
                if (desc) desc.textContent = job.description;
                if (reqs) reqs.textContent = job.requirements;

                // Set application action
                if (applyBtn) {
                    // Remove old event listeners
                    const newApplyBtn = applyBtn.cloneNode(true);
                    applyBtn.parentNode.replaceChild(newApplyBtn, applyBtn);
                    
                    newApplyBtn.addEventListener('click', () => {
                        const namePrompt = prompt("Nhập họ và tên của bạn:");
                        if (namePrompt === null) return;
                        if (!namePrompt.trim()) {
                            showToast("Vui lòng nhập họ và tên của bạn để ứng tuyển!", "error");
                            return;
                        }
                        const phonePrompt = prompt("Nhập số điện thoại của bạn:");
                        if (phonePrompt === null) return;
                        if (!/^[0-9]{9,11}$/.test(phonePrompt.trim())) {
                            showToast("Số điện thoại không hợp lệ!", "error");
                            return;
                        }
                        
                        showToast(`Ứng tuyển thành công! Cảm ơn ${namePrompt}, Jolliebear sẽ liên hệ qua SĐT ${phonePrompt} sớm nhất!`, 'success');
                        modal.classList.remove('show');
                        document.body.style.overflow = '';
                    });
                }

                modal.classList.add('show');
                document.body.style.overflow = 'hidden';
            })
            .catch(err => {
                console.error('Error fetching job details:', err);
                showToast('Không thể tải thông tin chi tiết tuyển dụng!', 'error');
            });
    };

    // Filter Listeners
    const selectLoc = document.getElementById('filter-location');
    const selectDept = document.getElementById('filter-dept');
    if (selectLoc) {
        selectLoc.addEventListener('change', () => renderJobs(jobsList));
    }
    if (selectDept) {
        selectDept.addEventListener('change', () => renderJobs(jobsList));
    }

    // ==========================================
    // LANGUAGE TOGGLE & I18N SYSTEM
    // ==========================================
    let currentLang = localStorage.getItem('jolliebear_lang') || 'vi';

    const setupLanguageToggle = () => {
        const langVi = document.getElementById('lang-vi');
        const langEn = document.getElementById('lang-en');

        if (langVi) {
            langVi.addEventListener('click', () => {
                currentLang = 'vi';
                applyTranslations('vi');
                renderFoodItems(foodItemsList);
            });
        }
        if (langEn) {
            langEn.addEventListener('click', () => {
                currentLang = 'en';
                applyTranslations('en');
                renderFoodItems(foodItemsList);
            });
        }

        // Apply saved language on startup
        if (typeof applyTranslations === 'function') {
            applyTranslations(currentLang);
        }
    };

    // ==========================================
    // DYNAMIC MENU ITEMS MODULE
    // ==========================================
    let foodItemsList = [];
    let currentCategory = 'All';

    const loadFoodItems = () => {
        const container = document.getElementById('food-items-container');
        if (!container) return;

        container.innerHTML = '<div style="grid-column: 1/-1; text-align: center; color: #94a3b8; padding: 40px;"><i class="fa-solid fa-spinner fa-spin fa-2x"></i><p style="margin-top: 10px;">Đang tải thực đơn Jolliebear...</p></div>';

        fetch('api/menu')
            .then(res => res.json())
            .then(data => {
                foodItemsList = data;
                renderFoodItems(foodItemsList);
            })
            .catch(err => {
                console.error('Error loading menu:', err);
                container.innerHTML = '<div style="grid-column: 1/-1; text-align: center; color: #ef4444; padding: 40px;"><p>Không thể tải danh sách thực đơn.</p></div>';
            });
    };

    const renderFoodItems = (items) => {
        const container = document.getElementById('food-items-container');
        if (!container) return;

        let filtered = items;

        // Filter by category
        if (currentCategory !== 'All') {
            filtered = filtered.filter(item => item.category === currentCategory);
        }

        // Filter by search query
        const searchVal = document.getElementById('food-search-input')?.value.toLowerCase().trim();
        if (searchVal) {
            filtered = filtered.filter(item => {
                const name = currentLang === 'en' ? item.nameEn : item.nameVi;
                const desc = currentLang === 'en' ? item.descriptionEn : item.descriptionVi;
                return (name && name.toLowerCase().includes(searchVal)) || (desc && desc.toLowerCase().includes(searchVal));
            });
        }

        if (filtered.length === 0) {
            const noFoundMsg = currentLang === 'en' ? 'No matching food items found.' : 'Không tìm thấy món ăn phù hợp.';
            container.innerHTML = `<div style="grid-column: 1/-1; text-align: center; color: #64748b; padding: 40px; font-weight: 600;"><i class="fa-solid fa-utensils fa-2x" style="margin-bottom: 10px; opacity: 0.5;"></i><p>${noFoundMsg}</p></div>`;
            return;
        }

        container.innerHTML = '';
        filtered.forEach(item => {
            const name = currentLang === 'en' ? (item.nameEn || item.nameVi) : item.nameVi;
            const desc = currentLang === 'en' ? (item.descriptionEn || item.descriptionVi) : item.descriptionVi;
            const priceFormatted = new Intl.NumberFormat('vi-VN').format(item.price);
            const btnText = currentLang === 'en' ? 'Order Now' : 'Đặt Món Ngay';

            const card = document.createElement('div');
            card.className = 'food-card';
            card.innerHTML = `
                <div class="food-img-box">
                    <img src="${item.imageUrl || 'https://images.unsplash.com/photo-1626645738196-c2a7c87a8f58?w=500'}" alt="${name}">
                </div>
                <div class="food-details">
                    <h3 class="food-title">${name}</h3>
                    <p class="food-desc">${desc || ''}</p>
                    <div class="food-bottom-row">
                        <span class="food-price">${priceFormatted} đ</span>
                        <button class="btn-order-food" data-id="${item.id}">
                            <i class="fa-solid fa-cart-shopping"></i> ${btnText}
                        </button>
                    </div>
                </div>
            `;

            card.querySelector('.btn-order-food').addEventListener('click', () => {
                window.JBCart.add({
                    id: item.id,
                    name: name,
                    price: item.price,
                    image: item.imageUrl || ''
                });
                const msg = currentLang === 'en'
                    ? `Added "${name}" to your cart!`
                    : `Đã thêm món "${name}" vào giỏ hàng!`;
                showToast(msg, 'success');
                if (window.JBDrawer) window.JBDrawer.open();
            });

            container.appendChild(card);
        });
    };

    // Category Tabs listeners
    document.querySelectorAll('.food-cat-tab').forEach(tab => {
        tab.addEventListener('click', () => {
            document.querySelectorAll('.food-cat-tab').forEach(t => t.classList.remove('active'));
            tab.classList.add('active');
            currentCategory = tab.getAttribute('data-category');
            renderFoodItems(foodItemsList);
        });
    });

    // Search input listener
    const foodSearchInput = document.getElementById('food-search-input');
    if (foodSearchInput) {
        foodSearchInput.addEventListener('input', () => {
            renderFoodItems(foodItemsList);
        });
    }

    // ==========================================
    // CART DRAWER (ngăn kéo giỏ hàng)
    // ==========================================
    const cartDrawer = document.getElementById('cart-drawer');
    const cartOverlay = document.getElementById('cart-drawer-overlay');
    const cartToggleBtn = document.getElementById('cart-toggle-btn');
    const cartDrawerClose = document.getElementById('cart-drawer-close');
    const drawerItems = document.getElementById('cart-drawer-items');
    const drawerEmpty = document.getElementById('cart-drawer-empty');
    const drawerFooter = document.getElementById('cart-drawer-footer');
    const drawerTotal = document.getElementById('cart-drawer-total');
    const drawerCheckout = document.getElementById('cart-drawer-checkout');
    const drawerClear = document.getElementById('cart-drawer-clear');

    const formatVnd = (n) => new Intl.NumberFormat('vi-VN').format(n) + ' đ';

    const renderCartDrawer = () => {
        if (!drawerItems) return;
        const cart = window.JBCart.get();

        if (!cart.length) {
            drawerEmpty.style.display = 'flex';
            drawerItems.style.display = 'none';
            drawerFooter.style.display = 'none';
            return;
        }

        drawerEmpty.style.display = 'none';
        drawerItems.style.display = 'block';
        drawerFooter.style.display = 'block';

        drawerItems.innerHTML = '';
        cart.forEach(item => {
            const row = document.createElement('div');
            row.className = 'drawer-item';
            row.innerHTML = `
                <div class="drawer-item-img">
                    <img src="${item.image || 'https://images.unsplash.com/photo-1626645738196-c2a7c87a8f58?w=200'}" alt="${item.name}" onerror="this.src='https://images.unsplash.com/photo-1626645738196-c2a7c87a8f58?w=200'">
                </div>
                <div class="drawer-item-info">
                    <h4>${item.name}</h4>
                    <span class="drawer-item-price">${formatVnd(item.price)}</span>
                    <div class="drawer-item-qty">
                        <button class="drawer-qty-btn drawer-minus" data-id="${item.id}" aria-label="Giảm">&minus;</button>
                        <span class="drawer-qty-value">${item.qty}</span>
                        <button class="drawer-qty-btn drawer-plus" data-id="${item.id}" aria-label="Tăng">&plus;</button>
                    </div>
                </div>
                <button class="drawer-item-remove" data-id="${item.id}" aria-label="Xóa"><i class="fa-solid fa-xmark"></i></button>
            `;
            drawerItems.appendChild(row);
        });

        if (drawerTotal) drawerTotal.textContent = formatVnd(window.JBCart.total());
    };

    const openCartDrawer = () => {
        if (!cartDrawer) return;
        renderCartDrawer();
        cartDrawer.classList.add('open');
        cartOverlay.classList.add('open');
        document.body.style.overflow = 'hidden';
    };

    const closeCartDrawer = () => {
        if (!cartDrawer) return;
        cartDrawer.classList.remove('open');
        cartOverlay.classList.remove('open');
        document.body.style.overflow = '';
    };

    if (cartToggleBtn) cartToggleBtn.addEventListener('click', (e) => { e.preventDefault(); openCartDrawer(); });
    if (cartDrawerClose) cartDrawerClose.addEventListener('click', closeCartDrawer);
    if (cartOverlay) cartOverlay.addEventListener('click', closeCartDrawer);
    // Đóng bằng phím ESC
    document.addEventListener('keydown', (e) => {
        if (e.key === 'Escape' && cartDrawer && cartDrawer.classList.contains('open')) closeCartDrawer();
    });

    // Tăng / giảm / xóa món trong drawer
    if (drawerItems) {
        drawerItems.addEventListener('click', (e) => {
            const btn = e.target.closest('button');
            if (!btn) return;
            const id = btn.getAttribute('data-id');
            const item = window.JBCart.get().find(i => String(i.id) === String(id));
            if (!item) return;

            if (btn.classList.contains('drawer-plus')) {
                window.JBCart.setQty(id, item.qty + 1);
            } else if (btn.classList.contains('drawer-minus')) {
                window.JBCart.setQty(id, item.qty - 1);
            } else if (btn.classList.contains('drawer-item-remove')) {
                window.JBCart.remove(id);
            }
            renderCartDrawer();
        });
    }

    // Xóa toàn bộ
    if (drawerClear) {
        drawerClear.addEventListener('click', () => {
            if (!window.JBCart.get().length) return;
            window.JBCart.clear();
            renderCartDrawer();
            showToast('Đã xóa toàn bộ giỏ hàng.', 'warning');
        });
    }

    // Đặt hàng -> mở modal checkout 2 bước
    if (drawerCheckout) {
        drawerCheckout.addEventListener('click', () => {
            if (!window.JBCart.get().length) { showToast('Giỏ hàng đang trống!', 'error'); return; }
            closeCartDrawer();
            openCheckout();
        });
    }

    // Cho phép nơi khác (nút Đặt món) mở/cập nhật drawer
    window.JBDrawer = { open: openCartDrawer, close: closeCartDrawer, render: renderCartDrawer };

    // ==========================================
    // CHECKOUT - ĐẶT HÀNG 2 BƯỚC (thông tin -> thanh toán)
    // ==========================================
    const checkoutOverlay = document.getElementById('checkout-overlay');
    const checkoutClose = document.getElementById('checkout-close');
    const checkoutSlide1 = document.getElementById('checkout-slide-1');
    const checkoutSlide2 = document.getElementById('checkout-slide-2');
    const cstep1 = document.getElementById('cstep-1');
    const cstep2 = document.getElementById('cstep-2');
    const checkoutNext = document.getElementById('checkout-next');
    const checkoutBack = document.getElementById('checkout-back');
    const checkoutConfirm = document.getElementById('checkout-confirm');
    const checkoutSummary = document.getElementById('checkout-summary');
    const checkoutTotalEl = document.getElementById('checkout-total');
    const coName = document.getElementById('co-name');
    const coPhone = document.getElementById('co-phone');
    const coAddress = document.getElementById('co-address');
    const coNote = document.getElementById('co-note');

    const showCheckoutSlide = (n) => {
        if (!checkoutSlide1) return;
        checkoutSlide1.classList.toggle('hidden', n !== 1);
        checkoutSlide2.classList.toggle('hidden', n !== 2);
        cstep1.classList.toggle('active', n === 1);
        cstep2.classList.toggle('active', n === 2);
    };

    const openCheckout = () => {
        if (!checkoutOverlay) return;
        if (!window.JBCart.get().length) { showToast('Giỏ hàng đang trống!', 'error'); return; }
        // BẮT BUỘC ĐĂNG NHẬP mới được thanh toán
        const loggedIn = !!(sessionStorage.getItem('currentUser') || localStorage.getItem('currentUser'));
        if (!loggedIn) {
            showToast('Vui lòng đăng nhập để thanh toán giỏ hàng!', 'warning');
            closeCartDrawer();
            openLoginModal();
            return;
        }
        try {
            const u = JSON.parse(sessionStorage.getItem('currentUser') || localStorage.getItem('currentUser'));
            if (u) {
                if (coName && !coName.value) coName.value = u.name || '';
                if (coPhone && !coPhone.value) coPhone.value = u.phone || '';
            }
        } catch (e) {}
        // Điền sẵn địa chỉ đã lưu từ thanh tìm địa chỉ ở trang chủ
        if (coAddress && !coAddress.value) {
            const savedAddr = localStorage.getItem('jb_delivery_address');
            if (savedAddr) coAddress.value = savedAddr;
        }
        showCheckoutSlide(1);
        checkoutOverlay.classList.add('show');
        document.body.style.overflow = 'hidden';
    };

    const closeCheckout = () => {
        if (!checkoutOverlay) return;
        checkoutOverlay.classList.remove('show');
        document.body.style.overflow = '';
    };

    const renderCheckoutSummary = () => {
        if (!checkoutSummary) return;
        const cart = window.JBCart.get();
        checkoutSummary.innerHTML = cart.map(i =>
            `<div class="csum-row"><span>${i.name} x${i.qty}</span><span>${formatVnd(i.price * i.qty)}</span></div>`
        ).join('');
        if (checkoutTotalEl) checkoutTotalEl.textContent = formatVnd(window.JBCart.total());
    };

    const buildItemsSummary = () => window.JBCart.get().map(i => `${i.name} x${i.qty}`).join(', ');

    if (checkoutClose) checkoutClose.addEventListener('click', closeCheckout);
    if (checkoutOverlay) checkoutOverlay.addEventListener('click', (e) => { if (e.target === checkoutOverlay) closeCheckout(); });

    if (checkoutNext) {
        checkoutNext.addEventListener('click', () => {
            document.querySelectorAll('#checkout-slide-1 .error-msg').forEach(el => el.textContent = '');
            const name = coName.value.trim();
            const phone = coPhone.value.trim();
            const address = coAddress.value.trim();
            let bad = false;
            if (name.length < 2) { document.getElementById('co-name-error').textContent = 'Vui lòng nhập họ tên.'; bad = true; }
            if (!/^[0-9]{9,11}$/.test(phone)) { document.getElementById('co-phone-error').textContent = 'Số điện thoại 9-11 chữ số.'; bad = true; }
            if (address.length < 5) { document.getElementById('co-address-error').textContent = 'Vui lòng nhập địa chỉ giao hàng.'; bad = true; }
            if (bad) return;
            renderCheckoutSummary();
            showCheckoutSlide(2);
        });
    }
    if (checkoutBack) checkoutBack.addEventListener('click', () => showCheckoutSlide(1));

    if (checkoutConfirm) {
        checkoutConfirm.addEventListener('click', () => {
            const cart = window.JBCart.get();
            if (!cart.length) { showToast('Giỏ hàng đang trống!', 'error'); closeCheckout(); return; }
            const payEl = document.querySelector('input[name="pay-method"]:checked');
            const payload = {
                customerName: coName.value.trim(),
                phone: coPhone.value.trim(),
                address: coAddress.value.trim(),
                note: coNote.value.trim(),
                paymentMethod: payEl ? payEl.value : 'Tiền mặt khi nhận hàng (COD)',
                items: buildItemsSummary(),
                total: window.JBCart.total(),
                // Gửi danh sách món có cấu trúc để SERVER tự tính lại giá (chống sửa giá)
                cartItems: window.JBCart.get().map(i => ({ id: i.id, name: i.name, price: i.price, qty: i.qty }))
            };
            checkoutConfirm.disabled = true;
            checkoutConfirm.innerHTML = '<i class="fa-solid fa-spinner fa-spin"></i> Đang đặt...';
            fetch('api/orders', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json; charset=UTF-8' },
                body: JSON.stringify(payload)
            })
            .then(res => res.json().then(data => ({ ok: res.ok, data })))
            .then(({ ok, data }) => {
                if (!ok) { showToast(data.message || 'Đặt hàng thất bại.', 'error'); return; }
                showToast(data.message || 'Đặt hàng thành công!', 'success');
                window.JBCart.clear();
                if (window.JBDrawer) window.JBDrawer.render();
                coName.value = ''; coPhone.value = ''; coAddress.value = ''; coNote.value = '';
                closeCheckout();
                if (typeof window.JBCartPageRender === 'function') window.JBCartPageRender();
            })
            .catch(() => showToast('Lỗi kết nối máy chủ.', 'error'))
            .finally(() => {
                checkoutConfirm.disabled = false;
                checkoutConfirm.innerHTML = '<i class="fa-solid fa-check"></i> Đặt hàng';
            });
        });
    }

    window.JBCheckout = { open: openCheckout };

    // ==========================================
    // NÚT "ĐẶT NGAY" Ở TRANG KHUYẾN MÃI
    // ==========================================
    // Combo thật -> thêm vào giỏ + mở giỏ hàng
    document.querySelectorAll('.promo-order-btn[data-promo-id]').forEach(btn => {
        btn.addEventListener('click', () => {
            const name = btn.getAttribute('data-promo-name');
            window.JBCart.add({
                id: btn.getAttribute('data-promo-id'),
                name: name,
                price: Number(btn.getAttribute('data-promo-price')) || 0,
                image: ''
            });
            showToast(`Đã thêm "${name}" vào giỏ hàng!`, 'success');
            if (window.JBDrawer) window.JBDrawer.open();
        });
    });
    // Ưu đãi (freeship, giảm giá) -> chuyển sang trang thực đơn
    document.querySelectorAll('.promo-order-btn[data-nav]').forEach(btn => {
        btn.addEventListener('click', () => {
            window.location.href = btn.getAttribute('data-nav');
        });
    });

    // ==========================================
    // THANH TÌM ĐỊA CHỈ Ở TRANG CHỦ (lưu để điền sẵn khi đặt hàng)
    // ==========================================
    const addrSearchBtn = document.querySelector('.address-search-box .search-btn');
    if (addrSearchBtn && addressInput) {
        const saveAddr = () => {
            const addr = addressInput.value.trim();
            if (!addr) { showToast('Vui lòng nhập địa chỉ giao hàng.', 'warning'); return; }
            localStorage.setItem('jb_delivery_address', addr);
            showToast('Đã lưu địa chỉ! Sẽ tự điền khi bạn đặt hàng.', 'success');
        };
        addrSearchBtn.addEventListener('click', saveAddr);
        addressInput.addEventListener('keydown', (e) => { if (e.key === 'Enter') { e.preventDefault(); saveAddr(); } });
    }

    // ==========================================
    // HIỆU ỨNG XUẤT HIỆN KHI CUỘN (scroll reveal)
    // ==========================================
    if ('IntersectionObserver' in window) {
        const revealTargets = document.querySelectorAll(
            '.service-card, .promo-card, .category-card, .promo-brand-card, ' +
            '.section-title, .services-header, .promotions-header, .news-header, .stores-header, .contact-header, .jobs-header, ' +
            '.flash-sale-banner, .contact-info-panel, .contact-form-panel'
        );
        if (revealTargets.length) {
            const io = new IntersectionObserver((entries) => {
                entries.forEach(entry => {
                    if (entry.isIntersecting) {
                        entry.target.classList.add('visible');
                        io.unobserve(entry.target);
                    }
                });
            }, { threshold: 0.1 });
            revealTargets.forEach(el => { el.classList.add('reveal'); io.observe(el); });
        }
    }

    // --- TRIGGER INITIAL DATA LOADS ---
    setupLanguageToggle();
    loadFoodItems();
    loadNews();
    loadStores();
    loadJobs();
});
