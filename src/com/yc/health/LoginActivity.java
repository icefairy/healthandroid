package com.yc.health;

import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.ui.BindView;

import com.yc.health.fragment.PersonalPopupWindow;
import com.yc.health.fragment.TextPopupWindow;
import com.yc.health.http.HttpUserRequest;
import com.yc.health.manager.ActivityManager;
import com.yc.health.util.Method;
import com.yc.health.widget.CustomToast;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow.OnDismissListener;

public class LoginActivity extends KJActivity implements OnGestureListener{

	@BindView(id = R.id.login_back, click = true)
	private ImageView backBtn;
	@BindView(id = R.id.login_loginName, click = true)
	private EditText loginName;
	@BindView(id = R.id.login_password, click = true)
	private EditText password;
	@BindView(id = R.id.login_info, click = true)
	private Button infoBtn; //入会需知
	@BindView(id = R.id.login_forgetPsw, click = true)
	private Button forgetPswBtn; //忘记密码
	@BindView(id = R.id.login_login, click = true)
	private ImageView loginBtn;
	
	private TextPopupWindow textWindow;
	private SharedPreferences userPreferences;
	
	private PersonalPopupWindow menuWindow = null;
	private GestureDetector gestureDetector;
	
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if ( msg.what == 1 ) {
				String message = (String) msg.obj;
				if ( "noUser".equals(message) ) {
					CustomToast.showToast(aty, "用户不存在！", 6000);
				} else if ( "pwdIsWrong".equals(message) ) {
					CustomToast.showToast(aty, "密码错误！", 6000);
				} else if ( "login".equals(message) ) {
					LoginActivity.this.finish();
					
					Editor editor = userPreferences.edit();
					editor.putString("loginName", loginName.getText().toString());
					editor.commit();
				}
			}//t
			super.handleMessage(msg);
		}
	};
	
	@Override
	public void setRootView() {
		setContentView(R.layout.login);
	}

	@SuppressLint("WorldReadableFiles")
	@SuppressWarnings("deprecation")
	@Override
	public void initData() {
		super.initData();
		
		ActivityManager.getInstace().addActivity(aty);
		
		userPreferences = getSharedPreferences("user", MODE_WORLD_READABLE);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void initWidget() {
		super.initWidget();
		
		gestureDetector = new GestureDetector(this); // 手势滑动
	}

	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		
		switch(v.getId()) {
		case R.id.login_back:
			if ( ActivityManager.getInstace().isEmpty() ) {
				this.showActivity(aty, HomeActivity.class);
			}
			this.finish();
			break;
		case R.id.login_forgetPsw:
			if ( textWindow == null ) {
				textWindow = new TextPopupWindow(aty,4);//显示窗口  
				textWindow.showAtLocation(this.findViewById(R.id.login), 
	            		Gravity.CENTER, 0, 0); 
				textWindow.setOnDismissListener(new OnDismissListener(){
					@Override
					public void onDismiss() {
						Method method = new Method(aty);
						method.backgroundAlpha(1f);
						textWindow = null;
					}
	            });
			}
			break;
		case R.id.login_info:
			if ( textWindow == null ) {
				textWindow = new TextPopupWindow(aty,3);//显示窗口  
				textWindow.showAtLocation(this.findViewById(R.id.login), 
	            		Gravity.CENTER, 0, 0); 
				textWindow.setOnDismissListener(new OnDismissListener(){
					@Override
					public void onDismiss() {
						Method method = new Method(aty);
						method.backgroundAlpha(1f);
						textWindow = null;
					}
	            });
			}
			break;
		case R.id.login_login:
			HttpUserRequest request = new HttpUserRequest(aty,mHandler,1);
			request.loginInit(loginName.getText().toString(), password.getText().toString());
			request.start();
			break;
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return gestureDetector.onTouchEvent(event);
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		gestureDetector.onTouchEvent(ev);
		super.dispatchTouchEvent(ev);
		return false;
	}
	
	@Override
	public boolean onDown(MotionEvent arg0) {
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float arg2,
			float arg3) {
		if ( (e2.getX() - e1.getX()) > 80 && Math.abs(e2.getY() - e1.getY()) < 80 ) {
			if ( menuWindow == null ) {
				menuWindow = new PersonalPopupWindow(aty);//显示窗口  
	            menuWindow.showAtLocation(this.findViewById(R.id.login), 
	            		Gravity.LEFT | Gravity.BOTTOM, 0, 0); 
	            menuWindow.setOnDismissListener(new OnDismissListener(){
					@Override
					public void onDismiss() {
						Method method = new Method(aty);
						method.backgroundAlpha(1f);
						menuWindow = null;
					}
	            });
			}
		}
		return true;
	}

	@Override
	public void onLongPress(MotionEvent arg0) {
	}

	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent arg0) {
	}

	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {
		return false;
	}
}
