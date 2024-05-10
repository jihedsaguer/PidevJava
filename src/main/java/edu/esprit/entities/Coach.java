package edu.esprit.entities;

import java.util.Objects;

public class Coach {
    private int id;
    private String name;
    private String email;
    private String img;

    public Coach() {
    }

    public Coach(int id, String name, String email, String img) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.img = img;
    }

    public Coach(String name, String email, String img) {
        this.name = name;
        this.email = email;
        this.img = img;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coach coach = (Coach) o;
        return id == coach.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Coach{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", img='" + img + '\'' +
                '}';
    }
}
