package com.college.dao;

import com.college.model.Fee;
import com.college.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FeeDAO {

    public Fee getFeeByRollNo(String rollNo) {
        String query = "SELECT * FROM fees WHERE roll_no = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, rollNo);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Fee f = new Fee();
                    f.setFeeId(rs.getInt("fee_id"));
                    f.setRollNo(rs.getString("roll_no"));
                    f.setTotalFees(rs.getDouble("total_fees"));
                    f.setPaidFees(rs.getDouble("paid_fees"));
                    f.setDueFees(rs.getDouble("due_fees"));
                    f.setStatus(rs.getString("status"));
                    return f;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Fee> getAllFees() {
        List<Fee> list = new ArrayList<>();
        String query = "SELECT * FROM fees";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Fee f = new Fee();
                f.setFeeId(rs.getInt("fee_id"));
                f.setRollNo(rs.getString("roll_no"));
                f.setTotalFees(rs.getDouble("total_fees"));
                f.setPaidFees(rs.getDouble("paid_fees"));
                f.setDueFees(rs.getDouble("due_fees"));
                f.setStatus(rs.getString("status"));
                list.add(f);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean updateFeePayment(String rollNo, double additionalPayment) {
        Fee current = getFeeByRollNo(rollNo);
        if (current == null) return false;

        double newPaid = current.getPaidFees() + additionalPayment;
        double newDue = current.getTotalFees() - newPaid;
        if (newDue < 0) newDue = 0.0;

        String status = "Pending";
        if (newDue == 0.0) {
            status = "Paid";
        } else if (newPaid > 0.0) {
            status = "Partial";
        }

        String query = "UPDATE fees SET paid_fees = ?, due_fees = ?, status = ? WHERE roll_no = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setDouble(1, newPaid);
            pstmt.setDouble(2, newDue);
            pstmt.setString(3, status);
            pstmt.setString(4, rollNo);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
