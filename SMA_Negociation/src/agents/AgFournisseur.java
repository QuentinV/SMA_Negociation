package agents;

import communication.Action;
import communication.MailBox;
import communication.Message;
import communication.MessageContent;
import entities.Billet;
import entities.Compagnie;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AgFournisseur extends Agent {
    private int SLEEP_TIME = 500;
    private Compagnie compagnie;

    private Map<AgNegociateur, Message> negociateurs;   //sauvegarde des messages
    private Map<AgNegociateur, Billet> negBillets;      //sauvegarde billet en cours de negociation

    private double percentDimPrix;

    public AgFournisseur(Compagnie compagnie, double percentDimPrix) {
        this.negociateurs = new HashMap<>();
        this.negBillets = new HashMap<>();
        this.compagnie = compagnie;
        this.percentDimPrix = percentDimPrix;
    }

    public String getCompanyName()
    {
        return this.compagnie.getNom();
    }

    private void doDemand(AgNegociateur neg, Message m, long timeNow)
    {
        //trouver le billet
        Billet b = compagnie.retirerBillet(m.getContent().getDestination(), new Date(timeNow));

        Message messageToSent = null;
        if (b != null)
        {//envoyer offre au client et sauvegarder le billet
            long timeMax = b.getDateMiseEnVenteMax().getTime() / 1000;
            long tn = timeNow/1000;
            double prix = Math.round(b.getPrixBase() - b.getPrixBase() * ((double)timeMax / (double) timeNow) * (100-compagnie.getMaxPercentNegoc()));

            messageToSent = new Message(
                    this,
                    m.getEmetteur(),
                    new MessageContent(
                            Action.OFFRE,
                            prix,
                            0,
                            m.getContent().getDestination()
                    )
            );

            negociateurs.put(neg, m);
            negBillets.put(neg, b);

            this.console("Envoi offer to "+m.getEmetteur()+" = "+String.valueOf(prix));
        } else
        {
            //pas le billet
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

            this.console("No billet available envoi refus to "+m.getEmetteur());
        }

        MailBox.send(messageToSent);
    }

    private void sendRefus(AgNegociateur neg, Message m, long timeNow)
    {
        Message messageToSent = new Message(
                this,
                m.getEmetteur(),
                new MessageContent(
                        Action.REFUS,
                        0,
                        0,
                        m.getContent().getDestination()
                )
        );
        MailBox.send(messageToSent);

        this.console("Envoi refus to "+m.getEmetteur());
    }

    @Override
    public void run() {
        this.console("HELLO");

        for (;;)
        {
            final long timeNow = System.currentTimeMillis();

            Message m = MailBox.pollFirst(this);

            if (m != null && m.getContent() != null)
            {
                AgNegociateur neg = (AgNegociateur)m.getEmetteur();

                if (m.getContent().getAction() == Action.REFUS)
                { //REFUS
                    compagnie.deposerBillet(negBillets.get(neg)); //remettre le billet en vente
                    negociateurs.remove(neg); //suppr message
                    negBillets.remove(neg); //suppr billet

                    this.console("Refus from client "+neg);
                } else if (m.getContent().getAction() == Action.ACCEPTATION)
                { //ACCEPTATION DU CLIENT
                    //verification acceptation
                    Message old = negociateurs.get(neg);
                    if (old.getContent().getOffreFournisseur() <= m.getContent().getOffreClient())
                    { //Accepter l'offre si supérieure à la précédente offre
                        Message messageToSent = new Message(
                                this,
                                m.getEmetteur(),
                                new MessageContent(
                                        Action.VERIF_ACCEPT,
                                        m.getContent().getOffreClient(),
                                        m.getContent().getOffreClient(),
                                        m.getContent().getDestination()
                                )
                        );
                        MailBox.send(messageToSent);

                        negociateurs.remove(neg); //suppr message
                        negBillets.remove(neg); //suppr billet

                        this.console("Acceptation from client "+neg+" pour "+m.getContent().getOffreClient());
                    } else
                    { //Ne pas accepter n'importe qu'elle offre
                        this.sendRefus(neg, m, timeNow);

                        compagnie.deposerBillet(negBillets.get(neg)); //remettre le billet en vente
                        negociateurs.remove(neg); //suppr message
                        negBillets.remove(neg); //suppr billet

                        this.console("False acceptation from "+neg+" = REFUS");
                    }
                } else
                { //DEMANDE, CONTRE OFFRE
                    Message old = negociateurs.get(neg);
                    if (old == null && m.getContent().getAction() == Action.DEMANDE)
                    { //traiter DEMANDE
                        this.doDemand(neg, m, timeNow);
                    } else if (old.getContent().getDestination() == m.getContent().getDestination()
                            && m.getContent().getOffreClient() >= old.getContent().getOffreClient())
                    { //contre offre
                         if (m.getContent().getAction() == Action.CONTRE_OFFRE) {
                            negociateurs.put(neg, m);//sauvegarder message

                            Billet b = negBillets.get(neg);

                            double seuil = compagnie.getMaxPercentNegoc() * b.getPrixBase() / 100;
                            if (m.getContent().getOffreClient() < seuil)
                            { //CONTRE OFFRE trop basse
                                this.sendRefus(neg, m, timeNow);
                            } else
                            { //Remonter prix
                                if (m.getContent().getOffreFournisseur() != m.getContent().getOffreClient())
                                {
                                    double offreBase = m.getContent().getOffreFournisseur();
                                    double coClient = m.getContent().getOffreClient();
                                    double diff = offreBase - coClient; //offre client tjs inférieure

                                    //diminuer l'offre de base de 20% selon l'écart entre les offres
                                    double co = Math.round(offreBase - diff * 20 / 100);

                                    if (m.getContent().getOffreFournisseur() == co && m.getContent().getOffreClient() >= seuil)
                                    {
                                        co = m.getContent().getOffreClient(); //s'aligner avec le négociateur
                                    }

                                    Message messageToSent = new Message(
                                            this,
                                            m.getEmetteur(),
                                            new MessageContent(
                                                    Action.CONTRE_OFFRE,
                                                    co,
                                                    m.getContent().getOffreClient(),
                                                    m.getContent().getDestination()
                                            )
                                    );
                                    MailBox.send(messageToSent);

                                    this.console("Envoi contre offre " + String.valueOf(co) + " to " + m.getEmetteur());
                                }
                            }
                        }
                    } else
                    { //ENVOI REFUS le client a descendu son offre de base
                        this.sendRefus(neg, m, timeNow);
                    }
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

    @Override
    public String toString()
    {
        return this.getCompanyName();
    }
}
