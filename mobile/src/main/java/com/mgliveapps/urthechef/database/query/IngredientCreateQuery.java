package com.mgliveapps.urthechef.database.query;

import com.mgliveapps.urthechef.database.dao.IngredientDAO;
import com.mgliveapps.urthechef.database.data.Data;
import com.mgliveapps.urthechef.database.model.IngredientModel;

import java.sql.SQLException;


public class IngredientCreateQuery extends Query
{
	private IngredientModel mIngredient;


	public IngredientCreateQuery(IngredientModel ingredient)
	{
		mIngredient = ingredient;
	}
	
	
	@Override
	public Data<Integer> processData() throws SQLException
	{
		Data<Integer> data = new Data<>();
		data.setDataObject(IngredientDAO.create(mIngredient));
		return data;
	}
}
