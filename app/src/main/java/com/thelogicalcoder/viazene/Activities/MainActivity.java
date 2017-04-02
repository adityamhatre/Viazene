package com.thelogicalcoder.viazene.Activities;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.blunderer.materialdesignlibrary.activities.NavigationDrawerActivity;
import com.blunderer.materialdesignlibrary.handlers.ActionBarHandler;
import com.blunderer.materialdesignlibrary.handlers.ActionBarSearchHandler;
import com.blunderer.materialdesignlibrary.handlers.NavigationDrawerAccountsHandler;
import com.blunderer.materialdesignlibrary.handlers.NavigationDrawerAccountsMenuHandler;
import com.blunderer.materialdesignlibrary.handlers.NavigationDrawerBottomHandler;
import com.blunderer.materialdesignlibrary.handlers.NavigationDrawerStyleHandler;
import com.blunderer.materialdesignlibrary.handlers.NavigationDrawerTopHandler;
import com.blunderer.materialdesignlibrary.listeners.OnSearchListener;
import com.blunderer.materialdesignlibrary.models.Account;
import com.blunderer.materialdesignlibrary.views.ToolbarSearch;
import com.thelogicalcoder.viazene.Application.AppController;
import com.thelogicalcoder.viazene.AsyncTasks.CheckLogin;
import com.thelogicalcoder.viazene.AsyncTasks.GetProfile;
import com.thelogicalcoder.viazene.AsyncTasks.GetUserInfo;
import com.thelogicalcoder.viazene.AsyncTasks.GetWallForUser;
import com.thelogicalcoder.viazene.Fragment.MainFragment;
import com.thelogicalcoder.viazene.Fragment.MyCartFragment;
import com.thelogicalcoder.viazene.Fragment.MyMatchesFragment;
import com.thelogicalcoder.viazene.Fragment.MyOrdersFragment;
import com.thelogicalcoder.viazene.Fragment.MyServicesFragment;
import com.thelogicalcoder.viazene.Fragment.ProfileFragment;
import com.thelogicalcoder.viazene.Interfaces.onLoginCheckedListener;
import com.thelogicalcoder.viazene.Interfaces.onProfileLoadedListener;
import com.thelogicalcoder.viazene.Interfaces.onUserInfoLoadedListener;
import com.thelogicalcoder.viazene.Interfaces.onUserWallLoadListener;
import com.thelogicalcoder.viazene.JSONParsers.ProfileDetailsParser;
import com.thelogicalcoder.viazene.JSONParsers.UserInfoParser;
import com.thelogicalcoder.viazene.JSONParsers.UserWallParser;
import com.thelogicalcoder.viazene.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends NavigationDrawerActivity {

    private static final int MY_INTENT_CLICK = 302;
    static Boolean productCategoriesSet = false;
    static Boolean serviceCategoriesSet = false;
    List<String> searchTags;
    ProfileFragment profileFragment;
    MainFragment mainFragment;

    public MainActivity() {
        searchTags = new ArrayList<>();
        if (AppController.getInstance().getProductDataList() != null) {
            for (int i = 0; i < AppController.getInstance().getProductDataList().size(); i++) {
                searchTags.add(AppController.getInstance().getProductDataList().get(i).getProductName());
            }
            for (int i = 0; i < AppController.getInstance().getProductDataList().size(); i++) {
                if (!searchTags.contains("Category: " + AppController.getInstance().getProductDataList().get(i).getProductCategory())) {
                    searchTags.add("Category: " + AppController.getInstance().getProductDataList().get(i).getProductCategory());
                    if (!productCategoriesSet) {
                        AppController.getInstance().setProductCategories(AppController.getInstance().getProductDataList().get(i).getProductCategory());
                    }
                }
            }
            productCategoriesSet = true;
            for (int i = 0; i < AppController.getInstance().getProductDataList().size(); i++) {
                if (!searchTags.contains("Subcategory: " + AppController.getInstance().getProductDataList().get(i).getProductSubCategory())) {
                    searchTags.add("Subcategory: " + AppController.getInstance().getProductDataList().get(i).getProductSubCategory());
                }
            }
        }

        if (AppController.getInstance().getServices() != null) {
            for (int i = 0; i < AppController.getInstance().getServices().getServices().size(); i++) {
                if (!serviceCategoriesSet) {
                    if (!AppController.getInstance().getServiceCategories().contains(AppController.getInstance().getServices().getServices().get(i).getCategory()))
                        AppController.getInstance().setServiceCategories(AppController.getInstance().getServices().getServices().get(i).getCategory());
                }
            }
            System.out.println(AppController.getInstance().getServiceCategories());

            /*for (int i = 0; i < AppController.getInstance().getServices().getServices().size(); i++) {
                searchTags.add("Service: " + AppController.getInstance().getServices().getServices().get(i).getName());
            }

            for (int i = 0; i < AppController.getInstance().getServices().getServices().size(); i++) {
                if (!searchTags.contains("Service Category: " + AppController.getInstance().getServices().getServices().get(i).getCategory())) {
                    searchTags.add("Service Category: " + AppController.getInstance().getServices().getServices().get(i).getCategory());
                }
            }*/
            serviceCategoriesSet = true;
        }


    }

    @Override
    protected ActionBarHandler getActionBarHandler() {
        return new ActionBarSearchHandler(this, new OnSearchListener() {

            @Override
            public void onSearched(String text) {
                //TODO: write your code here
                //if()
                System.out.println(text);
                startActivity(new Intent(MainActivity.this, ProductListView.class).putExtra("productName", text));

            }

        })
                .enableAutoCompletion()
                .setAutoCompletionMode(ToolbarSearch.AutoCompletionMode.CONTAINS).setAutoCompletionSuggestions(searchTags);
    }

    @Override
    public NavigationDrawerStyleHandler getNavigationDrawerStyleHandler() {
        return null;
    }

    @Override
    public NavigationDrawerAccountsHandler getNavigationDrawerAccountsHandler() {
        if (AppController.getInstance().isLoggedIn()) {
            if (!AppController.getInstance().getUserInfo().getDpURL().trim().equalsIgnoreCase("null")) {
                return new NavigationDrawerAccountsHandler(this).addAccount(AppController.getInstance().getUserInfo().getUser(), AppController.getInstance().getUserInfo().getEmail(), AppController.getInstance().getUserInfo().getDpURL().trim().replace(" ", "%20") + "?" + System.currentTimeMillis(), R.drawable.navigation_drawer_bg).enableSmallAccountsLayout(false);
            } else {
                return new NavigationDrawerAccountsHandler(this).addAccount(AppController.getInstance().getUserInfo().getUser(), AppController.getInstance().getUserInfo().getEmail(), R.drawable.profile1, R.drawable.navigation_drawer_bg).enableSmallAccountsLayout(false);
            }
        } else
            return new NavigationDrawerAccountsHandler(this).addAccount("User", "Not logged in", R.drawable.profile1, R.drawable.navigation_drawer_bg).enableSmallAccountsLayout(false);
    }


    @Override
    public NavigationDrawerAccountsMenuHandler getNavigationDrawerAccountsMenuHandler() {
        return null;
    }

    @Override
    public void onNavigationDrawerAccountChange(Account account) {

    }

    @Override
    protected void onResume() {
        if (AppController.getInstance().getMainActivityWhichClass() == MainActivity.class) {
            String action = getIntent().getAction();
            // Prevent endless loop by adding a unique action, don't restart if action is present
            if (action == null || !action.equals("Already created")) {
                Log.v("Example", "Force restart");
                Intent intent = new Intent(this, MainActivity.class);

                startActivity(intent);
                finish();


            }
            // Remove the unique action so the next time onResume is called it will restart
            else {

                getIntent().setAction(null);
            }
        } else if (AppController.getInstance().getMainActivityWhichClass() == ProfileFragment.class) {
            performNavigationDrawerItemClick(1);
        } else if (AppController.getInstance().getMainActivityWhichClass() == MyCartFragment.class) {
            performNavigationDrawerItemClick(5);
        } else if (AppController.getInstance().getMainActivityWhichClass() == MyOrdersFragment.class) {
            performNavigationDrawerItemClick(3);
        } else if (AppController.getInstance().getMainActivityWhichClass() == MyMatchesFragment.class) {
            performNavigationDrawerItemClick(2);
        } else if (AppController.getInstance().getMainActivityWhichClass() == MyServicesFragment.class) {
            performNavigationDrawerItemClick(4);
        } else {
            String action = getIntent().getAction();
            AppController.getInstance().setMainActivityWhichClass(MainActivity.class);
            // Prevent endless loop by adding a unique action, don't restart if action is present
            if (action == null || !action.equals("Already created")) {
                Log.v("Example", "Force restart");
                Intent intent = new Intent(this, MainActivity.class);

                startActivity(intent);
                finish();


            }
            // Remove the unique action so the next time onResume is called it will restart
            else {

                getIntent().setAction(null);
            }
        }
        super.onResume();
    }

    public void autoLogin() {
        AppController.getInstance().setAutoLoginTry(false);
        if (getSharedPreferences("savedSession", Context.MODE_PRIVATE).contains("email") && getSharedPreferences("savedSession", Context.MODE_PRIVATE).contains("password")) {
            AppController.getInstance().showMaterialProgress(this);
            final String email = getSharedPreferences("savedSession", Context.MODE_PRIVATE).getString("email", "").trim(), password = getSharedPreferences("savedSession", Context.MODE_PRIVATE).getString("password", "").trim().trim();
            new CheckLogin(this, email, password, new onLoginCheckedListener() {
                @Override
                public void onLoginChecked(String response) {
                    if (response.trim().equalsIgnoreCase("success")) {

                        new GetUserInfo(MainActivity.this, email, new onUserInfoLoadedListener() {
                            @Override
                            public void onUserInfoLoaded(String response) {
                                System.out.println("MainActivity: " + response);
                                if (response.equalsIgnoreCase("") || response.equalsIgnoreCase("errorOccurred")) {
                                    AppController.getInstance().dismissMaterialProgress();
                                    Snackbar.make(findViewById(R.id.root), "Cannot connect to server", Snackbar.LENGTH_SHORT).show();
                                } else {
                                    new UserInfoParser(MainActivity.this, response);
                                    new GetWallForUser(MainActivity.this, new onUserWallLoadListener() {
                                        @Override
                                        public void onUserWallLoaded(String response) {
                                            if (response.equalsIgnoreCase("") || response.equalsIgnoreCase("errorOccurred")) {
                                                //loginButton.setVisibility(View.VISIBLE);
                                                //loginProgress.setVisibility(View.GONE);
                                                AppController.getInstance().dismissMaterialProgress();
                                                Snackbar.make(findViewById(R.id.root), "Cannot connect to server", Snackbar.LENGTH_SHORT).show();
                                            } else {
                                                new UserWallParser(response);
                                                //loginProgress.setVisibility(View.GONE);

                                                new GetProfile(MainActivity.this, AppController.getInstance().getUserInfo().getEmail(), new onProfileLoadedListener() {
                                                    @Override
                                                    public void onProfileLoaded(String response) {
                                                        if (response.equalsIgnoreCase("") || response.equalsIgnoreCase("errorOccurred")) {
                                                            AppController.getInstance().dismissMaterialProgress();
                                                            Snackbar.make(findViewById(R.id.root), "Cannot connect to server", Snackbar.LENGTH_SHORT).show();
                                                        } else {
                                                            new ProfileDetailsParser(response);
                                                            AppController.getInstance().setLoggedIn(true);
                                                            AppController.getInstance().dismissMaterialProgress();
                                                            finish();
                                                            startActivity(new Intent(MainActivity.this, MainActivity.class));
                                                        }
                                                    }
                                                }).execute();


                                                        /*AppController.getInstance().setLoggedIn(true);
                                                        String source = "";


                                                        try {
                                                            source = this.getIntent().getStringExtra("source");
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                        if (source.equalsIgnoreCase("mma") || source.equalsIgnoreCase("spv")) {
                                                            this.onBackPressed();
                                                        } else {
                                                            startActivity(new Intent(this, MainActivity.class));
                                                            this.finish();
                                                        }*/
                                            }
                                        }
                                    }).execute();


                                }
                            }
                        }).execute();
                    } else if (response.trim().equalsIgnoreCase("fail")) {
                        Snackbar.make(findViewById(R.id.root), "Invalid credentials", Snackbar.LENGTH_SHORT).show();

                    } else {
                        Snackbar.make(findViewById(R.id.root), "Cannot connect to server", Snackbar.LENGTH_SHORT).show();
                    }
                }
            }).execute();
        }
    }

    @Override
    public NavigationDrawerTopHandler getNavigationDrawerTopHandler() {
        if (AppController.getInstance().isLoggedIn()) {
            mainFragment = new MainFragment();
            profileFragment = new ProfileFragment();
            return new NavigationDrawerTopHandler(this)
                    .addItem("Viazene", mainFragment)
                    .addItem("My Profile", profileFragment)
                    .addItem("My Matches", new MyMatchesFragment())
                    .addItem("My Orders", new MyOrdersFragment())
                    .addItem("My Services", new MyServicesFragment())
                    .addItem("My Cart", new MyCartFragment());

        } else {
            mainFragment = new MainFragment();
            return new NavigationDrawerTopHandler(this)
                    .addItem("Viazene", mainFragment);
        }
    }

    @Override
    public NavigationDrawerBottomHandler getNavigationDrawerBottomHandler() {
        return new NavigationDrawerBottomHandler(this)
                .addItem("About us", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        closeNavigationDrawer();
                        /*getSupportFragmentManager().beginTransaction().replace(com.blunderer.materialdesignlibrary.R.id.fragment_container, profileFragment).commit();*/
                    }
                })
                .addItem("Rate", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(MainActivity.this).setTitle("Rate Viazene").setView(View.inflate(MainActivity.this, R.layout.rating_bar, null)).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                closeNavigationDrawer();
                            }
                        }).create().show();
                    }
                })
                .addItem("Share App", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendAPK();
                    }
                });
    }

    void sendAPK() {
        try {
            ArrayList<Uri> uris = new ArrayList<Uri>();
            Intent sendIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
            sendIntent.setType("application/vnd.android.package-archive");
            uris.add(Uri.fromFile(new File(getApplicationInfo().publicSourceDir)));
            sendIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
            startActivity(Intent.createChooser(sendIntent, null));
        } catch (Exception e) {
            ArrayList<Uri> uris = new ArrayList<Uri>();
            Intent sendIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
            sendIntent.setType("application/zip");
            uris.add(Uri.fromFile(new File(getApplicationInfo().publicSourceDir)));
            sendIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
            startActivity(Intent.createChooser(sendIntent, null));
        }
    }


    @Override
    public boolean overlayActionBar() {
        return false;
    }

    @Override
    public boolean replaceActionBarTitleByNavigationDrawerItemTitle() {
        return true;
    }

    @Override
    public int defaultNavigationDrawerItemSelectedPosition() {
        return 0;
    }

    @Override
    public void onBackPressed() {
        if (AppController.getInstance().getMainActivityWhichClass() == ProfileFragment.class) {
            //getSupportFragmentManager().beginTransaction().replace(com.blunderer.materialdesignlibrary.R.id.fragment_container, mainFragment).commit();
            performNavigationDrawerItemClick(0);
        } else if (AppController.getInstance().getMainActivityWhichClass() == MyMatchesFragment.class) {
            //getSupportFragmentManager().beginTransaction().replace(com.blunderer.materialdesignlibrary.R.id.fragment_container, mainFragment).commit();
            performNavigationDrawerItemClick(0);
        } else if (AppController.getInstance().getMainActivityWhichClass() == MyOrdersFragment.class) {
            //getSupportFragmentManager().beginTransaction().replace(com.blunderer.materialdesignlibrary.R.id.fragment_container, mainFragment).commit();
            performNavigationDrawerItemClick(0);
        } else if (AppController.getInstance().getMainActivityWhichClass() == MyServicesFragment.class) {
            //getSupportFragmentManager().beginTransaction().replace(com.blunderer.materialdesignlibrary.R.id.fragment_container, mainFragment).commit();
            performNavigationDrawerItemClick(0);
        } else if (AppController.getInstance().getMainActivityWhichClass() == MyCartFragment.class) {
            //getSupportFragmentManager().beginTransaction().replace(com.blunderer.materialdesignlibrary.R.id.fragment_container, mainFragment).commit();
            performNavigationDrawerItemClick(0);
        } else {
            super.onBackPressed();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    System.exit(0);
                }
            }, 700);
        }

    }

    @Override
    protected boolean enableActionBarShadow() {
        return false;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIntent().setAction("Already created");
        if (AppController.getInstance().getAutoLoginTry()) {
            autoLogin();
        } else AppController.getInstance().setAutoLoginTry(false);

    }

   /* @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            System.out.println(requestCode + " " + MY_INTENT_CLICK);
            //if (requestCode == MY_INTENT_CLICK) {
                if (null == data) return;
                Uri selectedImageUri = data.getData();
                String selectedImagePath = ImageFilePath.getPath(this.getApplicationContext(), selectedImageUri);
                performNavigationDrawerItemClick(1);
                profileFragment.uploadDp(selectedImagePath);
            //}
        }
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (AppController.getInstance().isLoggedIn()) {
            menu.add(Menu.FIRST, 0, Menu.NONE, "My Cart").setIcon(R.drawable.ic_cart).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    performNavigationDrawerItemClick(5);
                    return false;
                }
            }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            menu.add(Menu.NONE, 0, Menu.NONE, "Profile").setIcon(R.drawable.user_icon_logged_in).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    //getSupportFragmentManager().beginTransaction().replace(com.blunderer.materialdesignlibrary.R.id.fragment_container, profileFragment).commit();
                    performNavigationDrawerItemClick(1);
                    return false;
                }
            }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        } else {
            menu.add(Menu.NONE, 0, Menu.NONE, "Log in").setIcon(R.drawable.user_icon_not_logged_in).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    startActivity(new Intent(MainActivity.this, LoginRegisterActivity.class).putExtra("source", "ma"));
                    finish();
                    return false;
                }
            }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }
        return super.onCreateOptionsMenu(menu);
    }

}