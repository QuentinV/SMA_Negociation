package communication;

import entities.Destination;

public class MessageContent {
    private Action action;

    private double offreFournisseur;
    private double offreClient;

    private Destination destination;

    public MessageContent(Action action, double offreFournisseur, double offreClient, Destination destination) {
        this.action = action;
        this.offreFournisseur = offreFournisseur;
        this.offreClient = offreClient;
        this.destination = destination;
    }

    public Action getAction() {
        return action;
    }

    public double getOffreFournisseur() {
        return offreFournisseur;
    }

    public double getOffreClient() {
        return offreClient;
    }

    public Destination getDestination() {
        return destination;
    }
}
