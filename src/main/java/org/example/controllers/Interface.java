package org.example.controllers;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.example.entities.User;
import org.example.service.UserService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Optional;
import java.util.Random;
import java.util.prefs.Preferences;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Interface {
    @FXML
    public TextField logE;
    @FXML
    public TextField captchaC;
    @FXML
    public TextField captchaE;
    @FXML
    public TextField captcha;
    public Button updateButton;
    @FXML
    private Pagination pagination;
    @FXML
    private ImageView qrcode;
    @FXML
    private Label id;
    @FXML
    private TextField tf_log;
    @FXML
    private Pane pn_home;
    @FXML
    private TextField tous;
    @FXML
    private Button btnclose;

    @FXML
    private Pane pn_index;

    @FXML
    private Pane pn_signin;

    @FXML
    private Pane pn_signup;

    @FXML
    private Pane pn_update;
    @FXML
    private Preferences preferences;

    @FXML
    private TextField tf_email;

    @FXML
    private TextField tf_pw;

    @FXML
    private TextField tf_email1;

    @FXML
    private TextField tf_fn;
    @FXML
    private TextField tousE;
    @FXML
    private TextField numE;
    @FXML
    private TextField tf_fn1;

    @FXML
    private TextField tf_ln;

    @FXML
    private TextField tf_ln1;

    @FXML
    private TextField tf_num;

    @FXML
    private TextField tf_num1;

    @FXML
    private TextField tf_pass;

    @FXML
    private TextField tf_pass1;
    @FXML
    private ToggleButton toggleButton; // Assuming you have a toggle button in your FXML

    User tmpp = new User();
    UserService us = new UserService();




    // If using ToggleButton, bind this method to the ToggleButton's onAction event

    @FXML
    void signup(ActionEvent event) {
        if (tf_ln.getText().isEmpty() || tf_fn.getText().isEmpty() || tf_num.getText().isEmpty() || tf_email.getText().isEmpty() || tf_pass.getText().isEmpty()) {
            tousE.setText("Remplir tous les champs, s'il vous plaît."); // Display an error message
            return;
        }

        // Validate password strength
        String password = tf_pass.getText();
        String passwordErrorMessage = validatePassword(password);
        if (passwordErrorMessage != null) {
            // Display an error message if the password does not meet the requirements
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Mot de passe invalide");
            alert.setHeaderText(null);
            alert.setContentText(passwordErrorMessage);
            alert.showAndWait();
            return;
        }


        try {
            // Attempt to parse the text as an integer
            int num = Integer.parseInt(tf_num.getText());
        } catch (NumberFormatException e) {
            // If parsing fails (NumberFormatException is thrown), display an alert
            numE.setText("Numéro invalide");
            captchaE.setText(""); // Clear the captcha error message
            return;
        }

        if (!captcha.getText().equalsIgnoreCase(captchaText)) {
            captchaE.setText("CAPTCHA incorrect !");
            tf_email.setText("");
            return; // Return if the CAPTCHA is incorrect
        }

        String emailPattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(tf_email.getText());

        if (!matcher.matches()) {
            // If the email doesn't match the pattern, display an alert
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Mail incorrect");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez choisir un e-mail valide !");
            alert.showAndWait();
            return;
        }

        String ln = tf_ln.getText();
        String fn = tf_fn.getText();
        String mail = tf_email.getText();
        String pass = tf_pass.getText();
        int num = Integer.parseInt(tf_num.getText());

        // Create a new user if all validation passes
        User u = new User(mail, "[\"ROLE_USER\"]", pass, fn, ln, num, 0);
        us.ajouterEntite(u);
        tf_fn.clear();
        tf_ln.clear();
        tf_num.clear();
        tf_email.clear();
        tf_pass.clear();
        pn_signin.toFront();
    }

    // Validate password strength
    private String validatePassword(String password) {
        // Define password requirements
        int minLength = 8;
        int minUpperCase = 1;
        int minLowerCase = 1;
        int minDigits = 1;
        int minSpecialChars = 1;

        // Check password length
        if (password.length() < minLength) {
            return "Le mot de passe doit contenir au moins " + minLength + " caractères.";
        }

        // Check for uppercase letters
        if (password.chars().filter(Character::isUpperCase).count() < minUpperCase) {
            return "Le mot de passe doit contenir au moins " + minUpperCase + " lettre(s) majuscule(s).";
        }

        // Check for lowercase letters
        if (password.chars().filter(Character::isLowerCase).count() < minLowerCase) {
            return "Le mot de passe doit contenir au moins " + minLowerCase + " lettre(s) minuscule(s).";
        }

        // Check for digits
        if (password.chars().filter(Character::isDigit).count() < minDigits) {
            return "Le mot de passe doit contenir au moins " + minDigits + " chiffre(s).";
        }


        // Password meets all requirements
        return null;
    }


    @FXML
    void delete(ActionEvent event) {
        try {
            int userId = Integer.parseInt(id.getText());

            // Create a confirmation dialog
            Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationDialog.setTitle("Confirmation");
            confirmationDialog.setHeaderText(null);
            confirmationDialog.setContentText("etes vous sure de vouloir supprimer votre compte?");

            // Add "OK" and "Cancel" buttons to the dialog
            confirmationDialog.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

            // Show the confirmation dialog and wait for the user's response
            ButtonType userResponse = confirmationDialog.showAndWait().orElse(ButtonType.CANCEL);

            // If the user clicked "OK" in the confirmation dialog, proceed with the deletion
            if (userResponse == ButtonType.OK) {
                // Create a new User instance with the provided ID
                User userToDelete = new User(userId);

                // Call the method to delete the user entity
                us.supprimerEntite(userToDelete);

                // Move to the "signin" pane
                pn_signin.toFront();
            }
        } catch (NumberFormatException e) {
            // Handle the case where the ID entered by the user is not a valid integer
            // Display an error message or handle it as appropriate for your application
            e.printStackTrace(); // Or log the error
        }
    }

    @FXML
    void update(ActionEvent event) {
        if (tf_ln1.getText().isEmpty() || tf_fn1.getText().isEmpty() || tf_num1.getText().isEmpty() || tf_email1.getText().isEmpty() || tf_pass1.getText().isEmpty() || tf_num.getText().isEmpty() ) {
            // Afficher un message d'alerte
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Champs manquants");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs !");
            alert.showAndWait();
            return;
        }

        try {
            // Attempt to parse the text as an integer
            int num = Integer.parseInt(tf_num1.getText());
        } catch (NumberFormatException e) {
            // If parsing fails (NumberFormatException is thrown), display an alert
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Num tel incorrect");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez choisir un num exact !");
            alert.showAndWait();

            // Return or perform any necessary action based on the invalid input
            return;
        }
        String emailPattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(tf_email1.getText());

        if (!matcher.matches()) {
            // If the email doesn't match the pattern, display an alert
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Mail incorrect");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez choisir un mail valide!");
            alert.showAndWait();
            return;
        }


        String ln = tf_ln1.getText();
        String fn = tf_fn1.getText();
        String mail = tf_email1.getText();
        String pass = tf_pass1.getText();
        int num = Integer.parseInt(tf_num1.getText());


        //    public User(String email, String roles, String password, String name, String prenom, int tel, int is_banned) {
        User u = new User(Integer.parseInt(id.getText()), mail, "[\"ROLE_USER\"]", pass, fn, ln, num, 0);
        us.modifierEntite(u);

    }

    @FXML
    void toSignup(ActionEvent event) {
        generateCaptcha();

        pn_signup.toFront();
    }

    @FXML
    void toUpdate(ActionEvent event) {
        pn_update.toFront();
    }

    @FXML
    void tosignin(ActionEvent event) {
        pn_signin.toFront();
    }

    @FXML
    void toSignin(ActionEvent event) {
        pn_signin.toFront();
    }


    @FXML
    public void pwd(ActionEvent event) throws SQLException, IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/chooseforget.fxml"));
        Parent signInRoot = loader.load();
        Scene signInScene = new Scene(signInRoot);

        // Create a new stage for the forget password window
        Stage forgetPwdStage = new Stage();
        forgetPwdStage.initModality(Modality.APPLICATION_MODAL); // Set modality to APPLICATION_MODAL
        forgetPwdStage.setScene(signInScene);
        forgetPwdStage.showAndWait();

    }
    @FXML
    protected String generateQRCode(String verificationCode) {
        try {
            // Create a QRCode writer
            QRCodeWriter qrCodeWriter = new QRCodeWriter();

            // Encode the verification code into a BitMatrix
            BitMatrix bitMatrix = qrCodeWriter.encode(verificationCode, BarcodeFormat.QR_CODE, 200, 200);

            // Convert the BitMatrix to a BufferedImage
            BufferedImage bufferedImage = new BufferedImage(bitMatrix.getWidth(), bitMatrix.getHeight(), BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x < bitMatrix.getWidth(); x++) {
                for (int y = 0; y < bitMatrix.getHeight(); y++) {
                    bufferedImage.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF); // Set black or white based on bit value
                }
            }
            // Convert the BufferedImage to a byte array
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", byteArrayOutputStream);

            // Convert the byte array to a Base64 encoded string
            byte[] bytes = byteArrayOutputStream.toByteArray();

            // Return the Base64 encoded string
            return Base64.getEncoder().encodeToString(bytes);
        } catch (WriterException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    @FXML
    void login(ActionEvent event) {
        String email = tf_log.getText();
        String password = tf_pw.getText();

        // First, verify email and password
        ResultSet resultSet = us.log(email, password);
        System.out.println(resultSet);
        try {
            if (resultSet.next()) {
                // User authenticated, generate verification code and QR code
                String verificationCode = generateVerificationCode();
                String qrCodeBase64 = generateQRCode(verificationCode);
                System.out.println("Verification code: " + verificationCode);

                // Show alert box with QR code image
                Alert qrCodeAlert = new Alert(Alert.AlertType.INFORMATION);
                qrCodeAlert.setTitle("QR Code Verification");
                qrCodeAlert.setHeaderText("Scan the QR code to verify your login.");

                // Add QR code image to the alert content
                ImageView qrCodeImageView = new ImageView();
                qrCodeImageView.setImage(new Image(new ByteArrayInputStream(Base64.getDecoder().decode(qrCodeBase64))));
                qrCodeImageView.setFitWidth(200); // Set width of the image
                qrCodeImageView.setFitHeight(200); // Set height of the image
                qrCodeAlert.getDialogPane().setGraphic(qrCodeImageView);

                // Add input field for verification code
                TextField verificationCodeInput = new TextField();
                verificationCodeInput.setPromptText("Enter Verification Code");
                qrCodeAlert.getDialogPane().setContent(verificationCodeInput);

                // Show the alert and wait for user response
                Optional<ButtonType> result = qrCodeAlert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    // User clicked OK, check verification code
                    String enteredCode = verificationCodeInput.getText();
                    if (enteredCode.equals(verificationCode)) {
                        // Verification code matches, proceed with login
                        System.out.println("Verification successful.");

                        // Proceed with user authentication
                        tmpp = new User(
                                resultSet.getInt("id"),
                                resultSet.getString("email"),
                                resultSet.getString("roles"),
                                resultSet.getString("password"),
                                resultSet.getString("name"),
                                resultSet.getString("prenom"),
                                resultSet.getInt("tel"),
                                resultSet.getInt("is_banned")
                        );

                        tf_fn1.setText(tmpp.getName());
                        tf_ln1.setText(tmpp.getPrenom());
                        tf_num1.setText(String.valueOf(tmpp.getTel()));
                        tf_email1.setText(tmpp.getEmail());
                        tf_pass1.setText(tmpp.getPassword());

                        if (tmpp.getIs_banned() == 1) {
                            System.out.println("User is banned.");
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("User banned");
                            alert.setHeaderText(null);
                            alert.setContentText("User banned !");
                            alert.showAndWait();
                        } else {
                            if (tmpp.getRoles().equals("[\"ROLE_ADMIN\"]")) {
                                System.out.println("Admin logged in.");
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/SideBarAdmin.fxml"));
                                Parent adminRoot = loader.load();

                                Scene adminScene = new Scene(adminRoot);
                                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                                window.setScene(adminScene);
                                window.show();
                            } else {
                                System.out.println("User logged in.");
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/sideBar.fxml"));
                                Parent adminRoot = loader.load();

                                Scene adminScene = new Scene(adminRoot);
                                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                                window.setScene(adminScene);
                                window.show();

                            }
                        }
                    } else {
                        // Verification code doesn't match, show error message
                        System.out.println("Incorrect verification code.");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Incorrect Verification Code");
                        alert.setHeaderText(null);
                        alert.setContentText("The entered verification code is incorrect. Please try again.");
                        alert.showAndWait();
                    }
                } else {
                    // User closed the alert without verifying QR code
                    System.out.println("QR Code verification canceled.");
                }
            } else {
                System.out.println("Invalid credentials.");
                logE.setText("Invalid credentials");
            }
        } catch (SQLException e) {
            System.out.println("SQL error: " + e.getMessage());
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.out.println("IO error: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }



    private String generateVerificationCode() {
        // Generate a random 6-digit verification code
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }


    @FXML
    void toHome(ActionEvent event) {
        System.out.println("helo");
        pn_home.toFront();
        pn_update.toBack();
    }


    private String captchaText;

    private void generateCaptcha() {
        // Generate a random sequence of letters for the CAPTCHA
        Random random = new Random();
        StringBuilder captcha = new StringBuilder();
        for (int i = 0; i < 6; i++) { // Generate a 6-letter CAPTCHA
            char randomChar = (char) (random.nextInt(26) + 'A'); // Generate random uppercase letters
            captcha.append(randomChar);
        }
        captchaText = captcha.toString(); // Assign the generated CAPTCHA text to captchaText
        captchaC.setText(captchaText);
        System.out.println(captchaText);
    }




    // Method to generate CAPTCHA
    @FXML
    public void initialize() {
    }

}






