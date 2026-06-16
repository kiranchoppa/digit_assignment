# Requirements Traceability

This project implements the backend-only DIGIT assignment for module `digit_assignment`
with base package `digit.academy.tutorial`.

| Requirement | Implementation |
| --- | --- |
| Advocate create/update/search APIs | `src/main/java/digit/academy/tutorial/web/controllers/AdvocateApiController.java` |
| Advocate clerk create/update/search APIs | `src/main/java/digit/academy/tutorial/web/controllers/ClerkApiController.java` |
| IDGen integration | `AdvocateService` calls `IdgenUtil` for `advocate.applicationid` and `advocate.clerk.applicationid` |
| MDMS v2 integration | `AdvocateService` calls `MdmsUtil`; schemas and payloads are under `masters/` |
| Workflow integration | `AdvocateService` calls `WorkflowUtil`; business services are in `workflow/workflow_config.json` |
| Persister integration | Kafka topics are configured in `application.properties`; mappings are in `persister/persister_config.yaml` |
| Indexer deliverable | `indexer/indexer_config.yaml` |
| Flyway DB migrations | `src/main/resources/db/migration/main/` |
| Postman tests | `api_test/postman_collection_advocate.json` and `api_test/postman_collection_clerk.json` |
| Jar deliverable | `build/digit_assignment.jar` after packaging |

Open items from source documents: exact PRD production tenant and final IDGen sequence names should be confirmed before deploying to a shared DIGIT environment. Local defaults are provided for evaluation.
