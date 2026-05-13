create unique index organisation_nom_uk on organisation (lower(nom));
create unique index site_organisation_nom_uk on site (organisation_id, lower(nom));