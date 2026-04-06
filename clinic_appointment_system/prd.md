# Product Requirements Document (PRD)
## Clinic Appointment System (JavaFX Mini Project)

---

## 1. Overview
The Clinic Appointment System is a desktop-based application developed using Java and JavaFX. It streamlines basic clinic operations such as managing patients, doctors, and appointment bookings through a simple graphical interface.

The system demonstrates core programming concepts including Object-Oriented Programming (OOP), multithreading, generics, collections, and file handling.

---

## 2. Objectives
- Provide a simple interface to manage clinic appointments
- Enable concurrent booking simulation using multithreading
- Apply OOP principles like inheritance and encapsulation
- Use Java collections for efficient data storage
- Implement basic file persistence with minimal complexity

---

## 3. Scope

### In Scope
- Add and view patients
- Add and view doctors
- Book appointments
- Display appointment records
- Store and retrieve data from file

### Out of Scope
- Database integration
- Authentication system
- Advanced UI/UX design
- Real-time notifications

---

## 4. Functional Requirements

### 4.1 Patient Management
- Add new patient:
  - Patient ID
  - Name
  - Age
- View all patients

---

### 4.2 Doctor Management
- Add new doctor:
  - Doctor ID
  - Name
  - Specialization
- View all doctors

---

### 4.3 Appointment Booking
- Book appointment using:
  - Patient
  - Doctor
  - Time slot (string or integer)
- Store appointment mapping:
  - Pair<DoctorID, PatientName>
- Prevent duplicate booking of same slot

---

### 4.4 Appointment Display
- Show all appointments
- Optional: Filter by doctor

---

### 4.5 File Handling
- Save data to file (text-based)
- Load data on application start
- Suggested format:
  doctorId,patientName,slot
- Keep implementation simple (no serialization required)

---

### 4.6 Multithreading
- Simulate multiple patients booking simultaneously
- Use threads to attempt booking same doctor/slot
- Ensure thread safety using synchronization or locks

---

## 5. Non-Functional Requirements
- Simple and clean UI (JavaFX only)
- No heavy styling or animations
- Fast response time
- Code readability and modularity

---

## 6. System Design

### 6.1 Class Design

#### Base Class: Person
- Attributes:
  - name
- Methods:
  - getters/setters

---

#### Patient (inherits Person)
- Attributes:
  - patientId
  - age

---

#### Doctor (inherits Person)
- Attributes:
  - doctorId
  - specialization

---

#### Appointment
- Attributes:
  - doctorId
  - patientName
  - slot

---

#### Generic Class: Pair<K, V>
- Used for mapping:
  - doctorId → patientName

---

### 6.2 Collections Used
- ArrayList<Patient> → store patients
- ArrayList<Doctor> → store doctors
- HashMap<String, Pair<String, String>> → appointment mapping

---

## 7. GUI Design (JavaFX Only)

### Screens

#### Main Menu
- Buttons:
  - Add Patient
  - Add Doctor
  - Book Appointment
  - View Appointments

---

#### Add Patient Screen
- Input fields:
  - Name
  - Age
- Button:
  - Save

---

#### Add Doctor Screen
- Input fields:
  - Name
  - Specialization
- Button:
  - Save

---

#### Book Appointment Screen
- Dropdowns:
  - Select Patient
  - Select Doctor
- Input:
  - Slot
- Button:
  - Book

---

#### View Appointments Screen
- Table/List view:
  - Doctor ID
  - Patient Name
  - Slot

---

## 8. Constraints
- Must use JavaFX (no Swing)
- Keep UI minimal and functional
- Avoid complex frameworks or libraries
- Use simple text-based file handling

---

## 9. Risks / Weak Points
- Overcomplicating UI instead of focusing on logic
- Missing thread safety leading to duplicate bookings
- Poor class design affecting OOP evaluation
- Overengineering file handling

---

## 10. Development Plan
1. Create core classes (Person, Patient, Doctor, Appointment)
2. Implement collections (ArrayList, HashMap)
3. Add basic file read/write
4. Implement booking logic (without threads first)
5. Add multithreading with synchronization
6. Build JavaFX UI (minimal design)
7. Connect UI with backend logic
8. Test edge cases:
   - Duplicate slot booking
   - Invalid inputs
   - File loading

---

## 11. Future Scope
- Database integration (MySQL)
- Online appointment booking
- Doctor availability calendar
- User authentication system

## 12. USE ON:
- We need to run this on eclipse ide,
- My active JDK is Eclipse Adoptium JDK 17 (OpenJDK) 
- so dont put anything that doesnt run on eclipse