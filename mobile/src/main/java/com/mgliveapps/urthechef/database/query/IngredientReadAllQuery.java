package com.mgliveapps.urthechef.database.query;

import com.mgliveapps.urthechef.database.dao.IngredientDAO;
import com.mgliveapps.urthechef.database.data.Data;
import com.mgliveapps.urthechef.database.model.IngredientModel;

import java.sql.SQLException;
import java.util.List;


public class IngredientReadAllQuery extends Query
{
	private long mSkip = -1L;
	private long mTake = -1L;


	public IngredientReadAllQuery()
	{
	}


	public IngredientReadAllQuery(long skip, long take)
	{
		mSkip = skip;
		mTake = take;
	}


	@Override
	public Data<List<IngredientModel>> processData() throws SQLException
	{
		Data<List<IngredientModel>> data = new Data<>();
		data.setDataObject(IngredientDAO.readAll(mSkip, mTake));
		return data;
	}
}
