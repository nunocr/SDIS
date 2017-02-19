import java.net.*;
import java.io.*;
import java.util.*;

public class Client{	
	static InetAddress srvc_addr;	
	static InetAddress mcast_addr;
	static int srvc_port;
	static int mcast_port;	
	static DatagramSocket srvc_socket;
	static MulticastSocket mcast_socket;	
	
	public static void main(String[] args){
		
		if(args.length != 4 && args.length != 5){
			System.out.println("Usage: java Client <mcast_addr> <mcast_port> <oper> <opnd>*");
			return;
		}
			
		if(args[0].compareTo("224.0.0.0") < 0 || args[0].compareTo("239.255.255.255") > 0){
			System.out.println("Invalid multicast address: " + args[0]);
			return;
		}
		
		mcast_port = Integer.parseInt(args[1]);
		if(mcast_port < 0 || mcast_port > 65535){
			System.out.println("Invalid multicast port: " + args[1]);
			return;
		}
		
		String oper = args[2];
		if(!oper.equals("register") && !oper.equals("lookup")){
			System.out.println("Invalid operation: " + args[2]);
			return;
		}
		
		String plate_number = args[3];
		if(!plate_number.matches("[A-Z0-9]{2}-[A-Z0-9]{2}-[A-Z0-9]{2}")){
			System.out.println("Invalid plate number: " + args[3]);
			return;
		}
		
		String owner_name = "";
		if(oper.equals("register")) owner_name = args[4];		

		try{
			mcast_addr = InetAddress.getByName(args[0]);
			mcast_socket = new MulticastSocket(mcast_port);
			mcast_socket.joinGroup(mcast_addr);
			
			DatagramPacket broadcast = new DatagramPacket(new byte[64],64);

			mcast_socket.receive(broadcast);
			
			String broadcast_msg = new String(broadcast.getData()).trim();
			
			System.out.println(broadcast_msg);
			
			String[] tokens = broadcast_msg.split(" ");
			
			//multicast: <mcast_addr> <mcast_port>: <srvc_addr> <srvc_port> 			
			srvc_addr = InetAddress.getByName(tokens[3]);
			srvc_port = Integer.parseInt(tokens[4]);
			srvc_socket = new DatagramSocket();
			
			String request_msg = oper.toUpperCase() + " " + plate_number;
			if(oper.equals("register")) request_msg += " " + owner_name;
			
			DatagramPacket request = new DatagramPacket(
			request_msg.getBytes(),
			request_msg.length(),
			srvc_addr,
			srvc_port
			);
			
			srvc_socket.send(request);
			System.out.println("Request: " + request_msg);
			
			DatagramPacket reply = new DatagramPacket(new byte[64],64);
			
			srvc_socket.receive(reply);
			
			System.out.println("Reply: " + new String(reply.getData()).trim());
			
			srvc_socket.close();
			mcast_socket.leaveGroup(mcast_addr);
			mcast_socket.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
}