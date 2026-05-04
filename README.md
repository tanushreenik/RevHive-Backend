#  RevHive

A full-stack social platform with AI-powered features, premium subscriptions, and real-time interactions.

## 📌 Features

 🔐 Authentication (Login / Register / Forgot Password)
 👥 Follow system (only followers can chat)
 💬 Real-time chat (WebSocket based)
 🧠 AI Features:

  * Caption Generator
  * Hashtag Generator
  * Summarizer
  * Moderation
 💎 Premium System

  * Premium badge
  * Expiry handling
    
* 📊 Admin Dashboard
* 🎨 Modern Neon UI

---

## 🛠️ Tech Stack

### Frontend

* React.js
* Tailwind CSS
* Framer Motion

### Backend

* Spring Boot (Java)
* REST APIs
* WebSocket

### Database

* MySQL

### Others

* Firebase (File Uploads)
* AI APIs (for content generation)

---

## ⚙️ Setup Instructions

### 1. Clone Repository

```bash
git clone https://github.com/your-username/RevHive.git
cd RevHive
```

### 2. Backend Setup (Spring Boot)

```bash
cd backend
```

Configure `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/revhive
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
spring.jpa.hibernate.ddl-auto=update
```

Run backend:

```bash
mvn spring-boot:run
```

---

### 3. Frontend Setup (React)

```bash
cd frontend
npm install
npm run dev
```

---

## 🔐 Forgot Password Flow

1. User enters email
2. Backend sends reset link via email
3. User opens link → frontend reset page
4. User sets new password

---

## 🌐 API Base URL

```
http://localhost:8080/api
```

---

## 📂 Project Structure

```
RevHive/
│
├── backend/        # Spring Boot APIs
├── frontend/       # React UI
├── README.md
```

---

## 🚧 Future Improvements

* Notifications system
* Advanced analytics in admin dashboard
* AI-based recommendations
* Mobile app support

---

## 👨‍💻 Author

**Team RevHive**

---

## 📜 License

This project is for learning and development purposes.
