
# 🎥 Real-Time Donation Platform for Streamers

This project is a real-time donation platform built for streamers, allowing users to support their favorite creators through live donations that instantly appear during the stream. The platform includes secure user authentication, payment gateway integration, and a robust fee structure to ensure sustainability.

### 🔧 **Technologies Used:**

- **Backend Framework:** Spring Boot
- **Database:** PostgreSQL
- **Security:** JWT Authentication, OAuth2 (Telegram login)
- **Real-time Communication:** WebSocket
- **Testing:** JUnit (80% test coverage)
- **Payment Gateways:** Click, MirPay

---

## 🌟 **Key Features:**

### 1. **Real-Time Donations on Streams**  
Users can donate to their favorite streamers, and these donations are instantly displayed on the stream through WebSocket integration. 

### 2. **Secure User Authentication with JWT & OAuth2**  
User login and registration are secured using JWT tokens, with Telegram OAuth2 integrated for a seamless and secure login experience.

### 3. **Efficient Fee Management**  
The platform automatically deducts a fee during both donation transactions and withdrawals, ensuring a sustainable business model for the platform.

### 4. **Payment Gateway Integration**  
The system supports Click and MirPay payment gateways for processing donations and managing streamer withdrawals.

### 5. **Comprehensive Testing**  
With 80% test coverage using JUnit, the platform is built to ensure reliability and smooth functioning.

---

## 🔐 **Security**

- **JWT Authentication**: Secure authentication with JWT tokens for users and admins.
- **Role-Based Access**: Different access levels for users and admins, ensuring data privacy and security.

---

## 💳 **Payment Flow**

1. **User Donates**: A user selects a donation amount and completes payment through Click or MirPay.
2. **WebSocket Updates**: The donation is instantly reflected on the streamer’s live feed via WebSocket.
3. **Streamer Withdraws**: Streamers can request withdrawals, and a platform fee is applied during the transaction.

---

## 🧪 **Testing**

- **JUnit**: 80% code coverage ensures stability and prevents regressions.
- **Postman**: Used for API testing and ensuring correctness of the donation flow.

---

## 💼 **Future Enhancements**

- **Analytics Dashboard**: Provide streamers with detailed analytics on donations and audience engagement.
- **Subscription Model**: Introduce recurring donations and membership tiers for enhanced user engagement.
