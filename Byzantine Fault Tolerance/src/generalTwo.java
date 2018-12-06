/**
 * Byzantine Generals War msg implementation
 */

/**
 * @author ALI
 *
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map; 


public class generalTwo 
{
	public static final int NODE_PORT_1 = 12345;
	public static final int NODE_PORT_2 = 12346;
	public static final int NODE_PORT_3 = 12347;
	public static final int NODE_PORT_4 = 12348;
	public static final String IP = "127.0.0.1";
	public static final String LISTEN = "\nListening ...";



	//Constructor
	public generalTwo(String ip_address, int NODE_PORT_1,  int NODE_PORT_2, int NODE_PORT_3, int NODE_PORT_4) 
	{
		try
		{ 	
			/**
			 * identity port for current node_1 Socket
			 * Node_1
			 **/
			ServerSocket general_two_socket = new ServerSocket(NODE_PORT_2);	
			//String generalTwoResponse = "1";
			String generalTwoResponse = "generalTwoResponse =1 | node =generalTwo";

			/**
			 *	Flags be used to check if all nodes have responded
			 **/
			int generalOneFlag = 0; 			
			int generalTwoFlag = 0; 			
			int generalThreeFlag = 0; 			
			int generalFourFlag = 0; 			
			int countVote = 0;
			String fromGeneral = "";

			//To store all responses
			Map<String,String>  allResponses = new HashMap<String,String>(); 

			// running infinite loop for getting 
			// requests
			while(true){
				//Give time to show connection
				Thread.sleep(2000);

				System.out.println(LISTEN);

				//Accepts communication from other processes
				Socket processResponse = general_two_socket.accept();	
				//Reads the encoded message
				BufferedReader in = new BufferedReader(new InputStreamReader(processResponse.getInputStream()));
				while((fromGeneral = in.readLine()) != null)
				{
					//splits the incoming data
					String[] separated =  fromGeneral.split("\\|");
					String[] values =  fromGeneral.split("\\=");

					// Extract relevant information from incoming message
					int size = separated.length;
					int sizeValues = values.length;
					String fromGeneralResp = separated[0].trim();
					String fromGeneralNum = separated[1].trim();
					System.out.println("\nReceiving Message...");
					String fromGeneralNumVal = fromGeneralNum.substring(fromGeneralNum.lastIndexOf("=") + 1);
					String fromGeneralRespVal = fromGeneralResp.substring(fromGeneralResp.lastIndexOf("=") + 1);
					String RespValTrimmed = fromGeneralRespVal.trim();
					String NumValTrimmed = fromGeneralNumVal.trim();

					Thread.sleep(3000);
					System.out.println("Message: " + RespValTrimmed);
					Thread.sleep(1000);
					System.out.println("From: " + NumValTrimmed);

					//Send to all other generals if received from generalFour[initiator]
					if(NumValTrimmed.equals("generalFour")){
						for (int i = 5; i <= 8; i++){
							int base = 1234;
							int j = i;
							int port = Integer.valueOf(String.valueOf(base) + String.valueOf(j));

							//correct
							if(port == NODE_PORT_2 )
							{
								//Don't Send To Self
								System.out.println("Port: " + port + "; Don't Send to self");
							}else
							{
								Socket toGeneral = new Socket(ip_address, port);
								//Send To Others
								System.out.println("Sending Data to port : " + port); 
								Thread.sleep(3000);
								// write text to the socket
								BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(toGeneral.getOutputStream()));
								bufferedWriter.write(generalTwoResponse);
								bufferedWriter.flush();

								try
								{ 
									// closing resources 
									//general_two_socket.close(); 
									toGeneral.close(); 

								}catch(IOException e){ 
									e.printStackTrace(); 
								}
							}

						}
					}

					switch(NumValTrimmed){
					case "generalOne" :
						//Updated flag as responded
						generalOneFlag = 1;

						//Add responses to Queue to display
						allResponses.put(NumValTrimmed, fromGeneralRespVal.trim());

						//Count the Ok responses given by 1
						if(RespValTrimmed.equals("1")){
							countVote++;
						}
						break;
					case "generalTwo" : 
						//Updated flag as responded
						generalTwoFlag = 1;

						//Add responses to Queue to display
						allResponses.put(NumValTrimmed, RespValTrimmed);

						//Count the Ok responses given by 1
						if(RespValTrimmed.equals("1")){
							countVote++;
						}
						break;
					case "generalThree" : 
						//Updated flag as responded
						generalThreeFlag = 1;

						//Add responses to Queue to display
						allResponses.put(NumValTrimmed, RespValTrimmed);

						//Count the Ok responses given by 1
						if(RespValTrimmed.equals("1")){
							countVote++;
						}
						break;				 
					case "generalFour" : 
						//Updated flag as responded
						generalFourFlag = 1;

						//Add responses to Queue to display
						allResponses.put(NumValTrimmed, RespValTrimmed);

						//Count the Ok responses given by 1
						if(RespValTrimmed.equals("1")){
							countVote++;
						}
						break;
					default :
						System.out.println("Invalid node : ");
						break; 
					}
					// If any of the general update flag, show vote count
					if(generalOneFlag == 1 || generalThreeFlag == 1 || generalFourFlag == 1)
					{
						Thread.sleep(3000);
						System.out.println("Responses Received: " + allResponses); 
						System.out.println("Votes Counted 1 [To War] : " + countVote);
						//If all have voted show final results
						if(generalOneFlag == 1 && generalThreeFlag == 1 && generalFourFlag == 1)
						{
							if(countVote >= 2){
								System.out.println("Final Decision : " + RespValTrimmed + " [To War]"); 
							}else{
								System.out.println("Final Decision : 0 [No War]"); 
							}
						}
					}

					/**try
					{ 
						// closing resources 
						general_two_socket.close(); 

					}catch(IOException e){ 
						e.printStackTrace(); 
					} **/


				}

			}

		}
		catch(Exception e)
		{
			System.out.println(e);
		} 
	}

	/**
	 * Main Program
	 **/
	public static void main(String[] args) 
	{  
		generalTwo node_2 = new generalTwo(IP , NODE_PORT_1, NODE_PORT_2, NODE_PORT_3, NODE_PORT_4); 
	} 

}
