#!/usr/bin/python         

#General Number : 3
import socket               # Import socket module
import time

serverName = '127.0.0.1'
NODE_PORT_1 = 12345
NODE_PORT_2 = 12346
NODE_PORT_3 = 12347
NODE_PORT_4 = 12348

#Message for current general
generalFourMsg = "generalFourResponse = 1 | node =generalFour"
serverName = "localhost"
# Function that sends messages to sockets
def send_data( serverLocahost, port, msg):
    client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    client_socket.connect((serverLocahost, port))
    while 1:
        data = msg

        client_socket.sendto(data.encode(),(serverName, NODE_PORT_1))
        client_socket.close()
        break;


generalFourSocket = socket.socket()         # Create a socket object
host = "localhost" # Get local machine name
port = NODE_PORT_3              # Reserve a port for your service.
generalFourSocket.bind((host, NODE_PORT_3))        # Bind to the port

#Flags That will assist to count and see already visited nodes
generalOneFlag = 0			
generalTwoFlag = 0			
generalThreeFlag = 0		
generalFourFlag = 0			
countVote = 0

generalFourSocket.listen(5)                 # Now wait for general connection.
while True:
   time.sleep(4)
   print('\nListening ...')
   c, addr = generalFourSocket.accept()     # Establish connection with general.
   data=c.recv(20000)
   decodedMsg = data.decode()
   time.sleep(4)

   # Extracting relevant info from incoming message
   generalResponseString,generalIdString = decodedMsg.split(' | ')
   generalResponseDiscard,generalResponse = generalResponseString.split(' =')
   generalIdDiscard,generalId = generalIdString.split(' =')
   print('General Response => ', generalResponse)
   print('General Identity => ', generalId)
   time.sleep(4)

   if generalId == 'generalFour': # Only send to all if 4th general returns a response
        for i in range(5,9):
            base = 1234
            j = i
            generalPort = str(base) + str(j) # concatenating integer values to string
            newPort = int(generalPort) # COnverting the resultant to integer for the port
            if(newPort == NODE_PORT_3): #Restrict sending to self and starting node
                print('Don\'t Send to self at port => ', newPort)
                time.sleep(4)
            else:
                print('Sending to port => ', newPort)
                time.sleep(4)
                send_data("localhost",newPort,"generalThreeResponse =1 | node =generalThree")

   if(generalId == 'generalOne'):
        generalOneFlag = 1
        print('Updating FLag for generalOne ')
        if(generalResponse == '1'):
            countVote += 1

   if(generalId == 'generalTwo'):
        generalTwoFlag = 1
        print('Updating FLag for generalTwo ')
        if(generalResponse == '1'):
            countVote += 1
            
   if(generalId == 'generalThree'):
        generalThreeFlag = 1
        print('Updating FLag for generalThree ')
        if(generalResponse == '1'):
            countVote += 1
            
   if(generalId == 'generalFour'):
        generalFourFlag = 1
        print('Updating FLag for generalFour ')
        if(generalResponse == '1'):
            countVote += 1
   # If any of the general update flag, show vote count
   if generalOneFlag == 1 or generalTwoFlag == 1 or generalFourFlag == 1 : #Restrict sending to self and starting node
        time.sleep(4)
        print("Vote Count for 1 [To War] : ", countVote)
        # If all of the general update flag, show final decision
        if generalOneFlag == 1 and generalTwoFlag == 1 and generalFourFlag == 1:
            if(countVote >= 2):
                print("Final Decision : 1 [To War]")
            else:
                print("Final Decision : 0 [No War]")
         
                 
   c.close() 
