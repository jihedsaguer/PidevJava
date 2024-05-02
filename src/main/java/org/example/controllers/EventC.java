package org.example.controllers;

import com.gluonhq.maps.MapLayer;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.example.entities.Event;
import javafx.fxml.FXML;
import org.example.entities.Participation;
import org.example.service.EventService;
import org.example.service.ParticipationService;
import com.gluonhq.maps.MapPoint;
import com.gluonhq.maps.MapView;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EventC {

    @FXML
    private Label id;

    @FXML
    private Label iduu;

    @FXML
    private DatePicker tfdate;

    @FXML
    private TextArea tflieu;

    @FXML
    private TextArea tfnom;

    @FXML
    private Label date;

    @FXML
    private TextArea desc;

    @FXML
    private Label lieu;

    @FXML
    private Label nom;

    private Event event= new Event();

    @FXML
    private Button todelete;

    @FXML
    private Button toupdate;

    @FXML
    private Button toparticiper;

    @FXML
    private Button toupdate1;

    @FXML
    private VBox box;

    @FXML
    private ImageView qrcode;

    EventService es = new EventService();

    ParticipationService ps = new ParticipationService();

    private MapPoint eiffelPoint = new MapPoint( 48.8583701,  2.2944813);

    @FXML
    void update(ActionEvent event) {
        // Get the current date
        LocalDate currentDate = LocalDate.now();
        if (tfnom.getText().isEmpty() || tflieu.getText().isEmpty() ||desc.getText().isEmpty() ||tfdate.getValue().isBefore(currentDate)||tfdate.getValue()==null) {
            // Afficher un message d'alerte
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Champs manquants");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs !");
            alert.showAndWait();
            return;
        }
        String description = desc.getText();
        String nom = tfnom.getText();
        String lieu = tflieu.getText();
        String date = String.valueOf(tfdate.getValue());
        Event p = new Event(Integer.parseInt(id.getText()),nom,description,"default",lieu,date);
        es.modifierEntite(p);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Valider");
        alert.setHeaderText(null);
        alert.setContentText("Evenement modifier avec success !");
        alert.showAndWait();

        tfnom.setVisible(false);
        tflieu.setVisible(false);
        tfdate.setVisible(false);
        desc.setEditable(false);
        toupdate.setVisible(true);
        toupdate1.setVisible(false);

    }

    @FXML
    void todelete(ActionEvent event) {
        try {
            int eventId = Integer.parseInt(id.getText());

            // Create a confirmation dialog
            Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationDialog.setTitle("Confirmation");
            confirmationDialog.setHeaderText(null);
            confirmationDialog.setContentText("etes vous sure de vouloir supprimer cet evenment?");

            // Add "OK" and "Cancel" buttons to the dialog
            confirmationDialog.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

            // Show the confirmation dialog and wait for the user's response
            ButtonType userResponse = confirmationDialog.showAndWait().orElse(ButtonType.CANCEL);

            // If the user clicked "OK" in the confirmation dialog, proceed with the deletion
            if (userResponse == ButtonType.OK) {
                // Create a new User instance with the provided ID
                Event eventToDelete = new Event(eventId,"","","","","");

                // Call the method to delete the user entity
                es.supprimerEntite(eventToDelete);
                /*
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/Dashboard.fxml"));
                Dashboard itemController = fxmlLoader.getController();
                ActionEvent e = new ActionEvent();
                itemController.refresh(e);

                 */
            }
        } catch (NumberFormatException e) {
            // Handle the case where the ID entered by the user is not a valid integer
            // Display an error message or handle it as appropriate for your application
            e.printStackTrace(); // Or log the error
        }
    }

    @FXML
    void toupdate(ActionEvent event) {
        desc.setEditable(true);
        tfnom.setVisible(true);
        tflieu.setVisible(true);
        tfdate.setVisible(true);
        toupdate1.setVisible(true);
        toupdate.setVisible(false);

    }

    @FXML
    void participer(ActionEvent event) {
        if(toparticiper.getText()=="Supprimer")
        {
            System.out.println(id.getText());
            Participation p = new Participation(Integer.valueOf(id.getText()),1,Integer.valueOf(iduu.getText()));
            ps.supprimerEntite(p);
        }else {
            Participation p = new Participation(Integer.valueOf(id.getText()),Integer.valueOf(iduu.getText()));
            ps.ajouterEntite(p);
        }
    }

    public void setData(Event q) {
        this.event = q;
        id.setText(String.valueOf(q.getId()));
        nom.setText(q.getNom());
        desc.setText(q.getDescription());
        date.setText("Date :"+q.getDate());
        lieu.setText("Lieu : "+q.getLieu());
        tflieu.setText(q.getDate());
        tfnom.setText(q.getNom());
        desc.setEditable(false);
        tfnom.setVisible(false);
        tflieu.setVisible(false);
        tfdate.setVisible(false);
        toupdate.setVisible(true);
        toupdate1.setVisible(false);
        toparticiper.setVisible(false);
        eiffelPoint.update(q.getLan(), q.getLon());
        MapView mapView = createMapView();
        box.getChildren().add(mapView);
        VBox.setVgrow(mapView, Priority.ALWAYS);
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            String Information = "Nom : " +q.getNom()+ "\n" + "description: " + q.getDescription() + "\n" + "date : " + q.getDate() + "\n" + "lieu : " + q.getLieu();
            int width = 300;
            int height = 300;

            BufferedImage bufferedImage = null;
            BitMatrix byteMatrix = qrCodeWriter.encode(Information, BarcodeFormat.QR_CODE, width, height);
            bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            bufferedImage.createGraphics();

            Graphics2D graphics = (Graphics2D) bufferedImage.getGraphics();
            graphics.setBackground(java.awt.Color.WHITE);
            graphics.fillRect(0, 0, width, height);
            graphics.setColor(java.awt.Color.BLACK);

            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (byteMatrix.get(i, j)) {
                        graphics.fillRect(i, j, 1, 1);
                    }
                }
            }
            System.out.println("Success...");
            qrcode.setImage(SwingFXUtils.toFXImage(bufferedImage, null));
            //  ImageView qrc = new ImageView();
            // TODO
        } catch (WriterException ex) {
            Logger.getLogger(EventC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    public void setDataF(Event q,String idu) {
        this.event = q;
        id.setText(String.valueOf(q.getId()));
        nom.setText(q.getNom());
        desc.setText(q.getDescription());
        date.setText("Date :"+q.getDate());
        lieu.setText("Lieu : "+q.getLieu());
        tflieu.setText(q.getDate());
        tfnom.setText(q.getNom());
        desc.setEditable(false);
        tfnom.setVisible(false);
        tflieu.setVisible(false);
        tfdate.setVisible(false);
        toupdate.setVisible(false);
        toupdate1.setVisible(false);
        todelete.setVisible(false);
        iduu.setText(idu);
        eiffelPoint.update(q.getLan(), q.getLon());
        MapView mapView = createMapView();
        box.getChildren().add(mapView);
        VBox.setVgrow(mapView, Priority.ALWAYS);
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            String Information = "Nom : " +q.getNom()+ "\n" + "description: " + q.getDescription() + "\n" + "date : " + q.getDate() + "\n" + "lieu : " + q.getLieu();
            int width = 300;
            int height = 300;

            BufferedImage bufferedImage = null;
            BitMatrix byteMatrix = qrCodeWriter.encode(Information, BarcodeFormat.QR_CODE, width, height);
            bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            bufferedImage.createGraphics();

            Graphics2D graphics = (Graphics2D) bufferedImage.getGraphics();
            graphics.setBackground(java.awt.Color.WHITE);
            graphics.fillRect(0, 0, width, height);
            graphics.setColor(java.awt.Color.BLACK);

            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (byteMatrix.get(i, j)) {
                        graphics.fillRect(i, j, 1, 1);
                    }
                }
            }
            System.out.println("Success...");
            qrcode.setImage(SwingFXUtils.toFXImage(bufferedImage, null));
            //  ImageView qrc = new ImageView();
            // TODO
        } catch (WriterException ex) {
            Logger.getLogger(EventC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setDataU(Event q,String idu) {
        this.event = q;
        id.setText(String.valueOf(q.getId()));
        nom.setText(q.getNom());
        desc.setText(q.getDescription());
        date.setText("Date :"+q.getDate());
        lieu.setText("Lieu : "+q.getLieu());
        tflieu.setText(q.getDate());
        tfnom.setText(q.getNom());
        desc.setEditable(false);
        tfnom.setVisible(false);
        tflieu.setVisible(false);
        tfdate.setVisible(false);
        toupdate.setVisible(false);
        toupdate1.setVisible(false);
        todelete.setVisible(false);
        toparticiper.setText("Supprimer");
        iduu.setText(idu);
        this.event.setLan(q.getLan());
        this.event.setLon(q.getLon());
        eiffelPoint.update(q.getLan(), q.getLon());
        MapView mapView = createMapView();
        box.getChildren().add(mapView);
        VBox.setVgrow(mapView, Priority.ALWAYS);
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            String Information = "Nom : " +q.getNom()+ "\n" + "description: " + q.getDescription() + "\n" + "date : " + q.getDate() + "\n" + "lieu : " + q.getLieu();
            int width = 300;
            int height = 300;

            BufferedImage bufferedImage = null;
            BitMatrix byteMatrix = qrCodeWriter.encode(Information, BarcodeFormat.QR_CODE, width, height);
            bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            bufferedImage.createGraphics();

            Graphics2D graphics = (Graphics2D) bufferedImage.getGraphics();
            graphics.setBackground(java.awt.Color.WHITE);
            graphics.fillRect(0, 0, width, height);
            graphics.setColor(java.awt.Color.BLACK);

            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (byteMatrix.get(i, j)) {
                        graphics.fillRect(i, j, 1, 1);
                    }
                }
            }
            System.out.println("Success...");
            qrcode.setImage(SwingFXUtils.toFXImage(bufferedImage, null));
            //  ImageView qrc = new ImageView();
            // TODO
        } catch (WriterException ex) {
            Logger.getLogger(EventC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private MapView createMapView() {
        MapView mapView = new MapView();
        mapView.setPrefSize(  500,  400);
        mapView.addLayer(new CustomMapLayer());
        mapView.setZoom (5);
        mapView.flyTo(  0, eiffelPoint,  0.1);
        return mapView;
    }

    private class CustomMapLayer extends MapLayer {
        private final Node marker;


        public CustomMapLayer() {
            marker = new Circle(5, Color.RED);
            getChildren().add(marker);
        }

        @Override
        protected void layoutLayer() {
            Point2D point = getMapPoint (eiffelPoint.getLatitude(), eiffelPoint.getLongitude());
            marker.setTranslateX(point.getX());
            marker.setTranslateY(point.getY());
        }
    }


}
