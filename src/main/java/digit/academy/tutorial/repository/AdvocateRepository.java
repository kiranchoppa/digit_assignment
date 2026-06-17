package digit.academy.tutorial.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import digit.academy.tutorial.web.models.Advocate;
import digit.academy.tutorial.web.models.AdvocateSearchCriteria;
import digit.academy.tutorial.web.models.AuditDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Repository
public class AdvocateRepository {

    private static final String BASE_SEARCH_QUERY =
            "SELECT id, tenantid, applicationnumber, barregistrationnumber, advocatetype, " +
                    "organisationid, individualid, status, isactive, createdby, createdtime, " +
                    "lastmodifiedby, lastmodifiedtime, additionaldetails " +
                    "FROM eg_advocate";

    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;

    public AdvocateRepository(JdbcTemplate jdbcTemplate, ObjectMapper objectMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.objectMapper = objectMapper;
    }

    public List<Advocate> search(String tenantId, AdvocateSearchCriteria criteria) {
        List<Object> preparedStatementValues = new ArrayList<>();
        StringBuilder query = new StringBuilder(BASE_SEARCH_QUERY);

        addClause(query, preparedStatementValues);
        query.append(" tenantid = ? ");
        preparedStatementValues.add(tenantId);

        if (criteria != null) {
            addFilter(query, preparedStatementValues, "id", criteria.getId());
            addFilter(query, preparedStatementValues, "applicationnumber", criteria.getApplicationNumber());
            addFilter(query, preparedStatementValues, "barregistrationnumber", criteria.getBarRegistrationNumber());
            addFilter(query, preparedStatementValues, "individualid", criteria.getIndividualId());
        }

        query.append(" ORDER BY createdtime DESC ");

        return jdbcTemplate.query(query.toString(), preparedStatementValues.toArray(), advocateRowMapper());
    }

    private void addFilter(StringBuilder query, List<Object> preparedStatementValues, String columnName, String value) {
        if (StringUtils.hasText(value)) {
            addClause(query, preparedStatementValues);
            query.append(columnName).append(" = ? ");
            preparedStatementValues.add(value);
        }
    }

    private void addClause(StringBuilder query, List<Object> preparedStatementValues) {
        if (preparedStatementValues.isEmpty()) {
            query.append(" WHERE ");
        } else {
            query.append(" AND ");
        }
    }

    private RowMapper<Advocate> advocateRowMapper() {
        return new RowMapper<Advocate>() {
            @Override
            public Advocate mapRow(ResultSet rs, int rowNum) throws SQLException {
                AuditDetails auditDetails = AuditDetails.builder()
                        .createdBy(rs.getString("createdby"))
                        .createdTime(rs.getLong("createdtime"))
                        .lastModifiedBy(rs.getString("lastmodifiedby"))
                        .lastModifiedTime(rs.getLong("lastmodifiedtime"))
                        .build();

                return Advocate.builder()
                        .id(toUuid(rs.getString("id")))
                        .tenantId(rs.getString("tenantid"))
                        .applicationNumber(rs.getString("applicationnumber"))
                        .barRegistrationNumber(rs.getString("barregistrationnumber"))
                        .advocateType(rs.getString("advocatetype"))
                        .organisationID(toUuid(rs.getString("organisationid")))
                        .individualId(rs.getString("individualid"))
                        .status(rs.getString("status"))
                        .isActive(rs.getBoolean("isactive"))
                        .auditDetails(auditDetails)
                        .additionalDetails(readJson(rs.getString("additionaldetails")))
                        .build();
            }
        };
    }

    private UUID toUuid(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        return UUID.fromString(value);
    }

    private Object readJson(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }

        try {
            return objectMapper.readValue(value, Object.class);
        } catch (IOException e) {
            log.error("Failed to parse advocate additionalDetails", e);
            return null;
        }
    }
}
