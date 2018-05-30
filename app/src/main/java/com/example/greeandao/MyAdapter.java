package com.example.greeandao;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<User> list;
    OnClickfl onClickfl;
    public MyAdapter(Context context, List<User> list) {
        this.context = context;
        this.list = list;
    }

    public interface OnClickfl{
        void onClickxq(int position);
    }
    public void setOnclickSpflAdpter(OnClickfl onClickfl) {
        this.onClickfl = onClickfl;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=View.inflate(context,R.layout.layout_item,null);
       MyViewHolder myViewHolder= new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        MyViewHolder hodwl= (MyViewHolder) holder;
        hodwl.text.setText(list.get(position).getName());
        hodwl.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickfl.onClickxq(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class  MyViewHolder extends RecyclerView.ViewHolder{
        TextView text;
        LinearLayout ll;
        public MyViewHolder(View itemView) {
            super(itemView);
            text=itemView.findViewById(R.id.text);
            ll=itemView.findViewById(R.id.ll);
        }
    }
}
