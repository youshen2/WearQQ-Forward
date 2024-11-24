package moye.wearqq.forward;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

public class GotoActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = ((Intent) getIntent().getParcelableExtra("intent")).getParcelableExtra("intent");
        if (intent != null) {
            Intent intent1 = new Intent("com.tencent.qq.action.conversation.reply");
            intent1.putExtra("intent", intent);
            sendBroadcast(intent1);
        }

        finish();
    }
}
