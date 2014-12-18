package channelsoft.com.my3;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by yuanshun on 2014/12/18.
 */
public class AlertActivity extends Activity {

    private Button mButton;
    private Button mButtonWeek;
    private static String ACTIVITY_TAG = "AlertActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alert);

        mButton = (Button) findViewById(R.id.button4);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AlertActivity.this);
                builder.setTitle("测试AlertDialog");
                builder.setPositiveButton("ok",new EmptyListener());
                builder.show();
            }
        });

        mButtonWeek = (Button) findViewById(R.id.button5);
        mButtonWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(MainActivity.ACTIVITY_TAG,"onClick");
                AlertDialog.Builder builder = new AlertDialog.Builder(AlertActivity.this);
                builder.setTitle("测试星期");
                builder.setItems(R.array.select_days, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(AlertActivity.ACTIVITY_TAG,"进入 onClick().");
                        //which是选中的位置(从0开始)
                        String[] items = getResources().getStringArray(R.array.select_days);
                        Toast.makeText(AlertActivity.this, items[which], Toast.LENGTH_LONG).show();
                    }
                });
                builder.show();
            }
        });

    }

    //空的监听类
    private class EmptyListener implements DialogInterface.OnClickListener{

        @Override
        public void onClick(DialogInterface dialog, int which) {
            Log.d(MainActivity.ACTIVITY_TAG, "alert的ok按钮被点击.");
        }

    }
}
