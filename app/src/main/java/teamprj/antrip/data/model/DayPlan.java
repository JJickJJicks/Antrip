package teamprj.antrip.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class DayPlan implements Parcelable {
    public final String name;

    public DayPlan(String name){
        this.name = name;
    }


    protected DayPlan(Parcel in) {
        name = in.readString();
    }

    public static final Creator<DayPlan> CREATOR = new Creator<DayPlan>() {
        @Override
        public DayPlan createFromParcel(Parcel in) {
            return new DayPlan(in);
        }

        @Override
        public DayPlan[] newArray(int size) {
            return new DayPlan[size];
        }
    };

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){
        dest.writeString(name);
    }

}
