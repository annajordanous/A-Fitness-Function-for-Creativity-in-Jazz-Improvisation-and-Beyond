package creativeJazzGA.genes;

import org.jgap.*;
import org.jgap.impl.*;

public class MaxNumNotesGene extends IntegerGene  implements Gene, java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6312685327284729028L;

	private int m_lowerBounds, m_upperBounds;
	Configuration conf;
	
	
	public MaxNumNotesGene(Configuration aConfiguration, int lowerBound, int upperBound)
			throws InvalidConfigurationException {
		super(aConfiguration, lowerBound, upperBound);
		m_lowerBounds = lowerBound;
		m_upperBounds = upperBound;
		conf = aConfiguration;
	}
	
	public Gene newGene()  {
		Gene newGene = null;
		try {
			newGene = new MaxNumNotesGene(conf, m_lowerBounds, m_upperBounds);
		} catch (InvalidConfigurationException e) { }
		return newGene;
	}

	 /**
	   * @return string representation of this Gene's value that may be useful for
	   * display purposes (adapted from IntegerGene)
	   *
	   */
	  public String toString() {
	    String s = "MaxNumNotesGene(" + m_lowerBounds + "," + m_upperBounds + ")"
	        + "=";
	    if (getInternalValue() == null) {
	      s += "null";
	    }
	    else {
	      s += getInternalValue().toString();
	    }
	    return s;
	  }

}
