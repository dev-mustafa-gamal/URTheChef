package com.mgliveapps.urthechef.database.query;

import com.mgliveapps.urthechef.database.dao.CategoryDAO;
import com.mgliveapps.urthechef.database.data.Data;

import java.sql.SQLException;


public class CategoryDeleteAllQuery extends Query
{
	public CategoryDeleteAllQuery()
	{
	}


	@Override
	public Data<Integer> processData() throws SQLException
	{
		Data<Integer> data = new Data<>();
		data.setDataObject(CategoryDAO.deleteAll());
		return data;
	}
}
