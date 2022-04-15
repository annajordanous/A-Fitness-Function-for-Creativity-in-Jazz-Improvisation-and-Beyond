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

	private static double wundtCurveFunction(double noveltyVal)   {
		return rewardFunction(noveltyVal) + punishmentFunction(noveltyVal);
	}

	private static double rewardFunction(double noveltyVal)  {
			
		return logisticSigmoid(noveltyVal, 1.0, 1.0, 0.5);
	}

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
