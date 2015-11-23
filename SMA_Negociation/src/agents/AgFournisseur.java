package agents;

import communication.Action;
import communication.MailBox;
import communication.Message;
import communication.MessageContent;
import entities.Billet;
import entities.Compagnie;

import java.util.Date;
import java.util.Map;

public class AgFournisseur extends Agent {
    private int SLEEP_TIME = 500;
    private Compagnie compagnie;

    private Map<AgNegociateur, Message> negociateurs;

    public AgFournisseur(Compagnie compagnie) {
        this.compagnie = compagnie;
    }

    public void addNegociateur(AgNegociateur neg)
    {
        this.negociateurs.put(neg,null);
    }

    public String getCompanyName()
    {
        return this.compagnie.getNom();
    }

    private double calculOffre(Date now, Billet b, double lastOffreF, double lastOffreC)
    {
        if (0 == lastOffreF)
        {
            long timeMax = b.getDateMiseEnVenteMax().getTime() / 1000;
            long timeNow = now.getTime()/1000;
            return b.getPrixBase() * ((double)timeMax / (double) timeNow) * compagnie.getMaxPercentNegoc();
        }

        return 0;
    }

    @Override
    public void run() {
        for (;;)
        {
            final long timeNow = System.currentTimeMillis();

            Message m = MailBox.pollFirst(this);

            if (m != null && m.getContent() != null)
            {
                //sauvegarder message
                Message old = negociateurs.get(m.getEmetteur());
                if (old != null)
                {
                    if (old.getContent().getDestination() == m.getContent().getDestination()
                            && m.getContent().getOffreClient() <= old.getContent().getOffreClient())
                        negociateurs.put((AgNegociateur) m.getEmetteur(), m);

                } else if (m.getContent().getAction() == Action.DEMANDE)
                {
                    negociateurs.put((AgNegociateur)m.getEmetteur(), m);

                    //trouver le billet
                    Billet b = compagnie.getBillet(m.getContent().getDestination(), new Date(timeNow));

                    Message messageToSent = null;
                    if (b != null)
                    {
                        //envoyer offre
                        messageToSent = new Message(
                                this,
                                m.getEmetteur(),
                                new MessageContent(
                                        Action.OFFRE,
                                        calculOffre(new Date(timeNow),
                                                b,
                                                0,
                                                0),
                                        0,
                                        m.getContent().getDestination()
                                )
                        );
                    } else {
                        //envoyer offre
                        messageToSent = new Message(
                                this,
                                m.getEmetteur(),
                                new MessageContent(
                                        Action.REFUS,
                                        0,
                                        0,
                                        m.getContent().getDestination()
                                )
                        );
                    }

                    MailBox.send(messageToSent);
                }
            }

            try
            {
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
}
