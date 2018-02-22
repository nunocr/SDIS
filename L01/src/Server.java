import java.io.IOException;
import java.util.regex.Pattern;
import java.net.*;

public class Server {
	
	public static void main (String[] args) throws IOException {
		if(args.length != 1){
			System.out.println("Invalid number of arguments");
			return;
		}
		
		boolean validPort = Pattern.matches("[0-9]*", args[0]);
		if(!validPort){
			System.out.println("Invalid port provided. Must be a number");
			return;
		}

		int port = Integer.parseInt(args[0]);
		DatagramSocket socket = new DatagramSocket(port);
		byte[] buf = new byte[256];
		DatagramPacket request = new DatagramPacket(buf, buf.length);
		socket.receive(request);
		
		String response = new String(request.getData());
		response = response.trim();
		
		String[] tokens = response.split(" ");
		System.out.println("Request: " + response);
		System.out.println("Length: " + tokens.length);
		
		String command, plate;
		String reply_message;
		
		if(tokens.length == 3){
			command = tokens[0];
			plate = tokens[1];
			String name = tokens[2];
			reply_message = "REGISTER operation successfully executed.";
		}
		else if(tokens.length == 2){
			command = tokens[0];
			plate = tokens[1];
			reply_message = "LOOKUP operation successfully executes.";
		}
		else{
			reply_message = "ERROR";
		}
		
		byte[] reply_buf = reply_message.getBytes();
		InetAddress reply_address = InetAddress.getByName("LOCALHOST");
		DatagramPacket reply_packet = new DatagramPacket(reply_buf, reply_buf.length, reply_address, port);
		socket.send(reply_packet);
	}
}
