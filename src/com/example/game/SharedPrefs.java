package com.example.game;

import org.andengine.ui.activity.BaseGameActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


public class SharedPrefs {
	public static String hScore="H_SCORE";
	public static final String myPref = "MY_PREF" ;
	public BaseGameActivity activity;
	private SharedPreferences shpref;
	public SharedPrefs(BaseGameActivity activity){
		this.activity=activity;
		shpref = activity.getSharedPreferences(myPref, Context.MODE_PRIVATE);
		if(!shpref.contains(hScore)){
			Editor editor = shpref.edit();
			editor.putString(hScore, "0");
			editor.commit();
		}
			
	}
	public void checkAndSetHscore(int score){
		if(score>Integer.parseInt(getHScore())){			
			Editor editor = shpref.edit();
			editor.putString(hScore, ""+score);
			editor.commit();		
		}
	}
	public String getHScore(){
		shpref = activity.getSharedPreferences(myPref, Context.MODE_PRIVATE);
		return shpref.getString(hScore, "");		
	}
	

}
