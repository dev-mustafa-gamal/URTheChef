package com.mgliveapps.urthechef.fragment;

import android.Manifest;
import android.animation.Animator;
import android.app.ActivityOptions;
import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.melnykov.fab.FloatingActionButton;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.mgliveapps.urthechef.CookbookApplication;
import com.mgliveapps.urthechef.CookbookConfig;
import com.mgliveapps.urthechef.R;
import com.mgliveapps.urthechef.activity.RecipeDetailActivity;
import com.mgliveapps.urthechef.adapter.RecipeListAdapter;
import com.mgliveapps.urthechef.adapter.SearchSuggestionAdapter;
import com.mgliveapps.urthechef.content.RecipeSearchRecentSuggestionsProvider;
import com.mgliveapps.urthechef.database.DatabaseCallListener;
import com.mgliveapps.urthechef.database.DatabaseCallManager;
import com.mgliveapps.urthechef.database.DatabaseCallTask;
import com.mgliveapps.urthechef.database.data.Data;
import com.mgliveapps.urthechef.database.model.RecipeModel;
import com.mgliveapps.urthechef.database.query.Query;
import com.mgliveapps.urthechef.database.query.RecipeReadAllQuery;
import com.mgliveapps.urthechef.database.query.RecipeReadByCategoryQuery;
import com.mgliveapps.urthechef.database.query.RecipeReadFavoritesQuery;
import com.mgliveapps.urthechef.database.query.RecipeSearchQuery;
import com.mgliveapps.urthechef.dialog.AboutDialogFragment;
import com.mgliveapps.urthechef.listener.OnSearchListener;
import com.mgliveapps.urthechef.utility.ImageLoaderUtility;
import com.mgliveapps.urthechef.utility.Logcat;
import com.mgliveapps.urthechef.utility.NetworkUtility;
import com.mgliveapps.urthechef.utility.PermissionUtility;
import com.mgliveapps.urthechef.view.GridSpacingItemDecoration;
import com.mgliveapps.urthechef.view.StatefulLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class RecipeListFragment extends TaskFragment implements DatabaseCallListener, RecipeListAdapter.RecipeViewHolder.OnItemClickListener
{
	public static final long CATEGORY_ID_ALL = -1L;
	public static final long CATEGORY_ID_FAVORITES = -2L;
	public static final long CATEGORY_ID_SEARCH = -3L;

	private static final String ARGUMENT_CATEGORY_ID = "category_id";
	private static final String ARGUMENT_SEARCH_QUERY = "search_query";
	private static final String DIALOG_ABOUT = "about";
	private static final int LAZY_LOADING_TAKE = 128;
	private static final int LAZY_LOADING_OFFSET = 4;

	private boolean mLazyLoading = false;
	private View mRootView;
	private StatefulLayout mStatefulLayout;
	private RecipeListAdapter mAdapter;
	private OnSearchListener mSearchListener;
	private ActionMode mActionMode;
	private DatabaseCallManager mDatabaseCallManager = new DatabaseCallManager();
	private long mCategoryId;
	private String mSearchQuery;
	private List<RecipeModel> mRecipeList = new ArrayList<>();
	private List<Object> mFooterList = new ArrayList<>();


	public static RecipeListFragment newInstance(long categoryId)
	{
		RecipeListFragment fragment = new RecipeListFragment();

		// arguments
		Bundle arguments = new Bundle();
		arguments.putLong(ARGUMENT_CATEGORY_ID, categoryId);
		fragment.setArguments(arguments);

		return fragment;
	}


	public static RecipeListFragment newInstance(String searchQuery)
	{
		RecipeListFragment fragment = new RecipeListFragment();

		// arguments
		Bundle arguments = new Bundle();
		arguments.putLong(ARGUMENT_CATEGORY_ID, CATEGORY_ID_SEARCH);
		arguments.putString(ARGUMENT_SEARCH_QUERY, searchQuery);
		fragment.setArguments(arguments);

		return fragment;
	}
	
	
	@Override
	public void onAttach(Context context)
	{
		super.onAttach(context);

		// set search listener
		try
		{
			mSearchListener = (OnSearchListener) getActivity();
		}
		catch(ClassCastException e)
		{
			throw new ClassCastException(getActivity().getClass().getName() + " must implement " + OnSearchListener.class.getName());
		}
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		setRetainInstance(true);

		// handle fragment arguments
		Bundle arguments = getArguments();
		if(arguments != null)
		{
			handleArguments(arguments);
		}
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{	
		mRootView = inflater.inflate(R.layout.fragment_recipe_list, container, false);
		setupRecyclerView();
		return mRootView;
	}
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		// setup stateful layout
		setupStatefulLayout(savedInstanceState);

		// load data
		if(mRecipeList==null || mRecipeList.isEmpty()) loadData();

		// lazy loading progress
		if(mLazyLoading) showLazyLoadingProgress(true);

		// show toolbar if hidden
		showToolbar(true);

		// check permissions
		PermissionUtility.checkPermissionWriteExternalStorage(this);
	}
	
	
	@Override
	public void onStart()
	{
		super.onStart();
	}
	
	
	@Override
	public void onResume()
	{
		super.onResume();
	}
	
	
	@Override
	public void onPause()
	{
		super.onPause();
		
		// stop adapter
		if(mAdapter!=null) mAdapter.stop();
	}
	
	
	@Override
	public void onStop()
	{
		super.onStop();
	}
	
	
	@Override
	public void onDestroyView()
	{
		super.onDestroyView();
		mRootView = null;
	}
	
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();

		// cancel async tasks
		mDatabaseCallManager.cancelAllTasks();
	}
	
	
	@Override
	public void onDetach()
	{
		super.onDetach();
	}
	
	
	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		// save current instance state
		super.onSaveInstanceState(outState);
		setUserVisibleHint(true);

		// stateful layout state
		if(mStatefulLayout!=null) mStatefulLayout.saveInstanceState(outState);
	}
	
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		// action bar menu
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_recipe_list, menu);
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		// action bar menu behavior
		switch(item.getItemId())
		{
			case R.id.menu_rate:
				startWebActivity(getString(R.string.app_store_uri, CookbookApplication.getContext().getPackageName()));
				return true;

			case R.id.menu_about:
				showAboutDialog();
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}


	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
	{
		switch(requestCode)
		{
			case PermissionUtility.REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE:
			{
				// if request is cancelled, the result arrays are empty
				if(grantResults.length > 0)
				{
					for(int i=0; i<grantResults.length; i++)
					{
						if(grantResults[i] == PackageManager.PERMISSION_GRANTED)
						{
							// permission granted
							String permission = permissions[i];
							if(permission.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE))
							{
								synchronized(this)
								{
									ImageLoader.getInstance().destroy();
									ImageLoaderUtility.init(getActivity().getApplicationContext());
								}
							}
						}
						else
						{
							// permission denied
						}
					}
				}
				else
				{
					// all permissions denied
				}
				break;
			}
		}
	}


	@Override
	public void onItemClick(View view, int position, long id, int viewType)
	{
		// position
		int recipePosition = mAdapter.getRecipePosition(position);

		// start activity
		RecipeModel recipe = mRecipeList.get(recipePosition);
		startRecipeDetailActivity(view, recipe.getId());
	}


	@Override
	public void onDatabaseCallRespond(final DatabaseCallTask task, final Data<?> data)
	{
		runTaskCallback(new Runnable()
		{
			public void run()
			{
				if(mRootView == null) return; // view was destroyed

				if(task.getQuery().getClass().equals(RecipeReadAllQuery.class))
				{
					Logcat.d("RecipeReadAllQuery");

					// get data
					Data<List<RecipeModel>> recipeReadAllData = (Data<List<RecipeModel>>) data;
					List<RecipeModel> recipeList = recipeReadAllData.getDataObject();
					Iterator<RecipeModel> iterator = recipeList.iterator();
					while(iterator.hasNext())
					{
						RecipeModel recipe = iterator.next();
						mRecipeList.add(recipe);
					}
				}
				else if(task.getQuery().getClass().equals(RecipeReadFavoritesQuery.class))
				{
					Logcat.d("RecipeReadFavoritesQuery");

					// get data
					Data<List<RecipeModel>> recipeReadFavoritesData = (Data<List<RecipeModel>>) data;
					List<RecipeModel> recipeList = recipeReadFavoritesData.getDataObject();
					Iterator<RecipeModel> iterator = recipeList.iterator();
					while(iterator.hasNext())
					{
						RecipeModel recipe = iterator.next();
						mRecipeList.add(recipe);
					}
				}
				else if(task.getQuery().getClass().equals(RecipeSearchQuery.class))
				{
					Logcat.d("RecipeSearchQuery");

					// get data
					Data<List<RecipeModel>> recipeSearchData = (Data<List<RecipeModel>>) data;
					List<RecipeModel> recipeList = recipeSearchData.getDataObject();
					Iterator<RecipeModel> iterator = recipeList.iterator();
					while(iterator.hasNext())
					{
						RecipeModel recipe = iterator.next();
						mRecipeList.add(recipe);
					}
				}
				else if(task.getQuery().getClass().equals(RecipeReadByCategoryQuery.class))
				{
					Logcat.d("RecipeReadByCategoryQuery");

					// get data
					Data<List<RecipeModel>> recipeReadByCategoryData = (Data<List<RecipeModel>>) data;
					List<RecipeModel> recipeList = recipeReadByCategoryData.getDataObject();
					Iterator<RecipeModel> iterator = recipeList.iterator();
					while(iterator.hasNext())
					{
						RecipeModel recipe = iterator.next();
						mRecipeList.add(recipe);
					}
				}

				// show content
				showLazyLoadingProgress(false);
				if(mRecipeList!=null && !mRecipeList.isEmpty()) mStatefulLayout.showContent();
				else mStatefulLayout.showEmpty();

				// finish query
				mDatabaseCallManager.finishTask(task);
			}
		});
	}


	@Override
	public void onDatabaseCallFail(final DatabaseCallTask task, final Exception exception)
	{
		runTaskCallback(new Runnable()
		{
			public void run()
			{
				if(mRootView==null) return; // view was destroyed

				if(task.getQuery().getClass().equals(RecipeReadAllQuery.class))
				{
					Logcat.d("RecipeReadAllQuery / exception " + exception.getClass().getSimpleName() + " / " + exception.getMessage());
				}
				else if(task.getQuery().getClass().equals(RecipeReadFavoritesQuery.class))
				{
					Logcat.d("RecipeReadFavoritesQuery / exception " + exception.getClass().getSimpleName() + " / " + exception.getMessage());
				}
				else if(task.getQuery().getClass().equals(RecipeSearchQuery.class))
				{
					Logcat.d("RecipeSearchQuery / exception " + exception.getClass().getSimpleName() + " / " + exception.getMessage());
				}
				else if(task.getQuery().getClass().equals(RecipeReadByCategoryQuery.class))
				{
					Logcat.d("RecipeReadByCategoryQuery / exception " + exception.getClass().getSimpleName() + " / " + exception.getMessage());
				}

				// hide progress
				showLazyLoadingProgress(false);
				if(mRecipeList!=null && !mRecipeList.isEmpty()) mStatefulLayout.showContent();
				else mStatefulLayout.showEmpty();

				// handle fail
				handleFail();

				// finish query
				mDatabaseCallManager.finishTask(task);
			}
		});
	}


	private void handleFail()
	{
		Toast.makeText(getActivity(), R.string.global_database_fail_toast, Toast.LENGTH_LONG).show();
	}


	private void handleArguments(Bundle arguments)
	{
		mCategoryId = arguments.getLong(ARGUMENT_CATEGORY_ID, CATEGORY_ID_ALL);
		mSearchQuery = arguments.getString(ARGUMENT_SEARCH_QUERY, "");
	}

	
	private void loadData()
	{
		if(!mDatabaseCallManager.hasRunningTask(RecipeReadAllQuery.class) &&
				!mDatabaseCallManager.hasRunningTask(RecipeReadFavoritesQuery.class) &&
				!mDatabaseCallManager.hasRunningTask(RecipeSearchQuery.class) &&
				!mDatabaseCallManager.hasRunningTask(RecipeReadByCategoryQuery.class))
		{
			// show progress
			mStatefulLayout.showProgress();

			// run async task
			Query query;
			if(mCategoryId==CATEGORY_ID_ALL)
			{
				query = new RecipeReadAllQuery(0, LAZY_LOADING_TAKE);
			}
			else if(mCategoryId==CATEGORY_ID_FAVORITES)
			{
				query = new RecipeReadFavoritesQuery(0, LAZY_LOADING_TAKE);
			}
			else if(mCategoryId==CATEGORY_ID_SEARCH)
			{
				query = new RecipeSearchQuery(mSearchQuery, 0, LAZY_LOADING_TAKE);
			}
			else
			{
				query = new RecipeReadByCategoryQuery(mCategoryId, 0, LAZY_LOADING_TAKE);
			}
			mDatabaseCallManager.executeTask(query, this);
		}
	}
	
	
	private void lazyLoadData()
	{
		// show lazy loading progress
		showLazyLoadingProgress(true);

		// run async task
		Query query;
		if(mCategoryId==CATEGORY_ID_ALL)
		{
			query = new RecipeReadAllQuery(mRecipeList.size(), LAZY_LOADING_TAKE);
		}
		else if(mCategoryId==CATEGORY_ID_FAVORITES)
		{
			query = new RecipeReadFavoritesQuery(mRecipeList.size(), LAZY_LOADING_TAKE);
		}
		else if(mCategoryId==CATEGORY_ID_SEARCH)
		{
			query = new RecipeSearchQuery(mSearchQuery, mRecipeList.size(), LAZY_LOADING_TAKE);
		}
		else
		{
			query = new RecipeReadByCategoryQuery(mCategoryId, mRecipeList.size(), LAZY_LOADING_TAKE);
		}
		mDatabaseCallManager.executeTask(query, this);
	}
	
	
	private void showLazyLoadingProgress(boolean visible)
	{
		if(visible)
		{
			mLazyLoading = true;

			// show footer
			if(mFooterList.size()<=0)
			{
				mFooterList.add(new Object());
				mAdapter.notifyItemInserted(mAdapter.getRecyclerPositionByFooter(0));
			}
		}
		else
		{
			// hide footer
			if(mFooterList.size()>0)
			{
				mFooterList.remove(0);
				mAdapter.notifyItemRemoved(mAdapter.getRecyclerPositionByFooter(0));
			}

			mLazyLoading = false;
		}
	}


	private void showToolbar(boolean visible)
	{
		final Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
		if(visible)
		{
			toolbar.animate()
					.translationY(0)
					.setDuration(200)
					.setInterpolator(new AccelerateDecelerateInterpolator())
					.setListener(new Animator.AnimatorListener()
					{
						@Override
						public void onAnimationStart(Animator animator)
						{
							toolbar.setVisibility(View.VISIBLE);
							toolbar.setEnabled(false);
						}


						@Override
						public void onAnimationEnd(Animator animator)
						{
							toolbar.setEnabled(true);
						}


						@Override
						public void onAnimationCancel(Animator animator)
						{
						}


						@Override
						public void onAnimationRepeat(Animator animator)
						{
						}
					});
		}
		else
		{
			toolbar.animate()
					.translationY(-toolbar.getBottom())
					.setDuration(200)
					.setInterpolator(new AccelerateDecelerateInterpolator())
					.setListener(new Animator.AnimatorListener()
					{
						@Override
						public void onAnimationStart(Animator animator)
						{
							toolbar.setEnabled(false);
						}


						@Override
						public void onAnimationEnd(Animator animator)
						{
							toolbar.setVisibility(View.GONE);
							toolbar.setEnabled(true);
						}


						@Override
						public void onAnimationCancel(Animator animator)
						{
						}


						@Override
						public void onAnimationRepeat(Animator animator)
						{
						}
					});
		}
	}


	private void showFloatingActionButton(boolean visible)
	{
		final FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
		if(visible)
		{
			fab.show();
		}
		else
		{
			fab.hide();
		}
	}
	
	
	private void bindData()
	{
		// reference
		final RecyclerView recyclerView = getRecyclerView();
		final FloatingActionButton floatingActionButton = (FloatingActionButton) getActivity().findViewById(R.id.fab);

		// content
		if(recyclerView.getAdapter()==null)
		{
			// create adapter
			mAdapter = new RecipeListAdapter(mRecipeList, mFooterList, this, getGridSpanCount());
		}
		else
		{
			// refill adapter
			mAdapter.refill(mRecipeList, mFooterList, this, getGridSpanCount());
		}

		// set fixed size
		recyclerView.setHasFixedSize(false);

		// add decoration
		RecyclerView.ItemDecoration itemDecoration = new GridSpacingItemDecoration(getResources().getDimensionPixelSize(R.dimen.fragment_recipe_list_recycler_item_padding));
		recyclerView.addItemDecoration(itemDecoration);

		// set animator
		recyclerView.setItemAnimator(new DefaultItemAnimator());

		// set adapter
		recyclerView.setAdapter(mAdapter);

		// lazy loading
		recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
		{
			private static final int THRESHOLD = 100;

			private int mCounter = 0;
			private Toolbar mToolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);


			@Override
			public void onScrollStateChanged(RecyclerView recyclerView, int newState)
			{
				super.onScrollStateChanged(recyclerView, newState);

				// reset counter
				if(newState==RecyclerView.SCROLL_STATE_DRAGGING)
				{
					mCounter = 0;
				}

				// disable item animation in adapter
				if(newState==RecyclerView.SCROLL_STATE_DRAGGING)
				{
					mAdapter.setAnimationEnabled(false);
				}
			}


			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy)
			{
				super.onScrolled(recyclerView, dx, dy);

				GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
				int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
				int visibleItemCount = layoutManager.getChildCount();
				int totalItemCount = layoutManager.getItemCount();
				int lastVisibleItem = firstVisibleItem + visibleItemCount;

				// lazy loading
				if(totalItemCount-lastVisibleItem <= LAZY_LOADING_OFFSET && mRecipeList.size() % LAZY_LOADING_TAKE==0 && !mRecipeList.isEmpty())
				{
					if(!mLazyLoading) lazyLoadData();
				}

				// toolbar and FAB animation
				mCounter += dy;
				if(recyclerView.getScrollState()==RecyclerView.SCROLL_STATE_DRAGGING || recyclerView.getScrollState()==RecyclerView.SCROLL_STATE_SETTLING)
				{
					// scroll down
					if(mCounter>THRESHOLD && firstVisibleItem>0)
					{
						// hide toolbar
						if(mToolbar.getVisibility()==View.VISIBLE && mToolbar.isEnabled())
						{
							showToolbar(false);
						}

						// hide FAB
						showFloatingActionButton(false);

						mCounter = 0;
					}

					// scroll up
					else if(mCounter<-THRESHOLD || firstVisibleItem==0)
					{
						// show toolbar
						if(mToolbar.getVisibility()==View.GONE && mToolbar.isEnabled())
						{
							showToolbar(true);
						}

						// show FAB
						showFloatingActionButton(true);

						mCounter = 0;
					}
				}
			}
		});

		// floating action button
		floatingActionButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				mActionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(new SearchActionModeCallback());
			}
		});

		// admob
		bindDataBanner();
	}


	private void bindDataBanner()
	{
		if(CookbookConfig.ADMOB_UNIT_ID_RECIPE_LIST != null && !CookbookConfig.ADMOB_UNIT_ID_RECIPE_LIST.equals("") && NetworkUtility.isOnline(getActivity()))
		{
			// reference
			ViewGroup contentLayout = (ViewGroup) mRootView.findViewById(R.id.container_content);

			// layout params
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			params.gravity = Gravity.CENTER;

			// create ad view
			AdView adView = new AdView(getActivity());
			adView.setId(R.id.adview);
			adView.setLayoutParams(params);
			adView.setAdSize(AdSize.SMART_BANNER);
			adView.setAdUnitId(CookbookConfig.ADMOB_UNIT_ID_RECIPE_LIST);

			// add to layout
			contentLayout.removeView(getActivity().findViewById(R.id.adview));
			contentLayout.addView(adView);

			// call ad request
			AdRequest adRequest = new AdRequest.Builder()
					.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
					.addTestDevice(CookbookConfig.ADMOB_TEST_DEVICE_ID)
					.build();
			adView.loadAd(adRequest);
		}
	}


	private RecyclerView getRecyclerView()
	{
		return mRootView!=null ? (RecyclerView) mRootView.findViewById(R.id.fragment_recipe_list_recycler) : null;
	}


	private void setupStatefulLayout(Bundle savedInstanceState)
	{
		// reference
		mStatefulLayout = (StatefulLayout) mRootView;

		// state change listener
		mStatefulLayout.setOnStateChangeListener(new StatefulLayout.OnStateChangeListener()
		{
			@Override
			public void onStateChange(View v, StatefulLayout.State state)
			{
				Logcat.d("" + (state == null ? "null" : state.toString()));

				// bind data
				if(state == StatefulLayout.State.CONTENT)
				{
					if(mLazyLoading && mAdapter!=null)
					{
						mAdapter.notifyDataSetChanged();
					}
					else
					{
						if(mRecipeList!=null && !mRecipeList.isEmpty()) bindData();
					}
				}

				// floating action button
				showFloatingActionButton(state == StatefulLayout.State.CONTENT);
			}
		});

		// restore state
		mStatefulLayout.restoreInstanceState(savedInstanceState);
	}


	private void setupRecyclerView()
	{
		GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), getGridSpanCount());
		gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
		RecyclerView recyclerView = getRecyclerView();
		recyclerView.setLayoutManager(gridLayoutManager);
	}


	private int getGridSpanCount()
	{
		Display display = getActivity().getWindowManager().getDefaultDisplay();
		DisplayMetrics displayMetrics = new DisplayMetrics();
		display.getMetrics(displayMetrics);
		float screenWidth  = displayMetrics.widthPixels;
		float cellWidth = getResources().getDimension(R.dimen.fragment_recipe_list_recycler_item_size);
		return Math.round(screenWidth / cellWidth);
	}


	private void showAboutDialog()
	{
		// create and show the dialog
		DialogFragment newFragment = AboutDialogFragment.newInstance();
		newFragment.setTargetFragment(this, 0);
		newFragment.show(getFragmentManager(), DIALOG_ABOUT);
	}


	private void startRecipeDetailActivity(View view, long recipeId)
	{
		Intent intent = RecipeDetailActivity.newIntent(getActivity(), recipeId);
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
		{
			ActivityOptions options = ActivityOptions.makeScaleUpAnimation(view, 0, 0, view.getWidth(), view.getHeight());
			getActivity().startActivity(intent, options.toBundle());
		}
		else
		{
			startActivity(intent);
		}
	}


	private void startWebActivity(String url)
	{
		try
		{
			Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url));
			startActivity(intent);
		}
		catch(android.content.ActivityNotFoundException e)
		{
			// can't start activity
		}
	}


	private class SearchActionModeCallback implements ActionMode.Callback
	{
		private SearchView mSearchView;
		private SearchSuggestionAdapter mSearchSuggestionAdapter;


		@Override
		public boolean onCreateActionMode(ActionMode actionMode, Menu menu)
		{
			// search view
			mSearchView = new SearchView(((AppCompatActivity) getActivity()).getSupportActionBar().getThemedContext());
			setupSearchView(mSearchView);

			// search menu item
			MenuItem searchMenuItem = menu.add(Menu.NONE, Menu.NONE, 1, getString(R.string.menu_search));
			searchMenuItem.setIcon(R.drawable.ic_menu_search);
			MenuItemCompat.setActionView(searchMenuItem, mSearchView);
			MenuItemCompat.setShowAsAction(searchMenuItem, MenuItem.SHOW_AS_ACTION_ALWAYS);

			return true;
		}


		@Override
		public boolean onPrepareActionMode(ActionMode actionMode, Menu menu)
		{
			showFloatingActionButton(false);
			return true;
		}


		@Override
		public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem)
		{
			return false;
		}


		@Override
		public void onDestroyActionMode(ActionMode actionMode)
		{
			showFloatingActionButton(true);
		}


		private void setupSearchView(SearchView searchView)
		{
			// expand action view
			searchView.setIconifiedByDefault(true);
			searchView.setIconified(false);
			searchView.onActionViewExpanded();

			// search hint
			searchView.setQueryHint(getString(R.string.menu_search_hint));

			// text color
			AutoCompleteTextView searchText = (AutoCompleteTextView) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
			searchText.setTextColor(ContextCompat.getColor(getActivity(), R.color.global_text_primary_inverse));
			searchText.setHintTextColor(ContextCompat.getColor(getActivity(), R.color.global_text_secondary_inverse));

			// suggestion listeners
			searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
			{
				@Override
				public boolean onQueryTextSubmit(String query)
				{
					// listener
					mSearchListener.onSearch(query);

					// save query for suggestion
					SearchRecentSuggestions suggestions = new SearchRecentSuggestions(getActivity(), RecipeSearchRecentSuggestionsProvider.AUTHORITY, RecipeSearchRecentSuggestionsProvider.MODE);
					suggestions.saveRecentQuery(query, null);

					// close action mode
					mActionMode.finish();

					return true;
				}

				@Override
				public boolean onQueryTextChange(String query)
				{
					if(query.length()>2)
					{
						updateSearchSuggestion(query);
					}
					return true;
				}
			});
			searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener()
			{
				@Override
				public boolean onSuggestionSelect(int position)
				{
					return false;
				}

				@Override
				public boolean onSuggestionClick(int position)
				{
					// get query
					Cursor cursor = (Cursor) mSearchSuggestionAdapter.getItem(position);
					String title = cursor.getString(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1));

					// listener
					mSearchListener.onSearch(title);

					// close action mode
					mActionMode.finish();

					return true;
				}
			});
		}


		private void updateSearchSuggestion(String query)
		{
			// cursor
			ContentResolver contentResolver = getActivity().getApplicationContext().getContentResolver();
			String contentUri = "content://" + RecipeSearchRecentSuggestionsProvider.AUTHORITY + '/' + SearchManager.SUGGEST_URI_PATH_QUERY;
			Uri uri = Uri.parse(contentUri);
			Cursor cursor = contentResolver.query(uri, null, null, new String[] { query }, null);

			// searchview content
			if(mSearchSuggestionAdapter==null)
			{
				// create adapter
				mSearchSuggestionAdapter = new SearchSuggestionAdapter(getActivity(), cursor);

				// set adapter
				mSearchView.setSuggestionsAdapter(mSearchSuggestionAdapter);
			}
			else
			{
				// refill adapter
				mSearchSuggestionAdapter.refill(getActivity(), cursor);

				// set adapter
				mSearchView.setSuggestionsAdapter(mSearchSuggestionAdapter);
			}
		}
	}
}
