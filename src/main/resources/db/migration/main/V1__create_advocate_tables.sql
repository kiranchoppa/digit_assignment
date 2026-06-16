CREATE TABLE eg_advocate (
    id                      VARCHAR(64) PRIMARY KEY,
    tenantid                VARCHAR(128) NOT NULL,
    applicationnumber       VARCHAR(64) NOT NULL,
    barregistrationnumber   VARCHAR(64) NOT NULL,
    advocatetype            VARCHAR(64) NOT NULL,
    organisationid          VARCHAR(64),
    individualid            VARCHAR(64) NOT NULL,
    status                  VARCHAR(64),
    isactive                BOOLEAN NOT NULL DEFAULT TRUE,
    createdby               VARCHAR(64) NOT NULL,
    createdtime             BIGINT NOT NULL,
    lastmodifiedby          VARCHAR(64) NOT NULL,
    lastmodifiedtime        BIGINT NOT NULL,
    additionaldetails       JSONB
);

CREATE TABLE eg_advocate_document (
    id                      VARCHAR(64) PRIMARY KEY,
    advocateid              VARCHAR(64) NOT NULL,
    tenantid                VARCHAR(128) NOT NULL,
    documenttype            VARCHAR(128),
    filestore               VARCHAR(256),
    documentuid             VARCHAR(64),
    additionaldetails       JSONB,
    CONSTRAINT fk_eg_advocate_document_advocate
        FOREIGN KEY (advocateid)
        REFERENCES eg_advocate(id)
        ON DELETE CASCADE
);
