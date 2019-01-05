package doryanbessiere.voiceapi.server;

public class Starter {

	public static void main(String[] args) {
		VoiceAPI voiceAPI = new VoiceAPI(500);
		voiceAPI.start();
	}
}
