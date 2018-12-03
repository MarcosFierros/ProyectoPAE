package sockets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable{

	ServerSocket serverSocket;
	Socket clientSocket;
	File file;
	
	public void run() {
		try {
			clientSocket = serverSocket.accept();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		InetAddress clientAddress = clientSocket.getInetAddress();  
		String clientName = clientAddress.getHostName();
		
		System.out.println("Client " + clientName + " with IP " + clientAddress.getHostAddress() + " just connected!");
		
		try {
			sendFile(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public Server(File file) throws IOException {
		serverSocket = new ServerSocket(10001);
		this.file = file;
	}
	
	public void sendFile(File file) throws IOException {
         
		DataInputStream din = new DataInputStream(clientSocket.getInputStream());  
		DataOutputStream dout = new DataOutputStream(clientSocket.getOutputStream());  
		try{
			
			String str="";  
	
			str=din.readUTF();
			System.out.println("SendGet....Ok");
		
			if(!str.equals("stop")){  
		
				System.out.println("Sending File: "+file.getPath());
				dout.writeUTF(file.getPath());  
				dout.flush();  
		
				FileInputStream fin = new FileInputStream(file);
				long sz = (int) file.length();
		
				byte b[] = new byte [1024];
		
				int read; 
		
				dout.writeUTF(Long.toString(sz)); 
				dout.flush(); 
		
				System.out.println ("Size: "+sz);
				System.out.println ("Buf size: "+serverSocket.getReceiveBufferSize());
		
				while((read = fin.read(b)) != -1){
				    dout.write(b, 0, read); 
				    dout.flush(); 
				}
				fin.close();
		
				System.out.println("..ok"); 
				clientSocket.close();  
			}
			
		} catch (Exception e) {
			
		}
	}
	
	public void closeSocket() throws IOException {
		serverSocket.close();
	}
	
}
