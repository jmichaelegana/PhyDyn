package phydyn.loggers;

import beast.base.core.Description;
import beast.base.core.Input;
import beast.base.core.Input.Validate;
import beast.base.core.Loggable;
import beast.base.evolution.tree.Tree;
import beast.base.inference.CalculationNode;
import phydyn.distribution.STreeGenericLikelihood;
import phydyn.distribution.StateProbabilities;
import phydyn.util.DVector;

import java.io.PrintStream;

/**
 * @author Igor Siveroni
 */

@Description("Logs root state probabilities")

public class STreeRootLogger extends CalculationNode implements Loggable {	
	 public Input<Tree> treeInput = new Input<Tree>("tree", "deprecated: tree to be logged");
	 public Input<STreeGenericLikelihood> densityInput = new Input<STreeGenericLikelihood>(
				"density",
				"density / structured tree likelihood",Validate.REQUIRED);
	 
	 int numStates = 0;
	 
	public void initAndValidate() {
		numStates = densityInput.get().numStates;
		if (treeInput.get()!=null) {
			System.out.println("Warning: tree option in STreeRootLogger deprecated - remove.");
		}
	}

	
	/**
     * Header information
     *
     * @param out log stream
     * @throws Exception
     */
	 @Override
    public void init(PrintStream out)  {
    	out.print("Sample\t");
    	for(int i=0; i < numStates; i++)
    		out.print("state"+i+"\t");
    	// out.println("");
    }

    /**
     * log this sample for current state to PrintStream,
     * e.g. value of a parameter, list of parameters or Newick tree
     *
     * @param nSample chain sample number
     * @param out     log stream
     */
    @Override
    public void log(long nSample, PrintStream out) {
    	out.print(nSample+"\t");
    	StateProbabilities sp = densityInput.get().getStateProbabilities();
    	// investigate: use tree.getRoot() instead
    	DVector probs = sp.getRootProbs();
    	if (probs != null) {
    		for(int i=0; i < numStates; i++)
    			out.print(probs.get(i)+"\t");
    	} else {
    		for(int i=0; i < numStates; i++)
    			out.print(0.00+"\t");
    	}
    	// out.println("");
    }

    /**
     * close log. An end of log message can be left (as in End; for Nexus trees)
     *
     * @param out log stream
     */
    public void close(PrintStream out) {
    	// last line null
    }

}
