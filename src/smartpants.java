import java.util.concurrent.Semaphore;

public class smartpants extends Thread {

    static Semaphore sem = new Semaphore(1); //
    Semaphore dateLock = new Semaphore(main.num_date); //

    static boolean isEnd;

    public smartpants() {
        setName("SMARTPANTS");
    }

    public void run() {

        try {
            main.spLock.acquire(); // waiting until game ends, needs to have someone wake up by calling sem.release
        } catch (InterruptedException e) {
        }

        System.out.println("");
        msg("TERMINATING...\n");
        dateLock.release(main.num_date); // release dates

    }

    public void greetContestant(contestant x) {

        msg(" Welcome to the show " + x.getName() + " go get some numbers!");
        //System.out.println(getName() + ": Welcome to the show " + x.getName() + " go get some numbers!");

    }

    public void msg(String m) {
        System.out.println("[" + (System.currentTimeMillis() - main.time) + "] " + getName() + ": " + m);
    }

}
