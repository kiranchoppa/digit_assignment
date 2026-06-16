# MDMS v2 Payloads

These payloads are local setup aids for Phase 1.

Confirmed from `PRD.txt`:

- `UserType` values:
  - `ADVOCATE` / `Advocate`
  - `ADVOCATE_CLERK` / `Advocate Clerk`
- Advocate application ID display format: `ADVOC_<SeqNumber>_<Year>`
- Advocate clerk application ID display format: `ADVOC_CLERK_<SeqNumber>_<Year>`

Unresolved in the available PRD/OpenAPI:

- Complete `AdvocateType` code list.
- Bar council prefix list and exact `barRegistrationNumber` validation patterns.
- Exact MDMS module name and schema-code naming convention expected by the local MDMS v2 instance.
- Exact IDGen `idName` values and DIGIT IDGen format syntax.

Do not upload guessed production codes for unresolved masters. Fill those only after the assignment
source of truth provides the exact values.
