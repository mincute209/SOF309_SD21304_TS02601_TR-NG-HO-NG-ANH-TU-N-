/**
 * Jolliebear - Trang tài khoản khách hàng (profile.jsp)
 * Xem thông tin + lịch sử đơn hàng + hủy đơn (khi còn "Chờ xử lý")
 */
document.addEventListener('DOMContentLoaded', () => {
    const guestBox = document.getElementById('profile-guest');
    const contentBox = document.getElementById('profile-content');
    const ordersBox = document.getElementById('profile-orders');
    if (!guestBox || !contentBox) return; // không phải trang profile

    const formatVnd = (n) => new Intl.NumberFormat('vi-VN').format(n) + ' đ';

    const toast = (msg, type = 'success') => {
        const c = document.getElementById('toast-container');
        if (!c) { alert(msg); return; }
        const t = document.createElement('div');
        t.className = `toast toast-${type}`;
        let icon = '<i class="fa-solid fa-circle-check"></i>';
        if (type === 'error') icon = '<i class="fa-solid fa-circle-exclamation"></i>';
        else if (type === 'warning') icon = '<i class="fa-solid fa-triangle-exclamation"></i>';
        t.innerHTML = `${icon}<div class="toast-content">${msg}</div><button class="toast-close-btn">&times;</button>`;
        c.appendChild(t);
        setTimeout(() => t.classList.add('show'), 10);
        const au = setTimeout(() => t.remove(), 4000);
        t.querySelector('.toast-close-btn').addEventListener('click', () => { clearTimeout(au); t.remove(); });
    };

    const statusClass = (s) => {
        if (s === 'Đã xác nhận') return 'st-xacnhan';
        if (s === 'Đang giao') return 'st-giao';
        if (s === 'Hoàn thành') return 'st-hoan';
        if (s === 'Đã hủy') return 'st-huy';
        return 'st-cho';
    };

    const loadOrders = (silent) => {
        if (!silent) {
            ordersBox.innerHTML = '<div style="text-align:center;color:var(--text-muted);padding:30px;"><i class="fa-solid fa-spinner fa-spin fa-2x"></i></div>';
        }
        return fetch('api/orders?mine=1')
            .then(res => res.json())
            .then(orders => {
                if (!Array.isArray(orders) || orders.length === 0) {
                    ordersBox.innerHTML = '<div class="cart-empty" style="padding:50px 20px;"><i class="fa-solid fa-basket-shopping"></i><h3>Chưa có đơn hàng nào</h3><p>Hãy khám phá thực đơn và đặt món đầu tiên nhé!</p><a href="menu.jsp" class="btn btn-primary">Xem thực đơn</a></div>';
                    return;
                }
                ordersBox.innerHTML = '';
                orders.forEach(o => {
                    const canCancel = (o.status === 'Chờ xử lý' || o.status === 'Đã xác nhận');
                    const card = document.createElement('div');
                    card.className = 'porder-card';
                    card.innerHTML = `
                        <div class="porder-head">
                            <div>
                                <span class="porder-id">Đơn #${o.id}</span>
                                <span class="porder-date"> · ${o.createdAt}</span>
                            </div>
                            <span class="porder-status ${statusClass(o.status)}">${o.status}</span>
                        </div>
                        <div class="porder-items"><i class="fa-solid fa-utensils" style="color:var(--secondary);margin-right:6px;"></i>${window.escapeHtml(o.items)}</div>
                        ${o.note ? `<div class="porder-note">Ghi chú: ${window.escapeHtml(o.note)}</div>` : ''}
                        <div class="porder-foot">
                            <div>
                                <span class="porder-total">${formatVnd(o.total)}</span>
                                <span class="porder-pay"> · ${window.escapeHtml(o.paymentMethod)}</span>
                            </div>
                            ${canCancel ? `<button class="porder-cancel-btn" data-id="${o.id}"><i class="fa-solid fa-xmark"></i> Hủy đơn</button>` : ''}
                        </div>
                    `;
                    const cancelBtn = card.querySelector('.porder-cancel-btn');
                    if (cancelBtn) cancelBtn.addEventListener('click', () => cancelOrder(o.id));
                    ordersBox.appendChild(card);
                });
            })
            .catch(() => {
                // Khi tự làm mới (silent) mà lỗi mạng tạm thời -> giữ nguyên danh sách cũ
                if (!silent) {
                    ordersBox.innerHTML = '<div style="text-align:center;color:#ef4444;padding:30px;">Lỗi tải đơn hàng. Vui lòng thử lại.</div>';
                }
            });
    };

    const cancelOrder = (id) => {
        if (!confirm(`Bạn có chắc muốn hủy đơn hàng #${id}?`)) return;
        fetch('api/orders', {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json; charset=UTF-8' },
            body: JSON.stringify({ id: id, status: 'Đã hủy' })
        })
        .then(res => res.json())
        .then(data => {
            if (data.status === 'success') {
                toast(data.message, 'warning');
                loadOrders();
            } else {
                toast(data.message || 'Không thể hủy đơn.', 'error');
            }
        })
        .catch(() => toast('Lỗi kết nối máy chủ.', 'error'));
    };

    // Kiểm tra phiên đăng nhập
    fetch('api/session')
        .then(res => res.json())
        .then(data => {
            if (data.status === 'success' && data.user) {
                guestBox.style.display = 'none';
                contentBox.style.display = 'block';
                document.getElementById('pf-name').textContent = data.user.name || '—';
                document.getElementById('pf-username').textContent = data.user.username || '—';
                document.getElementById('pf-email').textContent = data.user.email || '—';
                document.getElementById('pf-phone').textContent = data.user.phone || '—';
                loadOrders();

                // Nút "Làm mới" (làm mới im lặng + hiệu ứng xoay icon)
                const refreshBtn = document.getElementById('orders-refresh-btn');
                if (refreshBtn) {
                    refreshBtn.addEventListener('click', () => {
                        const icon = refreshBtn.querySelector('i');
                        if (icon) icon.classList.add('fa-spin');
                        Promise.resolve(loadOrders(true)).finally(() => {
                            if (icon) setTimeout(() => icon.classList.remove('fa-spin'), 500);
                        });
                    });
                }

                // Tự động cập nhật mỗi 15 giây (tạm dừng khi tab đang ẩn để đỡ tốn tài nguyên)
                setInterval(() => {
                    if (document.visibilityState === 'visible') loadOrders(true);
                }, 15000);
            } else {
                guestBox.style.display = 'block';
                contentBox.style.display = 'none';
            }
        })
        .catch(() => {
            guestBox.style.display = 'block';
            contentBox.style.display = 'none';
        });

    // Nút đăng nhập -> mở modal đăng nhập ở header
    const loginBtn = document.getElementById('profile-login-btn');
    if (loginBtn) {
        loginBtn.addEventListener('click', () => {
            const trigger = document.getElementById('login-trigger');
            if (trigger) trigger.click();
        });
    }
});
