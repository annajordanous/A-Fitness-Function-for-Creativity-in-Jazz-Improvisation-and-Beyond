package creativeJazzGA;

/**
 * @author Anna Jordanous
 * 
 * Based on Graeme Ritchie's criteria for creativity in examining the products of creative systems
 * 
 * See:
 * 
 * 	G. Ritchie. Some empirical criteria for attributing creativity to a computer program. Minds and Machines, 17:67Ð99, 2007.
 * 
 *  G. Ritchie. Assessing creativity. In Proceedings of the AISB Symposium on AI and Creativity in Arts and Science, York, UK, 2001.
 *
 */

public class RitchieCriteria   {
	
	private static boolean[] criteria = new boolean[18];
	/* weightings of criteria:
	 * 	1.0:  Very important to creativity judgement in this domain of jazz improvisation
	 *  0.0: Totally irrelevant or null and invalid criteria (e.g. if no inspiring set, criteria 9 (and others) rated 0.0
	 *  NB This is a real area of contention if I choose to vary the weights of these in any granularity...
	 *   - can the computer determine (without human assistance) the key components of creativity from these criteria?
	 *   - are they all necessary? 
	 *   - are they just rejiggings of what is essentially the human ratings (hence no automation)? 
	 */
	private static double[] weightings = 
	   {1.0, // 1:  meaning = Average typicality                            
		1.0, // 2:  meaning = #Typical results / #results                   
		1.0, // 3:  meaning = Average quality                               
		1.0, // 4:  meaning = #Good results / #results                      
		1.0, // 5:  meaning = #Good typical results / #typical results      
		1.0, // 6:  meaning = #Good atypical results / #results             
		1.0, // 7:  meaning = #Good atypical results / #atypical results	   
		1.0, // 8a: meaning = #Good atypical results / #good typical results
		0.0, // 9:  meaning = #Results in inspiring set / #inspiring set	   NB if no inspiring set this = 1
		0.0, // 10a:meaning = 1 - (#Results in inspiring set / #results)	   NB if no inspiring set this = 0   
		0.0, // 11: meaning = Average typicality of new results            	   NB if no inspiring set this = criteria 1
		0.0, // 12: meaning = Average quality of new results               	   NB if no inspiring set this = criteria 3
		0.0, // 13: meaning = #Typical new results / #results            	   NB if no inspiring set this = criteria 2          
		0.0, // 14: meaning = #Good new results / #results                     NB if no inspiring set this = criteria 4
		0.0, // 15: meaning = #Typical new results / #new results          	   NB if no inspiring set this = criteria 2 (and 13)
		0.0, // 16: meaning = #Good new results / #new results            	   NB if no inspiring set this = criteria 4 (and 14)                                
		1.0, // 17: meaning = #Good typical new results/ #new results (creativity within existing norms) NB even if no inspiring set this is still new information
		0.0};// 18: meaning = #Good atypical new results/ #new results (some original deviation from past practice) NB if no inspiring set this = criteria 6


	private static int popSize;
	private static int popSizeMinusI;
	
	static double calculateRitchieCriteria(Improvisations pop) {
		// calculate values for each of Ritchie's 18 criteria and return them in a single fitness function rating between 0 and 1
			
			// calculate the population size once now rather than every time it is needed in criteria (as popSize doesn't change)
			popSize = pop.getPopulationCapacity();  
			criteria[0] = calcCriteria1(pop);
			criteria[1] = calcCriteria2(pop);
			criteria[2] = calcCriteria3(pop);
			criteria[3] = calcCriteria4(pop);
			criteria[4] = calcCriteria5(pop);
			criteria[5] = calcCriteria6(pop);
			criteria[6] = calcCriteria7(pop);
			criteria[7] = calcCriteria8a(pop);
			
			// need to generate R-I which is Population pop with any members of the original inspiring set removed
			// criteria 11-18 use popMinusI
			// Just generate this set once, here
			Improvisations popMinusI = removeInspiringSetMembersFrom(pop);			
			// calculate the population size once now rather than every time it is needed in criteria (as popSizeMinusI doesn't change)
			// NB has to be the current population size (no of items in inspiring set), not population capacity (potential size... == popSize)
			popSizeMinusI = popMinusI.getCurrentPopulationSize();  
			
			criteria[8] = calcCriteria9(pop);
			criteria[9] = calcCriteria10a(pop);
			criteria[10] = calcCriteria11(popMinusI);
			criteria[11] = calcCriteria12(popMinusI);
			criteria[12] = calcCriteria13(popMinusI, pop);
			criteria[13] = calcCriteria14(popMinusI, pop);
			criteria[14] = calcCriteria15(popMinusI);
			criteria[15] = calcCriteria16(popMinusI);
			criteria[16] = calcCriteria17(popMinusI);
			criteria[17] = calcCriteria18(popMinusI);
			
			String output = display();
// TODO zzz System.out.println for debugging
System.err.println(output);
			double fitness = calculateFitnessFromCriteria();
			return fitness;
	}
	
	private static double calculateFitnessFromCriteria() {
		double criteriaFitness = 0.0;
		double sumOfWeightings = 0.0;
		for (int i=0; i<18; i++)  {
			sumOfWeightings += weightings[i];
			if (criteria[i])  {
				criteriaFitness += weightings[i];
			}
		}
		criteriaFitness = criteriaFitness / sumOfWeightings; // Normalise for 18 criteria
System.err.println("Criteria Fitness = "+criteriaFitness);
		return criteriaFitness;
	}

	private static Improvisations removeInspiringSetMembersFrom(Improvisations pop)  {
		// TODO at the moment there is no InspiringSet!
		int[] fakeImproParameters = {0, 0, 0, 0, 0, 0, 0, 0, 0};
		/*int popSize = pop.getPopulationCapacity();
		Improvisations popMinusI = new Improvisations(pop.getPopulationCapacity(), fakeImprovisationParameters, false);
		for (int i=0; i<popSize; i++)  {
			if (!pop.inInspiringSet(pop.getPopulationMemberAt(i)))  {
				popMinusI.addMemberAt(pop.getPopulationMemberAt(i), i);
			}
		}*/
		Improvisations popMinusI = new Improvisations(0, fakeImproParameters, false);
		return popMinusI;
	}

	private static String display()   {
		String displayOutput = "";
		String criteriaMeaning;  // suppress?
		int summary = 0;
		for (int i=0; i<18; i++)  {
			criteriaMeaning = getCriteriaMeaning(i);
			//displayOutput = displayOutput+"Criteria "+(i+1)+" [ "+criteriaMeaning+" ]: "+criteria[i]+"\n";
			if (criteria[i]) {
				summary++;
				displayOutput = displayOutput+"T";
			}
			else displayOutput = displayOutput+"F";
		}
		displayOutput = displayOutput+" - Total: "+summary+"/18";
		return displayOutput;
	}

	private static String getCriteriaMeaning(int index)  {
		// meanings taken from Pereira (2005) and Gervas (2002) (criteria 13 corrected slightly)
		String meaning = "";
		switch(index)   {
			case (0)  : { meaning = "Average typicality                            "; break; }
			case (1)  : { meaning = "#Typical results / #results                   "; break;}
			case (2)  : { meaning = "Average quality                               "; break; }
			case (3)  : { meaning = "#Good results / #results                      "; break; }
			case (4)  : { meaning = "#Good typical results / #typical results      "; break; }
			case (5)  : { meaning = "#Good atypical results / #results             "; break; }
			case (6)  : { meaning = "#Good atypical results / #atypical results    "; break; }
			case (7)  : { meaning = "#Good atypical results / #good typical results"; break; }
			case (8)  : { meaning = "#Results in inspiring set / #inspiring set    "; break; }
			case (9)  : { meaning = "1 - (#Results in inspiring set / #results)    "; break; }
			case (10) : { meaning = "Average typicality of new results             "; break; }
			case (11) : { meaning = "Average quality of new results                "; break; }
			case (12) : { meaning = "#Typical new results / #results               "; break; } // corrected slightly from .../#new results to .../#results
			case (13) : { meaning = "#Good new results / #results                  "; break; }
			case (14) : { meaning = "#Typical new results / #new results           "; break; }
			case (15) : { meaning = "#Good new results / #new results              "; break; }
			case (16) : { meaning = "#Good typical new results/ #new results (creativity within existing norms)"; break; }  
			case (17) : { meaning = "#Good atypical new results/ #new results (some original deviation from past practice)"; break; }}
		return meaning;
	}
	
/*	Definitions for Ritchie's criteria:
	 - AV(F, X):   the average value of a function F across finite set X
	 - R: 	       the set of basic items being rated: in this interpretation 
	 			   this is one generation of the population
	 - ratio(X, Y):The relative sizes of two finite sets X, Y, where Y is not empty
	 - T[a, b](X): The subset of set X which falls in a given range of typicality (from a to b, inclusive)
	 - V[a, b](X): The subset of set X which falls in a given range of value (from a to b, inclusive)
	 - theta:      The threshold score at which items are deemed to have reached acceptable typicality
*/	
	
	private static double AV(double[] F, Improvisations X)   {
		int popSize    = X.getPopulationCapacity();
		double average = 0.0;   // initialised value
		for (int i=0; i<popSize; i++)  {
			average = average+F[i];
		}
		average = average/popSize;
		return average;
	}
	
	private static double ratio(int sizeOfSetX, int sizeOfSetY)   {
		if (sizeOfSetY!=0)  {
			return ((double) sizeOfSetX / (double) sizeOfSetY);
		}
		else return 0;
	}
	
	private static int T(double alpha, double beta, Improvisations X)  {
		// counts the number of items in Population X that are rated between a and b for typicality
		int popSize      = X.getPopulationCapacity();
		double typicalityRating;
		int countTypical = 0;
		
		for (int i=0; i<popSize; i++)  {
			typicalityRating = X.getTypicalityRatingOfMemberAt(i);
			if ((typicalityRating>=alpha) && (typicalityRating<=beta))  {
				countTypical++;
			}
		}
		return countTypical;
	}
	
	/**
	 * Counts the number of items in Population X that are rated between alpha and beta for value
	 * @param alpha The minimum value being considered
	 * @param beta The maximum value being considered
	 * @param X The population being examined
	 * @return The number of items in that range
	 */
	private static int V(double alpha, double beta, Improvisations X)  {
		int popSize      = X.getPopulationCapacity();
		double valueRating;
		int countValuable = 0;
		
		for (int i=0; i<popSize; i++)  {
			valueRating = X.getValueRatingOfMemberAt(i);
			if ((valueRating>=alpha) && (valueRating<=beta))  {
				countValuable++;
			}
		}
		return countValuable;
	}
	
	/**
	 * Count the number of members of Population R that have a value rating between 
	 * minV and maxV (inclusive) and a typicality rating between minT and maxT (inclusive)
	 * 
	 * @param minV the minimum value being considered
	 * @param maxV the maximum value being considered
	 * @param minT the minimum typicality being considered
	 * @param maxT the maximum typicality being considered
	 * @param R the set of Population members being examined
	 * @return the number of members with value and typicality ratings in the specified range
	 */
	private static int VintersectT(double minV, double maxV, double minT, double maxT, Improvisations R)  {
		int popSize = R.getPopulationCapacity();
		int countItemsInIntersect = 0;
		double memberValue;
		double memberTypicality;
		for (int i=0; i<popSize; i++)  {
			memberValue = R.getValueRatingOfMemberAt(i);
			if ((memberValue >=minV) && (memberValue <=maxV))  {
				memberTypicality = R.getTypicalityRatingOfMemberAt(i);
				if ((memberTypicality >= minT) && (memberTypicality <= maxT))  {
					countItemsInIntersect++;
				}
			}
		}
		return countItemsInIntersect;
	}

	/**
	 * AV(typ, R) > theta, for suitable theta
	 * 
	 * Considers whether the average typicality rating across the set R exceeds some threshold (theta) 
	 * 
	 * @param R The population being examined for creativity on this criteria
	 * @return true if the criteria is satisfied, and false otherwise
	 */
	private static boolean calcCriteria1(Improvisations R) {
		double theta = 0.5;			
		return (AV(R.getTypicalityRatings(), R) > theta);
	}

	/**
	 * ratio( T[alpha, 1](R) / R ) > theta, for suitable alpha, theta
	 * 
	 * Examines whether a significant proportion of the result set are indeed
	 * examples of the intended genre
	 * 
	 * @param R The population being examined for creativity on this criteria
	 * @return true if the criteria is satisfied, and false otherwise
	 */
	private static boolean calcCriteria2(Improvisations R)  {
		double theta = 0.5;
		double alpha = 0.5;  // minimum acceptable rating of typicality
		
		int numAcceptablyTypicalMembers = T(alpha, 1, R);
		return (ratio(numAcceptablyTypicalMembers, popSize) > theta);
	}

	/**
	 * AV(val, R) > theta, for suitable theta
	 * Considers whether the average value rating across the set R exceeds some threshold (theta)
	 * 
	 * @param R The population being examined for creativity on this criteria
	 * @return true if the criteria is satisfied, and false otherwise
	 */
	private static boolean calcCriteria3(Improvisations R) {
		double theta = 0.5;
		return (AV(R.getValueRatings(), R) > theta);
	}

	/**
	 * ratio (V[gamma, 1](R) / R ) > theta, for suitable gamma, theta
	 * 
	 * Examines whether a significant proportion of the result set score well
	 * in terms of quality
	 * 
	 * @param R The population being examined for creativity on this criteria
	 * @return true if the criteria is satisfied, and false otherwise
	 */
	private static boolean calcCriteria4(Improvisations R) {
		double theta = 0.5;
		double gamma = 0.5;
		
		int numAcceptablyValuableMembers = V(gamma, 1, R);		
		return (ratio(numAcceptablyValuableMembers, popSize) > theta);
	}

	/**
	 * ratio (V[gamma, 1](R) INTERSECT T[alpha, 1](R) / T[alpha, 1](R)) > theta
	 *  for suitable alpha, gamma, theta
	 *  
	 * Just looking at the items which meet some typicality threshold (i.e. which
	 * are genuine instances of the artefact class), checks what proportion of these
	 * also score well in terms of quality
	 * 
	 * @param R The population being examined for creativity on this criteria
	 * @return true if the criteria is satisfied, and false otherwise
	 */
	private static boolean calcCriteria5(Improvisations R) {
		double theta = 0.5;
		double alpha = 0.5;
		double gamma = 0.5;
		
		int numAcceptablyValuableAndTypicalMembers = VintersectT(gamma, 1.0, alpha, 1.0, R);
		int numAcceptablyTypicalMembers = T(alpha, 1.0, R);
		return (ratio(numAcceptablyValuableAndTypicalMembers, numAcceptablyTypicalMembers) > theta);
	}
	
	/**
	 * ratio (V[gamma, 1](R) INTERSECT T[0, beta](R) / R) > theta
	 *  for suitable beta, gamma, theta
	 *  
	 * Considers whether a large proportion of the program's output falls into
	 * the (supposedly desirable) category of untypical but highly valued items.
	 * 
	 * @param R The population being examined for creativity on this criteria
	 * @return true if the criteria is satisfied, and false otherwise
	 */
	private static boolean calcCriteria6(Improvisations R) {
		double theta = 0.5;
		double beta  = 0.5;
		double gamma = 0.5;
		
		int numAcceptablyValuableButAtypicalMembers = VintersectT(gamma, 1.0, 0.0, beta, R);
		return (ratio(numAcceptablyValuableButAtypicalMembers, popSize) > theta);
	}
	
	/**
	 * ratio (V[gamma, 1](R) INTERSECT T[0, beta](R) / T[0, beta](R)) > theta
	 *  for suitable beta, gamma, theta
	 *  
	 * Considers whether a large proportion of the atypical items produced by the program
	 * are highly valued.
	 * 
	 * @param R The population being examined for creativity on this criteria
	 * @return true if the criteria is satisfied, and false otherwise
	 */
	private static boolean calcCriteria7(Improvisations R) {
		double theta = 0.5;
		double beta  = 0.5;
		double gamma = 0.5;
		
		int numAcceptablyValuableButAtypicalMembers = VintersectT(gamma, 1.0, 0.0, beta, R);
		int numUnacceptablyAtypicalMembers = T(0.0, beta, R);
		return ((ratio(numAcceptablyValuableButAtypicalMembers, numUnacceptablyAtypicalMembers)) > theta);
	}
	
	/**
	 * ratio (V[gamma, 1](R) INTERSECT T[0, beta](R), V[gamma, 1](R)) > theta
	 *  for suitable beta, gamma, theta
	 *  
	 * Considers whether a large proportion of the highly valued items are atypical
	 * 
	 * @param R The population being examined for creativity on this criteria
	 * @return true if the criteria is satisfied, and false otherwise
	 */
	private static boolean calcCriteria8a(Improvisations R) {
		double theta = 0.5;
		double beta  = 0.5;
		double gamma = 0.5;
		
		int numAcceptablyValuableButAtypicalMembers = VintersectT(gamma, 1.0, 0.0, beta, R);
		int numAcceptablyValuableMembers = V(gamma, 1.0, R);
		return (ratio(numAcceptablyValuableButAtypicalMembers, numAcceptablyValuableMembers) > theta);
	}
	
	/**
	 * ratio(I INTERSECT R / I) > theta, for suitable theta
	 * 
	 * Sees how many of the Inspiring set have been replicated in the program output
	 * (on the idea that it is good to be able to reproduce the inspiring set - 
	 *  personally I think this is far less important for creativity than criteria 10)
	 *  
	 * @param R The population being examined for creativity on this criteria
	 * @return true if the criteria is satisfied, and false otherwise
	 */
	private static boolean calcCriteria9(Improvisations R) {
		double theta = 0.5;
		
		int countReplicationsOfInspiringSetInCurrentPopulation = popSize - popSizeMinusI;
		int inspiringSetSize = R.getInspiringSetSize();
		return (ratio(countReplicationsOfInspiringSetInCurrentPopulation, inspiringSetSize) > theta);
	}
	
	/**
	 * (1 - ratio(I INTERSECT R / R)) > theta, for suitable theta
	 * 
	 * As producing more than just the inspiring set is generally agreed to be a symptom 
	 * of creativity, this considers the ratio of the results not in the inspiring set against
	 * all results
	 * 
	 * @param R The population being examined for creativity on this criteria
	 * @return true if the criteria is satisfied, and false otherwise
	 */
	private static boolean calcCriteria10a(Improvisations R) {
		double theta = 0.5;
		
		int countReplicationsOfInspiringSetInCurrentPopulation = popSize - popSizeMinusI;
		double ratio = ratio(countReplicationsOfInspiringSetInCurrentPopulation, popSize);
		return ((1-ratio) > theta);
	}
	
	/**
	 * AV(typ, (R-I)) > theta, for suitable theta
	 * 
	 * Looking just at novel items (i.e. not in the inspiring set), checks how many are 
	 * exemplars of the chosen genre
	 * 
	 * @param RminusI The population being examined for creativity on this criteria (with any duplications of the original inspiring set removed)
	 * @return true if the criteria is satisfied, and false otherwise
	 */
	private static boolean calcCriteria11(Improvisations RminusI) {
		double theta = 0.5;

		return (AV(RminusI.getTypicalityRatings(), RminusI) > theta);
	}
	
	/**
	 * AV(val, (R-I)) > theta, for suitable theta
	 * 
	 * Looking just at novel items, checks how many are highly valued
	 * 
	 * @param RminusI The population being examined for creativity on this criteria (with any duplications of the original inspiring set removed)
	 * @return true if the criteria is satisfied, and false otherwise
	 */
	private static boolean calcCriteria12(Improvisations RminusI) {
		double theta = 0.5;

		return (AV(RminusI.getValueRatings(), RminusI) > theta);
	}
	
	/**
	 * ratio(V[gamma, 1](R-I) / R) > theta, for suitable gamma, theta
	 * 
	 * Checks whether novel and highly typical items form a high proportion of the results
	 * 
	 * @param RminusI The population being examined for creativity on this criteria (with any duplications of the original inspiring set removed)
	 * @param R The population being examined for creativity on this criteria
	 * @return true if the criteria is satisfied, and false otherwise
	 */
	private static boolean calcCriteria13(Improvisations RminusI, Improvisations R) {
		double theta = 0.5;
		double gamma = 0.5;
		
		int numNovelAndAcceptablyTypicalMembers = V(gamma, 1.0, RminusI);
		return (ratio(numNovelAndAcceptablyTypicalMembers, popSize) > theta);
	}
	
	/**
	 * ratio(T[alpha, 1](R-I) / R) > theta, for suitable alpha, theta
	 * 
	 * Checks whether novel and highly valued items form a high proportion of the results
	 * 
	 * @param RminusI The population being examined for creativity on this criteria (with any duplications of the original inspiring set removed)
	 * @param R The population being examined for creativity on this criteria
	 * @return true if the criteria is satisfied, and false otherwise
	 */
	private static boolean calcCriteria14(Improvisations RminusI, Improvisations R) {
		double theta = 0.5;
		double alpha = 0.5;
		
		int numNovelAndAcceptablyValuableMembers = T(alpha, 1.0, RminusI);
		return (ratio(numNovelAndAcceptablyValuableMembers, popSize) > theta);
	}
	
	/**
	 * ratio(T[alpha, 1](R-I) / (R-I)) > theta, for suitable alpha, theta
	 * 
	 * Considers whether highly typical items form a high proportion of 
	 * the novel items
	 * 
	 * @param RminusI The population being examined for creativity on this criteria (with any duplications of the original inspiring set removed)
	 * @return true if the criteria is satisfied, and false otherwise
	 */
	private static boolean calcCriteria15(Improvisations RminusI) {
		double theta = 0.5;
		double alpha = 0.5;
		
		int numNovelAndAcceptablyTypicalMembers = T(alpha, 1.0, RminusI);
		return (ratio(numNovelAndAcceptablyTypicalMembers, popSizeMinusI) > theta);
	}
	
	/**
	 * ratio(V[gamma, 1(R-I) / (R-I)) > theta, for suitable gamma, theta
	 * 
	 * Considers whether highly valued items form a high proportion of 
	 * the novel items
	 * 
	 * @param RminusI The population being examined for creativity on this criteria (with any duplications of the original inspiring set removed)
	 * @return true if the criteria is satisfied, and false otherwise
	 */
	private static boolean calcCriteria16(Improvisations RminusI) {
		double theta = 0.5;
		double gamma = 0.5;
		
		int numNovelAndAcceptablyValuableMembers = V(gamma, 1.0, RminusI);
		return (ratio(numNovelAndAcceptablyValuableMembers, popSizeMinusI) > theta);
		}
	
	/**
	 * ratio(V[gamma, 1](R-I) INTERSECT T[alpha, 1](R-I) / (R-I)) > theta
	 *  for suitable alpha, gamma, theta
	 *  
	 * Considers the occurrence within the novel results of items which are of
	 * good quality and highly typical 
	 * (i.e. demonstrate creativity within existing norms)
	 * c.f. Boden's exploratory creativity?
	 *  
	 * @param RminusI The population being examined for creativity on this criteria (with any duplications of the original inspiring set removed)
	 * @return true if the criteria is satisfied, and false otherwise
	 */
	private static boolean calcCriteria17(Improvisations RminusI) {
		double theta = 0.5;
		double alpha = 0.5;
		double gamma = 0.5;
		
		int numNovelAndAcceptablyValuableAndTypicalMembers = VintersectT(gamma, 1.0, alpha, 1.0, RminusI);
		return (ratio(numNovelAndAcceptablyValuableAndTypicalMembers, popSizeMinusI) > theta);
	}
	
	/**
	 * ratio(V[gamma, 1](R-I) INTERSECT T[0, beta](R-I) / (R-I)) > theta
	 *  for suitable beta, gamma, theta
	 *  
	 * Considers the occurrence within the novel results of items which are of
	 * good quality and highly atypical 
	 * (i.e. demonstrate some original deviation from past practice)
	 * c.f. Boden's transformational creativity?
	 * 
	 * @param RminusI The population being examined for creativity on this criteria (with any duplications of the original inspiring set removed)
	 * @return true if the criteria is satisfied, and false otherwise
	 */
	private static boolean calcCriteria18(Improvisations RminusI) {
		double theta = 0.5;
		double beta  = 0.5;
		double gamma = 0.5;
		
		int numNovelAndAcceptablyValuableButAtypicalMembers = VintersectT(gamma, 1.0, 0.0, beta, RminusI);
		return (ratio(numNovelAndAcceptablyValuableButAtypicalMembers, popSizeMinusI) > theta);
	}
	
	
}
