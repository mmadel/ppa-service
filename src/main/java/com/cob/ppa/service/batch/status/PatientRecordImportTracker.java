package com.cob.ppa.service.batch.status;

import com.cob.ppa.constant.BatchStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;


@Service
public class PatientRecordImportTracker {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public long insertPendingStatus(String pmrbId) {
        String sql = "INSERT INTO patient_record_import_job (name, pmrb_id,created_at,status) " +
                "VALUES (?, ?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, "Upload " + LocalDateTime.now());
            ps.setString(2, pmrbId);

            ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            ps.setString(4, BatchStatus.Pending.name());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public void setReady(String pmrbId, String status) {
        String sql = "UPDATE patient_record_import_job SET completed_at =?, status = ? WHERE pmrb_id = ?";

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            ps.setString(2, status);
            ps.setString(3, pmrbId);
            return ps;
        });
    }
    public void setFailed(String pmrbId, String status, String errorMessage) {
        String sql = "UPDATE patient_record_import_job SET completed_at =?, status = ?, error_message=? WHERE pmrb_id = ?";

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            ps.setString(2, status);
            ps.setString(3, errorMessage);
            ps.setString(4, pmrbId);
            return ps;
        });
    }
}
