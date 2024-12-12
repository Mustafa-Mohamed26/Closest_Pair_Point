import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class ClosesPairDivideConquer extends JPanel {

    private final ArrayList<Point> points = new ArrayList<>();
    private Point closestPoint1 = null;
    private Point closestPoint2 = null;
    private static final int OFFSET = 40;
    private static final int TICK_SPACING = 10;
    private static final int MAX_LENGTH = 100;
    private static final int POINT_SIZE = 10;
    private static final int PANEL_WIDTH = 500;
    private static final int PANEL_HEIGHT = 300;

    private List<Point> sortedByX, sortedByY;

    public ClosesPairDivideConquer() {
        setLayout(new BorderLayout());

        // Create a panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER)); // Buttons will be aligned to the center
        buttonPanel.setPreferredSize(new Dimension(PANEL_WIDTH, 25)); // Set height for the button panel

        // Button to redraw the points
        JButton redrawButton = new JButton("Redraw Points");
        redrawButton.addActionListener(e -> {
            generateRandomPoints(32);
            sortedByX = new ArrayList<>(points);
            sortedByY = new ArrayList<>(points);
            sortedByX.sort(Comparator.comparingInt(p -> p.x));
            sortedByY.sort(Comparator.comparingInt(p -> p.y));
            closestPoint1 = closestPoint2 = null;
            repaint(); // Redraw the panel to reset everything
        });
        buttonPanel.add(redrawButton);

        // Button to start the algorithm
        JButton startAlgorithmButton = new JButton("Start Algorithm");
        startAlgorithmButton.addActionListener(e -> {
            if (closestPoint1 == null && closestPoint2 == null) {
                // Start the algorithm if no points are found yet
                closestPair(sortedByX, sortedByY);
                repaint(); // Redraw the panel after the algorithm is complete

                // Display the distance between the closest pair in a dialog
                if (closestPoint1 != null && closestPoint2 != null) {
                    double distance = closestPoint1.distance(closestPoint2);
                    JOptionPane.showMessageDialog(this, "The distance between the closest points is: " + distance,
                            "Closest Pair Distance", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        buttonPanel.add(startAlgorithmButton);

        // Add the button panel to the top
        add(buttonPanel, BorderLayout.NORTH);

        // Generate points initially
        generateRandomPoints(32);

        sortedByX = new ArrayList<>(points);
        sortedByY = new ArrayList<>(points);
        sortedByX.sort(Comparator.comparingInt(p -> p.x));
        sortedByY.sort(Comparator.comparingInt(p -> p.y));
    }

    private void generateRandomPoints(int count) {
        points.clear();
        Random rand = new Random();
        for (int i = 0; i < count; i++) {
            int x = rand.nextInt(MAX_LENGTH + 1);
            int y = rand.nextInt(MAX_LENGTH + 1);
            points.add(new Point(x, y));
        }
    }

    private void closestPair(List<Point> sortedByX, List<Point> sortedByY) {
        if (sortedByX.size() <= 3) {
            bruteForceClosestPair(sortedByX);
            return;
        }

        // Find the midpoint
        int mid = sortedByX.size() / 2;
        Point midPoint = sortedByX.get(mid);

        // Divide points into left and right halves
        List<Point> leftByX = sortedByX.subList(0, mid);
        List<Point> rightByX = sortedByX.subList(mid, sortedByX.size());

        // Divide points into left and right halves by Y-coordinate
        List<Point> leftByY = new ArrayList<>();
        List<Point> rightByY = new ArrayList<>();
        for (Point p : sortedByY) {
            if (p.x <= midPoint.x) leftByY.add(p);
            else rightByY.add(p);
        }

        // Recursive calls to the left and right sides
        closestPair(leftByX, leftByY);
        closestPair(rightByX, rightByY);

        // Merge step
        mergeAndCheck(sortedByX, sortedByY, midPoint);
    }

    private void bruteForceClosestPair(List<Point> points) {
        double minDistance = Double.MAX_VALUE;
        for (int i = 0; i < points.size(); i++) {
            for (int j = i + 1; j < points.size(); j++) {
                Point p1 = points.get(i);
                Point p2 = points.get(j);
                double distance = p1.distance(p2);
                if (distance < minDistance) {
                    minDistance = distance;
                    closestPoint1 = p1;
                    closestPoint2 = p2;
                }
            }
        }
    }

    private void mergeAndCheck(List<Point> sortedByX, List<Point> sortedByY, Point midPoint) {
        double delta = closestPoint1 != null && closestPoint2 != null
                ? closestPoint1.distance(closestPoint2)
                : Double.MAX_VALUE;

        List<Point> strip = new ArrayList<>();
        for (Point p : sortedByY) {
            if (Math.abs(p.x - midPoint.x) < delta) {
                strip.add(p);
            }
        }

        for (int i = 0; i < strip.size(); i++) {
            for (int j = i + 1; j < strip.size() && strip.get(j).y - strip.get(i).y < delta; j++) {
                Point p1 = strip.get(i);
                Point p2 = strip.get(j);
                double distance = p1.distance(p2);
                if (distance < delta) {
                    delta = distance;
                    closestPoint1 = p1;
                    closestPoint2 = p2;
                }
            }
        }
    }

    private void drawAxes(Graphics2D g2d) {
        g2d.setColor(Color.BLUE);
        int xAxisY = getHeight() - OFFSET;
        g2d.drawLine(OFFSET, xAxisY, getWidth() - OFFSET, xAxisY);
        g2d.drawLine(OFFSET, getHeight() - OFFSET, OFFSET, OFFSET);

        for (int i = 0; i <= MAX_LENGTH; i += TICK_SPACING) {
            int x = OFFSET + (i * (getWidth() - 2 * OFFSET) / MAX_LENGTH);
            g2d.drawLine(x, xAxisY - 5, x, xAxisY + 5);
            g2d.drawString(String.valueOf(i), x - 10, xAxisY + 20);
        }

        for (int i = 0; i <= MAX_LENGTH; i += TICK_SPACING) {
            int y = getHeight() - OFFSET - (i * (getHeight() - 2 * OFFSET) / MAX_LENGTH);
            g2d.drawLine(OFFSET - 5, y, OFFSET + 5, y);
            g2d.drawString(String.valueOf(i), OFFSET - 30, y + 5);
        }

        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        g2d.drawString("x", getWidth() - OFFSET + 10, xAxisY + 5);
        g2d.drawString("y", OFFSET - 30, OFFSET);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw axes
        drawAxes(g2d);

        // Draw points
        g2d.setColor(Color.BLACK);
        for (Point p : points) {
            g2d.fillOval(OFFSET + p.x * (getWidth() - 2 * OFFSET) / MAX_LENGTH - POINT_SIZE / 2,
                    getHeight() - OFFSET - p.y * (getHeight() - 2 * OFFSET) / MAX_LENGTH - POINT_SIZE / 2,
                    POINT_SIZE, POINT_SIZE);
        }

        // Draw closest points if found
        if (closestPoint1 != null && closestPoint2 != null) {
            g2d.setColor(Color.RED);
            g2d.fillOval(OFFSET + closestPoint1.x * (getWidth() - 2 * OFFSET) / MAX_LENGTH - POINT_SIZE / 2,
                    getHeight() - OFFSET - closestPoint1.y * (getHeight() - 2 * OFFSET) / MAX_LENGTH - POINT_SIZE / 2,
                    POINT_SIZE, POINT_SIZE);

            g2d.fillOval(OFFSET + closestPoint2.x * (getWidth() - 2 * OFFSET) / MAX_LENGTH - POINT_SIZE / 2,
                    getHeight() - OFFSET - closestPoint2.y * (getHeight() - 2 * OFFSET) / MAX_LENGTH - POINT_SIZE / 2,
                    POINT_SIZE, POINT_SIZE);

            g2d.setColor(Color.RED);
            g2d.drawLine(OFFSET + closestPoint1.x * (getWidth() - 2 * OFFSET) / MAX_LENGTH,
                    getHeight() - OFFSET - closestPoint1.y * (getHeight() - 2 * OFFSET) / MAX_LENGTH,
                    OFFSET + closestPoint2.x * (getWidth() - 2 * OFFSET) / MAX_LENGTH,
                    getHeight() - OFFSET - closestPoint2.y * (getHeight() - 2 * OFFSET) / MAX_LENGTH);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Closest Pair Divide and Conquer");
        ClosesPairDivideConquer panel = new ClosesPairDivideConquer();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.setSize(PANEL_WIDTH, PANEL_HEIGHT);
        frame.setVisible(true);
    }
}
