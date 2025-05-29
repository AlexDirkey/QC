package Util;

import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.paint.Color;
import org.opencv.core.Mat;
import org.opencv.core.CvType;
import org.opencv.videoio.VideoCapture;
import org.opencv.imgproc.Imgproc;

import java.nio.ByteBuffer;

public class OpenCVHelper {

    private final VideoCapture camera;

    public OpenCVHelper(int cameraIndex) {
        this.camera = new VideoCapture(cameraIndex);
    }

    public boolean isCameraOpen() {
        return camera.isOpened();
    }

    public Mat captureFrame() {
        Mat frame = new Mat();
        if (camera.read(frame)) {
            return frame;
        }
        return null;
    }

    public Image matToImage(Mat mat) {
        if (mat == null || mat.empty()) return null;

        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2RGB);

        int width = mat.cols();
        int height = mat.rows();
        byte[] buffer = new byte[width * height * (int) mat.elemSize()];
        mat.get(0, 0, buffer);

        WritableImage image = new WritableImage(width, height);
        PixelWriter pw = image.getPixelWriter();
        pw.setPixels(0, 0, width, height, WritablePixelFormat.getByteRgbInstance(), buffer, 0, width * 3);
        return image;
    }

    public void release() {
        if (camera.isOpened()) {
            camera.release();
        }
    }
}
