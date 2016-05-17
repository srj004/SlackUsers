package slackchallenge.example.com.slackusers;

/**
 * Created by Sahil on 5/14/2016.
 */
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.PersonViewHolder> {


    List<Person> persons;
    Context context;

    public RVAdapter(List<Person> persons, Context context) {
        this.persons = persons;
        this.context = context;
    }

    public static class PersonViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        ImageView personPhoto;
        TextView personName;
        TextView personTitle;
        TextView personUserName;

        //Detailed View
        CardView cv_d;
        ImageView personPhoto_d;
        TextView personName_d;
        TextView personTitle_d;
        TextView personUserName_d;
        TextView personEmail_d;
        TextView personNumber_d;

        public PersonViewHolder(View itemView, final View v_detail) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            personName = (TextView)itemView.findViewById(R.id.person_name);
            personTitle = (TextView)itemView.findViewById(R.id.person_title);
            personUserName = (TextView) itemView.findViewById(R.id.person_userName);
            personPhoto = (ImageView)itemView.findViewById(R.id.person_photo);

            //Detailed
            cv_d = (CardView) v_detail.findViewById(R.id.cv_detail);
            personPhoto_d = (ImageView)v_detail.findViewById(R.id.person_photo_detail);
            personName_d = (TextView)v_detail.findViewById(R.id.person_name_detail);
            personTitle_d = (TextView) v_detail.findViewById(R.id.person_title_detail);
            personUserName_d = (TextView) v_detail.findViewById(R.id.person_userName_detail);
            personEmail_d = (TextView)v_detail.findViewById(R.id.person_email_detail);
            personNumber_d = (TextView) v_detail.findViewById(R.id.person_phone_detail);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {

                    // Show the Detailed User Dialog
                    final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            ((ViewGroup) v_detail.getParent()).removeView(v_detail);
                        }
                    });
                    builder.setView(v_detail);
                    builder.show();
                }
            });
        }
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    /*
    This method is called when the custom ViewHolder needs to be initialized.
    We specify the layout that each item of the RecyclerView should use.
    This is done by inflating the layout using LayoutInflater,
    passing the output to the constructor of the custom ViewHolder.
     */
    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_card_view,
                viewGroup, false);

        View v_detail = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.user_card_detail_view,
                        viewGroup, false);

        PersonViewHolder pvh = new PersonViewHolder(v, v_detail);
        return pvh;
    }

    // Sets the data for the specific card view
    @Override
    public void onBindViewHolder(PersonViewHolder personViewHolder, int i) {

        Person person = persons.get(i);

        personViewHolder.personName.setText(person.name);
        personViewHolder.personTitle.setText(person.title);
        personViewHolder.personUserName.setText("User Name: " + person.userName
                                        + " (" + person.userId + ")");

        Log.e("IMAGE: ", person.photoURL);
        // Set the image
        ImageView imageView = personViewHolder.personPhoto;
        Picasso.with(context)
                .load(person.photoURL)
                .placeholder(R.drawable.img_placeholder_avatar)
                .into(imageView);

        // Detail views update:
        personViewHolder.personName_d.setText(person.name);
        personViewHolder.personEmail_d.setText(person.email);

        ImageView imageView_detail = personViewHolder.personPhoto_d;
        Picasso.with(context)
                .load(person.photoURL)
                .placeholder(R.drawable.img_placeholder_avatar)
                .into(imageView_detail);

        personViewHolder.personTitle_d.setText(person.title);
        personViewHolder.personNumber_d.setText(person.phoneNumber);
        personViewHolder.personUserName_d.setText("User Name: " + person.userName
                                    + " (" + person.userId + ")");

        personViewHolder.cv_d.setBackgroundColor(Color.parseColor("#" + person.colorCode));
        personViewHolder.cv.setBackgroundColor( Color.parseColor("#" + person.colorCode) );

    }

    /*
     This should return the number of items present in the data.
     As our data is in the form of a List, we only need to call
     the size method on the List object:
     */
    @Override
    public int getItemCount() {
        return persons.size();
    }
}