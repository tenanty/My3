package channelsoft.com.my3;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


public class MainActivity extends Activity {

    protected static final String ACTIVITY_TAG="MyAndroid";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(MainActivity.ACTIVITY_TAG,"XXXX");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ListView list = (ListView)findViewById(R.id.listView);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = (String) list.getItemAtPosition(position);
                Log.d(MainActivity.ACTIVITY_TAG," 点击位置:"+ name);
                if("HelloWorld".equals(name)){
                    Log.d(MainActivity.ACTIVITY_TAG,"进入helloWorld页面.");
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this,HelloWorldActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("name","Tom");
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                if("录音".equals(name)){
                    Log.d(MainActivity.ACTIVITY_TAG,"进入录音页面.");
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this,AudioActivity.class);
                    startActivity(intent);
                }
                if("弹出信息".equals(name)){
                    Log.d(MainActivity.ACTIVITY_TAG,"进入弹出信息");
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this,AlertActivity.class);
                    startActivity(intent);
                }
            }
        });


       /* Button button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(MainActivity.ACTIVITY_TAG,"点击了按钮");
                Intent intent = new Intent();
                //设置Intent的源地址与目标地址
                intent.setClass(MainActivity.this,ChangeActivity2.class);
                //Intent可以通过Bundle进行数据的传递
                Bundle bundle = new Bundle();
                bundle.putString("name","zhangsan");
                bundle.putInt("age",23);
                intent.putExtras(bundle);
                //调用startActivity方法发送意图给系统
                startActivity(intent);

                //关闭当前activity，添加了该语句后，用户通过点击返回键是无法返回该activity的
                //MainActivity.this.finish();
            }
        });*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onShowDialogClick(View v){
        Log.i(MainActivity.ACTIVITY_TAG,"onShowDialogClick");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("测试AlertDialog");
        builder.setPositiveButton("ok",new EmptyListener());
        builder.show();
    }

    public void onShowDialogClick1(View v){
        Log.i(MainActivity.ACTIVITY_TAG,"onShowDialogClick1");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("测试星期");
        builder.setItems(R.array.select_days, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(MainActivity.ACTIVITY_TAG,"进入 onShowDialogClick1().");
                //which是选中的位置(从0开始)
                String[] items = getResources().getStringArray(R.array.select_days);
                Toast.makeText(MainActivity.this, items[which], Toast.LENGTH_LONG).show();
            }
        });
        builder.show();

    }


    //空的监听类
    private class EmptyListener implements DialogInterface.OnClickListener{

        @Override
        public void onClick(DialogInterface dialog, int which) {
            Log.d(MainActivity.ACTIVITY_TAG,"alert的ok按钮被点击.");
        }

    }
}
