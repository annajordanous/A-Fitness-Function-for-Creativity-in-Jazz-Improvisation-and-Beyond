package creativeJazzGA;

import java.io.*;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;

/* import jm.music.data.Score;
import jm.util.Write;*/



import org.jgap.*;

/**
 * 
 */

public class ImproviserCreativityFitnessFunction extends FitnessFunction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//private static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	static Sequence sequence;
	static Sequencer sequencer;
	
	public ImproviserCreativityFitnessFunction()  {
		
	}
	
	static void playMidiFile(String filename)  {
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


	/*
	 * See org.jgap.FitnessFunction#evaluate(org.jgap.IChromosome)
	 */
	@Override
	protected double evaluate(IChromosome chromosome) {
		
		// NB all fitness measures for individual genes are normalised to be between 0 and 1 (inclusive)
		Integer polyphony  		   = EvolveImprovisers.getPolyphonyOf(chromosome);
		Integer maxNotesToGenerate = EvolveImprovisers.getMaxNotesOf(chromosome);		
		Integer key        		   = EvolveImprovisers.getKeyOf(chromosome);
		Integer noteRange  		   = EvolveImprovisers.getNoteRangeOf(chromosome);
		Integer lowestNote 		   = EvolveImprovisers.getLowestNoteOf(chromosome);
		Integer rhythmMultiplier   = EvolveImprovisers.getRhythmMultiplierOf(chromosome);
		Integer noteRestRatio 	   = EvolveImprovisers.getNoteRestRatioOf(chromosome);
		Integer medianTempo        = EvolveImprovisers.getMedianTempoOf(chromosome);
		Integer tempoVariance      = EvolveImprovisers.getTempoVarianceOf(chromosome);
		int[] improParameters = new int[9];
		improParameters[0] = polyphony;
		improParameters[1] = maxNotesToGenerate;
		improParameters[2] = key;
		improParameters[3] = noteRange;
		improParameters[4] = lowestNote;
		improParameters[5] = rhythmMultiplier;
		improParameters[6] = noteRestRatio;
		improParameters[7] = medianTempo;
		improParameters[8] = tempoVariance;
		
		// Score score = RandomSounds.generate(polyphony, maxNotesToGenerate, key, noteRange, lowestNote);
		Integer noOfImprovisations   = EvolveImprovisers.getNumImprovisationsOf(chromosome);
		// TODO this is where the improviser output is generated and put into a new Improvisations object
		Improvisations improviserOutput = new Improvisations(noOfImprovisations, improParameters, true);
		String filename = EvolveImprovisers.toString(chromosome);

		System.err.println("Evaluating "+filename);
		// old from 2010 code
		// double fitness = evaluateWithRitchieCriteria(improviserOutput);
		// new fitness function
		double fitness = evaluateWithMultiObjectives(improviserOutput);
		return fitness;
	}

	private double evaluateWithMultiObjectives(Improvisations improviserOutput)  {
		improviserOutput.ratePopulationSocialCommunicationAndInteraction();
		improviserOutput.ratePopulationDomainCompetence();
		improviserOutput.ratePopulationIntentionAndEmotionalInvolvement();
		double fitness = ComponentObjectives.calculateWeightedSum(improviserOutput);
		return fitness;
	}

	private double evaluateWithRitchieCriteria(Improvisations improviserOutput) {
		// rate all and evolve population accordingly (on the assumption it gets picked later)
		improviserOutput.ratePopulationWithRitchie(); 
		// use fitness ratings to calculate Ritchie's criteria on that improviser/composer's output
		double fitness = RitchieCriteria.calculateRitchieCriteria(improviserOutput);
		presentCriteriaAndWaitTillUserReady();
		// return some weighted sum of the criteria as the fitness function score for that improviser/composer
		return fitness;
	}

	private void presentCriteriaAndWaitTillUserReady() {
		System.out.print("Press ENTER to continue when you have finished looking at the output of Ritchie's criteria for this Improviser");
		BufferedReader in = Improvisations.getBufferedReaderIn();
		try			{
			in.readLine();   // dont worry about doing anything with this, just pause till some input received to continue 
		} catch(Exception e) { }
	}

	/**
	 * Assigns the output PrintStream to a temporary text file.
	 * This is because parts of the JMusic package produce a lot of output to the console
	 * which isn't needed and clutters up the console.
	 * 
	 * @return a copy of the original System.out PrintStream, for later re-assignment
	 */
	static PrintStream divertStandardOutput() {
		// Save the current standard input, output, and error streams
		// for later restoration.
		PrintStream	origOut	= System.out;
		try{
			File output = new File("temp_output.txt");
			output.createNewFile();
		}		catch (Exception e)  { e.printStackTrace(origOut);  }		
		try{
			System.setOut(new PrintStream(new FileOutputStream("temp_output.txt")));
		}		catch (Exception e)  { e.printStackTrace(origOut);  	}
		return origOut;
	}

	/**
	 * Assigns the error PrintStream to a temporary text file.
	 * This is to allow a log file (creativityGAResults.log) to be created which holds 
	 * log data from running the program, for later analysis. 
	 * 
	 * @return a copy of the original System.err PrintStream, for later re-assignment
	 */
	static PrintStream divertStandardError() {
		// Save the current standard input, output, and error streams
		// for later restoration.
		PrintStream	origErr	= System.err;
		try{
			File error = new File("creativityGAResults.log");
			error.createNewFile();
		}		catch (Exception e)  { e.printStackTrace(origErr);  }		
		try{
			System.setErr(new PrintStream(new FileOutputStream("creativityGAResults.log")));
		}		catch (Exception e)  { e.printStackTrace(origErr);  	}
		return origErr;
	}
	
}
