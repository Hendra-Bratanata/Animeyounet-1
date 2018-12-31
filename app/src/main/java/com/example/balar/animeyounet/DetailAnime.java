package com.example.balar.animeyounet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

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
    /*@BindView(R.id.dtGenre)
    TextView genre;*/

    @BindView(R.id.video)
    WebView video;

    public Anime Detail;

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

    }
}
