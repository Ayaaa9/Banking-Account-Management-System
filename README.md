🏦 Banking Account Management System

A secure and modular banking web application built with Spring Boot, MySQL, and Bootstrap, designed to manage client accounts, handle transactions, and perform credit operations efficiently and safely.

🚀 Features

🔐 User Authentication & Authorization

Secure login system (Spring Security)

Role-based access (Admin / Client)

💰 Account Management

Create, update, and delete accounts

Real-time account balance tracking

💳 Transactions

Deposit, withdrawal, and transfer operations

Transaction history and statement view

🧾 Credit Operations

Loan management (apply, approve, repay)

Interest calculation & schedules

📊 Dashboard

Overview of balances, transactions, and credits

Responsive Bootstrap interface

🏗️ Architecture Overview
Banking-Account-Management-System/
│
├── src/
│   ├── main/
│   │   ├── java/com/banking/
│   │   │   ├── controller/     # REST controllers (AccountController, ClientController, etc.)
│   │   │   ├── entity/         # JPA entities (Account, Client, Transaction, Credit)
│   │   │   ├── repository/     # Spring Data JPA repositories
│   │   │   ├── service/        # Business logic & transaction management
│   │   │   └── security/       # Spring Security configuration
│   │   └── resources/
│   │       ├── application.properties  # Database configuration
│   │       └── templates/     # Thymeleaf views (if using web UI)
│   └── test/                  # Unit & integration tests
│
├── pom.xml                    # Maven dependencies
└── README.md

🧩 Technologies Used
Layer	Technologies
Backend	Spring Boot, Spring Security, Spring Data JPA
Database	MySQL
Frontend/UI	Bootstrap 5, Thymeleaf
Build Tool	Maven
Language	Java 17+

⚙️ Installation & Setup

1️⃣ Clone the repository
git clone https://github.com/Ayaaa9/Banking-Account-Management-System.git
cd Banking-Account-Management-System

2️⃣ Configure the database

Edit the file src/main/resources/application.properties:

spring.datasource.url=jdbc:mysql://localhost:3306/bankingdb
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
server.port=8080

3️⃣ Build & Run
mvn clean install
mvn spring-boot:run


Then open your browser at 👉 http://localhost:8080

👩‍💼 Roles & Permissions
Role	Access Rights
Admin	Manage clients, view all accounts, approve loans
Client	View own accounts, make transactions, apply for loans

📚 API Endpoints (if REST version)
Method	Endpoint	Description
GET	/api/accounts	Get all accounts
POST	/api/accounts	Create new account
GET	/api/accounts/{id}	Get account by ID
POST	/api/transactions/transfer	Perform transfer
GET	/api/transactions/history/{accountId}	Get transaction history

🧠 Example Use Case

The Admin creates client accounts.

The Client logs in, checks balances, and transfers money between accounts.

The System records all operations and updates the balances in real time.

Optional: Clients can apply for credits, which Admins can review and approve.


🌐 Future Improvements

✅ Add PDF export for account statements

✅ Integrate email notifications for transactions

🚀 Add RESTful API with JWT authentication

📱 React front-end for clients (mobile responsive dashboard)
