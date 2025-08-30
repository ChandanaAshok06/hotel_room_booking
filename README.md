# 🏨 Hotel Booking System

This repository contains the implementation of a **Hotel Room Booking System** using **Java** and **MySQL**.  
The system allows customers to register, log in, and book rooms, while managers can log in to manage bookings and oversee hotel operations.  

This project is designed as a **console-based backend system (no frontend yet)**. It focuses on handling database operations, authentication, and booking workflows.  
A **MySQL database** is used to persist all the data, and **JDBC** is used for connecting Java with the database.  

---

## 📌 Features

### Customer Functionalities
- Create an account (signup).
- Login using email and password.
- View available rooms.
- Book a room.
- View booking history.
- Cancel a booking.
- Checkout (complete the stay).

### Manager Functionalities
- Login using manager credentials.
- View all customers and bookings.
- Add new rooms to the system.
- Remove/disable rooms.
- Monitor customer checkouts.
- Oversee the entire booking flow.

---

## 🛠️ Tech Stack

- **Java (Core Java + JDBC)** – Business logic and database connectivity  
- **MySQL** – Database storage for users, rooms, and bookings  
- **GitHub** – Version control and collaboration  

---

## 📂 Project Structure

```

HotelBookingSystem/
│── src/com/hotelbooking/        # Java backend source files
│── database/hotel\_booking.sql   # Database schema & sample data
│── README.md                    # Documentation

````

---

## ⚙️ Prerequisites

Before running this project, ensure you have the following installed:

- [Java JDK 8+](https://www.oracle.com/java/technologies/javase-downloads.html)  
- [MySQL Server](https://dev.mysql.com/downloads/mysql/)  
- [MySQL Workbench or CLI](https://dev.mysql.com/downloads/workbench/)  
- An IDE (IntelliJ IDEA, Eclipse, or VS Code) or terminal for running Java programs  

---

## 🗄️ Database Setup

1. Open **MySQL Workbench** or terminal.  
2. Run the provided SQL script to set up the database:  

   ```bash
   mysql -u root -p < database/hotel_booking.sql
```
```

This will create a database named **hotel\_booking** with tables:

* `customers` – stores customer info (name, email, password, etc.)
* `managers` – stores manager accounts
* `rooms` – stores available rooms and their details
* `bookings` – stores booking details made by customers

---

## 🔌 Configure Database Connection

Update your **DBConnection.java** file (inside `src/com/hotelbooking/`) with your MySQL credentials:

```java
private static final String URL = "jdbc:mysql://localhost:3306/hotel_booking";
private static final String USER = "root";          // your MySQL username
private static final String PASSWORD = "yourpassword"; // your MySQL password
```

---

## ▶️ Running the Project

### Option 1: Run in IDE

1. Open the project in **IntelliJ IDEA / Eclipse / VS Code**.
2. Build and run the `Main.java` file.
3. The application will start in the console with a menu-driven interface.

### Option 2: Run from Command Line

```bash
cd src
javac com/hotelbooking/*.java
java com.hotelbooking.Main
```

---

## 👀 Usage Flow

### Example Customer Flow

1. Sign up with name, email, and password.
2. Log in with registered credentials.
3. View available rooms.
4. Book a room.
5. Check booking history.
6. Cancel or checkout when needed.

### Example Manager Flow

1. Log in with manager credentials.
2. Add rooms to the system.
3. View all customers and their bookings.
4. Monitor active bookings and checkouts.
5. Remove or disable rooms if needed.

---

## 📌 Notes

* This repo contains **only the backend implementation (no frontend yet)**.
* Database schema and queries are provided in `database/hotel_booking.sql`.
* Use your own MySQL credentials in the `DBConnection.java` file.
* The project is **menu-driven** and runs entirely in the **console**.

---

## 👨‍💻 Authors

* CHANDANA A
* CHAITHRA M

---

## 📜 License

This project is created for **educational purposes**.
Feel free to fork, modify, and extend it.
 
