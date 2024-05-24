package bku.iot.demoiot;

        import androidx.appcompat.app.AppCompatActivity;
        import androidx.appcompat.widget.SwitchCompat;

        import android.content.Intent;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.CompoundButton;
        import android.widget.RelativeLayout;
        import android.widget.TextView;

        import com.github.angads25.toggle.interfaces.OnToggledListener;
        import com.github.angads25.toggle.model.ToggleableView;
        import com.github.angads25.toggle.widget.LabeledSwitch;

        import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
        import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
        import org.eclipse.paho.client.mqttv3.MqttException;
        import org.eclipse.paho.client.mqttv3.MqttMessage;

        import java.nio.charset.Charset;
        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.Date;
        import java.util.Locale;

        import androidx.appcompat.widget.SwitchCompat;
        import android.widget.CompoundButton;
// test git on android studio
public class MainActivity extends AppCompatActivity {
    MQTTHelper mqttHelper;
    TextView txtTemp, txtLight, txtHumid;
    SwitchCompat btnLed, btnPump;
    String username, key;
    RelativeLayout TempCard, HumidCard, LightCard;

    private boolean isWifiConnected = false;

    private ArrayList<String> LightList = new ArrayList<>();
    private ArrayList<String> TempList = new ArrayList<>();
    private ArrayList<String> HumidList = new ArrayList<>();
//    private ArrayList<String> DiaryList = new ArrayList<>();
//    private ArrayList<String> DiaryTimeList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
//        Gui va nhan key tu login activity
        username = intent.getStringExtra("username");
        key = intent.getStringExtra("key");

        txtTemp = findViewById(R.id.TempValue);
        txtHumid = findViewById(R.id.HumidValue);
        txtLight = findViewById(R.id.LightValue);
        btnLed = findViewById(R.id.switchLight);
        btnPump = findViewById(R.id.switchPump);
        TempCard = findViewById(R.id.TempCard);
        HumidCard = findViewById(R.id.HumidCard);
        LightCard = findViewById(R.id.LightCard);
//        DiaryCard = findViewById(R.id.DiaryCard);


        btnLed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonview, boolean isChecked) {
                Log.d ("TEST", String.valueOf(isWifiConnected));
                if (isChecked){
                    if (isWifiConnected){
                        sendDataMQTT(username + "/feeds/nutnhan1", "1");
                    }
                    else {
                        btnLed.setChecked(false);
                    }
                }
                else {
                    if (isWifiConnected){
                        sendDataMQTT(username + "/feeds/nutnhan1", "0");
                    }
                    else{
                        btnLed.setChecked(true);
                    }
                }

            }
        });
        btnPump.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonview, boolean isChecked) {
                Log.d ("TEST", String.valueOf(isWifiConnected));
                if (isChecked){
                    if (isWifiConnected){
                        sendDataMQTT(username + "/feeds/nutnhan2", "1");
                    }
                    else {
                        btnPump.setChecked(false);
                    }
                }
                else {
                    if (isWifiConnected){
                        sendDataMQTT(username + "/feeds/nutnhan2", "0");
                    }
                    else{
                        btnPump.setChecked(true);
                    }
                }
            }
        });

//        btnPump.setOnToggledListener();

        TempCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String [] templist= TempList.toArray(new String[0]);
                Intent intent = new Intent(MainActivity.this, TempActivity.class);
                intent.putExtra("TempList", templist);
                startActivity(intent);
            }
        });
        HumidCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String [] humidlist = HumidList.toArray(new String[0]);
                Intent intent = new Intent(MainActivity.this, HumidActivity.class);
                intent.putExtra("HumidList", humidlist);
                startActivity(intent);
            }
        });
        LightCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] lightlist = LightList.toArray(new String[0]);

                Intent intent = new Intent(MainActivity.this, LightActivity.class);
                intent.putExtra("LightList", lightlist);

                startActivity(intent);
            }
        });

        startMQTT();
    }
    public void sendDataMQTT(String topic, String value){
        MqttMessage msg = new MqttMessage();
        msg.setId(1234);
        msg.setQos(0);
        msg.setRetained(false);

        byte[] b = value.getBytes(Charset.forName("UTF-8"));
        msg.setPayload(b);

        try {
            mqttHelper.mqttAndroidClient.publish(topic, msg);
        }catch (MqttException e){
        }
    }
    public void startMQTT(){
        mqttHelper = new MQTTHelper(this, username, key);
        mqttHelper.setConnectionFailedListener(new MQTTHelper.ConnectionFailedListener() {
            @Override
            public void onConnectionFailed() {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        mqttHelper.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                isWifiConnected = true;
                Log.d("TEST", "Connected to " + serverURI);
            }

            @Override
            public void connectionLost(Throwable cause) {
                isWifiConnected = false;
                Log.d("TEST", "Connection lost", cause);
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Log.d("TEST", topic + "***" + message.toString());
                if (topic.contains("cambien1")){
                    txtTemp.setText(message.toString() + "°C");
                    TempList.add(message.toString());
//                    String diaryentry = "cambien1: " + message.toString();
//                    DiaryList.add (diaryentry);
//                    String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
//                    DiaryTimeList.add(currentTime);
                }
                else if (topic.contains("cambien2")){
                    txtLight.setText(message.toString() + "lux");
                    LightList.add(message.toString());
//                    String diaryentry = "cambien2: " + message.toString();
//                    DiaryList.add (diaryentry);
//                    DiaryList.add (message.toString());
//                    String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
//                    DiaryTimeList.add(currentTime);
                }else if (topic.contains("cambien3")){
                    txtHumid.setText(message.toString() + "%");
                    HumidList.add(message.toString());
//                    String diaryentry = "cambien3: " + message.toString();
//                    DiaryList.add (diaryentry);
//                    String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
//                    DiaryTimeList.add(currentTime);
                }
                else if (topic.contains("nutnhan1")){
                    if (message.toString().equals("1")){
                        btnLed.setChecked(true);
//                        String diaryentry = "nutnhan1: " + message.toString();
//                        DiaryList.add (diaryentry);
//                        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
//                        DiaryTimeList.add(currentTime);
                    }else if (message.toString().equals("0")){
                        btnLed.setChecked(false);
//                        String diaryentry = "nutnhan1: " + message.toString();
//                        DiaryList.add (diaryentry);
//                        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
//                        DiaryTimeList.add(currentTime);
                    }
                } else if (topic.contains("nutnhan2")){
                    if (message.toString().equals("1")){
                        btnPump.setChecked(true);
//                        String diaryentry = "nutnhan2: " + message.toString();
//                        DiaryList.add (diaryentry);
//                        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
//                        DiaryTimeList.add(currentTime);
                    }else if (message.toString().equals("0")){
                        btnPump.setChecked(false);
//                        String diaryentry = "nutnhan2: " + message.toString();
//                        DiaryList.add (diaryentry);
//                        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
//                        DiaryTimeList.add(currentTime);
                    }
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                Log.d("TEST", "Delivery complete for token: " + token);
            }
        });
    }
}