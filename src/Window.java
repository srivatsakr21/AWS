import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
public class Window extends JFrame implements ActionListener {
	private Container C;
	private JLabel lblHeading, lblFooter, lblServer1,lblServer2,lblServer3,lblServer4;
	private JTextField tfServer1Cpu,tfServer2Cpu,tfServer3Cpu,tfServer4Cpu;
	private JTextField tfServer1Memory,tfServer2Memory,tfServer3Memory,tfServer4Memory;
	private JTextField tfServer1Jobs,tfServer2Jobs,tfServer3Jobs,tfServer4Jobs;
	private JButton btStart, btStop, btExit;
	private Admin admin;
	private Window This;
	public Window() {
		this.This=this;
		this.C=this.getContentPane();
		this.setLayout(null);
		this.setTitle("Load Balancer Admin");
		
		this.lblHeading=new JLabel("Server            CPU             Memory             Jobs");
		this.lblHeading.setBounds(10,10,500,50);
		this.C.add(this.lblHeading);
		
		this.lblFooter=new JLabel("attempting connection to load balancer");
		this.lblFooter.setBounds(10,250,500,50);
		this.C.add(this.lblFooter);
		
/*****************************************************/
		this.lblServer1=new JLabel("Server1");
		this.lblServer1.setBounds(10,50,80,50);
		this.C.add(this.lblServer1);
		
		this.tfServer1Cpu=new JTextField();
		this.tfServer1Cpu.setBounds(85,63,50,23);
		this.tfServer1Cpu.setEditable(false);
		this.C.add(this.tfServer1Cpu);
		
		this.tfServer1Memory=new JTextField();
		this.tfServer1Memory.setBounds(145,63,50,23);
		this.tfServer1Memory.setEditable(false);
		this.C.add(this.tfServer1Memory);

		this.tfServer1Jobs=new JTextField();
		this.tfServer1Jobs.setBounds(230,63,50,23);
		this.tfServer1Jobs.setEditable(false);
		this.C.add(this.tfServer1Jobs);
/*****************************************************/		
		this.lblServer2=new JLabel("Server2");
		this.lblServer2.setBounds(10,100,80,50);
		this.C.add(this.lblServer2);
		
		this.tfServer2Cpu=new JTextField();
		this.tfServer2Cpu.setBounds(85,113,50,23);
		this.tfServer2Cpu.setEditable(false);
		this.C.add(this.tfServer2Cpu);
		
		this.tfServer2Memory=new JTextField();
		this.tfServer2Memory.setBounds(145,113,50,23);
		this.tfServer2Memory.setEditable(false);
		this.C.add(this.tfServer2Memory);

		this.tfServer2Jobs=new JTextField();
		this.tfServer2Jobs.setBounds(230,113,50,23);
		this.tfServer2Jobs.setEditable(false);
		this.C.add(this.tfServer2Jobs);
/*****************************************************/
		this.lblServer3=new JLabel("Server3");
		this.lblServer3.setBounds(10,150,80,50);
		this.C.add(this.lblServer3);
		
		this.tfServer3Cpu=new JTextField();
		this.tfServer3Cpu.setBounds(85,163,50,23);
		this.tfServer3Cpu.setEditable(false);
		this.C.add(this.tfServer3Cpu);
		
		this.tfServer3Memory=new JTextField();
		this.tfServer3Memory.setBounds(145,163,50,23);
		this.tfServer3Memory.setEditable(false);
		this.C.add(this.tfServer3Memory);

		this.tfServer3Jobs=new JTextField();
		this.tfServer3Jobs.setBounds(230,163,50,23);
		this.tfServer3Jobs.setEditable(false);
		this.C.add(this.tfServer3Jobs);
/*****************************************************/
		this.lblServer4=new JLabel("Server4");
		this.lblServer4.setBounds(10,200,80,50);
		this.C.add(this.lblServer4);
		
		this.tfServer4Cpu=new JTextField();
		this.tfServer4Cpu.setBounds(85,213,50,23);
		this.tfServer4Cpu.setEditable(false);
		this.C.add(this.tfServer4Cpu);
		
		this.tfServer4Memory=new JTextField();
		this.tfServer4Memory.setBounds(145,213,50,23);
		this.tfServer4Memory.setEditable(false);
		this.C.add(this.tfServer4Memory);

		this.tfServer4Jobs=new JTextField();
		this.tfServer4Jobs.setBounds(230,213,50,23);
		this.tfServer4Jobs.setEditable(false);
		this.C.add(this.tfServer4Jobs);
/*****************************************************/
		this.btStart=new JButton("Start");
		this.btStart.setBounds(320,50,100,30);
		this.btStart.addActionListener(this);
		this.C.add(this.btStart);
		
		this.btStop=new JButton("Stop");
		this.btStop.setBounds(320,120,100,30);
		this.btStop.addActionListener(this);
		this.C.add(this.btStop);
		
		this.btExit=new JButton("Exit");
		this.btExit.setBounds(320,190,100,30);
		this.btExit.addActionListener(this);
		this.C.add(this.btExit);
		
		this.resetOutput();
		
		this.setSize(500,350);
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		Dimension dimension=Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(
				dimension.width/2-this.getSize().width/2,
				dimension.height/2-this.getSize().height/2
		);
		this.setVisible(true);
		this.admin=new Admin();
		this.admin.start();
	}
	@Override
	public void actionPerformed(ActionEvent event) {
		JButton source=(JButton)event.getSource();
		if(source==btStart){
			this.admin.command=Command.Ready;
		} else if(source==btStop) {
			this.admin.command=Command.Wait;
		} else if(source==btExit) {
			admin.terminate();
			System.exit(0);
		}
	}
	public enum Command {
		Wait,
		Ready
	}
	private class Admin extends Thread {
		private final int PORT=9000;
		private final String HOST="52.10.236.205";
		private boolean run;
		private boolean connected;
		private SocketIO socketIO;
		public Command command;
		public Admin() {
			this.run=true;
			this.connected=this.connect();
			this.command=Command.Ready;
		}
		private boolean connect() {
			lblFooter.setText("attempting connection to load balancer");
			Socket loadBalancer=new Socket();
			boolean status=false;
			try {
				loadBalancer.connect(new InetSocketAddress(HOST,PORT));
				this.socketIO=new SocketIO(loadBalancer);
				Packet packet=new Packet("Admin");
				Return r=socketIO.send(packet);
				if(r.type.name().equals("False")) {					
				} else {
					lblFooter.setText("connected to load balancer");
					status=true;
				}
			} catch(IOException ioex){}
			finally {
				return status;
			}
		}
		public void terminate() {
			this.run=false;
			if(this.socketIO!=null)
				this.socketIO.terminate();
		}
		@Override
		public void run() {
			while(this.run) {
				if(!this.connected) {
					this.connected=this.connect();
					continue;
				}
				ArrayList<Packet>packets=this.socketIO.receivePackets();
				resetOutput();
				if(packets==null) {
					this.connected=false;
					continue;
				}
				lblFooter.setText("");
				if(packets.size()==0) continue;
				for(Packet p:packets) {
					if(p.id.equalsIgnoreCase("s1")||p.id.equalsIgnoreCase("server1")) {
						tfServer1Cpu.setText(p.cpu+"");
						tfServer1Memory.setText(p.memory+"");
						tfServer1Jobs.setText(p.jobs+"");
						if(p.load) {
							tfServer1Cpu.setBackground(Color.GREEN);
							tfServer1Memory.setBackground(Color.GREEN);
							tfServer1Jobs.setBackground(Color.GREEN);
							lblFooter.setText("Load on Server 1");
						} else {
							tfServer1Cpu.setBackground(Color.RED);
							tfServer1Memory.setBackground(Color.RED);
							tfServer1Jobs.setBackground(Color.RED);
						}
					} else if(p.id.equalsIgnoreCase("s2")||p.id.equalsIgnoreCase("server2")) {
						tfServer2Cpu.setText(p.cpu+"");
						tfServer2Memory.setText(p.memory+"");
						tfServer2Jobs.setText(p.jobs+"");
						if(p.load) {
							tfServer2Cpu.setBackground(Color.GREEN);
							tfServer2Memory.setBackground(Color.GREEN);
							tfServer2Jobs.setBackground(Color.GREEN);
							lblFooter.setText("Load on Server 2");
						} else {
							tfServer2Cpu.setBackground(Color.RED);
							tfServer2Memory.setBackground(Color.RED);
							tfServer2Jobs.setBackground(Color.RED);
						}
					} else if(p.id.equalsIgnoreCase("s3")||p.id.equalsIgnoreCase("server3")) {
						tfServer3Cpu.setText(p.cpu+"");
						tfServer3Memory.setText(p.memory+"");
						tfServer3Jobs.setText(p.jobs+"");
						if(p.load) {
							tfServer3Cpu.setBackground(Color.GREEN);
							tfServer3Memory.setBackground(Color.GREEN);
							tfServer3Jobs.setBackground(Color.GREEN);
							lblFooter.setText("Load on Server 3");
						} else {
							tfServer3Cpu.setBackground(Color.RED);
							tfServer3Memory.setBackground(Color.RED);
							tfServer3Jobs.setBackground(Color.RED);
						}
					} else if(p.id.equalsIgnoreCase("s4")||p.id.equalsIgnoreCase("server4")) {
						tfServer4Cpu.setText(p.cpu+"");
						tfServer4Memory.setText(p.memory+"");
						tfServer4Jobs.setText(p.jobs+"");
						if(p.load) {
							tfServer4Cpu.setBackground(Color.GREEN);
							tfServer4Memory.setBackground(Color.GREEN);
							tfServer4Jobs.setBackground(Color.GREEN);
							lblFooter.setText("Load on Server 4");
						} else {
							tfServer4Cpu.setBackground(Color.RED);
							tfServer4Memory.setBackground(Color.RED);
							tfServer4Jobs.setBackground(Color.RED);
						}
					}
				}
/****************************************************/
				Packet packet=new Packet("Admin");
				packet.setType("Wait");
				if(this.command==Command.Ready)
					packet.setType("Ready");
				this.socketIO.send(packet);
/****************************************************/
			}
		}
	}

	private void resetOutput() {
		Color color=Color.WHITE;
		this.tfServer1Cpu.setBackground(color);
		this.tfServer1Cpu.setText("");
		this.tfServer1Memory.setBackground(color);
		this.tfServer1Memory.setText("");
		this.tfServer1Jobs.setBackground(color);
		this.tfServer1Jobs.setText("");

		this.tfServer2Cpu.setBackground(color);
		this.tfServer2Cpu.setText("");
		this.tfServer2Memory.setBackground(color);
		this.tfServer2Memory.setText("");
		this.tfServer2Jobs.setBackground(color);
		this.tfServer2Jobs.setText("");
		
		this.tfServer3Cpu.setBackground(color);
		this.tfServer3Cpu.setText("");
		this.tfServer3Memory.setBackground(color);
		this.tfServer3Memory.setText("");
		this.tfServer3Jobs.setBackground(color);
		this.tfServer3Jobs.setText("");
		
		this.tfServer4Cpu.setBackground(color);
		this.tfServer4Cpu.setText("");
		this.tfServer4Memory.setBackground(color);
		this.tfServer4Memory.setText("");
		this.tfServer4Jobs.setBackground(color);
		this.tfServer4Jobs.setText("");
	}
}