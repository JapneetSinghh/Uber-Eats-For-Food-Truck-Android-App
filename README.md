# 🚚 **Food Truck Locator App**

A seamless platform for discovering, tracking, and enjoying local food trucks with real-time updates. This app bridges the gap between food enthusiasts and food truck owners, ensuring an engaging and personalized experience for everyone.

---

## 🌟 **Features**

### **For Customers**
- **🌍 Easy Access to Diverse Foods:** Discover food trucks offering a wide variety of cuisines, from Italian and Indian to Mexican.
- **🔎 Improved Search & Filters:** Advanced filtering options to locate trucks based on cuisine, dietary needs, or location.
- **⭐ Reviews & Ratings:** Access customer reviews and ratings to make informed choices.
- **📍 Real-Time Updates:** Stay updated with live truck locations, special offers, and changes in hours of operation.
- **🚗 Google Maps Integration:** Navigate effortlessly to your favorite food trucks.
- **❤️ Personalized Recommendations:** Enjoy ML-based recommendations tailored to your preferences.

### **For Food Truck Owners**
- **👤 Profile Management:** Manage your truck’s profile, including menus, operating hours, and specialties.
- **💸 Discount Management:** Create and control custom promotions and discounts.
- **📍 Real-Time Location Updates:** Update your truck’s live location for customers.
- **🎟️ QR Code Integration:** Generate and share QR codes for marketing purposes.
- **📊 Customer Engagement:** Respond to reviews and gain insights into customer preferences.
- **📈 Analytics:** Track customer interactions and adapt your offerings accordingly.

---

## 🛠️ **Tech Stack**
- **Frontend:** Android Studio, Java, XML
- **Backend:** Node.js, Firebase
- **Database:** Firebase Realtime Database
- **Storage:** Firebase Storage for image uploads
- **APIs:** Google Maps API for real-time location tracking
- **Additional Tools:** UML diagrams for app design and architecture

---

## 🔒 **Key Features and Integrations**

- **Firebase Integration for Secure and Real-Time Data Management:**
  - Leveraged Firebase for real-time database storage and dynamic updates to food truck listings and user activities.
  - Enabled seamless synchronization of data, including user reviews, ratings, and food truck details.
  - Implemented image upload functionality for food truck images using Firebase Storage.

- **Robust Authentication System:**
  - Secure login and registration process.
  - "Forget Password" feature that sends recovery links via email for enhanced user experience and security.

---

## 🏗️ **App Architecture**
The app follows the **MVVM (Model-View-ViewModel)** architecture for efficient code management and scalability:
1. **Model:** Manages data and business logic.
2. **View:** Responsible for displaying data to users.
3. **ViewModel:** Serves as a bridge between the View and Model, handling LiveData updates.

---

## 🖼️ **Screenshots**

### **Home Screen**
The home screen displays nearby food trucks with real-time updates on their locations and offers.

![Home Screen](screenshots/home_screen.png)

### **Search Screen**
Search and filter food trucks by cuisine, dietary needs, or location.

![Search Screen](screenshots/search_screen.png)

### **Details Screen**
View food truck details, including menus, reviews, and live location.

![Details Screen](screenshots/details_screen.png)

---

## 🚀 **Getting Started**

### **Prerequisites**
- Android Studio installed on your machine.
- A Firebase project with necessary configurations.
- Google Maps API key for real-time location tracking.

### **Installation**
1. Clone the repository:
   ```bash
   git clone https://github.com/japneetsinghh/food-truck-locator.git
