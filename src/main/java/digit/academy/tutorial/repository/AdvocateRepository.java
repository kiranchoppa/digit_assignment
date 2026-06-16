package digit.academy.tutorial.repository;

import digit.academy.tutorial.web.models.Advocate;
import digit.academy.tutorial.web.models.AdvocateClerk;
import digit.academy.tutorial.web.models.AuditDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class AdvocateRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public AdvocateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Advocate> searchAdvocates(String tenantId, String id, String applicationNumber,
                                          String barRegistrationNumber, String individualId, String status) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM eg_advocate WHERE 1=1");
        append(sql, params, "tenantid", tenantId);
        append(sql, params, "id", id);
        append(sql, params, "applicationnumber", applicationNumber);
        append(sql, params, "barregistrationnumber", barRegistrationNumber);
        append(sql, params, "individualid", individualId);
        append(sql, params, "status", status);

        return jdbcTemplate.query(sql.toString(), params.toArray(), (rs, rowNum) -> Advocate.builder()
            .id(UUID.fromString(rs.getString("id")))
            .tenantId(rs.getString("tenantid"))
            .applicationNumber(rs.getString("applicationnumber"))
            .barRegistrationNumber(rs.getString("barregistrationnumber"))
            .advocateType(rs.getString("advocatetype"))
            .organisationID(toUuid(rs.getString("organisationid")))
            .individualId(rs.getString("individualid"))
            .status(rs.getString("status"))
            .isActive(rs.getBoolean("isactive"))
            .auditDetails(AuditDetails.builder()
                .createdBy(rs.getString("createdby"))
                .createdTime(rs.getLong("createdtime"))
                .lastModifiedBy(rs.getString("lastmodifiedby"))
                .lastModifiedTime(rs.getLong("lastmodifiedtime"))
                .build())
            .build());
    }

    public List<AdvocateClerk> searchClerks(String tenantId, String id, String applicationNumber,
                                            String stateRegnNumber, String individualId, String status) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM eg_advocate_clerk WHERE 1=1");
        append(sql, params, "tenantid", tenantId);
        append(sql, params, "id", id);
        append(sql, params, "applicationnumber", applicationNumber);
        append(sql, params, "stateregnnumber", stateRegnNumber);
        append(sql, params, "individualid", individualId);
        append(sql, params, "status", status);

        return jdbcTemplate.query(sql.toString(), params.toArray(), (rs, rowNum) -> AdvocateClerk.builder()
            .id(UUID.fromString(rs.getString("id")))
            .tenantId(rs.getString("tenantid"))
            .applicationNumber(rs.getString("applicationnumber"))
            .stateRegnNumber(rs.getString("stateregnnumber"))
            .individualId(rs.getString("individualid"))
            .status(rs.getString("status"))
            .isActive(rs.getBoolean("isactive"))
            .auditDetails(AuditDetails.builder()
                .createdBy(rs.getString("createdby"))
                .createdTime(rs.getLong("createdtime"))
                .lastModifiedBy(rs.getString("lastmodifiedby"))
                .lastModifiedTime(rs.getLong("lastmodifiedtime"))
                .build())
            .build());
    }

    private void append(StringBuilder sql, List<Object> params, String column, String value) {
        if (!StringUtils.isEmpty(value)) {
            sql.append(" AND ").append(column).append(" = ?");
            params.add(value);
        }
    }

    private UUID toUuid(String value) {
        return StringUtils.isEmpty(value) ? null : UUID.fromString(value);
    }
}
