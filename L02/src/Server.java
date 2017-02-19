import java.net.*;
import java.io.*;
import java.util.*;

public class Server{
	
	static HashMap<String,String> park;
	static InetAddress srvc_addr;	
	static InetAddress mcast_addr;
	static int srvc_port;
	static int mcast_port;	
	static DatagramSocket srvc_socket;
	static MulticastSocket mcast_socket;
	
	static class BroadcastThread extends TimerTask{
		Timer timer;
		DatagramPacket packet;		
		
		@Override
		public void run(){
			try{mcast_socket.send(packet);}
			catch(Exception e){e.printStackTrace();}
			System.out.println(new String(packet.getData()));
		}
		
		public BroadcastThread(){			
			//multicast: <mcast_addr> <mcast_port>: <srvc_addr> <srvc_port> 
			String message =
			"multicast: " +
			mcast_addr.getHostAddress() + " " +
			Integer.toString(mcast_port) + ": " +
			srvc_addr.getHostAddress() + " " +
			Integer.toString(srvc_port);			
			
			packet = new DatagramPacket(
			message.getBytes(),
			message.length(),
			mcast_addr,
			mcast_port
			);
			
			timer = new Timer();
			timer.schedule(this,0,1000);
		}
	}
	
	static class HandlerThread extends Thread{
		DatagramPacket reply;
		
		@Override
		public void run(){
			try{srvc_socket.send(reply);}
			catch(Exception e){e.printStackTrace();}			
			System.out.println(new String(reply.getData()));
		}
		
		HandlerThread(DatagramPacket request){
			String request_msg = new String(request.getData()).trim();
			String[] tokens = request_msg.split(" ");
			
			System.out.println("Request: " + request_msg);
			
			String oper = tokens[0];
			String plate_number = tokens[1];
			String owner_name = "";
			String result = "ERROR";
			String reply_msg = oper.toLowerCase() + " " + plate_number;
			
			switch(oper){
				case "REGISTER":
				owner_name = tokens[2];
				reply_msg += " " + owner_name;
				if(!park.containsKey(plate_number)){
					park.put(plate_number,owner_name);
					result = String.valueOf(park.size());
				}
				break;
				
				case "LOOKUP":
				owner_name = park.get(plate_number);
				if(owner_name != null){
					reply_msg += " " + owner_name;
					result = String.valueOf(park.size());
				}
				break;
			}
			reply_msg += " :: " + result;
			
			reply = new DatagramPacket(
			reply_msg.getBytes(),
			reply_msg.length(),
			request.getAddress(),
			request.getPort()
			);
		}
	}

	public static void main(String[] args){
		if(args.length != 3){
			System.out.println("Usage: java Server <srvc_port> <mcast_addr> <mcast_port>");
			return;
		}
		
		srvc_port = Integer.parseInt(args[0]);
		if(srvc_port < 0 || srvc_port > 65535){
			System.out.println("Invalid server port: " + args[0]);
			return;
		}		
		
		if(args[1].compareTo("224.0.0.0") < 0 || args[1].compareTo("239.255.255.255") > 0){
			System.out.println("Invalid multicast address: " + args[1]);
			return;
		}
		
		mcast_port = Integer.parseInt(args[2]);
		if(mcast_port < 0 || mcast_port > 65535){
			System.out.println("Invalid multicast port: " + args[2]);
			return;
		}		
		
		park = new HashMap<String,String>();
		
		try{
			srvc_addr = InetAddress.getLocalHost();		
			mcast_addr = InetAddress.getByName(args[1]);
			
			srvc_socket = new DatagramSocket(srvc_port);
			mcast_socket = new MulticastSocket(mcast_port);
			mcast_socket.joinGroup(mcast_addr);
			mcast_socket.setTimeToLive(1);		
			
			new BroadcastThread();
				
			while(true){
				DatagramPacket packet = new DatagramPacket(new byte[64],64);
				srvc_socket.receive(packet);
				new HandlerThread(packet).start();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}