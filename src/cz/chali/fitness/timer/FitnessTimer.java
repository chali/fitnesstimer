/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.chali.fitness.timer;

import cz.chali.fitness.timer.bean.Setting;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import org.netbeans.microedition.databinding.DataBinder;
import org.netbeans.microedition.databinding.DataSet;
import org.netbeans.microedition.databinding.lcdui.TextFieldBindingProvider;

/**
 * @author chali
 */
public class FitnessTimer extends MIDlet implements CommandListener {

    private final static byte TIME_TYPE_STARTUP = 1;
    private final static byte TIME_TYPE_WORKOUT = 2;
    private final static byte TIME_TYPE_REST = 3;
    private boolean midletPaused = false;
    private RecordStore actualSettingRecortStore;
    private Setting actualSettingBean;
    private long actualTime;
    private byte actualTimeType;
    private byte actualRound;
    private Timer timer = new Timer();
    private TimerTask counter;
    private boolean isTimmerRuning = false;
    private Player player;
    //<editor-fold defaultstate="collapsed" desc=" Generated Fields ">//GEN-BEGIN:|fields|0|
    private Form mainMenu;
    private StringItem time;
    private StringItem round;
    private StringItem timeType;
    private List options;
    private Form timesForm;
    private TextField workoutTime;
    private TextField restTime;
    private TextField rounds;
    private TextField startupTime;
    private Alert restart;
    private Alert validationError;
    private Command optionsCommand;
    private Command backCommand;
    private Command exitCommand;
    private Command cancelTimesFormCommand;
    private Command okTimesFormCommand;
    private Command startCommnad;
    private Command restartCommand;
    private Command stopCommand;
    private Command cancelRestartCommand;
    private Command okRestartCommand;
    private DataSet actualSetting;
    //</editor-fold>//GEN-END:|fields|0|

    /**
     * The FitnessTimer constructor.
     */
    public FitnessTimer() {
    }

    //<editor-fold defaultstate="collapsed" desc=" Generated Methods ">//GEN-BEGIN:|methods|0|
    //</editor-fold>//GEN-END:|methods|0|
    //<editor-fold defaultstate="collapsed" desc=" Generated Method: initialize ">//GEN-BEGIN:|0-initialize|0|0-preInitialize
    /**
     * Initilizes the application.
     * It is called only once when the MIDlet is started. The method is called before the <code>startMIDlet</code> method.
     */
    private void initialize() {//GEN-END:|0-initialize|0|0-preInitialize
        try {
            // write pre-initialize user code here
            actualSettingBean = new Setting();
            actualSettingRecortStore = RecordStore.openRecordStore("actualSetting", true);
            if (actualSettingRecortStore.getNumRecords() > 0) {
                DataInputStream stream = new DataInputStream(new ByteArrayInputStream(actualSettingRecortStore.getRecord(1)));
                actualSettingBean.setStartupTime(stream.readUTF());
                actualSettingBean.setWorkoutTime(stream.readUTF());
                actualSettingBean.setRestTime(stream.readUTF());
                actualSettingBean.setRounds(stream.readUTF());
            } else {
                actualSettingBean.setStartupTime("0:05");
                actualSettingBean.setWorkoutTime("0:10");
                actualSettingBean.setRestTime("0:05");
                actualSettingBean.setRounds("3");
            }
            InputStream is = getClass().getResourceAsStream("alert.mp3");
            player = Manager.createPlayer(is, "audio/mpeg");
            initTimer();
            getTime().setText(convertTimeFromLong(actualTime));
            getRound().setText(String.valueOf(actualRound));
            setTimeTypeStringItem();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (RecordStoreException ex) {
            ex.printStackTrace();
        } catch (MediaException me) {
            me.printStackTrace();
        }
//GEN-LINE:|0-initialize|1|0-postInitialize
        // write post-initialize user code here
    }//GEN-BEGIN:|0-initialize|2|
    //</editor-fold>//GEN-END:|0-initialize|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Method: startMIDlet ">//GEN-BEGIN:|3-startMIDlet|0|3-preAction
    /**
     * Performs an action assigned to the Mobile Device - MIDlet Started point.
     */
    public void startMIDlet() {//GEN-END:|3-startMIDlet|0|3-preAction
        // write pre-action user code here
        switchDisplayable(null, getMainMenu());//GEN-LINE:|3-startMIDlet|1|3-postAction
        // write post-action user code here
    }//GEN-BEGIN:|3-startMIDlet|2|
    //</editor-fold>//GEN-END:|3-startMIDlet|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Method: resumeMIDlet ">//GEN-BEGIN:|4-resumeMIDlet|0|4-preAction
    /**
     * Performs an action assigned to the Mobile Device - MIDlet Resumed point.
     */
    public void resumeMIDlet() {//GEN-END:|4-resumeMIDlet|0|4-preAction
        // write pre-action user code here
//GEN-LINE:|4-resumeMIDlet|1|4-postAction
        // write post-action user code here
    }//GEN-BEGIN:|4-resumeMIDlet|2|
    //</editor-fold>//GEN-END:|4-resumeMIDlet|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Method: switchDisplayable ">//GEN-BEGIN:|5-switchDisplayable|0|5-preSwitch
    /**
     * Switches a current displayable in a display. The <code>display</code> instance is taken from <code>getDisplay</code> method. This method is used by all actions in the design for switching displayable.
     * @param alert the Alert which is temporarily set to the display; if <code>null</code>, then <code>nextDisplayable</code> is set immediately
     * @param nextDisplayable the Displayable to be set
     */
    public void switchDisplayable(Alert alert, Displayable nextDisplayable) {//GEN-END:|5-switchDisplayable|0|5-preSwitch
        // write pre-switch user code here
        Display display = getDisplay();//GEN-BEGIN:|5-switchDisplayable|1|5-postSwitch
        if (alert == null) {
            display.setCurrent(nextDisplayable);
        } else {
            display.setCurrent(alert, nextDisplayable);
        }//GEN-END:|5-switchDisplayable|1|5-postSwitch
        // write post-switch user code here
    }//GEN-BEGIN:|5-switchDisplayable|2|
    //</editor-fold>//GEN-END:|5-switchDisplayable|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Method: commandAction for Displayables ">//GEN-BEGIN:|7-commandAction|0|7-preCommandAction
    /**
     * Called by a system to indicated that a command has been invoked on a particular displayable.
     * @param command the Command that was invoked
     * @param displayable the Displayable where the command was invoked
     */
    public void commandAction(Command command, Displayable displayable) {//GEN-END:|7-commandAction|0|7-preCommandAction
        // write pre-action user code here
        if (displayable == mainMenu) {//GEN-BEGIN:|7-commandAction|1|27-preAction
            if (command == exitCommand) {//GEN-END:|7-commandAction|1|27-preAction
                // write pre-action user code here
                exitMIDlet();//GEN-LINE:|7-commandAction|2|27-postAction
                // write post-action user code here
            } else if (command == optionsCommand) {//GEN-LINE:|7-commandAction|3|16-preAction
                // write pre-action user code here
                switchDisplayable(null, getOptions());//GEN-LINE:|7-commandAction|4|16-postAction
                // write post-action user code here
            } else if (command == restartCommand) {//GEN-LINE:|7-commandAction|5|99-preAction
                // write pre-action user code here
                switchDisplayable(null, getRestart());//GEN-LINE:|7-commandAction|6|99-postAction
                // write post-action user code here
                restart();
            } else if (command == startCommnad) {//GEN-LINE:|7-commandAction|7|95-preAction
                // write pre-action user code here
                switchDisplayable(null, getMainMenu());//GEN-LINE:|7-commandAction|8|95-postAction
                // write post-action user code here
                start();
            } else if (command == stopCommand) {//GEN-LINE:|7-commandAction|9|97-preAction
                // write pre-action user code here
                switchDisplayable(null, getMainMenu());//GEN-LINE:|7-commandAction|10|97-postAction
                // write post-action user code here
                stop();
            }//GEN-BEGIN:|7-commandAction|11|20-preAction
        } else if (displayable == options) {
            if (command == List.SELECT_COMMAND) {//GEN-END:|7-commandAction|11|20-preAction
                // write pre-action user code here
                optionsAction();//GEN-LINE:|7-commandAction|12|20-postAction
                // write post-action user code here
            } else if (command == backCommand) {//GEN-LINE:|7-commandAction|13|24-preAction
                // write pre-action user code here
                switchDisplayable(null, getMainMenu());//GEN-LINE:|7-commandAction|14|24-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|15|110-preAction
        } else if (displayable == restart) {
            if (command == cancelRestartCommand) {//GEN-END:|7-commandAction|15|110-preAction
                // write pre-action user code here
                switchDisplayable(null, getMainMenu());//GEN-LINE:|7-commandAction|16|110-postAction
                // write post-action user code here
            } else if (command == okRestartCommand) {//GEN-LINE:|7-commandAction|17|118-preAction
                // write pre-action user code here
                switchDisplayable(null, getMainMenu());//GEN-LINE:|7-commandAction|18|118-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|19|48-preAction
        } else if (displayable == timesForm) {
            if (command == cancelTimesFormCommand) {//GEN-END:|7-commandAction|19|48-preAction
                // write pre-action user code here
                switchDisplayable(null, getOptions());//GEN-LINE:|7-commandAction|20|48-postAction
                // write post-action user code here
            } else if (command == okTimesFormCommand) {//GEN-LINE:|7-commandAction|21|51-preAction
                // write pre-action user code here
                DataBinder.writeValue("actualSetting.workoutTime",getWorkoutTime().getString());//GEN-BEGIN:|7-commandAction|22|51-postAction
                DataBinder.writeValue("actualSetting.restTime",getRestTime().getString());
                DataBinder.writeValue("actualSetting.rounds",getRounds().getString());
                DataBinder.writeValue("actualSetting.startupTime",getStartupTime().getString());
                validateTimesForm();//GEN-END:|7-commandAction|22|51-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|23|7-postCommandAction
        }//GEN-END:|7-commandAction|23|7-postCommandAction
        // write post-action user code here
    }//GEN-BEGIN:|7-commandAction|24|
    //</editor-fold>//GEN-END:|7-commandAction|24|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: mainMenu ">//GEN-BEGIN:|14-getter|0|14-preInit
    /**
     * Returns an initiliazed instance of mainMenu component.
     * @return the initialized component instance
     */
    public Form getMainMenu() {
        if (mainMenu == null) {//GEN-END:|14-getter|0|14-preInit
            // write pre-init user code here
            mainMenu = new Form("Fitness Timer", new Item[] { getTime(), getRound(), getTimeType() });//GEN-BEGIN:|14-getter|1|14-postInit
            mainMenu.addCommand(getOptionsCommand());
            mainMenu.addCommand(getExitCommand());
            mainMenu.addCommand(getStartCommnad());
            mainMenu.addCommand(getStopCommand());
            mainMenu.addCommand(getRestartCommand());
            mainMenu.setCommandListener(this);//GEN-END:|14-getter|1|14-postInit
            // write post-init user code here
        }//GEN-BEGIN:|14-getter|2|
        return mainMenu;
    }
    //</editor-fold>//GEN-END:|14-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: options ">//GEN-BEGIN:|18-getter|0|18-preInit
    /**
     * Returns an initiliazed instance of options component.
     * @return the initialized component instance
     */
    public List getOptions() {
        if (options == null) {//GEN-END:|18-getter|0|18-preInit
            // write pre-init user code here
            options = new List("Options", Choice.IMPLICIT);//GEN-BEGIN:|18-getter|1|18-postInit
            options.append("Times", null);
            options.addCommand(getBackCommand());
            options.setCommandListener(this);
            options.setSelectedFlags(new boolean[] { false });//GEN-END:|18-getter|1|18-postInit
            // write post-init user code here
        }//GEN-BEGIN:|18-getter|2|
        return options;
    }
    //</editor-fold>//GEN-END:|18-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Method: optionsAction ">//GEN-BEGIN:|18-action|0|18-preAction
    /**
     * Performs an action assigned to the selected list element in the options component.
     */
    public void optionsAction() {//GEN-END:|18-action|0|18-preAction
        // enter pre-action user code here
        String __selectedString = getOptions().getString(getOptions().getSelectedIndex());//GEN-BEGIN:|18-action|1|29-preAction
        if (__selectedString != null) {
            if (__selectedString.equals("Times")) {//GEN-END:|18-action|1|29-preAction
                // write pre-action user code here
                switchDisplayable(null, getTimesForm());//GEN-LINE:|18-action|2|29-postAction
                // write post-action user code here
            }//GEN-BEGIN:|18-action|3|18-postAction
        }//GEN-END:|18-action|3|18-postAction
        // enter post-action user code here
    }//GEN-BEGIN:|18-action|4|
    //</editor-fold>//GEN-END:|18-action|4|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: optionsCommand ">//GEN-BEGIN:|15-getter|0|15-preInit
    /**
     * Returns an initiliazed instance of optionsCommand component.
     * @return the initialized component instance
     */
    public Command getOptionsCommand() {
        if (optionsCommand == null) {//GEN-END:|15-getter|0|15-preInit
            // write pre-init user code here
            optionsCommand = new Command("Options", Command.SCREEN, 0);//GEN-LINE:|15-getter|1|15-postInit
            // write post-init user code here
        }//GEN-BEGIN:|15-getter|2|
        return optionsCommand;
    }
    //</editor-fold>//GEN-END:|15-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: backCommand ">//GEN-BEGIN:|23-getter|0|23-preInit
    /**
     * Returns an initiliazed instance of backCommand component.
     * @return the initialized component instance
     */
    public Command getBackCommand() {
        if (backCommand == null) {//GEN-END:|23-getter|0|23-preInit
            // write pre-init user code here
            backCommand = new Command("Back", Command.BACK, 0);//GEN-LINE:|23-getter|1|23-postInit
            // write post-init user code here
        }//GEN-BEGIN:|23-getter|2|
        return backCommand;
    }
    //</editor-fold>//GEN-END:|23-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: exitCommand ">//GEN-BEGIN:|26-getter|0|26-preInit
    /**
     * Returns an initiliazed instance of exitCommand component.
     * @return the initialized component instance
     */
    public Command getExitCommand() {
        if (exitCommand == null) {//GEN-END:|26-getter|0|26-preInit
            // write pre-init user code here
            exitCommand = new Command("Exit", Command.EXIT, 0);//GEN-LINE:|26-getter|1|26-postInit
            // write post-init user code here
        }//GEN-BEGIN:|26-getter|2|
        return exitCommand;
    }
    //</editor-fold>//GEN-END:|26-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: timesForm ">//GEN-BEGIN:|39-getter|0|39-preInit
    /**
     * Returns an initiliazed instance of timesForm component.
     * @return the initialized component instance
     */
    public Form getTimesForm() {
        if (timesForm == null) {//GEN-END:|39-getter|0|39-preInit
            // write pre-init user code here
            timesForm = new Form("Times", new Item[] { getStartupTime(), getWorkoutTime(), getRestTime(), getRounds() });//GEN-BEGIN:|39-getter|1|39-postInit
            timesForm.addCommand(getCancelTimesFormCommand());
            timesForm.addCommand(getOkTimesFormCommand());
            timesForm.setCommandListener(this);//GEN-END:|39-getter|1|39-postInit
            // write post-init user code here
        }//GEN-BEGIN:|39-getter|2|
        return timesForm;
    }
    //</editor-fold>//GEN-END:|39-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: cancelTimesFormCommand ">//GEN-BEGIN:|47-getter|0|47-preInit
    /**
     * Returns an initiliazed instance of cancelTimesFormCommand component.
     * @return the initialized component instance
     */
    public Command getCancelTimesFormCommand() {
        if (cancelTimesFormCommand == null) {//GEN-END:|47-getter|0|47-preInit
            // write pre-init user code here
            cancelTimesFormCommand = new Command("Cancel", Command.CANCEL, 0);//GEN-LINE:|47-getter|1|47-postInit
            // write post-init user code here
        }//GEN-BEGIN:|47-getter|2|
        return cancelTimesFormCommand;
    }
    //</editor-fold>//GEN-END:|47-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: okTimesFormCommand ">//GEN-BEGIN:|50-getter|0|50-preInit
    /**
     * Returns an initiliazed instance of okTimesFormCommand component.
     * @return the initialized component instance
     */
    public Command getOkTimesFormCommand() {
        if (okTimesFormCommand == null) {//GEN-END:|50-getter|0|50-preInit
            // write pre-init user code here
            okTimesFormCommand = new Command("Ok", Command.OK, 0);//GEN-LINE:|50-getter|1|50-postInit
            // write post-init user code here
        }//GEN-BEGIN:|50-getter|2|
        return okTimesFormCommand;
    }
    //</editor-fold>//GEN-END:|50-getter|2|
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: startupTime ">//GEN-BEGIN:|75-getter|0|75-preInit
    /**
     * Returns an initiliazed instance of startupTime component.
     * @return the initialized component instance
     */
    public TextField getStartupTime() {
        if (startupTime == null) {//GEN-END:|75-getter|0|75-preInit
            // write pre-init user code here
            startupTime = new TextField("Startup time", null, 32, TextField.ANY);//GEN-BEGIN:|75-getter|1|75-postInit
            DataBinder.registerDataSet(getActualSetting(), "actualSetting");
            DataBinder.bind("actualSetting.startupTime", new TextFieldBindingProvider(), getStartupTime(), new TextFieldBindingProvider.FeatureText(false));//GEN-END:|75-getter|1|75-postInit
            // write post-init user code here
        }//GEN-BEGIN:|75-getter|2|
        return startupTime;
    }
    //</editor-fold>//GEN-END:|75-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: workoutTime ">//GEN-BEGIN:|76-getter|0|76-preInit
    /**
     * Returns an initiliazed instance of workoutTime component.
     * @return the initialized component instance
     */
    public TextField getWorkoutTime() {
        if (workoutTime == null) {//GEN-END:|76-getter|0|76-preInit
            // write pre-init user code here
            workoutTime = new TextField("Workout time", null, 32, TextField.ANY);//GEN-BEGIN:|76-getter|1|76-postInit
            DataBinder.registerDataSet(getActualSetting(), "actualSetting");
            DataBinder.bind("actualSetting.workoutTime", new TextFieldBindingProvider(), getWorkoutTime(), new TextFieldBindingProvider.FeatureText(false));//GEN-END:|76-getter|1|76-postInit
            // write post-init user code here
        }//GEN-BEGIN:|76-getter|2|
        return workoutTime;
    }
    //</editor-fold>//GEN-END:|76-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: restTime ">//GEN-BEGIN:|77-getter|0|77-preInit
    /**
     * Returns an initiliazed instance of restTime component.
     * @return the initialized component instance
     */
    public TextField getRestTime() {
        if (restTime == null) {//GEN-END:|77-getter|0|77-preInit
            // write pre-init user code here
            restTime = new TextField("Rest time", null, 32, TextField.ANY);//GEN-BEGIN:|77-getter|1|77-postInit
            DataBinder.registerDataSet(getActualSetting(), "actualSetting");
            DataBinder.bind("actualSetting.restTime", new TextFieldBindingProvider(), getRestTime(), new TextFieldBindingProvider.FeatureText(false));//GEN-END:|77-getter|1|77-postInit
            // write post-init user code here
        }//GEN-BEGIN:|77-getter|2|
        return restTime;
    }
    //</editor-fold>//GEN-END:|77-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: rounds ">//GEN-BEGIN:|78-getter|0|78-preInit
    /**
     * Returns an initiliazed instance of rounds component.
     * @return the initialized component instance
     */
    public TextField getRounds() {
        if (rounds == null) {//GEN-END:|78-getter|0|78-preInit
            // write pre-init user code here
            rounds = new TextField("Rounds", null, 32, TextField.DECIMAL);//GEN-BEGIN:|78-getter|1|78-postInit
            DataBinder.registerDataSet(getActualSetting(), "actualSetting");
            DataBinder.bind("actualSetting.rounds", new TextFieldBindingProvider(), getRounds(), new TextFieldBindingProvider.FeatureText(false));//GEN-END:|78-getter|1|78-postInit
            // write post-init user code here
        }//GEN-BEGIN:|78-getter|2|
        return rounds;
    }
    //</editor-fold>//GEN-END:|78-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: actualSetting ">//GEN-BEGIN:|80-getter|0|80-preInit
    /**
     * Returns an initiliazed instance of actualSetting component.
     * @return the initialized component instance
     */
    public DataSet getActualSetting() {
        if (actualSetting == null) {//GEN-END:|80-getter|0|80-preInit
            // write pre-init user code here
            actualSetting = new ActualSetting();//GEN-LINE:|80-getter|1|80-postInit
            // write post-init user code here
        }//GEN-BEGIN:|80-getter|2|80codeZone1
        return actualSetting;
    }
    private class ActualSetting implements DataSet {//GEN-END:|80-getter|2|80codeZone1
        // declaration field

        public Class getType(String name) throws IllegalStateException {//GEN-LINE:|80-getter|3|80codeZone2
            return String.class;
        }//GEN-LINE:|80-getter|4|80codeZone3

        public Object getValue(String name) throws IllegalStateException {//GEN-LINE:|80-getter|5|80codeZone4
            if (name.equals("startupTime")) {
                return actualSettingBean.getStartupTime();
            } else if (name.equals("workoutTime")) {
                return actualSettingBean.getWorkoutTime();
            } else if (name.equals("restTime")) {
                return actualSettingBean.getRestTime();
            } else if (name.equals("rounds")) {
                return actualSettingBean.getRounds();
            }
            throw new IllegalStateException("Uknow property name.");
        }//GEN-LINE:|80-getter|6|80codeZone5
        private void setValueInternal(String name, String value) {
            if (name.equals("startupTime")) {
                actualSettingBean.setStartupTime(value);
                return;
            } else if (name.equals("workoutTime")) {
                actualSettingBean.setWorkoutTime(value);
                return;
            } else if (name.equals("restTime")) {
                actualSettingBean.setRestTime(value);
                return;
            } else if (name.equals("rounds")) {
                actualSettingBean.setRounds(value);
                return;
            }
            throw new IllegalStateException("Unknow property name: " + name);

        }
        public void setValue(String name, Object value) throws IllegalStateException {//GEN-LINE:|80-getter|7|80codeZone6
            if (value instanceof String) {
                setValueInternal(name, (String) value);
            } else {
                throw new IllegalStateException("Uknow property type.");
            }
            DataBinder.fireDataSetChanged(this, name);

        }//GEN-LINE:|80-getter|8|80codeZone7

        public void setAsString(String name, String stringValue) throws IllegalStateException {//GEN-LINE:|80-getter|9|80codeZone8
            setValueInternal(name, stringValue);
            DataBinder.fireDataSetChanged(this, name);
        }//GEN-LINE:|80-getter|10|80codeZone9

        public boolean isReadOnly(String readOnly) throws IllegalStateException {//GEN-LINE:|80-getter|11|80codeZone10
            return false;
        }//GEN-LINE:|80-getter|12|80codeZone11
    };//GEN-LINE:|80-getter|13|80end
    //</editor-fold>//GEN-LINE:|80-getter|14|















    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: time ">//GEN-BEGIN:|92-getter|0|92-preInit
    /**
     * Returns an initiliazed instance of time component.
     * @return the initialized component instance
     */
    public StringItem getTime() {
        if (time == null) {//GEN-END:|92-getter|0|92-preInit
            // write pre-init user code here
            time = new StringItem("Time", "", Item.PLAIN);//GEN-LINE:|92-getter|1|92-postInit
            // write post-init user code here
        }//GEN-BEGIN:|92-getter|2|
        return time;
    }
    //</editor-fold>//GEN-END:|92-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: startCommnad ">//GEN-BEGIN:|94-getter|0|94-preInit
    /**
     * Returns an initiliazed instance of startCommnad component.
     * @return the initialized component instance
     */
    public Command getStartCommnad() {
        if (startCommnad == null) {//GEN-END:|94-getter|0|94-preInit
            // write pre-init user code here
            startCommnad = new Command("Start", Command.OK, 0);//GEN-LINE:|94-getter|1|94-postInit
            // write post-init user code here
        }//GEN-BEGIN:|94-getter|2|
        return startCommnad;
    }
    //</editor-fold>//GEN-END:|94-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: stopCommand ">//GEN-BEGIN:|96-getter|0|96-preInit
    /**
     * Returns an initiliazed instance of stopCommand component.
     * @return the initialized component instance
     */
    public Command getStopCommand() {
        if (stopCommand == null) {//GEN-END:|96-getter|0|96-preInit
            // write pre-init user code here
            stopCommand = new Command("Stop", Command.STOP, 0);//GEN-LINE:|96-getter|1|96-postInit
            // write post-init user code here
        }//GEN-BEGIN:|96-getter|2|
        return stopCommand;
    }
    //</editor-fold>//GEN-END:|96-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: restartCommand ">//GEN-BEGIN:|98-getter|0|98-preInit
    /**
     * Returns an initiliazed instance of restartCommand component.
     * @return the initialized component instance
     */
    public Command getRestartCommand() {
        if (restartCommand == null) {//GEN-END:|98-getter|0|98-preInit
            // write pre-init user code here
            restartCommand = new Command("Restart", Command.SCREEN, 0);//GEN-LINE:|98-getter|1|98-postInit
            // write post-init user code here
        }//GEN-BEGIN:|98-getter|2|
        return restartCommand;
    }
    //</editor-fold>//GEN-END:|98-getter|2|
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: restart ">//GEN-BEGIN:|101-getter|0|101-preInit
    /**
     * Returns an initiliazed instance of restart component.
     * @return the initialized component instance
     */
    public Alert getRestart() {
        if (restart == null) {//GEN-END:|101-getter|0|101-preInit
            // write pre-init user code here
            restart = new Alert("Restart?", "Restart timer", null, null);//GEN-BEGIN:|101-getter|1|101-postInit
            restart.addCommand(getCancelRestartCommand());
            restart.addCommand(getOkRestartCommand());
            restart.setCommandListener(this);
            restart.setTimeout(Alert.FOREVER);//GEN-END:|101-getter|1|101-postInit
            // write post-init user code here
        }//GEN-BEGIN:|101-getter|2|
        return restart;
    }
    //</editor-fold>//GEN-END:|101-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: cancelRestartCommand ">//GEN-BEGIN:|109-getter|0|109-preInit
    /**
     * Returns an initiliazed instance of cancelRestartCommand component.
     * @return the initialized component instance
     */
    public Command getCancelRestartCommand() {
        if (cancelRestartCommand == null) {//GEN-END:|109-getter|0|109-preInit
            // write pre-init user code here
            cancelRestartCommand = new Command("No", Command.CANCEL, 0);//GEN-LINE:|109-getter|1|109-postInit
            // write post-init user code here
        }//GEN-BEGIN:|109-getter|2|
        return cancelRestartCommand;
    }
    //</editor-fold>//GEN-END:|109-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: okRestartCommand ">//GEN-BEGIN:|117-getter|0|117-preInit
    /**
     * Returns an initiliazed instance of okRestartCommand component.
     * @return the initialized component instance
     */
    public Command getOkRestartCommand() {
        if (okRestartCommand == null) {//GEN-END:|117-getter|0|117-preInit
            // write pre-init user code here
            okRestartCommand = new Command("Yes", Command.OK, 0);//GEN-LINE:|117-getter|1|117-postInit
            // write post-init user code here
        }//GEN-BEGIN:|117-getter|2|
        return okRestartCommand;
    }
    //</editor-fold>//GEN-END:|117-getter|2|
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>


    //</editor-fold>
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: round ">//GEN-BEGIN:|123-getter|0|123-preInit
    /**
     * Returns an initiliazed instance of round component.
     * @return the initialized component instance
     */
    public StringItem getRound() {
        if (round == null) {//GEN-END:|123-getter|0|123-preInit
            // write pre-init user code here
            round = new StringItem("Round", "");//GEN-LINE:|123-getter|1|123-postInit
            // write post-init user code here
        }//GEN-BEGIN:|123-getter|2|
        return round;
    }
    //</editor-fold>//GEN-END:|123-getter|2|
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: timeType ">//GEN-BEGIN:|124-getter|0|124-preInit
    /**
     * Returns an initiliazed instance of timeType component.
     * @return the initialized component instance
     */
    public StringItem getTimeType() {
        if (timeType == null) {//GEN-END:|124-getter|0|124-preInit
            // write pre-init user code here
            timeType = new StringItem("Type", "");//GEN-LINE:|124-getter|1|124-postInit
            // write post-init user code here
        }//GEN-BEGIN:|124-getter|2|
        return timeType;
    }
    //</editor-fold>//GEN-END:|124-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Method: validateTimesForm ">//GEN-BEGIN:|127-if|0|127-preIf
    /**
     * Performs an action assigned to the validateTimesForm if-point.
     */
    public void validateTimesForm() {//GEN-END:|127-if|0|127-preIf
        // enter pre-if user code here
        boolean validationResult = true;
        validationResult = validationResult && checkTimeFormat(actualSettingBean.getStartupTime());
        validationResult = validationResult && checkTimeFormat(actualSettingBean.getWorkoutTime());
        validationResult = validationResult && checkTimeFormat(actualSettingBean.getRestTime());
        if (validationResult) {//GEN-LINE:|127-if|1|128-preAction
            // write pre-action user code here
            saveSetting();
            switchDisplayable(null, getOptions());//GEN-LINE:|127-if|2|128-postAction
            // write post-action user code here
        } else {//GEN-LINE:|127-if|3|129-preAction
            // write pre-action user code here
            switchDisplayable(getValidationError(), getTimesForm());//GEN-LINE:|127-if|4|129-postAction
            // write post-action user code here
        }//GEN-LINE:|127-if|5|127-postIf
        // enter post-if user code here
    }//GEN-BEGIN:|127-if|6|
    //</editor-fold>//GEN-END:|127-if|6|
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: validationError ">//GEN-BEGIN:|126-getter|0|126-preInit
    /**
     * Returns an initiliazed instance of validationError component.
     * @return the initialized component instance
     */
    public Alert getValidationError() {
        if (validationError == null) {//GEN-END:|126-getter|0|126-preInit
            // write pre-init user code here
            validationError = new Alert("Bad time format", "Bad time format", null, AlertType.ERROR);//GEN-BEGIN:|126-getter|1|126-postInit
            validationError.setTimeout(Alert.FOREVER);//GEN-END:|126-getter|1|126-postInit
            // write post-init user code here
        }//GEN-BEGIN:|126-getter|2|
        return validationError;
    }
    //</editor-fold>//GEN-END:|126-getter|2|



    /**
     * Returns a display instance.
     * @return the display instance.
     */
    public Display getDisplay() {
        return Display.getDisplay(this);
    }

    /**
     * Exits MIDlet.
     */
    public void exitMIDlet() {
        switchDisplayable(null, null);
        destroyApp(true);
        notifyDestroyed();
    }

    /**
     * Called when MIDlet is started.
     * Checks whether the MIDlet have been already started and initialize/starts or resumes the MIDlet.
     */
    public void startApp() {
        if (midletPaused) {
            resumeMIDlet();
        } else {
            initialize();
            startMIDlet();
        }
        midletPaused = false;
    }

    /**
     * Called when MIDlet is paused.
     */
    public void pauseApp() {
        midletPaused = true;
    }

    /**
     * Called to signal the MIDlet to terminate.
     * @param unconditional if true, then the MIDlet has to be unconditionally terminated and all resources has to be released.
     */
    public void destroyApp(boolean unconditional) {
        try {
            actualSettingRecortStore.closeRecordStore();
        } catch (RecordStoreException ex) {
            ex.printStackTrace();
        }
    }

    private void saveSetting() {
        try {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            DataOutputStream stream = new DataOutputStream(byteStream);
            stream.writeUTF(actualSettingBean.getStartupTime());
            stream.writeUTF(actualSettingBean.getWorkoutTime());
            stream.writeUTF(actualSettingBean.getRestTime());
            stream.writeUTF(actualSettingBean.getRounds());
            if (actualSettingRecortStore.getNumRecords() > 0) {
                actualSettingRecortStore.setRecord(1, byteStream.toByteArray(), 0, byteStream.size());
            } else {
                actualSettingRecortStore.addRecord(byteStream.toByteArray(), 0, byteStream.size());
            }
            initTimer();
            getTime().setText(convertTimeFromLong(actualTime));
            getRound().setText(String.valueOf(actualRound));
            setTimeTypeStringItem();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (RecordStoreException ex) {
            ex.printStackTrace();
        }
    }

    private void initTimer() {
        if (actualSettingBean.getStartupTimeAsLong() > 0){
            actualTime = actualSettingBean.getStartupTimeAsLong();
            actualRound = 0;
            actualTimeType = TIME_TYPE_STARTUP;
        }
        else if (actualSettingBean.getWorkoutTimeAsLong() > 0){
            actualTime = actualSettingBean.getWorkoutTimeAsLong();
            actualRound = 1;
            actualTimeType = TIME_TYPE_WORKOUT;
        }
        else if (actualSettingBean.getRestTimeAsLong() > 0){
            actualTime = actualSettingBean.getRestTimeAsLong();
            actualRound = 1;
            actualTimeType = TIME_TYPE_REST;
        }
        else {
            actualTime = 0;
            actualRound = 0;
            actualTimeType = TIME_TYPE_STARTUP;
        }
        
    }

    private void start() {
        if (!isTimmerRuning) {
            isTimmerRuning = true;
            getTime().setText(convertTimeFromLong(actualTime));
            getRound().setText(String.valueOf(actualRound));
            setTimeTypeStringItem();
            counter = new TimerTask() {

                public void run() {
                    //dokoncil pocitani a muzou nastan nasledujici situace
                    boolean stop = false;
                    if (actualTime == 0) {
                        makeAlert();
                        if (actualTimeType == TIME_TYPE_STARTUP) {
                            stop = handleStarupEnd();
                        } else if (actualTimeType == TIME_TYPE_WORKOUT) {
                            stop = handleWorkoutEnd();
                        } else if (actualTimeType == TIME_TYPE_REST) {
                            stop = handleRestEnd();
                        }
                        getRound().setText(String.valueOf(actualRound));
                        setTimeTypeStringItem();

                    }
                    getTime().setText(convertTimeFromLong(actualTime));
                    actualTime -= 1000;
                    if (stop) {
                        isTimmerRuning = false;
                        this.cancel();
                        initTimer();
                    }
                }

                private boolean handleStarupEnd() {
                    //mame nastavenou zatez a kola, nastavime tedy cas zateze a prvni kolo
                    if (actualSettingBean.getWorkoutTimeAsLong() > 0 && actualSettingBean.getRoundsAsInt() > 0) {
                        actualTime = actualSettingBean.getWorkoutTimeAsLong();
                        actualTimeType = TIME_TYPE_WORKOUT;
                        actualRound = 1;
                        return false;
                    } //zatez nemame ale mame nastaveny cas odpocinku
                    else if (actualSettingBean.getRestTimeAsLong() > 0 && actualSettingBean.getRoundsAsInt() > 0) {
                        actualTime = actualSettingBean.getRestTimeAsLong();
                        actualTimeType = TIME_TYPE_REST;
                        actualRound = 1;
                        return false;
                    } //nemame ani jeden cas koncime
                    else {
                        return true;
                    }
                }

                private boolean handleWorkoutEnd() {
                    //konrola nastaveni casu odpocinku a jeho nastaveni
                    if (actualSettingBean.getRestTimeAsLong() > 0) {
                        actualTime = actualSettingBean.getRestTimeAsLong();
                        actualTimeType = TIME_TYPE_REST;
                        return false;
                    } //pokud nemame nastaveny odpocinek zacne dalsi zatez jeslti zbyvaji jeste kola
                    else if (actualSettingBean.getRoundsAsInt() > actualRound) {
                        actualTime = actualSettingBean.getWorkoutTimeAsLong();
                        actualTimeType = TIME_TYPE_WORKOUT;
                        actualRound++;
                        return false;
                    } else {
                        return true;
                    }
                }

                private boolean handleRestEnd() {
                    //mame nastavenou zatez a zbyvaji kola, nastavime tedy cas zateze a dalsi kolo
                    if (actualSettingBean.getWorkoutTimeAsLong() > 0 && actualSettingBean.getRoundsAsInt() > actualRound) {
                        actualTime = actualSettingBean.getWorkoutTimeAsLong();
                        actualTimeType = TIME_TYPE_WORKOUT;
                        actualRound++;
                        return false;
                    } //pokud nemame nastavenu zatez zacne dalsi odpocinek jeslti zbyvaji jeste kola
                    else if (actualSettingBean.getRoundsAsInt() > actualRound) {
                        actualTime = actualSettingBean.getRestTimeAsLong();
                        actualTimeType = TIME_TYPE_REST;
                        actualRound++;
                        return false;
                    } else {
                        return true;
                    }
                }
            };
            timer.scheduleAtFixedRate(counter, 0, 1000);
        }
    }

    private boolean checkTimeFormat(String time){
        //mame to delsi nez 5:00 nebo 50:00
        if (time.length()!=4 && time.length() != 5)
            return false;
        //konrola pozic dvojtecek
        if (time.length() == 4 && time.indexOf(':')!=1)
            return false;
        if (time.length() == 5 && time.indexOf(':')!=2)
            return false;
        //mame vice dvojtecek
        if (time.indexOf(":")!=time.lastIndexOf(':'))
            return false;
        //kontrola zda na svych pozicich jsou cisla a ve prostred jeste muze byt dvojtecka
        for (int i=0; i<time.length(); i++){
            if (!Character.isDigit(time.charAt(i)) && time.charAt(i)!=':')
                return false;
        }
        return true;
    }

    private String convertTimeFromLong(long time) {
        long minute = time / (1000 * 60);
        long seconds = (time % (1000 * 60)) / 1000;
        String secondsString = String.valueOf(seconds);
        if (secondsString.length() < 2) {
            secondsString = "0".concat(secondsString);
        }
        return String.valueOf(minute).concat(":").concat(secondsString);
    }

    private void setTimeTypeStringItem() {
        if (actualTimeType == TIME_TYPE_STARTUP) {
            getTimeType().setText("Startup");
        } else if (actualTimeType == TIME_TYPE_WORKOUT) {
            getTimeType().setText("Workout");
        } else if (actualTimeType == TIME_TYPE_REST) {
            getTimeType().setText("Rest");
        }
    }

    private void stop() {
        if (counter != null) {
            isTimmerRuning = false;
            counter.cancel();
        }

    }

    private void restart() {
        if (counter != null) {
            isTimmerRuning = false;
            counter.cancel();
        }
        initTimer();
        getTime().setText(convertTimeFromLong(actualTime));
        getRound().setText(String.valueOf(actualRound));
        setTimeTypeStringItem();
    }

    private void makeAlert() {
        try {
            getDisplay().vibrate(1000);
            player.start();
        }  catch (MediaException me) {
            me.printStackTrace();
        }
    }
}
