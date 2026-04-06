# Clinic Appointment System (CAS)
## JavaFX Desktop App — Eclipse IDE Setup Guide

### Requirements
- Eclipse IDE (2021-09 or later recommended)
- Eclipse Adoptium JDK 17 (already configured per PRD)
- JavaFX SDK 17+ (download separately — see Step 2)

---

## Step 1 — Download JavaFX SDK

1. Go to: https://gluonhq.com/products/javafx/
2. Download **JavaFX SDK 17** (Windows, x64)
3. Unzip to a folder, e.g.: `C:\javafx-sdk-17\`

---

## Step 2 — Import Project into Eclipse

1. Open Eclipse → **File → Import → General → Existing Projects into Workspace**
2. Set Root Directory to the `CAS` folder
3. Click **Finish**

---

## Step 3 — Add JavaFX to the Build Path

1. Right-click project → **Build Path → Configure Build Path**
2. Click **Libraries → Add External JARs...**
3. Navigate to `C:\javafx-sdk-17\lib\` and select **all** `.jar` files
4. Click **Apply and Close**

---

## Step 4 — Configure Run/Debug Settings (VM Arguments)

1. Right-click `MainApp.java` → **Run As → Run Configurations...**
2. Go to the **Arguments** tab
3. In **VM arguments**, paste:

```
--module-path "C:\javafx-sdk-17\lib" --add-modules javafx.controls,javafx.fxml
```

> Replace `C:\javafx-sdk-17\lib` with your actual JavaFX SDK lib path.

4. Click **Apply → Run**

---

## Project Structure

```
CAS/
├── src/
│   └── cas/
│       ├── Person.java          ← Abstract base class
│       ├── Patient.java         ← Extends Person (patientId, age)
│       ├── Doctor.java          ← Extends Person (doctorId, specialization)
│       ├── Appointment.java     ← Appointment record + CSV helpers
│       ├── Pair.java            ← Generic Pair<K,V> class
│       ├── ClinicService.java   ← Backend logic (thread-safe, file I/O)
│       └── MainApp.java         ← JavaFX UI (main entry point)
├── patients.txt                 ← Auto-created on first run
├── doctors.txt                  ← Auto-created on first run
├── appointments.txt             ← Auto-created on first run
└── README.md
```

---

## Features

| Feature | Implementation |
|---|---|
| Patient Management | `ArrayList<Patient>` with auto-ID generation |
| Doctor Management | `ArrayList<Doctor>` with auto-ID generation |
| Appointment Booking | `HashMap<String, Pair<String,String>>` prevents duplicates |
| Thread Safety | `synchronized` on `bookAppointment()` |
| Multithreading Demo | Two threads compete for the same slot |
| File Persistence | Plain text files loaded on startup, saved on every change |
| OOP Design | Inheritance: `Person → Patient`, `Person → Doctor` |
| Generics | `Pair<K,V>` class used in appointment mapping |

---

## Data File Formats

**patients.txt**
```
P001,John Smith,35
P002,Alice Brown,28
```

**doctors.txt**
```
D001,Michael Lee,Cardiology
D002,Sarah Khan,General
```

**appointments.txt**
```
D001,John Smith,10:00 AM
D002,Alice Brown,Slot-3
```
