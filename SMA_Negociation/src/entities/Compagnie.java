package entities;

import java.util.Date;
import java.util.Map;

public class Compagnie {
    private String nom;
    private Map<Billet, Integer> nbBillets;

    private double maxPercentNegoc;

    public Compagnie(String nom, double maxPercentNegoc)
    {
        this.maxPercentNegoc = maxPercentNegoc;
        this.nom = nom;
    }

    public double getMaxPercentNegoc()
    {
        return maxPercentNegoc;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Billet getBillet(Destination destination, Date now)
    {
        for (Map.Entry<Billet, Integer> e : nbBillets.entrySet())
            if (e.getValue() > 0 && e.getKey().getDestination() == destination
                    && e.getKey().getDateSouhaite().getTime() >= now.getTime()
                    && e.getKey().getDateMiseEnVenteMax().getTime() < now.getTime())
                return e.getKey();

        return null;
    }

    public void addBillet(Billet b, int nb)
    {
        nbBillets.put(b, nb);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Compagnie compagnie = (Compagnie) o;

        return nom.equals(compagnie.nom);

    }

    @Override
    public int hashCode() {
        return nom.hashCode();
    }
}
