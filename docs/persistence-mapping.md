# Persistence Mapping

This document maps the persistent domain fields from
`advocate-api-0.1.0.yaml` to the local PostgreSQL schema.

The tables will be created in the `public` schema of the `digit_assignment`
database through Flyway migrations.

## Mapping Rules

- `Advocate` and `AdvocateClerk` are persistent domain entities and use parent
  tables.
- Each entity's `documents` array uses a separate child table because one
  parent can contain multiple documents.
- `auditDetails` is flattened into audit columns on each parent table.
- `additionalDetails` is stored as `JSONB`.
- `workflow` is not persisted as domain data. It is a request sent to the
  Workflow service.
- Request, response, search-criteria, pagination, and API metadata models are
  not persisted.
- Only `tenantId` and `individualId` are required by the OpenAPI for the parent
  entities. Other `NOT NULL` constraints are persistence decisions that must be
  confirmed before migrations are written.

## `eg_advocate`

Source model: `Advocate`

| JSON path | SQL column | SQL type | OpenAPI required | Proposed constraint/default | Search/index use | Persister JSON path |
|---|---|---|---|---|---|---|
| `advocate.id` | `id` | `VARCHAR(64)` | No, read-only generated UUID | Primary key, `NOT NULL` | Search by ID | `$.advocate.id` |
| `advocate.tenantId` | `tenantid` | `VARCHAR(128)` | Yes | `NOT NULL` | Included in tenant-scoped indexes | `$.advocate.tenantId` |
| `advocate.applicationNumber` | `applicationnumber` | `VARCHAR(64)` | No | `NOT NULL` | Search by application number | `$.advocate.applicationNumber` |
| `advocate.barRegistrationNumber` | `barregistrationnumber` | `VARCHAR(64)` | No | Nullable | Search by bar registration number | `$.advocate.barRegistrationNumber` |
| `advocate.advocateType` | `advocatetype` | `VARCHAR(64)` | No | Nullable; store MDMS master code | None currently defined | `$.advocate.advocateType` |
| `advocate.organisationID` | `organisationid` | `VARCHAR(64)` | No | Nullable | None currently defined | `$.advocate.organisationID` |
| `advocate.individualId` | `individualid` | `VARCHAR(64)` | Yes | `NOT NULL` | Search by individual ID | `$.advocate.individualId` |
| `advocate.status` | `status` | `VARCHAR(64)` | No | Nullable | Search by status | `$.advocate.status` |
| `advocate.isActive` | `isactive` | `BOOLEAN` | No | `NOT NULL DEFAULT TRUE` | None currently defined | `$.advocate.isActive` |
| `advocate.auditDetails.createdBy` | `createdby` | `VARCHAR(64)` | No | Nullable; confirm before migration | None | `$.advocate.auditDetails.createdBy` |
| `advocate.auditDetails.createdTime` | `createdtime` | `BIGINT` | No | Nullable; confirm before migration | Supports oldest-first ordering | `$.advocate.auditDetails.createdTime` |
| `advocate.auditDetails.lastModifiedBy` | `lastmodifiedby` | `VARCHAR(64)` | No | Nullable; confirm before migration | None | `$.advocate.auditDetails.lastModifiedBy` |
| `advocate.auditDetails.lastModifiedTime` | `lastmodifiedtime` | `BIGINT` | No | Nullable; confirm before migration | None | `$.advocate.auditDetails.lastModifiedTime` |
| `advocate.additionalDetails` | `additionaldetails` | `JSONB` | No | Nullable | None | `$.advocate.additionalDetails` |

Not stored in this table:

- `advocate.documents` is stored in `eg_advocate_document`.
- `advocate.workflow` is sent to the Workflow service and is not persisted here.

## `eg_advocate_clerk`

Source model: `AdvocateClerk`

| JSON path | SQL column | SQL type | OpenAPI required | Proposed constraint/default | Search/index use | Persister JSON path |
|---|---|---|---|---|---|---|
| `clerk.id` | `id` | `VARCHAR(64)` | No, read-only generated UUID | Primary key, `NOT NULL` | Search by ID | `$.clerk.id` |
| `clerk.tenantId` | `tenantid` | `VARCHAR(128)` | Yes | `NOT NULL` | Included in tenant-scoped indexes | `$.clerk.tenantId` |
| `clerk.applicationNumber` | `applicationnumber` | `VARCHAR(64)` | No | Proposed unique with `tenantid`; confirm `NOT NULL` | Search by application number | `$.clerk.applicationNumber` |
| `clerk.stateRegnNumber` | `stateregnnumber` | `VARCHAR(64)` | No | Nullable | Search by state registration number | `$.clerk.stateRegnNumber` |
| `clerk.individualId` | `individualid` | `VARCHAR(64)` | Yes | `NOT NULL` | Search by individual ID | `$.clerk.individualId` |
| `clerk.status` | `status` | `VARCHAR(64)` | No | Nullable | Search by status | `$.clerk.status` |
| `clerk.isActive` | `isactive` | `BOOLEAN` | No | `NOT NULL DEFAULT TRUE` | None currently defined | `$.clerk.isActive` |
| `clerk.auditDetails.createdBy` | `createdby` | `VARCHAR(64)` | No | Nullable; confirm before migration | None | `$.clerk.auditDetails.createdBy` |
| `clerk.auditDetails.createdTime` | `createdtime` | `BIGINT` | No | Nullable; confirm before migration | Supports oldest-first ordering | `$.clerk.auditDetails.createdTime` |
| `clerk.auditDetails.lastModifiedBy` | `lastmodifiedby` | `VARCHAR(64)` | No | Nullable; confirm before migration | None | `$.clerk.auditDetails.lastModifiedBy` |
| `clerk.auditDetails.lastModifiedTime` | `lastmodifiedtime` | `BIGINT` | No | Nullable; confirm before migration | None | `$.clerk.auditDetails.lastModifiedTime` |
| `clerk.additionalDetails` | `additionaldetails` | `JSONB` | No | Nullable | None | `$.clerk.additionalDetails` |

Not stored in this table:

- `clerk.documents` is stored in `eg_advocate_clerk_document`.
- `clerk.workflow` is sent to the Workflow service and is not persisted here.

## `eg_advocate_document`

Source model: each item in `Advocate.documents`

| JSON path | SQL column | SQL type | OpenAPI required | Proposed constraint/default | Search/index use | Persister JSON path |
|---|---|---|---|---|---|---|
| `document.id` | `id` | `VARCHAR(64)` | No | Primary key, `NOT NULL`; generated before persistence | None currently defined | `$.id` |
| Parent `advocate.id` | `advocateid` | `VARCHAR(64)` | Derived relation | `NOT NULL`, foreign key to `eg_advocate(id)` | Index for parent lookup | Parent advocate ID |
| `document.documentType` | `documenttype` | `VARCHAR(128)` | No | Nullable | None currently defined | `$.documentType` |
| `document.fileStore` | `filestore` | `VARCHAR(256)` | No | Nullable | None currently defined | `$.fileStore` |
| `document.documentUid` | `documentuid` | `VARCHAR(64)` | No | Nullable | None currently defined | `$.documentUid` |
| `document.additionalDetails` | `additionaldetails` | `JSONB` | No | Nullable | None | `$.additionalDetails` |

## `eg_advocate_clerk_document`

Source model: each item in `AdvocateClerk.documents`

| JSON path | SQL column | SQL type | OpenAPI required | Proposed constraint/default | Search/index use | Persister JSON path |
|---|---|---|---|---|---|---|
| `document.id` | `id` | `VARCHAR(64)` | No | Primary key, `NOT NULL`; generated before persistence | None currently defined | `$.id` |
| Parent `clerk.id` | `clerkid` | `VARCHAR(64)` | Derived relation | `NOT NULL`, foreign key to `eg_advocate_clerk(id)` | Index for parent lookup | Parent clerk ID |
| `document.documentType` | `documenttype` | `VARCHAR(128)` | No | Nullable | None currently defined | `$.documentType` |
| `document.fileStore` | `filestore` | `VARCHAR(256)` | No | Nullable | None currently defined | `$.fileStore` |
| `document.documentUid` | `documentuid` | `VARCHAR(64)` | No | Nullable | None currently defined | `$.documentUid` |
| `document.additionalDetails` | `additionaldetails` | `JSONB` | No | Nullable | None | `$.additionalDetails` |

## Proposed Constraints and Indexes

These should be reviewed before creating the Flyway migrations:

| Table | Proposed constraint or index | Reason |
|---|---|---|
| `eg_advocate` | Primary key on `id` | Entity identity |
| `eg_advocate` | Unique index on `(tenantid, applicationnumber)` | Application numbers should identify an application within a tenant |
| `eg_advocate` | Index on `(tenantid, status)` | Status search endpoint |
| `eg_advocate` | Index on `(tenantid, barregistrationnumber)` | Advocate search criterion |
| `eg_advocate` | Index on `(tenantid, individualid)` | Advocate search criterion |
| `eg_advocate_document` | Foreign key from `advocateid` to `eg_advocate(id)` | Parent-child integrity |
| `eg_advocate_document` | Index on `advocateid` | Load documents for an advocate |
| `eg_advocate_clerk` | Primary key on `id` | Entity identity |
| `eg_advocate_clerk` | Unique index on `(tenantid, applicationnumber)` | Application numbers should identify an application within a tenant |
| `eg_advocate_clerk` | Index on `(tenantid, status)` | Status search endpoint |
| `eg_advocate_clerk` | Index on `(tenantid, stateregnnumber)` | Clerk search criterion |
| `eg_advocate_clerk` | Index on `(tenantid, individualid)` | Clerk search criterion |
| `eg_advocate_clerk_document` | Foreign key from `clerkid` to `eg_advocate_clerk(id)` | Parent-child integrity |
| `eg_advocate_clerk_document` | Index on `clerkid` | Load documents for a clerk |

## Models Not Persisted

| OpenAPI model/field | Reason |
|---|---|
| `AdvocateRequest`, `AdvocateClerkRequest` | API request wrappers |
| `AdvocateSearchRequest`, `AdvocateClerkSearchRequest` | Temporary search input |
| `AdvocateSearchCriteria`, `AdvocateClerkSearchCriteria` | Temporary query filters; their fields determine indexes |
| Response and list-response models | API response wrappers |
| `RequestInfo`, `ResponseInfo`, `Pagination` | API metadata |
| `workflow` | Command sent to the external Workflow service |

## Decisions to Confirm Before Writing Migrations

1. Whether generated `applicationNumber` values must be `NOT NULL` at persistence
   time.
2. Whether application-number uniqueness is scoped by `tenantid`.
3. Whether audit fields must be `NOT NULL` at persistence time.
4. Whether document IDs are always generated before persistence.
5. Whether deleting a parent should cascade-delete its documents.
6. Whether fuzzy/partial search requires PostgreSQL trigram indexes in addition
   to the standard indexes above.
7. Whether one registration per `individualId` should be enforced in these
   tables. The PRD states one registration per mobile number, but mobile number
   belongs to the external user/individual registry.
