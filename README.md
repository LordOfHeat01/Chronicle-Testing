## Chronicle – University Publication Management System

Chronicle is a web-based publication management system designed to help universities store, track, and analyze research publications in a structured manner. Instead of relying on Excel sheets or manual records, Chronicle provides a centralized repository for both students and faculty to manage their academic contributions.

⚡ This repository is primarily created for testing and development purposes.

✨ Features

👨‍🏫 Role-based profiles – Faculty and students can sign up with their enrollment/employee numbers.

📝 Publication management – Add and manage publications (journals, conferences, books, book chapters).

🔍 Search & Filter – Find publications easily by author, year, or type.

⚙️ Selenium Automation –
Used to verify publications entered by the user through their ORCID ID, ensuring data accuracy.

📊 Analytics-ready schema – Database structure supports growth trends, performance indicators, and research metrics.

🏗️ Tech Stack

Backend: PostgreSQL (ACID-compliant relational database)

Frontend: Prototype with HTML/CSS/JS (extendable to React)

Automation: Selenium (for publication verification via ORCID)

Development Tools: GitHub for version control, testing repo for experimental builds


Setup
# Clone the repo
git clone https://github.com/<your-username>/Chronicle.git
cd Chronicle


🔎 Usage

Add a publication using enrollment/employee number.

Run Selenium module to automatically fetch & verify details from ORCID.

Publications get stored in the database for analytics and reporting.

🧪 Development Note

This repo is under active development and used mainly for testing new features and integrations.
Future versions may include:

Scopus API integration

Analytics dashboard

Export to BibTeX/APA/IEEE formats

📧 Author

Tapendra Verma
B.Tech CSE, Jaypee University of Engineering and Technology
