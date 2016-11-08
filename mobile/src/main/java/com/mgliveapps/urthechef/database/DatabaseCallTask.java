package com.mgliveapps.urthechef.database;

import android.os.AsyncTask;

import com.mgliveapps.urthechef.database.data.Data;
import com.mgliveapps.urthechef.database.query.Query;
import com.mgliveapps.urthechef.utility.Logcat;

import java.lang.ref.WeakReference;


public class DatabaseCallTask extends AsyncTask<Void, Void, Data<?>>
{
	private Query mQuery;
	private WeakReference<DatabaseCallListener> mListener;
	private Exception mException = null;
	
	
	public DatabaseCallTask(Query query, DatabaseCallListener listener)
	{
		mQuery = query;
		setListener(listener);
	}
	
	
	@Override
	protected Data<?> doInBackground(Void... params)
	{
		try
		{
			Data<?> data = mQuery.processData();
			
			if(isCancelled()) return null;
			else return data;
		}
		catch(Exception e)
		{
			mException = e;
			e.printStackTrace();
			return null;
		}
	}
	
	
	@Override
	protected void onPostExecute(Data<?> data)
	{
		if(isCancelled()) return;
		
		DatabaseCallListener listener = mListener.get();
		if(listener != null)
		{
			if(data != null)
			{
				listener.onDatabaseCallRespond(this, data);
			}
			else
			{
				listener.onDatabaseCallFail(this, mException);
			}
		}
	}
	
	
	@Override
	protected void onCancelled()
	{
		Logcat.d("");
	}


	public Query getQuery()
	{
		return mQuery;
	}


	public void setListener(DatabaseCallListener listener)
	{
		mListener = new WeakReference<>(listener);
	}
}
