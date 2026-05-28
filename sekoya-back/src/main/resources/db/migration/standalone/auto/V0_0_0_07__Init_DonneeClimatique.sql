INSERT INTO DonneeClimatique
       (id, aleaType, horizon, scenario, evolution,
        donneeClimatiqueBrute_id, pointGeographique_id)
SELECT nextval('DonneeClimatique_id_seq'),
       'MODIFICATION_TEMPERATURES_AIR',
       m.horizon,
       m.scenario,
       CASE
         WHEN m.ecart_max <  0.0 THEN 'FAVORABLE'
         WHEN m.ecart_max <= 1.5 THEN 'PAS_EVOLUTION'
         WHEN m.ecart_max <= 2.0 THEN 'LEGEREMENT_DEFAVORABLE'
         WHEN m.ecart_max <= 2.5 THEN 'MODEREMENT_DEFAVORABLE'
         WHEN m.ecart_max <= 3.0 THEN 'DEFAVORABLE'
         WHEN m.ecart_max >  3.0 THEN 'FORTEMENT_DEFAVORABLE'
         ELSE NULL
       END AS evolution,
       m.id,
       m.pointGeographique_id
FROM (
  SELECT DISTINCT ON (b.horizon, b.scenario, b.pointGeographique_id)
         b.id,
         b.horizon,
         b.scenario,
         b.pointGeographique_id,
         b.ecart AS ecart_max
  FROM DonneeClimatiqueBrute b
  WHERE b.indicateur = 'ATAV'
  ORDER BY b.horizon, b.scenario, b.pointGeographique_id,
           b.ecart DESC, b.id DESC
) AS m;

INSERT INTO DonneeClimatique
       (id, aleaType, horizon, scenario, evolution,
        donneeClimatiqueBrute_id, pointGeographique_id)
SELECT nextval('DonneeClimatique_id_seq'),
       'STRESS_THERMIQUE',
       m.horizon,
       m.scenario,
       CASE
         WHEN m.ecart_max <  0.0 THEN 'FAVORABLE'
         WHEN m.ecart_max <= 0.5 THEN 'PAS_EVOLUTION'
         WHEN m.ecart_max <= 1.5 THEN 'LEGEREMENT_DEFAVORABLE'
         WHEN m.ecart_max <= 2.5 THEN 'MODEREMENT_DEFAVORABLE'
         WHEN m.ecart_max <= 3.5 THEN 'DEFAVORABLE'
         WHEN m.ecart_max >  3.5 THEN 'FORTEMENT_DEFAVORABLE'
         ELSE NULL
       END AS evolution,
       m.id,
       m.pointGeographique_id
FROM (
  SELECT DISTINCT ON (b.horizon, b.scenario, b.pointGeographique_id)
         b.id,
         b.horizon,
         b.scenario,
         b.pointGeographique_id,
         b.ecart AS ecart_max
  FROM DonneeClimatiqueBrute b
  WHERE b.indicateur = 'ATXQ90'
  ORDER BY b.horizon, b.scenario, b.pointGeographique_id,
           b.ecart DESC, b.id DESC
) AS m;

INSERT INTO DonneeClimatique
       (id, aleaType, horizon, scenario, evolution,
        donneeClimatiqueBrute_id, pointGeographique_id)
SELECT nextval('DonneeClimatique_id_seq'),
       'VARIABILITE_TEMPERATURES',
       m.horizon,
       m.scenario,
       CASE
         WHEN m.ecart_max <  0.0 THEN 'FAVORABLE'
         WHEN m.ecart_max <= 2.0 THEN 'PAS_EVOLUTION'
         WHEN m.ecart_max <= 4.0 THEN 'LEGEREMENT_DEFAVORABLE'
         WHEN m.ecart_max <= 6.0 THEN 'MODEREMENT_DEFAVORABLE'
         WHEN m.ecart_max <= 8.0 THEN 'DEFAVORABLE'
         WHEN m.ecart_max >  8.0 THEN 'FORTEMENT_DEFAVORABLE'
         ELSE NULL
       END AS evolution,
       m.id,
       m.pointGeographique_id
FROM (
  SELECT DISTINCT ON (b.horizon, b.scenario, b.pointGeographique_id)
         b.id,
         b.horizon,
         b.scenario,
         b.pointGeographique_id,
         b.ecart AS ecart_max
  FROM DonneeClimatiqueBrute b
  WHERE b.indicateur = 'ATRAV'
  ORDER BY b.horizon, b.scenario, b.pointGeographique_id,
           b.ecart DESC, b.id DESC
) AS m;

INSERT INTO DonneeClimatique
       (id, aleaType, horizon, scenario, evolution,
        donneeClimatiqueBrute_id, pointGeographique_id)
SELECT nextval('DonneeClimatique_id_seq'),
       'VAGUE_CHALEUR',
       m.horizon,
       m.scenario,
       CASE
         WHEN m.ecart_max <  0.0 THEN 'FAVORABLE'
         WHEN m.ecart_max <= 0.0 THEN 'PAS_EVOLUTION'
         WHEN m.ecart_max <= 3.0 THEN 'LEGEREMENT_DEFAVORABLE'
         WHEN m.ecart_max <= 6.0 THEN 'MODEREMENT_DEFAVORABLE'
         WHEN m.ecart_max <= 9.0 THEN 'DEFAVORABLE'
         WHEN m.ecart_max >  9.0 THEN 'FORTEMENT_DEFAVORABLE'
         ELSE NULL
       END AS evolution,
       m.id,
       m.pointGeographique_id
FROM (
  SELECT DISTINCT ON (b.horizon, b.scenario, b.pointGeographique_id)
         b.id,
         b.horizon,
         b.scenario,
         b.pointGeographique_id,
         b.ecart AS ecart_max
  FROM DonneeClimatiqueBrute b
  WHERE b.indicateur = 'ATXHWD'
  ORDER BY b.horizon, b.scenario, b.pointGeographique_id,
           b.ecart DESC, b.id DESC
) AS m;

INSERT INTO DonneeClimatique
       (id, aleaType, horizon, scenario, evolution,
        donneeClimatiqueBrute_id, pointGeographique_id)
SELECT nextval('DonneeClimatique_id_seq'),
       'VAGUE_FROID_OU_GEL',
       m.horizon,
       m.scenario,
       CASE
         WHEN m.ecart_max <  0.0 THEN 'FAVORABLE'
         WHEN m.ecart_max <= 0.0 THEN 'PAS_EVOLUTION'
         WHEN m.ecart_max >  0.0 THEN 'FORTEMENT_DEFAVORABLE'
         ELSE NULL
       END AS evolution,
       m.id,
       m.pointGeographique_id
FROM (
  SELECT DISTINCT ON (b.horizon, b.scenario, b.pointGeographique_id)
         b.id,
         b.horizon,
         b.scenario,
         b.pointGeographique_id,
         b.ecart AS ecart_max
  FROM DonneeClimatiqueBrute b
  WHERE b.indicateur = 'ATNFD'
  ORDER BY b.horizon, b.scenario, b.pointGeographique_id,
           b.ecart DESC, b.id DESC
) AS m;

INSERT INTO DonneeClimatique
       (id, aleaType, horizon, scenario, evolution,
        donneeClimatiqueBrute_id, pointGeographique_id)
SELECT nextval('DonneeClimatique_id_seq'),
       'FEU_FORET',
       m.horizon,
       m.scenario,
       CASE
         WHEN m.ecart_max <  0.0  THEN 'FAVORABLE'
         WHEN m.ecart_max <= 0.0  THEN 'PAS_EVOLUTION'
         WHEN m.ecart_max <= 5.0  THEN 'LEGEREMENT_DEFAVORABLE'
         WHEN m.ecart_max <= 10.0 THEN 'MODEREMENT_DEFAVORABLE'
         WHEN m.ecart_max <= 15.0 THEN 'DEFAVORABLE'
         WHEN m.ecart_max >  15.0 THEN 'FORTEMENT_DEFAVORABLE'
         ELSE NULL
       END AS evolution,
       m.id,
       m.pointGeographique_id
FROM (
  SELECT DISTINCT ON (b.horizon, b.scenario, b.pointGeographique_id)
         b.id,
         b.horizon,
         b.scenario,
         b.pointGeographique_id,
         b.ecart AS ecart_max
  FROM DonneeClimatiqueBrute b
  WHERE b.indicateur = 'AIFM20'
  ORDER BY b.horizon, b.scenario, b.pointGeographique_id,
           b.ecart DESC, b.id DESC
) AS m;

INSERT INTO DonneeClimatique
       (id, aleaType, horizon, scenario, evolution,
        donneeClimatiqueBrute_id, pointGeographique_id)
SELECT nextval('DonneeClimatique_id_seq'),
       'TEMPETES',
       m.horizon,
       m.scenario,
       CASE
         WHEN m.ecart_max <  0.0 THEN 'FAVORABLE'
         WHEN m.ecart_max <= 0.0 THEN 'PAS_EVOLUTION'
         WHEN m.ecart_max <= 0.5 THEN 'LEGEREMENT_DEFAVORABLE'
         WHEN m.ecart_max <= 1.0 THEN 'MODEREMENT_DEFAVORABLE'
         WHEN m.ecart_max <= 1.5 THEN 'DEFAVORABLE'
         WHEN m.ecart_max >  1.5 THEN 'FORTEMENT_DEFAVORABLE'
         ELSE NULL
       END AS evolution,
       m.id,
       m.pointGeographique_id
FROM (
  SELECT DISTINCT ON (b.horizon, b.scenario, b.pointGeographique_id)
         b.id,
         b.horizon,
         b.scenario,
         b.pointGeographique_id,
         b.ecart AS ecart_max
  FROM DonneeClimatiqueBrute b
  WHERE b.indicateur = 'AFFQ98'
  ORDER BY b.horizon, b.scenario, b.pointGeographique_id,
           b.ecart DESC, b.id DESC
) AS m;

INSERT INTO DonneeClimatique
       (id, aleaType, horizon, scenario, evolution,
        donneeClimatiqueBrute_id, pointGeographique_id)
SELECT nextval('DonneeClimatique_id_seq'),
       'MODIFICATION_REGIMES_PRECIPITATIONS_PLUIE',
       m.horizon,
       m.scenario,
       CASE
         WHEN m.ecart_max <  0.0 THEN 'FAVORABLE'
         WHEN m.ecart_max <= 0.0 THEN 'PAS_EVOLUTION'
         WHEN m.ecart_max <= 0.1 THEN 'LEGEREMENT_DEFAVORABLE'
         WHEN m.ecart_max <= 0.2 THEN 'MODEREMENT_DEFAVORABLE'
         WHEN m.ecart_max <= 0.3 THEN 'DEFAVORABLE'
         WHEN m.ecart_max >  0.3 THEN 'FORTEMENT_DEFAVORABLE'
         ELSE NULL
       END AS evolution,
       m.id,
       m.pointGeographique_id
FROM (
  SELECT DISTINCT ON (b.horizon, b.scenario, b.pointGeographique_id)
         b.id,
         b.horizon,
         b.scenario,
         b.pointGeographique_id,
         b.ecart AS ecart_max
  FROM DonneeClimatiqueBrute b
  WHERE b.indicateur = 'ARRR'
  ORDER BY b.horizon, b.scenario, b.pointGeographique_id,
           b.ecart DESC, b.id DESC
) AS m;

INSERT INTO DonneeClimatique
       (id, aleaType, horizon, scenario, evolution,
        donneeClimatiqueBrute_id, pointGeographique_id)
SELECT nextval('DonneeClimatique_id_seq'),
       'SECHERESSE',
       m.horizon,
       m.scenario,
       CASE
         WHEN m.ecart_max <  0.0 THEN 'FAVORABLE'
         WHEN m.ecart_max <= 0.0 THEN 'PAS_EVOLUTION'
         WHEN m.ecart_max <= 1.0 THEN 'LEGEREMENT_DEFAVORABLE'
         WHEN m.ecart_max <= 2.0 THEN 'MODEREMENT_DEFAVORABLE'
         WHEN m.ecart_max <= 3.0 THEN 'DEFAVORABLE'
         WHEN m.ecart_max >  3.0 THEN 'FORTEMENT_DEFAVORABLE'
         ELSE NULL
       END AS evolution,
       m.id,
       m.pointGeographique_id
FROM (
  SELECT DISTINCT ON (b.horizon, b.scenario, b.pointGeographique_id)
         b.id,
         b.horizon,
         b.scenario,
         b.pointGeographique_id,
         b.ecart AS ecart_max
  FROM DonneeClimatiqueBrute b
  WHERE b.indicateur = 'APXCDD'
  ORDER BY b.horizon, b.scenario, b.pointGeographique_id,
           b.ecart DESC, b.id DESC
) AS m;

INSERT INTO DonneeClimatique
       (id, aleaType, horizon, scenario, evolution,
        donneeClimatiqueBrute_id, pointGeographique_id)
SELECT nextval('DonneeClimatique_id_seq'),
       'FORTES_PRECIPITATIONS_PLUIE',
       m.horizon,
       m.scenario,
       CASE
         WHEN m.ecart_max <  0.0 THEN 'FAVORABLE'
         WHEN m.ecart_max <= 0.0 THEN 'PAS_EVOLUTION'
         WHEN m.ecart_max <= 1.0 THEN 'LEGEREMENT_DEFAVORABLE'
         WHEN m.ecart_max <= 2.0 THEN 'MODEREMENT_DEFAVORABLE'
         WHEN m.ecart_max <= 3.0 THEN 'DEFAVORABLE'
         WHEN m.ecart_max >  3.0 THEN 'FORTEMENT_DEFAVORABLE'
         ELSE NULL
       END AS evolution,
       m.id,
       m.pointGeographique_id
FROM (
  SELECT DISTINCT ON (b.horizon, b.scenario, b.pointGeographique_id)
         b.id,
         b.horizon,
         b.scenario,
         b.pointGeographique_id,
         b.ecart AS ecart_max
  FROM DonneeClimatiqueBrute b
  WHERE b.indicateur = 'APN20MM'
  ORDER BY b.horizon, b.scenario, b.pointGeographique_id,
           b.ecart DESC, b.id DESC
) AS m;

INSERT INTO DonneeClimatique
       (id, aleaType, horizon, scenario, evolution,
        donneeClimatiqueBrute_id, pointGeographique_id)
SELECT nextval('DonneeClimatique_id_seq'),
       'INONDATION_PLUVIALE',
       m.horizon,
       m.scenario,
       CASE
         WHEN m.ecart_max <  0.0  THEN 'FAVORABLE'
         WHEN m.ecart_max <= 0.0  THEN 'PAS_EVOLUTION'
         WHEN m.ecart_max <= 0.05 THEN 'LEGEREMENT_DEFAVORABLE'
         WHEN m.ecart_max <= 0.1  THEN 'MODEREMENT_DEFAVORABLE'
         WHEN m.ecart_max <= 0.15 THEN 'DEFAVORABLE'
         WHEN m.ecart_max >  0.15 THEN 'FORTEMENT_DEFAVORABLE'
         ELSE NULL
       END AS evolution,
       m.id,
       m.pointGeographique_id
FROM (
  SELECT DISTINCT ON (b.horizon, b.scenario, b.pointGeographique_id)
         b.id,
         b.horizon,
         b.scenario,
         b.pointGeographique_id,
         b.ecart AS ecart_max
  FROM DonneeClimatiqueBrute b
  WHERE b.indicateur = 'ARPQ99'
  ORDER BY b.horizon, b.scenario, b.pointGeographique_id,
           b.ecart DESC, b.id DESC
) AS m;

INSERT INTO DonneeClimatique
       (id, aleaType, horizon, scenario, evolution,
        donneeClimatiqueBrute_id, pointGeographique_id)
SELECT nextval('DonneeClimatique_id_seq'),
       'INONDATION_REMONTEE_NAPPE',
       m.horizon,
       m.scenario,
       CASE
         WHEN m.ecart_max <  0.0 THEN 'FAVORABLE'
         WHEN m.ecart_max <= 0.0 THEN 'PAS_EVOLUTION'
         WHEN m.ecart_max <= 0.1 THEN 'LEGEREMENT_DEFAVORABLE'
         WHEN m.ecart_max <= 0.2 THEN 'MODEREMENT_DEFAVORABLE'
         WHEN m.ecart_max <= 0.3 THEN 'DEFAVORABLE'
         WHEN m.ecart_max >  0.3 THEN 'FORTEMENT_DEFAVORABLE'
         ELSE NULL
       END AS evolution,
       m.id,
       m.pointGeographique_id
FROM (
  SELECT DISTINCT ON (b.horizon, b.scenario, b.pointGeographique_id)
         b.id,
         b.horizon,
         b.scenario,
         b.pointGeographique_id,
         b.ecart AS ecart_max
  FROM DonneeClimatiqueBrute b
  WHERE b.indicateur = 'ARRR'
  ORDER BY b.horizon, b.scenario, b.pointGeographique_id,
           b.ecart DESC, b.id DESC
) AS m;

INSERT INTO DonneeClimatique
       (id, aleaType, horizon, scenario, evolution,
        donneeClimatiqueBrute_id, pointGeographique_id)
SELECT nextval('DonneeClimatique_id_seq'),
       'RETRAIT_GONFLEMENT_ARGILES',
       m.horizon,
       m.scenario,
       CASE
         WHEN m.ecart_max <  0.0 THEN 'FAVORABLE'
         WHEN m.ecart_max <= 0.0 THEN 'PAS_EVOLUTION'
         WHEN m.ecart_max <= 1.0 THEN 'LEGEREMENT_DEFAVORABLE'
         WHEN m.ecart_max <= 2.0 THEN 'MODEREMENT_DEFAVORABLE'
         WHEN m.ecart_max <= 3.0 THEN 'DEFAVORABLE'
         WHEN m.ecart_max >  3.0 THEN 'FORTEMENT_DEFAVORABLE'
         ELSE NULL
       END AS evolution,
       m.id,
       m.pointGeographique_id
FROM (
  SELECT DISTINCT ON (b.horizon, b.scenario, b.pointGeographique_id)
         b.id,
         b.horizon,
         b.scenario,
         b.pointGeographique_id,
         b.ecart AS ecart_max
  FROM DonneeClimatiqueBrute b
  WHERE b.indicateur = 'APXCDD'
  ORDER BY b.horizon, b.scenario, b.pointGeographique_id,
           b.ecart DESC, b.id DESC
) AS m;

INSERT INTO DonneeClimatique
       (id, aleaType, horizon, scenario, evolution,
        donneeClimatiqueBrute_id, pointGeographique_id)
SELECT nextval('DonneeClimatique_id_seq'),
       'GLISSEMENT_TERRAIN',
       m.horizon,
       m.scenario,
       CASE
         WHEN m.ecart_max <  0.0  THEN 'FAVORABLE'
         WHEN m.ecart_max <= 0.0  THEN 'PAS_EVOLUTION'
         WHEN m.ecart_max <= 0.05 THEN 'LEGEREMENT_DEFAVORABLE'
         WHEN m.ecart_max <= 0.1  THEN 'MODEREMENT_DEFAVORABLE'
         WHEN m.ecart_max <= 0.15 THEN 'DEFAVORABLE'
         WHEN m.ecart_max >  0.15 THEN 'FORTEMENT_DEFAVORABLE'
         ELSE NULL
       END AS evolution,
       m.id,
       m.pointGeographique_id
FROM (
  SELECT DISTINCT ON (b.horizon, b.scenario, b.pointGeographique_id)
         b.id,
         b.horizon,
         b.scenario,
         b.pointGeographique_id,
         b.ecart AS ecart_max
  FROM DonneeClimatiqueBrute b
  WHERE b.indicateur = 'ARPQ99'
  ORDER BY b.horizon, b.scenario, b.pointGeographique_id,
           b.ecart DESC, b.id DESC
) AS m;

INSERT INTO DonneeClimatique
       (id, aleaType, horizon, scenario, evolution,
        donneeClimatiqueBrute_id, pointGeographique_id)
SELECT nextval('DonneeClimatique_id_seq'),
       'AFFAISSEMENT',
       m.horizon,
       m.scenario,
       CASE
         WHEN m.ecart_max <  0.0  THEN 'FAVORABLE'
         WHEN m.ecart_max <= 0.0  THEN 'PAS_EVOLUTION'
         WHEN m.ecart_max <= 0.05 THEN 'LEGEREMENT_DEFAVORABLE'
         WHEN m.ecart_max <= 0.1  THEN 'MODEREMENT_DEFAVORABLE'
         WHEN m.ecart_max <= 0.15 THEN 'DEFAVORABLE'
         WHEN m.ecart_max >  0.15 THEN 'FORTEMENT_DEFAVORABLE'
         ELSE NULL
       END AS evolution,
       m.id,
       m.pointGeographique_id
FROM (
  SELECT DISTINCT ON (b.horizon, b.scenario, b.pointGeographique_id)
         b.id,
         b.horizon,
         b.scenario,
         b.pointGeographique_id,
         b.ecart AS ecart_max
  FROM DonneeClimatiqueBrute b
  WHERE b.indicateur = 'ARPQ99'
  ORDER BY b.horizon, b.scenario, b.pointGeographique_id,
           b.ecart DESC, b.id DESC
) AS m;
