package com.thelogicalcoder.viazene.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.thelogicalcoder.viazene.Activities.BuyProductCOD;
import com.thelogicalcoder.viazene.Activities.LoginRegisterActivity;
import com.thelogicalcoder.viazene.Activities.SingleProductView;
import com.thelogicalcoder.viazene.Activities.SingleServiceView;
import com.thelogicalcoder.viazene.Application.AppController;
import com.thelogicalcoder.viazene.AsyncTasks.AddComment;
import com.thelogicalcoder.viazene.AsyncTasks.CartOperation;
import com.thelogicalcoder.viazene.AsyncTasks.DeleteComment;
import com.thelogicalcoder.viazene.AsyncTasks.GetProfile;
import com.thelogicalcoder.viazene.AsyncTasks.GetWall;
import com.thelogicalcoder.viazene.AsyncTasks.GetWallForUser;
import com.thelogicalcoder.viazene.AsyncTasks.WallLiker;
import com.thelogicalcoder.viazene.Data.ProductData;
import com.thelogicalcoder.viazene.Data.ServiceData;
import com.thelogicalcoder.viazene.Data.WallCommentData;
import com.thelogicalcoder.viazene.Data.WallData;
import com.thelogicalcoder.viazene.Helper.Data;
import com.thelogicalcoder.viazene.Interfaces.onCartCallBackListener;
import com.thelogicalcoder.viazene.Interfaces.onCommentDeleteListener;
import com.thelogicalcoder.viazene.Interfaces.onProfileLoadedListener;
import com.thelogicalcoder.viazene.Interfaces.onUserWallLoadListener;
import com.thelogicalcoder.viazene.Interfaces.onWallCallBackListener;
import com.thelogicalcoder.viazene.Interfaces.onWallCommentsLoadedListener;
import com.thelogicalcoder.viazene.Interfaces.onWallLoadedListener;
import com.thelogicalcoder.viazene.JSONParsers.ProfileDetailsParser;
import com.thelogicalcoder.viazene.JSONParsers.UserWallParser;
import com.thelogicalcoder.viazene.JSONParsers.WallCommentsParser;
import com.thelogicalcoder.viazene.JSONParsers.WallParser;
import com.thelogicalcoder.viazene.R;
import com.thelogicalcoder.viazene.Server.Server;
import com.thelogicalcoder.viazene.Volley.FeedImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aditya on 011, 11 July 2015.
 */
public class WallAdapter extends BaseAdapter {

    private List<WallData> wallDataList;
    private List<WallCommentData> wallCommentDataList;
    private Context context;
    private LayoutInflater inflater;

    public WallAdapter(Context context, List<WallData> wallDataList) {
        this.wallDataList = wallDataList;
        this.context = context;
        this.wallCommentDataList = AppController.getInstance().getWallCommentDataList();
    }


    @Override
    public void notifyDataSetChanged() {
        this.wallCommentDataList = AppController.getInstance().getWallCommentDataList();

        if (AppController.getInstance().isLoggedIn()) {
            AppController.getInstance().showMaterialProgress((Activity) context);
            new GetWallForUser(context, new onUserWallLoadListener() {
                @Override
                public void onUserWallLoaded(String response) {
                    AppController.getInstance().dismissMaterialProgress();
                    if (response.equalsIgnoreCase("") || response.equalsIgnoreCase("errorOccurred")) {
                    } else {
                        new UserWallParser(response);
                        WallAdapter.super.notifyDataSetChanged();
                    }

                }
            }).execute();
        }

    }

    @Override
    public int getCount() {
        return wallDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return wallDataList.get(position);
        //return wallDataList.get(wallDataList.size() - position - 1);

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.wall_card, null);
        }
        final TextView postTitle, wallDataText, likeCountText;
        FeedImageView productImage;
        final Button likeButton, commentButton, shareButton;
        final ImageView cartButton;
        final Button buyButton;

        postTitle = (TextView) convertView.findViewById(R.id.postTitle);
        wallDataText = (TextView) convertView.findViewById(R.id.wallData);
        likeCountText = (TextView) convertView.findViewById(R.id.likeCount);
        productImage = (FeedImageView) convertView.findViewById(R.id.productImage);
        likeButton = (Button) convertView.findViewById(R.id.likeButton);
        commentButton = (Button) convertView.findViewById(R.id.commentButton);
        shareButton = (Button) convertView.findViewById(R.id.shareButton);
        buyButton = (Button) convertView.findViewById(R.id.buyButton);
        cartButton = (ImageView) convertView.findViewById(R.id.cartButton);


        final WallData wallData = (WallData) getItem(position);
        ProductData productData = new ProductData();
        ServiceData serviceData = new ServiceData();

        /*for (int i = 0; i < AppController.getInstance().getProductDataList().size(); i++) {
            if (AppController.getInstance().getProductDataList().get(i).getProductID().trim().equals(wallData.getProductID().trim())) {
                productData = AppController.getInstance().getProductDataList().get(i);
                break;
            }
        }*/
        List<String> urlListProduct = new ArrayList<>();
        List<String> urlListService = new ArrayList<>();
        boolean product = true;
        if (wallData.getPostType().equalsIgnoreCase("Product")) {
            product = true;
            productData = new Data().getProductFromID(wallData.getID().trim());
            for (int i = 0; i < AppController.getInstance().getProductImagesDataList().size(); i++) {
                if (AppController.getInstance().getProductImagesDataList().get(i).getProductID().trim().equalsIgnoreCase(productData.getProductID().trim())) {
                    urlListProduct = AppController.getInstance().getProductImagesDataList().get(i).getImageURLs();
                }
            }
        } else if (wallData.getPostType().equalsIgnoreCase("Service")) {
            product = false;
            serviceData = new Data().getServiceFromID(wallData.getID().trim());
            for (int i = 0; i < AppController.getInstance().getServiceImagesDataList().size(); i++) {
                if (AppController.getInstance().getServiceImagesDataList().get(i).getServiceID().trim().equalsIgnoreCase(serviceData.getServiceID().trim())) {
                    urlListService = AppController.getInstance().getServiceImagesDataList().get(i).getImageURLs();
                }
            }
        }


        String user, comment, showThis = "";
        int count = 0;
        /*for (int i = (wallCommentDataList.size() - 1); i >= 0; i--) {
            if (wallCommentDataList.get(i).getWallID().trim().equals("" + position)) {
                if (count < 3) {
                    user = wallCommentDataList.get(i).getUser();
                    comment = wallCommentDataList.get(i).getComment();
                    showThis = showThis + "\n" + user + ": " + comment + "\n";
                    count++;
                } else break;
            }
        }*/
        for (int i = (wallCommentDataList.size() - 1); i >= 0; i--) {
            if (wallCommentDataList.get(i).getWallID().trim().equals(wallData.getWallID())) {
                if (count < 3) {
                    user = wallCommentDataList.get(i).getUser();
                    comment = wallCommentDataList.get(i).getComment();
                    showThis = showThis + "\n" + user + ": " + comment + "\n";
                    count++;
                } else break;
            }
        }


        postTitle.setText(wallData.getPostTitle() + "\n- " + wallData.getUser());
        if (showThis.equals("")) {
            wallDataText.setText("No comments yet\nBe the first to comment !");
        } else
            wallDataText.setText(showThis);

        likeCountText.setText(wallData.getLikeCount().trim().equalsIgnoreCase("0") ? "No likes yet\nBe the first to like" : wallData.getLikeCount().trim().equalsIgnoreCase("1") ? wallData.getLikeCount().trim() + " user likes this" : wallData.getLikeCount().trim() + " users like this");

        productImage.setDefaultImageResId(R.drawable.logo_loading);
        if (product) {
            productImage.setImageUrl((Server.PRODUCTS_DOMAIN + productData.getProductCategory().trim() + "/" + productData.getProductSubCategory() + "/" + productData.getProductName().trim() + "/" + urlListProduct.get(0)).replace(" ", "%20"), AppController.getInstance().getImageLoader());
        } else {
            productImage.setImageUrl((Server.SERVICES_DOMAIN + serviceData.getCategory().trim() + "/" + serviceData.getName().trim() + "/" + urlListService.get(0)).replace(" ", "%20"), AppController.getInstance().getImageLoader());
        }
        productImage.setErrorImageResId(R.drawable.error);


        if (AppController.getInstance().getLikeMap().get(position)) {
            likeButton.setText("LIKED");
            likeButton.setTextColor(context.getResources().getColor(R.color.color_primary));
        } else {
            likeButton.setText("LIKE");
            likeButton.setTextColor(Color.parseColor("#000000"));
        }

        final int pos = position;
        final ProductData finalProductData = productData;
        final ServiceData finalServiceData = serviceData;

        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (AppController.getInstance().isLoggedIn()) {
                    AppController.getInstance().showMaterialProgress((Activity) context);
                    if (AppController.getInstance().getLikeMap().get(pos)) {
                        new WallLiker(context, false, wallData.getID(), wallData.getWallID()/*"" + (pos + 1)*/, new onWallCallBackListener() {
                            @Override
                            public void onWallCallBack(String response) {
                                if (!response.trim().equalsIgnoreCase("errorOccurred")) {
                                    new GetWallForUser(context, new onUserWallLoadListener() {
                                        @Override
                                        public void onUserWallLoaded(String response) {
                                            if (response.equalsIgnoreCase("") || response.equalsIgnoreCase("errorOccurred")) {
                                                Snackbar.make(view.findViewById(R.id.root), "Cannot connect to server", Snackbar.LENGTH_SHORT).show();
                                            } else {
                                                new UserWallParser(response);
                                                new GetWall(context, new onWallLoadedListener() {
                                                    @Override
                                                    public void onWallLoaded(String response) {
                                                        AppController.getInstance().dismissMaterialProgress();
                                                        if (!response.equalsIgnoreCase("errorOccurred")) {
                                                            new WallParser(response);
                                                            WallAdapter.this.wallDataList = AppController.getInstance().getWallDataList();
                                                            if (AppController.getInstance().getLikeMap().get(pos)) {
                                                                likeButton.setText("LIKED");
                                                                likeButton.setTextColor(context.getResources().getColor(R.color.color_primary));

                                                            } else {
                                                                likeButton.setText("LIKE");
                                                                likeButton.setTextColor(Color.parseColor("#000000"));

                                                            }
                                                            WallAdapter.this.notifyDataSetChanged();
                                                        }
                                                    }
                                                }, new onWallCommentsLoadedListener() {
                                                    @Override
                                                    public void onWallCommentsLoaded(String response) {

                                                    }
                                                }).execute();

                                            }

                                        }
                                    }).execute();
                                } else {
                                    Snackbar.make(view, "Cannot connect to server", Snackbar.LENGTH_SHORT).show();
                                }
                                AppController.getInstance().dismissMaterialProgress();

                            }
                        }).execute();

                    } else {
                        new WallLiker(context, true, wallData.getID(), wallData.getWallID()/*"" + (pos + 1)*/, new onWallCallBackListener() {
                            @Override
                            public void onWallCallBack(String response) {
                                if (!response.trim().equalsIgnoreCase("errorOccurred")) {
                                    //TODO make a new like map...therefore no need to remove and add booleans to likemap....make a new request to getWallLikes.php (to be made) and update the AppController likemap according to wallID received from the above php to TRUE else set FALSE
                                    new GetWallForUser(context, new onUserWallLoadListener() {
                                        @Override
                                        public void onUserWallLoaded(String response) {
                                            if (response.equalsIgnoreCase("") || response.equalsIgnoreCase("errorOccurred")) {
                                                Snackbar.make(view.findViewById(R.id.root), "Cannot connect to server", Snackbar.LENGTH_SHORT).show();
                                            } else {
                                                new UserWallParser(response);
                                                new GetWall(context, new onWallLoadedListener() {
                                                    @Override
                                                    public void onWallLoaded(String response) {
                                                        AppController.getInstance().dismissMaterialProgress();
                                                        if (!response.equalsIgnoreCase("errorOccurred")) {
                                                            new WallParser(response);
                                                            WallAdapter.this.wallDataList = AppController.getInstance().getWallDataList();
                                                            if (AppController.getInstance().getLikeMap().get(pos)) {
                                                                likeButton.setText("LIKED");
                                                                likeButton.setTextColor(context.getResources().getColor(R.color.color_primary));

                                                            } else {
                                                                likeButton.setText("LIKE");
                                                                likeButton.setTextColor(Color.parseColor("#000000"));

                                                            }
                                                            WallAdapter.this.notifyDataSetChanged();
                                                        }
                                                    }
                                                }, new onWallCommentsLoadedListener() {
                                                    @Override
                                                    public void onWallCommentsLoaded(String response) {

                                                    }
                                                }).execute();
                                            }

                                        }
                                    }).execute();
                                } else {
                                    Snackbar.make(view, "Cannot connect to server", Snackbar.LENGTH_SHORT).show();
                                }
                                AppController.getInstance().dismissMaterialProgress();

                            }
                        }).execute();

                    }
                } else {
                    Snackbar.make(view, "Login to like posts", Snackbar.LENGTH_SHORT).setAction("LOGIN", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            context.startActivity(new Intent(context, LoginRegisterActivity.class).putExtra("source", "wall"));
                        }
                    }).show();
                }
            }
        });

        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppController.getInstance().isLoggedIn()) {
                    Button addCommentButton;
                    final ListView commentList;
                    final EditText commentText;
                    View v = View.inflate(context, R.layout.comments_dialog, null);
                    addCommentButton = (Button) v.findViewById(R.id.addCommentButton);
                    commentList = (ListView) v.findViewById(R.id.commentList);
                    commentText = (EditText) v.findViewById(R.id.newComment);
                    final int pos = position;
                    final List<String> commentViewList = new ArrayList<String>();
                    final List<String> commentEmailViewList = new ArrayList<String>();
                    final List<String> commentCommentViewList = new ArrayList<String>();
                    final List<String> commentCommentIDViewList = new ArrayList<String>();
                    for (int i = wallCommentDataList.size() - 1; i >= 0; i--) {
                        if (wallCommentDataList.get(i).getWallID().trim().equalsIgnoreCase(wallData.getWallID()/*"" + pos*/)) {
                            commentViewList.add(wallCommentDataList.get(i).getUser() + ":\n" + wallCommentDataList.get(i).getComment() + "\n");
                            commentEmailViewList.add(wallCommentDataList.get(i).getEmail());
                            commentCommentViewList.add(wallCommentDataList.get(i).getComment());
                            commentCommentIDViewList.add(wallCommentDataList.get(i).getId());
                        }
                    }

                    commentList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, long id) {
                            if (commentEmailViewList.get(position).trim().equalsIgnoreCase(AppController.getInstance().getUserInfo().getEmail())) {
                                new AlertDialog.Builder(context).setTitle("Confirm action").setMessage("Do you want to delete this comment?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        AppController.getInstance().showMaterialProgress((Activity) context);
                                        /*new DeleteComment(context, AppController.getInstance().getUserInfo().getEmail().trim(), commentCommentViewList.get(position).trim(), new onCommentDeleteListener() {*/
                                        new DeleteComment(context, commentCommentIDViewList.get(position), wallData.getWallID(), new onCommentDeleteListener() {
                                            @Override
                                            public void onCommentDeleted(String response) {
                                                AppController.getInstance().dismissMaterialProgress();

                                                if (!response.equalsIgnoreCase("errorOccurred")) {
                                                    new GetWall(context, new onWallLoadedListener() {
                                                        @Override
                                                        public void onWallLoaded(String response) {
                                                            if (!response.equalsIgnoreCase("errorOccurred")) {
                                                                new WallParser(response);
                                                            } else {
                                                                Snackbar.make(view, "Cannot connect to server", Snackbar.LENGTH_LONG).setActionTextColor(Color.WHITE).show();
                                                                //                             AppController.getInstance().dismissMaterialProgress();
                                                            }
                                                        }
                                                    }, new onWallCommentsLoadedListener() {
                                                        @Override
                                                        public void onWallCommentsLoaded(String response) {
                                                            AppController.getInstance().dismissMaterialProgress();
                                                            if (!response.equalsIgnoreCase("errorOccurred")) {
                                                                new WallCommentsParser(response);
                                                                WallAdapter.this.wallCommentDataList = AppController.getInstance().getWallCommentDataList();
                                                                WallAdapter.this.notifyDataSetChanged();
                                                                commentViewList.removeAll(commentViewList);
                                                                commentEmailViewList.removeAll(commentEmailViewList);
                                                                commentCommentViewList.removeAll(commentCommentViewList);
                                                                commentCommentIDViewList.removeAll(commentCommentIDViewList);
                                                                for (int i = wallCommentDataList.size() - 1; i > 0; i--) {
                                                                    if (wallCommentDataList.get(i).getWallID().trim().equalsIgnoreCase(wallData.getWallID()/*"" + pos*/)) {
                                                                        commentViewList.add(wallCommentDataList.get(i).getUser() + ":\n" + wallCommentDataList.get(i).getComment() + "\n");
                                                                        commentEmailViewList.add(wallCommentDataList.get(i).getEmail());
                                                                        commentCommentViewList.add(wallCommentDataList.get(i).getComment());
                                                                        commentCommentIDViewList.add(wallCommentDataList.get(i).getId());


                                                                    }
                                                                }
                                                                commentList.setAdapter(new MaterialArrayAdapter(context, R.layout.material_simple_list_item_1, commentViewList, "wallCommentsView"));

                                                                commentText.setText("");

                                                            } else {
                                                                Snackbar.make(view, "Cannot connect to server", Snackbar.LENGTH_LONG).setActionTextColor(Color.WHITE).show();
                                                            }
                                                        }
                                                    }).execute();
                                                } else {
                                                    Snackbar.make(view, "Cannot connect to server", Snackbar.LENGTH_LONG).setActionTextColor(Color.WHITE).show();
                                                    AppController.getInstance().dismissMaterialProgress();
                                                }
                                            }
                                        }).execute();
                                    }
                                }).setNegativeButton("No", null).create().show();
                            }
                            return false;
                        }
                    });

                    addCommentButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View v) {
                            if (commentText.getText().toString().trim().isEmpty()) {
                                commentText.setError("Enter comment");
                            } else {
                                AppController.getInstance().showMaterialProgress((Activity) context);
                                new AddComment(context, wallData.getID(), wallData.getWallID()/*"" + (pos)*/, commentText.getText().toString().trim(), new onWallCallBackListener() {
                                    @Override
                                    public void onWallCallBack(String response) {
                                        if (!response.equalsIgnoreCase("errorOccurred")) {
                                            new GetWall(context, new onWallLoadedListener() {
                                                @Override
                                                public void onWallLoaded(String response) {
                                                    if (!response.equalsIgnoreCase("errorOccurred")) {
                                                        new WallParser(response);
                                                    } else {
                                                        Snackbar.make(v.findViewById(R.id.root), "Cannot connect to server", Snackbar.LENGTH_LONG).setActionTextColor(Color.WHITE).show();

                                                    }
                                                }
                                            }, new onWallCommentsLoadedListener() {
                                                @Override
                                                public void onWallCommentsLoaded(String response) {
                                                    AppController.getInstance().dismissMaterialProgress();

                                                    if (!response.equalsIgnoreCase("errorOccurred")) {

                                                        new WallCommentsParser(response);
                                                        WallAdapter.this.wallCommentDataList = AppController.getInstance().getWallCommentDataList();
                                                        WallAdapter.this.notifyDataSetChanged();
                                                        commentViewList.removeAll(commentViewList);
                                                        commentEmailViewList.removeAll(commentEmailViewList);
                                                        commentCommentViewList.removeAll(commentCommentViewList);
                                                        commentCommentIDViewList.removeAll(commentCommentIDViewList);

                                                        for (int i = wallCommentDataList.size() - 1; i > 0; i--) {
                                                            if (wallCommentDataList.get(i).getWallID().trim().equalsIgnoreCase(wallData.getWallID()/*"" + pos*/)) {
                                                                commentViewList.add(wallCommentDataList.get(i).getUser() + ":\n" + wallCommentDataList.get(i).getComment() + "\n");
                                                                commentEmailViewList.add(wallCommentDataList.get(i).getEmail());
                                                                commentCommentViewList.add(wallCommentDataList.get(i).getComment());
                                                                commentCommentIDViewList.add(wallCommentDataList.get(i).getId());

                                                            }
                                                        }
                                                        commentList.setAdapter(new MaterialArrayAdapter(context, R.layout.material_simple_list_item_1, commentViewList, "wallCommentsView"));

                                                        commentText.setText("");
                                                    } else {
                                                        Snackbar.make(v.findViewById(R.id.root), "Cannot connect to server", Snackbar.LENGTH_LONG).setActionTextColor(Color.WHITE).show();
                                                    }
                                                }
                                            }).execute();
                                        }
                                    }
                                }).execute();
                            }
                        }
                    });
                    commentList.setAdapter(new MaterialArrayAdapter(context, R.layout.material_simple_list_item_1, commentViewList, "wallCommentsView"));
                    new AlertDialog.Builder(context).setView(v).create().show();
                } else {
                    Snackbar.make(view, "Login to comment on posts", Snackbar.LENGTH_SHORT).setAction("LOGIN", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            context.startActivity(new Intent(context, LoginRegisterActivity.class).putExtra("source", "wall"));
                        }
                    }).show();
                }
            }
        });

        final boolean finalProduct = product;
        final List<String> finalUrlListProduct = urlListProduct;
        final List<String> finalUrlListService = urlListService;
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppController.getInstance().isLoggedIn()) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    if (finalProduct) {
                        sendIntent.putExtra(Intent.EXTRA_TEXT, "Post Title: " + wallData.getPostTitle() + "\n" + (Server.PRODUCTS_DOMAIN + finalProductData.getProductCategory().trim() + "/" + finalProductData.getProductSubCategory() + "/" + finalProductData.getProductName().trim() + "/" + finalUrlListProduct.get(0)).replace(" ", "%20") + "\n" + wallData.getLikeCount() + " people like this\n\nShared via VIAZENE App\nDownload the app now - Hurry Up !!!");
                    } else {
                        sendIntent.putExtra(Intent.EXTRA_TEXT, "Post Title: " + wallData.getPostTitle() + "\n" + (Server.SERVICES_DOMAIN + finalServiceData.getCategory().trim() + "/" + finalServiceData.getName().trim() + "/" + finalUrlListService.get(0)).replace(" ", "%20") + "\n" + wallData.getLikeCount() + " people like this\n\nShared via VIAZENE App\nDownload the app now - Hurry Up !!!");
                    }
                    sendIntent.setType("text/plain");
                    context.startActivity(Intent.createChooser(sendIntent, "Share with"));
                } else
                    Snackbar.make(v, "Login to share posts", Snackbar.LENGTH_SHORT).setAction("LOGIN", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            context.startActivity(new Intent(context, LoginRegisterActivity.class).putExtra("source", "wall"));
                        }
                    }).show();
            }
        });
        final boolean finalProduct3 = product;
        final ProductData finalProductData3 = productData;

        if (AppController.getInstance().isLoggedIn()) {
            //System.out.println("Debugging\n"+finalProduct3 + "\n" + finalProductData3.getProductID()+"\n\n\n-");
            cartButton.setVisibility(View.VISIBLE);
            if (wallData.getPostType().trim().equalsIgnoreCase("Product".trim())) {
                if (AppController.getInstance().getUserProfile().getUserCart(). contains(finalProductData3.getProductID())) {
                    cartButton.setImageResource(R.drawable.remove_from_cart);
                } else cartButton.setImageResource(R.drawable.add_to_cart);
            } else {
                cartButton.setVisibility(View.GONE);
            }
        }

        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (AppController.getInstance().isLoggedIn()) {
                    if (!AppController.getInstance().getUserProfile().getUserCart().contains(finalProductData.getProductID())) {
                        AppController.getInstance().showMaterialProgress((Activity) context);
                        new CartOperation(context, true, finalProductData.getProductID(), new onCartCallBackListener() {
                            @Override
                            public void onCartCallBack(String response) {
                                if (response.equalsIgnoreCase("errorOccurred")) {
                                    Snackbar.make(v, "Cannot connect to server", Snackbar.LENGTH_SHORT).show();
                                } else {
                                    new GetProfile(context, AppController.getInstance().getUserInfo().getEmail(), new onProfileLoadedListener() {
                                        @Override
                                        public void onProfileLoaded(String response) {
                                            if (!response.equalsIgnoreCase("errorOccurred")) {
                                                new ProfileDetailsParser(response);
                                                cartButton.setImageResource(R.drawable.remove_from_cart);
                                            } else {
                                                Snackbar.make(v, "Cannot connect to server", Snackbar.LENGTH_SHORT).show();
                                            }
                                        }
                                    }).execute();

                                }
                                AppController.getInstance().dismissMaterialProgress();
                            }
                        }).execute();
                    } else {
                        AppController.getInstance().showMaterialProgress((Activity) context);
                        new CartOperation(context, false, finalProductData.getProductID(), new onCartCallBackListener() {
                            @Override
                            public void onCartCallBack(String response) {
                                if (response.equalsIgnoreCase("errorOccurred")) {
                                    Snackbar.make(v, "Cannot connect to server", Snackbar.LENGTH_SHORT).show();
                                } else {
                                    new GetProfile(context, AppController.getInstance().getUserInfo().getEmail(), new onProfileLoadedListener() {
                                        @Override
                                        public void onProfileLoaded(String response) {
                                            if (!response.equalsIgnoreCase("errorOccurred")) {
                                                new ProfileDetailsParser(response);
                                                cartButton.setImageResource(R.drawable.add_to_cart);
                                            } else {
                                                Snackbar.make(v, "Cannot connect to server", Snackbar.LENGTH_SHORT).show();
                                            }
                                        }
                                    }).execute();
                                }
                                AppController.getInstance().dismissMaterialProgress();
                            }
                        }).execute();
                    }
                } else {
                    Snackbar.make(v, "Login to add product to cart", Snackbar.LENGTH_LONG).setAction("LOGIN", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            context.startActivity(new Intent(context, LoginRegisterActivity.class).putExtra("source", "spv"));
                        }
                    }).show();
                }
            }
        });

        final boolean finalProduct2 = product;
        final ServiceData finalServiceData2 = serviceData;
        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalProduct2) {
                    context.startActivity(new Intent(context, BuyProductCOD.class).putExtra("productID", finalProductData.getProductID()).putExtra("mode", "single"));
                } else {
                    context.startActivity(new Intent(context, SingleServiceView.class).putExtra("serviceID", finalServiceData2.getServiceID()));
                }
            }
        });

        final boolean finalProduct1 = product;
        final ServiceData finalServiceData1 = serviceData;
        productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalProduct1) {
                    context.startActivity(new Intent(context, SingleProductView.class).putExtra("productID", finalProductData.getProductID()));
                } else {
                    context.startActivity(new Intent(context, SingleServiceView.class).putExtra("serviceID", finalServiceData1.getServiceID()));
                }
            }
        });

        return convertView;
    }
}
