package com.example.bestbestiary;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import static com.example.bestbestiary.BaseActivity.getResourseId;

class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<Monster> monsters;
    private OnMonsterClickListener onMonsterClickListener;
    int indexOfList;

    DataAdapter(Context context, List<Monster> monsters, OnMonsterClickListener onMonsterClickListener) {
        this.monsters = monsters;
        this.inflater = LayoutInflater.from(context);
        this.onMonsterClickListener = onMonsterClickListener;
    }
    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataAdapter.ViewHolder holder, final int position) {
        Monster m = monsters.get(position);
        holder.imageView.setImageResource(m.getHeadindex());
        holder.nameView.setText(m.getName());

        if(indexOfList == position){
            holder.layout.setBackgroundColor(Color.parseColor("#FF4081"));
        }else{
            holder.layout.setBackgroundColor(Color.parseColor("#24252E"));
        }
    }

    @Override
    public int getItemCount() {
        return monsters.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageView;
        final TextView nameView;
        final LinearLayout layout;
        ViewHolder(View view){
            super(view);
            imageView = (ImageView)view.findViewById(R.id.listImage);
            nameView = (TextView) view.findViewById(R.id.listName);
            layout = (LinearLayout) view.findViewById(R.id.linLayout);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    indexOfList = getLayoutPosition();
                    Monster m = monsters.get(getLayoutPosition());
                    onMonsterClickListener.onMonsterClick(m);
                    notifyDataSetChanged();
                    notifyItemRemoved(getAdapterPosition());
                }
            });
        }
    }

    public interface OnMonsterClickListener {
        void onMonsterClick(Monster monster);
    }
}