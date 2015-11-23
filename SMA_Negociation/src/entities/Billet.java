package entities;

import java.util.Date;

public class Billet {
    //private String depart;
    private Destination destination;

    private Date dateSouhaite;
    private Date dateMiseEnVenteMax;

    private double prixBase;

    public Billet(Destination destination) {
        this.destination = destination;
    }

    public Billet(Destination destination, Date dateSouhaite, Date dateMiseEnVenteMax, double prixBase) {
        this.destination = destination;
        this.dateSouhaite = dateSouhaite;
        this.dateMiseEnVenteMax = dateMiseEnVenteMax;
        this.prixBase = prixBase;
    }

    public Destination getDestination() {
        return destination;
    }

    public Date getDateSouhaite() {
        return dateSouhaite;
    }

    public Date getDateMiseEnVenteMax() {
        return dateMiseEnVenteMax;
    }

    public double getPrixBase() {
        return prixBase;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Billet billet = (Billet) o;

        return destination == billet.destination;

    }

    @Override
    public int hashCode() {
        return destination.hashCode();
    }
}
