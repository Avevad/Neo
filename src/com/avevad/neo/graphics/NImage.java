package com.avevad.neo.graphics;

public abstract class NImage {
    public final int w, h;

    public NImage(int w, int h) {
        this.w = w;
        this.h = h;
    }

    public abstract int getPixel(int x, int y);

    public NDimension getSize() {
        return new NDimension(w, h);
    }


    public interface NImageIO<I extends NImage> {
        I loadImage(InputStream from, String format);

        void saveImage(I image, OutputStream to, String format);
    }
}
