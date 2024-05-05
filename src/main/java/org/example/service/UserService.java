package org.example.service;

import org.example.entities.User;
import org.example.tools.DBconnexion;

import java.sql.*;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;


public class UserService implements ICrud<User>{
    Connection cnx2;
    public UserService() {
        cnx2 = DBconnexion.getInstance().getCnx();
    }

    public ResultSet SelectionnerSingle(int id) {
        ResultSet rs = null;
        try {
            String req = "SELECT * FROM `user` WHERE `id` ="+id;
            PreparedStatement st = cnx2.prepareStatement(req);
            rs = st.executeQuery(req);
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        return rs;
    }
    // Method to retrieve users within a specific range

    @Override
    public void ajouterEntite(User p) {
        String req1 = "INSERT INTO `user`( `email`, `roles`, `password`, `name`, `prenom`, `tel`, `is_banned`) VALUES (?,?,?,?,?,?,?)";
        try {
            PreparedStatement st = cnx2.prepareStatement(req1);
            st.setString(1, p.getEmail());
            st.setString(2, p.getRoles());
            st.setString(3,p.getPassword());
            st.setString(4, p.getName());
            st.setString(5, p.getPrenom());
            st.setInt(6,p.getTel());
            st.setInt(7, p.getIs_banned());
            st.executeUpdate();
            System.out.println("user ajouté");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }
    private String hashPassword(String plainPassword) {
        // Generate a salt and hash the password with it using bcrypt
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    @Override
    public List<User> afficherEntite() {
        return null;
    }

    @Override
    public void modifierEntite(User p) {
        String requet = "UPDATE user SET email=?, password=?,name=?,prenom=?,tel=? WHERE id =?";
        try {
            PreparedStatement st = cnx2.prepareStatement(requet);
            st.setInt(6,p.getId());
            st.setString(1, p.getEmail());
            st.setString(2, p.getPassword());
            st.setString(3, p.getName());
            st.setString(4, p.getPrenom());
            st.setInt(5,p.getTel());
            int rowsAffected = st.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Modification réussie");
            } else {
                System.out.println("Aucune modification effectuée. Vérifiez l'ID.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    @Override
    public void supprimerEntite(User p) {
        String requet = "DELETE FROM user WHERE id =?";
        try {

            PreparedStatement pst = cnx2.prepareStatement(requet);
            pst.setInt(1, p.getId());  // Assuming getQuizId() returns the Quiz ID
            int rowsAffected = pst.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Suppression réussie");
            } else {
                System.out.println("Aucune suppression effectuée. Vérifiez l'ID.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }
    }
    public ResultSet searchUsers(String searchText) {
        String query = "SELECT * FROM user WHERE name LIKE ? OR email LIKE ?";
        try {
            PreparedStatement stmt = cnx2.prepareStatement(query);
            stmt.setString(1, "%" + searchText + "%");
            stmt.setString(2, "%" + searchText + "%");
            return stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public ResultSet Getall() {
        ResultSet rs = null;
        try {
            String req = "SELECT * FROM `user`";
            PreparedStatement st = cnx2.prepareStatement(req);
            rs = st.executeQuery(req);
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        return rs;
    }

    public ResultSet log(String email , String pw ) {
        ResultSet rs = null;
        try {
            String req = "SELECT * FROM user WHERE email = '"+email+"' AND password = '"+pw+"'";
            PreparedStatement st = cnx2.prepareStatement(req);
            rs = st.executeQuery(req);
            return rs;
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        return rs;
    }

    public void ban(int id) {
        String requet = "UPDATE `user` SET `is_banned`=1 WHERE `id` =?";
        try {
            PreparedStatement st = cnx2.prepareStatement(requet);
            st.setInt(1, id);
            int rowsAffected = st.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Banned ");
            } else {
                System.out.println("Aucune modification effectuée. Vérifiez l'ID.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


// pagination function ;;
    public ResultSet getUsersInRange(int startIndex, int endIndex) {
        String query = "SELECT * FROM user LIMIT ? OFFSET ?";
        try {
            PreparedStatement stmt = cnx2.prepareStatement(query);
            stmt.setInt(1, endIndex - startIndex);
            stmt.setInt(2, startIndex);
            return stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }




}

