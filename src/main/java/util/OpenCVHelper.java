package util;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

import java.awt.image.BufferedImage;
import java.io.File;

// Hjælper-klasse til at arbejde med kamera via OpenCV. Er implementeret som en Singleton, for kun at have én opsætning af gangen.

public class OpenCVHelper {
    private static OpenCVHelper instance;
    private final VideoCapture camera;

    // Statisk, der kører en gang ved første indlæsning af klassen
    // Forsøger at loade OpenCV-biblioteket

    static {
        try {
            // Forsøger at indlæse native-bibliotek via java.library.path

            System.out.println("Trying loadLibrary: " + Core.NATIVE_LIBRARY_NAME);
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
            System.out.println("Loaded via loadLibrary: " + Core.NATIVE_LIBRARY_NAME);
        } catch (UnsatisfiedLinkError e) {

            // Hvis det mislykkedes, forsøger vi at lede i kendte stier i projektet for dll'en

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

                    //Dll'en er fundet, så den loades herfra

                    System.load(dllPath);
                    System.out.println("Loaded DLL from: " + dllPath);
                    loaded = true;
                    break;
                }
            }
            if (!loaded) {

                // Vi kaster en exception, og giver en fejlbesked, hvis dll'en ikke kan findes

                System.err.println("Kunne ikke finde opencv_java490.dll i nogen kendte stier.");
                throw new UnsatisfiedLinkError("Missing DLL: opencv_java490.dll");
            }
        }
    }

    //Konstruktør, der sikrer vi kun laver instancen via getInstance

    private OpenCVHelper() {
        camera = new VideoCapture(0); //Åbner kameraet
    }

    //Singletong adgang - opretter en instans, hvis den ikke allerede findes, ellers genbruger den

    public static OpenCVHelper getInstance() {
        if (instance == null) {
            instance = new OpenCVHelper();
        }
        return instance;
    }

    // Returnerer true, hvis kameraet er åbent og klar

    public boolean isCameraOpen() {
        return camera.isOpened();
    }

    // Fanger et frame fra kameraet som OpenCV-Mat objekt

    public Mat captureFrame() {
        Mat frame = new Mat();
        if (camera.isOpened()) {
            camera.read(frame);
        }
        return frame;
    }

    // Konverterer et OpenCV-mat objekt til et JavaFX element, så det kan vises i UI

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

    // Lukker kameraet igen, når billedet er taget

    public void releaseCamera() {
        if (camera.isOpened()) {
            camera.release();
        }
    }

    //Gemmer et freme/billede direkte til en fil (jpg eller png)

    public boolean saveFrameToFile(String path) {
        Mat frame = captureFrame();
        if (frame == null || frame.empty()) {
            System.err.println("Intet billede at gemme");
            return false;
        }
        return Imgcodecs.imwrite(path, frame);
    }
}


