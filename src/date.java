import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;

public class date extends Thread {

    Semaphore sem = new Semaphore(1); // start at 0, no passes available
    boolean visit[] = new boolean[main.num_contestant]; // array to store previous attempts by contestants

    public date(int i) {

        setName("Date " + (i + 1));

    }

    public void run() {

        try {
            main.sp.dateLock.acquire(); //wait for lock to be released to terminate..
        } catch (InterruptedException e) {

        }

        msg("TERMINATING...");


    }

    public void approach(contestant c) throws InterruptedException { //talking to contestant and match decision

        int rand1 = ThreadLocalRandom.current().nextInt(1500, 5000);
        int rand2 = ThreadLocalRandom.current().nextInt(1, 3);

        setPriority(10); //raise thread priority to make decision

        //System.out.println(this.getName() + " is now talking to " + c.getName());
        sleep(rand1); // simulate 'talking phase'

        visit[c.getID()] = true; // record contestant attempt

        if (rand2 == 2) { // if roll is successful (contestant gets number) (33.3% chance of success)

            msg("has given their number to " + c.getName());
            //System.out.println(this.getName() + " has given their number to " + c.getName());
            c.datenums.add(this); // record successful match inside contestant

        } else

            msg("has rejected " + c.getName());
        //System.out.println(this.getName() + " has rejected " + c.getName());

        sem.release();

        setPriority(5); //reset thread priority to default

        increaseRound(c); // increase round total

    }

    void increaseRound(contestant c) {

        c.attempts++;
        main.roundsTotal++;

    }


    public void msg(String m) {
        System.out.println("[" + (System.currentTimeMillis() - main.time) + "] " + getName() + ": " + m);
    }

}
