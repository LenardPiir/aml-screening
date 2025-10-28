CREATE SCHEMA IF NOT EXISTS aml_screening_schema;

CREATE TABLE IF NOT EXISTS aml_screening_schema.databasechangeloglock (
    id BIGINT NOT NULL PRIMARY KEY,
    locked BOOLEAN NOT NULL,
    lockgranted TIMESTAMP,
    lockedby VARCHAR(255),
    CONSTRAINT databasechangeloglock_id_key UNIQUE (id)
    );

CREATE TABLE IF NOT EXISTS aml_screening_schema.databasechangelog (
    id VARCHAR(255) NOT NULL PRIMARY KEY,
    author VARCHAR(255) NOT NULL,
    filename VARCHAR(255) NOT NULL,
    dateexecuted TIMESTAMP NOT NULL,
    orderexecuted INT NOT NULL,
    exectype VARCHAR(10) NOT NULL,
    md5sum VARCHAR(35),
    description VARCHAR(255),
    comments TEXT,
    tag VARCHAR(255),
    liquibase VARCHAR(20),
    contextsexe VARCHAR(255),
    labels VARCHAR(255),
    deployment_id VARCHAR(10)
    );