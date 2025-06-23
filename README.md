# 🗳️ Voting System - Java Swing Desktop Application

![Java](https://img.shields.io/badge/Java-17%2B-blue.svg)
![GUI](https://img.shields.io/badge/GUI-Swing-green.svg)
![Status](https://img.shields.io/badge/Project-Complete-brightgreen)
![License](https://img.shields.io/badge/License-Custom-lightgrey)

---

![Coding in action](https://media.giphy.com/media/dZX3AduGrY3uJ7qCsx/giphy.gif)

> A secure, file-based desktop voting system built with **Java Swing**, featuring user authentication, admin control, and vote tracking — ideal for academic projects and prototype demonstrations.

---

## 📌 Core Features

### 👥 Voter Interface
- 🔐 **Secure Login** (SHA‑256 hashing)
- 🗳️ **One‑Vote Guarantee** (no double voting)
- 🎨 **Polished UI** with candidate images, names & symbols
- 📊 **Real‑time Vote Tally Display**

### 🛠️ Admin Panel
- 🔑 Login with default credentials
- 📈 View counts: voters, candidates, votes
- ➕ Add / 🗑️ Delete voters & candidates
- 📤 Export data (CSV)
- 🔄 Intuitive sidebar + tab layout

### 💾 Local Data Persistence
Stored in `.txt` files for portability:
- `candidates.txt` → names, votes, symbols  
- `voters.txt` → usernames + hashed passwords  
- `voted.txt` → recorded voters

---

## 🗂️ Project Structure

```

VotingSystem/
├── src/votingapp/
│   ├── AdminPanel.java
│   ├── VotingPanel.java
│   ├── LoginFrame.java
│   ├── VotingSystemApp.java
│   ├── DataStore.java
│   └── SecurityUtils.java
├── candidates.txt
├── voters.txt
├── voted.txt
└── src/images/
├── icon.png
├── vote.png
├── dashboard.png
├── candidates.png
├── voters.png
├── results.png
└── \[candidate-symbols].png

```

---

## 🔐 Admin Credentials (Default)

```

Username: admin
Password: admin123

````

> ⚠️ Please update before real-world use.

---

## ▶️ How to Run

### 🧾 Terminal

```bash
cd src
javac votingapp/*.java
java votingapp.VotingSystemApp
````

### 💻 IntelliJ / Eclipse

1. Import as Java project
2. Mark `src` as source root
3. Run `VotingSystemApp.java`

---

## 📤 CSV Export Output

| Filename         | Contents                      |
| ---------------- | ----------------------------- |
| `candidates.csv` | Candidate name, votes, symbol |
| `voters.csv`     | Username, hashed password     |
| `votedusers.csv` | List of users who voted       |

> Compatible with Excel & Sheets.

---

## 🧪 Sample Data

**candidates.txt**

```
Abrar,0,brain
John,0,star
```

**voters.txt**

```
alice,ef92b778ba...
bob,5e88489da4...
```

**voted.txt**

```
alice
```

---

## 💼 Tech Stack

* **Java SE 17+**
* **Swing** (GUI)
* **File I/O**
* **SHA‑256 hashing**
* **PrintWriter** (CSV export)

---

## 📸 Screenshots *(Recommended)*

*(Add images of login, voting, and admin panels for visual impact.)*

---

## 📎 Notes

* Place symbol images in `/src/images/`, e.g. `brain.png`, `star.png`
* Fully offline and portable
* Future-ready: can add DB, charts, PDF export

---

## 🧑‍🎓 Use Cases

* Academic semester projects
* Demo prototypes for offline voting
* Java GUI programming learning tool

---

## 📜 License

This is for **educational/demo purposes**.
Modify and share with proper credit.

---

> Built professionally, coded confidently. ☕💻

```
