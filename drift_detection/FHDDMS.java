
package moa.classifiers.core.driftdetection;

import java.util.Arrays;

import moa.classifiers.core.driftdetection.AbstractChangeDetector;
import moa.core.ObjectRepository;
import moa.options.FloatOption;
import moa.options.IntOption;
import moa.tasks.TaskMonitor;

/**
 *  <p>Stacking Fast Hoeffding Drift Detection Method (FHDDMS)</p>
 *  <p>A. Pesaranghader, H.L. Viktor, Eric Paquet, Reservoir of Diverse Adaptive Learners and 
 *  Stacking Fast Hoeffding Drift Detection Methods for Evolving Data Streams</p>
 *  
 *  @author Ali Pesaranghader (apesaran@uottawa.ca, alipsgh@gmail.com)
 *  @version $Revision: 7 $
 */
public class FHDDMS extends AbstractChangeDetector {
        
    private static final long serialVersionUID = 1L;
    
    public IntOption stackSizeOption = new IntOption("stackSize",'s',"The size of the stack.", 4, 0, Integer.MAX_VALUE);
    public IntOption shortWinSizeOption = new IntOption("shortWinSize",'w',"The size of the short Window.", 25, 0, Integer.MAX_VALUE);
    public FloatOption confidenceOption = new FloatOption("confidence", 'c', "The confidence level. The default value is E-6.", 0.000001, 0, 1);
    
    private int[] stack;
    private int pointer;
    
    private static int w_s, w_l;
    private static double epsilon_s, epsilon_l;
    private double u_s_max, u_l_max;

    public FHDDMS() {
        resetLearning();
    }

    @Override
    public void resetLearning() {
        stack = new int[shortWinSizeOption.getValue() * stackSizeOption.getValue()];
        pointer = 0;
        
        w_s = shortWinSizeOption.getValue();
        w_l = shortWinSizeOption.getValue() * stackSizeOption.getValue();
                
        epsilon_s = Math.sqrt((Math.log(1 / confidenceOption.getValue())) / (2 * w_s));
        epsilon_l = Math.sqrt((Math.log(1 / confidenceOption.getValue())) / (2 * w_l));
        
        u_s_max = 0.0;
        u_l_max = 0.0;
    }

    @Override
    public void input(double prediction) {
        
        if (this.isChangeDetected == true || this.isInitialized == false) {
            resetLearning();
            this.isInitialized = true;
        }
        
        boolean drift_status = false;
        boolean warning_status = false;
        
        if (pointer < stack.length){
            stack[pointer] = (prediction == 0) ? 1 : 0;
            pointer += 1;
        } else {
            for (int i = 0; i < stack.length; i++){
                if (i == stack.length - 1){
                    stack[i] = (prediction == 0) ? 1 : 0;
                } else {
                    stack[i] = stack[i + 1];
                }
            }
        }
        
        if (pointer == stack.length){
            
            // testing the short window
            double u_s = ((double) count_ones(Arrays.copyOfRange(stack, stack.length - w_s, stack.length))) / w_s;
            u_s_max = (u_s_max < u_s) ? u_s : u_s_max;
            boolean drift_status_s = (u_s_max - u_s > epsilon_s) ? true : false;
            
            // testing the long window
            double u_l = ((double) count_ones(stack)) / w_l;
            u_l_max = (u_l_max < u_l) ? u_l : u_l_max;
            boolean drift_status_l = (u_l_max - u_l > epsilon_l) ? true : false;
            
            // concluding drift status
            drift_status = (drift_status_s || drift_status_l);
            
        }
        
        this.isWarningZone = warning_status;
        this.isChangeDetected = drift_status;
        
    }
    
    private int count_ones(int[] win){
        int count = 0;
        for (int e : win){
            count += e;
        }
        return count;
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