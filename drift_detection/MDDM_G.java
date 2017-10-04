
package moa.classifiers.core.driftdetection;

import moa.classifiers.core.driftdetection.AbstractChangeDetector;
import moa.core.ObjectRepository;
import moa.options.FloatOption;
import moa.options.IntOption;
import moa.tasks.TaskMonitor;

/**
 * <p> McDiarmid Drift Detection Method - Geometric Scheme (MDDM_G) </p>
 * <p> A. Pesaranghader, H.L. Viktor, Eric Paquet, McDiarmid Drift Detection Methods for Evolving Data Streams. </p>
 *  
 * @author Ali Pesaranghader (apesaran@uottawa.ca, alipsgh@gmail.com)
 * @version $Revision: 7 $
 */
public class MDDM_G extends AbstractChangeDetector {
        
    private static final long serialVersionUID = 1L;
    
    public IntOption slidingWinSizeOption = new IntOption("slidingWinSize",'s',"The size of Sliding Window.", 100, 0, Integer.MAX_VALUE);
    public FloatOption ratioOption = new FloatOption("ratio", 'r', "The ratio between two consecutive elements in the window.", 1.01, 1, Double.MAX_VALUE);
    public FloatOption confidenceOption = new FloatOption("confidence", 'c', "The confidence level. The default value is E-6.", 0.000001, 0, 1);
    
    private int[] win;
    private int pointer;
    
    public static double ratio;
    public static double delta;
    public static double epsilon;
    
    private double u_max;

    public MDDM_G() {
        resetLearning();
    }

    @Override
    public void resetLearning() {
        win = new int[slidingWinSizeOption.getValue()];
        pointer = 0;
        ratio = ratioOption.getValue();
        delta = confidenceOption.getValue();          
        epsilon = Math.sqrt(0.5 * cal_sigma() * (Math.log(1 / delta)));
        u_max = 0;
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
            pointer += 1;
        } else {
            for (int i = 0; i < win.length; i++){
                if (i == win.length - 1){
                    win[i] = (prediction == 0) ? 1 : 0;
                } else {
                    win[i] = win[i + 1];
                }
            }
        }
        
        if (pointer == win.length){
            double u = cal_w_mean();
            u_max = (u_max < u) ? u : u_max;
            drift_status = (u_max - u > epsilon) ? true : false;
        }
        
        this.isWarningZone = warning_status;
        this.isChangeDetected = drift_status;
        
    }
    
    private double cal_sigma() {
        double sum = 0, bound_sum = 0, r = ratio;
        for (int i = 0; i < win.length; i++){
            sum += r;
            r *= ratio;
        }
        r = ratio;
        for (int i = 0; i < win.length; i++){
            bound_sum += Math.pow(r / sum, 2);
            r *= ratio;
        }
        return bound_sum;
    }

    private double cal_w_mean() {
        double total_sum = 0, win_sum = 0, r = ratio;
        for (int i = 0; i < win.length; i++){
            total_sum += r;
            win_sum += win[i] * r;
            r *= ratio;
        }
        return win_sum / total_sum;
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