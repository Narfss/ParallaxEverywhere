package com.fmsirvent.ParallaxEverywhereSample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

/**
 * Created by narf on 21/11/14.
 */
public class AboutActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parallax_everywhere_about);

        findViewById(R.id.about_background).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView viewById = (TextView) findViewById(R.id.about_info);
        viewById.setMovementMethod(LinkMovementMethod.getInstance());
        viewById.setText(Html.fromHtml(getString(R.string.about_text_2)));
    }

    public static Intent createIntent(Context context) {
        return new Intent(context, AboutActivity.class);
    }
}
