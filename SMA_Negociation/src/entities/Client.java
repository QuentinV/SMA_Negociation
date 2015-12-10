package entities;

import java.util.Date;
import java.util.List;

public class Client {
    private List<Compagnie> companiesFav;
    private List<Compagnie> companiesNotFav;

    private double budget;
    private double maxPercentNegoc;

    private Destination destination;
    private Date dateAchatMax;

    private String nom;

    public Client(String nom,
                  List<Compagnie> companiesFav,
                  List<Compagnie> companiesNotFav,
                  double budget,
                  double maxPercentNegoc,
                  Destination destination,
                  Date dateAchatMax) {
        this.companiesFav = companiesFav;
        this.companiesNotFav = companiesNotFav;
        this.budget = budget;
        this.destination = destination;
        this.dateAchatMax = dateAchatMax;

        this.nom = nom;
        this.maxPercentNegoc = maxPercentNegoc;
    }

    public double getMaxPercentNegoc()
    {
        return maxPercentNegoc;
    }

    public void setMaxPercentNegoc(double maxPercentNegoc)
    {
        this.maxPercentNegoc = maxPercentNegoc;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    public Date getDateAchatMax() {
        return dateAchatMax;
    }

    public void setDateAchatMax(Date dateAchatMax) {
        this.dateAchatMax = dateAchatMax;
    }

    public boolean checkFavComp(String name)
    {
        if (name == null) return false;
        return companiesFav.contains(new Compagnie(name, 0));
    }

    public boolean checkNotFavComp(String name)
    {
        if (name == null) return false;
        return companiesNotFav.contains(new Compagnie(name, 0));
    }

    @Override
    public String toString()
    {
        return this.nom;
    }
}
