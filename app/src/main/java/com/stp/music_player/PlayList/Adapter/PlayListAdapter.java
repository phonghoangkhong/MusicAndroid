package com.stp.music_player.PlayList.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.stp.music_player.R;
import com.stp.music_player.database.AppDatabase;
import com.stp.music_player.database.Playlist;

import java.util.List;

public class PlayListAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<Playlist> list_play_list;
    public PlayListAdapter(Context context, int layout, List<Playlist> list_play_list){
        this.context = context;
        this.layout = layout;
        this.list_play_list = list_play_list;
    }
    // trả về số dòng trong list tương ứng với số dòng trong adapter
    public  int getCount(){return list_play_list.size();}
    public Object getItem(int position){
        return list_play_list.get(position);
    }

    @Override
    public long getItemId(int position ) {
        return list_play_list.get(position).getPlaylistId();
    }
    private class ViewHolder{
        ImageView imageView,imgbutton;
        TextView txtview;

    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout,null);
            holder = new ViewHolder();
            //anh xa
            holder.txtview = view.findViewById(R.id.txtviewName);
            holder.imgbutton = view.findViewById(R.id.img_btn_opt);
            view.setTag(holder);
        }
        else{
            holder = (ViewHolder) view.getTag();
        }
        final Playlist play_list = list_play_list.get(i);
        holder.txtview.setText(play_list.getPlaylistName());
        holder.imgbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                String[] opt = {"Xóa"};
                builder.setItems(opt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                deletePlayList(play_list.getPlaylistId());
                                break;
                        }
                    }
                });

                AlertDialog optDialog = builder.create();
                optDialog.show();

            }
        });
        return view;
    }
    public void deletePlayList(int id_playlist){
        if(AppDatabase.getInstance(context).deletePlayList(id_playlist)){
            list_play_list.remove(id_playlist -1 );
            this.notifyDataSetChanged();
            Toast.makeText(context, "Xóa PlayList thành công", Toast.LENGTH_SHORT).show();
        }
    }



}
