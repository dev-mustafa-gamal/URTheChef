package com.mgliveapps.urthechef.database.query;

import com.mgliveapps.urthechef.database.dao.IngredientDAO;
import com.mgliveapps.urthechef.database.data.Data;
import com.mgliveapps.urthechef.database.model.IngredientModel;

import java.sql.SQLException;


public class IngredientReadQuery extends Query
{
	private long mId;


	public IngredientReadQuery(long id)
	{
		mId = id;
	}


	@Override
	public Data<IngredientModel> processData() throws SQLException
	{
		Data<IngredientModel> data = new Data<>();
		data.setDataObject(IngredientDAO.read(mId));
		return data;
	}
}
