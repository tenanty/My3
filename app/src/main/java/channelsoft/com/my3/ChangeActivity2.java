package channelsoft.com.my3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by yuanshun on 2014/12/16.
 */
public class ChangeActivity2 extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout1);

        TextView textView = (TextView)findViewById(R.id.textView);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        String result = "";
        result+="姓名： "+bundle.getString("name");
        result+=" 年龄："+bundle.getInt("age");
        textView.setText(result);
    }
}
