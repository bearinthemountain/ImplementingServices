# MarmotteMovie 🎬

Application Jakarta EE de location de films et séries, déployée sur WildFly.

---

## Prérequis

| Outil | Version |
|-------|---------|
| Java | 21 |
| Maven | 3.8+ |
| WildFly | 27+ |
| IDE | Eclipse (avec JBoss Tools) |

---

## Installation et déploiement

### 1. Peupler la base de données

Lancer le test JUnit `PopulateDB` pour créer le schéma et insérer les données initiales (catégories, films, séries, utilisateurs) :

```
src/test/java/ch/hevs/test/PopulateDB.java → Run As > JUnit Test
```

> ⚠️ Ce test utilise la persistence unit `MarmottePU_unitTest` (HSQLDB embarqué).  
> À relancer à chaque fois que le schéma change (ex. après modification d'une entité JPA).

### 2. Configurer la sécurité dans WildFly

L'authentification utilise **BASIC auth** via le realm WildFly nommé **`MarmotteRealm`**.

Créer les utilisateurs et leur attribuer un rôle dans la console d'administration WildFly (`http://localhost:9990`) :

| Rôle | Accès |
|------|-------|
| `client` | Catalogue, location, mes locations |
| `manager` | Tout + interface d'administration |

### 3. Déployer l'application

```bash
mvn clean package wildfly:deploy
```

Ou via Eclipse : clic droit sur le projet → *Run As > Run on Server*.

---

## Accès à l'application

URL de base :
```
http://localhost:8080/MarmotteMovie/
```

### Pages disponibles

| Page | URL | Rôle requis |
|------|-----|-------------|
| Catalogue | `http://localhost:8080/MarmotteMovie/catalog.xhtml` | `client`, `manager` |
| Mes locations | `http://localhost:8080/MarmotteMovie/rentals.xhtml` | `client`, `manager` |
| Administration | `http://localhost:8080/MarmotteMovie/admin.xhtml` | `manager` uniquement |

> L'accès à une page protégée déclenche automatiquement la fenêtre de login HTTP Basic du navigateur.  
> La page d'accueil (`/`) redirige directement vers le catalogue.

---

## Structure du projet

```
src/
├── main/java/ch/hevs/
│   ├── businessobject/     Entités JPA (User, Media, Movie, Serie, Rental, Category)
│   ├── service/            Services CDI (MediaService, RentalService, UserService)
│   ├── presentation/       Beans JSF (AdminBean, RentalBean, MediaBean, UserBean)
│   └── exception/          Exceptions métier (RentalException)
├── main/webapp/
│   ├── catalog.xhtml       Page catalogue (liste des médias)
│   ├── rentals.xhtml       Page mes locations actives
│   ├── admin.xhtml         Interface ajout de médias
│   └── showRentalResult.xhtml  Confirmation de location
└── test/java/ch/hevs/test/
    └── PopulateDB.java     Script de peuplement de la base
```

---

## Stack technique

- **Jakarta EE 11** — CDI, JSF, JPA, Security
- **WildFly** — Serveur d'application, gestion de la sécurité (BASIC auth / `MarmotteRealm`)
- **Hibernate** — Implémentation JPA
- **HSQLDB** — Base de données embarquée (tests uniquement)
