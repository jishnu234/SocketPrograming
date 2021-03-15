package com.example.asynctask;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


//    Integer[] fruitsImg;
//    String[] fruitsName;
    public static final int MESSAGE_SENDER_ME = 1;
    public static final int MESSAGE_SENDER_OTHER = 2;
    RecyclerView recyclerView;
    ChatAdapter chatAdapter;
    ArrayList<ChatModel> chatList;
    FloatingActionButton send;
    EditText messageField;
    String myUserName = "jishnu";
    private Socket mSocket;
    private Boolean isConnected;
    {
        try {
            mSocket = IO.socket(String.valueOf("https://socket-io-chat.now.sh/"));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        chatList = new ArrayList<>();
        send = (FloatingActionButton) findViewById(R.id.sendButton);
        messageField = (EditText) findViewById(R.id.messageField);
        onSocketConnect();

        send.setOnClickListener(this);


//        fruitsImg = new Integer[]{R.drawable.apple,R.drawable.banana,R.drawable.papaya,
//                R.drawable.jackfruit,R.drawable.pineapple,R.drawable.orange};
//
//        fruitsName = new String[]{"Apple","Banana","Papaya","Jack fruit",
//                "Pine Apple","Orange"};

        chatAdapter = new ChatAdapter(this,chatList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(chatAdapter);

    }

    private void onSocketConnect() {

        mSocket.connect();
        mSocket.on(Socket.EVENT_CONNECT,onConnect);
        mSocket.on(Socket.EVENT_DISCONNECT,onDisconnected);
        mSocket.on(Socket.EVENT_CONNECT_ERROR,onConectionError);
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT,onConectionError);
        mSocket.on("new message", onNewMessage);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();
        mSocket.off(Socket.EVENT_CONNECT,onConnect);
        mSocket.off(Socket.EVENT_DISCONNECT,onDisconnected);
        mSocket.off(Socket.EVENT_CONNECT_ERROR,onConectionError);
        mSocket.off(Socket.EVENT_CONNECT_TIMEOUT,onConectionError);
        mSocket.off("new message", onNewMessage);

    }

    @Override
    public void onBackPressed() {


        AlertDialog.Builder builder=new AlertDialog.Builder(this);

        builder.setTitle("Exit..");
        builder.setMessage("exit");
        builder.setCancelable(true);
        builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.super.onBackPressed();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog dialog=builder.create();
        dialog.show();
    }


    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.sendButton) {
            String msg=messageField.getText().toString().trim();

            if(!msg.equals(null) || !TextUtils.isEmpty(msg)) {

                chatList.add(new ChatModel(myUserName, msg,MESSAGE_SENDER_ME ));
                messageField.setText("");
                mSocket.emit("new message", msg);
                if (chatAdapter!=null)
                chatAdapter.notifyDataSetChanged();
            }


        }
    }

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(!isConnected) {
                        mSocket.emit("add User", myUserName);
                        Toast.makeText(MainActivity.this, "Conected", Toast.LENGTH_SHORT).show();
                        isConnected = true;
                    }
                }
            });
        }
    };

    private Emitter.Listener onDisconnected = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    isConnected = false;

                    Toast.makeText(MainActivity.this, "Disconnected", Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

    private Emitter.Listener onConectionError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    Toast.makeText(MainActivity.this, "Connection Error", Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject dataReceived = (JSONObject) args[0];
                    String username,message;

                    try{
                        username = dataReceived.getString("username");
                        message = dataReceived.getString(("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return;
                    }

                    Toast.makeText(MainActivity.this, username, Toast.LENGTH_SHORT).show();
                    chatList.add(new ChatModel(myUserName, message, MESSAGE_SENDER_OTHER));
                    if(chatAdapter != null)
                    chatAdapter.notifyDataSetChanged();
                }
            });
        }
    };
}