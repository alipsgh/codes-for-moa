
package moa.classifiers.core.driftdetection;

import moa.classifiers.core.driftdetection.AbstractChangeDetector;
import moa.core.ObjectRepository;
import moa.options.FloatOption;
import moa.options.IntOption;
import moa.tasks.TaskMonitor;

/**
 *  <p>Fast Hoeffding Drift Detection Method (FHDDM)</p>
 *  <p>A. Pesaranghader, H.L. Viktor, Fast Hoeffding Drift Detection Method for Evolving Data Streams.
 *  In the Proceedings of ECML-PKDD 2016.</p>
 *  
 *  @author Ali Pesaranghader (alipsgh@gmail.com)
 *  @version $Revision: 7 $
 */
public class FHDDM extends AbstractChangeDetector {
        
    private static final long serialVersionUID = 1L;
    
    public IntOption slidingWinSizeOption = new IntOption("slidingWinSize",'s',"The size of Sliding Window.", 100, 0, Integer.MAX_VALUE);
    public FloatOption confidenceOption = new FloatOption("confidence", 'c', "The confidence level. The default value is E-6.", 0.000001, 0, 1);
    
    private int[] win;
    private int pointer;
    
    public static double delta;
    public static double epsilon;

    private int n_one;
    private double u_max;

    public FHDDM() {
        resetLearning();
    }

    @Override
    public void resetLearning() {
        win = new int[slidingWinSizeOption.getValue()];
        pointer = 0;
        delta = confidenceOption.getValue();
        epsilon = Math.sqrt((Math.log(1 / delta)) / (2 * win.length));
        u_max = 0;
        n_one = 0;
    }

    @Override
    public void input(double prediction) {
        
        if (this.isChangeDetected == true || this.isInitialized == false) {
            resetLearning();
            this.isInitialized = true;
        }
        
        boolean drift_status = false;
        boolean warning_status = false;
        
        if (pointer < win.length){
            win[pointer] = (prediction == 0) ? 1 : 0;
            n_one += win[pointer];
            pointer += 1;
        } else {
            n_one -= win[0];
            for (int i = 0; i < win.length; i++){
                if (i == win.length - 1){
                    win[i] = (prediction == 0) ? 1 : 0;
                    n_one += win[i];
                } else {
                    win[i] = win[i + 1];
                }
            }
        }
        
        if (pointer == win.length){
            double u = ((double) n_one) / win.length;
            u_max = (u_max < u) ? u : u_max;
            drift_status = (u_max - u > epsilon) ? true : false;
        }
        
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