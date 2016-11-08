package com.mgliveapps.urthechef.database.query;

import com.mgliveapps.urthechef.database.dao.CategoryDAO;
import com.mgliveapps.urthechef.database.data.Data;
import com.mgliveapps.urthechef.database.model.CategoryModel;

import java.sql.SQLException;
import java.util.List;


public class CategoryReadAllQuery extends Query
{
	private long mSkip = -1L;
	private long mTake = -1L;


	public CategoryReadAllQuery()
	{
	}


	public CategoryReadAllQuery(long skip, long take)
	{
		mSkip = skip;
		mTake = take;
	}


	@Override
	public Data<List<CategoryModel>> processData() throws SQLException
	{
		Data<List<CategoryModel>> data = new Data<>();
		data.setDataObject(CategoryDAO.readAll(mSkip, mTake));
		return data;
	}
}
