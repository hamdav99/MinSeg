import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

public class SerialCommunication3 {
    static StreamConnection connection;
    private SeqwayMonitor mon;
    private static OutputStream outputStream;
    private static  InputStream inputStream ;
   
    public SerialCommunication3( SeqwayMonitor mon){
        this.mon=mon;
    }
    
    public boolean connect() throws IOException{
        try {
            // Get the Bluetooth address of the HC-06 module
            String hc06Address = "201808171304"; // Replace with the Bluetooth address of your HC-06 module

            // Create the connection URL using the HC-06 address and SPP UUID
            String url = "btspp://" + hc06Address + ":1;authenticate=false;encrypt=false";
        


            // Open the Bluetooth connection
            connection = (StreamConnection) Connector.open(url);

            // Get the input and output streams for the Bluetooth connection
            inputStream = connection.openInputStream();
            outputStream = connection.openOutputStream();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            // Close the Bluetooth connection when done
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /* public static void main(String[] args) {
        try {
            // Get the Bluetooth address of the HC-06 module
            String hc06Address = "201808171304"; // Replace with the Bluetooth address of your HC-06 module

            // Create the connection URL using the HC-06 address and SPP UUID
            String url = "btspp://" + hc06Address + ":1;authenticate=false;encrypt=false";
           


            // Open the Bluetooth connection
            connection = (StreamConnection) Connector.open(url);

            // Get the input and output streams for the Bluetooth connection
            InputStream inputStream = connection.openInputStream();
             outputStream = connection.openOutputStream();

            // Read data from the Bluetooth connection
            while (true) {
                if (inputStream.available() > 0) {
                    int data  = inputStream.read();
                    // mon.addData(data); //lägger till det i list för ska plottas i monitorn
                   System.out.println("got " + data);
                    // Process the received data as needed
                }

                // Send data to the Bluetooth connection
               /*   Scanner scanner = new Scanner(System.in);
                System.out.print("Enter a number (1, 2, or 3) to send to HC-06: ");
                int number = scanner.nextInt();*/
                 // outputStream.write(number);
               //  outputStream.flush(); // Flush the output stream to ensure data is sent immediately
               // send(number);
                // Thread.sleep(104);
                

          /*   }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close the Bluetooth connection when done
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }  */
    public void send(int nbrToSend) throws IOException{
        // nbrToSend = nbrToSend+ 48; // för aski
        outputStream.write(nbrToSend);
        outputStream.flush(); // Flush the output stream to ensure data is sent immediately
        

    }
    public double checkinput(){
        double data=0;
        try{
            if (inputStream.available() > 0) {
                 data = inputStream.read();
                 
                 data = data-48;
                 mon.addtoInput(data); //lägger till det i list för ska plottas i monitorn
              
              
                // Process the received data as needed
            } else {

            }
        } catch(Exception e){

        }
        return data;
       
    }
}
