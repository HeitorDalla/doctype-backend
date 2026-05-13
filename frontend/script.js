document.addEventListener('DOMContentLoaded', () => {
    setupNav();
    setupForms();
});

function setupNav() {
    const links = document.querySelectorAll('.sidebar nav a');
    links.forEach(link => {
        if (link.href === window.location.href) {
            link.classList.add('active');
        }
    });
}

function setupForms() {
    const loginForm = document.getElementById('loginForm');
    const registerForm = document.getElementById('registerForm');
    const logoutBtn = document.getElementById('logout');

    if (loginForm) {
        loginForm.addEventListener('submit', handleLogin);
    }

    if (registerForm) {
        registerForm.addEventListener('submit', handleRegister);
    }

    if (logoutBtn) {
        logoutBtn.addEventListener('click', handleLogout);
    }
}

function handleLogin(event) {
    event.preventDefault();
    window.location.href = 'perfil.html';
}

function handleRegister(event) {
    event.preventDefault();
    const senha = document.getElementById('senha').value;
    const confirmSenha = document.getElementById('confirmSenha').value;

    if (senha !== confirmSenha) {
        showMessage('As senhas não coincidem.');
        return;
    }

    showMessage('Registro realizado com sucesso! Redirecionando para login...');
    setTimeout(() => window.location.href = 'index.html', 1200);
}

function handleLogout() {
    window.location.href = 'index.html';
}

function showMessage(message) {
    const messageDiv = document.getElementById('message');
    if (messageDiv) {
        messageDiv.textContent = message;
    }
}
