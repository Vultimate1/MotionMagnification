package com.spy.motionmagnificationsinglescreen;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.io.File;

public class RecyclerViewHolder extends RecyclerView.Adapter<RecyclerViewHolder.ViewHolder> {
    Context mContext;
    File[] FilesAndFolders;
    File file_to_be_magnified;
    boolean only_videos_shown;
    public RecyclerViewHolder(Context context, File[] filesAndFolders) {
        this.mContext = context;
        this.FilesAndFolders = filesAndFolders;
//        this.file_to_be_magnified = selected_file;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_item,parent, false);
        //Toast.makeText(mContext.getApplicationContext(),"only showing videos? "+only_videos_shown, Toast.LENGTH_SHORT);
        /*if (only_videos_shown==false) {
            Toast.makeText(mContext.getApplicationContext(),"only show videos? false", Toast.LENGTH_SHORT);
        }   else    {
            Toast.makeText(mContext.getApplicationContext(),"only show videos? true", Toast.LENGTH_SHORT);
        }*/
        return new ViewHolder(view);

    }
    public boolean check (RecyclerViewHolder.ViewHolder holder, int position) {
        holder.check_mark.setVisibility(View.VISIBLE);
        holder.textView.setBackgroundColor(Color.GREEN);

        return true;
    }
    @Override
    public void onBindViewHolder( RecyclerViewHolder.ViewHolder holder, int position) {

        File selectedFile = FilesAndFolders[position];
        //holder.textView.title.setText(Constant.allMediaList.get(position).getName());
        //if (only_videos_shown==true) {
        String filename = selectedFile.getAbsolutePath();
        String filenamearray[] = filename.split("\\.");
        String type = filenamearray[filenamearray.length-1];
        //Toast.makeText(mContext.getApplicationContext(), type, Toast.LENGTH_SHORT).show();
            /*if (type.equalsIgnoreCase("mp4")==true || type.equalsIgnoreCase("avi")==true || selectedFile.isDirectory())
            {
                holder.textView.setText(selectedFile.getName());
                //holder.itemView.setVisibility(View.VISIBLE);
            } else {
                holder.itemView.setVisibility(View.GONE);
            }*/
        //}
        holder.textView.setText(selectedFile.getName());
        if (selectedFile.isDirectory()) {
            holder.imageView.setImageResource(R.drawable.ic_baseline_folder_24);
        } else {
            holder.imageView.setImageResource(R.drawable.ic_baseline_insert_drive_file_24);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedFile.isDirectory()) {
                    Intent intent = new Intent(mContext, FileListActivity.class);
                    final String path = selectedFile.getAbsolutePath();
                    intent.putExtra("path", path);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                } else {
                    try {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        String type="image/*";
                        if ((selectedFile.getAbsolutePath().endsWith("jpg") || selectedFile.getAbsolutePath().endsWith("png"))) {
                            type = "image/*";
                        }
                        if ((selectedFile.getAbsolutePath().endsWith("mp4"))) {
                            type = "video/mp4";
                        }
                        if ((selectedFile.getAbsolutePath().endsWith("avi"))) {
                            type = "video/avi";
                        }
                        if (selectedFile.getAbsolutePath().endsWith("wav")) {
                            type = "audio/";
                        }
                        intent.setDataAndType(Uri.parse(selectedFile.getAbsolutePath()), type);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);

                    }
                    catch(Exception e) {
                        //Toast.makeText(mContext.getApplicationContext(),"cannot open the file", Toast.LENGTH_SHORT);
                    }
                }
            }


        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {

            public boolean onLongClick(View v) {
                File selectedFile = FilesAndFolders[position];
                if (selectedFile.isDirectory()) {
                    //Toast.makeText(mContext, "is directory", Toast.LENGTH_SHORT).show();
                    PopupMenu popupMenu = new PopupMenu(mContext, v);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {

                            popupMenu.show();
                            return true;
                        }
                    });
                }
                PopupMenu popupMenu = new PopupMenu(mContext, v);
                popupMenu.getMenu().add("DELETE");
                popupMenu.getMenu().add("RENAME");
                popupMenu.getMenu().add("SELECT");
                /*if (selectedFile.isDirectory()) {
                    popupMenu.getMenu().add("ONLY SHOW VIDEOS");
                }*/
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    if (menuItem.getTitle().equals("DELETE")) {
                        boolean deleted = selectedFile.delete();
                        if (deleted) {
                            Toast.makeText(mContext.getApplicationContext(), "DELETED ", Toast.LENGTH_SHORT).show();
                            v.setVisibility(View.GONE);
                        }
                    }
                    if (menuItem.getTitle().equals("RENAME")) {
                        Toast.makeText(mContext.getApplicationContext(), "RENAMED ", Toast.LENGTH_SHORT).show();
                    }
                    if (menuItem.getTitle().equals("SELECT")) {
                        Toast.makeText(mContext.getApplicationContext(), "SELECTED ", Toast.LENGTH_SHORT).show();
                        boolean selected = check(holder, position);

                        if (selected && (selectedFile.getAbsolutePath().endsWith("mp4") || selectedFile.getAbsolutePath().endsWith("avi"))) {
                        //    Toast.makeText(mContext.getApplicationContext(), "Video chosen, selected "+ selectedFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
                            file_to_be_magnified = selectedFile;
                            //v.setVisibility(View.GONE);
                        } else {
                        //    Toast.makeText(mContext.getApplicationContext(),"Video format incompatible. Acceptable formats include .mp4, .avi, .mp3.", Toast.LENGTH_SHORT);
                        }

                    }
                    /*if (menuItem.getTitle().equals("ONLY SHOW VIDEOS")) {
                        only_videos_shown = true;
                        if (only_videos_shown==false) {
                            Toast.makeText(mContext.getApplicationContext(),"only show videos? false", Toast.LENGTH_SHORT);
                        }   else    {
                            Toast.makeText(mContext.getApplicationContext(),"only show videos? true", Toast.LENGTH_SHORT);

                        }
                    }*/
                    return true;
                 }
                });
                popupMenu.show();
                return true;
            }
        });

    }

    /*public void onBindViewHolder(RecyclerViewHolder.ViewHolder holder, int position) {
        File selectedFile = FilesAndFolders[position];
        //holder.textView.title.setText(Constant.allMediaList.get(position).getName());
        holder.textView.setText(selectedFile.getName());
        if (selectedFile.isDirectory()) {
            holder.imageView.setImageResource(R.drawable.ic_baseline_folder_24);
        } else {
            holder.imageView.setImageResource(R.drawable.ic_baseline_insert_drive_file_24);
        }
        //load thumbnail using glidlibrary
        //Uri uri = Uri.fromFile(Constant.allMediaList.get(position));
        //Glide.with(mContext).load(uri).thumbnail(0.1f).into(((FileLayoutHolder)holder).thumbnail);
    }*/


    @Override
    public int getItemCount() {
        return FilesAndFolders.length;
    }

    public File getFile_to_be_magnified() {return file_to_be_magnified;}
    public boolean isOnly_videos_shown() {return only_videos_shown;}
    /*class FileLayoutHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;
        TextView title;
        ImageButton ic_more_btn;
        public FileLayoutHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            title = itemView.findViewById(R.id.videotitle);
            ic_more_btn = itemView.findViewById(R.id.ic_more_btn);
        }
    }*/

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;
        ImageView check_mark;
        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.file_name_text_view);
            imageView = itemView.findViewById(R.id.icon_View);
            check_mark = itemView.findViewById(R.id.checkmark);
            //android:foreground="@drawable/ic_baseline_blur_linear_24"
        }
    }
}
