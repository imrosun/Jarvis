package com.example.googleassistance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    TextToSpeech tts;

    private TextView txtSpeechInput;
    private ImageButton btnSpeak;
    String name="";
    private final int REQ_CODE_SPEECH_INPUT = 100;
    String callstatus="0";
    String searchstatus="0";
    String filestatus="0";
    String text = "Hi How Can i assist You";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Ids.name="";
        txtSpeechInput = (TextView) findViewById(R.id.txtSpeechInput);
        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);
        tts=new TextToSpeech(MainActivity.this, new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(int status) {
                // TODO Auto-generated method stub
                if(status == TextToSpeech.SUCCESS){
                    int result=tts.setLanguage(Locale.ENGLISH);
                    if(result==TextToSpeech.LANG_MISSING_DATA ||
                            result==TextToSpeech.LANG_NOT_SUPPORTED){
                        Log.e("error", "This Language is not supported");
                    }
                    else{
                        ConvertTextToSpeech();
                    }
                }
                else
                    Log.e("error", "Initilization Failed!");
            }
        });
        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });

    }

    private void promptSpeechInput() {
        txtSpeechInput.setText("");
        Ids.name="";
        Ids.name1="";
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txtSpeechInput.setText(result.get(0));
                    String a=result.get(0);
                    String[] b=a.split(" ");
                    for(int i=1;i<b.length;i++)
                    {
                        name =name+b[i]+" ";
                    }
                    Ids.name="";
                    Ids.name=name;
//                    Toast.makeText(getApplicationContext(),name,Toast.LENGTH_SHORT).show();

                    if(b.length>0)
                    {
                        for(int i=0;i<b.length;i++)
                        {
                            if(b[i].equals("call"))
                            {
                                callstatus="1";
                                searchstatus="0";
                                filestatus="0";
                            }
                            else if(b[i].equals("search")||b[i].equals("information")||b[i].equals("inquiry"))
                            {
                                searchstatus="1";
                                callstatus="0";
                                filestatus="0";
                            }
                            else if(b[i].equals("file")||b[i].equals("PDF")||b[i].equals("pdf")||b[i].equals("document")||b[i].equals("documents"))
                            {
                                searchstatus="0";
                                callstatus="0";
                                filestatus="1";
                            }


                        }
                        if(searchstatus.equals("1"))
                        {
                            callstatus="0";
                            text="Here Is the list of information";
                            ConvertTextToSpeech();
                            String s=txtSpeechInput.getText().toString();
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com/search?q="+s));
                            startActivity(browserIntent);
//                            startActivity(new Intent(getApplicationContext(),Information.class));
                        }
                        else if(callstatus.equals("1"))
                        {
                            searchstatus="0";
                            text="Calling "+name;
                            ConvertTextToSpeech();
                            startActivity(new Intent(getApplicationContext(),CallingActivity.class));
                        }
                        else if(filestatus.equals("1"))
                        {
                            String a1=result.get(0);
                            String[] b1=a.split(" ");
                            for(int i=0;i<b1.length;i++)
                            {
                                if(i==2)
                                {
                                    Ids.name1=b1[2];
                                }
                            }


                            searchstatus="0";
                            text="Searching File "+Ids.name1;
                            ConvertTextToSpeech();
                            startActivity(new Intent(getApplicationContext(),SearchFiles.class));
                        }
                        else {
                            text="information not clear";
                            ConvertTextToSpeech();
                            Toast.makeText(getApplicationContext(),"Information not clear",Toast.LENGTH_SHORT).show();
                        }

                    }

                }
                break;
            }

        }
    }



    private void ConvertTextToSpeech() {
        // TODO Auto-generated method stub

        if(text==null||"".equals(text))
        {
            text = "Content not available";
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }else {
            Set<String> a=new HashSet<>();
            a.add("male");
            Voice v=new Voice("en-us-x-sfg#male_2-local",new Locale("en","US"),400,200,true,a);
            tts.setPitch(0.7f);
            tts.setVoice(v);
            tts.setSpeechRate(0.7f);
            tts.speak(text, TextToSpeech.QUEUE_ADD, null);
        }
    }
}
