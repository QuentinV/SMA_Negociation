package main;

import agents.AgFournisseur;
import agents.AgNegociateur;
import agents.Agent;
import entities.Billet;
import entities.Client;
import entities.Compagnie;
import entities.Destination;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Factory {
    public static class ReturnAgents {
        private final List<AgFournisseur> agF;
        private final List<AgNegociateur> agN;

        public ReturnAgents(List<AgFournisseur> agF, List<AgNegociateur> agN)
        {
            this.agF = agF;
            this.agN = agN;
        }

        public List<AgNegociateur> getAgN()
        {
            return agN;
        }

        public List<AgFournisseur> getAgF()
        {
            return agF;
        }

        public List<Agent> getAgents()
        {
            List<Agent> listAll = new ArrayList<>();
            listAll.addAll(agN);
            listAll.addAll(agF);

            return listAll;
        }
    };

    public static Billet createBillet(Destination dest, int offsetSecSouhaite, int offsetSecMiseEnVenteMax, int prix)
    {
        return new Billet(
                Destination.Lyon,
                new Date(System.currentTimeMillis() + offsetSecSouhaite * 1000),
                new Date(System.currentTimeMillis() + offsetSecMiseEnVenteMax * 1000),
                prix
        );
    }

    public static Compagnie createCompagnie(String name, int percentSeuilMax, Billet... billets)
    {
        Compagnie co = new Compagnie(name, percentSeuilMax);
        Random r = new Random();

        for (Billet b : billets)
            co.addBillet(b, r.nextInt(5) + 1);

        return co;
    }

    public static Client createClient(String name, double budget, double percentSeuilMax, Destination d,
                                      int offsetSecDateAchatMax, Compagnie... avoids)
    {
        List<Compagnie> lC = new ArrayList<>();
        for (Compagnie c : avoids)
            lC.add(c);

        return new Client(
                name,
                lC,
                budget,
                percentSeuilMax,
                d,
                new Date(System.currentTimeMillis()+offsetSecDateAchatMax*1000)
        );
    }

    public static List<AgFournisseur> createFournisseurs(double percentDimPrix, Compagnie... companies)
    {
        List<AgFournisseur> list = new ArrayList<>();
        for(Compagnie c : companies)
            list.add(new AgFournisseur(c, percentDimPrix));

        return list;
    }

    public static List<AgNegociateur> createNegociateurs(double percentAugmPrix, List<AgFournisseur> f, Client...clients)
    {
        List<AgNegociateur> l = new ArrayList<>();
        for (Client c : clients)
        {
            l.add(new AgNegociateur(c, percentAugmPrix, f));
        }
        return l;
    }

    public static boolean startThread(ReturnAgents r)
    {
        if (r == null) return false;

        //Demarrer les fournisseurs
        System.out.println("start fournisseurs");
        for (Agent a : r.getAgF())
            a.start();

        try
        {
            Thread.sleep(1000);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        System.out.println("start negociateurs");
        //Demarrer les negociateurs
        for (Agent a : r.getAgN())
            a.start();

        return true;
    }

    public static ReturnAgents scenarioRefus()
    {
        Billet b1 = Factory.createBillet(Destination.Lyon, 0, 60, 500);

        //Compagnies
        Compagnie co1 = Factory.createCompagnie("CO1", 60, b1);
        Compagnie co2 = Factory.createCompagnie("CO2", 85, b1); //seuil de refus élevé
        List<AgFournisseur> listAgF = Factory.createFournisseurs(20, co1, co2);

        //Clients
        Client c1 = Factory.createClient("CL1", 450, 88, Destination.Lyon, 30);

        List<AgNegociateur> listAgN = Factory.createNegociateurs(20, listAgF, c1);

        //AGENTS
        return new ReturnAgents(listAgF, listAgN);
    }

    public static ReturnAgents scenarioDepart()
    {
        //prix base = 500 ou 400
        //Budget client = 450
        //Seuil client = 400
        //prix refus fournisseur = 350

        //billets
        Billet b1 = Factory.createBillet(Destination.Lyon, 0, 60, 500);
        //Billet b2 = Factory.createBillet(Destination.Lyon, 0, 60, 400);

        //Compagnies
        Compagnie co1 = Factory.createCompagnie("CO1", 60, b1);
        Compagnie co2 = Factory.createCompagnie("CO2", 50, b1);
       // Compagnie co3 = Factory.createCompagnie("CO3", 60, b2);

        List<AgFournisseur> listAgF = Factory.createFournisseurs(20, co1, co2);

        //Clients
        Client c1 = Factory.createClient("CL1", 450, 88, Destination.Lyon, 30);

        List<AgNegociateur> listAgN = Factory.createNegociateurs(20, listAgF, c1);

        //AGENTS
        return new ReturnAgents(listAgF, listAgN);
    }
}
