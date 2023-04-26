import java.awt.image.DataBufferFloat;
import java.io.IOException;
import java.util.LinkedList;


public class SeqwayMonitor{

    private SeqwayGui gui;
    private SerialCommunication3 com;
    private LinkedList <Double> inputList;
    private LinkedList <Integer> outputList;
    private int yref;
    
    

    public SeqwayMonitor(){
      
        outputList= new LinkedList<Integer>();
        inputList= new LinkedList<Double>();
        yref=0;
        
    }
    public void setGui(SeqwayGui gui ){
        this.gui = gui;
    }
    public void setCom (SerialCommunication3 com){
        this.com = com;
    }
    public synchronized void addToOutput(int nbrToSend){
        //System.out.println("check");
        outputList.add(nbrToSend);
        notifyAll();
    }

    public synchronized void send () throws InterruptedException{
        try {
            while(outputList.isEmpty()){
                wait();
            }
            int i = outputList.poll();
            System.out.println("sending " + i);
            com.send(i);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("could not send");
        }
   
    }
    
    public synchronized void plottData(long startTime) throws InterruptedException{
        while(inputList.isEmpty()){// ha en tråd som bara är att kolla om det finns någor i denna lista och om säger till edt att plotta den;
            wait();
        }
        long t = System.currentTimeMillis()-startTime;
        gui.putPlotData(t,yref,inputList.poll());
         
    }
    public synchronized void addtoInput(double newData){
        inputList.add(newData);
        notifyAll();
    }
    public synchronized void changeYref(int yref){
        this.yref=yref;
    }
    public void addFullspeed() {
        outputList.add(1);
       // System.out.println("check3");
    }
}
