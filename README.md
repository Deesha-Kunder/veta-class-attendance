# VETA Class Attendance

A facial recognition-based attendance and course-hour management system designed for institutes that offer flexible, hour-based learning programs. The application automates attendance tracking, course-hour calculation, and learning material management while providing dedicated dashboards for both students and administrators.

---

## Problem Statement

Many language institutes and training centers operate on an hour-based learning model where students are assigned a fixed number of course hours and can attend classes at flexible times. Attendance is often managed manually by recording entry time, exit time, and hours attended in physical registers.

---

## Solution

VETA Class Attendance digitizes the entire workflow through facial recognition and automated attendance tracking.

Students perform facial recognition at entry and exit. The system records entry time when entering the classroom and exit time when leaving, automatically calculating the total duration attended and deducting it from their allocated course hours. The platform also provides access to learning materials, attendance reports, and course progress analytics.

---

## Features

### Student Module
- Secure Sign In and Sign Up
- Password setup and management
- Profile management
- Access PDF learning materials uploaded by admin
- First-time face registration
- Update registered face data
- Face-based entry and exit attendance
- Automatic calculation of attendance duration
- View attendance history
- Monitor total completed hours
- Track remaining course hours
- View entry and exit time records
- 7-day attendance analytics graph

---

### Admin Module
- Register new students
- Assign course hours
- Assign batches and Manage joining dates
- View and manage student records
- View all registered students
- Access individual student attendance records
- Monitor completed and remaining course hours
- Upload PDF learning materials

## Attendance Workflow

1. Admin registers a student and assigns course hours.
2. Student signs in to the application.
3. Student registers facial data.
4. Student scans their face when entering the class.
5. Student scans their face again while leaving.
6. The system calculates attendance duration automatically.
7. Attended hours are deducted from allocated course hours.
8. Reports and analytics are updated in real time.

---

## Tech Stack

### Frontend

* Kotlin
* Jetpack Compose
* Android Studio
* Material Design 3

### AI & Face Recognition

- ML Kit Face Detection
- MobileFaceNet (TensorFlow Lite) for face recognition

### Backend

* Java
* Spring Boot
* REST APIs

### Database

* Supabase (PostgreSQL)

### Deployment

- Docker (Backend Containerization)
- Render (Spring Boot Backend Hosting)

### Libraries & Tools

* Navigation Compose
* MPAndroidChart
* Kotlin Coroutines
* ViewModel
* StateFlow

---

## Key Features

✅ Face Recognition Attendance System

✅ Automated Entry & Exit Tracking

✅ Hour-Based Course Management

✅ Real-Time Attendance Reports

✅ Learning Material Distribution

✅ Student & Admin Dashboards

✅ Attendance Analytics & Visualization

✅ Role-Based Authentication

---
