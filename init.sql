-- Nettoyage complet (si jamais on exécute ce script manuellement)
DROP TABLE IF EXISTS interactions;
DROP TABLE IF EXISTS campaigns;
DROP TABLE IF EXISTS publications;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS advertiser_profile;

-- 1. Table des Publications (Médiathèque)
CREATE TABLE IF NOT EXISTS publications (
    id UUID PRIMARY KEY,
    title VARCHAR(255),
    media_url VARCHAR(500),
    content_type VARCHAR(50), -- IMAGE, VIDEO, STORY...
    description VARCHAR(255),
    media_urls TEXT[],
    version BIGINT DEFAULT 0
    );

-- 2. Table des Campagnes (Liée à une publication)
CREATE TABLE IF NOT EXISTS campaigns (
    id UUID PRIMARY KEY,
    advertiser_profile_id UUID NOT NULL,
    publication_id UUID,        -- Lien vers la publication
    status VARCHAR(50) NOT NULL,
    min_age INTEGER,
    max_age INTEGER,
    city VARCHAR(100),
    country VARCHAR(100),
    target_interests TEXT[],
    bid_amount DECIMAL(10, 2),
    budget_remaining DECIMAL(10, 2),
    version BIGINT DEFAULT 0
    );

-- 3. Table des Interactions (Logs)
CREATE TABLE IF NOT EXISTS interactions (
    id UUID PRIMARY KEY,
    campaign_id UUID NOT NULL,
    user_id UUID NOT NULL,
    interaction_type VARCHAR(50),
    timestamp TIMESTAMP,
    user_age INTEGER,
    user_city VARCHAR(100),
    version BIGINT DEFAULT 0
    );

-- ==========================================
-- SEED DATA (Données de test)
-- ==========================================

-- A. Création des Publications
-- Pub 1: Visuel Tech
INSERT INTO publications (id, title, media_url, content_type, version)
VALUES ('11111111-1111-1111-1111-111111111111', 'Promo Tech 2025', 'https://cdn.test.com/tech.jpg', 'BUSINESS', 0);

-- Pub 2: Story Voyage
INSERT INTO publications (id, title, media_url, content_type, version)
VALUES ('22222222-2222-2222-2222-222222222222', 'Kribi Beach Story', 'https://cdn.test.com/kribi.mp4', 'STORY', 0);

-- Pub 3: Profil Business
INSERT INTO publications (id, title, media_url, content_type, version)
VALUES ('33333333-3333-3333-3333-333333333333', 'Finance Corp Profile', 'app://profile/finance-corp', 'PROFILE', 0);


-- B. Création des Campagnes (Liées aux publications ci-dessus)

-- 1. Campagne Jeunes & Tech (Douala) -> Utilise Pub 1 (Image)
INSERT INTO campaigns (id, advertiser_profile_id, publication_id, status, min_age, max_age, city, country, target_interests, bid_amount, budget_remaining, version)
VALUES (
           'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11',
           'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380b22',
           '11111111-1111-1111-1111-111111111111', -- Lien vers Pub 1
           'ACTIVE',
           18, 30, 'Douala', 'Cameroon',
           ARRAY['sport', 'tech', 'musique'],
           1.50, 1000.00, 0
       );

-- 2. Campagne Gastronomie (Kribi) -> Utilise Pub 2 (Story)
INSERT INTO campaigns (id, advertiser_profile_id, publication_id, status, min_age, max_age, city, country, target_interests, bid_amount, budget_remaining, version)
VALUES (
           'c5caf555-9c0b-4ef8-bb6d-6bb9bd380c55',
           'f4eebc99-9c0b-4ef8-bb6d-6bb9bd380f66',
           '22222222-2222-2222-2222-222222222222', -- Lien vers Pub 2
           'ACTIVE',
           18, 65, 'Kribi', 'Cameroon',
           ARRAY['cuisine', 'musique', 'voyage'],
           1.10, 800.00, 0
       );

-- 3. Campagne Finance (National) -> Utilise Pub 3 (Profil App)
INSERT INTO campaigns (id, advertiser_profile_id, publication_id, status, min_age, max_age, city, country, target_interests, bid_amount, budget_remaining, version)
VALUES (
           'f2abc888-9c0b-4ef8-bb6d-6bb9bd380f22',
           'c1eebc99-9c0b-4ef8-bb6d-6bb9bd380c33',
           '33333333-3333-3333-3333-333333333333', -- Lien vers Pub 3
           'ACTIVE',
           25, 55, NULL, 'Cameroon',
           ARRAY['finance', 'tech', 'business'],
           2.50, 5000.00, 0
       );



-- --- D. NOUVELLES PUBLICATIONS ---

-- Pub 4: Catalogue Mode (Yaoundé) - Type CATALOG
INSERT INTO publications (id, title, media_url, content_type, version)
VALUES ('44444444-4444-4444-4444-444444444444', 'Collection Été 2026', 'api/catalogs/summer-collection', 'PROFILE', 0);

-- Pub 5: Université (Buea) - Type VIDEO
INSERT INTO publications (id, title, media_url, content_type, version)
VALUES ('55555555-5555-5555-5555-555555555555', 'Join Buea University', 'https://cdn.test.com/campus-tour.mp4', 'BUSINESS', 0);

-- Pub 6: Immobilier de Luxe (Douala) - Type BUSINESS_PAGE
INSERT INTO publications (id, title, media_url, content_type, version)
VALUES ('66666666-6666-6666-6666-666666666666', 'Bonapriso Real Estate', 'api/business/bonapriso-immo', 'BUSINESS', 0);

-- Pub 7: Agriculture Bio (Bafoussam) - Type IMAGE
INSERT INTO publications (id, title, media_url, content_type, version)
VALUES ('77777777-7777-7777-7777-777777777777', 'Engrais Bio Cameroun', 'https://cdn.test.com/engrais.jpg', 'IMAGE', 0);

-- Pub 8: Concert Musique (Garoua) - Type STORY
INSERT INTO publications (id, title, media_url, content_type, version)
VALUES ('88888888-8888-8888-8888-888888888888', 'Garoua Live Festival', 'https://cdn.test.com/festival.mp4', 'STORY', 0);


-- --- E. NOUVELLES CAMPAGNES ---

-- 4. Campagne Mode (Yaoundé) -> Cible les femmes actives intéressées par le shopping
INSERT INTO campaigns (id, advertiser_profile_id, publication_id, status, min_age, max_age, city, country, target_interests, bid_amount, budget_remaining, version)
VALUES (
           'd4def777-9c0b-4ef8-bb6d-6bb9bd380d44',
           'd2eebc99-9c0b-4ef8-bb6d-6bb9bd380d44', -- Profil "Mode House"
           '44444444-4444-4444-4444-444444444444', -- Lien vers Pub 4 (Catalogue)
           'ACTIVE',
           18, 45, 'Yaounde', 'Cameroon',
           ARRAY['mode', 'shopping', 'luxe'],
           2.00, 3000.00, 0
       );

-- 5. Campagne Éducation (Buea) -> Cible les étudiants
INSERT INTO campaigns (id, advertiser_profile_id, publication_id, status, min_age, max_age, city, country, target_interests, bid_amount, budget_remaining, version)
VALUES (
           'e5eef888-9c0b-4ef8-bb6d-6bb9bd380e55',
           'e3eebc99-9c0b-4ef8-bb6d-6bb9bd380e55', -- Profil "University"
           '55555555-5555-5555-5555-555555555555', -- Lien vers Pub 5 (Vidéo)
           'ACTIVE',
           16, 25, 'Yaounde', 'Cameroon',
           ARRAY['éducation', 'université', 'tech'],
           0.50, 500.00, 0
       );

-- 6. Campagne Immobilier (Douala) -> Cible les investisseurs âgés (Gros Budget)
INSERT INTO campaigns (id, advertiser_profile_id, publication_id, status, min_age, max_age, city, country, target_interests, bid_amount, budget_remaining, version)
VALUES (
           'f6fff999-9c0b-4ef8-bb6d-6bb9bd380f66',
           'f4eebc99-9c0b-4ef8-bb6d-6bb9bd380f66', -- Profil "Immo Luxe"
           '66666666-6666-6666-6666-666666666666', -- Lien vers Pub 6 (Business Page)
           'ACTIVE',
           35, 65, 'Douala', 'Cameroon',
           ARRAY['immobilier', 'finance', 'investissement'],
           5.00, 10000.00, 0
       );

-- 7. Campagne Agri-Business (Bafoussam) -> Cible locale spécifique
INSERT INTO campaigns (id, advertiser_profile_id, publication_id, status, min_age, max_age, city, country, target_interests, bid_amount, budget_remaining, version)
VALUES (
           'a7aaa000-9c0b-4ef8-bb6d-6bb9bd380a77',
           'a5eebc99-9c0b-4ef8-bb6d-6bb9bd380a77',
           '77777777-7777-7777-7777-777777777777', -- Lien vers Pub 7 (Image)
           'ACTIVE',
           25, 60, 'Yaounde', 'Cameroon',
           ARRAY['agriculture', 'business', 'nature'],
           1.00, 1500.00, 0
       );

-- 8. Campagne Événementiel (Garoua) -> Cible large et jeune
INSERT INTO campaigns (id, advertiser_profile_id, publication_id, status, min_age, max_age, city, country, target_interests, bid_amount, budget_remaining, version)
VALUES (
           'b8bbb111-9c0b-4ef8-bb6d-6bb9bd380b88',
           'b6eebc99-9c0b-4ef8-bb6d-6bb9bd380b88',
           '88888888-8888-8888-8888-888888888888', -- Lien vers Pub 8 (Story)
           'ACTIVE',
           15,
           35,
           'Douala',
           'Cameroon',
           ARRAY['musique', 'fête', 'culture'],
           0.80,
           2000.00, 0
       );