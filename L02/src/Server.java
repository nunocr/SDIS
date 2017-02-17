import java.net.*;
import java.util.*;
import java.io.*;
import java.lang.*;

public class Server{
Thread broadcast_thread;
Thread handler_thread;
	static Map<String,String> park = new HashMap<String,String>();

	public static void main(String[] args){

		try{
			if(args.length != 1)
				throw new IllegalArgumentException("Usage: java Server <srvc_port> <mcast_addr> <mcast_port>");

			int port = Integer.parseInt(args[0]);
			if(port < 0 || port > 65535)
				throw new IllegalArgumentException("Invalid port: " + args[0]);

        InetAddress mcast_addr = InetAddress.getByName(args[1]);

        int mcast_port = Integer.parseInt(args[2]);
  			if(mcast_port < 0 || mcast_port > 65535)
  				throw new IllegalArgumentException("Invalid port: " + args[2]);

			DatagramSocket socket = new DatagramSocket(port);

			while(true){
				byte[] buffer = new byte[1024];
				DatagramPacket request = new DatagramPacket(buffer,buffer.length);
				socket.receive(request);
				String request_message = new String(request.getData());
				System.out.println("Request: " + request_message.trim());

				String[] tokens = request_message.trim().split(" ");
				String oper = tokens[0];
				String plate_number = tokens[1];
				String owner_name = "";
				String result = "";

				switch(oper){
					case "REGISTER":

				owner_name = tokens[2];

					if(park.containsKey(plate_number)){
						result = "-1";
					}
					else{
						park.put(plate_number,owner_name);
						System.out.println("Hashcode: " + plate_number.hashCode());
						System.out.println("Added: <"+plate_number+","+owner_name+">");
						result = String.valueOf(park.size());
					}
					break;

					case "LOOKUP":
					System.out.println(park.toString());
					System.out.println("Hashcode: " + plate_number.hashCode());
					owner_name = park.get(plate_number);

					if(owner_name == null){
						result = "-1";
					}
					else{
						result = String.valueOf(park.size());
					}
					break;
					default:
					result = "-1";
					break;
				}

				String reply_message = result;
				if(!result.equals("-1"))
					reply_message += "\n" + plate_number + " " + owner_name;

				DatagramPacket reply = new DatagramPacket(reply_message.getBytes(),reply_message.length(),request.getAddress(),request.getPort());

				socket.send(reply);
				System.out.println("Reply:\n" + reply_message.trim()+"\n");
			}

		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}

	}
}
