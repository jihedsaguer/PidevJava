package edu.esprit.entities;

import java.util.Date;
import java.util.Objects;

public class Program {
    private int id;
    private Coach coach;
    private String type;
    private String duree;
    private Date startDate;

    public Program() {
    }

    public Program(int id, Coach coach, String type, String duree, Date startDate) {
        this.id = id;
        this.coach = coach;
        this.type = type;
        this.duree = duree;
        this.startDate = startDate;
    }

    public Program(Coach coach, String type, String duree, Date startDate) {
        this.coach = coach;
        this.type = type;
        this.duree = duree;
        this.startDate = startDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Coach getCoach() {
        return coach;
    }

    public void setCoach(Coach coach) {
        this.coach = coach;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDuree() {
        return duree;
    }

    public void setDuree(String duree) {
        this.duree = duree;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Program program = (Program) o;
        return id == program.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Program{" +
                "coach=" + coach +
                ", type='" + type + '\'' +
                ", duree='" + duree + '\'' +
                ", startDate=" + startDate +
                '}';
    }
}
