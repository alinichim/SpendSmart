# 💸 SpendSmart - Expense Tracking App

SpendSmart is a modern, intuitive Android application built to help users effortlessly track their daily expenses, manage budgets, and monitor currency exchange rates. The app is built entirely using **Kotlin** and **Jetpack Compose**, adhering strictly to Google's recommended Android Application Architecture.

---

## 📱 Project Requirements Checklist

This project was developed to meet the following core requirements:

- [x] **Kotlin Programming Language:** The entire codebase is written in Kotlin.
- [x] **Jetpack Compose:** 100% declarative UI using Jetpack Compose.
- [x] **Multiple Screens:** Includes a Dashboard, Add Expense, Expense Details, and Settings screen.
- [x] **Jetpack Navigation:** Seamless screen transitions handled via the Navigation Compose component.
- [x] **Application Architecture:** Follows the recommended MVVM (Model-View-ViewModel) architecture, with a clear separation of concerns (UI Layer, Domain Layer, Data Layer).
- [x] **API Integration:** Integrates with an online API using **Retrofit** (e.g., [ExchangeRate-API](https://www.exchangerate-api.com/) to fetch real-time currency conversion rates, or a local mock server for cloud-syncing expenses).

---

## 🌟 Bonus Points Implemented

- [x] **Database (0.5 pts):** Uses **Room Database** (an SQLite abstraction) to persist local expense entries.
- [x] **Input Sanitization (0.5 pts):** Uses Room's built-in parameterized queries to completely prevent SQLite injection. Additionally, UI inputs (like amount and category) are sanitized and validated using Regex before being passed to the data layer.
- [x] **Encrypted Communication (0.5 pts):** - Network traffic is secured via HTTPS/TLS.
    - Uses the `EncryptedSharedPreferences` library to securely store sensitive user preferences (like API keys or authentication tokens).
    - *Optional: The "notes" field in the database is encrypted using AES before storage.*
- [x] **Settings Screen (0.5 pts):** A dedicated Settings screen allowing users to toggle Dynamic Colors (Dark/Light mode) and change the app's display language.
- [x] **Unit Testing (0.5 pts):** Includes unit tests using **JUnit4** and **MockK** to verify the ViewModel logic, API repository responses, and data sanitization functions.
- [x] **Code Readability & Clean Code (0.5 pts):** The codebase follows Clean Architecture principles, SOLID principles, uses dependency injection (e.g., Hilt/Dagger), and is modularized logically into `ui`, `data`, `domain`, and `di` packages.

---

## 🚀 Features

* **Dashboard:** View a summary of total expenses, recent transactions, and a visual breakdown of spending categories.
* **Add Expense:** Quickly log new expenses with amount, category, date, and optional notes.
* **Currency Converter:** Check live exchange rates for international expenses (powered by Retrofit).
* **Offline Support:** All expenses are saved locally in the Room Database, so the app works perfectly offline.

---

## 🏗️ Architecture & Tech Stack

* **Language:** Kotlin
* **UI:** Jetpack Compose, Material Design 3
* **Navigation:** Jetpack Navigation Compose
* **Architecture:** MVVM + Clean Architecture
* **Dependency Injection:** Dagger Hilt
* **Network:** Retrofit, OkHttp, Moshi/Gson
* **Local Storage:** Room Database, EncryptedSharedPreferences
* **Concurrency:** Kotlin Coroutines & Flow
* **Testing:** JUnit4, MockK, Coroutines Test

---

## 📂 Folder Structure

```text
com.example.spendsmart
│
├── di/                 # Dagger Hilt Modules
├── data/               # Data Layer (Room DB, Retrofit API, Repositories)
│   ├── local/          # Room Entities, DAOs
│   ├── remote/         # Retrofit Interfaces, DTOs
│   └── repository/     # Repository Implementations
│
├── domain/             # Domain Layer (Models, Use Cases, Repository Interfaces)
│
└── ui/                 # UI Layer (Jetpack Compose)
    ├── navigation/     # NavHost, Routes
    ├── screens/        # Dashboard, AddExpense, Settings, etc.
    └── theme/          # Color, Type, Shape