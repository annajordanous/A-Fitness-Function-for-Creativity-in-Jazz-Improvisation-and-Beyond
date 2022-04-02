package creativeJazzGA;

import jm.music.data.Score;

/**
 * @author Anna Jordanous
 * 
 * Working implementation of the findings of Table 1, Juslin and Laukka (2004) (p. 221)
 * 
 * Table 1. Summary of musical features correlated with discrete emotions in musical expression. 
 * Happiness 
 * 	Fast tempo, small tempo variability, major mode, simple and consonant harmony, medium-high sound level, small sound level variability, high pitch, much pitch variability, wide pitch range, ascending pitch, perfect 4th and 5th intervals, rising micro intonation, raised singer’s formant, staccato articulation, large articulation variability, smooth and fluent rhythm, bright timbre, fast tone attacks, small timing varibility, sharp contrasts between “long” and “short” notes, medium-fast vibrato rate, medium vibrato extent, micro-structural regularity
 * Sadness
 * 	Slow tempo, minor mode, dissonance, low sound level, moderate sound level variability, low pitch, narrow pitch range, descending pitch, “flat” (or falling) intonation, small intervals (e.g., minor 2nd), lowered singer’s formant, legato articulation, small articulation variability, dull timbre, slow tone attacks, large timing variability (e.g., rubato), soft contrasts between “long” and “short” notes, pauses, slow vibrato, small vibrato extent, ritardando, micro-structural irregularity
 * Anger
 *  Fast tempo, small tempo variability, minor mode, atonality, dissonance, high sound level, small loudness variability, high pitch, small pitch variability, ascending pitch, major 7th and augmented 4th intervals, raised singer’s formant, staccato articulation, moderate articulation variability, complex rhythm, sudden rhythmic changes (e.g., syncopations), sharp timbre, spectral noise, fast tone attacks/decays, small timing variability, accents on tonally unstable notes, sharp contrasts between “long” and “short” notes, accelerando, medium-fast vibrato rate, large vibrato extent, micro-structural irregularity  	
 * Fear
 * 	Fast tempo, large tempo variability, minor mode, dissonance, low sound level, large sound level variability, rapid changes in sound level, high pitch, ascending pitch, wide pitch range, large pitch contrasts, staccato articulation, large articulation variability, jerky rhythms, soft timbre, very large timing variability, pauses, soft tone attacks, fast vibrato rate, small vibrato extent, micro-structural irregularity
 * Tenderness
 * 	Slow tempo, major mode, consonance, medium-low sound level, small sound level variability, low pitch, fairly narrow pitch range, lowered singer’s formant, legato articulation, small articulation variability, slow tone attacks, soft timbre, moderate timing variability, soft contrasts between long and short notes, accents on tonally stable notes, medium fast vibrato, small vibrato extent, micro-structural regularity 
 * 
 * 
 * Patrik N. Juslin & Petri Laukka (2004) Expression, Perception, and Induction of Musical Emotions: A Review and a Questionnaire Study of Everyday Listening, Journal of New Music Research, 33:3, 217-238, DOI: 10.1080/0929821042000317813
 */

public class CompIntentEmotionalInvolvement   {
	

	public double ratePopulationMemberForIEI(Score popMember) {
        double tempo = popMember.getTempo();
		//tempo variability
		int mode = popMember.getKeyQuality(); //Returns the Score's key quality 0 is Major, 1 is minor
		//harmony simplicity
		// harmony consonance
		// sound level (volume?)
		// soundlevel variability
		// pitches
		//pitch variablility
		// pitch range
		// ascending pitch,
		// perfect 4th and 5th intervals,
		//  rising micro intonation,
		// raised singer’s formant,
		//  staccato articulation, large articulation variability,
		//  smooth and fluent rhythm,
		// bright timbre,
		//  fast tone attacks
		// small timing varibility,
		// sharp contrasts between “long” and “short” notes,
		// medium-fast vibrato rate, medium vibrato extent, 
		// micro-structural regularity

		//sadness:
		//*  “flat” (or falling) intonation, small intervals (e.g., minor 2nd), lowered singer’s formant, legato articulation, small articulation variability, dull timbre, slow tone attacks, large timing variability (e.g., rubato), soft contrasts between “long” and “short” notes, pauses, slow vibrato, small vibrato extent, ritardando, micro-structural irregularity

		//* Anger
		//*  Fast tempo, small tempo variability, minor mode, atonality, dissonance, high sound level, small loudness variability, high pitch, small pitch variability, ascending pitch, major 7th and augmented 4th intervals, raised singer’s formant, staccato articulation, moderate articulation variability, complex rhythm, sudden rhythmic changes (e.g., syncopations), sharp timbre, spectral noise, fast tone attacks/decays, small timing variability, accents on tonally unstable notes, sharp contrasts between “long” and “short” notes, accelerando, medium-fast vibrato rate, large vibrato extent, micro-structural irregularity  	

		//* Fear
		//* 	Fast tempo, large tempo variability, minor mode, dissonance, low sound level, large sound level variability, rapid changes in sound level, high pitch, ascending pitch, wide pitch range, large pitch contrasts, staccato articulation, large articulation variability, jerky rhythms, soft timbre, very large timing variability, pauses, soft tone attacks, fast vibrato rate, small vibrato extent, micro-structural irregularity

		//* Tenderness
		//* 	Slow tempo, major mode, consonance, medium-low sound level, small sound level variability, low pitch, fairly narrow pitch range, lowered singer’s formant, legato articulation, small articulation variability, slow tone attacks, soft timbre, moderate timing variability, soft contrasts between long and short notes, accents on tonally stable notes, medium fast vibrato, small vibrato extent, micro-structural regularity 
		

		return Math.random();
    }
}
