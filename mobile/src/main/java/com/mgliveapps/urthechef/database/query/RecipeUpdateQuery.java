package com.mgliveapps.urthechef.database.query;

import com.mgliveapps.urthechef.database.dao.RecipeDAO;
import com.mgliveapps.urthechef.database.data.Data;
import com.mgliveapps.urthechef.database.model.RecipeModel;

import java.sql.SQLException;


public class RecipeUpdateQuery extends Query
{
	private RecipeModel mRecipe;


	public RecipeUpdateQuery(RecipeModel recipe)
	{
		mRecipe = recipe;
	}


	@Override
	public Data<Integer> processData() throws SQLException
	{
		Data<Integer> data = new Data<>();
		data.setDataObject(RecipeDAO.update(mRecipe));
		return data;
	}
}
