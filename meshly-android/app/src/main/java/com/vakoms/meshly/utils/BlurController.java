package com.vakoms.meshly.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.BasePostprocessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.facebook.imagepipeline.request.Postprocessor;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 9/7/15.
 */
public class BlurController  {

    Context mContext;
    float mRadius;
    public BlurController(Context context , float radius) {
        mContext = context;
        mRadius = radius;
    }
    Postprocessor porcessor = new BasePostprocessor() {


        @Override
        public String getName() {
            return "redMeshPostprocessor";
        }

        @Override
        public void process(Bitmap bitmap) {
            final RenderScript rs = RenderScript.create(mContext);
            final Allocation input = Allocation.createFromBitmap(rs, bitmap,
                    Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
            final Allocation output = Allocation.createTyped(rs, input.getType());


            final ScriptIntrinsicBlur script =
                    ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            script.setRadius(mRadius);
            script.setInput(input);
            script.forEach(output);
            output.copyTo(bitmap);

            rs.destroy();
        }



    };

    public void blurImage(Uri uri, SimpleDraweeView draweeView) {
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setPostprocessor(porcessor)
                .build();

        PipelineDraweeController controller = (PipelineDraweeController)
                Fresco.newDraweeControllerBuilder()
                        .setImageRequest(request)
                        .setOldController(draweeView.getController())
                                // other setters as you need
                        .build();

        draweeView.setController(controller);

    }


}

