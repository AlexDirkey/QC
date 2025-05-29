package util;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

import java.awt.image.BufferedImage;

public class OpenCVHelper {

    private static OpenCVHelper instance;
    private final VideoCapture camera;

    private OpenCVHelper() {
        camera = new VideoCapture(0);
    }

    public static OpenCVHelper getInstance() {
        if (instance == null) {
            instance = new OpenCVHelper();
        }
        return instance;
    }

    public boolean isCameraOpen() {
        return camera.isOpened();
    }


    public Mat captureFrame() {
        Mat frame = new Mat();
        if (camera.isOpened()) {
            camera.read(frame);
        }
        return frame;
    }

    public Image matToImage(Mat frame) {
        if (frame.empty()) return null;

        int width = frame.width(), height = frame.height(), channels = frame.channels();
        byte[] sourcePixels = new byte[width * height * channels];
        frame.get(0, 0, sourcePixels);

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        image.getRaster().setDataElements(0, 0, width, height, sourcePixels);

        return SwingFXUtils.toFXImage(image, null);
    }

    public void releaseCamera() {
        if (camera.isOpened()) {
            camera.release();
        }
    }

    public boolean saveFrameToFile(String path) {
        Mat frame = captureFrame();
        return Imgcodecs.imwrite(path, frame);
    }
}

