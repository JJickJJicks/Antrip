package teamprj.antrip.data.model;

public class TravelSetData {
    int cnt = 0;

    public boolean checkDataEA(){
        cnt++;
        if(cnt == 5){
            cnt = 0;
            return true;
        }
        return false;
    }

    public int checkdata(){
        return cnt;
    }
}
