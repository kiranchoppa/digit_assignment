CREATE UNIQUE INDEX uq_eg_advocate_tenant_applicationnumber
    ON eg_advocate (tenantid, applicationnumber);

CREATE INDEX idx_eg_advocate_tenant_status
    ON eg_advocate (tenantid, status);

CREATE INDEX idx_eg_advocate_tenant_barregistrationnumber
    ON eg_advocate (tenantid, barregistrationnumber);

CREATE INDEX idx_eg_advocate_tenant_individualid
    ON eg_advocate (tenantid, individualid);

CREATE INDEX idx_eg_advocate_document_advocateid
    ON eg_advocate_document (advocateid);
