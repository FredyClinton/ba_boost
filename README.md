# AdTech Boosting API (RTB Server)

**Serveur de publicité Real-Time .**

L'AdTech Boosting API est un serveur boost de nouvelle génération, conçu pour la faible latence (< 100ms). Il permet de sélectionner en temps réel la publicité la plus pertinente pour un utilisateur en combinant :
1.  **Ciblage Démographique** (SQL)
2.  **Contraintes Budgétaires** (Real-time Budgeting)
3.  **Fréquence d'exposition** (Capping Redis)
4.  **Intelligence Artificielle** (Prédiction pCTR)

Le projet repose sur une architecture **Reactive (Non-bloquante)** capable de gérer une montée en charge massive (C10k problem).

---

## 🛠 Stack Technique

| Composant        | Technologie                     | Description                                               |
|:-----------------|:--------------------------------|:----------------------------------------------------------|
| **Backend Core** | **Java 17 / Spring Boot 3.4.1** | Framework **WebFlux** (Réactif) pour l'orchestration.     |
| **Database**     | **PostgreSQL 15**               | Stockage persistant avec driver **R2DBC** (Non-bloquant). |
| **Cache**        | **Redis**                       | Gestion du Frequency Capping et compteurs rapides.        |
| **ML Engine**    | **Python 3.9 / FastAPI**        | Microservice de prédiction pCTR et training.              |
| **DevOps**       | **Docker Compose**              | Orchestration des conteneurs.                             |
| **Docs**         | **Swagger / OpenAPI 3**         | Documentation interactive de l'API.                       |

---

## 🏗 Architecture du Pipeline de Décision

Lorsqu'une requête arrive sur `/ad-decision`, le système exécute ce flux :

1.  **Module 1 - Targeting (Filtrage) :** Interroge PostgreSQL pour trouver les campagnes actives correspondant aux critères (Âge, Ville, Pays) et dont les dates (`start_date`, `end_date`) sont valides.
2.  **Module 2 - Activity Guard (Capping) :** Interroge Redis pour vérifier si l'utilisateur n'a pas déjà vu cette publication trop souvent (Limitation : 3 vues / 24h).
3.  **Module 3 - ML Engine (Scoring) :** Envoie les candidats au service Python qui prédit la probabilité de clic (pCTR) basée sur l'historique et les intérêts.
4.  **Module 4 - Auction (Enchère) :** Calcule le score final : `Score = BidAmount * pCTR`. La campagne avec le meilleur score gagne et est diffusé à l'utilisateur.

---

## Installation et Lancement

### Pré-requis
*   Docker & Docker Compose installés.

### Démarrage Rapide

```bash
# 1. Nettoyer les volumes (Important pour charger les données de test initiales)
docker-compose down -v

# 2. Construire et lancer l'infrastructure
docker-compose up --build
```

### Accès Rapides
*   **API Java :** `http://localhost:8080`
*   **Swagger UI (Documentation) :** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
*   **Adminer (Vue BDD) :** [http://localhost:8081](http://localhost:8081)
    *   *System:* PostgreSQL | *Server:* `db` | *User:* `ba_user` | *Pass:* `ba_password`

---

## Référence Complète de l'API

### 1. Moteur de Décision (Frontend - Cœur du Système)

#### `POST /ad-decision`
C'est l'endpoint principal appelé par le Frontend pour récupérer une publicité à afficher.

**Corps de la requête (UserContext) :**
```json
{
  "userId": "123e4567-e89b-12d3-a456-426614174000",
  "age": 25,
  "city": "Douala",
  "interests": ["tech", "sport", "mode"]
}
```

**Réponse (200 OK) :**
```json
{
  "campaignId": "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11",
  "finalScore": 1.25,
  "contentUrl": "/api/v1/stories/b0eebc99..."
}
```
> **Note Frontend :** `contentUrl` est dynamique. Il pointe vers la ressource média associée à la campagne (Story, Image, Lien Profile, etc.). Si aucune pub n'est trouvée, l'API renvoie `200 OK` avec un corps vide (Empty Mono) ou `204 No Content`.

---

### 2. Tracking & Facturation (Frontend)

#### `POST /track/{campaignId}?type={TYPE}`
Cet endpoint **doit** être appelé par le frontend pour signaler une action.
Lorsqu'un utilisateur réagis sur un publication (vue, click etc...)

**Paramètres :**
*   `campaignId` (Path) : UUID reçu lors de la décision.
*   `type` (Query) :
    *   `VIEW` : Enregistre l'impression (Gratuit).
    *   `CLICK` : **Débite le budget** de l'annonceur (Payant).
    *   Les autres types d'interactions seront ajouté plustard

**Exemple :** `POST /track/a0eebc99...?type=CLICK`

---

### 3. Analytics (Dashboard Annonceur)

#### `GET /analytics/{campaignId}`
Fournit les statistiques agrégées en temps réel.
Pour le moment il fournit uniquement les statistiques d'un utilisateur

**Réponse :**
```json
{
  "campaignId": "a0eebc99-...",
  "views": 1500,
  "clicks": 45,
  "ctrs": 3.0  // Taux de clic en %
}
```

---

### 4. Gestion des Campagnes (Back-Office)

Endpoints pour créer et gérer les campagnes publicitaires.

*   **Lister les campagnes :** `GET /campaigns`
*   **Détail d'une campagne :** `GET /campaigns/{id}`
*   **Créer une campagne :** `POST /campaigns`
    ```json
    {
      "advertiserProfileId": "UUID_ANNONCEUR",
      "publicationId": "UUID_MEDIA_EXISTANT",
      "status": "ACTIVE",
      "minAge": 18,
      "maxAge": 30,
      "city": "Yaoundé",
      "country": "Cameroon",
      "targetInterests": ["tech", "business"],
      "bidAmount": 1.50,
      "budgetRemaining": 1000.00,
      "startDate": "2025-01-01T00:00:00Z",
      "endDate": "2025-01-30T00:00:00Z"
    }
    ```
*   **Modifier une campagne :** `PUT /campaigns/{id}`
*   **Supprimer une campagne :** `DELETE /campaigns/{id}`

---

### 5. Médiathèque / Publications (Back-Office)

Gestion des assets visuels (Images, Vidéos, Stories) indépendamment des campagnes.

*   **Lister les publications :** `GET /publications`
*   **Créer une publication :** `POST /publications`
    ```json
    {
      "title": "Promo Noël 2025",
      "description": "Vidéo 15s pour les réseaux",
      "mediaUrl": "https://cdn.example.com/video.mp4",
      "contentType": "STORY"
    }
    ```
    *Types de contenu supportés :* `IMAGE`, `STORY`, `PROFILE`, `BUSINESS`.

---

### 6. Machine Learning Engine (Interne)

Le service Python expose un endpoint pour réentraîner le modèle sur les données réelles.

*   **Entraînement :** `POST http://localhost:8001/train`
    *   Lit la table `interactions` de PostgreSQL.
    *   Recalcule les scores CTR (Click-Through Rate).
    *   Met à jour les poids en mémoire.

---

## Guide d'Intégration Frontend

### 1. Gestion du Capping (Fatigue Utilisateur)
⚠ **Important :** Si vous testez l'affichage des pubs en boucle avec le même `userId`, la publicité disparaîtra après **3 affichages** (ou changera pour une autre).
*   *C'est le comportement normal du module "Activity Guard".*
*   Pour reset le test, changez l'ID utilisateur dans le JSON ou videz Redis (`docker exec -it ba_redis redis-cli FLUSHALL`).

### 2. Routing Dynamique (`contentUrl`)
Le champ `contentUrl` ne pointe pas toujours vers une image statique. Il s'agit d'une ressource logique.
*   Si `contentType == STORY` -> `contentUrl` sera `/api/v1/stories/{id}`.
*   Si `contentType == PROFILE` -> `contentUrl` sera `/users/{id}` (Lien interne app).
*   **Le Frontend doit router l'utilisateur en fonction de cette URL.**

### 3. Gestion des Erreurs
L'API renvoie des erreurs structurées au format standard :
```json
{
  "timestamp": "2025-12-27T10:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Budget épuisé pour cette campagne",
  "path": "/track/..."
}
```

---

##  Roadmap & Prochaines Étapes

1. Implémenter elasticsearch pour l'optimisation de la recherche des campagnes des user.
2. **Résilience :** Ajouter un vrai modèle de ML pour le ml-engine.
3. **Sécurité :** Intégration de Spring Security (JWT) pour protéger les routes Back-Office.