package channelsoft.com.my3;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

/**
 * Created by yuanshun on 2014/12/22.
 */
public class OpenActivity extends Activity {
    private static String ACTIVITY_TAG = "openUrl";
    private Button button1;
    private Button button2;
    private Button button3;

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open);
        button1 = (Button)findViewById(R.id.button6);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.util.Log.d(ACTIVITY_TAG,"按钮被点击.");
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://www.baidu.com"));
                startActivity(intent);
            }
        });

        button2 = (Button)findViewById(R.id.button7);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(ACTIVITY_TAG,"打开电话按钮被点击.");
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:10086"));
                startActivity(intent);
            }
        });

        button3 = (Button)findViewById(R.id.button8);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView = (WebView) findViewById(R.id.web_view);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.setWebViewClient(new WebViewClient(){
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);
                        return true;
//                        return super.shouldOverrideUrlLoading(view, url);
                    }

                });
                webView.loadUrl("http://www.baidu.com");
            }
        });


    }

}
