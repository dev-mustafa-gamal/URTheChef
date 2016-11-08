package com.mgliveapps.urthechef.database.query;

import com.mgliveapps.urthechef.database.dao.RecipeDAO;
import com.mgliveapps.urthechef.database.data.Data;
import com.mgliveapps.urthechef.database.model.RecipeModel;

import java.sql.SQLException;


public class RecipeReadQuery extends Query
{
	private long mId;


	public RecipeReadQuery(long id)
	{
		mId = id;
	}


	@Override
	public Data<RecipeModel> processData() throws SQLException
	{
		Data<RecipeModel> data = new Data<>();
		data.setDataObject(RecipeDAO.read(mId));
		return data;
	}
}
