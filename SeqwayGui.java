import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.w3c.dom.Text;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import se.lth.control.*;
import se.lth.control.plot.*;
import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SeqwayGui {
    private JPanel buttonPanel;
    private JFrame frame;
    private JPanel connectionPanel;
    private JTextField offline;
    private JTextField online;
    private ExecutorService pool ;
    private int fullSpeed=100;
    private int halfSpeed=50;
     int temp;
    private  SeqwayMonitor mon;
    LinkedList<Integer> list;
    private PlotterPanel plotter;
   

    public SeqwayGui( SeqwayMonitor mon) {
        this.mon = mon;
        list = new LinkedList<Integer>();
        pool = Executors.newFixedThreadPool(2);
        // Initialize the GUI components
        buttonPanel = new JPanel();

        JButton forwardFull = new JButton("Full speed forward");
        JButton forwardHalf = new JButton("Half speed forward");
        JButton backwardHalf = new JButton("Half speed backwards");
        JButton backwardFull = new JButton("Full speed backwards");
        JButton stopButton = new JButton("Stop");
       
        buttonPanel.add(backwardFull);
        buttonPanel.add(backwardHalf);
        buttonPanel.add(stopButton);
        buttonPanel.add(forwardHalf);
        buttonPanel.add(forwardFull);
       

        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        connectionPanel=new JPanel();
        offline = new JTextField("not connected");
        offline.setFont(new java.awt.Font("Arial", Font.ITALIC | Font.BOLD, 12));
        offline.setForeground(Color.BLACK);
        offline.setBackground(Color.RED);

        online = new JTextField("Connected");
        online.setFont(new java.awt.Font("Arial", Font.ITALIC | Font.BOLD, 12));
        online.setForeground(Color.BLACK);
        online.setBackground(Color.GREEN);

        connectionPanel.add(offline);

        plotter= new PlotterPanel(1,1);
        plotter.setYAxis(180.0, -90.0, 2, 2);
        plotter.setUpdateFreq(10);

        

        frame = new JFrame("Seqway GUI");
        frame.setLayout(new BorderLayout()); // Set the layout manager for the frame
        frame.add(connectionPanel, BorderLayout.NORTH);
        frame.add(plotter, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        
        frame.setVisible(true);

        // Add action listeners to the buttons
        forwardFull.addActionListener(e -> {
            //System.out.println("check1");
            task(1);
            
              //  mon.addToOutput(fullSpeed);
                //mon.changeYref(fullSpeed);

            // Code for full speed forward action
        });
        forwardHalf.addActionListener(e -> {
            task(2);
          
            //SeqwayMonitor.send(halfSpeed);
            // Code for half speed forward action
        });
        stopButton.addActionListener(e -> {
            task(3);
           /*  mon.addToOutput(-fullSpeed);
            mon.changeYref(-fullSpeed);*/
           // changeConnectionStatus(false);
            //SeqwayMonitor.send(-halfSpeed);
            // Code for full speed backward action
        });
        backwardHalf.addActionListener(e -> {
           task(4);
          /*   mon.addToOutput(-halfSpeed);
            mon.changeYref(-halfSpeed);*/
            //SeqwayMonitor.send(-maxSpeed);
            // Code for half speed backward action
        });
        backwardFull.addActionListener(e -> {
            task(5);
           /*  mon.addToOutput(-fullSpeed);
            mon.changeYref(-fullSpeed);*/
           // changeConnectionStatus(false);
            //SeqwayMonitor.send(-halfSpeed);
            // Code for full speed backward action
        });
        
    }

    /*public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SeqwayGui(listener));
    }
	public static void startGui(){
		SwingUtilities.invokeLater(() -> new SeqwayGui());
	}*/

    public void changeConnectionStatus(boolean connectionStatus){
        if(connectionStatus){
            SwingUtilities.invokeLater(() -> {
                connectionPanel.remove(offline);
                connectionPanel.add(online);
                frame.revalidate(); // Revalidate the frame to update the layout
                frame.repaint(); // Repaint the frame to reflect the changes
            });
        }
    }
    public void putPlotData(double t, int yref,double u){
        SwingUtilities.invokeLater(() -> {
        plotter.putData(t, yref,u);
        });
    }
    public void task( int nbrtoSend){
        Runnable newTask = ()->{
            
           // System.out.println("check2");
                mon.addToOutput(nbrtoSend);
               // mon.addFullspeed();
                
            
    
        };
         pool.submit(newTask);
    }
    
  
}


