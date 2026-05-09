# 🚖 YatriMitra – Smart Shared Auto Tracking System

## 📌 Overview
YatriMitra is an Android application that helps users track shared autos in real-time. It provides route visualization, live vehicle simulation, and Estimated Time of Arrival (ETA) to improve daily commuting.

---

## 🎯 Problem Statement
In many towns and rural areas, passengers face uncertainty in shared transportation due to lack of tracking systems. This leads to increased waiting time and inefficient travel planning.

---

## 💡 Solution
YatriMitra solves this problem by:
- Displaying routes on a map
- Simulating live vehicle movement
- Providing real-time ETA updates

---

## 🚀 Features
- 🗺️ Route visualization using OpenStreetMap (OSM)
- 🚗 Live vehicle tracking (simulation)
- ⏱️ Dynamic ETA calculation
- 🔄 Real-time updates using StateFlow
- 📱 Built with Jetpack Compose
- 🔁 Handles screen rotation without resetting simulation

---

## 🛠️ Tech Stack
- **Language:** Kotlin  
- **UI:** Jetpack Compose  
- **Map:** OSMDroid (OpenStreetMap)  
- **Networking:** Retrofit  
- **State Management:** ViewModel + StateFlow  

---

## 📂 Project Structure
com.example.yatrimitra
│
├── model # Data models
├── network # API services (OSRM)
├── utils # Helper functions (polyline decoding)
├── viewmodel # Business logic (TrackingViewModel)
├── screens # UI screens (Compose)
└── components # Map components (OSMMapView)

---

## 🔄 How It Works
1. User selects a route  
2. Route coordinates are fetched  
3. Route is displayed on the map  
4. Vehicle moves along the route (simulation)  
5. Progress updates continuously  
6. ETA is calculated and displayed  

---

## 📊 Core Logic

### ETA Formula:
ETA = Distance / Speed

### Simulation:
Progress (0 → 1) → Route Index → Vehicle Position

---

## 🌐 Live Demo
https://appetize.io/app/b_vbwq4glbrfppklwcszhkdlln6q

---

## 🧠 Future Enhancements
- Real-time GPS tracking
- Multiple vehicle tracking
- Push notifications
- Booking system
- Driver-side application

---

## 📈 Impact
- Reduces waiting time
- Improves transport efficiency
- Useful for rural and semi-urban areas

---

## 📸 Screenshots
<img width="621" height="1271" alt="Screenshot 2026-05-08 at 8 26 23 PM" src="https://github.com/user-attachments/assets/eea855d5-1c64-4c96-b0d7-f87f77fce1c7" />
<img width="614" height="1242" alt="Screenshot 2026-05-08 at 8 27 02 PM" src="https://github.com/user-attachments/assets/92618551-d138-435b-a5bd-6af5f74db9dd" />
<img width="614" height="1267" alt="Screenshot 2026-05-08 at 8 27 14 PM" src="https://github.com/user-attachments/assets/ae7da22d-928f-4d30-b55d-97c878826cca" />
<img width="606" height="1262" alt="Screenshot 2026-05-08 at 8 27 21 PM" src="https://github.com/user-attachments/assets/c277568b-01d1-4d1b-b87f-b15a7b91066b" />
<img width="618" height="1259" alt="Screenshot 2026-05-08 at 8 27 50 PM" src="https://github.com/user-attachments/assets/36cc7bdd-32ef-4659-9eda-1783c6ebfacc" />
<img width="585" height="1253" alt="Screenshot 2026-05-08 at 8 28 07 PM" src="https://github.com/user-attachments/assets/4c4fa499-eb7d-4f6e-b4a7-290db633c1a9" />


