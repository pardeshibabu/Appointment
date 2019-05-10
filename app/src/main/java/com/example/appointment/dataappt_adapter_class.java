package com.example.appointment;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class dataappt_adapter_class extends ArrayAdapter<DataApptClass> {
    private Activity context;
    private List<DataApptClass> dataapptList;

    public dataappt_adapter_class(Activity context, List<DataApptClass> dataapptList) {
        super(context, R.layout.list_view_myappointment,dataapptList);
        this.context = context;
        this.dataapptList = dataapptList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView,@NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View ListViewItem = inflater.inflate(R.layout.list_view_myappointment,null,true);

        TextView name = (TextView)ListViewItem.findViewById(R.id.ApptDetailName);
        TextView ci = (TextView)ListViewItem.findViewById(R.id.ApptDetailCI);
        TextView starttime = (TextView)ListViewItem.findViewById(R.id.ApptDetailstrttime);
        TextView endtime = (TextView)ListViewItem.findViewById(R.id.ApptDetailendtime);
        TextView day = (TextView)ListViewItem.findViewById(R.id.ApptDetailday);
        TextView reason = (TextView)ListViewItem.findViewById(R.id.ApptDetailReason);
        TextView type = (TextView)ListViewItem.findViewById(R.id.ApptDetailAppttype);

        DataApptClass obj = dataapptList.get(position);

        name.setText(obj.getResponderName());
        ci.setText(obj.getResponderId());
        starttime.setText(obj.getApptStartTime());
        endtime.setText(obj.getApptEndTime());
        day.setText(obj.getApptDay());
        reason.setText(obj.getMessagebody());
        type.setText(obj.getApptType());

        return  ListViewItem;
    }
}
