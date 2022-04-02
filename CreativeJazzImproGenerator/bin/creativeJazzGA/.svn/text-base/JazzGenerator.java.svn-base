package creativeJazzGA;

import java.util.Random;
import jm.JMC;
import jm.music.data.*;


public class JazzGenerator implements JMC{

	private static Random generator = new Random();
	
	private static int generateBluesScaleNote(int key, int noteRange, int lowestNote)  {
		boolean found = false;
        int pitch = 0;
        int pitchMod12;
        while (!found)  { 
        	pitch = generateRandomInt(noteRange, 3)+lowestNote;   
       		pitchMod12 = (pitch+key) % 12;      // check note is in blues scale
       		switch (pitchMod12)  {
       			case 0: found = true; break;
       			case 3: found = true; break;
       			case 5: found = true; break;
       			case 6: found = true; break;
       			case 7: found = true; break;
       			case 10: found = true; break;
       			default: break;
       		}
       	}
        return pitch;
	}

	static Score generate(int polyphony, int maxNotesToGenerate, int key, int noteRange, int lowestNote, int rhythm, int noteRatio, int medianTempo, int tempoVariance)  {
		Score score = new Score();
		Part[] part = new Part[polyphony];      
		// Potentially the noteRange and lowestNote combination might lead to the piece trying to generate MIDI notes > 127
		// (if noteRange + lowestNote > 127)
		// These next few lines perform a check to make sure this can't happen
		if ((noteRange+lowestNote)>127)   {
			noteRange = 127-lowestNote;
		}
        Phrase[] randomPhrasesArray = generateRandomPhrasesArray(polyphony, maxNotesToGenerate, key, noteRange, lowestNote, rhythm, noteRatio, medianTempo, tempoVariance, initialPhrasesArray(polyphony));
        for (int i=0; i< polyphony; i++)  {
        	part[i] = new Part(randomPhrasesArray[i]);
        	score.addPart(part[i]);
        }
        return score;
	}

	private static Phrase[] initialPhrasesArray(int polyphony) {
		Phrase[] randomPhrasesArray = new Phrase[polyphony];
		for (int i=0; i<polyphony; i++)  {
			randomPhrasesArray[i] = new Phrase();
		}
		return randomPhrasesArray;
	}
	
	/**
	 * Generates a set of musical phrases at random, within some stylistic constraints:
	 * 	- notes taken from Blues scale
	 *  - notes must fit into the noteRange
	 * @param polyphony the number of phrases in the array
	 * @param key the key in which the phrases should be
	 * @param numNotesToGenerate the number of notes in total which will be added to the array
	 * @param randomPhrasesArray the container for the new phrases
	 * @return randomPhrasesArray an Phrase[] array containing the generated phrases
	 */
	private static Phrase[] generateRandomPhrasesArray(int polyphony, int numNotesToGenerate, int key, 
			int noteRange, int lowestNote, int rhythmMultiplier, int noteRatio, int medianTempo, int tempoVariance, Phrase[] randomPhrasesArray) {
		Note n;
		Rest r;
		int pitch, dynamic, phrase;
		double rhythmValue;
		boolean isNoteNotRest;
		
		// Set tempo
		randomPhrasesArray = setTempoMarking(medianTempo, tempoVariance, randomPhrasesArray);
		for (int i=0; i<numNotesToGenerate; i++)  {
        	pitch = generateBluesScaleNote(key, noteRange, lowestNote);
            rhythmValue = (double) generateRandomInt((rhythmMultiplier+1), 5) * (generator.nextDouble()/2.0); // 0.5 = quaver. /2.0 to allow smaller values
            // e.g. If note ratio is 75 %, then 75% of the time we want a note, 25% of the time we want a rest
            // generate a random no between 0-100 inclusive. 
            isNoteNotRest = (generator.nextInt(101)<=noteRatio);   // thank you leon for helping me with the logic here!
            dynamic = generator.nextInt(128);  // dynamic between 0-127
            phrase = generateRandomInt(polyphony, 0);
            
            if (isNoteNotRest) {
                n = new Note(pitch, rhythmValue, dynamic);            	
                randomPhrasesArray[phrase].addNote(n);      	
            }
            else {
            	r = new Rest(rhythmValue);
            	randomPhrasesArray[phrase].addRest(r);
            }
        }
		return randomPhrasesArray;
	}

	/**
	 * @param medianTempo
	 * @param tempoVariance
	 * @param randomPhrasesArray
	 */
	private static Phrase[] setTempoMarking(int medianTempo, int tempoVariance,
			Phrase[] randomPhrasesArray) {
		int tempo = 0;
		while (tempo<=0)  {  // make sure tempo is positive number (NB deliberately no check for upper tempo)
			if (generator.nextBoolean())  {
				tempo = medianTempo + generateRandomInt(tempoVariance, 9);
			} else tempo = medianTempo - generateRandomInt(tempoVariance, 9);
		}
		for (int i=0; i<randomPhrasesArray.length; i++)  {
			randomPhrasesArray[i].setTempo(tempo);
		}
		return randomPhrasesArray;
	}
	
	private static int generateRandomInt(int upperLimit, int improParameter)  {
		int randomInt = 10;  // arbitrarily chosen value - should be overridden
		try {
			randomInt = generator.nextInt(upperLimit);
		} catch(Exception e)  {
			System.err.println(upperLimit+" negative in generateRandomInt for case "+improParameter+" - default value applied");
			e.printStackTrace();
			switch (improParameter)  {
				case 0: randomInt = generator.nextInt(EvolveImprovisers.getMaxPolyphony());
				case 1: randomInt = generator.nextInt(EvolveImprovisers.getMaxNumNotes());
				case 2: randomInt = generator.nextInt(EvolveImprovisers.getMaxKey());
				case 3: randomInt = generator.nextInt(EvolveImprovisers.getMaxNoteRange()); 
				case 4: randomInt = generator.nextInt(EvolveImprovisers.getMaxLowestNote());
				case 5: randomInt = generator.nextInt(EvolveImprovisers.getMaxRhythmMult());
				case 6: randomInt = generator.nextInt(EvolveImprovisers.getMaxNoteRatio());
				case 7: randomInt = generator.nextInt(EvolveImprovisers.getMaxNumImpros());
				case 8: randomInt = generator.nextInt(EvolveImprovisers.getMaxMedianTempo());
				case 9: randomInt = generator.nextInt(EvolveImprovisers.getMaxTempoVar()); 
				//default:randomInt = generator.nextInt(10); // default value chosen at random - shouldn't be used
			}
		}
		return randomInt;
	}
}
