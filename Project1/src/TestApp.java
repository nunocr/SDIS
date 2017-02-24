public class TestApp{
    
    public static void main(String[] args){

        if(args.length != 3 || args.length != 4){
            System.out.println("Usage: java TestApp <peer_ap> <sub_protocol> <opnd_1> <opnd_2>");
            return;
        }
        
        int peer_ap = Integer.parseInt(args[0]);
        if(peer_ap < 0 || peer_ap > 65535){
            System.out.println("Invalid peer access point: " + args[0]);
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
            System.out.println("Invlaid replication degree: " + args[3]);
            return;
        }
        

    }
}
