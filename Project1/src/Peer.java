/*
each peer is identified by an integer
each file has an hash identifier (sha256)
files are split in chunks
chunks are identified by the pair (fieldID,cunkNo)
size of each chunk is 64e3 bytes
peers dont need to store all the chunks from a file (they cna be spread accross the network)
chunks of the same file must have the same degree of replication (be duplicated across the same nr of peers)

peers have control of how much space the give for backups
if the peer admin decides to free up allocated backup space, chunks on that space need to be sent to ohter peers to maintain their desired replication degree
when a file is deleted, all chunks across the network must also be deleted

subprotocols to implement:
1. chunk backup
2. chunk restore
3. file deletion
4. space reclaiming

all subprotocols use a multicast control channel (MC)
all peers must subscribe the MC
there's a backup multicat data channel MDB
and a file chunk restore multicast data channel MDR

control message: MC
<MessageType> <Version> <SenderId> <FileId> <ChunkNo> <ReplicationDeg> <CRLF><CRLF><Body>

Backup subprotocol: MDB
innitiator peer sends
PUTCHUNK <Version> <SenderId> <FileId> <ChunkNo> <ReplicationDeg> <CRLF><CRLF><Body>

a peer that stores upon receiving the PUTCHUNK replies after a radnom delay between 0 and 400ms
STORED <Version> <SenderId> <FileId> <ChunkNo> <CRLF><CRLF>

the initiator peer will count the received STORED msgs for 1 seconds
"If the number of confirmation messages it received up to the end of that interval is lower than the desired replication degree, it retransmits the backup message on the MDB channel, and doubles the time interval for receiving confirmation messages. This procedure is repeated up to a maximum number of five times, i.e. the initiator will send at most 5 PUTCHUNK messages per chunk."

"A peer should also count the number of confirmation messages for each of the chunks it has stored and keep that count in non-volatile memory. This information is used if the peer runs out of disk space: in that event, the peer will try to free some space by evicting chunks whose actual replication degree is higher than the desired replication degree."

!a peer must never store chunks of it's own file!

*/

public class Peer{
	
	HashMap<String,HashMap<>>
	
	final static String CRLF = "\r\n";
	
	static int id;
	
	static MulticastSocket mc_socket;
	static int mc_port;
	
	static MulticastSocket mdb_socket;
	static int mdb_port;
	
	static MulticastSocket mdr_socket;
	static int mdr_port;	
	
	static DatagramSocket peer_socket;
	static int peer_port;	
	
	abstract static class ListenerThread extends Thread{		
		
		DatagramPacket packet;
		Socket socket;
		
		abstract public void handle(DatagramPacket packet);		
		
		ListenerThread(){
			packet = new DatagramPacket(new byte[64],64);
		}
		
		@Override
		public void run(){
			while(true){
				socket.receive(packet);
				handle(packet);
			}
		}
		
		static class MCListenerThread extends ListenerThread{
			public void handle(DatagramPacket packet){
				new MCHandlerThread(packet);
			}
			MCListenerThread(){
				super();
				socket = mc_socket;
			}
		}	
		static class MDBListenerThread extends ListenerThread{
			public void handle(DatagramPacket packet){
				new MDBHandlerThread(packet);
			}
			MDBListenerThread(){
				super();
				socket = mdb_socket;
			}
		}
		static class MDRListenerThread extends ListenerThread{
			public void handle(DatagramPacket packet){
				new MDRHandlerThread(packet);
			}
			MDRListenerThread(){
				super();
				socket = mdr_socket;
			}
		}
		static class PeerListenerThread extends ListenerThread{
			public void handle(DatagramPacket packet){
				new PeerHandlerThread(packet);
			}
			PeerListenerThread(){
				super();
				socket = peer_socket;
			}
		}		
	}
	
	abstract static class HandlerThread extends Thread{
		DatagramPacket packet;
		abstract public void run();
		HandlerThread(DatagramPacket packet){this.packet = packet;}
		
		static class MCHandlerThread extends HandlerThread{
			MCHandlerThread(DatagramPacket packet){}
			public void run(){
				
			}
		}
		static class MDBHandlerThread extends HandlerThread{
			MDBHandlerThread(DatagramPacket packet){}
			public void run(){
				String msg = packet.getData().trim();
				System.out.println("MDB: "+msg);
				
				String[] tkns = String(msg).split(" ");	
				String sender_id = tkns[2];
				String file_id = tkns[3];
				int chunk_no = Integer.parseInt(tkns[4]);
				int degree = Integer.parseInt(tkns[5]);
				byte[] data = msg.substring(msg.lastIndexOf(CRLF+CRLF)+4).getBytes();
			}
		}
		static class MDRHandlerThread extends HandlerThread{
			MDRHandlerThread(DatagramPacket packet){}
			public void run(){
				String msg = String(packet.getData().trim());
			}
		}
		static class PeerHandlerThread extends HandlerThread{
			PeerHandlerThread(DatagramPacket packet){}
			public void run(){
				String msg = String(packet.getData().trim());
			}
		}
		
	}
	
	
	public static void main(String[] args){
		
		if(args.length != 7){
			System.our.println("Usage: java Peer <peer_id> <mc_addr> <mc_port> <mdb_addr> <mdb_port> <mdr_addr> <mdr_port>");
			return;
		}
		
		peer_port = Integer.parseInt(args[0]);
		peer_socket = new DatagramSocket(peer_port);
		// java Peer MC_Address MC_Port MDB_address MDB_port MDR_Address MDr_port Server_id
		
		while(true){
			DatagramPacket request = new DatagramPacket(new byte[64],64);
			socket.receive(request);
			
			
		}
	}
	
	class Chunk{
	String fileId;
	int chunkNo;
	byte[64000] data;
	}
}

