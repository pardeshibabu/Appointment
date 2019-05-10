package com.example.appointment;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class professor_list_Adapter extends ArrayAdapter<RegisterSaveData> {

    private Activity context;
    private List<RegisterSaveData> professorList;

    public professor_list_Adapter(Activity context, List<RegisterSaveData> professorList) {
        super(context,R.layout.professor_list_layout,professorList);
        this.context = context;
        this.professorList = professorList;
    }

    @NonNull
    @Override
    public View getView(int position,View convertView,@NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View ListViewItem = inflater.inflate(R.layout.professor_list_layout,null,true);

        TextView textviewName = (TextView)ListViewItem.findViewById(R.id.textView1);
        TextView textViewName2 = (TextView)ListViewItem.findViewById(R.id.textView2);
        TextView textViewName3 = (TextView)ListViewItem.findViewById(R.id.textView3);

        RegisterSaveData obj = professorList.get(position);

        textviewName.setText(obj.getRegister_name());
        textViewName2.setText(obj.getRegister_collegeid());
        textViewName3.setText(obj.getRegister_email());
        return ListViewItem;
    }
}
