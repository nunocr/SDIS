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

!a peer must never stroe chunks of it's own file!



*/

class Chunk{
String fileId;
int chunkNo;
byte data[64000];
}
