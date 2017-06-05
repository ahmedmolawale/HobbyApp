
package ng.org.nacoss.www.hobbyapp.model;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data implements Parcelable
{

    @SerializedName("password")
    @Expose
    private Object password;
    @SerializedName("dateCreated")
    @Expose
    private String dateCreated;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("hobbies")
    @Expose
    private List<Hobby> hobbies = null;
    public final static Creator<Data> CREATOR = new Creator<Data>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Data createFromParcel(Parcel in) {
            Data instance = new Data();
            instance.password = ((Object) in.readValue((Object.class.getClassLoader())));
            instance.dateCreated = ((String) in.readValue((String.class.getClassLoader())));
            instance.username = ((String) in.readValue((String.class.getClassLoader())));
            instance.email = ((String) in.readValue((String.class.getClassLoader())));
            in.readList(instance.hobbies, (Hobby.class.getClassLoader()));
            return instance;
        }

        public Data[] newArray(int size) {
            return (new Data[size]);
        }

    }
    ;

    public Object getPassword() {
        return password;
    }

    public void setPassword(Object password) {
        this.password = password;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Hobby> getHobbies() {
        return hobbies;
    }

    public void setHobbies(List<Hobby> hobbies) {
        this.hobbies = hobbies;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(password);
        dest.writeValue(dateCreated);
        dest.writeValue(username);
        dest.writeValue(email);
        dest.writeList(hobbies);
    }

    public int describeContents() {
        return  0;
    }

}
