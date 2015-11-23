package main;

import agents.AgFournisseur;
import agents.AgNegociateur;
import agents.Agent;
import entities.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        //COMPAGNIES
        Billet b1 = new Billet(Destination.Lyon,
                                new Date(System.currentTimeMillis()),
                                new Date(System.currentTimeMillis()+50000),
                                800
                );
        Billet b2 = new Billet(Destination.Lyon,
                new Date(System.currentTimeMillis()),
                new Date(System.currentTimeMillis()+50000),
                700
        );
        Billet b3 = new Billet(Destination.Marseille,
                new Date(System.currentTimeMillis()+5000),
                new Date(System.currentTimeMillis()+50000),
                800
        );
        Billet b4 = new Billet(Destination.Marseille,
                new Date(System.currentTimeMillis()+5000),
                new Date(System.currentTimeMillis()+40000),
                800
        );
        Compagnie co1 = new Compagnie("C1", 20);
        co1.addBillet(b1, 3);
        co1.addBillet(b3, 2);

        Compagnie co2 = new Compagnie("C2", 20);
        co2.addBillet(b2, 3);
        co2.addBillet(b4, 2);

        AgFournisseur agF1 = new AgFournisseur(co1);
        AgFournisseur agF2 = new AgFournisseur(co2);

        //CLIENTS
        Date dateAchatMax = new Date(System.currentTimeMillis()+30000);
        Client cl1 = new Client(new ArrayList<>(), new ArrayList<>(), 500, Destination.Lyon, dateAchatMax, Strategie.AGGRESIVE);
        Client cl2 = new Client(new ArrayList<>(), new ArrayList<>(), 500, Destination.Marseille, dateAchatMax, Strategie.PASSIVE);
        AgNegociateur agN1 = new AgNegociateur(cl1);
        AgNegociateur agN2 = new AgNegociateur(cl2);
        agN1.addFournisseur(agF1);
        agN2.addFournisseur(agF2);

        //AGENTS
        List<Agent> agents = new ArrayList<>();
        agents.add(agF1);
        agents.add(agF2);
        agents.add(agN1);
        agents.add(agN2);
        agF1.addNegociateur(agN1);
        agF2.addNegociateur(agN2);

        for (Agent a : agents)
            a.run();

        for (Agent a : agents)
            try {
                a.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    }
}
