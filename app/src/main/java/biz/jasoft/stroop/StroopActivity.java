package biz.jasoft.stroop;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;


public class StroopActivity extends ActionBarActivity {

    String filename = "stroop-session-" + (new Date()).toString() + ".csv";
    Integer currentTrial = 0;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    Date previousDateStored;
    Integer maxCongruent = 5;
    Integer currentCongruent = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_stroop);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_stroop, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            setContentView(R.layout.setup_activity_stroop);
            setupViewStroopSettings();
            return true;
        } else if (id == R.id.email_file) {
            emailFile();
            setContentView(R.layout.setup_activity_stroop);
            setupViewStroopSettings();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClickBtn1(View v) {

        writeData(setupData(getText(R.string.red).toString()));

        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        Integer numberOfTrials = preferences.getInt("numberOfTrials", 0);

        if (this.currentTrial > numberOfTrials) {
            setContentView(R.layout.setup_activity_stroop);
            setupViewStroopSettings();
        } else {
            setContentView(R.layout.activity_stroop);

            setTextParticipant((TextView) findViewById(R.id.participant));
            setTextTrialCount((TextView) findViewById(R.id.trialCount), this.currentTrial);

            TextView determineTextView = (TextView) findViewById(R.id.determineText);
            setTextRandomColorAndText(determineTextView);
        }
    }

    public void onClickBtn2(View v) {

        writeData(setupData(getText(R.string.yellow).toString()));

        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        Integer numberOfTrials = preferences.getInt("numberOfTrials", 0);

        if (this.currentTrial > numberOfTrials) {
            setContentView(R.layout.setup_activity_stroop);
            setupViewStroopSettings();
        } else {
            setContentView(R.layout.activity_stroop);

            setTextParticipant((TextView) findViewById(R.id.participant));
            setTextTrialCount((TextView) findViewById(R.id.trialCount), this.currentTrial);

            TextView determineTextView = (TextView) findViewById(R.id.determineText);
            setTextRandomColorAndText(determineTextView);
        }
    }

    public void onClickBtn3(View v) {

        writeData(setupData(getText(R.string.green).toString()));

        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        Integer numberOfTrials = preferences.getInt("numberOfTrials", 0);

        if (this.currentTrial > numberOfTrials) {
            setContentView(R.layout.setup_activity_stroop);
            setupViewStroopSettings();
        } else {

            setContentView(R.layout.activity_stroop);

            setTextParticipant((TextView) findViewById(R.id.participant));
            setTextTrialCount((TextView) findViewById(R.id.trialCount), this.currentTrial);

            TextView determineTextView = (TextView) findViewById(R.id.determineText);
            setTextRandomColorAndText(determineTextView);
        }
    }

    public void onClickBtn4(View v) {

        writeData(setupData(getText(R.string.blue).toString()));

        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        Integer numberOfTrials = preferences.getInt("numberOfTrials", 0);

        if (this.currentTrial > numberOfTrials) {
            setContentView(R.layout.setup_activity_stroop);
            setupViewStroopSettings();
        } else {
            setContentView(R.layout.activity_stroop);

            setTextParticipant((TextView) findViewById(R.id.participant));
            setTextTrialCount((TextView) findViewById(R.id.trialCount), this.currentTrial);

            TextView determineTextView = (TextView) findViewById(R.id.determineText);
            setTextRandomColorAndText(determineTextView);
        }
    }

    private void setTextRandomColorAndText(TextView textView) {

        Random randomColor = new Random();

        int randomColorInt = randomColor.nextInt(100) % 4;
        if (randomColorInt == 1) {
            textView.setTextColor(Color.RED);
        } else if (randomColorInt == 2) {
            textView.setTextColor(Color.YELLOW);
        } else if (randomColorInt == 3) {
            textView.setTextColor(Color.GREEN);
        } else {
            textView.setTextColor(Color.BLUE);
        }

        Random randomText = new Random();

        int randomTextInt = randomText.nextInt(100) % 4;

        //if congruent, try randomizing again.
        if (randomColorInt == randomTextInt) {
            if (this.currentCongruent >= this.maxCongruent) {
                if (randomColorInt > 3) {
                    randomColorInt -= 1;
                } else {
                    randomColorInt += 1;
                }
            }
            this.currentCongruent += 1;
        }

        if (randomTextInt == 0) {
            textView.setText(getText(R.string.red));
        } else if (randomTextInt == 1) {
            textView.setText(getText(R.string.yellow));
        } else if (randomTextInt == 2) {
            textView.setText(getText(R.string.green));
        } else {
            textView.setText(getText(R.string.blue));
        }
    }

    public void onClickSaveBtn(View v) {

        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        EditText participantIdText = (EditText) findViewById(R.id.participantId);
        Integer participantId = Integer.parseInt(participantIdText.getText().toString());
        editor.putInt("participantId", participantId);

        EditText numberOfTrialsText = (EditText) findViewById(R.id.numberOfTrials);
        Integer numberOfTrials = Integer.parseInt(numberOfTrialsText.getText().toString());
        editor.putInt("numberOfTrials", numberOfTrials);

        EditText emailAddressText = (EditText) findViewById(R.id.emailAddress);
        String emailAddress = emailAddressText.getText().toString();
        editor.putString("emailAddress", emailAddress);

        // Commit the edits!
        editor.commit();

        this.currentTrial = 0;

        InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        setContentView(R.layout.activity_stroop);
    }

    private void setTextParticipant(TextView textView) {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        Integer participantId = preferences.getInt("participantId", 0);
        textView.setText(String.valueOf(participantId));
    }

    private void setTextTrialCount(TextView textView, Integer currentTrial) {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        Integer numberOfTrials = preferences.getInt("numberOfTrials", 0);
        textView.setText(String.valueOf(currentTrial) + " of " + String.valueOf(numberOfTrials));
    }

    private void writeData(String data) {

        this.currentTrial += 1;

        FileOutputStream outputStream;
        if (isExternalStorageWritable()) {

            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), filename);

            StringBuilder fileText = new StringBuilder();
            try {
                if (file.exists()) {
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    String line = "";

                    while ((line = br.readLine()) != null) {
                        fileText.append(line);
                        fileText.append('\n');
                    }
                    br.close();
                }
            } catch (FileNotFoundException fnf) {
                //File not found but will be creating it during outputStream writer.
            } catch (Exception e) {
                e.printStackTrace();
            }

            String dataToWrite = fileText + data;

            try {
                outputStream = new FileOutputStream(file);
                outputStream.write(dataToWrite.getBytes());
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    private void setupViewStroopSettings() {

        SharedPreferences preferences = getPreferences(MODE_PRIVATE);

        Integer numberOfTrials = preferences.getInt("numberOfTrials", 0);
        Integer participantId = preferences.getInt("participantId", 0);
        String emailAddress = preferences.getString("emailAddress", "");

        EditText participantIdText = (EditText) findViewById(R.id.participantId);
        participantIdText.setText(String.valueOf(participantId));

        EditText trialCountText = (EditText) findViewById(R.id.numberOfTrials);
        trialCountText.setText(String.valueOf(numberOfTrials));

        EditText emailAddressText = (EditText) findViewById(R.id.emailAddress);
        emailAddressText.setText(String.valueOf(emailAddress));
    }

    private void emailFile() {
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("image/jpeg");

        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]
                {preferences.getString("emailAddress", "")});
        Integer participantId = preferences.getInt("participantId", 0);

        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"Stroop - " + participantId);
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,"Participant: " + participantId);

        Uri uri = Uri.fromFile(new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS), filename));
        emailIntent.putExtra(Intent.EXTRA_STREAM, uri);

        startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    private String convertToColor(int id) {
        if (id == -16776961) {
            return "Blue";
        } else if (id == -16711936) {
            return "Green";
        } else if (id == -65536) {
            return "Red";
        } else {
            return "Yellow";
        }
    }

    private String setupData(String colorButtonPressed) {
        StringBuffer sb = new StringBuffer();
        TextView determineText = (TextView) findViewById(R.id.determineText);
        String determineTextString = determineText.getText().toString();
        String determineTextColor = convertToColor(determineText.getCurrentTextColor());

        Date currentDate = new Date();

        //WORD THAT APPEARS
        sb.append(determineTextString+",");

        //ACTUAL COLOR OF THE DISPLAYED WORD

        sb.append(determineTextColor + ",");

        //IF THE COLOR AND WORD ARE THE SAME.
        if (determineTextString.equalsIgnoreCase(determineTextColor)) {
            sb.append("Congruent,");
        } else {
            sb.append("Incongruent,");
        }

        //THE BUTTON THAT WAS PRESSED.
        sb.append(colorButtonPressed + ",");


        //IF THE BUTTON PRESSED DOES NOT MATCH THE ACTUAL COLOR OF THE WORD
        if (!determineTextColor.equalsIgnoreCase(colorButtonPressed)) {
            //correct answer
            sb.append(1+",");
        } else {
            //incorrect answer
            sb.append(0+",");
        }

        //THE CURRENT DATE WHEN THE BUTTON WAS PRESSED.
        //sb.append(sdf.format(currentDate).toString()+",");

        //THE DIFFERENCE BETWEEN THE CURRENT BUTTON PRESSED AND PREVIOUS BUTTON PRESSED.
        if (this.previousDateStored != null) {
            Map<TimeUnit,Long> actualTimeDiffMap = computeDiff(this.previousDateStored, currentDate);

            long seconds = actualTimeDiffMap.get(TimeUnit.SECONDS);
            long minutesToSecs = actualTimeDiffMap.get(TimeUnit.MINUTES)*60;
            long hoursToSecs = actualTimeDiffMap.get(TimeUnit.HOURS)*60*60;
            long daysToSecs = actualTimeDiffMap.get(TimeUnit.DAYS)*60*60*24;
            long milliSeconds = actualTimeDiffMap.get(TimeUnit.MILLISECONDS);
            long finalSecondCount = seconds + minutesToSecs + hoursToSecs + daysToSecs;

            String timeDifference = finalSecondCount + "." + milliSeconds;
            sb.append(timeDifference);
        }

        this.previousDateStored = currentDate;

        if (determineTextString.equalsIgnoreCase(getText(R.string.start).toString())) {
            sb = new StringBuffer();
            SharedPreferences preferences = getPreferences(MODE_PRIVATE);
            Integer numberOfTrials = preferences.getInt("numberOfTrials", 0);
            Integer participantId = preferences.getInt("participantId", 0);

            sb.append("Word,Color of Word,Congruence?,Selection,Correct?,Time,,");

            sb.append("ParticipantId: " + participantId);
            sb.append(", Number of trials: " + numberOfTrials);
        }

        return sb.toString();
    }

    private Map<TimeUnit,Long> computeDiff(Date date1, Date date2) {
        long diffInMillies = date2.getTime() - date1.getTime();
        List<TimeUnit> units = new ArrayList<TimeUnit>(EnumSet.allOf(TimeUnit.class));
        Collections.reverse(units);

        Map<TimeUnit,Long> result = new LinkedHashMap<TimeUnit,Long>();
        long milliesRest = diffInMillies;
        for ( TimeUnit unit : units ) {
            long diff = unit.convert(milliesRest,TimeUnit.MILLISECONDS);
            long diffInMilliesForUnit = unit.toMillis(diff);
            milliesRest = milliesRest - diffInMilliesForUnit;
            result.put(unit,diff);
        }
        return result;
    }

}
