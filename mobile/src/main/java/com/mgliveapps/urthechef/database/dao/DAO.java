package com.mgliveapps.urthechef.database.dao;

import com.mgliveapps.urthechef.database.DatabaseHelper;
import com.mgliveapps.urthechef.utility.Logcat;

import java.sql.SQLException;


public class DAO
{
	public static void printDatabaseInfo()
	{
		DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
		try
		{
			Logcat.d("%d categories", databaseHelper.getCategoryDao().countOf());
			Logcat.d("%d recipes", databaseHelper.getRecipeDao().countOf());
			Logcat.d("%d ingredients", databaseHelper.getIngredientDao().countOf());
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
}
