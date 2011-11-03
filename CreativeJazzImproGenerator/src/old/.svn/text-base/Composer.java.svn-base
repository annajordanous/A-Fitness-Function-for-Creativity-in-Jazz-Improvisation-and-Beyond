package old;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import creativeJazzGA.Improvisations;

public class Composer extends Thread {
	
	private static Improvisations pop;
	private int popIndex;
	private int popSize;
	private int polyphony;
	private int maxNotesToGenerate;
	private int key;
	private int noteRange;
	private int lowestNote;
	
	private static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

	public Composer(int populationIndex, int populationSize, int numParts, int maxNumNotes, int pitchClass, int rangeOfNotes, int bassNote)  {
		popIndex 		   = populationIndex;
		popSize            = populationSize;
		polyphony 		   = numParts;
		maxNotesToGenerate = maxNumNotes;
		key 			   = pitchClass;
		noteRange 		   = rangeOfNotes;
		lowestNote 		   = bassNote; 
	}

	int getPopIndex()  {
		return popIndex;
	}
	
	int getPopSize()  {
		return popSize;
	}
	
	int getPolyphony()  {
		return polyphony;
	}
	
	int getMaxNotesToGenerate()  {
		return maxNotesToGenerate;
	}
	
	int getKey()  {
		return key;
	}
	
	int getNoteRange()  {
		return noteRange;
	}
	
	int getLowestNote()  {
		return lowestNote;
	}
	
	private static boolean checkAboutEvolvingAgain() {
		boolean keepEvolving = true;
		boolean askForDecision = true;
		String answer = "";		
		while(askForDecision)   {
			System.out.print("Evolve a new generation again? (y/n): ");
			try			{
				answer = in.readLine(); 
			}
			catch(Exception e){}	
			if (answer.equalsIgnoreCase("y"))  {   // evolve another generation
				askForDecision = false;
			}
			else if (answer.equalsIgnoreCase("n"))  {  // stop evolving and quit
				askForDecision = false;
				keepEvolving   = false;
			}
		}
		return keepEvolving;
	}
	
	private static void evolution() {
		boolean keepEvolving = true;
		while (keepEvolving)  {	
			evolveNextGeneration(null); // 
			keepEvolving = checkAboutEvolvingAgain();
		}
	}

	private static double evolveNextGeneration(Improvisations composerOutput)  { 
/*		composerOutput.selection();
		composerOutput.heredity();
		composerOutput.variability();
		double criteriaFitness = ritchiesCriteria.calculateRitchieCriteria(composerOutput);
		//composerOutput.tidyUpPopulation();
		return criteriaFitness;*/
		return 0.5;
	}


	public void run()  {
		pop = new Improvisations(popSize, null, true); 
		evolution();
		// pop.savePopulation();
		System.out.println("Newest set of music has been saved as MIDI files. Composer "+pop.toString()+" finished.");
	}


}
