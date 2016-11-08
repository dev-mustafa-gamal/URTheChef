package com.mgliveapps.urthechef.database;

import com.mgliveapps.urthechef.database.data.Data;


public interface DatabaseCallListener
{
	void onDatabaseCallRespond(DatabaseCallTask task, Data<?> data);
	void onDatabaseCallFail(DatabaseCallTask task, Exception exception);
}
