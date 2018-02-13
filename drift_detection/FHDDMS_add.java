
package moa.classifiers.core.driftdetection;

import java.util.Arrays;

import moa.core.ObjectRepository;
import moa.options.FloatOption;
import moa.options.IntOption;
import moa.tasks.TaskMonitor;

/**
 *  <p>Additive Stacking Fast Hoeffding Drift Detection Method (FHDDMS_add)</p>
 *  <p>A. Pesaranghader, H.L. Viktor, Eric Paquet, Reservoir of Diverse Adaptive Learners and 
 *  Stacking Fast Hoeffding Drift Detection Methods for Evolving Data Streams</p>
 *  
 *  @author Ali Pesaranghader (apesaran@uottawa.ca, alipsgh@gmail.com)
 *  @version $Revision: 7 $
 */
public class FHDDMS_add extends AbstractChangeDetector {
        
    private static final long serialVersionUID = 1L;
    
    public IntOption stackSizeOption = new IntOption("stackSize",'s',"The size of the stack.", 4, 0, Integer.MAX_VALUE);
    public IntOption shortWinSizeOption = new IntOption("shortWinSize",'w',"The size of the short Window.", 25, 0, Integer.MAX_VALUE);
    public FloatOption confidenceOption = new FloatOption("confidence", 'c', "The confidence level. The default value is E-6.", 0.000001, 0, 1);
    
    private int[] stack;
    private int counter;
    
    public static int w_s, w_l;
    public static double epsilon_s, epsilon_l;

    private double u_s_max, u_l_max;
    private int n_1;
    
    private boolean first_round;
    
    public FHDDMS_add() {
        resetLearning();
    }

    @Override
    public void resetLearning() {
        
        first_round = true;
        
        stack = new int[stackSizeOption.getValue()];
        counter = 0;
        
        w_s = shortWinSizeOption.getValue();
        w_l = shortWinSizeOption.getValue() * stackSizeOption.getValue();
        
        epsilon_s = Math.sqrt((Math.log(1 / confidenceOption.getValue())) / (2 * w_s));
        epsilon_l = Math.sqrt((Math.log(1 / confidenceOption.getValue())) / (2 * w_l));
        
        u_s_max = 0.0;
        u_l_max = 0.0;
        n_1 = 0;
        
    }

    @Override
    public void input(double prediction) {
        
        if (this.isChangeDetected == true || this.isInitialized == false) {
            resetLearning();
            this.isInitialized = true;
        }
        
        boolean drift_status = false;
        boolean warning_status = false;
        
        if (counter == stack.length * w_s){
            counter -= w_s;
            n_1 -= stack[0];
            stack = Arrays.copyOf(Arrays.copyOfRange(stack, 1, stack.length), stack.length);
            if (first_round)
                first_round = false;
        }
        
        int index = (first_round == true) ? (int)((double) counter / w_s) : stack.length - 1;
        if (prediction == 0){
            stack[index] += 1;
            n_1 += 1;
        }
        
        counter += 1;
        
        boolean drift_status_s = false;
        boolean drift_status_l = false;
        
        if (counter % w_s == 0){
            // testing the short window
            double u_s = ((double) stack[index]) / w_s;
            u_s_max = (u_s_max < u_s) ? u_s : u_s_max;
            drift_status_s = (u_s_max - u_s > epsilon_s) ? true : false;
        }
              
        if (counter == w_l){
            // testing the long window
            double u_l = ((double) n_1) / w_l;
            u_l_max = (u_l_max < u_l) ? u_l : u_l_max;
            drift_status_l = (u_l_max - u_l > epsilon_l) ? true : false;
        }
        
        // concluding drift status
        drift_status = (drift_status_s || drift_status_l);
        
        this.isWarningZone = warning_status;
        this.isChangeDetected = drift_status;
        
    }

    @Override
    public void getDescription(StringBuilder sb, int indent) {
        // TODO Auto-generated method stub
    }

    @Override
    protected void prepareForUseImpl(TaskMonitor monitor,
            ObjectRepository repository) {
        // TODO Auto-generated method stub
    }
}