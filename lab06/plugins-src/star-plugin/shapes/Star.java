package shapes;

/**
 * Five-pointed star defined by center and outer radius (plugin-provided shape).
 */
public class Star extends Shape {
    private static final int SPIKES = 5;
    private static final double INNER_RATIO = 0.4;

    private int centerX;
    private int centerY;
    private int outerRadius;

    public Star(int centerX, int centerY, int outerRadius) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.outerRadius = outerRadius;
    }

    public int getCenterX() { return centerX; }
    public int getCenterY() { return centerY; }
    public int getOuterRadius() { return outerRadius; }

    public void setCenterX(int centerX) { this.centerX = centerX; }
    public void setCenterY(int centerY) { this.centerY = centerY; }
    public void setOuterRadius(int outerRadius) { this.outerRadius = outerRadius; }

    /**
     * Computes polygon vertices for drawing (outer and inner points alternate).
     */
    public int[] getXPoints() {
        int[] points = new int[SPIKES * 2];
        double inner = outerRadius * INNER_RATIO;
        for (int i = 0; i < SPIKES * 2; i++) {
            double angle = Math.PI / 2.0 + i * Math.PI / SPIKES;
            double radius = (i % 2 == 0) ? outerRadius : inner;
            points[i] = centerX + (int) Math.round(radius * Math.cos(angle));
        }
        return points;
    }

    public int[] getYPoints() {
        int[] points = new int[SPIKES * 2];
        double inner = outerRadius * INNER_RATIO;
        for (int i = 0; i < SPIKES * 2; i++) {
            double angle = Math.PI / 2.0 + i * Math.PI / SPIKES;
            double radius = (i % 2 == 0) ? outerRadius : inner;
            points[i] = centerY - (int) Math.round(radius * Math.sin(angle));
        }
        return points;
    }

    @Override
    public String getDisplayName() {
        return String.format("Star center=(%d,%d) R=%d", centerX, centerY, outerRadius);
    }
}
