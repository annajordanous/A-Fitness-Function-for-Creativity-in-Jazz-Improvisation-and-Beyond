package creativeJazzGA;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Random;

import jm.music.data.Score;

import org.jgap.*;
import org.jgap.impl.*;

import creativeJazzGA.genes.KeyGene;
import creativeJazzGA.genes.LowestNoteGene;
import creativeJazzGA.genes.MaxNumNotesGene;
import creativeJazzGA.genes.NoteRangeGene;
import creativeJazzGA.genes.NoteRestRatioGene;
import creativeJazzGA.genes.NumImprovisationsGene;
import creativeJazzGA.genes.PolyphonyGene;
import creativeJazzGA.genes.RhythmMultiplierGene;
import creativeJazzGA.genes.medianTempoGene;
import creativeJazzGA.genes.tempoVarianceGene;

/**
 * @author Anna Jordanous
 * @version Summer 2010
 *
 */
public class EvolveImprovisers extends Thread {

	static Random generator = new Random();

	static Configuration conf;
	static Genotype population;

	private static final int MAX_POLYPHONY = 20; // 20 arbitrarily chosen. polyphony -
											// no. of parts
	private static final int MAX_NUM_NOTES = 200; // 200 arbitrarily chosen num notes to
											// generate
	private static final int MAX_KEY = 11; // key - pitch class // 0 = C, 1 = C#, up to
									// 11 = B
	private static final int MAX_NOTE_RANGE = 128; // note range in MIDI // max possible
											// is 128 i.e. 0-127
	private static final int MAX_LOWEST_NOTE = 127; // bottom note in MIDI nos // max
											// possible is 127 (highest MIDI
											// note)
	private static final int MAX_RHYTHM_MULT = 10; // affects the longest note lengths
	private static final int MAX_NOTE_RATIO = 100; // determines what the proportion of
											// notes to rests is (max 100%
											// notes)
	private static final int MAX_NUM_IMPROS = 10; // no of Improvisations this
											// improviser will do
	private static final int MAX_MEDIAN_TEMPO = 240; // 1 - 60 - 90 - 120 - 150 - 180 -
												// 240 chosen for musicality
	private static final int MAX_TEMPO_VAR = MAX_MEDIAN_TEMPO; // amount tempo can vary up
														// or down - chosen to
														// be as unrestrictive
														// as poss

	private static double popMeanPolyphony, popMeanNumNotes, popMeanKey, popMeanNoteRange, popMeanLowestNote, popMeanRhythmMult, popMeanNoteRatio, popMeanMedianTempo, popMeanTempoVar;
	

	private static BufferedReader in = new BufferedReader(
			new InputStreamReader(System.in));

	/**
	 * Set up GA parameters: 
	 * 	Default configuration, 
	 *  fitness function as determined by ImproviserCreativityFitnessFunction,
	 *  population Size as set by the parameter,
	 *  a sample Chromosome to be used as templates for all Chromosomes in the GA that contains 10 genes:
	 * 		PolyphonyGene, MaxNumNotesGene, KeyGene, NoteRangeGene, LowestNoteGene, RhythmMultiplierGene, 
	 * 		NoteRestRatioGene, NumImprovisationsGene, medianTempoGene, maxTempoGene
	 * NB all genes are given an appropriate range of values, as set in the global variables for this class
	 * 
	 * @param populationSize the population Size for the GA
	 * @return a default [org.jgap.]Configuration: set of parameters for a jGap Genetic Algorithm
	 * @throws InvalidConfigurationException
	 * 
	 */
	private static Configuration setUpGAParameters(int populationSize)
			throws InvalidConfigurationException {
		Configuration conf = new DefaultConfiguration();

		FitnessFunction myFunc = new ImproviserCreativityFitnessFunction();
		conf.setFitnessFunction(myFunc);

		conf.setPopulationSize(populationSize);

		Gene[] sampleGenes = new Gene[10];
		sampleGenes[0] = new PolyphonyGene(conf, 1, MAX_POLYPHONY);
		sampleGenes[1] = new MaxNumNotesGene(conf, 1, MAX_NUM_NOTES);
		sampleGenes[2] = new KeyGene(conf, 0, MAX_KEY);
		sampleGenes[3] = new NoteRangeGene(conf, 1, MAX_NOTE_RANGE);
		sampleGenes[4] = new LowestNoteGene(conf, 0, MAX_LOWEST_NOTE);
		sampleGenes[5] = new RhythmMultiplierGene(conf, 1, MAX_RHYTHM_MULT);
		sampleGenes[6] = new NoteRestRatioGene(conf, 0, MAX_NOTE_RATIO);
		sampleGenes[7] = new NumImprovisationsGene(conf, 1, MAX_NUM_IMPROS);
		sampleGenes[8] = new medianTempoGene(conf, 1, MAX_MEDIAN_TEMPO);
		sampleGenes[9] = new tempoVarianceGene(conf, 0, MAX_TEMPO_VAR);
		IChromosome sampleChromosome = new Chromosome(conf, sampleGenes); 
		conf.setSampleChromosome(sampleChromosome);
		return conf;
	}

	/**
	 * @return true if the user wants the evolution process to repeat for another generation, false otherwise
	 */
	private static boolean evolveNextGeneration() {
		boolean keepEvolving;
		// Evolve a new generation of the population
		population.evolve();
		recordMeanGenesForPopulation();
		
		// update the log file to mark that the end of a generation has been reached
		System.err.println("END OF GENERATION");
		System.err.println(population.toString());
		System.err
				.println("==========================================================================");
	
		// display fitness results on console
		System.out.println("Fitness of most creative Improviser = "
				+ population.getFittestChromosome()
						.getFitnessValueDirectly());
	
		// check to see if the user wants the evolution process to repeat for another generation
		keepEvolving = checkAboutEvolvingAgain();
		return keepEvolving;
	}

	//TODO DO THIS quick hack, need to actually write method, probably as part of initialising population
	private static void recordMeanGenesForPopulation() { 
		popMeanPolyphony =  MAX_POLYPHONY/2/0;
		popMeanNumNotes = MAX_NUM_NOTES/2.0;
		popMeanKey = MAX_KEY/2.0;
		popMeanNoteRange =  MAX_NOTE_RANGE/2.0;
		popMeanLowestNote = MAX_LOWEST_NOTE/2.0;
		popMeanRhythmMult = MAX_RHYTHM_MULT/2.0;
		popMeanNoteRatio = MAX_NOTE_RATIO/2.0;
		popMeanMedianTempo = MAX_MEDIAN_TEMPO/2.0;
		popMeanTempoVar = MAX_TEMPO_VAR/2.0;

	}

	/**
	 * Performs a check to see if a new generation should be evolved, 
	 * by asking the user and acting on their input
	 * 
	 * @return true if the user indicates a new generation should be evolved, false otherwise
	 */
	private static boolean checkAboutEvolvingAgain() {
		boolean keepEvolving = true;
		boolean askForDecision = true;
		String answer = "";
		while (askForDecision) {
			System.out.print("Evolve a new generation again? (y/n): ");
			try {
				answer = in.readLine();
			} catch (Exception e) {
			}
			if (answer.equalsIgnoreCase("y")) { // evolve another generation
				askForDecision = false;
			} else if (answer.equalsIgnoreCase("n")) { // stop evolving and quit
				askForDecision = false;
				keepEvolving = false;
			}
		}
		return keepEvolving;
	}

	/**
	 * Runs after the final evolution, 
	 * to report the details of the fittest individual in the current population.
	 * Details are printed to System.out
	 */
	private static void generateFinalResults() {
		IChromosome chromosome = population.getFittestChromosome();
		Integer polyphony = getPolyphonyOf(chromosome);
		Integer maxNotesToGenerate = getMaxNotesOf(chromosome);
		Integer key = getKeyOf(chromosome);
		Integer noteRange = getNoteRangeOf(chromosome);
		Integer lowestNote = getLowestNoteOf(chromosome);
		Integer rhythmMult = getRhythmMultiplierOf(chromosome);
		Integer noteRestRatio = getNoteRestRatioOf(chromosome);
		Integer noOfImprovisations = getNumImprovisationsOf(chromosome);
		Integer medianTempo = getMedianTempoOf(chromosome);
		Integer tempoVariance = getTempoVarianceOf(chromosome);

		System.out.println("Most Creative Improviser Parameters - Fitness="
				+ chromosome.getFitnessValueDirectly());
		System.out.println(" - Polyphony:   " + polyphony);
		System.out.println(" - MaxNumNotes: " + maxNotesToGenerate);
		System.out.println(" - Key:         " + key);
		System.out.println(" - Note Range:  " + noteRange);
		System.out.println(" - Lowest Note: " + lowestNote);
		System.out.println(" - Note Length Multiplier: " + rhythmMult);
		System.out.println(" - Note not Rest Percentage: " + noteRestRatio);
		System.out.println(" - NumImprovisations: " + noOfImprovisations);
		System.out.println(" - Median Tempo Marking: " + medianTempo);
		System.out.println(" - Tempo variance range (+/-): " + tempoVariance);
		System.out.println();

		// Log final results
		System.err.println("Most creative improviser: "
				+ toString(population.getFittestChromosome()));

		generateFinalRepresentativeImpro(chromosome, polyphony,
				maxNotesToGenerate, key, noteRange, lowestNote, rhythmMult,
				noteRestRatio, medianTempo, tempoVariance);
	}

	/**
	 * Generates a representative sample of the improvisations by an individual in the population
	 * 
	 * @param chromosome chromosome representing the individual in the population
	 * @param polyphony the number of voices in the improv sample
	 * @param maxNotesToGenerate the max number of notes to be generated in the improv sample
	 * @param key the key of the improv sample
	 * @param noteRange the range of notes to be used in the improv sample (as no. of notes above the lowestNote)
	 * @param lowestNote the lowest note that could be used in the improv sample (as midi value)
	 * @param rhythmMult divisive factor representing the length of the shortest notes possible in the improv sample
	 * @param noteRestRatio ratio of notes to rests in the improv sample
	 * @param medianTempo the tempo of the improv sample (which may be affected by the tempoVariance parameter)
	 * @param tempoVariance the amount by which the medianTempo can be varied as the tempo for the improv sample
	 */
	private static void generateFinalRepresentativeImpro(
			IChromosome chromosome, Integer polyphony,
			Integer maxNotesToGenerate, Integer key, Integer noteRange,
			Integer lowestNote, Integer rhythmMult, Integer noteRestRatio,
			Integer medianTempo, Integer tempoVariance) {
		Score sampleScore = JazzGenerator.generate(polyphony,
				maxNotesToGenerate, key, noteRange, lowestNote, rhythmMult,
				noteRestRatio, medianTempo, tempoVariance);
		String filename = toString(chromosome) + ".mid";
		Improvisations.savePopulationMember(sampleScore, filename);
		System.out.println("Sample improvisation saved in " + filename);
		long pieceDuration = Math.round(sampleScore.getEndTime());
		System.out.println("Duration (seconds) = " + pieceDuration);
		System.out
				.print("Would you like to play this sample before exiting the program? (y/n): ");
		String answer = "";
		try {
			answer = in.readLine();
		} catch (Exception e) {
		}
		if (answer.equalsIgnoreCase("y")) {
			Improvisations.playMidiFile(filename, sampleScore);
			try {
				sleep(pieceDuration * 1000);
			} catch (Exception e) {
			}
		}
		Improvisations.stopPlayback();
		System.out.println("Program Finished");
	}

	/**
	 * Returns a String representation of a given chromosome
	 * @param chromosome The IChromosome to be represented as a String
	 * @return String representation of the chromosome
	 */
	static String toString(IChromosome chromosome) {
		String filename = "poly" + getPolyphonyOf(chromosome) + "max"
				+ getMaxNotesOf(chromosome) + "key" + getKeyOf(chromosome)
				+ "range" + getNoteRangeOf(chromosome) + "from"
				+ getLowestNoteOf(chromosome) + "rhythm"
				+ getRhythmMultiplierOf(chromosome) + "noteRest"
				+ getNoteRestRatioOf(chromosome) + "impros"
				+ getNumImprovisationsOf(chromosome) + "tempo"
				+ getMedianTempoOf(chromosome) + "vary"
				+ getTempoVarianceOf(chromosome);
		return filename;
	}

	/**
	 * @param chromosome The IChromosome being queried for details
	 * @return polyphony (number of parts) for that Improvisor chromosome 
	 */
	static Integer getPolyphonyOf(IChromosome chromosome) {
		Integer polyphony = (Integer) chromosome.getGene(0).getAllele();
		return polyphony;
	}

	/**
	 * @param chromosome The IChromosome being queried for details
	 * @return maxNotesToGenerate number of notes this Improvisor chromosome will generate

	 */
	static Integer getMaxNotesOf(IChromosome chromosome) {
		Integer maxNotesToGenerate = (Integer) chromosome.getGene(1)
				.getAllele();
		return maxNotesToGenerate;
	}

	/**
	 * @param chromosome The IChromosome being queried for details
	 * @return key an integer indicating the key (pitch class) this Improvisor will work in: 0=C, 1=C#, up to 11=B 
	 */
	static Integer getKeyOf(IChromosome chromosome) {
		Integer key = (Integer) chromosome.getGene(2).getAllele();
		return key;
	}

	/**
	 * @param chromosome The IChromosome being queried for details
	 * @return noteRange The note range (in MIDI, 0-127) that the Improvisor will be restricted to
	 */
	static Integer getNoteRangeOf(IChromosome chromosome) {
		Integer noteRange = (Integer) chromosome.getGene(3).getAllele();
		return noteRange;
	}

	/**
	 * @param chromosome The IChromosome being queried for details
	 * @return lowestNote The lowest note in MIDI (0-127) this Improvisor chromosome can use
	 */
	static Integer getLowestNoteOf(IChromosome chromosome) {
		Integer lowestNote = (Integer) chromosome.getGene(4).getAllele();
		return lowestNote;
	}

	/**
	 * @param chromosome The IChromosome being queried for details
	 * @return rhythmMult A representation of the longest note lengths an Improvisor chromosome can use
	 */
	static Integer getRhythmMultiplierOf(IChromosome chromosome) {
		Integer key = (Integer) chromosome.getGene(5).getAllele();
		return key;
	}

	/**
	 * @param chromosome The IChromosome being queried for details
	 * @return noteRatio A determiner of what the proportion of notes to rests is (min 0% max 100% notes)
	 */
	static Integer getNoteRestRatioOf(IChromosome chromosome) {
		Integer noteRatio = (Integer) chromosome.getGene(6).getAllele();
		return noteRatio;
	}

	/**
	 * @param chromosome The IChromosome being queried for details
	 * @return noOfImprovisations the number of Improvisations this improviser will do
	 */
	static Integer getNumImprovisationsOf(IChromosome chromosome) {
		Integer noOfImprovisations = (Integer) chromosome.getGene(7)
				.getAllele();
		return noOfImprovisations;
	}

	/**
	 * @param chromosome The IChromosome being queried for details
	 * @return medianTempo The average (median) tempo the Improvisor will generate improvisations with (1-240)
	 */
	static Integer getMedianTempoOf(IChromosome chromosome) {
		Integer medianTempo = (Integer) chromosome.getGene(8).getAllele();
		return medianTempo;
	}

	/**
	 * @param chromosome The IChromosome being queried for details
	 * @return the amount by which the improvisation tempo can vary (the tempoVariance variable)
	 */
	static Integer getTempoVarianceOf(IChromosome chromosome) {
		Integer tempoVariance = (Integer) chromosome.getGene(9).getAllele();
		return tempoVariance;
	}

	/**
	 * @return
	 */
	static int getMaxPolyphony() {
		return MAX_POLYPHONY;
	}

	/**
	 * @return
	 */
	static int getMaxNumNotes() {
		return MAX_NUM_NOTES;
	}

	/**
	 * @return
	 */
	static int getMaxKey() {
		return MAX_KEY;
	}

	/**
	 * @return
	 */
	static int getMaxNoteRange() {
		return MAX_NOTE_RANGE;
	}

	/**
	 * @return
	 */
	static int getMaxLowestNote() {
		return MAX_LOWEST_NOTE;
	}

	/**
	 * @return
	 */
	static int getMaxRhythmMult() {
		return MAX_RHYTHM_MULT;
	}

	/**
	 * @return
	 */
	static int getMaxNoteRatio() {
		return MAX_NOTE_RATIO;
	}

	/**
	 * @return
	 */
	static int getMaxNumImpros() {
		return MAX_NUM_IMPROS;
	}

	/**
	 * @return
	 */
	static int getMaxMedianTempo() {
		return MAX_MEDIAN_TEMPO;
	}

	/**
	 * @return
	 */
	static int getMaxTempoVar() {
		return MAX_TEMPO_VAR;
	}


	private static double meanPopulationAverage(String geneLabel)  throws Exception{
			switch(geneLabel) {
				case "POLYPHONY" : return popMeanPolyphony;
				case "NUM_NOTES" : return popMeanNumNotes;
				case "KEY" : return popMeanKey;
				case "NOTE_RANGE" : return popMeanNoteRange;
				case"LOWEST_NOTE": return popMeanLowestNote;
				case "RHYTHM_MULT": return popMeanRhythmMult;
				case "NOTE_RATIO": return popMeanNoteRatio;
				case "MEDIAN_TEMPO": return popMeanMedianTempo;
				case "TEMPO_VAR": return popMeanTempoVar;
				}
			// if none of these cases, we have an error - probably need to deal with this better in code with Exceptions
			throw new Exception("issue in meanPopulation");
		}

	private static int maxPopulation(String geneLabel)  throws Exception{
		switch(geneLabel) {
			case "POLYPHONY" : return MAX_POLYPHONY;
			case "NUM_NOTES" : return MAX_NUM_NOTES;
			case "KEY" : return MAX_KEY;
			case "NOTE_RANGE" : return MAX_NOTE_RANGE;
			case"LOWEST_NOTE": return MAX_LOWEST_NOTE;
			case "RHYTHM_MULT": return MAX_RHYTHM_MULT;
			case "NOTE_RATIO": return MAX_NOTE_RATIO;
			case "MEDIAN_TEMPO": return MAX_MEDIAN_TEMPO;
			case "TEMPO_VAR": return MAX_TEMPO_VAR;
			}
		// if none of these cases, we have an error - probably need to deal with this better in code with Exceptions
		throw new Exception("issue in maxPopulation");
	}
/** 
	/EvolveImprovisers.compareMemberToAverage(improParameters[0], "POLYPHONY");
	noveltyVal += EvolveImprovisers.compareMemberToAverage(improParameters[1], "NUM_NOTES");
	noveltyVal += EvolveImprovisers.compareMemberToAverage(improParameters[2], "KEY");
	noveltyVal += EvolveImprovisers.compareMemberToAverage(improParameters[3], "NOTE_RANGE");
	noveltyVal += EvolveImprovisers.compareMemberToAverage(improParameters[4], "LOWEST_NOTE");
	noveltyVal += EvolveImprovisers.compareMemberToAverage(improParameters[5], "RHYTHM_MULT");
	noveltyVal += EvolveImprovisers.compareMemberToAverage(improParameters[6], "NOTE_RATIO");
	noveltyVal += EvolveImprovisers.compareMemberToAverage(improParameters[7], "MEDIAN_TEMPO");
	noveltyVal += EvolveImprovisers.compareMemberToAverage(improParameters[8], "TEMPO_VAR");
	*/
	public static double compareMemberToAverage(int i, String geneLabel) {
		double returnValue = 1.0;
		try {
			returnValue = (i- meanPopulationAverage(geneLabel))/maxPopulation(geneLabel);
		} catch(Exception e)  {	System.err.println(" geneLabel problem causing "+e.getMessage());  }
		return returnValue;

	}


	/**
	 * Starts off the Evolving of Improvisors and initialises parameters during
	 * the program
	 * 
	 * @param args
	 *            The first argument to the main method specifies the size of
	 *            the population being evolved, so e.g. EvolveImprovisors 10
	 *            would evolve a population of 10 composers
	 */
	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		// Before we start:
		// Divert standard error to log file, for saving results of program runtime
		PrintStream originalStandardError = ImproviserCreativityFitnessFunction.divertStandardError();

		int noOfImprovisers = Integer.valueOf(args[0]);
		
		// Set up GA parameters - structure and permissable ranges of
		// chromosomes, fitness function, etc
		conf = setUpGAParameters(noOfImprovisers);

		// Initialise new population at random
		population = Genotype.randomInitialGenotype(conf);

		// Keep evolving until the user chooses to stop the evolution process
		boolean keepEvolving = true;
		while (keepEvolving) {
			keepEvolving = evolveNextGeneration();
		}
		// display the parameters of the fittest population member in the most recent generation
		generateFinalResults();

		
		
		// Now program has finished running:
		// Restore standard error display settings
		System.setErr(originalStandardError);
	}


}