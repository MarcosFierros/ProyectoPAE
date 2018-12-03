package sockets;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

import MusicPlayer.ui.ControllerClient;

public class Client implements Runnable{

	Socket clientSocket;
	ControllerClient cc;

	public Client(ControllerClient cc) throws UnknownHostException, IOException {
		clientSocket = new Socket("localHost", 10001);
		this.cc = cc;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		try {
			
			DataInputStream din = new DataInputStream(clientSocket.getInputStream());  
			DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());  
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));  
			
			String str="start",filename="";  
			
			while(!str.equals("start"))
				str = br.readLine(); 
			 
			dos.writeUTF(str); 
			dos.flush();  
			
			filename = din.readUTF(); 
			System.out.println("Receving file: " + filename);
			filename = "C:\\tmp\\streamingTmp.mp3";
			System.out.println("Saving as file: " + filename);
			
			long size = Long.parseLong(din.readUTF());
			System.out.println ("File Size: "+(size/(1024*1024))+" MB");
	
			byte[] b = new byte [1024];
			System.out.println("Receving file..");
			FileOutputStream fos = new FileOutputStream(new File(filename),true);
			long bytesRead;
			do {
				bytesRead = din.read(b, 0, b.length);
				fos.write(b,0,b.length);
			} while(!(bytesRead < 1024));
			System.out.println("Comleted");
			fos.close(); 
			dos.close();  	
			clientSocket.close();  
			
			cc.getFile(filename);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
