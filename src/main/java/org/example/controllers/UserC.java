package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.example.entities.User;

public class UserC {

    private User user= new User();
    @FXML
    private Label ban;

    @FXML
    private Label mail;

    @FXML
    private Label name;

    @FXML
    private Label tel;
    public void setData(User q) {
        this.user = q;
        name.setText("Name : "+q.getName()+" "+q.getPrenom());
        mail.setText("Email :"+q.getEmail());
        tel.setText("Phone : "+String.valueOf(q.getTel()));
        if(q.getIs_banned()!=0)
        {
            ban.setText("this user is banned");
        }else {
            ban.setText("This user account isn't banned");
        }

    }
}
