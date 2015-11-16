package main;

import java.util.Date;
import java.util.List;

public class Client {
    private List<String> companiesFav;
    private List<String> companiesNotFav;
    private double budget;
    private String destination;
    private Date dateAchatMax;

    private Strategie strat;

    public Client(List<String> companiesFav, List<String> companiesNotFav, double budget, String destination, Date dateAchatMax, Strategie strat) {
        this.companiesFav = companiesFav;
        this.companiesNotFav = companiesNotFav;
        this.budget = budget;
        this.destination = destination;
        this.dateAchatMax = dateAchatMax;
        this.strat = strat;
    }

    public Strategie getStrat() {
        return strat;
    }

    public void setStrat(Strategie strat) {
        this.strat = strat;
    }

    public List<String> getCompaniesFav() {
        return companiesFav;
    }

    public void setCompaniesFav(List<String> companiesFav) {
        this.companiesFav = companiesFav;
    }

    public List<String> getCompaniesNotFav() {
        return companiesNotFav;
    }

    public void setCompaniesNotFav(List<String> companiesNotFav) {
        this.companiesNotFav = companiesNotFav;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Date getDateAchatMax() {
        return dateAchatMax;
    }

    public void setDateAchatMax(Date dateAchatMax) {
        this.dateAchatMax = dateAchatMax;
    }
}
