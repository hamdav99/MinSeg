import java.io.IOException;
import java.util.Scanner;

public class SeqwayMain{
    private SerialCommunication3 com;
    private SeqwayGui gui;
    private static  SeqwayMonitor mon ;
    public static void main(String[] args) {
        SeqwayMonitor mon = new SeqwayMonitor();
        SerialCommunication3 com = new SerialCommunication3(mon);
        SeqwayGui gui = new SeqwayGui (mon);
         mon.setGui(gui);
         mon.setCom(com);
        try {
            gui.changeConnectionStatus(com.connect());

           Thread output = new outputThread(mon);
            Thread input = new inputThread(com, mon); // Skapa o start de trådar som används
            Thread plotter = new plotterThread (mon);
             input.start();
            output.start();
           // plotter.start();
            /*Thread input = new inputThread(com, mon);
            input.start();
            Scanner scanner = new Scanner(System.in);
            while(true){
               
                System.out.print("Enter a number (1, 2, or 3) to send to HC-06: ");
                int number = scanner.nextInt();
                com.send(number);
                
                 // com.checkinput();
            }*/
           


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

       

    }
}