package com.cob.ppa.service.tracker;

import com.cob.ppa.constant.RequestStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;

@Service
public class RequestTrackerService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public long initRequest(String requestId) {
        String sql = "INSERT INTO request_logs (username,request_id,status, created_at, updated_at) " +
                "VALUES (?, ?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, getUserName());
            ps.setString(2, requestId);
            ps.setString(3, RequestStatus.Pending.name());
            ps.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            ps.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public void setCompleted(String requestId) {
        String sql = "UPDATE request_logs SET  updated_at=? ,status = ? WHERE request_id = ? ";
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            ps.setString(2, RequestStatus.Ready.name());
            ps.setString(3, requestId);
            return ps;
        });
    }

    public void setFailed(String requestId, String errorMessage) {
        String sql = "UPDATE request_logs SET  updated_at=? ,error_message =? , status = ? WHERE request_id = ? ";
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            ps.setString(2, errorMessage);
            ps.setString(3, RequestStatus.Failed.name());
            ps.setString(4, requestId);
            return ps;
        });
    }

    private String getUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return null;
    }
}
