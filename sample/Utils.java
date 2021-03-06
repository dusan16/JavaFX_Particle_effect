package sample;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import sample.Settings;

public class Utils {

    /**
     * Clamp value between min and max
     * @param value
     * @param min
     * @param max
     * @return
     */
    public static double clamp(double value, double min, double max) {

        if (value < min)
            return min;

        if (value > max)
            return max;

        return value;
    }

    /**
     * Map value of a given range to a target range
     * @param value
     * @param currentRangeStart
     * @param currentRangeStop
     * @param targetRangeStart
     * @param targetRangeStop
     * @return
     */
    public static double map(double value, double currentRangeStart, double currentRangeStop, double targetRangeStart, double targetRangeStop) {
        return targetRangeStart + (targetRangeStop - targetRangeStart) * ((value - currentRangeStart) / (currentRangeStop - currentRangeStart));
    }

    /**
     * Snapshot an image out of a node, consider transparency.
     *
     * @param node
     * @return
     */
    public static Image createImage(Node node) {

        WritableImage wi;

        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);

        int imageWidth = (int) node.getBoundsInLocal().getWidth();
        int imageHeight = (int) node.getBoundsInLocal().getHeight();

        wi = new WritableImage(imageWidth, imageHeight);
        node.snapshot(parameters, wi);

        return wi;

    }

    /**
     * Pre-create images with various gradient colors and sizes.
     *
     * @return
     */
    public static Image[] preCreateImages() {

        int count = (int) Settings.PARTICLE_LIFE_SPAN_MAX;

        Image[] list = new Image[count];

        double radius = Settings.PARTICLE_WIDTH;

        for (int i = 0; i < count; i++) {

            double opacity = (double) i / (double) count;

            // get color depending on lifespan
            Color color;

            double threshold = 0.9;
            double threshold2 = 0.4;
            if (opacity >= threshold) {
                color = Color.BLUEVIOLET.interpolate(Color.WHITE, Utils.map(opacity, threshold, 1, 0, 1));
            } else if (opacity >= threshold2) {
                color = Color.VIOLET.interpolate(Color.BLUEVIOLET, Utils.map(opacity, threshold2, threshold, 0, 1));
            } else {
                color = Color.BLACK.interpolate(Color.VIOLET, Utils.map(opacity, 0, threshold2, 0, 1));
            }

            // create gradient image with given color
            Circle ball = new Circle(radius);

            RadialGradient gradient1 = new RadialGradient(0, 0, 0, 0, radius, false, CycleMethod.NO_CYCLE, new Stop(0, color.deriveColor(1, 1, 1, 1)), new Stop(1, color.deriveColor(1, 1, 1, 0)));

            ball.setFill(gradient1);

            // create image
            list[i] = Utils.createImage(ball);
        }

        return list;
    }
}
