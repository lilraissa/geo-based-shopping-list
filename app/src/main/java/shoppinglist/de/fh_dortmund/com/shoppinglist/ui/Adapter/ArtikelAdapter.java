package shoppinglist.de.fh_dortmund.com.shoppinglist.ui.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;
import shoppinglist.de.fh_dortmund.com.shoppinglist.model.Artikel;


public class ArtikelAdapter  extends RealmBaseAdapter<Artikel> implements ListAdapter {
    private static class ViewHolder {
        TextView text;
    }

    public ArtikelAdapter(OrderedRealmCollection<Artikel> realmResults) {
        super(realmResults);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(android.R.layout.simple_list_item_1, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.text = (TextView) convertView.findViewById(android.R.id.text1);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (adapterData != null) {
            Artikel artikel = adapterData.get(position);
            viewHolder.text.setText(artikel.getName());
        }
        return convertView;

    }
}