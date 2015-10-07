package com.sthybrid.fiftytonemap.myUI;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sthybrid.fiftytonemap.R;

/**
 * 
 * @author ºúÑó
 * @date 2015/9/1
 *
 */

@SuppressLint("InflateParams") public class ErrorListAdapter extends BaseAdapter{

	private List<ErrorListItem> list;
	Context context;

	public ErrorListAdapter(Context context, List<ErrorListItem> list){
		this.context = context;
		this.list = list;
	}


	@Override
	public int getCount() {
		return (null == list)?0:list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ErrorListItem errorListItem = (ErrorListItem)getItem(position);
		ViewHolder viewHolder = null;
		if( null == convertView){
			convertView = LayoutInflater.from(context).inflate(R.layout.error_list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.errorItemName = (TextView)convertView.findViewById(R.id.error_item_name);
			viewHolder.errorPercent = (TextView)convertView.findViewById(R.id.error_percent);
			viewHolder.errorPercentBar = (ProgressBar)convertView.findViewById(R.id.error_percent_bar);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder)convertView.getTag();
		}
		
		viewHolder.errorItemName.setText(errorListItem.getName());
		viewHolder.errorPercent.setText(String.valueOf(errorListItem.getErrorPercent()) + "%");
		viewHolder.errorPercentBar.setProgress(errorListItem.getErrorPercent());
		return convertView;
	}
	
	class ViewHolder{
		TextView errorItemName;
		ProgressBar errorPercentBar;
		TextView errorPercent;
	}
}
