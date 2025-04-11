# 📚 Formation Management Microservice

Ce projet Spring Boot permet de gérer des programmes de formation de manière complète, avec des fonctionnalités avancées telles que : export PDF/Excel/CSV, statistiques, chatbot, certificat PDF, envoi d’email via Mailtrap, et génération de graphique (camembert) en PDF et image.

---

## 🚀 Fonctionnalités principales

### 📋 CRUD de formations
- Création, mise à jour, suppression, consultation de formations.
- Recherche par mot-clé.
- Tri par durée ou prix.



### 📁 Exportations
- 🔹 Export PDF d’une formation (`/export/{id}`)
- 🔹 Export PDF par catégorie (`/export-category`, `/export-category-excel`)


### 📈 Statistiques et filtres
- Statistiques globales et par durée ou catégorie.
- Filtres dynamiques sur prix et durée (`/filter`).

### 🎓 Certificats
- Génération d’un certificat PDF personnalisé (`/generate-certificate`)

### 📊 Graphiques
- 🥧 Diagramme circulaire (Pie Chart) des formations par catégorie :
  - 📷 Image : `/chart/pie-category`
  - 📄 PDF : `/chart/pdf-category`

---

## 🛠️ Technologies utilisées

- Java 17
- Spring Boot 3.4.2
- Spring Data JPA
- Spring Web
- Spring Cloud Gateway + Eureka 
- iText (PDF)
- JFreeChart (Diagrammes)



