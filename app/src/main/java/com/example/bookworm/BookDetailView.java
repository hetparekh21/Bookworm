package com.example.bookworm;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class BookDetailView extends AppCompatActivity {

    private String title ;
    private String subtitle ;
    private String publisher ;
    private String publishedDate ;
    private String authors;
    private String description ;
    private Bitmap bitmapimage ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail_view);

        Bundle b = getIntent().getExtras();
        if (b != null) {

            this.title = b.getString("title");
            this.authors = b.getString("author");
            this.description = b.getString("description");
            this.publishedDate = b.getString("publishdate");
            this.publisher = b.getString("publisher");
            this.subtitle = b.getString("subtitle");
            this.bitmapimage = b.getParcelable("thumbnail");

        }

        TextView detail_title = findViewById(R.id.detail_title) ;
        detail_title.setText(detail_title.getText() + title);

        TextView detail_subtitle = findViewById(R.id.detail_subtitle) ;
        detail_subtitle.setText(detail_subtitle.getText() + subtitle);

        TextView detail_author = findViewById(R.id.detail_author) ;
        detail_author.setText(detail_author.getText() + authors);

        TextView detail_publisher = findViewById(R.id.detail_publisher) ;
        detail_publisher.setText(detail_publisher.getText() + publisher);

        TextView detail_publishdate = findViewById(R.id.detail_Publishdate) ;
        detail_publishdate.setText(detail_publishdate.getText() + publishedDate);

        TextView detail_description = findViewById(R.id.detail_description) ;
        detail_description.setText(detail_description.getText() + description);

        ImageView imageView = findViewById(R.id.detail_image);

        if (bitmapimage != null){

            imageView.setImageBitmap(bitmapimage);

        }else{

            imageView.setImageResource(0);
            TextView no_image = findViewById(R.id.no_image);
            no_image.setVisibility(View.VISIBLE);

        }

    }
}