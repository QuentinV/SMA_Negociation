package agents;

public abstract class Agent extends Thread {
    protected static final int MAX_MESS_NEGOCIATION = 5;

    @Override
    public abstract void run();
}
