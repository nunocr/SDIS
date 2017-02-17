import java.net.*;
import java.util.*;
import java.io.*;
import java.lang.*;

public class Client{	
	public static void main(String[] args){
		
		try{
			
			if(args.length < 4)
				throw new IllegalArgumentException("Usage: java Client <host_name> <port_number> <oper> <opnd>*");
			
			InetAddress host = InetAddress.getByName(args[0]);
			
			int port = Integer.parseInt(args[1]);		
			if(port < 0 || port > 65535)
				throw new IllegalArgumentException("Invalid port: " + args[1]);
			
			String oper = args[2];
			if(!oper.equals("register") && !oper.equals("lookup"))
				throw new IllegalArgumentException("Invalid operation: " + args[2]);
			
			if(oper.equals("register") && args.length != 5 || oper.equals("lookup") && args.length != 4)
				throw new IllegalArgumentException("Invalid nr of arguments for operation: " + args[2]);
			
			String plate_number = args[3];
			if(!plate_number.matches("[A-Z0-9]{2}-[A-Z0-9]{2}-[A-Z0-9]{2}"))
				throw new IllegalArgumentException("Invalid plate number: " + args[3]);
			
			String message = oper.toUpperCase() + " " + plate_number;
			
			if(oper.equals("register")){
				
				String owner_name = args[4];
				if(owner_name.length() > 256)
					throw new IllegalArgumentException("Invalid owner name: " + args[4]);
				
				message += " " + owner_name;
			}
			
			DatagramSocket socket = new DatagramSocket();			
			
			DatagramPacket request = new DatagramPacket(message.getBytes(),message.length(),host,port);
			
			socket.send(request);
			
			byte[] buffer = new byte[1024];
			Arrays.fill(buffer,(byte)0);
			DatagramPacket reply = new DatagramPacket(buffer,buffer.length);
			
			socket.receive(reply);
			String reply_message = new String(buffer);
			System.out.println(reply_message.trim());
			
			socket.close();
			
		}catch(Exception e){
			System.out.println(e.getMessage());
		};
	}
}