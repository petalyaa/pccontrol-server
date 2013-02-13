package org.pet.pccontrol;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JTextArea;

public class MouseEventListener {
        
        private boolean term = false;

	public MouseEventListener(){
	}

	public void startListen(int port, JTextArea textArea){
		ServerSocket echoServer = null;
		Socket clientSocket = null;
		try {
			echoServer = new ServerSocket(port);
			textArea.setText("Start listening on port : " + port);
		} catch (IOException e) {
			System.out.println(e);
		}
		while(!term){
			try {
				clientSocket = echoServer.accept();
				Thread t = new MouseMoverThread(clientSocket.getInputStream(), clientSocket.getOutputStream(), textArea);
				t.start();
			} catch (IOException e) {
				System.out.println(e);
			} 
		}
	}
        
        public void stopListen(){
            term = true;
        }

}
