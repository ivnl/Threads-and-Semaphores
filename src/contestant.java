import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ThreadLocalRandom;

public class contestant extends Thread {

    int id;
    int attempts = 0; // track contestants used attempts
    Queue<date> datenums = new LinkedList<date>(); // record successful numbers
    static Queue<contestant> groupLeave = new LinkedList<contestant>(); // record 'order' for leaving in groups

    public contestant(int i) {

        setName("Contestant " + (i + 1));
        id = i;

    }

    public void run() {

        int rand = ThreadLocalRandom.current().nextInt(1500, 6500); // Arriving at club random time

        try {
            sleep(rand);
        } catch (InterruptedException e) {
        }

        greetSP(this); // greet SP method with semaphore acquire

        while (attempts != main.num_round) { // continue while contestant still has 'rounds' remaining

            date d = (findAvailable(this)); // find available matching date and save to temp val

            if (d != null) { // if NOT an empty scan

                try {
                    d.sem.acquire(); // LOCK in date using dates mutex semaphore
                    msg("is approaching " + d.getName() + " to chat...");
                    d.approach(this); // approach and ask for number
                } catch (InterruptedException e) {
                }

            }

        }

        main.contDone++; // count for number of contestants finished
        groupLeave.add(this); // add finished contestant to end of group leave queue

        msg("is finished and waiting for other contestants (bragging outside)..");

        if (main.contDone == main.num_contestant) { // if all contestants finished detected, the 'last' contestant triggers leaving and smartpants

            leaveSequence();

            main.spLock.release(); // all contestants finished, trigger smartpants finish
        }

    }

    public synchronized void leaveSequence() { // leaving by groups

        int i = 1; // track group number

        while (groupLeave.peek() != null) {

            System.out.println();
            msg("(GROUP " + i + " IS NOW LEAVING)");

            for (int k = 0; k < main.num_group; k++) {

                groupLeave.peek().callTerm();

                if (!groupLeave.peek().datenums.isEmpty()) // list successful contestants with numbers from each date

                    System.out.print("[" + (System.currentTimeMillis() - main.time) + "] " + groupLeave.peek().getName() + ": received numbers from: ");
                while (!groupLeave.peek().datenums.isEmpty()) {

                    System.out.print(groupLeave.peek().datenums.peek().getName() + " ");
                    groupLeave.peek().datenums.remove();

                }

                System.out.println();

                groupLeave.remove(); // move on to next contestant

                if (groupLeave.peek() == null)
                    break;

            }

            i++;

            try {
                sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    private void callTerm() {

        msg("TERMINATING...");

    }

    public date findAvailable(contestant x) { // search for avaialable and compatible dates and return them

        for (int i = 0; i < main.num_date; i++) {
            if (main.D_array[i].visit[x.getID()] == false && main.D_array[i].sem.availablePermits() != 0) {
                return main.D_array[i];
            }

        }
        return null;

    }

    private synchronized void greetSP(contestant x) { // greet smart pants (synchronized because semaphore is being manipulated)

        try {
            main.sp.sem.acquire(); // attempt to acquire smartpants lock
        } catch (InterruptedException e) {
        }

        msg("HI SMARTPANTS!!");
        // System.out.println(getName() + ": HI SMARTPANTS!");

        main.sp.greetContestant(x);
        main.sp.sem.release(); //

    }

    public int getID() {
        return id;
    }

    public void msg(String m) {
        System.out.println("[" + (System.currentTimeMillis() - main.time) + "] " + getName() + ": " + m);
    }

}
