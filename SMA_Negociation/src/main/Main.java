package main;

import agents.Agent;

public class Main {
    public static void main(String[] args) {
        Factory.ReturnAgents agents = Factory.scenarioDepart();
        //Factory.ReturnAgents agents = Factory.scenarioRefus();

        //Démarrer les threads
        Factory.startThread(agents);

        //Attendre la fin des négociateurs
        for (Agent a : agents.getAgN())
            try {
                a.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        System.out.println("fin");

        //Terminer tous les threads restant des fournisseurs
        System.exit(0);
    }
}
