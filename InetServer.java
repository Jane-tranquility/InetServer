

import java.io.*;                 //import everything in package java.io
import java.net.*;                //import everything in package java.net


/*
 The class InetServer contains the main function,
 it opens the server and make it wait and listen to clients
 */
public class InetServer {

	public static void main(String[] args) throws IOException {//main function
		
		int q_len=6;       //the number of queues can get in operating system
		int port=57384;     //the port number is defined for the server
		Socket sock;
		ServerSocket servsock=new ServerSocket(port, q_len);   //implement the server socket, the server socket is open
		System.out.println("Jing Li's InetServer 1.8 starts to listen to port 573854.\n");
		while (true){
			sock=servsock.accept();                     //the server socket waits to be connected to a request and listen to it
			new Worker(sock).start();                   //the request starts, using the Worker class below, supports multithreading
		}

	}

}

/*
 create a class Worker which can been seen as a client, do the job for each thread.
*/
class Worker extends Thread{   //definition of Worker class
	Socket sock;               //define the class attaribute: sock, which is an instance of class Socket
	Worker (Socket s){sock=s;} //constructor of the Worker class
	
    /*
     the method run reads the message coming into the socket, 
     and then according to the message of each line, 
     print out its IP address and host name
    */
	public void run(){
		PrintStream out=null;         //create a new PrintStream instance out
		BufferedReader in=null;       //create a new BufferedReader instance in
		try{
			in=new BufferedReader(new InputStreamReader(sock.getInputStream()));  //in is a buffered read to read the character-based text coming into the socket
			out=new PrintStream(sock.getOutputStream());                          //the variable out is created to print the output of the socket
			try{
				String name;
				name=in.readLine();                          //read each line of the text coming into the socket and assign it to name
				System.out.println("Looking up "+name);      //print out "Looking up" and the name on the console
				printRemoteAddress(name, out);               //use printRemoteAddress to nicely print out the IP address and host name we want
			}catch(IOException x){
				System.out.println("Server read error");     //if IOException, print out an error message
				x.printStackTrace();
			}
			sock.close();         //close the Socket we've open
		}catch(IOException ioe){
			System.out.println(ioe);              //if IOException, print it out
		}
	}
    
    /*
     The method printRemoteAddress is used to format the information you want to print out:
     - a line to see what the name is
     - the hose name of the name
     - the IP address of the name
     If fialed to do so, also print out an error message
	*/
	static void printRemoteAddress(String name, PrintStream out){
		try{
			out.println("Looking up "+name+"...");                     //print out the name wanted to be printed to the client
			InetAddress machine=InetAddress.getByName(name);           //get the Internet Protocol(IP) address by the name
			out.println("Host name: "+machine.getHostName());          //print out the Host name according to the IP address to the client
			out.println("Host IP: "+toText (machine.getAddress()));    //print out the IP address using toText function defined below, which is portable for 128 bit format to the client
		}catch(UnknownHostException ex){
			out.println("Failed in attempt to look up"+name);          //If we failed to find the IP address for the name variable, then print out the error message
		}
	}
	
    /*
     A method toText is used to make the IP address portable for 128 bit format
    */
	static String toText(byte ip[]){
		StringBuffer result=new StringBuffer();
		for (int i=0; i<ip.length;i++){
			if (i>0){
				result.append(".");
			}
			result.append(0xff&ip[i]);	
		}
		return result.toString();
	}
	
}
