package agents;

import communication.Action;
import communication.MailBox;
import communication.Message;
import communication.MessageContent;
import entities.Client;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Map.Entry;

public class AgNegociateur extends Agent {
    private int SLEEP_TIME = 1000;
    private Client c;
    private Map<AgFournisseur, Message> fournisseurs;

    private double percentAugmPrix;
    private boolean startNegociation;

    public AgNegociateur(Client c, double percentAugmPrix) {
        this.c = c;
        this.startNegociation = false;
        this.percentAugmPrix = percentAugmPrix;
    }

    public void addFournisseur(AgFournisseur ag)
    {
        fournisseurs.put(ag, null);
    }

    private boolean sendMess(Agent destination, Action a, double offreFournisseur, double monOffre)
    {
        Message messageToSent = new Message(
                this,
                destination,
                new MessageContent(
                        a,
                        offreFournisseur,
                        monOffre,
                        c.getDestination()
                )
        );

        return MailBox.send(messageToSent);
    }

    private boolean checkEnd()
    {
        return c.getDateAchatMax().getTime()-1000 <= System.currentTimeMillis();
    }

    private Entry<AgFournisseur, Double> chooseBestOffer()
    {
        Entry<AgFournisseur, Double> best = null;
        for (Entry<AgFournisseur, Message> f : fournisseurs.entrySet())
        {
            Double prix = f.getValue().getContent().getOffreFournisseur();
            if (prix <= c.getBudget() && (best == null || (best != null && best.getValue() > prix)))
                best = new AbstractMap.SimpleEntry<>(f.getKey(), prix);
        }

        return best;
    }

    private void acceptOffer(AgFournisseur f)
    {
        Message m = fournisseurs.get(f);//message
        double offre = m.getContent().getOffreFournisseur();
        fournisseurs.remove(f);

        this.sendMess(f, Action.ACCEPTATION, offre, offre); //envoi acceptation

        for (Entry<AgFournisseur, Message> fo : fournisseurs.entrySet())
            this.sendMess(
                    fo.getKey(),
                    Action.REFUS,
                    fo.getValue().getContent().getOffreFournisseur(),
                    0
            );
    }

    @Override
    public void run() {
        for (;;)
        {
            //ARRET
            if (checkEnd())
            {
                Entry<AgFournisseur, Double> e = chooseBestOffer();
                if (e != null)
                    this.acceptOffer(e.getKey());
                else
                    System.out.println("Date achat passé - Aucun fournisseur");

                break;
            }

            long waitingTime = SLEEP_TIME;

            Message m = MailBox.pollFirst(this);

            if (m != null && m.getContent() != null)
            {
                AgFournisseur f = (AgFournisseur) m.getEmetteur();

                //REFUS
                if (m.getContent().getAction() == Action.REFUS)
                {
                    fournisseurs.remove(f);
                    System.out.println(c+" >> refus fournisseur "+f);
                } else
                { //OFFRE ou CONTRE-OFFRE
                    Message old = fournisseurs.get(m.getEmetteur());
                    if (old == null || (old != null && (old.getContent().getDestination() == m.getContent().getDestination()
                            && m.getContent().getOffreFournisseur() <= old.getContent().getOffreFournisseur())))
                    { //old == null => OFFRE
                        double prixSeuil = c.getBudget() * c.getMaxPercentNegoc() / 100; // seuil acceptation du prix
                        if (m.getContent().getOffreFournisseur() <= prixSeuil)
                        { //on accepte l'offre du fournisseur
                            this.acceptOffer(f);
                            break; //fin negociation
                        } else
                        { //on envoi un contre offre
                            fournisseurs.put(f, m); //sauvegarder message

                            double prix = 0;
                            switch (m.getContent().getAction())
                            {
                                case OFFRE:
                                    prix = prixSeuil;
                                    break;
                                case CONTRE_OFFRE:
                                    double diff = m.getContent().getOffreFournisseur() - m.getContent().getOffreClient();

                                    //augmentation de l'offre de base
                                    prix = m.getContent().getOffreClient() + this.percentAugmPrix * diff / 100;
                                    break;
                            }

                            this.sendMess(f, Action.CONTRE_OFFRE, m.getContent().getOffreFournisseur(), prix);
                        }
                    }
                }
            } else {
                if (!this.startNegociation)
                {
                    //si on est pas en negociation - Envoyer demande à tous les fournisseurs
                    boolean check = false;
                    for (Entry<AgFournisseur, Message> agF : fournisseurs.entrySet())
                    {
                        if (c.checkFavComp(agF.getKey().getCompanyName()))
                        {
                            this.sendMess(agF.getKey(), Action.DEMANDE, 0, 0);
                            check = true;
                        }
                    }

                    this.startNegociation = check;
                }
            }

            try {
                Thread.sleep(waitingTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(c+" >> END negociation");
    }
}
