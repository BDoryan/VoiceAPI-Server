package doryanbessiere.voiceapi.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.UUID;

public class Client extends Thread {
	
	private UUID uuid;
	private Socket client;
	private Thread thread;
	private BufferedInputStream input;
	private BufferedOutputStream out;
	private VoiceAPI voiceAPI;

	private int size = 10000;
	private byte buffer[] = new byte[size];
	
	public Client(VoiceAPI voiceAPI, Socket socket) {
		this.voiceAPI = voiceAPI;
		this.uuid = UUID.randomUUID();
		this.client = socket;
	}
	
	public void kick() {
		try {
			this.client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Socket getSocket() {
		return client;
	}

	public UUID getUUID() {
		return uuid;
	}
	
	public Thread getThread() {
		return thread;
	}
	
	public BufferedOutputStream getOutput() {
		return out;
	}
	
	public BufferedInputStream getInput() {
		return input;
	}

	@Override
	public void run() {
		try {
			input = new BufferedInputStream(client.getInputStream());
			out = new BufferedOutputStream(client.getOutputStream());
			
			while (voiceAPI.isOnline() && input.read(buffer) != -1) {
				voiceAPI.sendVoice(this, buffer);
			}
		} catch (IOException e) {
			this.voiceAPI.disconnect(this);
		}
	}
}
