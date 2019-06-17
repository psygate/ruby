-- SCRIPT INFORMATION --
-- Types: mysql mariadb
-- Version: 1
-- Upgrades: 0
-- SCRIPT INFORMATION --

START TRANSACTION;
SET foreign_key_checks = 0;

DROP TABLE IF EXISTS ruby_entity CASCADE;
DROP TABLE IF EXISTS ruby_player CASCADE;
DROP TABLE IF EXISTS ruby_location CASCADE;
DROP TABLE IF EXISTS ruby_records CASCADE;
DROP TABLE IF EXISTS ruby_snitches CASCADE;
DROP TABLE IF EXISTS ruby_material CASCADE;
DROP TABLE IF EXISTS ruby_mutes CASCADE;
DROP TABLE IF EXISTS ruby_group_mutes CASCADE;
CREATE TABLE ruby_snitches (
  x           INTEGER     NOT NULL,
  y           INTEGER     NOT NULL,
  z           INTEGER     NOT NULL,
  world_uuid  BINARY(16)  NOT NULL,
  snitch_id   BIGINT      NOT NULL                AUTO_INCREMENT,
  creator     BINARY(16)  NOT NULL,
  creation    TIMESTAMP   NOT NULL,
  group_id    BIGINT      NOT NULL,
  snitch_name VARCHAR(64) NOT NULL,
  snitch_type INTEGER     NOT NULL,
  PRIMARY KEY (x, y, z, world_uuid),
  UNIQUE KEY (snitch_id),
  FOREIGN KEY (creator) REFERENCES nucleus_usernames (puuid),
  FOREIGN KEY (group_id) REFERENCES ivory_groups (group_id)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);

CREATE TABLE ruby_mutes (
  puuid     BINARY(16) NOT NULL,
  snitch_id BIGINT     NOT NULL,
  PRIMARY KEY (puuid),
  FOREIGN KEY (snitch_id) REFERENCES ruby_snitches (snitch_id)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);

CREATE TABLE ruby_group_mutes (
  puuid    BINARY(16) NOT NULL,
  group_id BIGINT     NOT NULL,
  PRIMARY KEY (puuid),
  FOREIGN KEY (group_id) REFERENCES ivory_groups (group_id)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);

CREATE TABLE ruby_records (
  snitch_id     BIGINT    NOT NULL,
  record_id     BIGINT    NOT NULL                AUTO_INCREMENT,
  time_of_event TIMESTAMP NOT NULL                DEFAULT CURRENT_TIMESTAMP,
  log_type      INTEGER   NOT NULL,
  PRIMARY KEY (record_id),
  FOREIGN KEY (snitch_id) REFERENCES ruby_snitches (snitch_id)
    ON UPDATE CASCADE
    ON DELETE CASCADE,
  INDEX (time_of_event),
  INDEX (log_type)
);

CREATE TABLE ruby_location (
  id                   BIGINT     NOT NULL AUTO_INCREMENT,
  x                    INTEGER    NOT NULL,
  y                    INTEGER    NOT NULL,
  z                    INTEGER    NOT NULL,
  world_uuid           BINARY(16) NOT NULL,
  record_id            BIGINT     NOT NULL,
  record_location_type INTEGER    NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (record_id) REFERENCES ruby_records (record_id)
    ON UPDATE CASCADE
    ON DELETE CASCADE,
  INDEX (x, y, z, world_uuid)
);

CREATE TABLE ruby_material (
  id            BIGINT  NOT NULL AUTO_INCREMENT,
  record_id     BIGINT  NOT NULL,
  material_type INTEGER NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (record_id) REFERENCES ruby_records (record_id)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);

CREATE TABLE ruby_player (
  id                 BIGINT     NOT NULL AUTO_INCREMENT,
  record_id          BIGINT     NOT NULL,
  puuid              BINARY(16) NOT NULL,
  record_player_type INTEGER    NOT NULL,
  FOREIGN KEY (record_id) REFERENCES ruby_records (record_id)
    ON UPDATE CASCADE
    ON DELETE CASCADE,
  PRIMARY KEY (id),
  FOREIGN KEY (puuid) REFERENCES nucleus_usernames (puuid)
);

CREATE TABLE ruby_entity (
  id                 BIGINT  NOT NULL AUTO_INCREMENT,
  record_id          BIGINT  NOT NULL,
  entity_type        INTEGER NOT NULL,
  entity_name        VARCHAR(64),
  record_entity_type INTEGER NOT NULL,
  FOREIGN KEY (record_id) REFERENCES ruby_records (record_id)
    ON UPDATE CASCADE
    ON DELETE CASCADE,
  PRIMARY KEY (id)
);
#
# CREATE TABLE ruby_player_events (
#   record_id BIGINT     NOT NULL                AUTO_INCREMENT,
#   puuid     BINARY(16) NOT NULL,
#   x         INTEGER    NOT NULL,
#   y         INTEGER    NOT NULL,
#   z         INTEGER    NOT NULL,
#
#   FOREIGN KEY (record_id) REFERENCES ruby_event (record_id)
#     ON DELETE CASCADE
#     ON UPDATE CASCADE,
#   FOREIGN KEY (puuid) REFERENCES nucleus_usernames (puuid)
# );
#
# CREATE TABLE ruby_player_player_events (
#   record_id  BIGINT     NOT NULL                AUTO_INCREMENT,
#   puuid      BINARY(16) NOT NULL,
#   victimuuid BINARY(16) NOT NULL,
#   x          INTEGER    NOT NULL,
#   y          INTEGER    NOT NULL,
#   z          INTEGER    NOT NULL,
#
#   FOREIGN KEY (record_id) REFERENCES ruby_event (record_id)
#     ON DELETE CASCADE
#     ON UPDATE CASCADE,
#   FOREIGN KEY (puuid) REFERENCES nucleus_usernames (puuid),
#   FOREIGN KEY (victimuuid) REFERENCES nucleus_usernames (puuid)
# );
#
# CREATE TABLE ruby_player_block_events (
#   record_id BIGINT     NOT NULL,
#   x         INTEGER    NOT NULL,
#   y         INTEGER    NOT NULL,
#   z         INTEGER    NOT NULL,
#   material  INTEGER    NOT NULL,
#   puuid     BINARY(16) NOT NULL,
#
#   FOREIGN KEY (record_id) REFERENCES ruby_event (record_id)
#     ON DELETE CASCADE
#     ON UPDATE CASCADE,
#   FOREIGN KEY (puuid) REFERENCES nucleus_usernames (puuid)
# );
#
# CREATE TABLE ruby_player_entity_events (
#   record_id   BIGINT      NOT NULL,
#   x           INTEGER     NOT NULL,
#   y           INTEGER     NOT NULL,
#   z           INTEGER     NOT NULL,
#   puuid       BINARY(16)  NOT NULL,
#   entity_type INTEGER     NOT NULL,
#   entity_name VARCHAR(64) NOT NULL,
#
#   FOREIGN KEY (record_id) REFERENCES ruby_event (record_id)
#     ON DELETE CASCADE
#     ON UPDATE CASCADE,
#   FOREIGN KEY (puuid) REFERENCES nucleus_usernames (puuid)
# );

SET foreign_key_checks = 1;
COMMIT;