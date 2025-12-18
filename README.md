# Contacts Book (FastAPI backend + Expo React Native frontend)

This repository contains a starter scaffold for a phone contacts book:
- Backend: FastAPI + PostgreSQL (Docker)
- Frontend: React Native (Expo) — initialize with `npx create-expo-app frontend`
- Run the backend in Docker and the frontend with Expo Go on your Android phone

Quick start
1. Start Postgres + backend with Docker Compose

```bash
docker compose up --build
```

The backend will be available on port `8000` on your machine.

2. Create the Expo app (run locally on your machine)

```bash
npx create-expo-app frontend
cd frontend
# Replace the default App.js with the example in this repo or copy the example App.js
npm install
npm start
```

3. Connect Expo Go on your Android phone
- Make sure your phone and computer are on the same Wi-Fi network.
- Find your computer LAN IP (macOS example):

```bash
ipconfig getifaddr en0
```

- Open `/Users/amin/Desktop/MyProjects/contacts_book/.env.example`, replace `MACHINE_IP` with your LAN IP (e.g. `192.168.1.42`) and use that as `API_BASE_URL` inside the Expo app.
- In the Expo dev tools, choose "Tunnel" or use the LAN QR code. Scan with Expo Go.

Notes
- The backend creates database tables automatically on startup.
- The backend allows CORS from anywhere for development; lock this down before production.

Running Tests
To run the backend tests with pytest:

```bash
cd backend
pip install -r requirements.txt
# Note: psycopg2-binary is commented out in requirements.txt for testing with SQLite.
# For production, uncomment and install: pip install psycopg2-binary
pytest
```

Files added
- Backend: [backend/main.py](/Users/amin/Desktop/MyProjects/contacts_book/backend/main.py)
- Backend: [backend/models.py](/Users/amin/Desktop/MyProjects/contacts_book/backend/models.py)
- Backend: [backend/schemas.py](/Users/amin/Desktop/MyProjects/contacts_book/backend/schemas.py)
- Backend: [backend/database.py](/Users/amin/Desktop/MyProjects/contacts_book/backend/database.py)
- Backend Dockerfile: [backend/Dockerfile](/Users/amin/Desktop/MyProjects/contacts_book/backend/Dockerfile)
- Docker Compose: [docker-compose.yml](/Users/amin/Desktop/MyProjects/contacts_book/docker-compose.yml)
- Frontend env example: [.env.example](/Users/amin/Desktop/MyProjects/contacts_book/.env.example)
- Tests: [backend/tests/conftest.py](/Users/amin/Desktop/MyProjects/contacts_book/backend/tests/conftest.py), [backend/tests/test_contacts.py](/Users/amin/Desktop/MyProjects/contacts_book/backend/tests/test_contacts.py)

Want me to initialize the Expo app now?
