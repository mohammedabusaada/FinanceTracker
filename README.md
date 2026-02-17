# FinanceTracker  

**FinanceTracker** is an Android application designed to help users manage their personal finances efficiently. This version has been significantly upgraded from simple storage to a robust architecture featuring **Firebase Authentication**, **Local SQLite Storage**, and **Real-time API Integration**.

The app allows users to:  
- Add income and expense transactions  
- View a list of all transactions  
- See details of each transaction  
- Delete transactions  
- View statistics of transactions in the **StatisticsFragment** (new feature)  

---

## 🚀 New & Core Features
- **Secure Authentication:** Real-time user login and registration using **Firebase Auth**.
- **Data Persistence:** All financial records are stored locally in an **SQLite Database**, ensuring data privacy and offline access.
- **User-Specific Data:** Each user sees only their own data, filtered by their unique Firebase UID.
- **Visual Analytics:** Interactive **PieChart** (using MPAndroidChart) in the Statistics Fragment to visualize income vs. expenses.
- **Currency Exchange Rates:** Real-time currency data fetched from an external API via **Retrofit**.
- **Transaction Management:** Full CRUD operations (Create, Read, Delete) for daily transactions.

---

## 🛠️ Technologies & Libraries Used
- **Language:** Java
- **Backend/Auth:** [Firebase Authentication](https://firebase.google.com/docs/auth)
- **Local Database:** [SQLite](https://developer.android.com/training/data-storage/sqlite)
- **Networking:** [Retrofit 2](https://square.github.io/retrofit/) & [GSON](https://github.com/google/gson) (API consumption)
- **Charts:** [MPAndroidChart](https://github.com/PhilJay/MPAndroidChart)
- **UI Components:** Material Design, RecyclerView, Fragments, Edge-to-Edge UI.

---

## 🏗️ Architecture Flow
1. **Auth Layer:** Firebase verifies the user identity.
2. **Data Layer:** SQLite handles local storage using the `DatabaseHelper` (Prepared Statements for security).
3. **Network Layer:** Retrofit fetches live exchange rates in a background thread (`enqueue`).
4. **Presentation Layer:** Data is displayed using RecyclerViews and dynamic Fragments.

---

## 👤 Created by  
Mohammed AbuSaada  

---

## 📺 Video Demos  

### Part 1  
[![Watch on Google Drive](https://img.shields.io/badge/Watch%20Part%201%20on-Google%20Drive-34A853?logo=google-drive&logoColor=white)](https://drive.google.com/file/d/14-mXjS5OGmYKUBE78VSeLt9a5gdEt8Iz/view?usp=drivesdk)  

### Part 2  
[![Watch on Google Drive](https://img.shields.io/badge/Watch%20Part%202%20on-Google%20Drive-34A853?logo=google-drive&logoColor=white)](https://drive.google.com/file/d/19zTTquxMrGPMBPDhMw0eKVTQVP8RxTiE/view?usp=drivesdk)

### Part 3 (Latest Update: SQLite, Retrofit, Firebase & API)
[![Watch on YouTube](https://img.shields.io/badge/Watch%20Part%203%20on-YouTube-FF0000?logo=youtube&logoColor=white)](https://youtu.be/yEJhjod6d_I)
