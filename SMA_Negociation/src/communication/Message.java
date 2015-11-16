package communication;

import agents.Agent;

public class Message {
    private Agent emetteur;
    private Agent destination;

    private MessageContent content;

    public Message(Agent emetteur, Agent destination, MessageContent content) {
        this.emetteur = emetteur;
        this.destination = destination;
        this.content = content;
    }

    public Agent getEmetteur() {
        return emetteur;
    }

    public Agent getDestination() {
        return destination;
    }

    public MessageContent getContent() {
        return content;
    }
}
