package audio;

import org.lwjgl.openal.AL10;

public class Source {

	
	private int sourceId;
	
	public Source(){
		sourceId = AL10.alGenSources();
		AL10.alSourcef(sourceId, AL10.AL_GAIN, 1);
		AL10.alSourcef(sourceId, AL10.AL_PITCH, 1);
		AL10.alSource3f(sourceId, AL10.AL_POSITION, 0, 0, 0);
	}
	
	public void play(int buffer){
		AL10.alSourcei(sourceId, AL10.AL_BUFFER, buffer);
		AL10.alSourcePlay(sourceId);
	}
	
	public void pause(int buffer){
		// This does not work in pausing the music
		// play() will start the music from the beginning
		AL10.alSourcei(sourceId, AL10.AL_BUFFER, buffer);
		AL10.alSourcePause(sourceId);
	}
	
	public void mute(){
		// will mute but the music will continue playing
		AL10.alSourcef(sourceId, AL10.AL_GAIN, 0);
	}
	
	public void unmute(){
		AL10.alSourcef(sourceId, AL10.AL_GAIN, 1);
	}
	
	public void stop(int buffer){
		AL10.alSourcei(sourceId, AL10.AL_BUFFER, buffer);
		AL10.alSourceStop(sourceId);
	}
	
	public void delete(){
		AL10.alDeleteSources(sourceId);
	}
	
	public int getSourceID(){
		return this.sourceId;
	}
}
