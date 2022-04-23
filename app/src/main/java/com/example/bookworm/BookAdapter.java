package com.example.bookworm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class BookAdapter extends ArrayAdapter<BookObject> {


    public BookAdapter(@NonNull Context context, @NonNull ArrayList<BookObject> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View root = convertView ;

        if (root == null) {
            root = LayoutInflater.from(getContext()).inflate(
                    R.layout.list, parent, false);
        }

        BookObject bookObject = getItem(position) ;

        // set the title
        TextView title = root.findViewById(R.id.title);
        title.setText(bookObject.getTitle());

        // set the publisher
        TextView publisher = root.findViewById(R.id.subtitle);
        publisher.setText(bookObject.getSubtitle());

        // set thumbnail
        ImageView thumbnail = root.findViewById(R.id.thumbnail);

        if (bookObject.getBookThumbnail() == null){

            thumbnail.setImageResource(0);
            thumbnail.setVisibility(View.GONE);
            root.findViewById(R.id.no_image).setVisibility(View.VISIBLE);

        }else{

            thumbnail.setImageBitmap(bookObject.getBookThumbnail());

        }

        return root ;
    }
}
