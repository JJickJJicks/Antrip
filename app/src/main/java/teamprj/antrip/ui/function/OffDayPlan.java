package teamprj.antrip.ui.function;

import android.os.Parcel;
import android.os.Parcelable;

public class OffDayPlan implements Parcelable {
    public final String name;

    public OffDayPlan(String name){
        this.name = name;
    }


    protected OffDayPlan(Parcel in) {
        name = in.readString();
    }

    public static final Creator<OffDayPlan> CREATOR = new Creator<OffDayPlan>() {
        @Override
        public OffDayPlan createFromParcel(Parcel in) {
            return new OffDayPlan(in);
        }

        @Override
        public OffDayPlan[] newArray(int size) {
            return new OffDayPlan[size];
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
