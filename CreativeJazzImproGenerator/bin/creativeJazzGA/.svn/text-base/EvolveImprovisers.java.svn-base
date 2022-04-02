package creativeJazzGA;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Random;

import jm.music.data.Score;

import org.jgap.*;
import org.jgap.impl.*;

/**
 * @author Anna Jordanous
 * @version Summer 2010
 *
 */
public class EvolveImprovisers extends Thread {

	static Random generator = new Random();

	static Configuration conf;
	static Genotype population;

	private static int maxPolyphony = 20; // 20 arbitrarily chosen. polyphony -
											// no. of parts
	private static int maxNumNotes = 200; // 200 arbitrarily chosen num notes to
											// generate
	private static int maxKey = 11; // key - pitch class // 0 = C, 1 = C#, up to
									// 11 = B
	private static int maxNoteRange = 128; // note range in MIDI // max possible
											// is 128 i.e. 0-127
	private static int maxLowestNote = 127; // bottom note in MIDI nos // max
											// possible is 127 (highest MIDI
											// note)
	private static int maxRhythmMult = 10; // affects the longest note lengths
	private static int maxNoteRatio = 100; // determines what the proportion of
											// notes to rests is (max 100%
											// notes)
	private static int maxNumImpros = 10; // no of Improvisations this
											// improviser will do
	private static int maxMedianTempo = 240; // 1 - 60 - 90 - 120 - 150 - 180 -
												// 240 chosen for musicality
	private static int maxTempoVar = maxMedianTempo; // amount tempo can vary up
														// or down - chosen to
														// be as unrestrictive
														// as poss

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
		sampleGenes[0] = new PolyphonyGene(conf, 1, maxPolyphony);
		sampleGenes[1] = new MaxNumNotesGene(conf, 1, maxNumNotes);
		sampleGenes[2] = new KeyGene(conf, 0, maxKey);
		sampleGenes[3] = new NoteRangeGene(conf, 1, maxNoteRange);
		sampleGenes[4] = new LowestNoteGene(conf, 0, maxLowestNote);
		sampleGenes[5] = new RhythmMultiplierGene(conf, 1, maxRhythmMult);
		sampleGenes[6] = new NoteRestRatioGene(conf, 0, maxNoteRatio);
		sampleGenes[7] = new NumImprovisationsGene(conf, 1, maxNumImpros);
		sampleGenes[8] = new medianTempoGene(conf, 1, maxMedianTempo);
		sampleGenes[9] = new tempoVarianceGene(conf, 0, maxTempoVar);
		IChromosome sampleChromosome = new Chromosome(conf, sampleGenes); // not
																			// sure
																			// why
																			// its
																			// IChromosome
																			// rather
																			// than
																			// Chromosome
		conf.setSampleChromosome(sampleChromosome);
		return conf;
	}

	/**
	 * @return
	 */
	private static boolean evolveNextGeneration() {
		boolean keepEvolving;
		// Evolve a new generation of the population
		population.evolve();
		
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
	 * @return
	 */
	static Integer getPolyphonyOf(IChromosome chromosome) {
		Integer polyphony = (Integer) chromosome.getGene(0).getAllele();
		return polyphony;
	}

	/**
	 * @param chromosome The IChromosome being queried for details
	 * @return
	 */
	static Integer getMaxNotesOf(IChromosome chromosome) {
		Integer maxNotesToGenerate = (Integer) chromosome.getGene(1)
				.getAllele();
		return maxNotesToGenerate;
	}

	/**
	 * @param chromosome The IChromosome being queried for details
	 * @return
	 */
	static Integer getKeyOf(IChromosome chromosome) {
		Integer key = (Integer) chromosome.getGene(2).getAllele();
		return key;
	}

	/**
	 * @param chromosome The IChromosome being queried for details
	 * @return
	 */
	static Integer getNoteRangeOf(IChromosome chromosome) {
		Integer noteRange = (Integer) chromosome.getGene(3).getAllele();
		return noteRange;
	}

	/**
	 * @param chromosome The IChromosome being queried for details
	 * @return
	 */
	static Integer getLowestNoteOf(IChromosome chromosome) {
		Integer lowestNote = (Integer) chromosome.getGene(4).getAllele();
		return lowestNote;
	}

	/**
	 * @param chromosome The IChromosome being queried for details
	 * @return
	 */
	static Integer getRhythmMultiplierOf(IChromosome chromosome) {
		Integer key = (Integer) chromosome.getGene(5).getAllele();
		return key;
	}

	/**
	 * @param chromosome The IChromosome being queried for details
	 * @return
	 */
	static Integer getNoteRestRatioOf(IChromosome chromosome) {
		Integer key = (Integer) chromosome.getGene(6).getAllele();
		return key;
	}

	/**
	 * @param chromosome The IChromosome being queried for details
	 * @return
	 */
	static Integer getNumImprovisationsOf(IChromosome chromosome) {
		Integer noOfImprovisations = (Integer) chromosome.getGene(7)
				.getAllele();
		return noOfImprovisations;
	}

	/**
	 * @param chromosome The IChromosome being queried for details
	 * @return
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
		return maxPolyphony;
	}

	/**
	 * @return
	 */
	static int getMaxNumNotes() {
		return maxNumNotes;
	}

	/**
	 * @return
	 */
	static int getMaxKey() {
		return maxKey;
	}

	/**
	 * @return
	 */
	static int getMaxNoteRange() {
		return maxNoteRange;
	}

	/**
	 * @return
	 */
	static int getMaxLowestNote() {
		return maxLowestNote;
	}

	/**
	 * @return
	 */
	static int getMaxRhythmMult() {
		return maxRhythmMult;
	}

	/**
	 * @return
	 */
	static int getMaxNoteRatio() {
		return maxNoteRatio;
	}

	/**
	 * @return
	 */
	static int getMaxNumImpros() {
		return maxNumImpros;
	}

	/**
	 * @return
	 */
	static int getMaxMedianTempo() {
		return maxMedianTempo;
	}

	/**
	 * @return
	 */
	static int getMaxTempoVar() {
		return maxTempoVar;
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
		// Divert standard error to log file, for saving results of program
		// runtime
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

		// Restore standard error display settings
		System.setErr(originalStandardError);
	}
}