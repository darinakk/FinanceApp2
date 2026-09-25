// Frontend och API körs på samma server (Spring Boot serverar mappen static), så relativa URL:er räcker
let authHeader = null;
let currentUser = null;

// Koppla knappar
document.getElementById('loginBtn').addEventListener('click', handleLogin);
document.getElementById('depositBtn').addEventListener('click', () => executeTransaction('deposit'));
document.getElementById('withdrawBtn').addEventListener('click', () => executeTransaction('withdraw'));
document.getElementById('logBtn').addEventListener('click', loadLogs);

// HTTP Basic-header. TextEncoder gör att å, ä och ö i lösenord fungerar.
function basicAuth(username, password) {
    const bytes = new TextEncoder().encode(`${username}:${password}`);
    return 'Basic ' + btoa(String.fromCharCode(...bytes));
}

// Alla API-anrop skickar med inloggningsuppgifterna
function api(path, options = {}) {
    return fetch(path, { ...options, headers: { ...options.headers, Authorization: authHeader } });
}

async function handleLogin() {
    const userVal = document.getElementById('username').value;
    const passVal = document.getElementById('password').value;
    authHeader = basicAuth(userVal, passVal);

    try {
        const response = await api('/api/me');

        if (response.ok) {
            currentUser = await response.json();
            document.getElementById('password').value = '';
            showDashboard();
        } else {
            authHeader = null;
            showMessage(response.status === 401 ? "Fel användarnamn eller lösenord" : "Inloggning misslyckades", false);
        }
    } catch (error) {
        authHeader = null;
        showMessage("Kunde inte ansluta till servern", false);
    }
}

function showDashboard() {
    document.getElementById('loginPage').style.display = 'none';
    document.getElementById('dashboardPage').style.display = 'block';
    document.getElementById('welcomeText').innerText = `Välkommen ${currentUser.name}!`;
    updateBalance();

    // Visa admin-verktyg om rollen matchar (servern kontrollerar också rollen)
    if (currentUser.role === 'ADMIN') {
        document.getElementById('adminArea').style.display = 'block';
    }
}

function updateBalance() {
    document.getElementById('balanceText').innerText = Number(currentUser.balance).toFixed(2);
}

async function executeTransaction(type) {
    const amount = Number(document.getElementById('amount').value);
    if (!amount || amount <= 0) return showMessage("Ange ett belopp större än 0", false);

    try {
        const response = await api(`/api/me/${type}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ amount })
        });
        const data = await response.json().catch(() => ({}));

        if (response.ok) {
            // Servern svarar med det uppdaterade kontot
            currentUser = data;
            updateBalance();
            showMessage(type === 'deposit' ? `Insättning på ${amount} kr lyckades!` : `Uttag på ${amount} kr lyckades!`, true);
        } else {
            showMessage(data.error ?? "Transaktionen misslyckades", false);
        }
    } catch (error) {
        showMessage("Ett fel uppstod vid transaktionen", false);
    }
}

async function loadLogs() {
    try {
        const res = await api('/api/admin/transactions');
        if (!res.ok) return showMessage("Kunde inte hämta loggar", false);
        const logs = await res.json();
        const container = document.getElementById('logContent');

        // textContent istället för innerHTML så att inget från servern tolkas som HTML
        container.replaceChildren(...logs.map(l => {
            const row = document.createElement('div');
            row.style.cssText = 'border-bottom: 1px solid #eee; padding: 2px;';
            row.textContent = `${l.createdAt.replace('T', ' ').slice(0, 16)} | ID:${l.userId} | ${l.type} | ${l.amount} kr`;
            return row;
        }));
    } catch (error) {
        showMessage("Kunde inte hämta loggar", false);
    }
}

function showMessage(text, isSuccess) {
    const msgDiv = document.getElementById('message');
    msgDiv.innerText = text;
    msgDiv.style.display = 'block';
    msgDiv.style.backgroundColor = isSuccess ? "#d4edda" : "#f8d7da";
    msgDiv.style.color = isSuccess ? "#155724" : "#721c24";
}
