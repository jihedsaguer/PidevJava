package edu.esprit.services;

import edu.esprit.entities.Coach;
import edu.esprit.utils.DataSource;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class ServiceCoach implements IService<Coach>{
    Connection cnx = DataSource.getInstance().getCnx();

    @Override
    public void ajouter(Coach coach) throws SQLException {
        String req = "INSERT INTO coach(name, email, img) VALUES (?, ?, ?)";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setString(1, coach.getName());
            ps.setString(2, coach.getEmail());
            ps.setString(3, coach.getImg());
            ps.executeUpdate();
            System.out.println("Coach added !");
        }
    }

    @Override
    public void modifier(Coach coach) {
        String req = "UPDATE coach SET name=?, email=?, img=? WHERE id=?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setString(1, coach.getName());
            ps.setString(2, coach.getEmail());
            ps.setString(3, coach.getImg());
            ps.setInt(4, coach.getId());
            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Coach updated successfully");
            } else {
                System.out.println("Failed to update coach");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    @Override
    public void supprimer(int id) {
        String req = "DELETE FROM coach WHERE id=?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("Coach deleted !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Set<Coach> getAll() {
        Set<Coach> coaches = new HashSet<>();
        String req = "SELECT * FROM coach";
        try (Statement st = cnx.createStatement(); ResultSet res = st.executeQuery(req)) {
            while (res.next()) {
                int id = res.getInt("id");
                String name = res.getString("name");
                String email = res.getString("email");
                String img = res.getString("img");
                Coach coach = new Coach(id, name, email, img);
                coaches.add(coach);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return coaches;
    }

    @Override
    public Coach getOneByID(int id) {
        String req = "SELECT * FROM coach WHERE id=?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setInt(1, id);
            try (ResultSet res = ps.executeQuery()) {
                if (res.next()) {
                    String name = res.getString("name");
                    String email = res.getString("email");
                    String img = res.getString("img");
                    return new Coach(id, name, email, img);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Set<String> getAllCoachNames() {
        Set<String> coachNames = new HashSet<>();
        String req = "SELECT name FROM coach";
        try (Statement st = cnx.createStatement(); ResultSet res = st.executeQuery(req)) {
            while (res.next()) {
                String name = res.getString("name");
                coachNames.add(name);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return coachNames;
    }
    public int getCoachIdByName(String coachName) {
        String req = "SELECT id FROM coach WHERE name=?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setString(1, coachName);
            try (ResultSet res = ps.executeQuery()) {
                if (res.next()) {
                    return res.getInt("id");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }
    public SortedSet<Coach> triParCritere(String critere) {
        switch (critere) {
            case "Name":
                return getAll().stream()
                        .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Coach::getName))));
            case "Email":
                return getAll().stream()
                        .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Coach::getEmail))));
            default:
                return new TreeSet<>();
        }
    }


}

