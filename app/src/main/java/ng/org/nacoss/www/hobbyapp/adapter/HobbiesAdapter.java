package ng.org.nacoss.www.hobbyapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ng.org.nacoss.www.hobbyapp.R;
import ng.org.nacoss.www.hobbyapp.model.Hobby;

/**
 * Created by root on 6/1/17.
 */

public class HobbiesAdapter extends RecyclerView.Adapter<HobbiesAdapter.CustomViewHolder> {

    List<Hobby> hobbies;

    public HobbiesAdapter(List<Hobby> hobbies){

        this.hobbies  = hobbies;
    }


    @Override
    public HobbiesAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        boolean shouldAttachToParentImmediately = false;

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.hobby_item,parent,shouldAttachToParentImmediately);

        CustomViewHolder customViewHolder = new CustomViewHolder(view);
        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(HobbiesAdapter.CustomViewHolder holder, int position) {
                holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return hobbies.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder{

        TextView hobby;
        public CustomViewHolder(View itemView) {
            super(itemView);
            hobby = (TextView) itemView.findViewById(R.id.hobby);
        }

        public void bind(int position){

            hobby.setText(hobbies.get(position).getHobby());

        }

    }
}
