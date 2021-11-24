package com.example.labapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ItemAdapter extends ArrayAdapter<Item> {

    private LayoutInflater mInflater;
    private List<Item> mItem;
    Context mContext;

    private static class ViewHolder{
        TextView txtNames;
        TextView txtCompany;
        TextView txtPost;
    }

    public ItemAdapter(Context context, List<Item> mItems){
        super(context, R.layout.list_item, mItems);
        this.mItem = mItems;
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
    }

    public void UpdateList(List<Item> data){
        mItem = data;
        notifyDataSetChanged();
    }

    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder viewHolder;

        if(convertView == null){
            convertView = mInflater.inflate(R.layout.list_item,null);
            viewHolder = new ViewHolder();
            viewHolder.txtNames = (TextView) convertView.findViewById(R.id.name_text);
            viewHolder.txtCompany = (TextView) convertView.findViewById(R.id.company_text);
            viewHolder.txtPost = (TextView) convertView.findViewById(R.id.post_text);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Item obj = this.mItem.get(position);
        viewHolder.txtNames.setText(obj.getName());
        viewHolder.txtCompany.setText(obj.getCompany());
        viewHolder.txtPost.setText(obj.getPost());

        return convertView;
    }
}
