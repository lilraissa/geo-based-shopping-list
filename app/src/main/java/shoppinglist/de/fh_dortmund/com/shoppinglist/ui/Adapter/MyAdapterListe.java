package shoppinglist.de.fh_dortmund.com.shoppinglist.ui.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.TwoLineListItem;

import java.util.ArrayList;
import java.util.List;

import shoppinglist.de.fh_dortmund.com.shoppinglist.model.Liste;

public class MyAdapterListe extends BaseAdapter{
    private Context context;
    private List<Liste> liste;

    public MyAdapterListe(Context context, List<Liste> dieListe) {
        this.context = context;
        this.liste = dieListe;
    }

    @Override
    public int getCount() {
        return liste.size();
    }

    @Override
    public Liste getItem(int position) {
        return liste.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TwoLineListItem  twoLineListItem;


        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            twoLineListItem = (TwoLineListItem) inflater.inflate(
                    android.R.layout.simple_list_item_2, null);
        } else {
            twoLineListItem = (TwoLineListItem) convertView;
        }

        TextView text1 = twoLineListItem.getText1();
        TextView text2 = twoLineListItem.getText2();

        text1.setText(liste.get(position).getName());
        text2.setText("" + liste.get(position).getLocationName());
        text2.setTextColor(Color.parseColor("#6bb4d3"));
        text2.setTypeface(null, Typeface.ITALIC);
        return twoLineListItem;
    }

    public void setListe(List<Liste> liste) {
        this.liste = liste;
        notifyDataSetChanged();
    }

}
