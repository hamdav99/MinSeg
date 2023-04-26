public class outputThread extends Thread{

    private SeqwayMonitor mon;

     public outputThread(SeqwayMonitor mon){
        this.mon = mon;
    }

    public void run(){
        while(true){
            try {
                
                mon.send();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}