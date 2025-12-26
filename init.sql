-- Création de la table campagnes
CREATE TABLE IF NOT EXISTS campaigns (
                                         id UUID PRIMARY KEY,
                                         advertiser_profile_id UUID NOT NULL,
                                         status VARCHAR(50) NOT NULL,
    min_age INTEGER,
    max_age INTEGER,
    city VARCHAR(100),
    country VARCHAR(100),
    target_interests TEXT[],
    bid_amount DECIMAL(10, 2),
    budget_remaining DECIMAL(10, 2),
    version BIGINT DEFAULT 0 -- Ajout de la colonne version pour Spring Data
    );

-- Suppression des données existantes pour éviter les doublons lors du reset
TRUNCATE TABLE campaigns;

-- Insertion de données de TEST (Seed Data) avec intérêts croisés

-- 1. Campagne Jeunes & Tech (Douala) - Intérêts: tech, sport
INSERT INTO campaigns (id, advertiser_profile_id, status, min_age, max_age, city, country, target_interests, bid_amount, budget_remaining, version)
VALUES (
           'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11',
           'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380b22',
           'ACTIVE',
           18, 30,
           'Douala', 'Cameroon',
           ARRAY['sport', 'tech', 'musique'],
           1.50,
           1000.00,
           0
       );

-- 2. Campagne Gaming & Étudiants (Yaoundé) - Intérêts: tech, gaming
INSERT INTO campaigns (id, advertiser_profile_id, status, min_age, max_age, city, country, target_interests, bid_amount, budget_remaining, version)
VALUES (
           'e1fbc999-9c0b-4ef8-bb6d-6bb9bd380e11',
           'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380b22',
           'ACTIVE',
           15, 25,
           'Yaoundé', 'Cameroon',
           ARRAY['gaming', 'tech', 'anime'],
           1.20,
           500.00,
           0
       );

-- 3. Campagne Finance & Business (National) - Intérêts: tech, finance
INSERT INTO campaigns (id, advertiser_profile_id, status, min_age, max_age, city, country, target_interests, bid_amount, budget_remaining, version)
VALUES (
           'f2abc888-9c0b-4ef8-bb6d-6bb9bd380f22',
           'c1eebc99-9c0b-4ef8-bb6d-6bb9bd380c33',
           'ACTIVE',
           25, 55,
           NULL, 'Cameroon',
           ARRAY['finance', 'tech', 'business'],
           2.50,
           5000.00,
           0
       );

-- 4. Campagne Mode & Lifestyle (Douala) - Intérêts: musique, mode
INSERT INTO campaigns (id, advertiser_profile_id, status, min_age, max_age, city, country, target_interests, bid_amount, budget_remaining, version)
VALUES (
           'd3def777-9c0b-4ef8-bb6d-6bb9bd380d33',
           'd2eebc99-9c0b-4ef8-bb6d-6bb9bd380d44',
           'ACTIVE',
           20, 40,
           'Douala', 'Cameroon',
           ARRAY['mode', 'musique', 'lifestyle'],
           1.80,
           1500.00,
           0
       );

-- 5. Campagne Sport & Nutrition (National) - Intérêts: sport, santé
INSERT INTO campaigns (id, advertiser_profile_id, status, min_age, max_age, city, country, target_interests, bid_amount, budget_remaining, version)
VALUES (
           'b4bae666-9c0b-4ef8-bb6d-6bb9bd380b44',
           'e3eebc99-9c0b-4ef8-bb6d-6bb9bd380e55',
           'ACTIVE',
           18, 50,
           NULL, 'Cameroon',
           ARRAY['sport', 'santé', 'fitness'],
           2.10,
           2500.00,
           0
       );

-- 6. Campagne Gastronomie (Kribi) - Intérêts: musique, cuisine
INSERT INTO campaigns (id, advertiser_profile_id, status, min_age, max_age, city, country, target_interests, bid_amount, budget_remaining, version)
VALUES (
           'c5caf555-9c0b-4ef8-bb6d-6bb9bd380c55',
           'f4eebc99-9c0b-4ef8-bb6d-6bb9bd380f66',
           'ACTIVE',
           18, 65,
           'Kribi', 'Cameroon',
           ARRAY['cuisine', 'musique', 'voyage'],
           1.10,
           800.00,
           0
       );


-- Table pour stocker les logs d'interactions (Click, View, etc.)
CREATE TABLE IF NOT EXISTS interactions (
                                            id UUID PRIMARY KEY,
                                            campaign_id UUID NOT NULL,
                                            user_id UUID NOT NULL,
                                            interaction_type VARCHAR(50),
    timestamp TIMESTAMP,
    version BIGINT DEFAULT 0 -- Gestion Optimistic Locking pour R2DBC
    );