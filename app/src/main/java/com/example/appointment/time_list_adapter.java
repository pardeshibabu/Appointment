package com.example.appointment;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class time_list_adapter extends ArrayAdapter {
    private Activity context;
    private List<date> dateList;

    public time_list_adapter(Activity context, List<date> dateList) {
        super(context,R.layout.list_date_time_layout,dateList);
        this.context = context;
        this.dateList = dateList;
    }
    @NonNull
    @Override
    public View getView(int position,View convertView,@NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
//        View v=convertView;

//        if(v == null){
//           v =  v.inflate(R.layout.list_date_time_layout,parent,true);
//        }
        View ListViewItem = inflater.inflate(R.layout.list_date_time_layout,null,true);

        TextView date1 = (TextView)ListViewItem.findViewById(R.id.FreetimeDate);
        TextView from1 = (TextView)ListViewItem.findViewById(R.id.FreetimeFrom);
        TextView to1 = (TextView)ListViewItem.findViewById(R.id.FreetimeTo);

        date obj = dateList.get(position);

        date1.setText(obj.getTime_date());
        from1.setText(obj.getTime_From());
        to1.setText(obj.getTime_To());

        return ListViewItem;
    }


}
