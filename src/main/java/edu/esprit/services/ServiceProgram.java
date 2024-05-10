package edu.esprit.services;

import edu.esprit.entities.Coach;
import edu.esprit.entities.Program;
import edu.esprit.utils.DataSource;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class ServiceProgram implements IService<Program> {
    Connection cnx = DataSource.getInstance().getCnx();

    @Override
    public void ajouter(Program program) throws SQLException {
        String req = "INSERT INTO programme(coach_id, type, duree, startdate) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setInt(1, program.getCoach().getId());
            ps.setString(2, program.getType());
            ps.setString(3, program.getDuree());
            ps.setDate(4, new java.sql.Date(program.getStartDate().getTime()));
            ps.executeUpdate();
            System.out.println("Program added !");
        }
    }

    @Override
    public void modifier(Program program) {
        String req = "UPDATE programme SET coach_id=?, type=?, duree=?, startdate=? WHERE id=?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setInt(1, program.getCoach().getId());
            ps.setString(2, program.getType());
            ps.setString(3, program.getDuree());
            ps.setDate(4, new java.sql.Date(program.getStartDate().getTime()));
            ps.setInt(5, program.getId());
            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Program updated successfully");
            } else {
                System.out.println("Failed to update program");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void supprimer(int id) {
        String req = "DELETE FROM programme WHERE id=?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("Program deleted !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Set<Program> getAll() {
        Set<Program> programs = new HashSet<>();
        String req = "SELECT * FROM programme";
        try (Statement st = cnx.createStatement(); ResultSet res = st.executeQuery(req)) {
            while (res.next()) {
                int id = res.getInt("id");
                int coachId = res.getInt("coach_id");
                String type = res.getString("type");
                String duree = res.getString("duree");
                Date startDate = res.getDate("startdate");
                // You need to fetch coach details separately based on coachId
                // Assuming you have a method to fetch coach by ID in ServiceCoach class
                Coach coach = new ServiceCoach().getOneByID(coachId);
                Program program = new Program(id, coach, type, duree, startDate);
                programs.add(program);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return programs;
    }

    @Override
    public Program getOneByID(int id) {
        String req = "SELECT * FROM programme WHERE id=?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setInt(1, id);
            try (ResultSet res = ps.executeQuery()) {
                if (res.next()) {
                    int coachId = res.getInt("coach_id");
                    String type = res.getString("type");
                    String duree = res.getString("duree");
                    Date startDate = res.getDate("startdate");
                    // You need to fetch coach details separately based on coachId
                    // Assuming you have a method to fetch coach by ID in ServiceCoach class
                    Coach coach = new ServiceCoach().getOneByID(coachId);
                    return new Program(id, coach, type, duree, startDate);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}