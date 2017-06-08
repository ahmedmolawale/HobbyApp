
package ng.org.nacoss.www.hobbyapp.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Hobby implements Parcelable
{

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("hobby")
    @Expose
    private String hobby;
    @SerializedName("dateCreated")
    @Expose
    private String dateCreated;
    public final static Creator<Hobby> CREATOR = new Creator<Hobby>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Hobby createFromParcel(Parcel in) {
            Hobby instance = new Hobby();
            instance.id = ((String) in.readValue((String.class.getClassLoader())));
            instance.hobby = ((String) in.readValue((String.class.getClassLoader())));
            instance.dateCreated = (String) in.readValue((String.class.getClassLoader()));
            return instance;
        }
        public Hobby[] newArray(int size) {
            return (new Hobby[size]);
        }

    }
    ;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(hobby);
        dest.writeValue(dateCreated);
    }

    public int describeContents() {
        return  0;
    }

}
