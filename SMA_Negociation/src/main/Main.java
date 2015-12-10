package main;

import agents.Agent;

public class Main {
    public static void main(String[] args) {
        Factory.ReturnAgents agents = Factory.scenarioDepart();
        Factory.startThread(agents);

        //Wait
        for (Agent a : agents.getAgN())
            try {
                a.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        System.out.println("fin");

        System.exit(0);
    }
}
