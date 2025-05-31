package util;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Hjælpeklasse til OpenCV-kameraopsætning og konvertering til JavaFX Image.
 */
public class OpenCVHelper {
    private static OpenCVHelper instance;
    private final VideoCapture camera;

    static {
        try {
            // Forsøg at indlæse native-bibliotek via java.library.path
            System.out.println("Trying loadLibrary: " + Core.NATIVE_LIBRARY_NAME);
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
            System.out.println("Loaded via loadLibrary: " + Core.NATIVE_LIBRARY_NAME);
        } catch (UnsatisfiedLinkError e) {
            // Fallback: prøv forskellige kendte steder i projektet
            String projectDir = System.getProperty("user.dir");
            String[] candidatePaths = new String[] {
                    projectDir + File.separator + "lib"  + File.separator + "opencv" + File.separator + "opencv_java490.dll",
                    projectDir + File.separator + "src"  + File.separator + "main" + File.separator + "resources" + File.separator + "lib" + File.separator + "opencv" + File.separator + "opencv_java490.dll",
                    projectDir + File.separator + "target" + File.separator + "classes" + File.separator + "lib"  + File.separator + "opencv" + File.separator + "opencv_java490.dll"
            };
            boolean loaded = false;
            for (String dllPath : candidatePaths) {
                System.out.println("Trying absolute load: " + dllPath);
                File dllFile = new File(dllPath);
                if (dllFile.exists()) {
                    System.load(dllPath);
                    System.out.println("Loaded DLL from: " + dllPath);
                    loaded = true;
                    break;
                }
            }
            if (!loaded) {
                System.err.println("Kunne ikke finde opencv_java490.dll i nogen kendte stier.");
                throw new UnsatisfiedLinkError("Missing DLL: opencv_java490.dll");
            }
        }
    }

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
        if (frame == null || frame.empty()) return null;

        int width = frame.width();
        int height = frame.height();
        int channels = frame.channels();
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
        if (frame == null || frame.empty()) {
            System.err.println("Ingen frame til at gemme (frame var tom).");
            return false;
        }
        return Imgcodecs.imwrite(path, frame);
    }
}


