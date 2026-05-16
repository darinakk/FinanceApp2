const API_URL = "http://localhost:8080";
let currentUser = null;

// Koppla knappar
document.getElementById('loginBtn').addEventListener('click', handleLogin);
document.getElementById('depositBtn').addEventListener('click', () => executeTransaction('add'));
document.getElementById('withdrawBtn').addEventListener('click', () => executeTransaction('withdraw'));
document.getElementById('logBtn').addEventListener('click', loadLogs);

async function handleLogin() {
    const userVal = document.getElementById('username').value;
    const passVal = document.getElementById('password').value;

    try {
        const response = await fetch(`${API_URL}/api/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username: userVal, password: passVal })
        });

        if (response.ok) {
            const user = await response.json();
            if (user) {
                currentUser = user;
                showDashboard();
            } else {
                showMessage("Fel användarnamn eller lösenord", false);
            }
        } else {
            showMessage("Inloggning misslyckades", false);
        }
    } catch (error) {
        showMessage("Kunde inte ansluta till servern", false);
    }
}

function showDashboard() {
    document.getElementById('loginPage').style.display = 'none';
    document.getElementById('dashboardPage').style.display = 'block';
    document.getElementById('welcomeText').innerText = `Välkommen ${currentUser.name}!`;
    document.getElementById('balanceText').innerText = currentUser.balance.toFixed(2);

    // Visa admin-verktyg om rollen matchar
    if (currentUser.role === 'ADMIN') {
        document.getElementById('adminArea').style.display = 'block';
    }
}

async function executeTransaction(type) {
    const amount = document.getElementById('amount').value;
    if (!amount || amount <= 0) return showMessage("Ange ett belopp", false);

    try {
        const response = await fetch(`${API_URL}/users/${currentUser.id}/${type}?amount=${amount}`, {
            method: 'POST'
        });
        const msg = await response.text();
        showMessage(msg, response.ok);

        if (response.ok) {
            // Uppdatera saldo på skärmen genom att hämta användaren på nytt
            const res = await fetch(`${API_URL}/users/${currentUser.id}`);
            currentUser = await res.json();
            document.getElementById('balanceText').innerText = currentUser.balance.toFixed(2);
        }
    } catch (error) {
        showMessage("Ett fel uppstod vid transaktionen", false);
    }
}

async function loadLogs() {
    try {
        const res = await fetch(`${API_URL}/admin/transactions`);
        const logs = await res.json();
        const container = document.getElementById('logContent');

        container.innerHTML = logs.map(l =>
            `<div style="border-bottom: 1px solid #eee; padding: 2px;">
                ID:${l.userId} | ${l.type} | ${l.amount}kr
            </div>`
        ).join('');
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