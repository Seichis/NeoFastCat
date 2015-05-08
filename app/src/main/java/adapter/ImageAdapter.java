package adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Image adapter to return the bitmap to the gridview in the game
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    int count;
    static List<Bitmap> mBitmaps;


    // Constructor
    public ImageAdapter(Context c) {
        mContext = c;
        count = 0;
        mBitmaps = new ArrayList<>();
        Log.i("Adapter", " Construct ");

    }


    public int getCount() {

        return mBitmaps.size();

    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView imageView;

        imageView = new ImageView(mContext);
        imageView.setLayoutParams(new GridView.LayoutParams(100, 100));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setPadding(8, 8, 8, 8);

        imageView.setImageBitmap(mBitmaps.get(position));
        Log.i("Adapter", " if  " + position);
        return imageView;

    }


    public static void addImage(Bitmap bm) {

        mBitmaps.add(bm);
        Log.i("Adapter", " Add image  " + mBitmaps.size());

    }
}
