package com.example.balar.animeyounet;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailAnime extends AppCompatActivity {


    @BindView(R.id.dtJudul)
    TextView judul;
    @BindView(R.id.dtGambar)
    ImageView gambar;
    @BindView(R.id.dtTanggal)
    TextView tanggal;
    @BindView(R.id.fb)
    TextView fb;

    @BindView(R.id.ig)
    TextView ig;

    /*@BindView(R.id.dtGenre)
    TextView genre;*/

    @BindView(R.id.video)
    WebView video;




    public Anime Detail;


    private VideoEnabledWebChromeClient webChromeClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_anime);

        ButterKnife.bind(this);




        Detail = getIntent().getParcelableExtra("detail");
        judul.setText(Detail.getJudul());
        tanggal.setText(Detail.getTanggal());
        Glide.with(this)
                .load(Detail.getGambar())
                .into(gambar);

        View nonVideoLayout = findViewById(R.id.nonVideoLayout);
        ViewGroup videoLayout = findViewById(R.id.videoLayout);
        View loadingView = getLayoutInflater().inflate(R.layout.fullscreen_video, null);
        webChromeClient = new VideoEnabledWebChromeClient(nonVideoLayout, videoLayout, video){
            // Subscribe to standard events, such as onProgressChanged()...
            @Override
            public void onProgressChanged(WebView view, int progress)
            {
                // Your code...
            }
        };

        webChromeClient.setOnToggledFullscreen(new VideoEnabledWebChromeClient.ToggledFullscreenCallback()
        {
            @Override
            public void toggledFullscreen(boolean fullscreen)
            {
                // Your code to handle the full-screen change, for example showing and hiding the title bar. Example:
                if (fullscreen)
                {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    /*Toast.makeText(DetailAnime.this, "test full", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DetailAnime.this, Fullscreen.class);
                    intent.putExtra("link",Detail);
                    startActivity(intent);*/

                    WindowManager.LayoutParams attrs = getWindow().getAttributes();
                    attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
                    attrs.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                    getWindow().setAttributes(attrs);
                    if (android.os.Build.VERSION.SDK_INT >= 21)
                    {
                        //noinspection all
                        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
                    }
                }
                else
                {
                    WindowManager.LayoutParams attrs = getWindow().getAttributes();
                    attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
                    attrs.flags &= ~WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                    getWindow().setAttributes(attrs);
                    if (android.os.Build.VERSION.SDK_INT >= 21)
                    {
                        //noinspection all
                        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    }
                }

            }
        });

        video.setWebChromeClient(webChromeClient);
        // Call private class InsideWebViewClient
        video.setWebViewClient(new InsideWebViewClient());

        video.loadUrl("file:///android_asset/video.html");
        video.getSettings().setJavaScriptEnabled(true);

//      untuk set webView saat diload pertamakali
        video.setWebViewClient(new WebViewClient(){

            // method loadPageFinish untuk set semua asset yang ada sebelum selesai di tampilkan
            @Override
            public void onPageFinished(WebView view, String url) {

                String Google = Detail.getVideo();//Masukan string video 1 disini
                String GDrive = Detail.getVideo1();//Masukan string video 2 disini
                String YouDrive = Detail.getVideo2();//Masukan string video 3 disini
//              panggil fungsi loadUrl lalu buat javascript untuk menganti atribute dari iframe dengan id iframe
//              berlaku juga untuk sytax html lain
                video.loadUrl("javascript:(function(){" +
                        "document.getElementById('Google').src ='"+ Google+"'})()");
                video.loadUrl("javascript:(function(){" +
                        "document.getElementById('GDrive').src ='"+ GDrive+"'})()");
                video.loadUrl("javascript:(function(){" +
                        "document.getElementById('YouDrive').src ='"+ YouDrive+"'})()");
            }
        });

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri url = Uri.parse("https://www.facebook.com/animeyou.net/");


                Intent intent = new Intent(Intent.ACTION_VIEW, url);
                startActivity(intent);


            }
        });
        ig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri url = Uri.parse("https://www.instagram.com/animeyou_net/");


                Intent intent = new Intent(Intent.ACTION_VIEW, url);
                startActivity(intent);


            }
        });

    }

    private class InsideWebViewClient extends WebViewClient {
        @Override
        // Force links to be opened inside WebView and not in Default Browser
        // Thanks http://stackoverflow.com/a/33681975/1815624
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    public void onBackPressed()
    {
        // Notify the VideoEnabledWebChromeClient, and handle it ourselves if it doesn't handle it
        if (!webChromeClient.onBackPressed())
        {
            if (video.canGoBack())
            {
                video.goBack();
            }
            else
            {
                // Standard back button implementation (for example this could close the app)
                super.onBackPressed();

            }
        }
    }
}
