/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.chali.fitness.timer.bean;

/**
 *
 * @author chali
 */
public class Setting {
    private String startupTime;
    private String workoutTime;
    private String restTime;
    private String rounds;

    public String getRestTime() {
        return restTime;
    }

    public long getRestTimeAsLong(){
        return convertTimeFromString(restTime);
    }

    public void setRestTime(String restTime) {
        this.restTime = restTime;
    }

    public String getRounds() {
        return rounds;
    }

    public int getRoundsAsInt(){
        return Integer.parseInt(rounds);
    }

    public void setRounds(String rounds) {
        this.rounds = rounds;
    }

    public String getStartupTime() {
        return startupTime;
    }

    public long getStartupTimeAsLong(){
        return convertTimeFromString(startupTime);
    }

    public void setStartupTime(String startupTime) {
        this.startupTime = startupTime;
    }

    public String getWorkoutTime() {
        return workoutTime;
    }

    public long getWorkoutTimeAsLong(){
        return convertTimeFromString(workoutTime);
    }

    public void setWorkoutTime(String workoutTime) {
        this.workoutTime = workoutTime;
    }

    private long convertTimeFromString(String time){
       String[] parts = split(time, ":");
       long millies = 0;
       millies += (Integer.parseInt(parts[0])*1000*60);
       millies += (Integer.parseInt(parts[1])*1000);
       return millies;
    }

    private String[] split(String s, String pattern){
        int partsCount = 0;
        int lastIndex = -1;
        do{
           partsCount++;
           lastIndex = s.indexOf(pattern, lastIndex+1);
        }
        while (lastIndex > -1);
        String[] parts = new String[partsCount];
        int startIndex = 0;
        lastIndex = -1;
        for (int i=0; i<partsCount; i++){
            lastIndex = s.indexOf(pattern, lastIndex+1);
            parts[i]=s.substring(i==0?startIndex:startIndex+1,lastIndex>-1?lastIndex:s.length());
            startIndex = lastIndex;
        }
        return parts;
    }

}
