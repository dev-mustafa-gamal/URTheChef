package com.mgliveapps.urthechef.database.query;

import android.os.Bundle;

import com.mgliveapps.urthechef.database.data.Data;

import java.sql.SQLException;


public abstract class Query
{
	private Bundle mMetaData = null;

	public abstract Data<?> processData() throws SQLException;


	public Bundle getMetaData()
	{
		return mMetaData;
	}


	public void setMetaData(Bundle metaData)
	{
		mMetaData = metaData;
	}
}
