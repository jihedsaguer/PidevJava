package org.example.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.entities.User;
import org.example.tools.DBconnexion;
import org.mindrot.jbcrypt.BCrypt;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


public class UserService implements ICrud<User> {
    Connection cnx2;

    public UserService() {
        cnx2 = DBconnexion.getInstance().getCnx();
    }

    public ResultSet SelectionnerSingle(int id) {
        ResultSet rs = null;
        try {
            String req = "SELECT * FROM `user` WHERE `id` =" + id;
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
            st.setString(3, hashPassword(p.getPassword())); // Hash the password before storing
            st.setString(4, p.getName());
            st.setString(5, p.getPrenom());
            st.setInt(6, p.getTel());
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
            st.setInt(6, p.getId());
            st.setString(1, p.getEmail());
            st.setString(2, p.getPassword());
            st.setString(3, p.getName());
            st.setString(4, p.getPrenom());
            st.setInt(5, p.getTel());
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

    public void modifierMdp(String email , String pw ) {
        String requet = "UPDATE user SET password=? WHERE email =?";
        try {
            PreparedStatement st = cnx2.prepareStatement(requet);
            System.out.println(email);
            System.out.println(pw);
            System.out.println(requet);
            st.setString(1,pw );
            st.setString(2, email);
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

    public ResultSet log(String email, String pw) {
        ResultSet rs = null;
        try {
            String req = "SELECT * FROM user WHERE email = '" + email + "' AND password = '" + pw + "'";
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


    public void exportUsersToExcel() {
        String query = "SELECT * FROM user";
        try (PreparedStatement stmt = cnx2.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            // Create a new workbook
            try (Workbook workbook = new XSSFWorkbook()) {
                // Create a new sheet
                Sheet sheet = workbook.createSheet("User Data");

                // Create a header row
                Row headerRow = sheet.createRow(0);
                String[] columns = {"ID", "Email", "Roles", "Password", "Name", "Prenom", "Tel", "Is Banned"};
                for (int i = 0; i < columns.length; i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(columns[i]);

                    // Apply cell style for header cells
                    CellStyle headerStyle = workbook.createCellStyle();
                    Font headerFont = workbook.createFont();
                    headerFont.setBold(true);
                    headerStyle.setFont(headerFont);
                    headerStyle.setAlignment(HorizontalAlignment.CENTER);
                    headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
                    headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    cell.setCellStyle(headerStyle); // Apply style to the cell
                }

                // Populate data rows
                int rowNum = 1;
                int totalUsers = 0;
                int bannedUsers = 0;
                int notBannedUsers = 0;
                while (rs.next()) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(rs.getInt("id"));
                    row.createCell(1).setCellValue(rs.getString("email"));
                    row.createCell(2).setCellValue(rs.getString("roles"));
                    row.createCell(3).setCellValue(rs.getString("password"));
                    row.createCell(4).setCellValue(rs.getString("name"));
                    row.createCell(5).setCellValue(rs.getString("prenom"));
                    row.createCell(6).setCellValue(rs.getInt("tel"));
                    row.createCell(7).setCellValue(rs.getInt("is_banned"));

                    // Increment the total number of users
                    totalUsers++;

                    // Check if the user is banned
                    if (rs.getInt("is_banned") == 1) {
                        bannedUsers++;
                    } else {
                        notBannedUsers++;
                    }

                    // Apply cell style for data cells
                    CellStyle dataStyle = workbook.createCellStyle();
                    dataStyle.setAlignment(HorizontalAlignment.CENTER);

                    // Check if the user is banned and set the name cell color to red
                    if (rs.getInt("is_banned") == 1) {
                        dataStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
                        dataStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    } else {
                        if (rs.getInt("is_banned") == 0) {
                            dataStyle.setFillForegroundColor(IndexedColors.LIGHT_TURQUOISE1.getIndex());
                            dataStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                        }
                    }

                    // Apply the data style to all cells in the row
                    for (Cell cell : row) {
                        cell.setCellStyle(dataStyle);
                    }
                }

                // Display the total number of users in a separate row
                Row totalUsersRow = sheet.createRow(rowNum++);
                totalUsersRow.createCell(0).setCellValue("Total Users");
                totalUsersRow.createCell(1).setCellValue(totalUsers);

                // Display the number of banned users in a separate row
                Row bannedUsersRow = sheet.createRow(rowNum++);
                bannedUsersRow.createCell(0).setCellValue("Banned Users");
                bannedUsersRow.createCell(1).setCellValue(bannedUsers);

                // Display the number of not banned users in a separate row
                Row notBannedUsersRow = sheet.createRow(rowNum++);
                notBannedUsersRow.createCell(0).setCellValue("Not Banned Users");
                notBannedUsersRow.createCell(1).setCellValue(notBannedUsers);

                // Apply some additional styles
                // Set column width
                sheet.setColumnWidth(0, 2000); // ID column
                sheet.setColumnWidth(1, 5000); // Email column
                // Add borders to cells
                for (int i = 0; i <= rowNum; i++) {
                    Row row = sheet.getRow(i);
                    if (row != null) {
                        for (int j = 0; j < columns.length; j++) {
                            Cell cell = row.getCell(j);
                            if (cell != null) {
                                CellStyle cellStyle = cell.getCellStyle();
                                cellStyle.setBorderBottom(BorderStyle.THIN);
                                cellStyle.setBorderTop(BorderStyle.THIN);
                                cellStyle.setBorderLeft(BorderStyle.THIN);
                                cellStyle.setBorderRight(BorderStyle.THIN);
                            }
                        }
                    }
                }

                // Write the workbook to a file
                try (FileOutputStream fileOut = new FileOutputStream("user_data.xlsx")) {
                    workbook.write(fileOut);
                    System.out.println("User data exported to user_data.xlsx successfully.");
                } catch (IOException e) {
                    e.printStackTrace();
                    // Handle the exception
                }
            } catch (IOException e) {
                e.printStackTrace();
                // Handle the exception
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception
        }
    }

}

