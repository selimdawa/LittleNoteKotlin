<h1 align="center">Little Note - Kotlin</h1>

<p align="center">
 <a><img alt="Min SDK" src="https://img.shields.io/badge/Min SDK-24-020290?logo=android&logoColor=white"/></a>
 <a><img alt="Target SDK" src="https://img.shields.io/badge/Target SDK-37-0EB265?logo=android&logoColor=0EB265"/></a>
 <a href="https://kotlinlang.org"><img alt="Kotlin" src="https://img.shields.io/badge/Kotlin-2.4.20-blue?logo=kotlin&logoColor=white"/></a>
 <a href="https://opensource.org/licenses/MIT"><img alt="License" src="https://img.shields.io/badge/License-MIT-CC9900?logo=MIT&logoColor=white"/></a>
</p>

<p align="center">
  <strong>Little Note</strong> is a highly secure, feature-rich Android application designed for streamlined personal note management. Built with modern Kotlin standards, it offers seamless cloud synchronization, biometric protection, and an intuitive user interface.
</p>

---

## ðŸ“± Screenshots

### Day Mode
Splash | Save Note | Login 
--- | --- | --- | 
![](UI/1.jpg) | ![](UI/2.jpg) | ![](UI/3.jpg) | 

Register | Forget Pass | My Notes
--- | --- | --- | 
![](UI/4.jpg) | ![](UI/5.jpg) | ![](UI/6.jpg) | 

Add/Edit Note | Details | Login Note 
--- | --- | --- | 
![](UI/7.jpg) | ![](UI/8.jpg) | ![](UI/9.jpg) | 

## Night Mode
Splash | Save Note | Login 
--- | --- | --- | 
![](UI/10.jpg) | ![](UI/11.jpg) | ![](UI/12.jpg) | 

Register | Forget Pass | My Notes
--- | --- | --- | 
![](UI/13.jpg) | ![](UI/14.jpg) | ![](UI/15.jpg) | 

Add/Edit Note | Details | Login Note 
--- | --- | --- | 
![](UI/16.jpg) | ![](UI/17.jpg) | ![](UI/18.jpg) | 

---

### âœ¨ Core Functionalities
*   **Robust Authentication Flow**: Secure identity management via **Firebase Authentication**.
*   **Dynamic Note Orchestration**: Full CRUD operations for managing personal records through a refined interface.
*   **Granular Privacy Controls**: Secure sensitive content using specialized **Private Note** lockers.
*   **Advanced Biometric Integration**: Rapid user access leveraging native hardware security.
*   **Elastic Cloud Synchronization**: Distributed data persistence via **Firebase Firestore**.
*   **Context-Aware Theming**: Adaptive UI support for dark and light modes.
*   **Resilient Offline Capabilities**: "Local-first" data strategy using **Room Database**.
*   **Intelligent Background Operations**: Decoupled task execution managed by **WorkManager**.

---

## ðŸ› ï¸ Built With
*   **Language:** [Kotlin](https://kotlinlang.org/)
*   **UI Framework:** Material Design 3, ViewBinding
*   **Database:** [Room](https://developer.android.com/training/data-storage/room)
*   **Cloud Infrastructure:** [Firebase](https://firebase.google.com/) (Auth, Firestore)
*   **Dependency Injection:** [Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
*   **Navigation:** [Jetpack Navigation](https://developer.android.com/guide/navigation)
*   **Async Operations:** Coroutines & Flow
*   **Logging:** [Timber](https://github.com/JakeWharton/timber)
*   **Background Tasks:** [WorkManager](https://developer.android.com/topic/libraries/architecture/workmanager)

---

### ðŸ—ï¸ Technical Architecture
The application is engineered using **MVVM (Model-View-ViewModel)** architectural patterns, strictly adhering to **Clean Architecture** principles to promote modularity and ease of maintenance.

```text
app/src/main/java/com/flatcode/littlenote/
â”œâ”€â”€ di/                 # Dependency Injection (Hilt modules)
â”œâ”€â”€ ui/                 # Presentation Layer
â”‚   â”œâ”€â”€ auth/           # Authentication (Login, Register, Forget Password)
â”‚   â”œâ”€â”€ home/           # Dashboard and Navigation
â”‚   â”œâ”€â”€ note/           # Note Operations (Add, Edit, View, Details)
â”‚   â”œâ”€â”€ splash/         # App Entry Point
â”‚   â””â”€â”€ adapter/        # RecyclerView Adapters
â”œâ”€â”€ viewmodel/          # Logic Layer (State Management & UI Logic)
â”œâ”€â”€ data/               # Data Layer (Local & Remote)
â”‚   â”œâ”€â”€ dao/            # Room Database DAOs
â”‚   â”œâ”€â”€ model/          # Data Entities & Models
â”‚   â”œâ”€â”€ repository/     # Repository Pattern Implementation
â”‚   â”œâ”€â”€ database/       # Room Database Configuration
â”‚   â””â”€â”€ sync/           # Firebase Sync Logic
â”œâ”€â”€ functions/          # Specialized Workflows (AppFunctions)
â””â”€â”€ utils/              # Extensions, Constants & Utility Classes
```

---

## ðŸš€ Getting Started

### Prerequisites
*   Android Studio Ladybug or newer.
*   JDK 21.
*   Android SDK Level 37 (Compile SDK).

### Installation
1.  Clone the repository:
    ```bash
    git clone https://github.com/selimdawa/LittleNoteKotlin.git
    ```
2.  Open the project in Android Studio.
3.  Sync Project with Gradle Files.
4.  Add your `google-services.json` file to the `app/` directory.
5.  Run the app on your device or emulator.

---

### ðŸ› ï¸ Technology Stack
*   **Language**: Kotlin (Modern, expressive, and safe development).
*   **Dependency Injection**: **Dagger Hilt** (Simplified, standard DI for Android).
*   **Cloud Infrastructure**: **Firebase** (Scalable authentication and real-time NoSQL storage).
*   **Local Persistence**: **Room** (SQLite abstraction layer for robust offline data).
*   **Navigation**: **Jetpack Navigation** (Type-safe and standardized screen transitions).
*   **Concurrency**: **Coroutines & Flow** (High-performance asynchronous data streams).
*   **View Interop**: **ViewBinding** (Safe and efficient UI component interaction).
*   **Observability**: **Timber** (Extensible logging for enhanced diagnostic capabilities).
*   **Background Tasks**: **WorkManager** (Reliable execution of deferred operations).
*   **Security**: **Biometric API** (Native hardware-backed authentication).

---

## ðŸ”— Links & Resources
*   **Compose Version:** [Little Note Compose](https://github.com/selimdawa/LittleNoteCompose/)
*   **Legacy Versions:**
    *   [Kotlin Old Code Version](https://github.com/selimdawa/LittleNoteKotlinOld/)
    *   [Java Old Code Version](https://github.com/selimdawa/LittleNote/)
*   **Author:** [Selim Dawa](https://github.com/selimdawa)

---

## ðŸ“œ License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

