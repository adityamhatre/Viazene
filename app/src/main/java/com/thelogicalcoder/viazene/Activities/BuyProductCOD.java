package com.thelogicalcoder.viazene.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.blunderer.materialdesignlibrary.activities.Activity;
import com.blunderer.materialdesignlibrary.handlers.ActionBarHandler;
import com.thelogicalcoder.viazene.Adapters.MaterialArrayAdapter;
import com.thelogicalcoder.viazene.Application.AppController;
import com.thelogicalcoder.viazene.AsyncTasks.AddToWall;
import com.thelogicalcoder.viazene.AsyncTasks.AddToWallMultiple;
import com.thelogicalcoder.viazene.AsyncTasks.BuyMultipleProductsCODAsync;
import com.thelogicalcoder.viazene.AsyncTasks.BuyProductCODAsync;
import com.thelogicalcoder.viazene.AsyncTasks.GetOrderID;
import com.thelogicalcoder.viazene.AsyncTasks.GetWall;
import com.thelogicalcoder.viazene.AsyncTasks.SendMultipleProductBoughtEmail;
import com.thelogicalcoder.viazene.AsyncTasks.SendProductBoughtEmail;
import com.thelogicalcoder.viazene.Data.ProductData;
import com.thelogicalcoder.viazene.Helper.Data;
import com.thelogicalcoder.viazene.Interfaces.onAsyncCallBack;
import com.thelogicalcoder.viazene.Interfaces.onWallCommentsLoadedListener;
import com.thelogicalcoder.viazene.Interfaces.onWallLoadedListener;
import com.thelogicalcoder.viazene.JSONParsers.WallCommentsParser;
import com.thelogicalcoder.viazene.JSONParsers.WallParser;
import com.thelogicalcoder.viazene.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aditya on 028, 28 July 2015.
 */
public class BuyProductCOD extends Activity {


    ListView productList;
    Button changeAddress, buy;

    List<ProductData> singleProduct;
    List<String> singleProductName;

    List<ProductData> multipleProductList;
    List<String> multipleProductNameList;

    TextView textView;

    int orderID = 0;

    Boolean isAddressChanged = false;
    String newAddress = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        productList = (ListView) findViewById(R.id.productList);
        changeAddress = (Button) findViewById(R.id.changeAddress);
        textView = (TextView) findViewById(R.id.textView7);
        buy = (Button) findViewById(R.id.buy);

        switch (getIntent().getStringExtra("mode").trim()) {
            case "single":
                singleProduct = new ArrayList<>();
                singleProductName = new ArrayList<>();

                singleProduct.add(new Data().getProductFromID(getIntent().getStringExtra("productID")));
                singleProductName.add(AppController.getInstance().getProductNameByID(getIntent().getStringExtra("productID")));
                productList.setAdapter(new MaterialArrayAdapter(this, R.layout.material_simple_list_item_1, singleProductName, "buyCODList"));
                textView.setText("BUY THIS PRODUCT");

                if (singleProduct.get(0).getProductCount().equalsIgnoreCase("0")) {
                    buy.setText("OUT OF STOCK");
                    buy.setEnabled(false);
                }

                buy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (AppController.getInstance().isLoggedIn()) {
                            new AlertDialog.Builder(BuyProductCOD.this).setTitle("Confirm Address").setMessage(isAddressChanged ? newAddress : AppController.getInstance().getUserInfo().getAddress()).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //:TODO add buy logic
                                    AppController.getInstance().showMaterialProgress(BuyProductCOD.this);
                                    new GetOrderID(BuyProductCOD.this, new onAsyncCallBack() {
                                        @Override
                                        public void onCallBack(String response) {
                                            if (!response.equalsIgnoreCase("errorOccurred")) {
                                                orderID = Integer.parseInt(response.trim()) + 1;
                                                new BuyProductCODAsync(BuyProductCOD.this, singleProduct.get(0), "" + orderID, new onAsyncCallBack() {
                                                    @Override
                                                    public void onCallBack(String response) {
                                                        AppController.getInstance().dismissMaterialProgress();

                                                        if (!response.equalsIgnoreCase("errorOccurred")) {
                                                            AppController.getInstance().showMaterialProgress(BuyProductCOD.this);
                                                            new SendProductBoughtEmail(BuyProductCOD.this, singleProduct.get(0),""+orderID, new onAsyncCallBack() {
                                                                @Override
                                                                public void onCallBack(String response) {
                                                                    AppController.getInstance().dismissMaterialProgress();
                                                                    if (!response.equalsIgnoreCase("errorOccurred")) {
                                                                        final AlertDialog alertDialog;
                                                                        alertDialog = new AlertDialog.Builder(BuyProductCOD.this).setTitle("INFO").setMessage("\"" + singleProduct.get(0).getProductName() + "\" ordered\nOrder ID is " + orderID + "\n\nDo you want to post your purchase on wall?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(DialogInterface dialog, int which) {
                                                                            }
                                                                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(DialogInterface dialog, int which) {
                                                                            }
                                                                        }).create();

                                                                        alertDialog.show();
                                                                        alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(View v) {
                                                                                alertDialog.dismiss();
                                                                                startActivity(new Intent(BuyProductCOD.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                                                                finish();
                                                                            }
                                                                        });
                                                                        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(View v) {
                                                                                View postTitleView = View.inflate(BuyProductCOD.this, R.layout.post_title, null);
                                                                                final EditText postTitle = (EditText) postTitleView.findViewById(R.id.postTitle);
                                                                                final AlertDialog alertDialog1 = new AlertDialog.Builder(BuyProductCOD.this).setTitle("Post Title").setView(postTitleView).setPositiveButton("Post", new DialogInterface.OnClickListener() {
                                                                                    @Override
                                                                                    public void onClick(DialogInterface dialog, int which) {

                                                                                    }
                                                                                }).create();

                                                                                alertDialog1.show();
                                                                                alertDialog1.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                                                                                    @Override
                                                                                    public void onClick(View v) {
                                                                                        if (postTitle.getText().toString().trim().isEmpty()) {
                                                                                            postTitle.setError("Enter post title");
                                                                                        } else {
                                                                                            AppController.getInstance().showMaterialProgress(BuyProductCOD.this);
                                                                                            new AddToWall(BuyProductCOD.this, singleProduct.get(0).getProductID(), postTitle.getText().toString().trim(), new onAsyncCallBack() {
                                                                                                @Override
                                                                                                public void onCallBack(String response) {                                                                                                                    AppController.getInstance().dismissMaterialProgress();

                                                                                                    if (!response.equalsIgnoreCase("errorOccurred")) {
                                                                                                        AppController.getInstance().showMaterialProgress(BuyProductCOD.this);

                                                                                                        new GetWall(BuyProductCOD.this, new onWallLoadedListener() {
                                                                                                            @Override
                                                                                                            public void onWallLoaded(String response) {
                                                                                                                if (!response.equalsIgnoreCase("errorOccurred")) {
                                                                                                                    new WallParser(response);
                                                                                                                } else {
                                                                                                                    Snackbar.make(findViewById(R.id.root), "Cannot connect to server", Snackbar.LENGTH_LONG).show();
                                                                                                                }
                                                                                                            }
                                                                                                        }, new onWallCommentsLoadedListener() {
                                                                                                            @Override
                                                                                                            public void onWallCommentsLoaded(String response) {
                                                                                                                if (!response.equalsIgnoreCase("errorOccurred")) {
                                                                                                                    new WallCommentsParser(response);
                                                                                                                    AppController.getInstance().getWallFragment().updateWall();
                                                                                                                    AppController.getInstance().dismissMaterialProgress();
                                                                                                                    if (!response.equalsIgnoreCase("errorOccurred")) {
                                                                                                                        alertDialog1.dismiss();
                                                                                                                        alertDialog.dismiss();
                                                                                                                        Snackbar.make(findViewById(R.id.root), "Posted to wall", Snackbar.LENGTH_SHORT).show();
                                                                                                                        new Handler().postDelayed(new Runnable() {
                                                                                                                            @Override
                                                                                                                            public void run() {
                                                                                                                                startActivity(new Intent(BuyProductCOD.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                                                                                                                finish();
                                                                                                                            }
                                                                                                                        }, 700);
                                                                                                                    } else {
                                                                                                                        Snackbar.make(findViewById(R.id.root), "Cannot connect to server", Snackbar.LENGTH_SHORT).show();
                                                                                                                    }
                                                                                                                } else {
                                                                                                                    Snackbar.make(findViewById(R.id.root), "Cannot connect to server", Snackbar.LENGTH_LONG).show();
                                                                                                                }
                                                                                                            }
                                                                                                        }).execute();
                                                                                                    }else {
                                                                                                        Snackbar.make(findViewById(R.id.root), "Cannot connect to server", Snackbar.LENGTH_LONG).show();
                                                                                                    }


                                                                                                }
                                                                                            }).execute();
                                                                                        }
                                                                                    }
                                                                                });
                                                                            }
                                                                        });
                                                                    } else {
                                                                        Snackbar.make(findViewById(R.id.root), "Cannot connect to server", Snackbar.LENGTH_LONG).show();
                                                                    }
                                                                }
                                                            }).execute();
                                                        } else
                                                            Snackbar.make(findViewById(R.id.root), "Cannot connect to server", Snackbar.LENGTH_LONG).show();
                                                    }
                                                }).execute();
                                            } else {
                                                Snackbar.make(findViewById(R.id.root), "Cannot connect to server", Snackbar.LENGTH_LONG).show();

                                                AppController.getInstance().dismissMaterialProgress();
                                            }
                                        }
                                    }).execute();

                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //:TODO change address
                                    changeAddress();

                                }
                            }).create().show();
                        } else {
                            Snackbar.make(findViewById(R.id.root), "Login to buy product", Snackbar.LENGTH_LONG).setAction("LOGIN", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(new Intent(BuyProductCOD.this, LoginRegisterActivity.class).putExtra("source", "buyP"));
                                }
                            }).show();
                        }
                    }
                });

                changeAddress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        changeAddress();
                    }
                });

                break;
            case "multiple":
                multipleProductList = new ArrayList<>();
                multipleProductNameList = new ArrayList<>();

                multipleProductList = (List<ProductData>) getIntent().getSerializableExtra("productIDs");
                for (int i = 0; i < multipleProductList.size(); i++) {
                    multipleProductNameList.add(AppController.getInstance().getProductNameByID(multipleProductList.get(i).getProductID()));
                }

                List<String> removedProducts = new ArrayList<>();
                for (int i = 0; i < multipleProductList.size(); i++) {
                    if (multipleProductList.get(i).getProductCount().trim().equalsIgnoreCase("0")) {
                        removedProducts.add(multipleProductList.get(i).getProductName());
                        multipleProductList.remove(i);
                    }
                }
                if (removedProducts.size() != 0) {
                    String showThis = "";
                    for (int i = 0; i < removedProducts.size(); i++) {
                        showThis = showThis + "\"" + removedProducts.get(i) + "\" out of stock\n";
                    }
                    showThis = showThis.trim();
                    new AlertDialog.Builder(BuyProductCOD.this).setTitle("Notice").setMessage(showThis).setPositiveButton("OK", null).create().show();
                }
                if (multipleProductList.size() == 0) {
                    buy.setText("OUT OF STOCK");
                    buy.setEnabled(false);
                }


                buy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (AppController.getInstance().isLoggedIn()) {
                            new AlertDialog.Builder(BuyProductCOD.this).setTitle("Confirm Address").setMessage(isAddressChanged ? newAddress : AppController.getInstance().getUserInfo().getAddress()).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //:TODO add buy logic
                                    AppController.getInstance().showMaterialProgress(BuyProductCOD.this);
                                    new GetOrderID(BuyProductCOD.this, new onAsyncCallBack() {
                                        @Override
                                        public void onCallBack(String response) {
                                            if (!response.equalsIgnoreCase("errorOccurred")) {
                                                orderID = Integer.parseInt(response.trim()) + 1;
                                                new BuyMultipleProductsCODAsync(BuyProductCOD.this, multipleProductList, "" + orderID, new onAsyncCallBack() {
                                                    @Override
                                                    public void onCallBack(String response) {
                                                        AppController.getInstance().dismissMaterialProgress();
                                                        if (!response.equalsIgnoreCase("errorOccurred")) {
                                                            AppController.getInstance().showMaterialProgress(BuyProductCOD.this);
                                                            new SendMultipleProductBoughtEmail(BuyProductCOD.this, multipleProductList,"" + orderID, new onAsyncCallBack() {
                                                                @Override
                                                                public void onCallBack(String response) {
                                                                    AppController.getInstance().dismissMaterialProgress();
                                                                    if (!response.equalsIgnoreCase("errorOccurred")) {
//Snackbar.make(findViewById(R.id.root), "\"" + singleProduct.get(0).getProductName() + "\" ordered\nOrder ID is " + orderID, Snackbar.LENGTH_LONG).show();
                                                                        final AlertDialog alertDialog;
                                                                        String showThis = "";
                                                                        int tempOrderID = orderID;
                                                                        for (int i = 0; i < multipleProductNameList.size(); i++, tempOrderID++) {
                                                                            showThis = showThis + "\"" + multipleProductNameList.get(i) + "\" ordered\nOrder ID is " + tempOrderID + "\n\n";
                                                                        }
                                                                        alertDialog = new AlertDialog.Builder(BuyProductCOD.this).setTitle("INFO").setMessage(showThis.trim() + "\n\nDo you want to post this purchase on wall?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(DialogInterface dialog, int which) {
                                                                            }
                                                                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(DialogInterface dialog, int which) {
                                                                            }
                                                                        }).create();

                                                                        alertDialog.show();
                                                                        alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(View v) {
                                                                                alertDialog.dismiss();
                                                                                startActivity(new Intent(BuyProductCOD.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                                                                finish();
                                                                            }
                                                                        });
                                                                        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(View v) {

                                                                                final LinearLayout linearLayout = new LinearLayout(BuyProductCOD.this);
                                                                                linearLayout.setOrientation(LinearLayout.VERTICAL);
                                                                                linearLayout.removeAllViews();
                                                                                final List<Boolean> booleans = new ArrayList<Boolean>();
                                                                                for (int i = 0; i < multipleProductList.size(); i++) {
                                                                                    final TextInputLayout textInputLayout = new TextInputLayout(BuyProductCOD.this);
                                                                                    final EditText editText = new EditText(BuyProductCOD.this);
                                                                                    editText.setSingleLine(true);
                                                                                    editText.setTag("" + i);
                                                                                    editText.setHint("Post title for product");
                                                                                    textInputLayout.addView(editText);
                                                                                    linearLayout.addView(textInputLayout);
                                                                                }

                                                                                //View postTitleView = View.inflate(BuyProductCOD.this, R.layout.post_title, null);
                                                                                //final EditText postTitle = (EditText) postTitleView.findViewById(R.id.postTitle);
                                                                                final AlertDialog alertDialog1 = new AlertDialog.Builder(BuyProductCOD.this).setTitle("Post Title").setView(linearLayout).setPositiveButton("Post", new DialogInterface.OnClickListener() {
                                                                                    @Override
                                                                                    public void onClick(DialogInterface dialog, int which) {

                                                                                    }
                                                                                }).create();

                                                                                alertDialog1.show();
                                                                                alertDialog1.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                                                                                    @Override
                                                                                    public void onClick(View v) {
                                                                                        for (int i = 0; i < multipleProductList.size(); i++) {
                                                                                            if (((EditText) linearLayout.findViewWithTag("" + i)).getText().toString().trim().isEmpty()) {
                                                                                                booleans.add(i, false);
                                                                                            } else {
                                                                                                booleans.add(i, true);
                                                                                            }
                                                                                        }

                                                                                        if (booleans.contains(false)) {
                                                                                            for (int i = 0; i < booleans.size(); i++) {
                                                                                                if (!booleans.get(i)) {
                                                                                                    ((EditText) linearLayout.findViewWithTag("" + i)).setError("Enter post title");
                                                                                                }
                                                                                            }
                                                                                        } else {
                                                                                            List<String> postTitles = new ArrayList<String>();
                                                                                            for (int i = 0; i < multipleProductList.size(); i++) {
                                                                                                postTitles.add(((EditText) linearLayout.findViewWithTag("" + i)).getText().toString().trim() + "\nFrom MAKE MY MATCH");
                                                                                            }
                                                                                            AppController.getInstance().showMaterialProgress(BuyProductCOD.this);
                                                                                            new AddToWallMultiple(BuyProductCOD.this, multipleProductList, postTitles, new onAsyncCallBack() {
                                                                                                @Override
                                                                                                public void onCallBack(String response) {
                                                                                                    AppController.getInstance().dismissMaterialProgress();
                                                                                                    if (!response.equalsIgnoreCase("errorOccurred")) {

                                                                                                        new GetWall(BuyProductCOD.this, new onWallLoadedListener() {
                                                                                                            @Override
                                                                                                            public void onWallLoaded(String response) {
                                                                                                                if (!response.equalsIgnoreCase("errorOccurred")) {
                                                                                                                    new WallParser(response);
                                                                                                                } else {
                                                                                                                    Snackbar.make(findViewById(R.id.root), "Cannot connect to server", Snackbar.LENGTH_LONG).show();
                                                                                                                }
                                                                                                            }
                                                                                                        }, new onWallCommentsLoadedListener() {
                                                                                                            @Override
                                                                                                            public void onWallCommentsLoaded(String response) {
                                                                                                                if (!response.equalsIgnoreCase("errorOccurred")) {
                                                                                                                    new WallCommentsParser(response);
                                                                                                                    AppController.getInstance().getWallFragment().updateWall();
                                                                                                                    AppController.getInstance().dismissMaterialProgress();
                                                                                                                    if (!response.equalsIgnoreCase("errorOccurred")) {
                                                                                                                        alertDialog1.dismiss();
                                                                                                                        alertDialog.dismiss();
                                                                                                                        Snackbar.make(findViewById(R.id.root), "Posted to wall", Snackbar.LENGTH_SHORT).show();
                                                                                                                        new Handler().postDelayed(new Runnable() {
                                                                                                                            @Override
                                                                                                                            public void run() {
                                                                                                                                startActivity(new Intent(BuyProductCOD.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                                                                                                                finish();
                                                                                                                            }
                                                                                                                        }, 700);
                                                                                                                    } else {
                                                                                                                        Snackbar.make(findViewById(R.id.root), "Cannot connect to server", Snackbar.LENGTH_SHORT).show();
                                                                                                                    }
                                                                                                                } else {
                                                                                                                    Snackbar.make(findViewById(R.id.root), "Cannot connect to server", Snackbar.LENGTH_LONG).show();
                                                                                                                }
                                                                                                            }
                                                                                                        }).execute();


                                                                                                    } else {
                                                                                                        Snackbar.make(findViewById(R.id.root), "Cannot connect to server", Snackbar.LENGTH_SHORT).show();
                                                                                                    }
                                                                                                }
                                                                                            }).execute();
                                                                                        }
                                                                                    }
                                                                                });
                                                                            }
                                                                        });
                                                                    } else {
                                                                        Snackbar.make(findViewById(R.id.root), "Cannot connect to server", Snackbar.LENGTH_LONG).show();
                                                                    }
                                                                }
                                                            }).execute();
                                                        } else
                                                            Snackbar.make(findViewById(R.id.root), "Cannot connect to server", Snackbar.LENGTH_LONG).show();
                                                    }
                                                }).execute();
                                            } else {
                                                Snackbar.make(findViewById(R.id.root), "Cannot connect to server", Snackbar.LENGTH_LONG).show();

                                                AppController.getInstance().dismissMaterialProgress();
                                            }
                                        }
                                    }).execute();

                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //:TODO change address
                                    changeAddress();

                                }
                            }).create().show();
                        } else {
                            Snackbar.make(findViewById(R.id.root), "Login to buy product", Snackbar.LENGTH_LONG).setAction("LOGIN", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(new Intent(BuyProductCOD.this, LoginRegisterActivity.class).putExtra("source", "buyP"));
                                }
                            }).show();
                        }
                    }
                });

                changeAddress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        changeAddress();
                    }
                });


                productList.setAdapter(new MaterialArrayAdapter(this, R.layout.material_simple_list_item_1, multipleProductNameList, "buyCODList"));
                textView.setText("BUY THESE PRODUCTS");


                break;
        }
    }

    void changeAddress() {
        View view = View.inflate(BuyProductCOD.this, R.layout.change_address, null);
        final EditText addressText1, addressText2, addressText3;
        addressText1 = (EditText) view.findViewById(R.id.addressText1);
        addressText2 = (EditText) view.findViewById(R.id.addressText2);
        addressText3 = (EditText) view.findViewById(R.id.addressText3);

        new AlertDialog.Builder(BuyProductCOD.this).setTitle("Change Address").setView(view).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Boolean a1 = false, a2 = false, a3 = false;
                if (!addressText1.getText().toString().isEmpty()) {
                    a1 = true;
                } else addressText1.setError("Enter Address");
                if (!addressText2.getText().toString().isEmpty()) {
                    a2 = true;
                } else addressText2.setError("Enter Address");
                if (!addressText3.getText().toString().isEmpty()) {
                    a3 = true;
                } else addressText3.setError("Enter Address");

                if (a1 && a2 && a3) {
                    isAddressChanged = true;
                    newAddress = addressText1.getText().toString().trim() + ", " + addressText2.getText().toString().trim() + ", " + addressText3.getText().toString().trim();
                }
            }
        }).create().show();
    }


    @Override
    protected int getContentView() {
        return R.layout.buy_product_cod;
    }

    @Override
    protected boolean enableActionBarShadow() {
        return false;
    }

    @Override
    protected ActionBarHandler getActionBarHandler() {
        return null;
    }
}
