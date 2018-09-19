package com.ncookhom;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.ncookhom.Card.MyCard;
import com.ncookhom.Contact.ContactIdara;
import com.ncookhom.MyOrders.Orders;
import com.ncookhom.MyProducts.MyProducts;
import com.ncookhom.NavFragments.AddProduct;
import com.ncookhom.NavFragments.HomeFragment;
import com.ncookhom.NavFragments.MainFragment;
import com.ncookhom.OrderedFromMe.OrderedFromM;
import com.ncookhom.Profile.Profile;
import com.ncookhom.Requests.LogIn;
import com.ncookhom.Requests.LogInTypes;
import com.squareup.picasso.Picasso;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static String customer_id = "";
    public static String email = "";
    public static String Name = "";
    public static String password = "";
    public static String phone = "";
    public static String user_image = "";
    public static String Address = "";
    public static int available ;
    public static int type = 0;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Dialog myDialog;
    MenuItem filter_icon, cart_icon;
    public static String selected_item ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MultiDex.install(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        //-----------------------------------------------------------------------------------
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/font.ttf");
        myDialog = new Dialog(this);

        Intent intent = getIntent();
        if (getIntent().getExtras() != null) {
            customer_id = intent.getExtras().getString("customer_id", "");
            email = intent.getExtras().getString("email", "");
            Name = intent.getExtras().getString("Name", "");
            password = intent.getExtras().getString("password", "");
            phone = intent.getExtras().getString("Phone", "");
            user_image = intent.getExtras().getString("img", "");
            Address = intent.getExtras().getString("Address", "");
            available = intent.getExtras().getInt("available", 0);
            type = intent.getExtras().getInt("type", -1);
        }
//        Intent intent2 = new Intent(getApplicationContext(), MainActivity.class);
//        intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        startActivity(intent2);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header_view = navigationView.getHeaderView(0);
        LinearLayout header_layout = header_view.findViewById(R.id.nav_login_layout);
        LinearLayout header_nav_logo = header_view.findViewById(R.id.nav_logo);
        Button header_login = header_view.findViewById(R.id.nav_login_btn);
        ImageView header_img = header_view.findViewById(R.id.main_profile_image);

        printhashkey();
        header_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, LogInTypes.class));
                finish();
            }
        });

        // check type to know that is login or not ....
        if (type == -1) {

            Menu nav_Menu = navigationView.getMenu();
            header_layout.setVisibility(View.VISIBLE);
            header_nav_logo.setVisibility(View.GONE);
            nav_Menu.findItem(R.id.add_product).setVisible(false);
            nav_Menu.findItem(R.id.nav_my_orders).setVisible(false);
            nav_Menu.findItem(R.id.nav_profile).setVisible(false);
            nav_Menu.findItem(R.id.settings).setVisible(false);
            nav_Menu.findItem(R.id.logout).setVisible(false);
        }
        if (type == 0) {
            Menu nav_Menu = navigationView.getMenu();
            header_layout.setVisibility(View.GONE);
            header_nav_logo.setVisibility(View.VISIBLE);
            nav_Menu.findItem(R.id.nav_my_orders).setVisible(true);
            nav_Menu.findItem(R.id.add_product).setVisible(true);
            nav_Menu.findItem(R.id.nav_profile).setVisible(true);
            nav_Menu.findItem(R.id.settings).setVisible(true);
            Picasso.with(MainActivity.this).load("http://cookehome.com/CookApp/images/" + user_image).into(header_img);
            nav_Menu.findItem(R.id.logout).setVisible(true);
        } else if (type == 1) {

            Menu nav_Menu = navigationView.getMenu();
            header_layout.setVisibility(View.GONE);
            header_nav_logo.setVisibility(View.VISIBLE);
            nav_Menu.findItem(R.id.add_product).setVisible(true);
            nav_Menu.findItem(R.id.nav_my_orders).setVisible(true);
            nav_Menu.findItem(R.id.nav_profile).setVisible(true);
            nav_Menu.findItem(R.id.settings).setVisible(true);
            Picasso.with(MainActivity.this).load("http://cookehome.com/CookApp/images/" + user_image).into(header_img);
            nav_Menu.findItem(R.id.logout).setVisible(true);
        }

        // set an icon at the right side of navigation items ...
        navigationView.getMenu().getItem(0).setActionView(R.layout.menu_image);
        navigationView.getMenu().getItem(1).setActionView(R.layout.menu_image);
        navigationView.getMenu().getItem(2).setActionView(R.layout.menu_image);
        navigationView.getMenu().getItem(3).setActionView(R.layout.menu_image);
        navigationView.getMenu().getItem(4).setActionView(R.layout.menu_image);
        navigationView.getMenu().getItem(5).setActionView(R.layout.menu_image);
        navigationView.getMenu().getItem(6).setActionView(R.layout.menu_image);
        navigationView.getMenu().getItem(7).setActionView(R.layout.menu_image);
        navigationView.getMenu().getItem(8).setActionView(R.layout.menu_image);
        navigationView.getMenu().getItem(9).setActionView(R.layout.menu_image);

        //Select Home by default
        MainFragment fragment = new MainFragment();
        displaySelectedFragment(fragment);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("هل تريد الخروج من التطبيق ؟")
                    .setCancelable(false)
                    .setNegativeButton("الغاء", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    }).setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //super.onBackPressed();
                    pref = getSharedPreferences(LogIn.MY_PREFS_NAME, Context.MODE_PRIVATE);
                    editor = pref.edit();
                    editor.clear();
                    editor.apply();

                    if (AccessToken.getCurrentAccessToken() == null) {
//                        return; // already logged out
                    }
                    new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                            .Callback() {
                        @Override
                        public void onCompleted(GraphResponse graphResponse) {
                            LoginManager.getInstance().logOut();
                        }
                    }).executeAsync();
                    // -------------------------------------------------------
//                    login_prefs = getSharedPreferences(login_pref_file, Context.MODE_PRIVATE);
//                    login_editor = login_prefs.edit();
//                  //  login_editor.putBoolean("login", false);
//                    login_editor.apply();
                    finish();

                }
            }).create().show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        filter_icon = menu.findItem(R.id.filter_icon);
        cart_icon = menu.findItem(R.id.card);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.card) {
            if (!customer_id.equals("0")) {
                startActivity(new Intent(MainActivity.this, MyCard.class));
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("يجب بتسجيل الدخول اولا")
                        .setCancelable(false)
                        .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        }).create().show();
            }
        } else if (id == R.id.filter_icon) {
            show_popup();
        }

        return super.onOptionsItemSelected(item);
    }

    private void show_popup() {
        final String[] data = getResources().getStringArray(R.array.popup_list);
        TextView txt_close;
        ListView filter_list;
        myDialog.setContentView(R.layout.filter_popup_window);
        txt_close = myDialog.findViewById(R.id.txt_close);
        filter_list = myDialog.findViewById(R.id.filter_list);
        txt_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, data);
        filter_list.setAdapter(adapter);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        myDialog.show();
        filter_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selected_item = data[i];
                Bundle bundle = new Bundle();
                bundle.putString("selected_item", selected_item);

                // set Fragment class Arguments
//                HomeFragment fragobj = new HomeFragment();
                MainFragment fragobj = new MainFragment();
                fragobj.setArguments(bundle);
                displaySelectedFragment(fragobj);
                myDialog.dismiss();
            }
        });
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        if (id == R.id.nav_home) {
            cart_icon.setVisible(true);
            filter_icon.setVisible(true);
            MainFragment mainFragment = new MainFragment();
            displaySelectedFragment(mainFragment);

        } else if (id == R.id.nav_my_orders) {
            if (customer_id.equals("")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("يجب تسجيل الدخول اولا")
                        .setCancelable(false)
                        .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create().show();
            } else {
                startActivity(new Intent(MainActivity.this, Orders.class));
            }

        } else if (id == R.id.nav_ordered_from_me) {
            if (customer_id.equals("")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("يجب تسجيل الدخول اولا")
                        .setCancelable(false)
                        .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create().show();
            } else {
                startActivity(new Intent(MainActivity.this, OrderedFromM.class));
            }

        } else if (id == R.id.nav_my_products) {
            if (customer_id.equals("")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("يجب تسجيل الدخول اولا")
                        .setCancelable(false)
                        .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create().show();
            } else {
                startActivity(new Intent(MainActivity.this, MyProducts.class));
            }

        } else if (id == R.id.nav_profile) {

            if (customer_id.equals("")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("يجب تسجيل الدخول اولا")
                        .setCancelable(false)
                        .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create().show();
            } else {

                Intent intent = new Intent(MainActivity.this, Profile.class);
                intent.putExtra("customer_id", customer_id);
                intent.putExtra("userName", Name);
                intent.putExtra("img", user_image);
                intent.putExtra("email", email);
                intent.putExtra("password", password);
                startActivity(intent);

            }
        } else if (id == R.id.nav_messages) {
            if (customer_id.equals("")) {
                Toast.makeText(this, "يجب تسجيل الدخول اولا", Toast.LENGTH_SHORT).show();
            } else {
                startActivity(new Intent(MainActivity.this, Messages.class));
            }
        } else if (id == R.id.nav_sreach) {
            startActivity(new Intent(MainActivity.this, SearchActivity.class));

        } else if (id == R.id.add_product) {
            cart_icon.setVisible(false);
            filter_icon.setVisible(false);
            fragment = new AddProduct();
            displaySelectedFragment(fragment);
        } else if (id == R.id.about_app) {
            startActivity(new Intent(MainActivity.this, About.class));

        } else if (id == R.id.contact) {
            startActivity(new Intent(MainActivity.this, ContactIdara.class));

        } else if (id == R.id.settings) {
            Intent intent = new Intent(MainActivity.this, Settings.class);
            intent.putExtra("id", customer_id);
            startActivity(intent);

        } else if (id == R.id.share_app) {

            int applicationNameId = this.getApplicationInfo().labelRes;
            final String appPackageName = this.getPackageName();
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, getString(applicationNameId));
            String text = "Install this cool application: ";
            String link = "https://play.google.com/store/apps/details?id=" + appPackageName;
            i.putExtra(Intent.EXTRA_TEXT, text + " " + link);
            startActivity(Intent.createChooser(i, "Share link:"));

        } else if (id == R.id.rate_app) {

//            Uri uri = Uri.parse("market://details?id=" + this.getPackageName());
//            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
//            // To count with Play market backstack, After pressing back button,
//            // to taken back to our application, we need to add following flags to intent.
//            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
//                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
//                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
//            try {
//                startActivity(goToMarket);
//            } catch (ActivityNotFoundException e) {
//                startActivity(new Intent(Intent.ACTION_VIEW,
//                        Uri.parse("http://play.google.com/store/apps/details?id=" + this.getPackageName())));
//            }
        } else if (id == R.id.logout) {

            // you should clear shared preferences first, then finish the activity ...
            pref = getSharedPreferences(LogIn.MY_PREFS_NAME, Context.MODE_PRIVATE);
            editor = pref.edit();
            editor.clear();
            editor.apply();

            new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                    .Callback() {
                @Override
                public void onCompleted(GraphResponse graphResponse) {

                    LoginManager.getInstance().logOut();
                }
            }).executeAsync();
//
//            // clear user data
//            user_pref = getSharedPreferences(LogIn.MY_PREFS_NAME, MODE_PRIVATE);
//            user_pref.edit().clear().apply();
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Loads the specified fragment to the frame
     *
     * @param fragment
     */
    private void displaySelectedFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void printhashkey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }
}