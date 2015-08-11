package com.example.testcustomlayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View ;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DurationControl extends LinearLayout implements View.OnClickListener{
	private static final String tag ="DurationControl";
	private Calendar fromDate = null;
	private Calendar toDate = null ;
	
	//1: days , 2 : weeks
	private static int ENUM_DAYS=1 ;
	private static int ENUM_WEEKS=2 ;
	private int durationUnits = ENUM_DAYS;
	
	///////////////////////////////////////////
	public DurationControl(Context context){
		super(context);
		initialize(context);
	}
	public DurationControl(Context context , AttributeSet attrs){
		this(context , attrs , 0);
	}
	
	public DurationControl(Context context , AttributeSet attrs , int defStyle){
		super(context , attrs , defStyle);
		TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.DurationControl,0,0);
		durationUnits = t.getInt(R.styleable.DurationControl_durationUnits , durationUnits);
		t.recycle();
		initialize(context);
	}
	
	private void initialize(Context context){
		LayoutInflater lif= (LayoutInflater)context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = lif.inflate(R.layout.duration_view_layout, this);
		Button b=(Button)v.findViewById(R.id.fromButton);
		b.setOnClickListener(this);
		Button z=(Button)v.findViewById(R.id.toButton);
		z.setOnClickListener(this);
		this.setSaveEnabled(true);
	}
	
	private FragmentManager getFragmentManager(){
		Context c = getContext();
		if(c instanceof Activity){
			return ((Activity)c).getFragmentManager();
		}
		throw new RuntimeException("Activity context expected instead");
	}
	@Override
	protected void dispatchSaveInstanceState(SparseArray<Parcelable> container){
		//Don't call this so that children won't be explicitly saved
		//super.dispatchSaveInstancesState(container);
		//Call your self onSavedInstanceState
		Log.d(tag , "in dispatchSaveinstanceState");
		super.dispatchFreezeSelfOnly(container);
	}
	@Override
	protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container){
		//Don't call this so that children won't be explicitly saved
		//super.dispatchRestoreInstanceState(container);
		Log.d(tag , "in dispatchRestoreInstanceState");
		super.dispatchThawSelfOnly(container);
	}
	@Override
	protected void onRestoreInstanceState(Parcelable state){
		Log.d(tag ,"in onRestoreInstanceState");
		if(!(state instanceof SavedState)){
			
		}
	}
	////////Saved State inner static class
	public static class SavedState extends BaseSavedState{
		// null values are allowed
		private Calendar fromDate;
		private Calendar toDate;
		
		SavedState(Parcelable superState){
			super(superState);
		}
		
		@Override
		public void writeToParcel(Parcel out , int flags){
			super.writeToParcel(out, flags);
			
			if(fromDate != null){
				out.writeLong(fromDate.getTimeInMillis());
			}else{
				out.writeLong(-1L);
			}
			if(toDate !=null){
				out.writeLong(toDate.getTimeInMillis());
			}else{
				out.writeLong(-1L);
			}
		}
		
		@Override
		public String toString(){
			StringBuffer sb= new StringBuffer("fromDate:"
					+DurationControl.getDateString(fromDate));
			sb.append("toDate:"+DurationControl.getDateString(toDate));
			
			return sb.toString();
		}
		
		//read back the values
		private SavedState(Parcel in){
			super(in);
			
			//Read the from date
			long lFromDate = in.readLong();
			if(-1==lFromDate){
				fromDate=null;
			}else{
				fromDate=Calendar.getInstance();
				fromDate.setTimeInMillis(lFromDate);
			}
			long lToDate = in.readLong();
			if(-1==lToDate){
				toDate=null;
			}else{
				toDate=Calendar.getInstance();
				toDate.setTimeInMillis(lToDate);
			}
		}
		
		@SuppressWarnings("hiding")
		public static final Parcelable.Creator<SavedState> CREATOR=
			new Parcelable.Creator<SavedState>() {
				public SavedState createFromParcel(Parcel in){
					return new SavedState(in);
				}
				
				public SavedState[] newArray(int size){
					return new SavedState[size];
				}
			};
	}
	
	
	
	//////////////////////////////////////////////
	@Override
	public void onClick(View v){
		Button b = (Button)v;
		if(R.id.fromButton==b.getId()){
			DialogFragment newFragment = new DatePickerFragment(this
					, R.id.fromButton);
			newFragment.show(getFragmentManager() , "com.example.testcustomlayout");
			return;
		}
		//Otherwise
		DialogFragment newFragment = new DatePickerFragment(this,R.id.toButton);
		newFragment.show(getFragmentManager(), "com.example.testcustomlayout");
		return;
	}
	
	public void onDateSet(int buttonId , int year , int month ,int day){
		Calendar c= getDate(year , month , day);
		if(buttonId==R.id.fromButton){
			setFromDate(c);
			return ;
		}
		setToDate(c);
	}
	
	private Calendar getDate(int year , int month , int day){
		Calendar c =Calendar.getInstance();
		c.set(year, month, day);
		return c ;
	}
	
	private void setFromDate(Calendar c){
		if(null==c){
			return;
		}
		this.fromDate=c;
		TextView tc=(TextView)findViewById(R.id.fromDate);
		tc.setText(getDateString(c));
	}
	
	private void setToDate(Calendar c){
		if(null==c){
			return ;
		}
		this.toDate= c;
		TextView tc=(TextView)findViewById(R.id.toDate);
		tc.setText(getDateString(c));
	}
	
	public static String getDateString(Calendar c){
		if(null==c){
			return "null";
		}
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyy");
		df.setLenient(false);
		String s=df.format(c.getTime());
		return s;
	}
	
	public long getDuration(){
		if(!validate()){
			return -1 ;
		}
		long fromMillis = fromDate.getTimeInMillis();
		long toMillis = toDate.getTimeInMillis();
		long diff = toMillis-fromMillis ;
		long day = 24*60*60*1000;
		long diffInDays = diff/day;
		long diffInWeeks = diff/(day*7);
		if(ENUM_DAYS==durationUnits ){
			return diffInDays;
		}
		return diffInWeeks ;
	}
	
	public boolean validate(){
		if(fromDate==null || toDate==null){
			return false;
		}
		if(toDate.after(fromDate)){
			return true;
		}
		return false;
	}
}
