package shoppinglist.de.fh_dortmund.com.shoppinglist.ui.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.realm.RealmList;
import shoppinglist.de.fh_dortmund.com.shoppinglist.R;
import shoppinglist.de.fh_dortmund.com.shoppinglist.model.Artikel;
import shoppinglist.de.fh_dortmund.com.shoppinglist.model.Kategorie;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<Kategorie> deptList; // header titles
    // child data in format of header title, child title
   // private HashMap<String, List<String>> _listDataChild;


    public ExpandableListAdapter(Context context,  ArrayList<Kategorie> deptList ) {
        this.context = context;
        this.deptList = deptList;

    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        List<Artikel> productList =
                deptList.get(groupPosition).getDieArtikel();
        return productList.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View view, ViewGroup parent) {

        Artikel artikel = (Artikel) getChild(groupPosition, childPosition);

        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.list_item, null);

            /*ImageView  imageviewGps = (ImageView) view.findViewById(R.id.gps_id);
            imageviewGps.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    Uri gmmIntentUri = Uri.parse("geo:37.7749,-122.4194");
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
                        context.startActivity(mapIntent);
                    }
                }
            });*/
        }

        TextView txtListChild = (TextView) view
                .findViewById(R.id.lblListItem);

        txtListChild.setText(artikel.getName().trim());
        return view;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        /*RealmList<Artikel> productList =
                deptList.get(groupPosition).getProductList();
        return productList.size();*/
        if(deptList.get(groupPosition).getDieArtikel() != null) {
            return deptList.get(groupPosition).getDieArtikel().size();
        }
        return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return deptList.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return deptList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View view, ViewGroup parent) {
        Kategorie kategorie = (Kategorie) getGroup(groupPosition);
        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater)context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.list_kategorie, null);
        }

        TextView lblListHeader = (TextView) view
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(kategorie.getName().trim());

        return view;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void setDeptList(ArrayList<Kategorie> deptList) {
        this.deptList = deptList;
        notifyDataSetChanged();
    }
}
