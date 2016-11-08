package com.mgliveapps.urthechef.database.query;

import com.mgliveapps.urthechef.database.dao.RecipeDAO;
import com.mgliveapps.urthechef.database.data.Data;
import com.mgliveapps.urthechef.database.model.RecipeModel;

import java.sql.SQLException;
import java.util.List;


public class RecipeReadByCategoryQuery extends Query
{
	private long mCategoryId;
	private long mSkip = -1L;
	private long mTake = -1L;


	public RecipeReadByCategoryQuery(long categoryId)
	{
		mCategoryId = categoryId;
	}


	public RecipeReadByCategoryQuery(long categoryId, long skip, long take)
	{
		mCategoryId = categoryId;
		mSkip = skip;
		mTake = take;
	}


	@Override
	public Data<List<RecipeModel>> processData() throws SQLException
	{
		Data<List<RecipeModel>> data = new Data<>();
		data.setDataObject(RecipeDAO.readByCategory(mCategoryId, mSkip, mTake));
		return data;
	}
}
