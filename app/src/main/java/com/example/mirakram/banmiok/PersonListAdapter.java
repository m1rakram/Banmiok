package com.example.mirakram.banmiok;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Mirakram on 6/24/2017.
 */

public class PersonListAdapter extends ArrayAdapter<Person> {
    private static final String TAG = "PersonListAdapter";

    private Context mContext;
    private int mResource;
    private int lastPosition = -1;

    /**
     * Holds variables in a View
     */
    private static class ViewHolder {
        TextView name;
        TextView score;
        TextView perc;
        ImageView byting;
        TextView n;
    }

    /**
     * Default constructor for the PersonListAdapter
     * @param context
     * @param resource
     * @param objects
     */
    public PersonListAdapter(Context context, int resource, ArrayList<Person> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //get the persons information
        String name = getItem(position).getName();
        String score = getItem(position).getScore();
        String perc = getItem(position).getPerc();
        String byting = getItem(position).getByting();
        String n = getItem(position).getN();
        //Create the person object with the information
        Person person = new Person(name, score, perc, byting, n);

        //create the view result for showing the animation
        final View result;

        //ViewHolder object
        ViewHolder holder;

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);
        holder = new ViewHolder();
        holder.name = (TextView) convertView.findViewById(R.id.textView50);
        holder.score = (TextView) convertView.findViewById(R.id.textView51);
        holder.perc = (TextView) convertView.findViewById(R.id.textView52);
        holder.byting = (ImageView) convertView.findViewById(R.id.imaging);
        holder.n=(TextView) convertView.findViewById(R.id.textView70);

        result = convertView;

        convertView.setTag(holder);


        holder.name.setText(person.getName());
        holder.score.setText(person.getScore());
        holder.perc.setText(person.getPerc());
        holder.byting.setImageBitmap(StringToBitMap(person.getByting()));
        holder.n.setText(person.getN());



        return convertView;
    }

    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

}
