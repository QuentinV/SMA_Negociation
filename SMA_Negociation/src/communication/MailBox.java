package communication;

import agents.Agent;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;


public class MailBox {
    private static final Map<Agent, LinkedList<Message>> boites = 
            new HashMap<Agent, LinkedList<Message>>();
    
    public static synchronized boolean send(Message m)
    {
        if (m == null) return false;
        if (m.getDestination() == null) return false;

        LinkedList<Message> list = boites.get(m.getDestination());
        if (list == null)
        {
            list = new LinkedList<>();
            boites.put(m.getDestination(), list);
        }

        return list.add(m);
    }
    
    public static synchronized Message pollFirst(Agent a)
    {
        if (a == null) return null;
        
        LinkedList<Message> list = boites.get(a);
        if (list == null) return null;
        
        return list.pollFirst();
    }
}
