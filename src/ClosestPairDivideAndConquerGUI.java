import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ClosestPairDivideAndConquerGUI extends JFrame {
    private final List<Point> points = new ArrayList<>();
    private Point closestPoint1 = null, closestPoint2 = null;
    private JLabel distanceLabel;  // Label to display the distance

    public ClosestPairDivideAndConquerGUI() {
        setTitle("Closest Pair of Points (Divide and Conquer)");
        setSize(800, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        JButton generateButton = new JButton("Generate Points");
        JButton findClosestButton = new JButton("Find Closest Pair");

        generateButton.addActionListener(e -> generatePoints());
        findClosestButton.addActionListener(e -> findClosestPair());

        panel.add(generateButton);
        panel.add(findClosestButton);

        // Initialize the distance label and add it to the panel
        distanceLabel = new JLabel("Distance: ");
        panel.add(distanceLabel);

        add(panel, BorderLayout.SOUTH);
    }

    private void generatePoints() {
        points.clear();
        Random rand = new Random();
        for (int i = 0; i < 15; i++) {
            points.add(new Point(rand.nextInt(750), rand.nextInt(500)));
        }
        closestPoint1 = closestPoint2 = null;
        repaint();
    }

    private void findClosestPair() {
        if (points.size() < 2) return;

        Point[] pointsX = points.toArray(new Point[0]);
        Point[] pointsY = points.toArray(new Point[0]);

        // Sort points by X and Y coordinates
        mergeSort(pointsX, pointsX.length, true); // Sort by X
        mergeSort(pointsY, pointsY.length, false); // Sort by Y

        // Find the closest pair using divide and conquer
        double minDistance = closestPair(pointsX, pointsY);

        // Find the actual points forming the closest pair
        closestPoint1 = null;
        closestPoint2 = null;
        for (Point p1 : points) {
            for (Point p2 : points) {
                if (p1 != p2 && dist(p1, p2) == minDistance) {
                    closestPoint1 = p1;
                    closestPoint2 = p2;
                    break;
                }
            }
            if (closestPoint1 != null) break;
        }

        // Update the distance label with the result
        distanceLabel.setText("Distance: " + String.format("%.2f", minDistance));

        repaint();
    }

    private double dist(Point p1, Point p2) {
        return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
    }

    private void mergeSort(Point[] points, int n, boolean sortByX) {
        if (n < 2) {
            return;
        }
        int mid = n / 2;
        Point[] left = new Point[mid];
        Point[] right = new Point[n - mid];

        // Splitting the array into two halves
        for (int i = 0; i < mid; i++) {
            left[i] = points[i];
        }
        for (int i = mid; i < n; i++) {
            right[i - mid] = points[i];
        }

        // Recursively sorting both halves
        mergeSort(left, mid, sortByX);
        mergeSort(right, n - mid, sortByX);

        // Merging the sorted halves
        merge(points, left, right, mid, n - mid, sortByX);
    }

    private void merge(Point[] points, Point[] left, Point[] right, int leftSize, int rightSize, boolean sortByX) {
        int i = 0, j = 0, k = 0;

        while (i < leftSize && j < rightSize) {
            if (sortByX) {
                // Sort by x coordinate
                if (left[i].x < right[j].x || (left[i].x == right[j].x && left[i].y <= right[j].y)) {
                    points[k++] = left[i++];
                } else {
                    points[k++] = right[j++];
                }
            } else {
                // Sort by y coordinate
                if (left[i].y < right[j].y || (left[i].y == right[j].y && left[i].x <= right[j].x)) {
                    points[k++] = left[i++];
                } else {
                    points[k++] = right[j++];
                }
            }
        }

        // Copy remaining elements
        while (i < leftSize) {
            points[k++] = left[i++];
        }
        while (j < rightSize) {
            points[k++] = right[j++];
        }
    }

    private double closestPair(Point[] pointsX, Point[] pointsY) {
        int n = pointsX.length;

        // Base cases
        if (n == 2) {
            return dist(pointsX[0], pointsX[1]);
        }
        if (n == 3) {
            return Math.min(Math.min(dist(pointsX[0], pointsX[1]), dist(pointsX[0], pointsX[2])), dist(pointsX[1], pointsX[2]));
        }

        // Divide
        int mid = n / 2;
        Point midPoint = pointsX[mid];

        // Recursively find the closest pair in the left and right halves
        double dl = closestPair(Arrays.copyOfRange(pointsX, 0, mid), Arrays.copyOfRange(pointsY, 0, mid));
        double dr = closestPair(Arrays.copyOfRange(pointsX, mid, n), Arrays.copyOfRange(pointsY, mid, n));
        double d = Math.min(dl, dr);

        // Conquer step: find the closest pair that crosses the middle line
        List<Point> strip = new ArrayList<>();
        for (Point p : pointsY) {
            if (Math.abs(p.x - midPoint.x) < d) {
                strip.add(p);
            }
        }

        for (int i = 0; i < strip.size(); i++) {
            for (int j = i + 1; j < strip.size() && strip.get(j).y - strip.get(i).y < d; j++) {
                d = Math.min(d, dist(strip.get(i), strip.get(j)));
            }
        }

        return d;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw grid
        g2d.setColor(Color.LIGHT_GRAY);
        for (int i = 50; i <= 750; i += 50) { // Vertical grid lines
            g2d.drawLine(i, 50, i, 550);
        }
        for (int i = 50; i <= 550; i += 50) { // Horizontal grid lines
            g2d.drawLine(50, i, 750, i);
        }

        // Draw axes
        g2d.setColor(Color.BLUE);
        g2d.drawLine(50, 550, 750, 550); // X-axis
        g2d.drawLine(50, 50, 50, 550); // Y-axis

        // Add numbers to axes
        g2d.setColor(Color.BLACK);
        // X-axis labels
        for (int i = 50; i <= 750; i += 50) {
            if (i > 50) {
                g2d.drawString(String.valueOf(i - 50), i - 10, 570); // Offset to align numbers with ticks
            }
        }
        // Y-axis labels
        for (int i = 550; i >= 50; i -= 50) {
            if (i < 550) {
                g2d.drawString(String.valueOf(550 - i), 25, i + 5); // Offset for proper alignment
            }
        }

        // Draw points
        for (Point p : points) {
            // Ensure points are within the range of the grid
            int adjustedX = Math.min(750, Math.max(50, p.x + 50)); // Scale and translate to fit grid
            int adjustedY = Math.min(550, Math.max(50, 550 - p.y)); // Scale and invert Y-axis for drawing

            // Check if the point is one of the closest pair
            if ((closestPoint1 != null && closestPoint1.equals(p)) ||
                    (closestPoint2 != null && closestPoint2.equals(p))) {
                g2d.setColor(Color.RED); // Highlight the closest pair
                g2d.fillOval(adjustedX - 6, adjustedY - 6, 12, 12); // Make the point larger
            } else {
                g2d.setColor(Color.BLACK);
                g2d.fillOval(adjustedX - 4, adjustedY - 4, 8, 8); // Regular points
            }
        }

        // Draw line connecting the closest pair
        if (closestPoint1 != null && closestPoint2 != null) {
            g2d.setColor(Color.RED);
            int x1 = Math.min(750, Math.max(50, closestPoint1.x + 50));
            int y1 = Math.min(550, Math.max(50, 550 - closestPoint1.y));
            int x2 = Math.min(750, Math.max(50, closestPoint2.x + 50));
            int y2 = Math.min(550, Math.max(50, 550 - closestPoint2.y));

            g2d.drawLine(x1, y1, x2, y2); // Draw the line in red
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ClosestPairDivideAndConquerGUI gui = new ClosestPairDivideAndConquerGUI();
            gui.setVisible(true);
        });
    }
}
