# 🎓 Microservice : OffresDeStage-Service

Bienvenue dans le microservice `OffresDeStage-Service` du projet **DevDynamous**.  
Ce service est dédié à la **gestion des offres de stages** et des **candidatures étudiantes**, avec envoi automatique d’e-mails de confirmation et génération de **statistiques**.

---

## 🧱 Architecture Microservices

```
📦 GInternship/
├── 📂 config-server/        → Configuration centralisée
├── 📂 discovery/            → Registre Eureka
├── 📂 gateway/              → API Gateway
└── 📂 internshipoffer/      → Microservice de gestion des stages
```

---

## 🚀 Fonctionnalités

### 📌 Offres de Stage

- `POST /api/v1/internshipOffers` : Créer une nouvelle offre
- `GET /api/v1/internshipOffers` : Lister toutes les offres
- `GET /api/v1/internshipOffers/{id}` : Obtenir une offre par ID
- `PUT /api/v1/internshipOffers/{id}` : Modifier une offre
- `DELETE /api/v1/internshipOffers/{id}` : Supprimer une offre

### 📝 Candidatures

- `POST /api/v1/internship-applications` : Postuler à une offre
- `GET /api/v1/internship-applications` : Lister toutes les candidatures
- `GET /api/v1/internship-applications/byOffer/{offerId}` : Candidatures pour une offre
- `DELETE /api/v1/internship-applications/{id}` : Supprimer une candidature
- `GET /api/v1/internship-applications/status?userId=5&offerId=2` : Vérifier une candidature

✅ **Email automatique** envoyé à l’étudiant après candidature.

---

## 📊 Statistiques

- Nombre de candidatures par offre
- Répartition par statut *(à venir)*
- Statistiques temporelles
- Export possible *(à venir)*

---

## 🖥️ Frontend Angular

- Liste et détails des offres
- Formulaire de candidature avec envoi de CV
- Statistiques visuelles (graphiques)
- Confirmation de candidature par email

---

## 🗄️ Base de Données

- La base de données utilisée est **MySQL**.

---

## ⚙️ Stack technique

- **Spring Boot 3**
- **Spring Cloud Config / Eureka**
- **MySQL / JPA / Hibernate**
- **JavaMailSender** (service mail intégré)
- **Swagger** pour la doc API
- **Docker** (optionnel)
- **Angular 16+** (frontend)

---

## 👨‍💻 Auteur

Développé par **Riahi Dorsaf**  
📧 Dorsaf.riahi@esprit.tn  
📱 +216 95075025  
🔗 [GitHub - DevDynamous](https://github.com/wissaldaoud/Devdynamousawd)

> N'hésitez pas à ⭐ le projet ou contribuer via Pull Request 🙌
