package teamprj.antrip.ui.function;

import android.view.View;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;

public class LottieAnimation {
    LottieAnimationView animationView;
    String fileName;

    public LottieAnimation(LottieAnimationView animationView, String fileName) {
        this.animationView = animationView;
        this.fileName = fileName;
    }

    public void DoAnimation() {
        animationView.setAnimation(fileName);
        animationView.setRepeatCount(LottieDrawable.INFINITE);
        animationView.setVisibility(View.VISIBLE);
        animationView.playAnimation();
    }

    public void StopAnimation() {
        animationView.pauseAnimation();
        animationView.setVisibility(View.GONE);
    }
}
