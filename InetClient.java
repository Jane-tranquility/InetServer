

import java.io.*;              //import everything in package java.io
import java.net.*;             ////import everything in package java.net

/*
 The class InetClient creates a connection with the server, send the input from System.in to the server
 Would loop to get input continuously until typing in "quit"
 */
public class InetClient {
	public static void main(String[] args){//main function
		String serverName;                         //define the server name
		if (args.length<1) {
			serverName="localhost";                //if given any arguement, assign the arguement to the server name; if not, set it to local host
		}else {serverName=args[0];}
		
		System.out.println("Jing Li's Inetclient, 1.8.\n");                 //print out some information on the console, including sername, and port number
		System.out.println("Using server: "+serverName+", port 573854");
		BufferedReader in=new BufferedReader(new InputStreamReader(System.in));  //define variable in as the content form system input
		
		try{
			String name;
			do {                      //do the loop until type in "quit"
				System.out.println("Enter a hostname or an IP address, (quit) to end: "); //print out the message on the console to get an input
				System.out.flush();
				name=in.readLine();                        // assign the input to a String variable called name
				if (name.indexOf("quit")<0) {              //if there's no "quit" in the input
					getRemoteAddress(name, serverName);    //use the getRemoteAddress method to print out some information nicely
				}
			}while(name.indexOf("quit")<0);
			System.out.println("Cancelled by user request.");  //if there's "quit", print out "Canceled by the user request" on the console
			  
		}catch(IOException e){
			e.printStackTrace();                              //if exists the IOException, print the satck trace
		}
		
	}
	
    /*
     the method getRemoteAddress implements a socket
     send the name to the server socket
     and get information back and print back on the client console
     then close the socket
    */
	static void getRemoteAddress(String name, String serverName){   //the method having two arguments: name and serverName
		Socket sock;
		BufferedReader fromServer;
		PrintStream toServer;
		String textFromServer;
		
		try{
			sock=new Socket(serverName, 57384);                  //create a new sock which is the server socket
			fromServer=new BufferedReader(new InputStreamReader(sock.getInputStream()));   //define the input stream from the server socket
			toServer=new PrintStream(sock.getOutputStream());   //define an outstream to the server socket
			toServer.println(name);         //sent the name as an output to the server socket
			toServer.flush();               //flush the stream
		    for (int i=0;i<=3; i++){                        //loop for 3 times
                textFromServer=fromServer.readLine();       //read line by line of the input from the server socket
                if (textFromServer!=null){
                    System.out.println(textFromServer);      //if the content is not empty, print out the message on the console
		    	}
		    }
		    sock.close();                                  
		}catch(IOException e){
			System.out.println("Socket Error!");            //IOException, prints an error message
			e.printStackTrace();
		}
	}
    
    
    /*
     A method toText is used to make the IP address portable for 128 bit format
     */
    static String toText(byte ip[]){
        StringBuffer result=new StringBuffer();
        for (int i=0; i<ip.length; i++){
            if (i>0){
                result.append(".");
            }
            result.append(0xff&ip[i]);
        }
        return result.toString();
    }
}
