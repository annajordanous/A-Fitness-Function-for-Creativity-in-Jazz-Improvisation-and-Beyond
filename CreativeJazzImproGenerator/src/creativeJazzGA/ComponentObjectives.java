package creativeJazzGA;

import jm.music.data.Score;

/**
 * @author Anna Jordanous
 * 
 * Basic implementation of multiple objectives for creative musical improvisation *
 */

public class ComponentObjectives   {
	
	//Weightings for weighted sum of components, from Jordanous & Keller 2012 what makes a creative musical improvisation (JIMS)
	final static double WEIGHTING_SCI = 1.0;
	final static double WEIGHTING_DC = 1.0;
	final static double WEIGHTING_IEI = 1.0;

	
	static double calculateWeightedSum(Improvisations imp) {
		// calculate values for each of Ritchie's 18 criteria and return them in a single fitness function rating between 0 and 1
			
			// calculate the population size once now rather than every time it is needed in criteria (as popSize doesn't change)
			double weightedFitness = (calcSocialCommunicationAndInteractionComponent(imp)
							 + calcDomainCompetenceComponent(imp)
							 + calcIntentionAndEmotionalInvolvementComponent(imp))
							  / 3.0;// Normalise for 3 objectives
		    System.err.println("Criteria Fitness = "+weightedFitness);
			return weightedFitness;
	}
	

	
	private static double calcSocialCommunicationAndInteractionComponent(Improvisations imp) {
		return (imp.getSocialCommunicationAndInteractionRating()*WEIGHTING_SCI);
	}

	private static double calcDomainCompetenceComponent(Improvisations imp) {
		return (imp.getDomainCompetenceRating()*WEIGHTING_DC);
	}

	private static double calcIntentionAndEmotionalInvolvementComponent(Improvisations imp) {
		return (imp.getIntentionAndEmotionalInvolvementRating()*WEIGHTING_IEI);
	}



    public static double ratePopulationMemberForSCI(int[] improParameters) {
		double noveltyVal  = EvolveImprovisers.compareMemberToAverage(improParameters[0], "POLYPHONY");
		noveltyVal += EvolveImprovisers.compareMemberToAverage(improParameters[1], "NUM_NOTES");
		noveltyVal += EvolveImprovisers.compareMemberToAverage(improParameters[2], "KEY");
		noveltyVal += EvolveImprovisers.compareMemberToAverage(improParameters[3], "NOTE_RANGE");
		noveltyVal += EvolveImprovisers.compareMemberToAverage(improParameters[4], "LOWEST_NOTE");
		noveltyVal += EvolveImprovisers.compareMemberToAverage(improParameters[5], "RHYTHM_MULT");
		noveltyVal += EvolveImprovisers.compareMemberToAverage(improParameters[6], "NOTE_RATIO");
		noveltyVal += EvolveImprovisers.compareMemberToAverage(improParameters[7], "MEDIAN_TEMPO");
		noveltyVal += EvolveImprovisers.compareMemberToAverage(improParameters[8], "TEMPO_VAR");
		noveltyVal = noveltyVal / 9.0;  // normalise over 9 parameters
		return (wundtCurveFunction(noveltyVal));
    }

	/** Comments from Max
	 * 
	 * "About the Wundt Curve, I haven’t seen a specific definition by anyone, even Rob’s papers keep it somewhat vague. But the approach is simple, you are adding two logistic functions, reward and punish, to get the hedonic function.
	 * 	 Sigmoid (quick): https://www.desmos.com/calculator/az3jdvngwo
	 *   CDF (very slow): https://www.desmos.com/calculator/s8nxtwgqbo
	 *  
	 * I’ve added some variables because the default sigmoid function didn’t really work for my application. 
	 * Basically, n1 and n2 are thresholds for functions r and p. Should both be > 5 if x > 0 and of course n1 < n2. 
	 * S is a scaling variable I added, maybe not necessary but makes the function easier to use. (Also see 4.4: https://cs.gmu.edu/~jgero/publications/2001/01SaundersGeroAISB.pdf)
	 * 
	 * I think the original curve by Berlyne is kind of hard to model, it is just a weird shape. I think you would need to use multiple functions with specific limits, but the functions above should do the trick."
	 * 
	 * 
	 * 
	 * 
	 * AND 
	 * 
	 * 
	 *  see 4.4: https://cs.gmu.edu/~jgero/publications/2001/01SaundersGeroAISB.pdf)
	 * "The most important feature of the hedonic function used in this research that it shares in common with the Wundt curve is that 
	 * it is the sum of two non-linear functions. In our model the hedonic function is calculated as the sum of two sigmoidal functions 
	 * whereas the Wundt curve is calculated as the sum of cumulative-Gaussian functions. In either case the functions are summed to 
	 * produce an inverted ‘U’ shaped curve, as sketched in Figure 6.
	 * 
	 *  The sigmoidal function labelled ‘Reward’ represents the intrinsic reward given to the agent for finding an arousal-inducing stimulus over a fairly low threshold, n1. 
	 * The second function is a punishment for finding an arousal- inducing stimulus over a higher threshold, n2.
	 * 
	 * The agents use the above hedonic function to calculate the level of interest that they have in a particular artwork 
	 * based upon the novelty detected by the self-organising map. 
	 * Figure 6 illustrates the use of the hedonic curve with an example novelty value Nx that is used to calculate its corresponding hedonic value Hx. 
	 * The preferred degree of novelty for an agent is determined by the posi- tion of the peak on the hedonic curve along the novelty axis. 
	 * By altering the thresholds for the reward and punishment sigmoid curves this peak can be positioned anywhere along the novelty axis."
	 * 
	 */



	private static double wundtCurveFunction(double noveltyVal)   {
		return rewardFunction(noveltyVal) + punishmentFunction(noveltyVal);
	}

	/** Simple sigmoidal (from Max) 
	 * r\left(x\right)=				1\cdot\ \left(\frac{1}{1+e^{-Sx+n_{1}}}\right)
	*/
	private static double rewardFunction(double noveltyVal)  {
			
		return logisticSigmoid(noveltyVal, 1.0, 1.0, 0.5);
	}

	/** Simple sigmoidal (from Max) 
	 * p\left(x\right)=				-1.1\cdot\left(\frac{1}{1+e^{-Sx+n_{2}}}\right)  )
	*/
	private static double punishmentFunction(double noveltyVal)  {

		return (-1 * logisticSigmoid(noveltyVal, 1.0, 1.0, 0.75));  // TODO this is  wrong
	}

	private static double logisticSigmoid(double noveltyVal, double L, double k, double x0)  {
		// e exponent = -k ( x - x0)
		double e_exponent = -1.0 * k * (noveltyVal - x0);
		// L = max value
		return (L / (L + Math.exp(e_exponent))) ; 
	}



	public static double ratePopulationMemberForDC(Score popMember) {
        return Math.random();
    }

	public static double ratePopulationMemberForIEI(Score popMember) {
        return Math.random();
    }
}
