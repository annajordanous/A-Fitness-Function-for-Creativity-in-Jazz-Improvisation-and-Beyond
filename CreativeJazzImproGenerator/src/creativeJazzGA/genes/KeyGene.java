package creativeJazzGA.genes;

import org.jgap.*;
import org.jgap.impl.*;

public class KeyGene extends IntegerGene  implements Gene, java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6312685327284729028L;

	private int m_lowerBounds, m_upperBounds;
	Configuration conf;
	
	
	public KeyGene(Configuration aConfiguration, int lowerBound, int upperBound)
			throws InvalidConfigurationException {
		super(aConfiguration, lowerBound, upperBound);
		m_lowerBounds = lowerBound;
		m_upperBounds = upperBound;
		conf = aConfiguration;
	}
	
	public Gene newGene()  {
		Gene newGene = null;
		try {
			newGene = new KeyGene(conf, m_lowerBounds, m_upperBounds);
		} catch (InvalidConfigurationException e) { }
		return newGene;
	}



}
