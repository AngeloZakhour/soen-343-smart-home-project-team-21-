public class SHPObserver implements Observer{

    private String timer;
    private int multiplier;

    private boolean houseSafe = true;
    private boolean callCops = false;
    private boolean turnOffAwayMode = false;
    private boolean userAlerted = false;

    /**
     * Parameterised constructor.
     *
     * @param timer amount of time before calling the cops in case of an emergency.
     * @param multiplier the time multiplier
     */
    public SHPObserver(String timer, int multiplier){
        this.timer = timer;
        this.multiplier = multiplier;
    }

    /**
     * Getter for timer.
     *
     * @return the timer
     */
    public String getTimer(){
        return timer;
    }

    /**
     * Getter for the multiplier
     *
     * @return the multiplier
     */
    public int getMultiplier(){
        return multiplier;
    }

    /**
     * Sets up all the variables when the user decides to cancel the alert.
     */
    public void cancelAlert(){
        houseSafe = true;
        callCops = false;
        turnOffAwayMode = true;
    }

    /**
     * Sets up all the variables when the user decides to confirm the alert before the timer runs out.
     */
    public void confirmAlert(){
        callCops = true;
    }

    /**
     * Updates the observers by checking if there are people in the house when away mode is on.
     * (Needs to also check for automated lights)
     * @param o the <code>Observable</code> object that is observed by this <code>Observer</code>.
     */
    @Override
    public void update(Observable o) {
        if(o == null || o.getClass() != SmartHomeDashboard.class)
            return;

        SmartHomeDashboard shd = (SmartHomeDashboard)o;

        if(shd.isSomeoneHome() == UserTypes.STRANGER) houseSafe = false;
        else houseSafe = true;

        if(!houseSafe && !userAlerted){
            new AlertConfirmationWindow(this).setVisible(true);
            userAlerted = true;
        }

        if(callCops){
            shd.printToConsole("COPS ARE ON THEIR WAY!!!");
            callCops = false;
        }

        if(turnOffAwayMode){
            shd.disableAwayMode();
        }
    }
}
