package com.mgliveapps.urthechef.database.query;

import com.mgliveapps.urthechef.database.dao.IngredientDAO;
import com.mgliveapps.urthechef.database.data.Data;

import java.sql.SQLException;


public class IngredientDeleteAllQuery extends Query
{
	public IngredientDeleteAllQuery()
	{
	}


	@Override
	public Data<Integer> processData() throws SQLException
	{
		Data<Integer> data = new Data<>();
		data.setDataObject(IngredientDAO.deleteAll());
		return data;
	}
}
