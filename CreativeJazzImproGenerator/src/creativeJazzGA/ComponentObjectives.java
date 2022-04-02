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



    public static double ratePopulationMemberForSCI(Score popMember) {
        return Math.random();
    }

	public static double ratePopulationMemberForDC(Score popMember) {
        return Math.random();
    }

	public static double ratePopulationMemberForIEI(Score popMember) {
        return Math.random();
    }
}
