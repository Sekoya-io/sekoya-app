-- Migration from HistoryEntityReferenceSchemaContributor

CREATE TYPE historylog_reference_type AS ENUM (
	'Alea',
	'Announcement',
	'Commune',
	'DataUpgradeRecord',
	'Departement',
	'HistoryDifference',
	'HistoryLog',
	'Organisation',
	'Parameter',
	'Processus',
	'QueuedTaskHolder',
	'Region',
	'Role',
	'Site',
	'User',
	'UserOrganisation');
create cast (varchar as historylog_reference_type) with inout as implicit;
create cast (historylog_reference_type as varchar) with inout as implicit;

-- /Migration from HistoryEntityReferenceSchemaContributor

-- Hibernate hbm2ddl script
create sequence Alea_id_seq start with 1 increment by 1;
create sequence Announcement_id_seq start with 1 increment by 1;
create sequence Commune_id_seq start with 1 increment by 1;
create sequence DataUpgradeRecord_id_seq start with 1 increment by 1;
create sequence Departement_id_seq start with 1 increment by 1;
create sequence HistoryDifference_id_seq start with 1 increment by 1;
create sequence HistoryLog_id_seq start with 1 increment by 1;
create sequence Organisation_id_seq start with 1 increment by 1;
create sequence Parameter_id_seq start with 1 increment by 1;
create sequence Processus_id_seq start with 1 increment by 1;
create sequence QueuedTaskHolder_id_seq start with 1 increment by 1;
create sequence Region_id_seq start with 1 increment by 1;
create sequence Role_id_seq start with 1 increment by 1;
create sequence Site_id_seq start with 1 increment by 1;
create sequence user__id_seq start with 1 increment by 1;
create table Alea (id bigint not null, creation_date timestamp(6) with time zone not null, creation_subject_label text, creation_subject_reference_id bigint, creation_subject_reference_type varchar(255), creation_subject_serialized text, impactPotentielBrut varchar(255) not null check ((impactPotentielBrut in ('OPPORTUNITE','MINEUR','SECONDAIRE','IMPORTANT','MAJEUR','CRITIQUE'))), modification_date timestamp(6) with time zone not null, modification_subject_label text, modification_subject_reference_id bigint, modification_subject_reference_type varchar(255), modification_subject_serialized text, sensibilite varchar(255) not null check ((sensibilite in ('OPPORTUNITE','TRES_FAIBLE','FAIBLE','MOYENNE','FORTE','TRES_FORTE'))), type varchar(255) not null check ((type in ('MODIFICATION_TEMPERATURES_AIR','STRESS_THERMIQUE','VARIABILITE_TEMPERATURES','VAGUE_CHALEUR','VAGUE_FROID_OU_GEL','FEU_FORET','TEMPETES','MODIFICATION_REGIMES_PRECIPITATIONS_PLUIE','EVOLUTION_PRECIPITATIONS_NEIGEUSES_MONTAGNE','VARIATION_PRECIPITATIONS_OU_HYDROLOGIE','STRESS_HYDRIQUE','INTRUSION_SALINE','HAUSSE_NIVEAU_MER','SECHERESSE','FORTES_PRECIPITATIONS_PLUIE','INONDATION_FLUVIALE','INONDATION_PLUVIALE','INONDATION_REMONTEE_NAPPE','INONDATION_COTIERE','RETRAIT_GONFLEMENT_ARGILES','GLISSEMENT_TERRAIN','AFFAISSEMENT'))), processus_id bigint not null, primary key (id), unique (processus_id, type));
create table Announcement (id bigint not null, content text, creation_date timestamp(6) with time zone not null, creation_subject_label text, creation_subject_reference_id bigint, creation_subject_reference_type varchar(255), creation_subject_serialized text, enabled boolean not null, modification_date timestamp(6) with time zone not null, modification_subject_label text, modification_subject_reference_id bigint, modification_subject_reference_type varchar(255), modification_subject_serialized text, publication_endDateTime timestamp(6) not null, publication_startDateTime timestamp(6) not null, type varchar(255) not null check ((type in ('NOTIFICATION','UNAVAILABILITY'))), unavailability_endDateTime timestamp(6), unavailability_startDateTime timestamp(6), primary key (id));
create table Commune (id bigint not null, deleteable boolean not null, disableable boolean not null, editable boolean not null, enabled boolean not null, position integer not null, label varchar(32600) not null, codeInsee varchar(5) not null, creation_date timestamp(6) with time zone not null, creation_subject_label text, creation_subject_reference_id bigint, creation_subject_reference_type varchar(255), creation_subject_serialized text, modification_date timestamp(6) with time zone not null, modification_subject_label text, modification_subject_reference_id bigint, modification_subject_reference_type varchar(255), modification_subject_serialized text, typeInsee varchar(255) not null check ((typeInsee in ('COMMUNE','ARRONDISSEMENT_MUNICIPAL','COMMUNE_DELEGUEE','COMMUNE_ASSOCIEE','COMMUNE_COMER'))), departement_id bigint, parent_id bigint, primary key (id), constraint commune_departement_check check (departement_id IS NOT NULL OR typeInsee IN ('COMMUNE_DELEGUEE', 'COMMUNE_ASSOCIEE')), constraint commune_parent_check check ((typeInsee IN ('COMMUNE', 'COMMUNE_COMER') AND parent_id IS NULL)
  OR (typeInsee IN ('ARRONDISSEMENT_MUNICIPAL', 'COMMUNE_DELEGUEE', 'COMMUNE_ASSOCIEE') AND parent_id IS NOT NULL)
));
create table commune_codepostal (Commune_id bigint not null, codepostal varchar(255));
create table DataUpgradeRecord (id bigint not null, autoPerform boolean not null, done boolean not null, executionDate timestamp(6) with time zone, name varchar(255) not null, primary key (id), unique (name));
create table Departement (id bigint not null, deleteable boolean not null, disableable boolean not null, editable boolean not null, enabled boolean not null, position integer not null, label varchar(32600) not null, codeInsee varchar(255) not null unique, creation_date timestamp(6) with time zone not null, creation_subject_label text, creation_subject_reference_id bigint, creation_subject_reference_type varchar(255), creation_subject_serialized text, modification_date timestamp(6) with time zone not null, modification_subject_label text, modification_subject_reference_id bigint, modification_subject_reference_type varchar(255), modification_subject_serialized text, region_id bigint not null, primary key (id));
create table HistoryDifference (id bigint not null, after_label text, after_reference_id bigint, after_reference_type historylog_reference_type, after_serialized text, before_label text, before_reference_id bigint, before_reference_type historylog_reference_type, before_serialized text, eventType varchar(255) not null check ((eventType in ('ADDED','UPDATED','REMOVED','UNTOUCHED'))), path_key_label text, path_key_reference_id bigint, path_key_reference_type historylog_reference_type, path_key_serialized text, path_path text not null, parentDifference_id bigint, parentLog_id bigint, differences_ORDER integer check ((differences_ORDER>=0)), primary key (id));
create table HistoryLog (id bigint not null, comment text, date timestamp(6) with time zone not null, eventType varchar(255) not null check ((eventType in ('CREATE','UPDATE','DELETE','DISABLE','ENABLE','SIGN_IN','SIGN_IN_FAIL','PASSWORD_RESET_REQUEST','PASSWORD_CREATION_REQUEST','PASSWORD_UPDATE'))), mainObject_label text, mainObject_reference_id bigint, mainObject_reference_type historylog_reference_type, mainObject_serialized text, object1_label text, object1_reference_id bigint, object1_reference_type historylog_reference_type, object1_serialized text, object2_label text, object2_reference_id bigint, object2_reference_type historylog_reference_type, object2_serialized text, object3_label text, object3_reference_id bigint, object3_reference_type historylog_reference_type, object3_serialized text, object4_label text, object4_reference_id bigint, object4_reference_type historylog_reference_type, object4_serialized text, subject_label text, subject_reference_id bigint, subject_reference_type historylog_reference_type, subject_serialized text, primary key (id));
create table Organisation (id bigint not null, chiffreAffaires integer, creation_date timestamp(6) with time zone not null, creation_subject_label text, creation_subject_reference_id bigint, creation_subject_reference_type varchar(255), creation_subject_serialized text, modification_date timestamp(6) with time zone not null, modification_subject_label text, modification_subject_reference_id bigint, modification_subject_reference_type varchar(255), modification_subject_serialized text, nom varchar(255) not null, primary key (id), unique (nom));
create table Parameter (id bigint not null, name varchar(255) not null unique, stringValue text, primary key (id));
create table Processus (id bigint not null, actif boolean not null, creation_date timestamp(6) with time zone not null, creation_subject_label text, creation_subject_reference_id bigint, creation_subject_reference_type varchar(255), creation_subject_serialized text, description text, modification_date timestamp(6) with time zone not null, modification_subject_label text, modification_subject_reference_id bigint, modification_subject_reference_type varchar(255), modification_subject_serialized text, nom varchar(255) not null, priorite varchar(255) not null check ((priorite in ('MINEUR','SECONDAIRE','IMPORTANT','MAJEUR','VITAL'))), type varchar(255) not null check ((type in ('TYPE1','TYPE2'))), site_id bigint not null, primary key (id));
create table QueuedTaskHolder (id bigint not null, creationDate timestamp(6) with time zone not null, endDate timestamp(6) with time zone, name varchar(255) not null, queueId varchar(255), report text, result varchar(255) check ((result in ('SUCCESS','WARN','ERROR','FATAL'))), serializedTask text not null, stackTrace text, startDate timestamp(6) with time zone, status varchar(255) not null check ((status in ('TO_RUN','RUNNING','COMPLETED','FAILED','INTERRUPTED','CANCELLED'))), taskType varchar(255) not null, triggeringDate timestamp(6) with time zone, optLock integer not null, primary key (id));
create table Region (id bigint not null, deleteable boolean not null, disableable boolean not null, editable boolean not null, enabled boolean not null, position integer not null, label varchar(32600) not null, codeInsee varchar(255) not null unique, creation_date timestamp(6) with time zone not null, creation_subject_label text, creation_subject_reference_id bigint, creation_subject_reference_type varchar(255), creation_subject_serialized text, modification_date timestamp(6) with time zone not null, modification_subject_label text, modification_subject_reference_id bigint, modification_subject_reference_type varchar(255), modification_subject_serialized text, primary key (id));
create table Role (id bigint not null, enumKey varchar(255) not null unique check ((enumKey in ('ORGANISATION','ADMINISTRATEUR_FONCTIONNEL'))), title varchar(255) not null unique, primary key (id));
create table Role_permissions (Role_id bigint not null, permissions varchar(255));
create table Site (id bigint not null, actif boolean not null, adresse_adresse1 varchar(255) not null, adresse_adresse2 varchar(255), adresse_codePostal varchar(255) not null, chiffreAffaires integer, creation_date timestamp(6) with time zone not null, creation_subject_label text, creation_subject_reference_id bigint, creation_subject_reference_type varchar(255), creation_subject_serialized text, latitude numeric(9,6), longitude numeric(10,6), modification_date timestamp(6) with time zone not null, modification_subject_label text, modification_subject_reference_id bigint, modification_subject_reference_type varchar(255), modification_subject_serialized text, nom varchar(255) not null, typologie varchar(255) not null check ((typologie in ('TYPOLOGIE1','TYPOLOGIE2'))), adresse_commune_id bigint not null, organisation_id bigint not null, primary key (id), unique (organisation_id, nom));
create table user_ (id bigint not null, announcementInformation_lastActionDate timestamp(6) with time zone, announcementInformation_open boolean not null, creation_date timestamp(6) with time zone not null, creation_subject_label text, creation_subject_reference_id bigint, creation_subject_reference_type varchar(255), creation_subject_serialized text, emailAddress varchar(255), enabled boolean not null, firstName varchar(255) not null, lastLoginDate timestamp(6) with time zone, lastName varchar(255) not null, locale varchar(255), modification_date timestamp(6) with time zone not null, modification_subject_label text, modification_subject_reference_id bigint, modification_subject_reference_type varchar(255), modification_subject_serialized text, passwordHash varchar(255), passwordInformation_lastUpdateDate timestamp(6) with time zone, passwordRecoveryRequest_creationDate timestamp(6) with time zone, passwordRecoveryRequest_initiator varchar(255) check ((passwordRecoveryRequest_initiator in ('USER','ADMIN'))), passwordRecoveryRequest_token varchar(255), passwordRecoveryRequest_type varchar(255) check ((passwordRecoveryRequest_type in ('CREATION','RESET'))), type varchar(255) not null check ((type in ('ORGANISATION','ADMINISTRATEUR_FONCTIONNEL','ADMINISTRATEUR_TECHNIQUE'))), username varchar(255) not null, primary key (id), unique (username));
create table user__passwordInformation_history (user__id bigint not null, passwordInformation_history varchar(255), history_ORDER integer not null check ((history_ORDER>=0)), primary key (user__id, history_ORDER));
create table user__Role (users_id bigint not null, roles_id bigint not null, primary key (users_id, roles_id));
create table UserOrganisation (id bigint not null, organisation_id bigint not null, primary key (id));
create index commune_codepostal_commune_id_idx on commune_codepostal (commune_id);
create index commune_codepostal_codepostal_idx on commune_codepostal (codepostal);
create index idx_HistoryDifference_parentLog on HistoryDifference (parentLog_id);
create index idx_HistoryDifference_parentDifference on HistoryDifference (parentDifference_id);
create index user__role_role_id_idx on user__Role (roles_id);
alter table if exists Alea add constraint FKtf8fcysbiqh8qcq9s9pg51cfh foreign key (processus_id) references Processus;
alter table if exists Commune add constraint FKbmxwiwglv5mtuwwyp3bl8nkg6 foreign key (departement_id) references Departement;
alter table if exists Commune add constraint FKrld6a6d192vwce9cfrlcb662y foreign key (parent_id) references Commune;
alter table if exists commune_codepostal add constraint FKjtnhrxh29j8m58apewbplra9x foreign key (Commune_id) references Commune;
alter table if exists Departement add constraint FKg7grqnmqlle0153ax0iabdtg1 foreign key (region_id) references Region;
alter table if exists HistoryDifference add constraint FKbs6nv5pjsfuj7na52g16pfbrw foreign key (parentDifference_id) references HistoryDifference;
alter table if exists HistoryDifference add constraint FK77dlplmm50qgjbl1cqv4nrgoi foreign key (parentLog_id) references HistoryLog;
alter table if exists Processus add constraint FK5j9rw5hy1hnshib42ox2fqndw foreign key (site_id) references Site;
alter table if exists Role_permissions add constraint FK1r7f38f77hj0tgnruphe4t90m foreign key (Role_id) references Role;
alter table if exists Site add constraint FK5si86xji1y1hn43dx2mfcaevo foreign key (adresse_commune_id) references Commune;
alter table if exists Site add constraint FKmuysj9bsk03tw9he5lr0fmrdw foreign key (organisation_id) references Organisation;
alter table if exists user__passwordInformation_history add constraint FKhhvmnfe1oakm04ad2k6xbrh59 foreign key (user__id) references user_;
alter table if exists user__Role add constraint FKrlkperdervjn1pk1uqlmniup foreign key (roles_id) references Role;
alter table if exists user__Role add constraint FK8cno5ok6839ygx5op7mpw99ib foreign key (users_id) references user_;
alter table if exists UserOrganisation add constraint FKgwcplaiupdklsa32nn2jhqgym foreign key (organisation_id) references Organisation;
alter table if exists UserOrganisation add constraint FKlj562n027qvid0t74rlrkkly2 foreign key (id) references user_;

-- Hibernate hbm2ddl script (end)