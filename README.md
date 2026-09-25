# FinanceApp

A small banking web app built as a three-person course project at KTH Royal Institute of Technology.
Users log in, deposit and withdraw money, and admins can view every transaction.

**Stack:** Java 17 · Spring Boot 4 · Spring Security · Spring Data JPA · MySQL · vanilla JavaScript · JUnit 5 · Mockito · H2 (tests) · GitHub Actions

## Features

- Login with HTTP Basic authentication. Passwords are stored as **BCrypt hashes**.
- Deposits and withdrawals on the logged-in user's own account, with a full transaction history.
- Role-based access control: only `ADMIN` users can list all transactions.
- Validation of amounts: positive, at most two decimals, at most 1,000,000 per transaction.

## Design notes

- **No account IDs in URLs.** Every account endpoint acts on the authenticated user (`/api/me/...`),
  so a user cannot read or change another user's account by editing a URL.
- **Race-free balance updates.** Balances are changed with a single conditional SQL `UPDATE`
  (`... SET balance = balance - :amount WHERE id = :id AND balance >= :amount`) instead of
  read-modify-write in Java. Two concurrent withdrawals therefore cannot both spend the same money.
  A `CHECK (balance >= 0)` constraint in the schema is a second line of defence.
- **Exact money arithmetic.** Amounts are `BigDecimal` in Java and `DECIMAL(15, 2)` in MySQL, never floating point.
- **Atomic history.** Each balance change and its transaction record are written in the same database transaction.
- **No password hashes in responses.** The API returns DTOs (`AccountResponse`, `TransactionResponse`), never JPA entities.

## Running locally

Requires Java 17+ and Docker.

```sh
docker compose up -d          # MySQL 8.4 on localhost:3306
./mvnw spring-boot:run        # API and frontend on http://localhost:8080
```

To use your own MySQL server instead, set `DB_URL`, `DB_USERNAME` and `DB_PASSWORD`.
Note that the schema is recreated on every start (demo mode).

Demo accounts (password `demo123`):

| User    | Role  | Starting balance |
|---------|-------|------------------|
| Darin   | USER  | 100.00           |
| William | ADMIN | 500.00           |
| Mikael  | USER  | 50,000.00        |

## API

All `/api` endpoints require HTTP Basic authentication.

| Method | Path                      | Description                                  |
|--------|---------------------------|----------------------------------------------|
| GET    | `/api/me`                 | The logged-in user's account                 |
| POST   | `/api/me/deposit`         | Deposit, body `{"amount": 100.00}`           |
| POST   | `/api/me/withdraw`        | Withdraw, body `{"amount": 50.00}`; `409` if balance is too low |
| GET    | `/api/admin/transactions` | All transactions, newest first (ADMIN only)  |

Invalid amounts return `400` with `{"error": "..."}`.

## Tests

```sh
./mvnw test
```

The tests need no database server: they run against an in-memory H2 database in MySQL mode.

- `UserServiceTest`: unit tests of the business rules, with Mockito.
- `BankApiIntegrationTest`: the whole application through HTTP, covering authentication, authorization, validation and persistence.
- `ConcurrentWithdrawalTest`: 20 simultaneous withdrawals of 10 from an account holding 100. Exactly 10 must succeed and the balance must end at 0.

## Team

Darin Abdullah, Mikael Blome and William Ngoka.
