CREATE TABLE eg_advocate_clerk (
    id                      VARCHAR(64) PRIMARY KEY,
    tenantid                VARCHAR(128) NOT NULL,
    applicationnumber       VARCHAR(64) NOT NULL,
    stateregnnumber         VARCHAR(64),
    individualid            VARCHAR(64) NOT NULL,
    status                  VARCHAR(64),
    isactive                BOOLEAN NOT NULL DEFAULT TRUE,
    createdby               VARCHAR(64) NOT NULL,
    createdtime             BIGINT NOT NULL,
    lastmodifiedby          VARCHAR(64) NOT NULL,
    lastmodifiedtime        BIGINT NOT NULL,
    additionaldetails       JSONB
);

CREATE TABLE eg_advocate_clerk_document (
    id                      VARCHAR(64) PRIMARY KEY,
    clerkid                 VARCHAR(64) NOT NULL,
    documenttype            VARCHAR(128),
    filestore               VARCHAR(256),
    documentuid             VARCHAR(64),
    additionaldetails       JSONB,
    CONSTRAINT fk_eg_advocate_clerk_document_clerk
        FOREIGN KEY (clerkid)
        REFERENCES eg_advocate_clerk(id)
        ON DELETE CASCADE
);
