-- ==========================================
-- NETTOYAGE
-- ==========================================
DROP TABLE IF EXISTS interactions;
DROP TABLE IF EXISTS campaigns;
DROP TABLE IF EXISTS publications;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS advertiser_profile;

-- ==========================================
-- CRÉATION DES TABLES
-- ==========================================

-- 1. Publications
CREATE TABLE IF NOT EXISTS publications (
                                            id UUID PRIMARY KEY,
                                            title VARCHAR(255),
    media_url VARCHAR(500),
    content_type VARCHAR(50),
    description VARCHAR(255),
    media_urls TEXT[],
    version BIGINT DEFAULT 0
    );

-- 2. Campagnes (AVEC start_date et end_date)
CREATE TABLE IF NOT EXISTS campaigns (
                                         id UUID PRIMARY KEY,
                                         advertiser_profile_id UUID NOT NULL,
                                         publication_id UUID,
                                         status VARCHAR(50) NOT NULL,
    min_age INTEGER,
    max_age INTEGER,
    city VARCHAR(100),
    country VARCHAR(100),
    target_interests TEXT[],
    bid_amount DECIMAL(10, 2),
    budget_remaining DECIMAL(10, 2),

    -- NOUVEAUX CHAMPS
    start_date TIMESTAMP,
    end_date TIMESTAMP,

    version BIGINT DEFAULT 0
    );

-- 3. Interactions
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
-- SEED DATA
-- ==========================================

-- --- A. PUBLICATIONS ---

INSERT INTO publications (id, title, media_url, content_type, version) VALUES
                                                                           ('11111111-1111-1111-1111-111111111111', 'Promo Tech 2025', 'https://cdn.test.com/tech.jpg', 'BUSINESS', 0),
                                                                           ('22222222-2222-2222-2222-222222222222', 'Kribi Beach Story', 'https://cdn.test.com/kribi.mp4', 'STORY', 0),
                                                                           ('33333333-3333-3333-3333-333333333333', 'Finance Corp Profile', 'app://profile/finance-corp', 'PROFILE', 0),
                                                                           ('44444444-4444-4444-4444-444444444444', 'Collection Été 2026', 'api/catalogs/summer-collection', 'PROFILE', 0),
                                                                           ('55555555-5555-5555-5555-555555555555', 'Join Buea University', 'https://cdn.test.com/campus-tour.mp4', 'BUSINESS', 0),
                                                                           ('66666666-6666-6666-6666-666666666666', 'Bonapriso Real Estate', 'api/business/bonapriso-immo', 'BUSINESS', 0),
                                                                           ('77777777-7777-7777-7777-777777777777', 'Engrais Bio Cameroun', 'https://cdn.test.com/engrais.jpg', 'IMAGE', 0),
                                                                           ('88888888-8888-8888-8888-888888888888', 'Garoua Live Festival', 'https://cdn.test.com/festival.mp4', 'STORY', 0);


-- --- B. CAMPAGNES (Avec Dates) ---
-- Note : NOW() crée une date actuelle, et NOW() + INTERVAL '30 days' définit la fin dans un mois.

-- 1. Campagne Jeunes & Tech (Douala)
INSERT INTO campaigns (id, advertiser_profile_id, publication_id, status, min_age, max_age, city, country, target_interests, bid_amount, budget_remaining, start_date, end_date, version)
VALUES (
           'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380b22', '11111111-1111-1111-1111-111111111111',
           'ACTIVE', 18, 30, 'Douala', 'Cameroon', ARRAY['sport', 'tech', 'musique'], 1.50, 1000.00,
           NOW(), NOW() + INTERVAL '30 days', 0
       );

-- 2. Campagne Gastronomie (Kribi)
INSERT INTO campaigns (id, advertiser_profile_id, publication_id, status, min_age, max_age, city, country, target_interests, bid_amount, budget_remaining, start_date, end_date, version)
VALUES (
           'c5caf555-9c0b-4ef8-bb6d-6bb9bd380c55', 'f4eebc99-9c0b-4ef8-bb6d-6bb9bd380f66', '22222222-2222-2222-2222-222222222222',
           'ACTIVE', 18, 65, 'Kribi', 'Cameroon', ARRAY['cuisine', 'musique', 'voyage'], 1.10, 800.00,
           NOW(), NOW() + INTERVAL '30 days', 0
       );

-- 3. Campagne Finance (National)
INSERT INTO campaigns (id, advertiser_profile_id, publication_id, status, min_age, max_age, city, country, target_interests, bid_amount, budget_remaining, start_date, end_date, version)
VALUES (
           'f2abc888-9c0b-4ef8-bb6d-6bb9bd380f22', 'c1eebc99-9c0b-4ef8-bb6d-6bb9bd380c33', '33333333-3333-3333-3333-333333333333',
           'ACTIVE', 25, 55, NULL, 'Cameroon', ARRAY['finance', 'tech', 'business'], 2.50, 5000.00,
           NOW(), NOW() + INTERVAL '60 days', 0
       );

-- 4. Campagne Mode (Yaoundé)
INSERT INTO campaigns (id, advertiser_profile_id, publication_id, status, min_age, max_age, city, country, target_interests, bid_amount, budget_remaining, start_date, end_date, version)
VALUES (
           'd4def777-9c0b-4ef8-bb6d-6bb9bd380d44', 'd2eebc99-9c0b-4ef8-bb6d-6bb9bd380d44', '44444444-4444-4444-4444-444444444444',
           'ACTIVE', 18, 45, 'Yaounde', 'Cameroon', ARRAY['mode', 'shopping', 'luxe'], 2.00, 3000.00,
           NOW(), NOW() + INTERVAL '15 days', 0
       );

-- 5. Campagne Éducation (Buea)
INSERT INTO campaigns (id, advertiser_profile_id, publication_id, status, min_age, max_age, city, country, target_interests, bid_amount, budget_remaining, start_date, end_date, version)
VALUES (
           'e5eef888-9c0b-4ef8-bb6d-6bb9bd380e55', 'e3eebc99-9c0b-4ef8-bb6d-6bb9bd380e55', '55555555-5555-5555-5555-555555555555',
           'ACTIVE', 16, 25, 'Buea', 'Cameroon', ARRAY['éducation', 'université', 'tech'], 0.50, 500.00,
           NOW(), NOW() + INTERVAL '90 days', 0
       );

-- 6. Campagne Immobilier (Douala)
INSERT INTO campaigns (id, advertiser_profile_id, publication_id, status, min_age, max_age, city, country, target_interests, bid_amount, budget_remaining, start_date, end_date, version)
VALUES (
           'f6fff999-9c0b-4ef8-bb6d-6bb9bd380f66', 'f4eebc99-9c0b-4ef8-bb6d-6bb9bd380f66', '66666666-6666-6666-6666-666666666666',
           'ACTIVE', 35, 65, 'Douala', 'Cameroon', ARRAY['immobilier', 'finance', 'investissement'], 5.00, 10000.00,
           NOW(), NOW() + INTERVAL '180 days', 0
       );

-- 7. Campagne Agri-Business (Bafoussam)
INSERT INTO campaigns (id, advertiser_profile_id, publication_id, status, min_age, max_age, city, country, target_interests, bid_amount, budget_remaining, start_date, end_date, version)
VALUES (
           'a7aaa000-9c0b-4ef8-bb6d-6bb9bd380a77', 'a5eebc99-9c0b-4ef8-bb6d-6bb9bd380a77', '77777777-7777-7777-7777-777777777777',
           'ACTIVE', 25, 60, 'Bafoussam', 'Cameroon', ARRAY['agriculture', 'business', 'nature'], 1.00, 1500.00,
           NOW(), NOW() + INTERVAL '45 days', 0
       );

-- 8. Campagne Événementiel (Garoua)
INSERT INTO campaigns (id, advertiser_profile_id, publication_id, status, min_age, max_age, city, country, target_interests, bid_amount, budget_remaining, start_date, end_date, version)
VALUES (
           'b8bbb111-9c0b-4ef8-bb6d-6bb9bd380b88', 'b6eebc99-9c0b-4ef8-bb6d-6bb9bd380b88', '88888888-8888-8888-8888-888888888888',
           'ACTIVE', 15, 35, 'Garoua', 'Cameroon', ARRAY['musique', 'fête', 'culture'], 0.80, 2000.00,
           NOW(), NOW() + INTERVAL '2 minutes', 0
       );

-- 9. Campagne "Flash Test" (Durée de vie : 2 minutes exactement)
INSERT INTO campaigns (
    id, advertiser_profile_id, publication_id, status,
    min_age, max_age, city, country, target_interests,
    bid_amount, budget_remaining,
    start_date, end_date, version
)
VALUES (
           '99999999-9999-9999-9999-999999999999', -- ID facile à retenir
           'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380b22', -- Profil existant
           '11111111-1111-1111-1111-111111111111', -- Pub existante (Tech)
           'ACTIVE',
           18, 90, NULL, 'Cameroon',               -- Cible tout le Cameroun
           ARRAY['test', 'flash', 'vitesse'],
           10.00, 500.00,                          -- Grosse enchère pour être sûr de la voir
           NOW(),                                  -- Début : Maintenant
           NOW() + INTERVAL '2 minutes',           -- Fin : Dans 2 minutes pile
           0
       );