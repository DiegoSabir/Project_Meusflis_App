package com.example.meusflis.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.meusflis.Activities.MainActivity;
import com.example.meusflis.Activities.VideoPlayerOnlineActivity;
import com.example.meusflis.Common.Common;
import android.Manifest;

import com.example.meusflis.Fragments.MovieDetailsFragment;
import com.example.meusflis.Models.MoviesModel;
import com.example.meusflis.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iambedant.text.BuildConfig;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class ClassDialogs {

    public static void showBottomDetailsMovie(Context context, MoviesModel moviesModel){

        FragmentManager fragmentManager = ((MainActivity)context).getSupportFragmentManager();
        Fragment detailsFragment = new MovieDetailsFragment();

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetDialogTheme);

        View bottomSheetView = LayoutInflater.from(context).inflate(R.layout.layout_bottom_sheet_info_movie, null);

        TextView tvTitle, tvSinopsis, tvTime;
        ImageView imgMovie, closeButtom;
        LinearLayout btnShowVideo, btnDownload, btnNoLink, btnShare;
        RelativeLayout containerMoreDetails;

        tvTitle = bottomSheetView.findViewById(R.id.tv_info_title_movie);
        tvSinopsis = bottomSheetView.findViewById(R.id.tv_info_sinopsis_movie);
        tvTime = bottomSheetView.findViewById(R.id.tv_info_time_movie);

        imgMovie = bottomSheetView.findViewById(R.id.img_info_movie);
        closeButtom = bottomSheetView.findViewById(R.id.close_info_movie);

        btnShowVideo = bottomSheetView.findViewById(R.id.show_icon);
        btnDownload = bottomSheetView.findViewById(R.id.download_icon);
        btnNoLink = bottomSheetView.findViewById(R.id.enlace_icon);
        btnShare = bottomSheetView.findViewById(R.id.share_icon);

        containerMoreDetails = bottomSheetView.findViewById(R.id.containerMoreDetails);

        tvTitle.setText(moviesModel.getTitleMovie());
        tvTime.setText(moviesModel.getTimeMovie());
        tvSinopsis.setText(moviesModel.getSinopsisMovie());

        try{
            Picasso.get().load(moviesModel.getImageMovie()).resize(720, 720).onlyScaleDown().into(imgMovie);
        }
        catch (Exception e){
            Picasso.get().load(R.drawable.baseline_image_not_supported_24).into(imgMovie);
        }

        btnShowVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showVideoMovie(context, moviesModel.getVideoMovie());
            }
        });

        btnNoLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean installed = appInstallOrNot("com.whatsapp", context);

                if (installed){
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + Common.MOBILE_NUMBER + "&text=El enlace de la pelicula *" + moviesModel.getTitleMovie() + "* no funciona."));
                    context.startActivity(intent);
                }
                else{
                    Toast.makeText(context, "WhatSapp no esta instalado", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareMovieWithImage(context, imgMovie, moviesModel.getIdMovie(), moviesModel.getTitleMovie());
            }
        });

        containerMoreDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                Bundle bundle = new Bundle();
                bundle.putString("title", moviesModel.getTitleMovie());
                bundle.putString("sinopsis", moviesModel.getSinopsisMovie());
                bundle.putString("time", moviesModel.getTimeMovie());
                bundle.putString("videoUrl", moviesModel.getVideoMovie());
                bundle.putString("image", moviesModel.getImageMovie());
                detailsFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.fragment_container, detailsFragment).addToBackStack(null).commit();
            }
        });

        closeButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    private static boolean appInstallOrNot(String url, Context context) {
        PackageManager packageManager = context.getPackageManager();
        boolean app_installed;
        try{
            packageManager.getPackageInfo(url, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }
        catch (PackageManager.NameNotFoundException e){
            app_installed = false;
        }
        return app_installed;
    }


    public static void showVideoMovie(Context context, String videoMovie) {
        Intent intent = new Intent(context, VideoPlayerOnlineActivity.class);
        intent.putExtra("videoUrl", videoMovie);
        context.startActivity(intent);
    }

    public static void shareMovieWithImage(Context context, ImageView ImageVideoPrincipal, String idMovie, String titleMovie) {
        Dexter.withContext(context)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).withListener(new MultiplePermissionsListener(){
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        ImageVideoPrincipal.buildDrawingCache();
                        Bitmap bitmap = ImageVideoPrincipal.getDrawingCache();

                        try{
                            File file = new File(context.getApplicationContext().getExternalCacheDir(), File.separator + "movie_" + idMovie + ".jpg");
                            FileOutputStream fOut = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                            fOut.flush();
                            fOut.close();
                            file.setReadable(true, false);
                            final Intent intent = new Intent(Intent.ACTION_SEND);
                            Uri photoUri = FileProvider.getUriForFile(context.getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", file);

                            //IMAGE
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra(Intent.EXTRA_STREAM, photoUri);
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            intent.setType("image/jpg");

                            //TEXT
                            intent.putExtra(Intent.EXTRA_TEXT, "Mira *" + titleMovie + "*\nDescarga *Meusflis* aqui\n\n" + "https://play.google.com/store/apps/details?id=" + context.getPackageName());
                            intent.setType("text/plain");

                            Intent chooser = intent.createChooser(intent, "Compartir Pelicula");

                            List<ResolveInfo> resolveInfos = context.getPackageManager().queryIntentActivities(chooser, PackageManager.MATCH_DEFAULT_ONLY);

                            for (ResolveInfo resolveInfo: resolveInfos){
                                String packageName = resolveInfo.activityInfo.packageName;
                                context.grantUriPermission(packageName, photoUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            }

                            context.startActivity(chooser);
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        Toast.makeText(context, "Debes habilitar los permisos para poder compartir ", Toast.LENGTH_SHORT).show();
                    }
                }).check();
    }

    public static void showBottomDetailsMovieWithID(Context context, String idMovie) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        FragmentManager fragmentManager = ((MainActivity)context).getSupportFragmentManager();
        Fragment detailsFragment = new MovieDetailsFragment();

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetDialogTheme);

        View bottomSheetView = LayoutInflater.from(context).inflate(R.layout.layout_bottom_sheet_info_movie, null);

        TextView tvTitle, tvSinopsis, tvTime;
        ImageView imgMovie, closeButtom;
        LinearLayout btnShowVideo, btnDownload, btnNoLink, btnShare;
        RelativeLayout containerMoreDetails;

        tvTitle = bottomSheetView.findViewById(R.id.tv_info_title_movie);
        tvSinopsis = bottomSheetView.findViewById(R.id.tv_info_sinopsis_movie);
        tvTime = bottomSheetView.findViewById(R.id.tv_info_time_movie);

        imgMovie = bottomSheetView.findViewById(R.id.img_info_movie);
        closeButtom = bottomSheetView.findViewById(R.id.close_info_movie);

        btnShowVideo = bottomSheetView.findViewById(R.id.show_icon);
        btnDownload = bottomSheetView.findViewById(R.id.download_icon);
        btnNoLink = bottomSheetView.findViewById(R.id.enlace_icon);
        btnShare = bottomSheetView.findViewById(R.id.share_icon);

        containerMoreDetails = bottomSheetView.findViewById(R.id.containerMoreDetails);

        reference.child(Common.NODO_MOVIES)
                .child(Common.NODO_GENERAL)
                .child(idMovie)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                    String id, img, title, time, sinopsis, video;

                                    id = String.valueOf(snapshot.child("idMovie").getValue());
                                    img = String.valueOf(snapshot.child("imageMovie").getValue());
                                    title = String.valueOf(snapshot.child("titleMovie").getValue());
                                    time = String.valueOf(snapshot.child("timeMovie").getValue());
                                    sinopsis = String.valueOf(snapshot.child("sinopsisMovie").getValue());
                                    video = String.valueOf(snapshot.child("videoMovie").getValue());

                                    tvTitle.setText(title);
                                    tvTime.setText(time);
                                    tvSinopsis.setText(sinopsis);

                                    try{
                                        Picasso.get().load(img).resize(720, 720).onlyScaleDown().into(imgMovie);
                                    }
                                    catch (Exception e){
                                        Picasso.get().load(R.drawable.baseline_image_not_supported_24).into(imgMovie);
                                    }

                                    btnNoLink.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            boolean installed = appInstallOrNot("com.whatsapp", context);

                                            if (installed){
                                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                                intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + Common.MOBILE_NUMBER + "&text=El enlace de la pelicula *" + title + "* no funciona."));
                                                context.startActivity(intent);
                                            }
                                            else{
                                                Toast.makeText(context, "WhatSapp no esta instalado", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                    btnShare.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            shareMovieWithImage(context, imgMovie, id, title);
                                        }
                                    });

                                    containerMoreDetails.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            bottomSheetDialog.dismiss();

                                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                                            Bundle bundle = new Bundle();
                                            bundle.putString("title", title);
                                            bundle.putString("sinopsis", sinopsis);
                                            bundle.putString("time", time);
                                            bundle.putString("videoUrl", video);
                                            bundle.putString("image", img);
                                            detailsFragment.setArguments(bundle);
                                            fragmentTransaction.replace(R.id.fragment_container, detailsFragment).addToBackStack(null).commit();
                                        }
                                    });

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

        closeButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }
}
