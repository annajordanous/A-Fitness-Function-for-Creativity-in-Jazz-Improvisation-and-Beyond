package creativeJazzGA;

import jm.music.data.Score;

/**
 * @author Anna Jordanous
 * 	 * TODO: One possible: change it to incorporate the following tests from Huron (2001):
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


public class CompDomainCompetence   {
	

	public double ratePopulationMemberForDC(Score popMember) {
      

		return Math.random();
    }
}
