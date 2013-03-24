package com.skymobi.cac.maopao.passport.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.skymobi.cac.maopao.passport.android.util.Des;
import com.skymobi.cac.maopao.passport.android.util.StringUtils;
import com.skymobi.cac.maopao.passport.api.CurrentUser;
import com.skymobi.cac.maopao.passport.dao.base.CURDSqliteDao;
import com.skymobi.cac.maopao.passport.exception.ServiceException;

public class AccountDAO extends CURDSqliteDao{
	
	private static final String tag = "AccountDAO";
	
	public  static final String DES_KEY ="PpSKymBi";
	
	private final String []columns = new String[]{"loginname","password","is_auto_login","is_remember_pwd","cryptpwd","pwdlength","logintime"};
	
	public AccountDAO(Context ctx){
		super(ctx);
	}

	
	/**
	 * 只更新账号状态
	 * @param loginname 登录账号
	 * @param isAutoLogin 是否自动登录,1是
	 * @param isRememberPwd 是否记住密码,1是
	 * 
	 * */
	public void setAccountState(String loginname,int isAutoLogin,int isRememberPwd){
    	SQLiteDatabase db = this.getSQLite();
		ContentValues vals = new ContentValues();
		vals.put("is_auto_login", isAutoLogin);
		vals.put("is_remember_pwd",isRememberPwd);
		db.update(USER_INFO_TABLE, vals, "loginname=?", new String[]{loginname});
		close(db);
		Log.i(tag, "setAccountState success!");
	}
	
	public void setAccountPwd(String loginname,String pwd,byte[] cryptpwd) throws Exception{
    	SQLiteDatabase db = this.getSQLite();
		ContentValues vals = new ContentValues();
		vals.put("password", Des.toHexString(Des.encrypt(pwd,DES_KEY)));
		if(null !=cryptpwd){
		vals.put("cryptpwd",cryptpwd);}
		else{
			vals.put("cryptpwd","");
		}
		vals.put("pwdlength", pwd.length());
		db.update(USER_INFO_TABLE, vals, "loginname=?", new String[]{loginname});
		close(db);
		Log.i(tag, "setAccountPwd success!");
	}
	
	/**
	 * 更新账号密码
	 * @param loginname 登录账号
	 * @param pwd 明文密码
	 * */
	public void updatePwd(String loginname,String pwd) throws Exception {
    	SQLiteDatabase db = this.getSQLite();
		ContentValues vals = new ContentValues();
		vals.put("password", Des.toHexString(Des.encrypt(pwd,DES_KEY)));
		db.update(USER_INFO_TABLE, vals, "loginname=?", new String[]{loginname});
		close(db);
		Log.i(tag, "update password success!");
	}
	
	/**
	 * 根据登录账号获取账户信息
	 * @param loginname 登录的账号
	 * @return 
	 * */
	public CurrentUser getAccountByLoginName(String loginname){
		SQLiteDatabase db = getSQLite(readOnly);
		Cursor cursor =db.query(USER_INFO_TABLE, columns, "loginname=?", new String[]{loginname}, null, null,null);
		CurrentUser entity = null;
		while(cursor.moveToNext()){
			entity = new CurrentUser();
			entity.setLoginname(cursor.getString(0));
			entity.setPassword(cursor.getString(1));
			entity.setIsAutoLogin(cursor.getInt(2));
			entity.setIsRememberPwd(cursor.getInt(3));
			entity.setCryptpwd(cursor.getBlob(4));
			entity.setPwdlength(cursor.getInt(5));
			entity.setLoginTime(cursor.getString(6)); //获取上次登录时间
		}
		close(cursor,db);
		return entity;
	}
	/**
	 * 保存账号信息,如果已经存在，则更新
	 * */
	public void save(final CurrentUser entity)  throws Exception{
		if(StringUtils.isBlank(entity.getLoginname())){
			throw new ServiceException("登录账号不能为空!");
		}
		CurrentUser account = getAccountByLoginName(entity.getLoginname());
		if(account==null){
			createAccount(entity);
		}else{
			updateAccount(entity);
		}
	}
	
	/**
	 * 创建账号信息
	 * @param entity 账户实体
	 * */
	public void createAccount(final CurrentUser entity)throws Exception{
		ContentValues vals = new ContentValues();
		vals.put("loginname", entity.getLoginname());
		if(!StringUtils.isBlank(entity.getPassword())){
			vals.put("password", Des.toHexString(Des.encrypt(entity.getPassword(),DES_KEY)));
		}else{
			vals.put("password","");
		}
		vals.put("pwdlength",entity.getPwdlength());
		if(null !=entity.getCryptpwd()){
		vals.put("cryptpwd", entity.getCryptpwd());}
		else{
			vals.put("cryptpwd", "");
		}
		vals.put("is_auto_login", entity.getIsAutoLogin());
		vals.put("is_remember_pwd", entity.getIsRememberPwd());
		vals.put("logintime", (new Date()).getTime()+"");  //更新登录时间
		
		SQLiteDatabase db = this.getSQLite();
		db.insert(USER_INFO_TABLE, null, vals);
		close(db);	
	}
	
	/**
	 * 更新账号信息
	 * @param entity 账户实体
	 * */
	public void updateAccount(final CurrentUser entity) throws Exception{
		ContentValues vals = new ContentValues();
		if(!StringUtils.isBlank(entity.getPassword())){
			vals.put("password", Des.toHexString(Des.encrypt(entity.getPassword(),DES_KEY)));
		}else{
			vals.put("password","");
		}
		
		vals.put("pwdlength",entity.getPwdlength());
		if(null !=entity.getCryptpwd()){
			vals.put("cryptpwd", entity.getCryptpwd());}
			else{
				vals.put("cryptpwd", "");
		}
		vals.put("is_auto_login", entity.getIsAutoLogin());
		vals.put("is_remember_pwd", entity.getIsRememberPwd());
		vals.put("logintime", (new Date()).getTime()+""); //更新登录时间
		
		SQLiteDatabase db = this.getSQLite();
		db.update(USER_INFO_TABLE, vals, "loginname=?", new String[]{entity.getLoginname()});
		close(db);	
	}
	
	
	
	/**
	 * 获取最新登录的账号
	 * @return entity
	 * */
	public CurrentUser getLatestLoginAccount(){
		List<CurrentUser> accounts = this.getAccountList();
		if(accounts!= null && accounts.size()>0){
			return accounts.get(0);
		}
		return null;
	}
	
	
	public List<CurrentUser> getAccountList(){
		SQLiteDatabase db = this.getSQLite(readOnly);
		Cursor cursor =db.query(USER_INFO_TABLE, columns, null, null, null, null, "logintime desc","5");
		List<CurrentUser> accounts = new ArrayList<CurrentUser>();
		CurrentUser entity = null; 
		while (cursor.moveToNext()) {
			entity = new CurrentUser();
			entity.setLoginname(cursor.getString(0));
			entity.setPassword(cursor.getString(1));
			entity.setIsAutoLogin(cursor.getInt(2));
			entity.setIsRememberPwd(cursor.getInt(3));
			entity.setCryptpwd(cursor.getBlob(4));
			entity.setPwdlength(cursor.getInt(cursor.getColumnIndex("pwdlength")));
			accounts.add(entity);
		}
		close(cursor,db);
		return accounts;
	}
	
	
}
