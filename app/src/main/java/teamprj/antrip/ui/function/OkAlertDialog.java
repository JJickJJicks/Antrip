package teamprj.antrip.ui.function;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.ContextThemeWrapper;

import teamprj.antrip.R;

class OkAlertDialog {
     static void viewOkAlertDialog(Context activity, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(activity, R.style.AppTheme));

        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    public void onClick (DialogInterface dialog,int which){

                    }
                });
        builder.show();
    }

    static void viewOkAlertDialogFinish(final Context activity, String title, String message, final boolean isFinish) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    public void onClick (DialogInterface dialog,int which){
                        if (isFinish) {
                            ((TravelPlanActivity) activity).finish();
                        }
                    }
                });
        builder.show();
    }
}
