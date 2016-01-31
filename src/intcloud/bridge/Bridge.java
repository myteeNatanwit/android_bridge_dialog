package intcloud.bridge;

import java.io.IOException;

import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Toast;

   
public class Bridge extends Activity {
	WebView webView;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        webView = (WebView) findViewById(R.id.webView1);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);
        webView.getSettings().setSupportMultipleWindows(false);
        webView.getSettings().setSupportZoom(false);
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        
        //Inject WebAppInterface methods into Web page by having Interface 'bridge' 
        webView.addJavascriptInterface(new bridge_protocol(this), "bridge");
        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl("file:///android_asset/www/index.html");
                
    }
    
    public void button1_onclick(View view) {
    	js_call(webView, "Javascript_function('this is from Java!')");

    }

    public void js_call(WebView webView, String jsString) {
		final String webUrl = "javascript:" +  jsString;
		 if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
	        webView.evaluateJavascript(jsString, null);
	        System.out.println(jsString);
	    } else {
	        webView.loadUrl(webUrl);
	        System.out.println("old way");
	    }
	}
	
// class to comm with JS	
public class bridge_protocol {
        Context aContext;

        bridge_protocol(Context c) {
            aContext = c;
        }

        //  Show Toast Message
        // @param toast as String
         
        @JavascriptInterface
        public void showToast(String toast) {
            Toast.makeText(aContext, toast, Toast.LENGTH_SHORT).show();
        }
        
        //  Show Dialog 
        @JavascriptInterface
        public void showDialog(String dialogMsg){
        	AlertDialog.Builder alertDialog = new AlertDialog.Builder(aContext);
        	 
            // Setting Dialog Title
            alertDialog.setTitle("Title of the Android dialog");
            // Setting Dialog Message
            alertDialog.setMessage(dialogMsg);
            alertDialog.setCancelable(true);
            // Setting Positive "Yes" Button
            alertDialog.setPositiveButton("YES",
                     new DialogInterface.OnClickListener() {
                         public void onClick(DialogInterface dialog, int which) {
  // do something
                        	 dialog.cancel();
                         }
                     });
     
            // Showing Alert Message
            alertDialog.show();
        }
	}
}

