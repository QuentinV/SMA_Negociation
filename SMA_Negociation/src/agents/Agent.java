package agents;

import java.util.Calendar;

public abstract class Agent extends Thread {
    protected static final int MAX_MESS_NEGOCIATION = 5;

    protected void console(String m)
    {
        Calendar c = Calendar.getInstance();
        String date = c.get(Calendar.HOUR) + ":"+c.get(Calendar.MINUTE) +":"+ c.get(Calendar.SECOND);

        System.out.println(this+ " ("+date+") >> "+m);
    }

    @Override
    public abstract void run();
}
