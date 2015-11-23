package agents;

import communication.Action;
import communication.MailBox;
import communication.Message;
import communication.MessageContent;
import entities.Client;

import java.util.Map;
import java.util.Map.Entry;

public class AgNegociateur extends Agent {
    private int SLEEP_TIME = 1000;
    private Client c;
    private Map<AgFournisseur, Message> fournisseurs;

    public AgNegociateur(Client c) {
        this.c = c;
    }

    public void addFournisseur(AgFournisseur ag)
    {
        fournisseurs.put(ag, null);
    }

    @Override
    public void run() {
        for (;;)
        {
            Message m = MailBox.pollFirst(this);

            if (m != null && m.getContent() != null)
            {
                //sauvegarder message
                Message old = fournisseurs.get(m.getEmetteur());
                if (old != null)
                {
                    if (old.getContent().getDestination() == m.getContent().getDestination()
                            && m.getContent().getOffreFournisseur() <= old.getContent().getOffreFournisseur())
                    {
                        fournisseurs.put((AgFournisseur) m.getEmetteur(), m);
                    } else {
                        //ne rien faire
                    }
                } else {
                    fournisseurs.put((AgFournisseur) m.getEmetteur(),m);
                }


            } else {
                //si on est pas en negociation
                for (Entry<AgFournisseur, Message> agF : fournisseurs.entrySet())
                {
                    if (c.checkFavComp(agF.getKey().getCompanyName()))
                    {
                        Message messageToSent = new Message(
                                this, agF.getKey(),
                                new MessageContent(
                                        Action.DEMANDE,
                                        0,
                                        0,
                                        c.getDestination()
                                )
                        );

                        MailBox.send(messageToSent);
                    }
                }
            }

            try {
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
