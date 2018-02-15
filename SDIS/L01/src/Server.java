import java.io.IOException;
import java.util.regex.Pattern;
import java.net.*;

public class Server {
	
	public static void main (String[] args) throws IOException {
		if(args.length != 1){
			System.out.println("Invalid number of arguments");
			return;
		}
		
		String port = args[0];
		boolean validPort = Pattern.matches("[0-9]*", port);
		if(validPort){
			System.out.println("Invalid port provided. Must be a number");
			return;
		}
	
		/*
		DatagramSocket socket = new DatagramSocket();
		byte[] buf = new byte[256];
		InetAddress address = InetAddress.getByName(host);
		DatagramPacket packet = new DatagramPacket(buf, buf.length, address, Integer.parseInt(port));
		socket.send(packet);
		*/
		
	}
}
