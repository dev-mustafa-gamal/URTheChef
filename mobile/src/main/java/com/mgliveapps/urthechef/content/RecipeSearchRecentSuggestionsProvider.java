package com.mgliveapps.urthechef.content;

import android.content.SearchRecentSuggestionsProvider;


public class RecipeSearchRecentSuggestionsProvider extends SearchRecentSuggestionsProvider
{
	public final static String AUTHORITY = "com.mgliveapps.urthechef.content.RecipeSearchRecentSuggestionsProvider";
	public final static int MODE = DATABASE_MODE_QUERIES;


	public RecipeSearchRecentSuggestionsProvider()
	{
		setupSuggestions(AUTHORITY, MODE);
	}
}
