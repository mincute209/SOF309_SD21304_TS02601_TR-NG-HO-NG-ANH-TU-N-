/**
 * Jolliebear - Trang giỏ hàng (cart.jsp)
 * Phụ thuộc window.JBCart (định nghĩa trong main.js)
 */
document.addEventListener('DOMContentLoaded', () => {
    const listEl = document.getElementById('cart-items-list');
    // Chỉ chạy khi đang ở trang giỏ hàng
    if (!listEl) return;

    const emptyEl = document.getElementById('cart-empty');
    const contentEl = document.getElementById('cart-content');
    const subtotalEl = document.getElementById('cart-subtotal');
    const totalEl = document.getElementById('cart-total');
    const checkoutBtn = document.getElementById('cart-checkout-btn');
    const clearBtn = document.getElementById('cart-clear-btn');

    const formatPrice = (n) => new Intl.NumberFormat('vi-VN').format(n) + ' đ';

    const showToast = (message, type = 'success') => {
        const container = document.getElementById('toast-container');
        if (!container) { alert(message); return; }
        const toast = document.createElement('div');
        toast.className = `toast toast-${type}`;
        let icon = '<i class="fa-solid fa-circle-check"></i>';
        if (type === 'error') icon = '<i class="fa-solid fa-circle-exclamation"></i>';
        else if (type === 'warning') icon = '<i class="fa-solid fa-triangle-exclamation"></i>';
        toast.innerHTML = `${icon}<div class="toast-content">${message}</div><button class="toast-close-btn">&times;</button>`;
        container.appendChild(toast);
        setTimeout(() => toast.classList.add('show'), 10);
        const auto = setTimeout(() => toast.remove(), 4000);
        toast.querySelector('.toast-close-btn').addEventListener('click', () => { clearTimeout(auto); toast.remove(); });
    };

    const render = () => {
        const cart = window.JBCart.get();

        if (!cart.length) {
            emptyEl.style.display = 'block';
            contentEl.style.display = 'none';
            return;
        }

        emptyEl.style.display = 'none';
        contentEl.style.display = 'grid';

        listEl.innerHTML = '';
        cart.forEach(item => {
            const lineTotal = item.price * item.qty;
            const row = document.createElement('div');
            row.className = 'cart-item-row';
            row.innerHTML = `
                <div class="cart-item-info">
                    <div class="cart-item-img">
                        <img src="${item.image || 'https://images.unsplash.com/photo-1626645738196-c2a7c87a8f58?w=200'}" alt="${item.name}" onerror="this.src='https://images.unsplash.com/photo-1626645738196-c2a7c87a8f58?w=200'">
                    </div>
                    <div class="cart-item-name">
                        <h4>${item.name}</h4>
                        <span class="cart-item-unit">${formatPrice(item.price)}</span>
                    </div>
                </div>
                <div class="cart-item-qty">
                    <button class="qty-btn qty-minus" data-id="${item.id}" aria-label="Giảm">&minus;</button>
                    <span class="qty-value">${item.qty}</span>
                    <button class="qty-btn qty-plus" data-id="${item.id}" aria-label="Tăng">&plus;</button>
                </div>
                <div class="cart-item-total">${formatPrice(lineTotal)}</div>
                <button class="cart-item-remove" data-id="${item.id}" aria-label="Xóa món"><i class="fa-solid fa-xmark"></i></button>
            `;
            listEl.appendChild(row);
        });

        const total = window.JBCart.total();
        if (subtotalEl) subtotalEl.textContent = formatPrice(total);
        if (totalEl) totalEl.textContent = formatPrice(total);
    };

    // Tăng / giảm / xóa (event delegation)
    listEl.addEventListener('click', (e) => {
        const btn = e.target.closest('button');
        if (!btn) return;
        const id = btn.getAttribute('data-id');
        const item = window.JBCart.get().find(i => String(i.id) === String(id));
        if (!item) return;

        if (btn.classList.contains('qty-plus')) {
            window.JBCart.setQty(id, item.qty + 1);
        } else if (btn.classList.contains('qty-minus')) {
            window.JBCart.setQty(id, item.qty - 1);
        } else if (btn.classList.contains('cart-item-remove')) {
            window.JBCart.remove(id);
            showToast('Đã xóa món khỏi giỏ hàng.', 'warning');
        }
        render();
    });

    // Xóa toàn bộ
    if (clearBtn) {
        clearBtn.addEventListener('click', () => {
            if (!window.JBCart.get().length) return;
            if (confirm('Bạn có chắc muốn xóa toàn bộ giỏ hàng?')) {
                window.JBCart.clear();
                render();
                showToast('Đã xóa toàn bộ giỏ hàng.', 'warning');
            }
        });
    }

    // Đặt hàng -> mở modal checkout 2 bước (dùng chung với giỏ hàng drawer)
    if (checkoutBtn) {
        checkoutBtn.addEventListener('click', () => {
            if (!window.JBCart.get().length) {
                showToast('Giỏ hàng của bạn đang trống!', 'error');
                return;
            }
            if (window.JBCheckout && typeof window.JBCheckout.open === 'function') {
                window.JBCheckout.open();
            } else {
                showToast('Không mở được trang thanh toán. Vui lòng tải lại trang.', 'error');
            }
        });
    }

    // Cho phép checkout (trong main.js) làm mới trang giỏ sau khi đặt hàng
    window.JBCartPageRender = render;

    render();
});
