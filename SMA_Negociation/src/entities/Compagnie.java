package entities;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Compagnie {
    private String nom;
    private Map<Billet, Integer> nbBillets;

    private double maxPercentNegoc;

    public Compagnie(String nom, double maxPercentNegoc)
    {
        this.maxPercentNegoc = maxPercentNegoc;
        this.nom = nom;

        this.nbBillets = new HashMap<>();
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

    public Billet retirerBillet(Destination destination, Date now)
    {
        for (Map.Entry<Billet, Integer> e : nbBillets.entrySet())
            if (e.getValue() > 0 && e.getKey().getDestination() == destination
                    && e.getKey().getDateSouhaite().getTime() <= now.getTime()
                    && e.getKey().getDateMiseEnVenteMax().getTime() > now.getTime())
            {
                nbBillets.put(e.getKey(), e.getValue()-1); //on enleve un billet
                return e.getKey();
            }

        return null;
    }

    public void deposerBillet(Billet b)
    {
        Integer nb = nbBillets.get(b);
        if (nb == null)
            nb = 0;
        nbBillets.put(b, ++nb);
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
