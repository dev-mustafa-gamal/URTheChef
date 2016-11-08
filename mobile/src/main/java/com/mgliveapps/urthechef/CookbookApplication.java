package com.mgliveapps.urthechef;

import android.app.Application;
import android.content.Context;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.mgliveapps.urthechef.utility.ImageLoaderUtility;


public class CookbookApplication extends Application
{
	private static CookbookApplication sInstance;

	private Tracker mTracker;


	public static Context getContext()
	{
		return sInstance;
	}


	public CookbookApplication()
	{
		sInstance = this;
	}


	@Override
	public void onCreate()
	{
		super.onCreate();
		
		// force AsyncTask to be initialized in the main thread due to the bug:
		// http://stackoverflow.com/questions/4280330/onpostexecute-not-being-called-in-asynctask-handler-runtime-exception
		try
		{
			Class.forName("android.os.AsyncTask");
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();
		}

		// init image caching
		ImageLoaderUtility.init(getApplicationContext());
	}


	public synchronized Tracker getTracker()
	{
		if(mTracker==null)
		{
			GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
			analytics.setDryRun(CookbookConfig.ANALYTICS_TRACKING_ID == null || CookbookConfig.ANALYTICS_TRACKING_ID.equals(""));
			mTracker = analytics.newTracker(R.xml.analytics_app_tracker);
			mTracker.set("&tid", CookbookConfig.ANALYTICS_TRACKING_ID);
		}
		return mTracker;
	}
}
