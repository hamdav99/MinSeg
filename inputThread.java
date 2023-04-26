public class inputThread extends Thread {
    private SerialCommunication3 com;
    private static  SeqwayMonitor mon ;
    double input;
    public inputThread(SerialCommunication3 com,SeqwayMonitor mon){
        this.com = com;
        this.mon = mon;
    }
    public void run(){
        while(true){
           input = com.checkinput();
           
           if (input > 0  ){
            System.out.println("got " + input);
            mon.addtoInput(input);
           }
        }
    }
}