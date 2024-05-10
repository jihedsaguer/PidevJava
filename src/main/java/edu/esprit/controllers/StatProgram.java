package edu.esprit.controllers;

import edu.esprit.utils.DataSource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class StatProgram
{
    @FXML
    private PieChart piechart;
    private ObservableList<PieChart.Data> data = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        loadStatistics();
    }

    private void loadStatistics() {
        try {
            String query = "SELECT COUNT(*) as count, coach.name as coach_name FROM programme JOIN coach ON programme.coach_id = coach.id GROUP BY coach_id;";
            Statement st = DataSource.getInstance().getCnx().createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                data.add(new PieChart.Data(rs.getString("coach_name") + " (" + rs.getInt("count") + ")", rs.getInt("count")));
            }
            piechart.setData(data);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

}