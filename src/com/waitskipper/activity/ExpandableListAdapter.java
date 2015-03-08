package com.waitskipper.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import com.waitskipper.R;
import com.waitskipper.logic.IEstablishmentListener;
import com.waitskipper.model.Establishment;

import java.util.ArrayList;

public class ExpandableListAdapter
        extends BaseExpandableListAdapter
        implements IEstablishmentListener
{
	private LayoutInflater inflater;
    private final ArrayList<Establishment> mEstablishments;

	public ExpandableListAdapter(Context context)
	{
        // Create Layout Inflator
        inflater = LayoutInflater.from(context);

        mEstablishments = new ArrayList<Establishment>();
    }

    public void establishmentsChanged(final ArrayList<Establishment> newEstablishments)
    {
        mEstablishments.clear();
        mEstablishments.addAll(newEstablishments);
        notifyDataSetChanged();

    }

    @Override
    public View getGroupView(
            int groupPosition,
            boolean isExpanded,
            View convertView,
            ViewGroup parentView)
    {
        final Establishment parent = mEstablishments.get(groupPosition);

        // Inflate grouprow.xml file for parent rows
        convertView = inflater.inflate(R.layout.establishmentlist, parentView, false);

        // Get grouprow.xml file elements and set values
        ((TextView) convertView.findViewById(R.id.name)).setText(parent.name);

        //Log.i("onCheckedChanged", "isChecked: "+parent.isChecked());

        return convertView;
    }

    // This Function used to inflate child rows view
    @Override
    public View getChildView(
            int groupPosition,
            int childPosition,
            boolean isLastChild,
            View convertView,
            ViewGroup parentView)
    {
        final Establishment parent = mEstablishments.get(groupPosition);

        // Inflate childrow.xml file for child rows
        convertView = inflater.inflate(R.layout.establishment, parentView, false);

        // Get childrow.xml file elements and set values
        ((TextView) convertView.findViewById(R.id.name)).setText(parent.name);

        return convertView;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition)
    {
        //Log.i("Childs", groupPosition+"=  getChild =="+childPosition);
        return mEstablishments.get(groupPosition);
    }

    //Call when child row clicked
    @Override
    public long getChildId(int groupPosition, int childPosition)
    {
        return childPosition;
    }

    @Override
    public int getChildrenCount(int groupPosition)
    {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition)
    {
        return mEstablishments.get(groupPosition);
    }

    @Override
    public int getGroupCount()
    {
        return mEstablishments.size();
    }

    //Call when parent row clicked
    @Override
    public long getGroupId(int groupPosition)
    {
        return groupPosition;
    }

    @Override
    public void notifyDataSetChanged()
    {
        // Refresh List rows
        super.notifyDataSetChanged();
    }

    @Override
    public boolean isEmpty()
    {
        return ((mEstablishments == null) || mEstablishments.isEmpty());
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition)
    {
        return true;
    }

    @Override
    public boolean hasStableIds()
    {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled()
    {
        return true;
    }
}
