package entities;

import java.util.Date;
import java.util.List;

public class Client {
    private List<Compagnie> companiesFav;
    private List<Compagnie> companiesNotFav;
    private double budget;
    private Destination destination;
    private Date dateAchatMax;

    private Strategie strat;

    public Client(List<Compagnie> companiesFav, List<Compagnie> companiesNotFav, double budget, Destination destination, Date dateAchatMax, Strategie strat) {
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
}
