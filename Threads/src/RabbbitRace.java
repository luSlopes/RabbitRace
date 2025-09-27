import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;


public class RabbbitRace {
    public static void main(String[] args) {
        Thread t0 = new Thread(new Race(new Rabbit("0")));
        Thread t1 = new Thread(new Race(new Rabbit("1")));
        Thread t2 = new Thread(new Race(new Rabbit("2")));
        Thread t3 = new Thread(new Race(new Rabbit("3")));
        Thread t4 = new Thread(new Race(new Rabbit("4")));

        t0.start();
        t1.start();
        t2.start();
        t3.start();
        t4.start();

        //Here I made the main thread be the last one ending, for print the final classification of the race
        try{
            t0.join();
            t1.join();
            t2.join();
            t3.join();
            t4.join();
        }catch(InterruptedException e){
            e.printStackTrace();
        }

        //Printing the classification
        ArrayList<Rabbit> classification = Race.getClassification();
        System.out.println("\nFinal Classification from first to the last: ");
        for(Rabbit r : classification){
            System.out.println("Rabbit: " + r.getNumber() + " jumps: " + r.getJumps());
        }
    }


    //Implementing the Runnable class
    public static class Race implements Runnable{

        private final Object lock = new Object();
        private final int goal = 20; //20 meters is the distance of the race
        Rabbit r;
        private static ArrayList<Rabbit> classification = new ArrayList<>();


        public Race(Rabbit r){
            this.r = r;
        }

        //Run method from the Runnable interface
        public void run(){
            while(r.getCurrentDistance() < 20) {
                r.jump();
            }
            synchronized(lock){
                classification.add(r);
            }
        }

        //Getters
        public int getGoal(){
            return goal;
        }

        public static ArrayList<Rabbit> getClassification(){
            return classification;
        }

    }

    public static class Rabbit{

        private Integer distance = 0;
        private String number;
        private Integer jumps = 0; //Counter var for how many jumps the rabbit made, useful only for the classification print

        public Rabbit(String number){
            this.number = number;
        }

        public void jump(){
            jumps++;
            Random r = new Random();
            int jump = r.nextInt(3) + 1; //The rabbits can do jumps that goes from 1 to 3 meters only
            distance += jump; //Increases the distance
            System.out.println("Rabbit " + number + ":" + distance + "meters");
            //This one above is my big problem, when the threads are running, it is quite confusing to see what's happening
            //due to they are all together in the prompt, but it works perfectly
            try {
                Thread.sleep(5000); //Wait 5 secs between the jumps
            }catch(InterruptedException e){
                e.printStackTrace();
            }

        }

        //Getters
        public String getNumber(){
            return number;
        }

        public int getJumps(){
            return jumps;
        }

        public int getCurrentDistance(){
            return distance;
        }

    }

}
