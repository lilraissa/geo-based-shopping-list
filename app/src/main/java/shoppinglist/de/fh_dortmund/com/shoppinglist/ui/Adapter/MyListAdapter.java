package shoppinglist.de.fh_dortmund.com.shoppinglist.ui.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Set;

import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;
import shoppinglist.de.fh_dortmund.com.shoppinglist.R;
import shoppinglist.de.fh_dortmund.com.shoppinglist.model.Item;

public class MyListAdapter extends RealmBaseAdapter<Item> implements ListAdapter
{
    private static class ViewHolder{
        TextView countText;
        CheckBox deleCheckBox;
    }

    private boolean inDeletionMode = false;
    private Set<Integer> countersToDelete = new HashSet<Integer>();

    public MyListAdapter(OrderedRealmCollection<Item> realmResults){
        super(realmResults);
    }

    public void enableDeletionMode(boolean enabled){
        inDeletionMode =  enabled;
        if (!enabled){
            countersToDelete.clear();;
        }
        notifyDataSetChanged();
    }

    public Set<Integer> getCountersToDelete() {
        return countersToDelete;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.row, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.countText = (TextView) convertView.findViewById(R.id.textview);
            viewHolder.deleCheckBox = (CheckBox) convertView.findViewById(R.id.checkBox);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if(adapterData != null){
            final Item item = adapterData.get(position);
            viewHolder.countText.setText(item.getCountString());
            if (inDeletionMode){
                viewHolder.deleCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        countersToDelete.add(item.getId());
                    }
                });
            }else {
                viewHolder.deleCheckBox.setOnCheckedChangeListener(null);
            }
            viewHolder.deleCheckBox.setChecked(countersToDelete.contains(item.getId()));
            viewHolder.deleCheckBox.setVisibility(inDeletionMode ? View.VISIBLE : View.GONE);

        }
        return convertView;
    }
}
