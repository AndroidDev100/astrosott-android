package com.astro.sott.utils.helpers;

import android.annotation.SuppressLint;
import android.app.Activity;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.astro.sott.R;
import com.astro.sott.adapter.playlist.MultiplePlaylistAdapter;
import com.astro.sott.callBacks.commonCallBacks.PlaylistCallback;
import com.kaltura.client.types.PersonalList;

import java.util.List;

public class CommonPlaylistDialog implements PlaylistCallback {

    private static CommonPlaylistDialog commonPlaylistDialog;
    private int playlistId;
    private String playlistname;

    public static CommonPlaylistDialog getInstance() {

        if (commonPlaylistDialog == null) {
            commonPlaylistDialog = new CommonPlaylistDialog();
        }
        return commonPlaylistDialog;


    }


    @SuppressLint("WrongConstant")
    public void createBottomSheet(Activity context, List<PersonalList> playlist, PlaylistCallback movieDescriptionActivity) {

        BottomSheetDialog dialog;
        RecyclerView recyclerView;
        TextView addPlaylist;
        TextView done;

        View view1 = context.getLayoutInflater().inflate(R.layout.bottom_sheet_playlist, null);
        dialog = new BottomSheetDialog(context);
        dialog.setContentView(view1);
        FrameLayout bottomSheet = (FrameLayout) dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
        BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
        dialog.show();

        PlaylistCallback itemClickListener = movieDescriptionActivity;

        recyclerView = dialog.findViewById(R.id.playlist_recycler);
        addPlaylist = dialog.findViewById(R.id.new_playlist);
        done = dialog.findViewById(R.id.done);
        if (playlist.size() > 0) {
            done.setVisibility(View.VISIBLE);
            recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayout.VERTICAL, false));
            MultiplePlaylistAdapter multiplePlaylistAdapter = new MultiplePlaylistAdapter(playlist, context, this);
            recyclerView.setAdapter(multiplePlaylistAdapter);
        } else {
            done.setVisibility(View.GONE);
        }

        done.setOnClickListener(view -> {
            itemClickListener.onClick(playlistname, playlistId);
            dialog.dismiss();
        });

        addPlaylist.setOnClickListener(view -> {
          /*  FragmentManager fm = ((AppCompatActivity) context).getSupportFragmentManager();
            PlaylistDialogFragment playlistDialogFragment = PlaylistDialogFragment.newInstance(context.getResources().getString(R.string.create_playlist_name_title), "");
            playlistDialogFragment.setEditDialogCallBack((PlaylistDialogFragment.EditDialogListener) context);
            playlistDialogFragment.show(fm, AppLevelConstants.TAG_FRAGMENT_ALERT);
            dialog.dismiss();*/
        });
    }


    @Override
    public void onClick(String name, int id) {
        playlistId = id;
        playlistname = name;
    }
}
