package doryanbessiere.voiceapi.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class VoiceAPI extends Thread {

	private int port;
	private ServerSocket server;
	
	private ArrayList<Client> clients = new ArrayList<>();
	
	public VoiceAPI(int port) {
		this.port = port;
	}
	
	@Override
	public void run() {
		try {
			server = new ServerSocket(this.port);
			System.out.println("server online on "+this.port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		while(isOnline()) {
			try {
				Client client = new Client(this, server.accept());
				client.start();
				clients.add(client);
				System.out.println("["+client.getSocket().getInetAddress().getHostAddress()+":"+client.getSocket().getPort()+"] connected.");
			} catch (IOException e) {
			}
		}
	}
	
	public boolean isOnline() {
		return !server.isClosed();
	}
	
	public void sendVoice(Client client_, byte[] buffer) {
		for(Client client : clients) {
			if(!client.getUUID().toString().equals(client_.getUUID().toString())) {
				try {
					client.getOutput().write(buffer, 0, buffer.length);
				} catch (IOException e) {
					disconnect(client);
				}
			}
		}
	}

	public void disconnect(Client client) {
		client.kick();
		System.out.println("["+client.getSocket().getInetAddress().getHostAddress()+":"+client.getSocket().getPort()+"] disconnected.");
		clients.remove(client);
		client.stop();
	}
}
