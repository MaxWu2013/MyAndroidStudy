package com.example.myandroidstores.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.example.myandroidstores.R;
/**
 * 对话框管理器，用于创建对话框，使用对话框
 * @author ASRock
 *
 */
public class DialogManager {
	
	/**
	 * 对话框实例
	 */
	public Dialog dlg ;
	/**
	 * 对话框rootView 
	 */
	private ViewGroup rootView;
	/**
	 * 当前显示的内容
	 */
	private View contentView ;
	
	/**
	 * 创建对话框---可以做为工具使用
	 * @param context
	 * @param view
	 * @return
	 */
	public static Dialog createCustomDialog(Context context , View view ){
		Dialog dialog = new Dialog(context , R.style.theme_customDialog);
		dialog.setContentView(view);
		return dialog;
	}
	/**
	 * 初始化对话框工具
	 * @param activity
	 */
	public DialogManager(Activity activity){
		initDialog(activity);
	}
	
	/**
	 * 初始化对话框
	 * @param context
	 */
	private void initDialog(Context context){
		if(null == dlg){
			rootView = new FrameLayout(context);
			dlg = createCustomDialog(context, rootView);
			WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
			lp.copyFrom(dlg.getWindow().getAttributes());
			lp.width=WindowManager.LayoutParams.MATCH_PARENT;
			lp.height=WindowManager.LayoutParams.MATCH_PARENT;
			dlg.getWindow().setAttributes(lp);
		}
	}
	
	/**
	 * 显示对话框
	 * @param contentView
	 * @param showCallback
	 */
	public void showDialog(View contentView , OnShowDialog showCallback){
		if(null == contentView){
			return ;
		}
		
		if(this.contentView != contentView){
			rootView.removeAllViews();
			rootView.addView(contentView);
			this.contentView = contentView;
			if(null != showCallback){
				showCallback.onAttachToDialogView(dlg, contentView);
			}
		}
		
		if(null != showCallback){
			showCallback.beforeShowDialog(dlg, contentView);
		}
		dlg.show();
	}
	
	
	/**
	 * 隐藏对话框
	 */
	public void dismissDlg(){
		if(null != dlg && dlg.isShowing()){
			dlg.cancel();
		}
	}
	
	/**
	 * 在 显示对话框的操作
	 * @author ASRock
	 *
	 */
	public interface OnShowDialog{
		/**
		 * contentView 添加到对话框的回调
		 * @param dlg
		 * @param dlgView  
		 */
		public void onAttachToDialogView(Dialog dlg , View contentView);
		
		/**
		 * 在显示对话框之前的回调，可以动态改变内容
		 * @param dlg
		 * @param dlgView
		 */
		public void beforeShowDialog(Dialog dlg , View contentView);
	}
}
