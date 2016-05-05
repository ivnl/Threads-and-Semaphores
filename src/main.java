

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class main {

    public static contestant[] C_array;
    public static date[] D_array;
    static Semaphore spLock = new Semaphore(0); //start at 0, no passes available
    static smartpants sp = new smartpants();
    static int num_round;
    static int num_contestant;
    static int num_date;
    static int num_group;
    static int roundsTotal = 0;
    static int contDone = 0;

    public static long time = System.currentTimeMillis();

    public static void main(String[] args) {

        System.out.println("Please provide #contestant, #date, #round, #group as arguments or the default values of 10,6,3,3 will be used.");

        if (args.length == 0) {
            num_contestant = 10;
            num_date = 6;
            num_round = 3;
            num_group = 3;
        } else {
            num_contestant = Integer.parseInt(args[0]);
            num_date = Integer.parseInt(args[1]);
            num_round = Integer.parseInt(args[2]);
            num_group = Integer.parseInt(args[3]);

        }

        if (args.length == 4)
            System.out.println("Values entered: " + Integer.parseInt(args[0]) + " " + Integer.parseInt(args[1]) + " " + Integer.parseInt(args[2]) + " " + Integer.parseInt(args[3]));

        sp.start();

        D_array = new date[num_date];
        for (int i = 0; i < num_date; i++) {
            D_array[i] = new date(i);
            D_array[i].start();
        }

        C_array = new contestant[num_contestant];
        for (int i = 0; i < num_contestant; i++) {
            C_array[i] = new contestant(i);
            C_array[i].start();
        }

    }

}
