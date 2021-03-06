import java.io.IOException;
import java.net.*;
import java.util.regex.Pattern;

public class Client {
	public static void main (String[] args) throws IOException{
	
		if(!args[2].equals("REGISTER") && !args[2].equals("LOOKUP")){
			System.out.println("Invalid command; Can either be REGISTER or LOOKUP");
			return;
		}
		
		if(args[2].equals("REGISTER") && args.length != 5){
			System.out.println("Invalid nr of arguments for REGISTER command");
			return;
		}
		
		if(args[2].equals("LOOKUP") && args.length != 4){
			System.out.println("Invalid nr of arguments for LOOKUP command");
			return;
		}
		
		if(args.length != 4 && args.length != 5){
			System.out.print("Invalid number of arguments; Can either be 2 or 3");
			return;
		}
		
		String plate = args[3];		
		boolean validPlate = Pattern.matches("^[A-Z0-9]{2}-[A-Z0-9]{2}-[A-Z0-9]{2}", plate);
		if(!validPlate){
			System.out.println("Invalid plate");
			return;
		}
		
		String command = args[2];
		String message = command + ' ' + plate;
		
		if(command.equals("REGISTER")){
			String owner = args[4];
			message = message + ' ' + owner; 
		}
		
		System.out.println(message);
		
		String host = args[0];
		String port = args[1];
		
		System.out.println("Host: " + host);
		System.out.println("Port: " + port);
		
		//send request
		
		DatagramSocket socket = new DatagramSocket();
		byte[] buf = message.getBytes();
		InetAddress address = InetAddress.getByName(host);
		DatagramPacket packet = new DatagramPacket(buf, buf.length, address, Integer.parseInt(port));
		socket.send(packet);
		
		//wait for reply
		
		DatagramSocket reply_socket = new DatagramSocket(Integer.parseInt(port));
		byte[] reply_buf = new byte[256];
		DatagramPacket reply = new DatagramPacket(reply_buf, reply_buf.length);
		socket.receive(reply);
		
		String reply_message = new String(reply.getData());
		reply_message = reply_message.trim();
		System.out.println("Server reply: " + reply_message);
	}
}