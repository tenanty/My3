package channelsoft.com.my3;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.MessageListener;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.SmackAndroid;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;

import java.io.File;
import java.io.IOException;
import java.util.Collection;


public class MainActivity extends Activity {

    protected static final String ACTIVITY_TAG = "MyAndroid";

    public static final int UPDATE_MESSAGE = 1;
    File file;
    MyHandler myHandler;

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            Log.d(ACTIVITY_TAG, "进入handleMessage" + msg.what);
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATE_MESSAGE:
                    String message = (String) msg.getData().get("message");
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public int getTaskId() {
        return super.getTaskId();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(MainActivity.ACTIVITY_TAG, "XXXX");


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //显示hello
        Toast.makeText(MainActivity.this, "hello", Toast.LENGTH_SHORT);
        //判断文件是否存在
        hasExist();
        //测试信息
        //SmackUtil.connectionOpenfire();

        conServer();

        //子线程负责处理服务器初始化
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                conServer();
//            }
//        }).start();


        final ListView list = (ListView) findViewById(R.id.listView);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = (String) list.getItemAtPosition(position);
                Log.d(MainActivity.ACTIVITY_TAG, " 点击位置:" + name);
                if ("HelloWorld".equals(name)) {
                    Log.d(MainActivity.ACTIVITY_TAG, "进入helloWorld页面.");
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, HelloWorldActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("name", "Tom");
                    intent.putExtras(bundle);
                    startActivity(intent);
                    return;
                }
                if ("录音".equals(name)) {
                    Log.d(MainActivity.ACTIVITY_TAG, "进入录音页面.");
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, AudioActivity.class);
                    startActivity(intent);
                    return;
                }
                if ("弹出信息".equals(name)) {
                    Log.d(MainActivity.ACTIVITY_TAG, "进入弹出信息");
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, AlertActivity.class);
                    startActivity(intent);
                    return;
                }
                if ("文件上传".equals(name)) {
                    Log.d(MainActivity.ACTIVITY_TAG, "进入文件上传");
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, UploadActivity.class);
                    startActivity(intent);
                    return;
                }
                if ("按钮".equals(name)) {
                    Log.d(MainActivity.ACTIVITY_TAG, "按钮");
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, OpenActivity.class);
                    startActivity(intent);
                    return;
                }
            }
        });
    }

    private static ConnectionConfiguration config;
    private static XMPPConnection connection;

    public void conServer() {
        Log.d(ACTIVITY_TAG, "系统初始化开始...");
        SmackAndroid.init(this);

        config = new ConnectionConfiguration("10.130.24.220", 5222);
        SASLAuthentication.supportSASLMechanism("PLAN", 0);
        config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        config.setCompressionEnabled(false);
        config.setReconnectionAllowed(false);
        config.setSendPresence(true);
        config.setDebuggerEnabled(true);
        XMPPConnection conn = new XMPPTCPConnection(config);
        try {
            conn.connect();//连接
            conn.login("y1", "y1");//登陆
            Log.d(ACTIVITY_TAG, "授权状态：" + conn.isAuthenticated());
            Log.d(ACTIVITY_TAG, "连接状态：" + conn.isConnected());
            Chat chat = ChatManager.getInstanceFor(conn).createChat("y1", new MessageListener() {
                @Override
                public void processMessage(Chat chat, Message message) {
                    Log.d(ACTIVITY_TAG, "获取消息：" + message);
                    Log.d(ACTIVITY_TAG, "message.getBody():" + message.getBody());
                    Toast.makeText(MainActivity.this, message.getBody(), Toast.LENGTH_SHORT).show();
                }
            });
            ChatManager.getInstanceFor(conn).addChatListener(new ChatManagerListener() {
                @Override
                public void chatCreated(Chat chat, boolean b) {
                    chat.addMessageListener(new MessageListener() {
                        @Override
                        public void processMessage(Chat chat, final Message message) {
                            Log.d(ACTIVITY_TAG, "xxReceived From [" + message + "] message:" + message.getBody());
//                            new Thread(new Runnable() {
//                                @Override
//                                public void run() {
                                    android.os.Message msg = new android.os.Message();
                                    msg.what = UPDATE_MESSAGE;
                                    Bundle data = new Bundle();
                                    data.putString("message", message.getBody());
                                    msg.setData(data);
                                    handler.sendMessage(msg);
//                                }
//                            }).start();
                        }
                    });
                }
            });


            Roster roster = conn.getRoster();
            Collection<RosterEntry> entries = roster.getEntries();
            for (RosterEntry entry : entries) {
                Log.d(ACTIVITY_TAG, entry.getName() + "-" + entry.getUser() + "-" + entry.getType() + "-" + entry.getGroups().size());
                Presence presence = roster.getPresence(entry.getUser());
                Log.d(ACTIVITY_TAG, "-" + presence.getStatus() + "-" + presence.getFrom());
            }

            roster.addRosterListener(new RosterListener() {
                @Override
                public void entriesAdded(Collection<String> strings) {

                }

                @Override
                public void entriesUpdated(Collection<String> strings) {

                }

                @Override
                public void entriesDeleted(Collection<String> strings) {

                }

                @Override
                public void presenceChanged(Presence presence) {

                }
            });

            for (RosterGroup g : roster.getGroups()) {
                for (RosterEntry entry : g.getEntries()) {
                    System.out.println("Group:" + g.getName() + ">>" + entry.getName() + "-" + entry.getUser() + "-" + entry.getType() + "-" + entry.getGroups().size());
                }
            }


            Log.d(ACTIVITY_TAG, "系统初始化结束...");
        } catch (SmackException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XMPPException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(MainActivity.ACTIVITY_TAG, "菜单被选择.");
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Toast.makeText(this, "select settings", Toast.LENGTH_SHORT).show();
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onShowDialogClick(View v) {
        Log.i(MainActivity.ACTIVITY_TAG, "onShowDialogClick");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("测试AlertDialog");
        builder.setPositiveButton("ok", new EmptyListener());
        builder.show();
    }

    public void onShowDialogClick1(View v) {
        Log.i(MainActivity.ACTIVITY_TAG, "onShowDialogClick1");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("测试星期");
        builder.setItems(R.array.select_days, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(MainActivity.ACTIVITY_TAG, "进入 onShowDialogClick1().");
                //which是选中的位置(从0开始)
                String[] items = getResources().getStringArray(R.array.select_days);
                Toast.makeText(MainActivity.this, items[which], Toast.LENGTH_LONG).show();
            }
        });
        builder.show();

    }


    //空的监听类
    private class EmptyListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            Log.d(MainActivity.ACTIVITY_TAG, "alert的ok按钮被点击.");
        }

    }

    public boolean hasExist() {
        file = new File(Environment.getExternalStorageDirectory(), "peipei.amr");
        Boolean exists = file.exists();
        Log.d(ACTIVITY_TAG, "文件是否存在:" + exists);
        return true;
    }

    class MyHandler extends Handler {
        public MyHandler() {
        }

        public MyHandler(Looper L) {
            super(L);
        }

        // 子类必须重写此方法,接受数据
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            Log.d("MyHandler", "handleMessage......");
            Log.d(MainActivity.ACTIVITY_TAG, "xxReceived From [" + msg + "] message:" + msg.getBody());
        }
    }

    class MyThread implements Runnable {
        public void run() {

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            Log.d("thread.......", "mThread........");
            Message msg = new Message();
            Bundle b = new Bundle();// 存放数据

            MainActivity.this.myHandler.handleMessage(msg); // 向Handler发送消息,更新UI

        }
    }


}

