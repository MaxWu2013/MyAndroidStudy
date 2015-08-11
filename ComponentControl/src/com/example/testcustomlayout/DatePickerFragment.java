package com.example.testcustomlayout;

import java.util.Calendar;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;

public class DatePickerFragment extends DialogFragment 
	implements DatePickerDialog.OnDateSetListener 
{
	public static String tag="DatePickerFragment";
	private DurationControl parent ;
	private int buttonId;
	
	public DatePickerFragment(DurationControl inParent , int inButtonId){
		parent = inParent;
		buttonId = inButtonId;
		
		Bundle argsBundle=this.getArguments();
		if(argsBundle == null){
			argsBundle=new Bundle();
		}
		argsBundle.putInt("parentid", inParent.getId());
		argsBundle.putInt("buttonid", buttonId);
		this.setArguments(argsBundle);
	}
	
	//Notice : Default constructor for device rotation
	public DatePickerFragment(){}
	
	@Override
	public Dialog onCreateDialog(Bundle saveinstanceState){
		//this.establishParent();
		//Use the current date as the default date in the picker
		final Calendar c= Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		
		//create a new instance of DatePickerDialog and return it 
		return new DatePickerDialog(getActivity() , this , year , month , day	);
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		// Do something with the date chosen by the user
		parent.onDateSet(buttonId, year, monthOfYear, dayOfMonth);
		
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		Log.d(tag , "DatePickerFragment on Activity created called");
		this.establishParent();
	}
	
	private void establishParent(){
		if(null != parent){
			return;
		} 
		Log.d(tag, "establishing parent");
		int parentid = this.getArguments().getInt("parentid");
		buttonId=this.getArguments().getInt("buttonid");
		View x = this.getActivity().findViewById(parentid);
		if(null == x){
			throw new RuntimeException("Sorry not able to establish parent on restart");
		}
		parent =(DurationControl)x;
	}
}
