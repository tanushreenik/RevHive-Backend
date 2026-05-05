#  RevHive Backend

Backend service for **RevHive**, a social platform with authentication, real-time chat, AI-powered features, and premium subscriptions.

---

## 🛠️ Tech Stack

* **Java (Spring Boot)**
* **Spring Security (JWT Authentication)**
* **Spring Data JPA (Hibernate)**
* **WebSocket (Real-time Chat)**
* **MySQL (Database)**
* **JavaMailSender (Email Service)**

---

## 📌 Features

* 🔐 User Authentication (Login / Register)
* 🔑 Forgot Password (Email-based reset)
* 👥 Follow System (only followers can interact/chat)
* 💬 Real-time Chat using WebSockets
* 🧠 AI APIs integration (Caption, Hashtag, Summary)
* 💎 Premium System

  * Enable/Disable premium users
  * Premium expiry management
* 📊 Admin APIs
* 📧 Email Service (for password reset)

---

## ⚙️ Setup Instructions

### 1. Clone Repository

```bash
git clone https://github.com/your-username/RevHive.git
cd RevHive/backend
```

---

### 2. Configure Database

Update `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/revhive
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

---

### 3. Configure Email (Forgot Password)

```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=YOUR_EMAIL
spring.mail.password=YOUR_APP_PASSWORD
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

---

### 4. Run the Application

```bash
mvn spring-boot:run
```

Server runs on:

```
http://localhost:8080
```

---

## 🔐 Authentication APIs

| Method | Endpoint           | Description      |
| ------ | ------------------ | ---------------- |
| POST   | /api/auth/register | Register user    |
| POST   | /api/auth/login    | Login user       |
| POST   | /api/auth/forgot   | Send reset email |
| POST   | /api/auth/reset    | Reset password   |

---

## 👥 User & Follow APIs

| Method | Endpoint             | Description        |
| ------ | -------------------- | ------------------ |
| POST   | /api/follow/{userId} | Follow a user      |
| GET    | /api/followers       | Get followers list |
| GET    | /api/following       | Get following list |

---

## 💬 Chat (WebSocket)

* Endpoint:

```
ws://localhost:8080/ws
```

* Condition:
  Only users who follow each other can chat.

---

## 💎 Premium System

* `isPremium` → boolean flag
* `premiumExpiry` → expiry date

Example:

```java
user.setPremium(true);
user.setPremiumExpiry(LocalDate.now().plusMonths(1));
```

---

## 📧 Forgot Password Flow

1. User submits email
2. Backend generates token
3. Email sent with reset link
4. User resets password using token

---

## 📂 Project Structure

```
backend/
│
├── controller/     # REST Controllers
├── service/        # Business logic
├── repository/     # JPA Repositories
├── model/          # Entity classes
├── config/         # Security & WebSocket config
└── util/           # Helper classes
```

---

## 🚧 Future Improvements

* Rate limiting for APIs
* Notification system
* Better role-based access control
* Logging & monitoring

---

## 👨‍💻 Author

** Team RevHive **

---

## 📜 License

For learning and development purposes.
