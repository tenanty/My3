package channelsoft.com.my3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * Created by yuanshun on 2014/12/18.
 */
public class HelloWorldActivity extends Activity {

    private static String ACTIVITY_TAG = "helloWorldTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hello_world);
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        String name = (String) bundle.get("name");
        Log.d(this.ACTIVITY_TAG,"获取名字为："+name);
        TextView view = (TextView) findViewById(R.id.textViewName);
        view.setText(name);
    }
}
