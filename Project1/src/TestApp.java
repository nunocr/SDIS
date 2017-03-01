public class TestApp{
    
    public static void main(String[] args){

        if(args.length != 3 || args.length != 4){
            System.out.println("Usage: java TestApp <peer_ap> <sub_protocol> <opnd_1> <opnd_2>");
            return;
        }
        
		InetAddress address;
		int port;
		try{
			if(args[0].contains(":")){
				String[] tokens = peer_ap.split(":");
				address = InetAddress.getByName(tokens[0]);
				port = Integer.parseInt(tokens[1]);
				
			}else{
				address = InetAddress.getLocalHost();
				port = Integer.parseInt(args[0]);
			}
		}catch(Exception e){
			System.out.println("Invalid client address: " + address.getHostName());
			return;
		}		

		if(port < 0 || port > 65535){
			System.out.println("Invalid client port: " + Integer.toString(port));
			return;
		}
        
        String sub_protocol = args[1];
        if(
        !sub_protocol.equals("BACKUP") &&
        !sub_protocol.equals("RESTORE") &&
        !sub_protocol.equals("DELETE") &&
        !sub_protocol.equals("RECLAIM")
        ){
            System.out.println("Invalid sub protocol: " + args[1]);
            return;
        }
        
        File file;
        int space;
        if(sub_protocol.equals("RECLAIM")){
            file.gc();
            space = Interger.parseInt(args[2]);
            if(space < 0){
                System.our.println("Invalid operand: "+args[2]);
                return;
            }
        }else{
            space.gc();
            file = new File(args[2]);
            if(!file.isFile()){
                System.out.println("Invalid file path: " + args[2]);
                return;
            }
        }
        
        if(!sub_protocol.equals("BACKUP") && args.length == 4){
            System.out.println("Usage: java TestApp <peer_ap> <sub_protocol> <opnd_1> <opnd_2>");
            return;
        }
        
        int degree = Integer.parseInt(args[3]);
        if(degree < 0 || degree > 9){
            System.out.println("Invalid replication degree: " + args[3]);
            return;
        }
        

    }
}
