package agents;

public abstract class Agent extends Thread {
    protected static final int MAX_MESS_NEGOCIATION = 5;

    protected void console(String m)
    {
        System.out.println(this+ " >> "+m);
    }

    @Override
    public abstract void run();
}
