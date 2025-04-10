# 🚀 Hackathon Management Module (Spring Boot + Angular)

Ce module fait partie d’un projet complet développé avec **Spring Boot** (backend) et **Angular** (frontend) basé sur une architecture **microservices**. Il se concentre sur la **gestion des hackathons**, permettant aux utilisateurs de créer, consulter, participer et interagir autour de compétitions tech en ligne.

---

## 🧩 Microservice : `hackathon-service`

Ce microservice assure la gestion complète des hackathons : création, inscription des participants, statistiques et interactions avec des publications.

### ✨ Fonctionnalités principales

#### 📌 Gestion des Hackathons
- Création de hackathon : `POST /api/v1/hackathons`
- Consultation d’un hackathon par ID : `GET /api/v1/hackathons/{id}`
- Liste paginée de tous les hackathons : `GET /api/v1/hackathons?page=0&size=10`
- Mise à jour : `PUT /api/v1/hackathons/{id}`
- Suppression : `DELETE /api/v1/hackathons/{id}`

#### 👥 Gestion des Participants
- Rejoindre un hackathon : `POST /api/v1/hackathons/join?hackathonId=1&userId=5`
- Quitter un hackathon : `DELETE /api/v1/hackathons/unjoin?hackathonId=1&userId=5`
- Voir les participations d’un utilisateur : `GET /api/v1/hackathons/user/{userId}`
- Voir les participants d’un hackathon : `GET /api/v1/hackathons/hackathon/{hackathonId}`
- Compter les participants : `GET /api/v1/hackathons/count/{hackathonId}`
- Vérifier si un utilisateur est inscrit : `GET /api/v1/hackathons/status?hackathonId=1&userId=5`

#### 📊 Statistiques
- Niveau de difficulté des hackathons : `GET /api/v1/hackathons/getStatistics`

#### 📰 Intégration avec les publications
- Liste paginée des publications : `GET /api/v1/hackathons/posts`
- Récupérer une publication par ID : `GET /api/v1/hackathons/post/{id}`
- Marquer une publication comme "meilleur post" pour un hackathon : `POST /api/v1/hackathons/{id}/best-post/{postId}`
- Voir les meilleurs posts : `GET /api/v1/hackathons/{id}/best-post`

---

## 🖥️ Frontend (Angular)
L’interface utilisateur permet :
- Visualisation des hackathons
- Participation / désinscription à un hackathon
- Affichage des meilleurs posts liés à chaque événement
- Consultation des statistiques et du nombre de participants

---

## ⚙️ Technologies utilisées
- ✅ Spring Boot 3.x
- ✅ Angular 16+
- ✅ MySQL / JPA / Hibernate
- ✅ Spring Cloud : Eureka Discovery, Config Server
- ✅ Docker (conteneurisation des microservices)
- ✅ Architecture RESTful
- ✅ Lombok

---

## 👨‍💻 Auteur de ce module
Développé par **Slim-Fady Hanafi**, dans le cadre d’un projet collaboratif DevDynamous 🎯.

---

## 📁 Arborescence principale (backend)

hackathon/ ├── controller/ │ ├── HackathonController.java │ └── HackathonParticipationController.java ├── entities/ │ └── Hackathon.java, HackathonParticipation.java, etc. ├── dtos/ ├── service/ ├── repository/ └── resources/
