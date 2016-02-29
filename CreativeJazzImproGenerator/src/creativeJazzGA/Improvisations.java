package creativeJazzGA;

import java.util.*;
import java.io.*;

import javax.sound.midi.*;

import jm.JMC;
import jm.music.data.*;
import jm.util.*;



public class Improvisations extends Thread implements JMC {

	private Score[] population, inspiringSet;
	private double[] ratings, value, typicality;
	private int populationCapacity, currentPopulationSize, inspiringSetCapacity;
	
	private int polyphony, maxNotes, key, noteRange, lowestNote, rhythm, restRatio, medianTempo, tempoVariance;

	// for MIDI playback of melodies
	static Sequence sequence;
	static Sequencer sequencer;
	
	private Random random = new Random();
	static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

	public Object clone()  throws CloneNotSupportedException {
		return super.clone();
	}
	public Improvisations(int numMembers, int[] improParameters, boolean initialisePopulation)  {
		populationCapacity = numMembers;
		currentPopulationSize = 0;
		population 	   = new Score[populationCapacity];
		ratings    	   = new double[populationCapacity]; 
		value 		   = new double[populationCapacity];
		typicality     = new double[populationCapacity];
		
		polyphony     = improParameters[0];
		maxNotes      = improParameters[1];
		key 	      = improParameters[2];
		noteRange     = improParameters[3];
		lowestNote    = improParameters[4];
		rhythm        = improParameters[5];
		restRatio     = improParameters[6];
		medianTempo   = improParameters[7];
		tempoVariance = improParameters[8];
				
		if (initialisePopulation) {
			initialisePopulation();
		}
		inspiringSet = population;
		inspiringSetCapacity = populationCapacity;
		
	}
	
	public static BufferedReader getBufferedReaderIn()   {
		return in;
	}
	/**
	 * 
	 */
	void initialisePopulation() {
		for (int i=0; i<populationCapacity; i++)  {
			addMemberAt(initialisePopulationMember(), i);
		}	
		savePopulation();
	}

	int getPopulationCapacity()  {
		return populationCapacity;
	}
	
	int getCurrentPopulationSize()  {
		return currentPopulationSize;
	}
	
/*	int getPopID()  {
		return popID;
	}*/
	
	Score[] getInspiringSet()  {
		return inspiringSet;
	}
	
	Score getInspiringSetMemberAt(int index)  {
		return inspiringSet[index];
	}

	int getInspiringSetSize()   {
		return inspiringSetCapacity;
	}
	
	boolean inInspiringSet(Score populationMember)  {
		boolean found = false;		
		for (int i=0; i<inspiringSetCapacity; i++)  {
			if (scoresAreEqual(populationMember, inspiringSet[i]))  {
				found = true;
				break;
			}
		}
		return found;
	}
	
	private boolean scoresAreEqual(Score score1, Score score2)   {
		boolean testingForEquality = true;
		//boolean scoresEqual = false; // default value
		if (score1.getSize() != score2.getSize())  {
			testingForEquality = false;
		} else if (score1.getPartArray().length != score2.getPartArray().length) {
			testingForEquality = false;
		} else if (score1.getKeyQuality() != score2.getKeyQuality())  {
			testingForEquality = false;
		} else if (score1.getKeySignature() != score2.getKeySignature()) {
			testingForEquality = false;
		} else if (score1.getNumerator() != score2.getNumerator())  {
			testingForEquality = false;
		} else if (score1.getDenominator() != score2.getDenominator())  {
			testingForEquality = false;
		} else if (score1.getHighestPitch() != score2.getHighestPitch())  {
			testingForEquality = false;
		} else if (score1.getLowestPitch() != score2.getLowestPitch())  {
			testingForEquality = false;
		} else if (score1.getLongestRhythmValue() != score2.getLongestRhythmValue())  {
			testingForEquality = false;
		} else if (score1.getShortestRhythmValue() != score2.getShortestRhythmValue()) {
			testingForEquality = false;
		} else if (score1.getEndTime() != score2.getEndTime())  {
			testingForEquality = false;
		} else if (score1.getTempo() != score2.getTempo()) {
			testingForEquality = false;
		} else if (!(score1.toString().equals(score2.toString())))  {  // final condition, done all the easier checks
			testingForEquality = false;
		}
		return testingForEquality;
	}
	
	

	/**
	 * Looks up the rating of the Population member at position [index] in the Population array
	 * 
	 * @param index The index of the Population member for which the rating is being looked up
	 * @return the rating of the Population member.
	 */
	double getRatingAt(int index)   {
		return ratings[index];
	}
	
	double[] getValueRatings()   {
		return value;
	}
	
	double[] getTypicalityRatings()  {
		return typicality;
	}
	
	double getValueRatingOfMemberAt(int index)   {
		return value[index];
	}
	
	double getTypicalityRatingOfMemberAt(int index)  {
		return typicality[index];
	}
	
	
	/**
	 * Looks up the member of the Population at position [index]
	 * 
	 * @param index the position in the Population being looked up
	 * @return the Score object containing the Population member required
	 */
	Score getPopulationMemberAt(int index)  {
		return population[index];
	}

	private String makeFilename(int index)  {
		String filename = "temp"+index+".mid";
		return filename;
	}
	
	/**
	 * Sets up a new (random) member of the Population from which the 
	 * genetic algorithm can start evolving from.
	 *
	 * @return      the Score object representing the new Population member
	 */	
	private Score initialisePopulationMember()  {
		Score score = JazzGenerator.generate(polyphony, maxNotes, key, noteRange, lowestNote, rhythm, restRatio, medianTempo, tempoVariance);
		return score;
	}

	static void savePopulationMember(Score scoreToBeSaved, String filename) {
		PrintStream originalStandardOutput = ImproviserCreativityFitnessFunction.divertStandardOutput();	
		Write.midi(scoreToBeSaved, filename);
		// Restore the output settings to standard output.
		System.setOut(originalStandardOutput);
	}
	
	

	/**
	 * Adds a new Score object representing a new member of the Population to the Population array
	 * 
	 * @param newMember The new population member to be added
	 * @param index The position at which the population member is to be added to the population array
	 */
	void addMemberAt(Score newMember, int index)  {
		
		population[index] = newMember;
		currentPopulationSize++;
	}
	
	/**
	 * Removes the member of the Population at position [index] and replaces it with null
	 * 
	 * @param index the position of the Population member being deleted
	 */
	void removeMemberAt(int index)  {  
		population[index] = null;
		currentPopulationSize--;
	}
	
	/** Step 1 of Genetic algorithm generation
	 *  This method chooses the fittest members of the population.
	 *  It leaves the Population array full of only the fittest members.
	 *  The rest of the Population array is null.
	 */
	void selection()  {   // TODO Lots of old GA methods to remove here
		
		ratePopulation();
		
	}
	
/*	*//**
	 * Performs 'house-keeping work' to make sure the Population array is in a suitable state for reuse
	 * and that the current array has been saved
	 * - Calls clearRatings() to clear it for future ratings.
	 * 
	 *//*
	void tidyUpPopulation() {
		savePopulation();
		clearRatings();	
	}
	
	*//**
	 * Deletes all rating information from the Ratings, value and typicality arrays
	 *//*
	private void clearRatings()   {
		ratings    = new double[populationCapacity];
		value      = new double[populationCapacity];
		typicality = new double[populationCapacity];
	}*/

	static void playMidiFile(String filename, Score score)  {
		try {
	        // From file
	        sequence = MidiSystem.getSequence(new File(filename));
	    
	        	        // Create a sequencer for the sequence
	        sequencer = MidiSystem.getSequencer();
	        sequencer.open();
	        sequencer.setSequence(sequence);
	    
	        // Start playing
	        sequencer.start();
	    } 
	    catch (Exception e) {
	    } 

        // QTPlayer.display(population[i]);
		//Instrument inst = new SimpleSineInst(44100);
		//Write.au(population[i], inst);
		//Play.midi(population[i]);
		/*Playback playback = new Playback(score);
		playback.run();*/
	}


	/**
	 * 
	 */
	static void stopPlayback() {
		//stop playing back the file
		try{
			sequencer.stop();
		}
		catch (Exception e)  {}
		sequencer.close();
	}

	void ratePopulation()  {
		int index1 = choosePopulationMemberAtRandom();	
		System.err.print("Rating example 1: ");
		rateChosenSample(index1);
		int index2 = index1;
		if (populationCapacity >= 2)  {   // if only one impro, can only rate one - otherwise rate two impros
			while (index2==index1)  {  // so it can only escape the while loop when it has chosen a different member of the population to index1
				index2 = choosePopulationMemberAtRandom();
			}
			System.err.print("Rating example 2: ");
			rateChosenSample(index2);
		}
		propogateRatings(index1, index2);
	}

	/**
	 * @param index
	 */
	private void rateChosenSample(int index) {
		String filename;
		filename = makeFilename(index);
		// View.notate(population[i]);
		playMidiFile(filename, population[index]);	
		ratePopulationMember(index); 
		stopPlayback();
	}

	/**
	 * Fills in the ratings for all individual improvisations by an Improviser, using two Improvisation examples that have
	 * been rated in ratePopulationMember as a sample of the entire Improvisations population for that Improviser.
	 * Takes the mean of the two value ratings and assigns this as the value rating for all unrated Improvisation examples.
	 * Does the same thing for the typicality ratings.
	 * 
	 * @param index1 Index position of the first Improvisation example to have been rated in ratePopulationMember()
	 * @param index2 Index position of the second Improvisation example to have been rated in ratePopulationMember()
	 */
	private void propogateRatings(int index1, int index2) {
		double aveValueRating = (double) (getValueRatingOfMemberAt(index1)+getValueRatingOfMemberAt(index2))/2.0;
		double aveTypicalityRating = (double) (getTypicalityRatingOfMemberAt(index1)+getTypicalityRatingOfMemberAt(index2))/2.0;
		for (int i=0; i<populationCapacity; i++)  {
			if ((i!=index1) && (i!=index2))  {
				value[i] = aveValueRating;
				typicality[i] = aveTypicalityRating;
			}
		}
		
	}
	/**
	 * @param index the Index number of the population member being rated
	 */
	private void ratePopulationMember(int index) {
		// rate out of 10 (0 to 10, where 0 is worst, and 10 is best) 
		long pieceDuration = Math.round(population[index].getEndTime());
		System.out.println("This piece is a maximum of "+pieceDuration+" seconds long");

// TODO zz PUT THIS LINE BACK IN AFTER DEBUGGING - TO MAKE THE RATING FUNCTION WAIT FOR THE PIECE TO FINISH BEFORE ASKING FOR A RATING
/*		try  {	sleep(pieceDuration *1000); 	} catch (Exception e)  {}
*/
		// ask what value the piece has
		rateForValue(index); 
		
		// ask how typical it is as a piece of jazz
		rateForTypicality(index); 
		ratings[index]    = (value[index]+typicality[index])/2.0;
		System.err.println("Value = "+value[index]+", Typicality = "+typicality[index]);
	}
	private void rateForTypicality(int index) {
		String typicalityString;
		boolean validInput;
		validInput = false;
		System.out.print("Please rate this piece on how typical it is, overall, as jazz (0=completely atypical, 10=totally typical): ");
		while (!validInput)  {
			try			{
				typicalityString = in.readLine(); 
				typicality[index] = (Integer.valueOf(typicalityString).intValue())/10.0;
				if ((typicality[index] >=0) && (typicality[index] <=1))  { // valid input entered
					validInput = true;
				} else System.out.print("This rating needs to be between 0 (worst) and 10 (best) - please re-enter your rating: ");
			} catch(Exception e) { 
				System.out.print("This rating needs to be between 0 (worst) and 10 (best) - please re-enter your rating: ");
				}	 
		}
	}
	
	/* 
	 * TODO: Want to change it to incorporate the following tests from Huron (2001):
	 * 
	 *  1.Toneness Principle   (CORE)
	 *  Strong auditory images are evoked
	 *  when tones exhibit a high degree of toneness or virtual pitch weight 
	 *  eg Harmonic complex tones between F2 and G5. 
	 *  
	 *  2. Principle of Temporal Continuity  (CORE)
	 *  In order to evoke strong auditory streams, use continuous or recurring rather than 
	 *  brief or intermittent sound sources. Intermittent sounds should be separated by no more 
	 *  than roughly 800 ms of silence in order to ensure the perception of continuity. 
	 *  But avoid overlapping sounds as in normal piano or gamelan (tutupan) performance.
	 *  
	 *  3. Minimum Masking Principle  (CORE)
	 *  Approximately equivalent amounts of spectral energy should fall in each 
	 *  critical band / auditory filter. For typical complex harmonic tones, this generally 
	 *  means that simultaneously sounding notes should be more widely spaced as the register 
	 *  descends.
	 *  
	 *  4. Tonal Fusion Principle  (CORE) 
	 *  To maintain independence avoid intervals that promote tonal fusion 
	 *  eg: unisons, octaves, perfect fifths
	 *  
	 *  5. Pitch Proximity Principle  (CORE)
	 *  The coherence of an auditory stream is maintained by close pitch proximity in 
	 *  successive tones within the stream. 
	 *  Pitch-based streaming is assured when pitch movement is within van Noorden's 
	 *  fission boundary (region 1 - normally 2 semitones or less for tones less than 700 ms 
	 *  in duration). 
	 *  When pitch distances are large, it may be possible to maintain the perception of a 
	 *  single stream by reducing the tempo. 
	 *  
	 *  6. Pitch Co-modulation Principle  (CORE)
	 *  The perceptual union of concurrent tones is encouraged when pitches move together 
	 *  maintaining constant ratios.
	 *  Darwin, Ciocca & Sandell, 1994 JASA showed that a mistuned harmonic continued to 
	 *  contribute to pitch with greater mistuning when coherent FM than when none. 
	 *  
	 *  7. Onset Synchrony Principle  (AUXILLIARY)
	 *  To keep parts independent, avoid note onsets of less than 100ms.
	 *  
	 *  8. Principle of limited density  (AUXILLIARY)
	 *  To keep parts independent, have 3 or fewer parts.
	 *  
	 *  9. Timbral differentation  (AUXILLIARY)
	 *  To keep parts independent, have unique timbres
	 *  
	 *  10. Spatial differentation  (AUXILLIARY)
	 *  To keep parts independent, have them spatially separated.
	 *  
	 *  Q. Are the six core principles selected specially by Huron for voice-leading or are they a 
	 *  necessary and sufficient set for satisfying psychoaccoustic constraints?
	 *  Q. Can the six core principles (and perhaps a weak inclusion of the 4 extra auxiliary 
	 *  principles as well) be computationalised easily?
	 *  Q. Does psychoaccoustic smoothness (?) correlate well enough to value of a musical piece? 
	 *  In other words what does value mean in music (and do we need to take a purely objective 
	 *  view of this? [easier for computers!] 
	 *  Or should we take context, personal preferences, external pressures, 
	 *  musical training etc into account?)
	 */
	private void rateForValue(int index) {
		String valueString;
		boolean validInput = false;
		System.out.print("Please rate this piece on how much you like it overall (0=totally hate it, 5=ambivalent, 10=totally love it): ");
			while (!validInput)  {
				try			{
					valueString = in.readLine(); 
					value[index] = (Integer.valueOf(valueString).intValue())/10.0;
					if ((value[index] >=0) && (value[index] <=1))  { // valid input entered
						validInput = true;
					} else System.out.print("This rating needs to be between 0 (worst) and 10 (best) - please re-enter your rating: ");
				} catch(Exception e) { 
					System.out.print("This rating needs to be between 0 (worst) and 10 (best) - please re-enter your rating: ");
					}	 
			}
	}
	


	/**
	 * Discards any members of the Population which are rated below 0.5 (out of maximum 1)
	 */
	private void selectBest()  {
		for (int i=0; i<populationCapacity; i++)  {
			if (ratings[i]<0.5)   {
				removeMemberAt(i);
			}
		}
	}
	

	
	
	/** Step 2 of Genetic algorithm generation
	 * Goes through the Population and fills any null positions in the Population 
	 * with a randomly chosen existing member of the Population
	 */
	void heredity()  {
		selectBest();
		if (currentPopulationSize==0)  {
			reInitialisePopulation();
		}
		else {
			fillInNewMembers();
		}
	}

	/**
	 * 
	 */
	private void fillInNewMembers() {
		Score newMember = null;
		for (int i=0; i<populationCapacity; i++)  {
			if (getPopulationMemberAt(i)==null)   {
				newMember = getPopulationMemberAt(choosePopulationMemberAtRandom());
				if (newMember!=null)  {
					addMemberAt(newMember, i);
					newMember = null;
				}
			}
		}
	}

	/**
	 * 
	 */
	private void reInitialisePopulation() {
		for (int i=0; i<populationCapacity; i++)  {
			population[i] = initialisePopulationMember();
		}
	}
	
	
	/**
	 * Chooses a member of the Population at random.
	 * Ignores any null elements in the Population array - if a null element is selected
	 * at random then the method chooses another element, and will never return a null element.
	 * 
	 * @return Randomly chosen member of the Population
	 */
	private int choosePopulationMemberAtRandom() {
		int position = 0;  // default value - will be changed by the while loop
		boolean found = false;
		Score member = null;
		if (currentPopulationSize>0)  {
			while (!found)  {
			position = random.nextInt(populationCapacity);
			member   = getPopulationMemberAt(position);
			if (member!=null)  {
				found = true;
				}
			}
		}
		return position;
	}

	/** Step 3 of Genetic algorithm generation
	 * 
	 */
	void variability()  {
		int numMutations = random.nextInt(100);
//		 TODO zzz remove debugging System.out.printlns
System.out.println("Number of mutations = "+numMutations);
		int memberToMutate;
		for (int i=0; i<numMutations; i++)   {
			memberToMutate = random.nextInt(populationCapacity);
			addMutationTo(memberToMutate);
		}
		savePopulation();
	}

	void savePopulation() {
		for (int i=0; i<populationCapacity; i++)  {
			String filename = makeFilename(i);
			savePopulationMember(population[i], filename);
		}
	}

	private void addMutationTo(int index) {
//		int levelOfMutation = random.nextInt(4); // choose the level at which the score will be mutated  
//			switch(levelOfMutation)  {
//				case(0): 
					mutateAtScoreLevel(index);  
//					break;
//				case(1): 
//					mutateAtPartLevel(index);   
//					break;
//				case(2): 
//					mutateAtPhraseLevel(index); 
//					break;
//				case(3): 
//					mutateAtNoteLevel(index);   
//					break;
//				default: 
//					break;
//			}
		//possible mutations:
	}

	@SuppressWarnings("unused")
	private void mutateAtPhraseLevel(int index) {
		// TODO mutateAtPhraseLevel
		// at Phrase level
		//		 void 	add(Note note)
		//         Add a note to this Phrase
		//void 	addChord(int[] pitches, double rv)
		//         Adds Multiple notes to the phrase all of which start at the same time and share the same duration.
		//void 	addNote(int pitch, double rv)
		//         Add a note to this Phrase
		//void 	addNote(Note note)
		//         Add a note to this Phrase
		//void 	addNoteList(double[] pitchAndRhythmArray)
		//         Adds Multiple notes to the phrase from one array of pitch, rhythm pairs
		//void 	addNoteList(double[] freqArray, double rhythmValue)
		//         Adds Multiple notes to the phrase from an array of frequency values
		//void 	addNoteList(double[] freqArray, double[] rhythmArray)
		//         Adds Multiple notes to the phrase from several arrays
		//void 	addNoteList(double[] freqArray, double[] rhythmArray, int[] dynamic)
		//         Adds Multiple notes to the phrase from several arrays
		//void 	addNoteList(double[] freqArray, double[] rhythmArray, int[] dynamic, boolean append)
		//         Adds Multiple notes to the phrase from several arrays A boolean option when true appends the notes to the end of the list if non true the current list is errased and replaced by the new notes
		//void 	addNoteList(double frequency, double[] rhythms)
		//         Adds Multiple notes to the phrase from one pitch and an array of rhythm values
		//void 	addNoteList(int[] pitchArray, double rhythmValue)
		//         Adds Multiple notes to the phrase from a pitch array and rhythm value
		//void 	addNoteList(int[] pitchArray, double[] rhythmArray)
		//         Adds Multiple notes to the phrase from several arrays
		//void 	addNoteList(int[] pitchArray, double[] rhythmArray, int[] dynamic)
		//         Adds Multiple notes to the phrase from several arrays
		//void 	addNoteList(int[] pitchArray, double[] rhythmArray, int[] dynamic, boolean append)
		//         Adds Multiple notes to the phrase from several arrays A boolean option when true appends the notes to the end of the list if non true the current list is errased and replaced by the new notes
		//void 	addNoteList(int[] pitchArray, double rhythmValue, int dynamic)
		//         Adds Multiple notes to the phrase from a pitch array, rhythm value, and dynmaic value
		//void 	addNoteList(int pitch, double[] rhythms)
		//         Adds Multiple notes to the phrase from one pitch and an array of rhythm values
		//void 	addNoteList(Note[] notes)
		//         Appends the specified notes to the end of this Phrase.
		//void 	addNoteList(Note[] noteArray, boolean append)
		//         Adds an array of notes to the phrase.
		//void 	addNoteList(java.util.Vector noteVector, boolean append)
		//         Adds a vector of notes to the phrase.
		//void 	addRest(Rest newRest)
		//         Add a rest to this Phrase
		//Phrase 	alias()
		//         Returns a carbon copy of a specified Phrase Changes to notes in the original or the alias will be echoed in the other.
		//boolean 	attemptAnchoringTo(Phrase anchor, Alignment alignment, double offset)
		//         The positions tries the phrase relative to another using the alignment specified.
		//Phrase 	copy()
		//         Returns a copy of the entire Phrase
		//Phrase 	copy(double startLoc, double endLoc)
		//         Returns a copy of a specified section of the Phrase, pads beginning and end with shortedend notes and rests if notes or phrase boundaries don't align with locations.
		//Phrase 	copy(double startLoc, double endLoc, boolean requireNoteStart)
		//         Returns a copy of a specified section of the Phrase, pads beginning and end with shortedend notes and rests if notes or phrase boundaries don't align with locations.
		//Phrase 	copy(double startLoc, double endLoc, boolean trimmed, boolean truncated, boolean startTimeShifts)
		//         Returns a copy of a specified section of the Phrase, pads beginning and end with shortedend notes and rests if notes or phrase boundaries don't align with locations.
		//Phrase 	copy(int highestPitch, int lowestPitch)
		//         Returns a copy of the entire Phrase only ontaining notes between highest and lowset specified pitch.
		//Note 	createNote()
		//         Generates and returns a new note with default values and adds it to this phrase.
		//void 	empty()
		//         Empty removes all elements in the note list vector
		//Anchoring 	getAnchoring()
		//         Returns details of how this is aligned relative to another phrase.
		//boolean 	getAppend()
		//         Return this phrases append status
		//double 	getBeatLength()
		//         Return the Duration of the phrase in beats.
		//int 	getDenominator()
		//         Returns the denominator of the Phrase's time signature
		//int[] 	getDynamicArray()
		//          
		//double 	getEndTime()
		//         Return the phrases endTime
		//int 	getHighestPitch()
		//         Return the pitch value of the highest note in the phrase.
		//int 	getInstrument()
		//         Return the program change assigned by this phrase
		//Phrase 	getLinkedPhrase()
		//         Return this phrases this phrase is linked to
		//double 	getLongestRhythmValue()
		//         Return the value of the longest rhythm value in the phrase.
		//int 	getLowestPitch()
		//         Return the pitch value of the lowest note in the phrase.
		//boolean 	getMute()
		//         Retrieve the current mute status.
		//Part 	getMyPart()
		//         returns a reference to the part that contains this phrase
		//Note 	getNote(int number)
		//         Get an individual note object by its number
		//Note[] 	getNoteArray()
		//         Returns the all notes in the phrase as a array of notes
		//java.util.Vector 	getNoteList()
		//         Returns the entire note list contained in a single voice
		//double 	getNoteStartTime(int noteIndex)
		//         Calculate the start time, in beats, of the note at the specified index.
		//int 	getNumerator()
		//         Returns the numerator of the Phrase's time signature
		//double 	getPan()
		//         Return the pan position for this phrase
		//int[] 	getPitchArray()
		//          
		//double[] 	getRhythmArray()
		//          
		//double 	getShortestRhythmValue()
		//         Return the value of the shortest rhythm value in the phrase.
		//int 	getSize()
		//         Get the number of notes in this phrase
		//double 	getStartTime()
		//         Return the phrase's startTime
		//double 	getTempo()
		//         Return the tempo in beats per minute for this phrase
		//java.lang.String 	getTitle()
		//         Return this phrases title
		//int 	getVolume()
		//         Retreive the current volume setting for this phrase.
		//int 	length()
		//         Get the number of notes in this phrase
		//void 	removeLastNote()
		//         Deletes the last note in the phrase
		//void 	removeNote(int noteNumb)
		//         Deletes the specified note in the phrase
		//void 	removeNote(Note note)
		//         Deletes the first occurence of the specified note in the phrase
		//void 	setAppend(boolean append)
		//         Gives the Phrase a new append status
		//void 	setDenominator(int dem)
		//         Specifies the denominator of the Phrase's time signature
		//void 	setDuration(double val)
		//         Change the Duration value of each note in the phrase.
		//void 	setDynamic(int dyn)
		//         Change the dynamic value of each note in the phrase.
		//void 	setInstrument(int value)
		//         Sets the program change value
		//void 	setLength(double newLength)
		//         Change both the rhythmValue and duration of each note in the phrase.
		//void 	setLinkedPhrase(Phrase link)
		//         Make a link from this phrase to another
		//void 	setMute(boolean state)
		//         Specify the mute status of this phrase.
		//void 	setMyPart(Part part)
		//         Sets a reference to the part containing this phrase
		//void 	setNote(Note n, int index)
		//          
		//void 	setNoteList(java.util.Vector newNoteList)
		//         Replaces the entire note list with a new note list vector
		//void 	setNumerator(int num)
		//         Specifies the numerator of the Phrase's time signature
		//void 	setPan(double pan)
		//         Determine the pan position for all notes in this phrase.
		//void 	setPitch(int val)
		//         Change the pitch value of each note in the phrase.
		//void 	setRhythmValue(double val)
		//         Change the rhythmValue value of each note in the phrase.
		//void 	setStartTime(double startTime)
		//         Sets the phrases startTime
		//void 	setTempo(double newTempo)
		//         Determine the tempo in beats per minute for this phrase
		//void 	setTitle(java.lang.String title)
		//         Gives the Phrase a new title
		//void 	setVolume(int val)
		//         Specify a new volume amount for this phrase.
		//int 	size()
		//         Get the number of notes in this phrase
		//java.lang.String 	toString()
		//         Prints the tracks attributes to stdout				
	}

	@SuppressWarnings("unused")
	private void mutateAtNoteLevel(int index) {
		// TODO mutateAtNoteLevel
		// at Note level
		//Note 	copy()
		//         Returns a copy of this note
		//double 	getDuration()
		//         Return note duration.
		//Phrase 	getMyPhrase()
		//         Return a reference to the phrase containing this note
		//double 	getOffset()
		//         Return note offset.
		//int 	getPitch()
		//         Retrieve the note's pitch as an integer.
		//boolean 	getPitchType()
		//         Retrieve note's pitch type
		//int 	getPitchValue()
		//         returns the pitches for the middle scale(default) on a keyboard
		//double 	getRhythmValue()
		//         Retrieve note's rhythmValue
		//double 	getSampleStartTime()
		//         Return note sampleStartTime
		//boolean 	isFlat()
		//         tells whether the note is a sharp or not by using its string value
		//boolean 	isNatural()
		//         tells whether the note is natural or not
		//boolean 	isRest()
		//         Check if the note is a rest or a pitched note.
		//boolean 	isScale(int[] scale)
		//         Checks if the note is within a particular scale There are a number of scale constants specified in the JMC which can be used with this method, these include MAJOR_SCALE, MINOR_SCALE, and PENTATONIC_SCALE
		//boolean 	isSharp()
		//         tells whether the note is a sharp or not
		//Note 	nextNote(int[] scale)
		//         gives the next note in the scale For example, if inputting a D note for a DMinor scale, the next note returned is an E if inputing a Bb for a C
		//void 	setDuration(double duration)
		//         Set notes duration.
		//void 	setDynamic(int dynamic)
		//         Assign notes dynamic
		//void 	setFrequency(double freq)
		//         Assign notes pitch as a frequency.
		//void 	setLength(double newLength)
		//         Change both the rhythmValue and duration of a note in the one step.
		//void 	setPan(double pan)
		//         Assign notes pan.
		//void 	setPitch(int pitch)
		//         Assign notes pitch.
		//void 	setPitchType(boolean newType)
		//         Specifies the note's pitch type.
		//void 	setRhythmValue(double rhythmValue)
		//         Assign notes rhythmValue
		//void 	setRhythmValue(double rv, boolean factorDuration)
		//         Sets the rhythmValue, and optionally change the duration at the same time.
		
	}

	@SuppressWarnings("unused")
	private void mutateAtPartLevel(int index) {
		// TODO mutateAtPartLevel
		// at Part level
		//void	add(Phrase phrase)
		//         Add a phrase to this Part
		//void 	addNote(Note n, double startTime)
		//         Add a note directly to a part, this method automatically encapsulates the note within a phrase.
		//void 	addPhrase(Phrase phrase)
		//         Add a phrase to this Part Phrases with a 'true' append flag are added to the end of the part.
		//void 	appendPhrase(Phrase phrase)
		//         Add a copy of a phrase to the end of this Part
		//void 	clean()
		//         Remove phrases from the score.
		//Part 	copy(double startLoc, double endLoc)
		//         Returns a copy of the Part between specified loactions
		//Phrase 	createPhrase()
		//         Generates and returns a new empty phrase and adds it to this part.
		//void 	empty()
		//         Empty removes all elements in the vector
		//int 	getChannel()
		//         Gets the channel for this channel
		//int 	getDenominator()
		//         Returns the Parts time signature denominator
		//double 	getEndTime()
		//         Return the Parts endTime
		//int 	getHighestPitch()
		//         Return the value of the highest note in the part.
		//int 	getInstrument()
		//         Get Instrument number / MIDI Program Change
		//int 	getKeyQuality()
		//         Returns the Parts key quality 0 is Major, 1 is minor
		//int 	getKeySignature()
		//         ' Returns the Parts key signature The number of sharps (+) or flats (-)
		//double 	getLongestRhythmValue()
		//         Return the value of the longest rhythm value in the part.
		//int 	getLowestPitch()
		//         Return the value of the lowest note in the part.
		//Score 	getMyScore()
		//         get a reference to the score that contains this part
		//int 	getNumerator()
		//         Returns the Parts time signature numerator
		//double 	getPan()
		//         Return the pan position for this part
		//Phrase 	getPhrase(int number)
		//         Get an individual phrase object from its number
		//Phrase[] 	getPhraseArray()
		//         Returns the all phrases in this part as a array
		//java.util.Vector 	getPhraseList()
		//         Returns the entire phrase list
		//double 	getPoint()
		//          
		//double 	getShortestRhythmValue()
		//         Return the value of the shortest rhythm value in the part.
		//int 	getSize()
		//         get the number of phrases in this part
		//double 	getTempo()
		//         Returns the Part's tempo
		//long 	getTime()
		//          
		//int 	getTimeIndex()
		//          
		//java.lang.String 	getTitle()
		//         Returns the Parts title
		//int 	getVolume()
		//         Returns the Part's volume
		//int 	length()
		//         Get the number of phrases in this part
		//void 	removeAllPhrases()
		//         Deletes all the phrases previously added to the part
		//void 	removeLastPhrase()
		//         Deletes the last phrase added to the part
		//void 	removePhrase(int phraseNumb)
		//         Deletes the specified phrase in the part
		//void 	removePhrase(Phrase phrase)
		//         Deletes the first occurence of the specified phrase in the Part.
		//void 	setChannel(int channel)
		//         Sets the MidiChannel for this part
		//void 	setDenominator(int dem)
		//         Specifies the Part's time signature denominator
		//void 	setDuration(double val)
		//         Change the duration value of each note in the Part.
		//void 	setDynamic(int dyn)
		//         Change the dynamic value of each note in the Part.
		//void 	setInstrument(int instrument)
		//         Set instrument number / MIDI Program Change
		//void 	setKeyQuality(int newQual)
		//         Specifies the Part's key quality 0 is Major, 1 is minor
		//void 	setKeySignature(int newSig)
		//         Specifies the Part's key signature The number of sharps (+) or flats (-)
		//void 	setLength(double newLength)
		//         Change both the rhythmValue and duration of each note in the part.
		//void 	setMyScore(Score scr)
		//         set a reference to the score containing this part
		//void 	setNumerator(int num)
		//         Specifies the Part's time signature numerator
		//void 	setPan(double pan)
		//         Determine the pan position for all notes in this part.
		//void 	setPhraseList(java.util.Vector newPhraseList)
		//         Updates the entire phrase list
		//void 	setPitch(int val)
		//         Change the Pitch value of each note in the Part.
		//void 	setPoints(double[] p)
		//          
		//void 	setProgChg(int program)
		//         Set instrument number / MIDI Program Change This method is deprecated in favour of setInstrument!!
		//void 	setRhythmValue(double val)
		//         Change the rhythmValue value of each note in the Part.
		//void 	setTempo(double tempo)
		//         Sets the Part's tempo
		//void 	setTime(long[] t)
		//          
		//void 	setTimeIndex(int index)
		//          
		//void 	setTitle(java.lang.String title)
		//         Sets the Parts title
		//void 	setVolume(int volume)
		//         Sets the Part's volume
		//int 	size()
		//         get the number of phrases in this part
		//void 	sort()
		//         Orders the phrases in the phrase list by start time.
		//java.lang.String 	toString()
		//         Collects the Parts attributes to a string		
	}

	private void mutateAtScoreLevel(int index) {
		// at Score level:
		int mutationChoice = random.nextInt(6);  // 6 possible mutations listed below
//		 TODO zzz remove debugging System.out.printlns
System.out.println("Mutating member at position "+index+" with mutation "+mutationChoice);		
		switch(mutationChoice)  {
			case(0): {
				//void 	removePart(int partNumb)
				//		Deletes the specified Part in the Score
				int numParts = population[index].getSize();
				if (numParts>1)  {          // check there will be a part left after deletion
					int partNumb = random.nextInt(numParts);
					population[index].removePart(partNumb);
				}
				break;
			}
			case(1): {
				//void 	addPart(Part part)
				//        add the specified Part to the Score
				Part newPart = JazzGenerator.generate(1, maxNotes, key, noteRange, lowestNote, rhythm, restRatio, medianTempo, tempoVariance).getPart(0);
				// TODO zzz remove debugging System.out.printlns
System.out.println("EndTime of music before adding part = "+population[index].getEndTime());
				population[index].addPart(newPart);
System.out.println("EndTime of music after adding part  = "+population[index].getEndTime());
				break;
			}
			case(2): {
				//void 	setNumerator(int num)/setDenominator
				//        Specifies the Score's time signature numerator/denominator
				int timeSig = random.nextInt(16)+1;
				boolean numerator = random.nextBoolean();
				if (numerator)  {
					population[index].setNumerator(timeSig);	
				}
				else {
					population[index].setDenominator(timeSig);
				}
				break;
			}
			case(3): {
				//void 	setKeyQuality(int newQual)
				//        Specifies the Score's key quality 0 is Major, 1 is minor
				int newQual = random.nextInt(2);
				population[index].setKeyQuality(newQual);
				break;
			}
			case(4): {
				//void 	setKeySignature(int newSig)
				//        Specifies the Score's key signature The number of sharps (+) or flats (-)
				int newSig = random.nextInt(15)-7;
				population[index].setKeySignature(newSig);
				break;
			}
			case(5): {
				//void 	setTempo(double tempo)
				//        Sets the Score's tempo
				double tempo = random.nextDouble()+0.0005;
				population[index].setTempo(tempo);
				break;
			}
			default: break;
		}
	}

	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}
	public static Improvisations makeImprovisations(Object applicationData) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
