// Jolliebear Dashboard Interaction Logic

// Chống XSS: escape HTML trước khi render dữ liệu do người dùng nhập
const esc = (s) => {
    if (s === null || s === undefined) return '';
    return String(s)
        .replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;').replace(/'/g, '&#39;');
};

document.addEventListener('DOMContentLoaded', () => {
    
    // ==========================================
    // 1. SESSION CHECK & INITIALIZATION
    // ==========================================
    let currentUser = null;

    const verifySession = () => {
        fetch('api/session')
            .then(res => res.json())
            .then(data => {
                if (data.status === 'success') {
                    currentUser = data.user;
                    const role = currentUser.role;

                    if (role === 'CUSTOMER') {
                        alert('Bạn không có quyền truy cập trang quản trị!');
                        window.location.href = 'index.jsp';
                        return;
                    }

                    // Render profile
                    document.getElementById('profile-name').textContent = currentUser.name;
                    document.getElementById('profile-role').textContent = role === 'ADMIN' ? 'Quản trị viên' : 'Nhân viên';

                    // Enable Admin only tabs
                    if (role === 'ADMIN') {
                        document.querySelectorAll('.admin-only').forEach(el => el.classList.remove('hidden'));
                    }

                    // Load initial stats & overview data
                    loadOverviewStats();
                    initClock();

                } else {
                    alert('Vui lòng đăng nhập tài khoản quản trị!');
                    window.location.href = 'index.jsp#login';
                }
            })
            .catch(err => {
                console.error('Session verification error:', err);
                alert('Lỗi xác thực phiên làm việc. Quay về trang chủ.');
                window.location.href = 'index.jsp';
            });
    };
    verifySession();

    // ==========================================
    // 2. REAL-TIME CLOCK
    // ==========================================
    const initClock = () => {
        const clockEl = document.getElementById('clock-display');
        const updateClock = () => {
            const now = new Date();
            if (clockEl) {
                clockEl.textContent = now.toLocaleString('vi-VN');
            }
        };
        updateClock();
        setInterval(updateClock, 1000);
    };

    // ==========================================
    // 3. TOAST NOTIFICATION SYSTEM
    // ==========================================
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
        setTimeout(() => toast.classList.add('show'), 10);

        const autoClose = setTimeout(() => {
            closeToast(toast);
        }, 4000);

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

    // ==========================================
    // 4. SIDEBAR NAVIGATION & TAB SWITCHING
    // ==========================================
    const navItems = document.querySelectorAll('.nav-item');
    const tabContents = document.querySelectorAll('.tab-content');
    const pageTitle = document.getElementById('page-title');

    navItems.forEach(item => {
        item.addEventListener('click', (e) => {
            e.preventDefault();
            
            const targetTab = item.getAttribute('href').substring(1); // news, stores, etc.
            
            // Set active class in sidebar
            navItems.forEach(n => n.classList.remove('active'));
            item.classList.add('active');

            // Switch tab content panel
            tabContents.forEach(content => {
                if (content.id === `content-${targetTab}`) {
                    content.classList.remove('hidden');
                } else {
                    content.classList.add('hidden');
                }
            });

            // Update title
            pageTitle.textContent = item.querySelector('span').textContent;

            // Trigger specific tab data loads
            switch (targetTab) {
                case 'overview':
                    loadOverviewStats();
                    break;
                case 'menu':
                    loadMenuList();
                    break;
                case 'news':
                    loadNewsList();
                    break;
                case 'stores':
                    loadStoresList();
                    break;
                case 'jobs':
                    loadJobsList();
                    break;
                case 'orders':
                    loadOrdersList();
                    break;
                case 'contacts':
                    loadContactsList();
                    break;
                case 'users':
                    loadUsersList();
                    break;
            }
        });
    });

    // ==========================================
    // 5. OVERVIEW STATISTICS
    // ==========================================
    let revenueChartObj = null, statusChartObj = null;

    // Vẽ (hoặc vẽ lại) biểu đồ doanh thu từ 1 mảng {date, amount}
    const renderRevenueChart = (series) => {
        const rc = document.getElementById('revenueChart');
        if (!rc || typeof Chart === 'undefined' || !Array.isArray(series)) return;
        if (revenueChartObj) revenueChartObj.destroy();
        revenueChartObj = new Chart(rc, {
            type: 'bar',
            data: {
                labels: series.map(d => d.date),
                datasets: [{ label: 'Doanh thu', data: series.map(d => d.amount), backgroundColor: '#f0a500', borderRadius: 6 }]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: { display: false },
                    tooltip: { callbacks: { label: (c) => new Intl.NumberFormat('vi-VN').format(c.parsed.y) + 'đ' } }
                },
                scales: {
                    x: { ticks: { color: '#cbd5e1', maxRotation: 0, autoSkip: true, maxTicksLimit: 16 }, grid: { color: 'rgba(255,255,255,0.05)' } },
                    y: { ticks: { color: '#cbd5e1', callback: (v) => (v / 1000) + 'k' }, grid: { color: 'rgba(255,255,255,0.05)' } }
                }
            }
        });
    };

    // Nạp doanh thu theo khoảng (7/30/60/90/all) rồi vẽ lại
    const loadRevenue = (range) => {
        fetch('api/admin?action=revenue&range=' + encodeURIComponent(range))
            .then(res => res.json())
            .then(data => { if (data.status === 'success' && data.series) renderRevenueChart(data.series); })
            .catch(err => console.error('Lỗi tải doanh thu:', err));
    };

    const renderDashboardCharts = (stats) => {
        if (typeof Chart === 'undefined') return;

        // Doanh thu (mặc định theo lựa chọn trong dropdown, mặc định 7 ngày)
        const rangeSel = document.getElementById('revenue-range');
        if (stats.revenue7days) renderRevenueChart(stats.revenue7days);
        if (rangeSel && rangeSel.value && rangeSel.value !== '7') loadRevenue(rangeSel.value);

        // Đơn theo trạng thái (tròn)
        const sc = document.getElementById('statusChart');
        if (sc && stats.ordersByStatus) {
            const labels = Object.keys(stats.ordersByStatus);
            const data = labels.map(l => stats.ordersByStatus[l]);
            const colorMap = { 'Chờ xử lý': '#f0a500', 'Đã xác nhận': '#06b6d4', 'Đang giao': '#3b82f6', 'Hoàn thành': '#22c55e', 'Đã hủy': '#ef4444' };
            if (statusChartObj) statusChartObj.destroy();
            statusChartObj = new Chart(sc, {
                type: 'doughnut',
                data: { labels: labels, datasets: [{ data: data, backgroundColor: labels.map(l => colorMap[l] || '#94a3b8') }] },
                options: { responsive: true, plugins: { legend: { position: 'bottom', labels: { color: '#cbd5e1' } } } }
            });
        }
    };

    // Đổi khoảng thời gian biểu đồ doanh thu
    const revenueRangeSel = document.getElementById('revenue-range');
    if (revenueRangeSel) {
        revenueRangeSel.addEventListener('change', () => loadRevenue(revenueRangeSel.value));
    }

    const loadOverviewStats = () => {
        fetch('api/admin?action=stats')
            .then(res => res.json())
            .then(data => {
                if (data.status === 'success') {
                    const stats = data.stats;
                    const setTxt = (id, val) => { const el = document.getElementById(id); if (el) el.textContent = val; };
                    setTxt('stat-users', stats.users);
                    setTxt('stat-menu', stats.menu || 0);
                    setTxt('stat-news', stats.news);
                    setTxt('stat-stores', stats.stores);
                    setTxt('stat-jobs', stats.jobs);
                    setTxt('stat-revenue', new Intl.NumberFormat('vi-VN').format(stats.revenue || 0) + 'đ');
                    setTxt('stat-orders-total', stats.ordersTotal || 0);
                    setTxt('stat-orders-today', stats.ordersToday || 0);
                    setTxt('stat-orders-pending', stats.ordersPending || 0);
                    renderDashboardCharts(stats);

                    // Unread contacts badge
                    const msgBadge = document.getElementById('unread-messages-count');
                    if (stats.unreadContacts > 0) {
                        msgBadge.textContent = stats.unreadContacts;
                        msgBadge.classList.remove('hidden');
                    } else {
                        msgBadge.classList.add('hidden');
                    }
                }
            })
            .catch(err => console.error('Error loading dashboard stats:', err));
    };

    // ==========================================
    // 6. LOGOUT ACTION
    // ==========================================
    const logoutTrigger = document.getElementById('logout-trigger');
    if (logoutTrigger) {
        logoutTrigger.addEventListener('click', (e) => {
            e.preventDefault();
            fetch('logout', { method: 'POST' })
                .then(() => {
                    sessionStorage.removeItem('currentUser');
                    localStorage.removeItem('currentUser');
                    alert('Bạn đã đăng xuất tài khoản quản trị.');
                    window.location.href = 'index.jsp';
                })
                .catch(err => {
                    console.error('Logout error:', err);
                    window.location.href = 'index.jsp';
                });
        });
    }

    // ==========================================
    // 7. MODALS HELPER: OPEN / CLOSE
    // ==========================================
    const setupModal = (overlayId, closeBtnId, cancelBtnId) => {
        const overlay = document.getElementById(overlayId);
        const closeBtn = document.getElementById(closeBtnId);
        const cancelBtn = document.getElementById(cancelBtnId);

        const closeModal = () => {
            overlay.classList.remove('show');
            // reset forms inside if exists
            const form = overlay.querySelector('form');
            if (form) form.reset();
        };

        if (closeBtn) closeBtn.addEventListener('click', closeModal);
        if (cancelBtn) cancelBtn.addEventListener('click', closeModal);
        overlay.addEventListener('click', (e) => {
            if (e.target === overlay) closeModal();
        });

        return {
            open: () => overlay.classList.add('show'),
            close: closeModal
        };
    };

    const newsModal = setupModal('news-modal', 'close-news-modal', 'cancel-news-modal');
    const storeModal = setupModal('store-modal', 'close-store-modal', 'cancel-store-modal');
    const jobModal = setupModal('job-modal', 'close-job-modal', 'cancel-job-modal');
    const contactModal = setupModal('contact-modal', 'close-contact-modal', 'close-contact-modal-btn');
    const userModal = setupModal('user-modal', 'close-user-modal', 'cancel-user-modal');
    const menuModal = setupModal('menu-item-modal', 'close-menu-modal', 'cancel-menu-modal');

    // ==========================================
    // LANGUAGE TOGGLE & I18N SYSTEM (DASHBOARD)
    // ==========================================
    let currentDbLang = localStorage.getItem('jolliebear_lang') || 'vi';

    const setupDashboardLanguage = () => {
        const langVi = document.getElementById('lang-vi');
        const langEn = document.getElementById('lang-en');

        if (langVi) {
            langVi.addEventListener('click', () => {
                currentDbLang = 'vi';
                applyTranslations('vi');
                loadOverviewStats();
            });
        }
        if (langEn) {
            langEn.addEventListener('click', () => {
                currentDbLang = 'en';
                applyTranslations('en');
                loadOverviewStats();
            });
        }

        if (typeof applyTranslations === 'function') {
            applyTranslations(currentDbLang);
        }
    };
    setupDashboardLanguage();

    // ==========================================
    // MODULE: MENU MANAGEMENT (CRUD)
    // ==========================================
    let localMenu = [];

    const loadMenuList = () => {
        const tbody = document.getElementById('menu-table-body');
        if (!tbody) return;

        tbody.innerHTML = '<tr><td colspan="6" style="text-align: center; color: var(--text-gray);"><i class="fa-solid fa-spinner fa-spin"></i> Đang tải thực đơn...</td></tr>';

        fetch('api/menu')
            .then(res => res.json())
            .then(data => {
                localMenu = data;
                renderMenuTable(localMenu);
            })
            .catch(err => {
                console.error(err);
                tbody.innerHTML = '<tr><td colspan="6" style="text-align: center; color: #ef4444;">Lỗi tải thực đơn.</td></tr>';
            });
    };

    const renderMenuTable = (menuArray) => {
        const tbody = document.getElementById('menu-table-body');
        if (!tbody) return;

        if (menuArray.length === 0) {
            tbody.innerHTML = '<tr><td colspan="6" style="text-align: center; color: var(--text-gray);">Không có món ăn nào.</td></tr>';
            return;
        }

        tbody.innerHTML = '';
        menuArray.forEach(item => {
            const priceFormatted = new Intl.NumberFormat('vi-VN').format(item.price) + ' đ';
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td><img class="table-img" src="${item.imageUrl || 'https://images.unsplash.com/photo-1626645738196-c2a7c87a8f58?w=100'}" alt="Thumb"></td>
                <td>
                    <strong style="color: var(--text-white); display: block;">${item.nameVi}</strong>
                    <span style="font-size: 0.8rem; color: var(--text-gray);">${item.nameEn || ''}</span>
                </td>
                <td><span class="role-badge role-staff">${item.category}</span></td>
                <td><strong style="color: var(--secondary);">${priceFormatted}</strong></td>
                <td><span class="role-badge ${item.available ? 'role-customer' : 'role-admin'}">${item.available ? 'Phục vụ' : 'Tạm ngưng'}</span></td>
                <td>
                    <div class="actions-cell">
                        <button class="btn-icon btn-icon-edit" data-id="${item.id}" title="Sửa"><i class="fa-solid fa-pen-to-square"></i></button>
                        <button class="btn-icon btn-icon-delete" data-id="${item.id}" title="Xóa"><i class="fa-solid fa-trash"></i></button>
                    </div>
                </td>
            `;

            tr.querySelector('.btn-icon-edit').addEventListener('click', () => openEditMenuItem(item.id));
            tr.querySelector('.btn-icon-delete').addEventListener('click', () => deleteMenuItem(item.id));

            tbody.appendChild(tr);
        });
    };

    // Search Menu
    const menuSearchInput = document.getElementById('menu-search');
    if (menuSearchInput) {
        menuSearchInput.addEventListener('input', (e) => {
            const query = e.target.value.toLowerCase().trim();
            const filtered = localMenu.filter(m => 
                m.nameVi.toLowerCase().includes(query) || 
                (m.nameEn && m.nameEn.toLowerCase().includes(query)) ||
                m.category.toLowerCase().includes(query)
            );
            renderMenuTable(filtered);
        });
    }

    // Add Menu Modal opening
    const addMenuBtn = document.getElementById('add-menu-btn');
    if (addMenuBtn) {
        addMenuBtn.addEventListener('click', () => {
            document.getElementById('menu-item-id').value = '';
            document.getElementById('menu-item-form').reset();
            document.getElementById('menu-modal-title-text').textContent = 'Thêm món ăn mới';
            menuModal.open();
        });
    }

    // Edit Menu Modal opening
    const openEditMenuItem = (id) => {
        fetch(`api/menu?id=${id}`)
            .then(res => res.json())
            .then(item => {
                document.getElementById('menu-item-id').value = item.id;
                document.getElementById('menu-item-name-vi').value = item.nameVi;
                document.getElementById('menu-item-name-en').value = item.nameEn || '';
                document.getElementById('menu-item-cat').value = item.category;
                document.getElementById('menu-item-price').value = item.price;
                document.getElementById('menu-item-desc-vi').value = item.descriptionVi || '';
                document.getElementById('menu-item-desc-en').value = item.descriptionEn || '';
                document.getElementById('menu-item-image').value = item.imageUrl || '';
                document.getElementById('menu-item-avail').value = item.available ? 'true' : 'false';

                document.getElementById('menu-modal-title-text').textContent = 'Chỉnh sửa món ăn';
                menuModal.open();
            })
            .catch(err => {
                console.error(err);
                showToast('Không thể tải thông tin món ăn!', 'error');
            });
    };

    // Save Menu Item Submit
    const menuItemForm = document.getElementById('menu-item-form');
    if (menuItemForm) {
        menuItemForm.addEventListener('submit', (e) => {
            e.preventDefault();
            const id = document.getElementById('menu-item-id').value;
            const nameVi = document.getElementById('menu-item-name-vi').value.trim();
            const nameEn = document.getElementById('menu-item-name-en').value.trim();
            const category = document.getElementById('menu-item-cat').value;
            const price = parseFloat(document.getElementById('menu-item-price').value);
            const descriptionVi = document.getElementById('menu-item-desc-vi').value.trim();
            const descriptionEn = document.getElementById('menu-item-desc-en').value.trim();
            const imageUrl = document.getElementById('menu-item-image').value.trim();
            const available = document.getElementById('menu-item-avail').value === 'true';

            const payload = { nameVi, nameEn, category, price, descriptionVi, descriptionEn, imageUrl, available };
            let method = 'POST';
            if (id) {
                payload.id = parseInt(id);
                method = 'PUT';
            }

            fetch('api/menu', {
                method: method,
                headers: { 'Content-Type': 'application/json; charset=UTF-8' },
                body: JSON.stringify(payload)
            })
            .then(res => res.json())
            .then(data => {
                if (data.status === 'success') {
                    showToast(data.message);
                    menuModal.close();
                    loadMenuList();
                    loadOverviewStats();
                } else {
                    showToast(data.message, 'error');
                }
            })
            .catch(err => {
                console.error(err);
                showToast('Lỗi gửi dữ liệu món ăn.', 'error');
            });
        });
    }

    // Delete Menu Item
    const deleteMenuItem = (id) => {
        if (!confirm('Bạn có chắc chắn muốn xóa món ăn này khỏi thực đơn?')) return;

        fetch(`api/menu?id=${id}`, {
            method: 'DELETE'
        })
        .then(res => res.json())
        .then(data => {
            if (data.status === 'success') {
                showToast(data.message);
                loadMenuList();
                loadOverviewStats();
            } else {
                showToast(data.message, 'error');
            }
        })
        .catch(err => {
            console.error(err);
            showToast('Lỗi gửi yêu cầu xóa món ăn.', 'error');
        });
    };

    // ==========================================
    // 8. MODULE: NEWS MANAGEMENT (CRUD)
    // ==========================================
    let localNews = [];
    
    const loadNewsList = () => {
        const tbody = document.getElementById('news-table-body');
        if (!tbody) return;

        tbody.innerHTML = '<tr><td colspan="5" style="text-align: center; color: var(--text-gray);"><i class="fa-solid fa-spinner fa-spin"></i> Đang tải dữ liệu tin tức...</td></tr>';

        fetch('api/news')
            .then(res => res.json())
            .then(data => {
                localNews = data;
                renderNewsTable(localNews);
            })
            .catch(err => {
                console.error(err);
                tbody.innerHTML = '<tr><td colspan="5" style="text-align: center; color: #ef4444;">Lỗi tải dữ liệu.</td></tr>';
            });
    };

    const renderNewsTable = (newsArray) => {
        const tbody = document.getElementById('news-table-body');
        if (newsArray.length === 0) {
            tbody.innerHTML = '<tr><td colspan="5" style="text-align: center; color: var(--text-gray);">Không tìm thấy bài viết nào.</td></tr>';
            return;
        }

        tbody.innerHTML = '';
        newsArray.forEach(item => {
            const dateStr = item.createdAt ? new Date(item.createdAt).toLocaleDateString('vi-VN') : '---';
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td><img class="table-img" src="${item.imageUrl || 'https://images.unsplash.com/photo-1555396273-367ea4eb4db5?w=100'}" alt="Thumb"></td>
                <td><strong style="color: var(--text-white);">${item.title}</strong></td>
                <td><span style="display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden; font-size: 0.8rem; line-height: 1.4; color: var(--text-gray);">${item.summary}</span></td>
                <td>${dateStr}</td>
                <td>
                    <div class="actions-cell">
                        <button class="btn-icon btn-icon-edit" data-id="${item.id}" title="Sửa"><i class="fa-solid fa-pen-to-square"></i></button>
                        <button class="btn-icon btn-icon-delete" data-id="${item.id}" title="Xóa"><i class="fa-solid fa-trash"></i></button>
                    </div>
                </td>
            `;

            // Attach event listeners
            tr.querySelector('.btn-icon-edit').addEventListener('click', () => openEditNews(item.id));
            tr.querySelector('.btn-icon-delete').addEventListener('click', () => deleteNews(item.id));

            tbody.appendChild(tr);
        });
    };

    // Search News
    document.getElementById('news-search').addEventListener('input', (e) => {
        const query = e.target.value.toLowerCase().trim();
        const filtered = localNews.filter(n => 
            n.title.toLowerCase().includes(query) || 
            n.summary.toLowerCase().includes(query) || 
            n.content.toLowerCase().includes(query)
        );
        renderNewsTable(filtered);
    });

    // Add News Modal opening
    document.getElementById('add-news-btn').addEventListener('click', () => {
        document.getElementById('news-id').value = '';
        document.getElementById('news-modal-title-text').textContent = 'Thêm bài viết mới';
        newsModal.open();
    });

    // Edit News Modal opening
    const openEditNews = (id) => {
        fetch(`api/news?id=${id}`)
            .then(res => res.json())
            .then(news => {
                document.getElementById('news-id').value = news.id;
                document.getElementById('news-title').value = news.title;
                document.getElementById('news-summary').value = news.summary;
                document.getElementById('news-content').value = news.content;
                document.getElementById('news-image').value = news.imageUrl || '';
                
                document.getElementById('news-modal-title-text').textContent = 'Chỉnh sửa bài viết';
                newsModal.open();
            })
            .catch(err => {
                console.error(err);
                showToast('Không thể lấy thông tin chi tiết bài viết!', 'error');
            });
    };

    // Save News Form Submit
    document.getElementById('news-form').addEventListener('submit', (e) => {
        e.preventDefault();
        const id = document.getElementById('news-id').value;
        const title = document.getElementById('news-title').value.trim();
        const summary = document.getElementById('news-summary').value.trim();
        const content = document.getElementById('news-content').value.trim();
        const imageUrl = document.getElementById('news-image').value.trim();

        const payload = { title, summary, content, imageUrl };
        let method = 'POST';
        if (id) {
            payload.id = parseInt(id);
            method = 'PUT';
        }

        fetch('api/news', {
            method: method,
            headers: { 'Content-Type': 'application/json; charset=UTF-8' },
            body: JSON.stringify(payload)
        })
        .then(res => res.json())
        .then(data => {
            if (data.status === 'success') {
                showToast(data.message);
                newsModal.close();
                loadNewsList();
            } else {
                showToast(data.message, 'error');
            }
        })
        .catch(err => {
            console.error(err);
            showToast('Lỗi gửi dữ liệu lên máy chủ.', 'error');
        });
    });

    // Delete News
    const deleteNews = (id) => {
        if (!confirm('Bạn có chắc chắn muốn xóa bài viết này không?')) return;

        fetch(`api/news?id=${id}`, {
            method: 'DELETE'
        })
        .then(res => res.json())
        .then(data => {
            if (data.status === 'success') {
                showToast(data.message);
                loadNewsList();
            } else {
                showToast(data.message, 'error');
            }
        })
        .catch(err => {
            console.error(err);
            showToast('Lỗi gửi yêu cầu xóa.', 'error');
        });
    };

    // ==========================================
    // 9. MODULE: STORES MANAGEMENT (CRUD)
    // ==========================================
    let localStores = [];

    const loadStoresList = () => {
        const tbody = document.getElementById('stores-table-body');
        if (!tbody) return;

        tbody.innerHTML = '<tr><td colspan="5" style="text-align: center; color: var(--text-gray);"><i class="fa-solid fa-spinner fa-spin"></i> Đang tải dữ liệu cửa hàng...</td></tr>';

        fetch('api/stores')
            .then(res => res.json())
            .then(data => {
                localStores = data;
                renderStoresTable(localStores);
            })
            .catch(err => {
                console.error(err);
                tbody.innerHTML = '<tr><td colspan="5" style="text-align: center; color: #ef4444;">Lỗi tải dữ liệu.</td></tr>';
            });
    };

    const renderStoresTable = (storesArray) => {
        const tbody = document.getElementById('stores-table-body');
        if (storesArray.length === 0) {
            tbody.innerHTML = '<tr><td colspan="5" style="text-align: center; color: var(--text-gray);">Không tìm thấy cửa hàng nào.</td></tr>';
            return;
        }

        tbody.innerHTML = '';
        storesArray.forEach(store => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td><strong style="color: var(--text-white);">${store.name}</strong></td>
                <td>${store.address}</td>
                <td>${store.phone || 'Chưa cập nhật'}</td>
                <td>${store.openingHours || '09:00 - 22:00'}</td>
                <td>
                    <div class="actions-cell">
                        <button class="btn-icon btn-icon-edit" data-id="${store.id}" title="Sửa"><i class="fa-solid fa-pen-to-square"></i></button>
                        <button class="btn-icon btn-icon-delete" data-id="${store.id}" title="Xóa"><i class="fa-solid fa-trash"></i></button>
                    </div>
                </td>
            `;

            tr.querySelector('.btn-icon-edit').addEventListener('click', () => openEditStore(store));
            tr.querySelector('.btn-icon-delete').addEventListener('click', () => deleteStore(store.id));

            tbody.appendChild(tr);
        });
    };

    // Search Stores
    document.getElementById('stores-search').addEventListener('input', (e) => {
        const query = e.target.value.toLowerCase().trim();
        const filtered = localStores.filter(s => 
            s.name.toLowerCase().includes(query) || 
            s.address.toLowerCase().includes(query)
        );
        renderStoresTable(filtered);
    });

    // Add Store Modal opening
    document.getElementById('add-store-btn').addEventListener('click', () => {
        document.getElementById('store-id').value = '';
        document.getElementById('store-form').reset();
        document.getElementById('store-modal-title-text').textContent = 'Thêm cửa hàng mới';
        storeModal.open();
    });

    // Edit Store Modal opening
    const openEditStore = (store) => {
        document.getElementById('store-id').value = store.id;
        document.getElementById('store-name').value = store.name;
        document.getElementById('store-address').value = store.address;
        document.getElementById('store-phone').value = store.phone || '';
        document.getElementById('store-hours').value = store.openingHours || '09:00 - 22:00';
        document.getElementById('store-map').value = store.mapUrl || '';
        
        document.getElementById('store-modal-title-text').textContent = 'Chỉnh sửa thông tin cửa hàng';
        storeModal.open();
    };

    // Save Store Form Submit
    document.getElementById('store-form').addEventListener('submit', (e) => {
        e.preventDefault();
        const id = document.getElementById('store-id').value;
        const name = document.getElementById('store-name').value.trim();
        const address = document.getElementById('store-address').value.trim();
        const phone = document.getElementById('store-phone').value.trim();
        const openingHours = document.getElementById('store-hours').value.trim();
        const mapUrl = document.getElementById('store-map').value.trim();

        const payload = { name, address, phone, openingHours, mapUrl };
        let method = 'POST';
        if (id) {
            payload.id = parseInt(id);
            method = 'PUT';
        }

        fetch('api/stores', {
            method: method,
            headers: { 'Content-Type': 'application/json; charset=UTF-8' },
            body: JSON.stringify(payload)
        })
        .then(res => res.json())
        .then(data => {
            if (data.status === 'success') {
                showToast(data.message);
                storeModal.close();
                loadStoresList();
            } else {
                showToast(data.message, 'error');
            }
        })
        .catch(err => {
            console.error(err);
            showToast('Lỗi gửi dữ liệu lên máy chủ.', 'error');
        });
    });

    // Delete Store
    const deleteStore = (id) => {
        if (!confirm('Bạn có chắc chắn muốn xóa cửa hàng này khỏi hệ thống không?')) return;

        fetch(`api/stores?id=${id}`, {
            method: 'DELETE'
        })
        .then(res => res.json())
        .then(data => {
            if (data.status === 'success') {
                showToast(data.message);
                loadStoresList();
            } else {
                showToast(data.message, 'error');
            }
        })
        .catch(err => {
            console.error(err);
            showToast('Lỗi gửi yêu cầu xóa.', 'error');
        });
    };

    // ==========================================
    // 10. MODULE: JOBS MANAGEMENT (CRUD)
    // ==========================================
    let localJobs = [];

    const loadJobsList = () => {
        const tbody = document.getElementById('jobs-table-body');
        if (!tbody) return;

        tbody.innerHTML = '<tr><td colspan="6" style="text-align: center; color: var(--text-gray);"><i class="fa-solid fa-spinner fa-spin"></i> Đang tải dữ liệu tuyển dụng...</td></tr>';

        fetch('api/jobs')
            .then(res => res.json())
            .then(data => {
                localJobs = data;
                renderJobsTable(localJobs);
            })
            .catch(err => {
                console.error(err);
                tbody.innerHTML = '<tr><td colspan="6" style="text-align: center; color: #ef4444;">Lỗi tải dữ liệu.</td></tr>';
            });
    };

    const renderJobsTable = (jobsArray) => {
        const tbody = document.getElementById('jobs-table-body');
        if (jobsArray.length === 0) {
            tbody.innerHTML = '<tr><td colspan="6" style="text-align: center; color: var(--text-gray);">Không tìm thấy tin tuyển dụng nào.</td></tr>';
            return;
        }

        tbody.innerHTML = '';
        jobsArray.forEach(job => {
            const deadlineStr = job.deadline ? new Date(job.deadline).toLocaleDateString('vi-VN') : 'Chưa thiết lập';
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td><strong style="color: var(--text-white);">${job.title}</strong></td>
                <td><span class="role-badge role-staff">${job.department}</span></td>
                <td>${job.location}</td>
                <td>${job.salary || 'Thỏa thuận'}</td>
                <td>${deadlineStr}</td>
                <td>
                    <div class="actions-cell">
                        <button class="btn-icon btn-icon-edit" data-id="${job.id}" title="Sửa"><i class="fa-solid fa-pen-to-square"></i></button>
                        <button class="btn-icon btn-icon-delete" data-id="${job.id}" title="Xóa"><i class="fa-solid fa-trash"></i></button>
                    </div>
                </td>
            `;

            tr.querySelector('.btn-icon-edit').addEventListener('click', () => openEditJob(job.id));
            tr.querySelector('.btn-icon-delete').addEventListener('click', () => deleteJob(job.id));

            tbody.appendChild(tr);
        });
    };

    // Search Jobs
    document.getElementById('jobs-search').addEventListener('input', (e) => {
        const query = e.target.value.toLowerCase().trim();
        const filtered = localJobs.filter(j => 
            j.title.toLowerCase().includes(query) || 
            j.department.toLowerCase().includes(query) ||
            j.location.toLowerCase().includes(query)
        );
        renderJobsTable(filtered);
    });

    // Add Job Modal opening
    document.getElementById('add-job-btn').addEventListener('click', () => {
        document.getElementById('job-id').value = '';
        document.getElementById('job-form').reset();
        document.getElementById('job-modal-title-text').textContent = 'Đăng tin tuyển dụng mới';
        jobModal.open();
    });

    // Edit Job Modal opening
    const openEditJob = (id) => {
        fetch(`api/jobs?id=${id}`)
            .then(res => res.json())
            .then(job => {
                document.getElementById('job-id').value = job.id;
                document.getElementById('job-title').value = job.title;
                document.getElementById('job-dept').value = job.department;
                document.getElementById('job-loc').value = job.location;
                document.getElementById('job-salary').value = job.salary || 'Thỏa thuận';
                document.getElementById('job-desc').value = job.description;
                document.getElementById('job-reqs').value = job.requirements;
                
                // deadline format check (YYYY-MM-DD)
                if (job.deadline) {
                    const dateObj = new Date(job.deadline);
                    const formattedDate = dateObj.toISOString().split('T')[0];
                    document.getElementById('job-deadline').value = formattedDate;
                }
                
                document.getElementById('job-modal-title-text').textContent = 'Chỉnh sửa tin tuyển dụng';
                jobModal.open();
            })
            .catch(err => {
                console.error(err);
                showToast('Không thể lấy thông tin tuyển dụng!', 'error');
            });
    };

    // Save Job Form Submit
    document.getElementById('job-form').addEventListener('submit', (e) => {
        e.preventDefault();
        const id = document.getElementById('job-id').value;
        const title = document.getElementById('job-title').value.trim();
        const department = document.getElementById('job-dept').value;
        const location = document.getElementById('job-loc').value.trim();
        const salary = document.getElementById('job-salary').value.trim();
        const deadline = document.getElementById('job-deadline').value;
        const description = document.getElementById('job-desc').value.trim();
        const requirements = document.getElementById('job-reqs').value.trim();

        const payload = { title, department, location, salary, deadline, description, requirements };
        let method = 'POST';
        if (id) {
            payload.id = parseInt(id);
            method = 'PUT';
        }

        fetch('api/jobs', {
            method: method,
            headers: { 'Content-Type': 'application/json; charset=UTF-8' },
            body: JSON.stringify(payload)
        })
        .then(res => res.json())
        .then(data => {
            if (data.status === 'success') {
                showToast(data.message);
                jobModal.close();
                loadJobsList();
            } else {
                showToast(data.message, 'error');
            }
        })
        .catch(err => {
            console.error(err);
            showToast('Lỗi gửi dữ liệu tuyển dụng.', 'error');
        });
    });

    // Delete Job
    const deleteJob = (id) => {
        if (!confirm('Bạn có chắc chắn muốn xóa tin tuyển dụng này?')) return;

        fetch(`api/jobs?id=${id}`, {
            method: 'DELETE'
        })
        .then(res => res.json())
        .then(data => {
            if (data.status === 'success') {
                showToast(data.message);
                loadJobsList();
            } else {
                showToast(data.message, 'error');
            }
        })
        .catch(err => {
            console.error(err);
            showToast('Lỗi gửi yêu cầu xóa tin.', 'error');
        });
    };

    // ==========================================
    // 11. MODULE: CONTACT MESSAGES
    // ==========================================
    let localContacts = [];

    const loadContactsList = () => {
        const tbody = document.getElementById('contacts-table-body');
        if (!tbody) return;

        tbody.innerHTML = '<tr><td colspan="6" style="text-align: center; color: var(--text-gray);"><i class="fa-solid fa-spinner fa-spin"></i> Đang tải dữ liệu liên hệ...</td></tr>';

        fetch('api/contacts')
            .then(res => res.json())
            .then(data => {
                localContacts = data;
                renderContactsTable(localContacts);
                // Also update overview counters
                loadOverviewStats();
            })
            .catch(err => {
                console.error(err);
                tbody.innerHTML = '<tr><td colspan="6" style="text-align: center; color: #ef4444;">Lỗi tải dữ liệu.</td></tr>';
            });
    };

    const renderContactsTable = (contactsArray) => {
        const tbody = document.getElementById('contacts-table-body');
        if (contactsArray.length === 0) {
            tbody.innerHTML = '<tr><td colspan="6" style="text-align: center; color: var(--text-gray);">Không tìm thấy tin nhắn liên hệ nào.</td></tr>';
            return;
        }

        tbody.innerHTML = '';
        contactsArray.forEach(msg => {
            const dateStr = msg.createdAt ? new Date(msg.createdAt).toLocaleString('vi-VN') : '---';
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td style="text-align: center;"><span class="status-badge ${msg.isRead ? 'status-read' : 'status-unread'}"></span></td>
                <td><strong>${esc(msg.name)}</strong></td>
                <td><span style="font-size: 0.8rem; color: var(--text-gray);">${esc(msg.email)}<br>${esc(msg.phone || '')}</span></td>
                <td><span style="font-weight: ${msg.isRead ? '500' : '700'}; color: ${msg.isRead ? 'var(--text-gray)' : 'var(--text-white)'}">${esc(msg.subject)}</span></td>
                <td><span style="font-size: 0.8rem;">${dateStr}</span></td>
                <td>
                    <div class="actions-cell">
                        <button class="btn-icon btn-icon-view" title="Xem chi tiết"><i class="fa-solid fa-eye"></i></button>
                        <button class="btn-icon btn-icon-delete" title="Xóa"><i class="fa-solid fa-trash"></i></button>
                    </div>
                </td>
            `;

            tr.querySelector('.btn-icon-view').addEventListener('click', () => openViewContact(msg));
            tr.querySelector('.btn-icon-delete').addEventListener('click', () => deleteContact(msg.id));

            tbody.appendChild(tr);
        });
    };

    // Search Contacts
    document.getElementById('contacts-search').addEventListener('input', (e) => {
        const query = e.target.value.toLowerCase().trim();
        const filtered = localContacts.filter(c => 
            c.name.toLowerCase().includes(query) || 
            c.email.toLowerCase().includes(query) || 
            c.subject.toLowerCase().includes(query) ||
            c.message.toLowerCase().includes(query)
        );
        renderContactsTable(filtered);
    });

    // View contact details modal
    const openViewContact = (msg) => {
        document.getElementById('msg-name').textContent = msg.name;
        document.getElementById('msg-email').textContent = msg.email;
        document.getElementById('msg-phone').textContent = msg.phone || 'Chưa cập nhật';
        document.getElementById('msg-date').textContent = msg.createdAt ? new Date(msg.createdAt).toLocaleString('vi-VN') : '---';
        document.getElementById('msg-subject').textContent = msg.subject;
        document.getElementById('msg-text').textContent = msg.message;

        const markReadBtn = document.getElementById('mark-read-btn');
        
        if (msg.isRead) {
            markReadBtn.style.display = 'none';
        } else {
            markReadBtn.style.display = 'inline-block';
            // Attach event to mark as read
            markReadBtn.onclick = () => {
                fetch(`api/contacts?id=${msg.id}`, {
                    method: 'PUT'
                })
                .then(res => res.json())
                .then(data => {
                    if (data.status === 'success') {
                        showToast(data.message);
                        contactModal.close();
                        loadContactsList();
                    }
                })
                .catch(err => console.error('Error marking as read:', err));
            };
        }

        contactModal.open();
    };

    // Delete contact inquiry
    const deleteContact = (id) => {
        if (!confirm('Bạn có chắc chắn muốn xóa thư liên hệ này?')) return;

        fetch(`api/contacts?id=${id}`, {
            method: 'DELETE'
        })
        .then(res => res.json())
        .then(data => {
            if (data.status === 'success') {
                showToast(data.message);
                loadContactsList();
            } else {
                showToast(data.message, 'error');
            }
        })
        .catch(err => {
            console.error(err);
            showToast('Lỗi gửi yêu cầu xóa liên hệ.', 'error');
        });
    };

    // ==========================================
    // 12. MODULE: USERS ACCOUNTS (ADMIN ONLY)
    // ==========================================
    let localUsers = [];

    const loadUsersList = () => {
        const tbody = document.getElementById('users-table-body');
        if (!tbody) return;

        tbody.innerHTML = '<tr><td colspan="5" style="text-align: center; color: var(--text-gray);"><i class="fa-solid fa-spinner fa-spin"></i> Đang tải dữ liệu thành viên...</td></tr>';

        fetch('api/admin')
            .then(res => {
                if (res.status === 403) {
                    throw new Error('Bạn không có quyền quản lý thành viên.');
                }
                return res.json();
            })
            .then(data => {
                localUsers = data;
                renderUsersTable(localUsers);
            })
            .catch(err => {
                console.error(err);
                tbody.innerHTML = `<tr><td colspan="5" style="text-align: center; color: #ef4444;">${err.message || 'Lỗi tải dữ liệu.'}</td></tr>`;
            });
    };

    const renderUsersTable = (usersArray) => {
        const tbody = document.getElementById('users-table-body');
        if (usersArray.length === 0) {
            tbody.innerHTML = '<tr><td colspan="5" style="text-align: center; color: var(--text-gray);">Không tìm thấy người dùng nào.</td></tr>';
            return;
        }

        tbody.innerHTML = '';
        usersArray.forEach(user => {
            let roleClass = 'role-customer';
            let roleName = 'Customer';
            if (user.role === 'ADMIN') {
                roleClass = 'role-admin';
                roleName = 'Administrator';
            } else if (user.role === 'STAFF') {
                roleClass = 'role-staff';
                roleName = 'Staff';
            }

            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td><strong style="color: var(--text-white);">${esc(user.name)}</strong></td>
                <td>${esc(user.phone)}</td>
                <td>${esc(user.email)}</td>
                <td><span class="role-badge ${roleClass}">${roleName}</span></td>
                <td>
                    <div class="actions-cell">
                        <button class="btn-icon btn-icon-edit" title="Sửa"><i class="fa-solid fa-pen-to-square"></i></button>
                        <button class="btn-icon btn-icon-delete" title="Xóa" ${currentUser && currentUser.email === user.email ? 'disabled style="opacity: 0.3; cursor: not-allowed;"' : ''}><i class="fa-solid fa-trash"></i></button>
                    </div>
                </td>
            `;

            tr.querySelector('.btn-icon-edit').addEventListener('click', () => openEditUser(user));
            tr.querySelector('.btn-icon-delete').addEventListener('click', () => {
                if (currentUser && currentUser.email === user.email) return;
                deleteUser(user.email);
            });

            tbody.appendChild(tr);
        });
    };

    // Search Users
    document.getElementById('users-search').addEventListener('input', (e) => {
        const query = e.target.value.toLowerCase().trim();
        const filtered = localUsers.filter(u => 
            u.name.toLowerCase().includes(query) || 
            u.email.toLowerCase().includes(query) || 
            u.phone.includes(query) ||
            u.role.toLowerCase().includes(query)
        );
        renderUsersTable(filtered);
    });

    // Cấp quyền NHÂN VIÊN cho tài khoản có sẵn bằng email
    const promoteBtn = document.getElementById('promote-staff-btn');
    if (promoteBtn) {
        promoteBtn.addEventListener('click', () => {
            const emailInput = document.getElementById('promote-email');
            const email = emailInput.value.trim();
            if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
                showToast('Vui lòng nhập email hợp lệ.', 'error');
                return;
            }
            fetch('api/admin', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json; charset=UTF-8' },
                body: JSON.stringify({ action: 'promoteStaff', email: email })
            })
            .then(res => res.json())
            .then(data => {
                if (data.status === 'success') {
                    showToast(data.message);
                    emailInput.value = '';
                    loadUsersList();
                } else {
                    showToast(data.message, 'error');
                }
            })
            .catch(err => { console.error(err); showToast('Lỗi gửi yêu cầu cấp quyền.', 'error'); });
        });
    }

    // Add User Modal opening
    document.getElementById('add-user-btn').addEventListener('click', () => {
        document.getElementById('user-is-edit').value = 'false';
        document.getElementById('user-old-email').value = '';
        document.getElementById('user-email').disabled = false;
        
        document.getElementById('user-password').required = true;
        document.getElementById('user-password-tip').style.display = 'none';
        document.getElementById('user-password-label').textContent = 'Mật khẩu *';
        
        document.getElementById('user-form').reset();
        document.getElementById('user-modal-title-text').textContent = 'Thạo tài khoản thành viên mới';
        userModal.open();
    });

    // Edit User Modal opening
    const openEditUser = (user) => {
        document.getElementById('user-is-edit').value = 'true';
        document.getElementById('user-old-email').value = user.email;
        
        document.getElementById('user-name').value = user.name;
        document.getElementById('user-phone').value = user.phone;
        document.getElementById('user-email').value = user.email;
        document.getElementById('user-role').value = user.role;
        
        // When editing, password is not required
        document.getElementById('user-password').value = '';
        document.getElementById('user-password').required = false;
        document.getElementById('user-password-tip').style.display = 'block';
        document.getElementById('user-password-label').textContent = 'Mật khẩu mới';
        
        document.getElementById('user-modal-title-text').textContent = 'Chỉnh sửa thông tin tài khoản';
        userModal.open();
    };

    // Save User Form Submit
    document.getElementById('user-form').addEventListener('submit', (e) => {
        e.preventDefault();
        const isEdit = document.getElementById('user-is-edit').value === 'true';
        const oldEmail = document.getElementById('user-old-email').value;
        const name = document.getElementById('user-name').value.trim();
        const phone = document.getElementById('user-phone').value.trim();
        const email = document.getElementById('user-email').value.trim();
        const password = document.getElementById('user-password').value;
        const role = document.getElementById('user-role').value;

        // Validation
        if (!/^[0-9]{9,11}$/.test(phone)) {
            showToast('Số điện thoại không hợp lệ (9 - 11 chữ số).', 'error');
            return;
        }

        const payload = { name, phone, email, role };
        if (password) {
            payload.password = password;
        }
        
        let method = 'POST';
        if (isEdit) {
            payload.email = oldEmail; // Send original email in email field
            payload.newEmail = email; // Send updated email in newEmail field
            method = 'PUT';
        }

        fetch('api/admin', {
            method: method,
            headers: { 'Content-Type': 'application/json; charset=UTF-8' },
            body: JSON.stringify(payload)
        })
        .then(res => res.json())
        .then(data => {
            if (data.status === 'success') {
                showToast(data.message);
                userModal.close();
                loadUsersList();
                
                // If admin modified their own profile, refresh session
                if (currentUser && currentUser.email === oldEmail) {
                    verifySession();
                }
            } else {
                showToast(data.message, 'error');
            }
        })
        .catch(err => {
            console.error(err);
            showToast('Lỗi gửi dữ liệu tài khoản.', 'error');
        });
    });

    // Delete User
    const deleteUser = (email) => {
        if (!confirm(`Bạn có chắc chắn muốn xóa tài khoản "${email}" khỏi hệ thống?`)) return;

        fetch(`api/admin?email=${email}`, {
            method: 'DELETE'
        })
        .then(res => res.json())
        .then(data => {
            if (data.status === 'success') {
                showToast(data.message);
                loadUsersList();
            } else {
                showToast(data.message, 'error');
            }
        })
        .catch(err => {
            console.error(err);
            showToast('Lỗi gửi yêu cầu xóa tài khoản.', 'error');
        });
    };

    // ==========================================
    // 13. MODULE: ĐƠN HÀNG (ORDERS)
    // ==========================================
    let localOrders = [];
    const ORDER_STATUSES = ['Chờ xử lý', 'Đã xác nhận', 'Đang giao', 'Hoàn thành', 'Đã hủy'];
    const fmtVnd = (n) => new Intl.NumberFormat('vi-VN').format(n) + ' đ';

    const loadOrdersList = () => {
        const tbody = document.getElementById('orders-table-body');
        if (!tbody) return;
        tbody.innerHTML = '<tr><td colspan="9" style="text-align:center;color:var(--text-gray);"><i class="fa-solid fa-spinner fa-spin"></i> Đang tải đơn hàng...</td></tr>';
        fetch('api/orders')
            .then(res => { if (res.status === 403) throw new Error('Bạn không có quyền xem đơn hàng.'); return res.json(); })
            .then(data => { localOrders = data; renderOrdersTable(localOrders); })
            .catch(err => { tbody.innerHTML = `<tr><td colspan="9" style="text-align:center;color:#ef4444;">${err.message || 'Lỗi tải dữ liệu.'}</td></tr>`; });
    };

    const renderOrdersTable = (arr) => {
        const tbody = document.getElementById('orders-table-body');
        if (!tbody) return;
        if (!arr.length) {
            tbody.innerHTML = '<tr><td colspan="9" style="text-align:center;color:var(--text-gray);">Chưa có đơn hàng nào.</td></tr>';
            return;
        }
        tbody.innerHTML = '';
        arr.forEach(o => {
            const tr = document.createElement('tr');
            const opts = ORDER_STATUSES.map(s => `<option value="${s}" ${s === o.status ? 'selected' : ''}>${s}</option>`).join('');
            tr.innerHTML = `
                <td><strong style="color:var(--text-white);">#${o.id}</strong></td>
                <td>${esc(o.customerName)}</td>
                <td>${esc(o.phone)}</td>
                <td style="max-width:170px;">${esc(o.address)}</td>
                <td style="max-width:220px;">${esc(o.items)}${o.note ? `<br><em style="color:var(--text-gray);font-size:0.8rem;">Ghi chú: ${esc(o.note)}</em>` : ''}</td>
                <td style="color:var(--secondary);font-weight:700;white-space:nowrap;">${fmtVnd(o.total)}</td>
                <td>${esc(o.paymentMethod)}</td>
                <td><select class="order-status-select" data-id="${o.id}" style="padding:6px 8px;border-radius:6px;background:rgba(0,0,0,0.25);color:#fff;border:1px solid rgba(255,255,255,0.15);">${opts}</select></td>
                <td style="white-space:nowrap;">${o.createdAt}</td>
            `;
            tr.querySelector('.order-status-select').addEventListener('change', (e) => updateOrderStatus(o.id, e.target.value));
            tbody.appendChild(tr);
        });
    };

    const updateOrderStatus = (id, status) => {
        fetch('api/orders', {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json; charset=UTF-8' },
            body: JSON.stringify({ id: id, status: status })
        })
        .then(res => res.json())
        .then(data => { showToast(data.message, data.status === 'success' ? 'success' : 'error'); })
        .catch(() => showToast('Lỗi cập nhật trạng thái đơn hàng.', 'error'));
    };

    const ordersSearch = document.getElementById('orders-search');
    if (ordersSearch) {
        ordersSearch.addEventListener('input', (e) => {
            const q = e.target.value.toLowerCase().trim();
            renderOrdersTable(localOrders.filter(o =>
                (o.customerName || '').toLowerCase().includes(q) ||
                (o.phone || '').includes(q) ||
                (o.status || '').toLowerCase().includes(q)
            ));
        });
    }
});
