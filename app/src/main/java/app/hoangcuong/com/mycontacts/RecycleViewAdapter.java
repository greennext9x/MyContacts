package app.hoangcuong.com.mycontacts;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.akashandroid90.imageletter.MaterialLetterIcon;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;


/**
 * Created by Jarvis on 10/26/2016.
 */

public  class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> {
    private final TypedValue mTypedValue = new TypedValue();
    private  int mBackground;
    private List<Contacts> mValues;
    private  int[] mMaterialColors;
    private ArrayList<Contacts> searchList;
    private static final Random RANDOM = new Random();

    public RecycleViewAdapter(Context context, List<Contacts> items) {
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        mMaterialColors = context.getResources().getIntArray(R.array.colors);
        mBackground = mTypedValue.resourceId;
        mValues = items;
        searchList = new ArrayList<>();
        searchList.addAll(mValues);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_contacts, parent, false);
        view.setBackgroundResource(mBackground);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
       if(mValues.get(position).getImage() != null){
           Bitmap bmp = BitmapFactory.decodeByteArray(mValues.get(position).getImage(), 0, mValues.get(position).getImage().length);
           holder.mIcon.setShapeType(MaterialLetterIcon.SHAPE_RECT);
           holder.mIcon.setOval(true);
           holder.mIcon.setImageBitmap(bmp);
       }else {
           holder.mIcon.setShapeType(MaterialLetterIcon.SHAPE_RECT);
           holder.mIcon.setShapeColor(mMaterialColors[RANDOM.nextInt(mMaterialColors.length)]);
           holder.mIcon.setLetter(String.valueOf(mValues.get(position).getName()));
           holder.mIcon.setInitials(true);
           holder.mIcon.setInitialsNumber(2);
           holder.mIcon.setLetterSize(28);
       }
        holder.mBoundString = mValues.get(position).getName();
        holder.mText.setText(mValues.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private final View mView;
        private final MaterialLetterIcon mIcon;
        private final TextView mText;
        private String mBoundString;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIcon = (MaterialLetterIcon) view.findViewById(R.id.icon);
            mText = (TextView) view.findViewById(R.id.text1);
        }

        @Override
        public String toString() {
            return super.toString() + " '"+ mText.getText();
        }
    }
    //Filter Contacts
    public void filter(String charText){
        charText = charText.toLowerCase(Locale.getDefault());
        mValues.clear();
        if(charText.length()==0){
            mValues.addAll(searchList);
        }else {
            for(Contacts s: searchList){
                if (s.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    mValues.add(s);
                }
            }
        }
        notifyDataSetChanged();
    }
}
