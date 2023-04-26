public class plotterThread extends Thread {
    
    private long startTime;
    private SeqwayMonitor mon;

    public plotterThread (SeqwayMonitor mon){
        startTime = System.currentTimeMillis();
        this.mon = mon;
    } 
    public void run(){
        while(true){
            try {
                mon.plottData(startTime);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}