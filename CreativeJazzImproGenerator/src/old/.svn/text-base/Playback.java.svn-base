package old;

import jm.util.*;
import jm.music.data.*;



public class Playback extends Thread {

	Score score;
	
	public Playback(Score scoreToBePlayed)  {
		score = scoreToBePlayed;
	}
	
	public void run()  {
		Play.midi(score);
	}
}
